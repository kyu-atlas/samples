package pers.sample.vx;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;
import io.vertx.pgclient.PgConnectOptions;
import io.vertx.pgclient.PgPool;
import io.vertx.sqlclient.PoolOptions;

public enum VxDemoConfiguration {
    INSTANCE;
    private static final Logger _logger = LoggerFactory.getLogger(VxDemoConfiguration.class);
    private PgPool pool;

    Future<Void> init(Vertx vertx) {
        return Future.future(promise -> {
            PgConnectOptions connectOptions = new PgConnectOptions()
            .setPort(5432)
            .setHost("127.0.0.1")
            .setDatabase("dev")
            .setUser("dev")
            .setPassword("dev");
            PoolOptions poolOptions = new PoolOptions().setMaxSize(5);
            pool = PgPool.pool(vertx, connectOptions, poolOptions);
            pool.getConnection()
            .onSuccess(conn -> {
                _logger.info(conn.databaseMetadata().fullVersion());
                conn.close();
                promise.complete();
            }).onFailure(promise::fail);
        });
    }

    public static PgPool getPool() {
        return INSTANCE.pool;
    }
}
