package pers.sample.vx;

import java.lang.management.ManagementFactory;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;
import io.vertx.ext.web.Router;

import static java.util.Objects.isNull;

public class VxDemoApplication {
    private static final Logger _logger = LoggerFactory.getLogger(VxDemoApplication.class);
    private final Vertx vertx = Vertx.vertx();

    public static void main(String[] args) {
        VxDemoApplication app = new VxDemoApplication();
        Runtime.getRuntime().addShutdownHook(new Thread(app::onShutdown));
        app.init()
        .compose(v -> app.deployService())
        .compose(v -> app.deployHttpServer())
        .onSuccess(ar -> app.showStartupTime())
        .onFailure(ar -> {
            _logger.error(ar.getCause());
            app.vertx.close();
        });
    }

    private Future<Void> init() {
        return Future.future(promise -> VxDemoConfiguration.INSTANCE.init(vertx)
            .onSuccess(v -> promise.complete())
            .onFailure(promise::fail));
    }

    private Future<Void> deployService() {
        return Future.future(promise -> {
            var options = new DeploymentOptions().setWorker(true).setInstances(1);
            vertx.deployVerticle(RandomNumberVerticle::new, options)
            .onSuccess(ar -> promise.complete())
            .onFailure(promise::fail);
        });
    }

    private Future<Void> deployHttpServer() {
        return Future.future(promise -> {
            Router router = Router.router(vertx);
            router.get("/randomNumber").handler(new RandomNumberHandler());
            vertx.createHttpServer().requestHandler(router).listen(8090)
            .onSuccess(server -> {
                _logger.info("HTTP server started on port " + server.actualPort());
                promise.complete();
            }).onFailure(promise::fail);
        });
    }

    private void showStartupTime() {
        var runtime = ManagementFactory.getRuntimeMXBean();
        long pid = runtime.getPid();
        long uptime = runtime.getUptime();
        _logger.info(String.format("pid:[%s] JVM running for [%s ms]", pid, uptime));
    }

    private void onShutdown() {
        if (isNull(this.vertx)) { return; }
        CountDownLatch countDownLatch = new CountDownLatch(1);
        vertx.close(ar -> countDownLatch.countDown());
        try {
            if (countDownLatch.await(10, TimeUnit.SECONDS)) {
                _logger.info("stop vertx success");
            }
        } catch (InterruptedException e) {
            _logger.error("error on shutdown...", e);
        }
    }

}
