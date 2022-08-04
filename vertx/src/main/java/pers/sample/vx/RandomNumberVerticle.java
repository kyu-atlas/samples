package pers.sample.vx;

import org.apache.commons.math3.random.RandomDataGenerator;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.eventbus.Message;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;
import io.vertx.core.json.JsonObject;
import io.vertx.sqlclient.Tuple;

import static pers.sample.vx.VxDemoConfiguration.getPool;


public class RandomNumberVerticle extends AbstractVerticle {
    private static final Logger _logger = LoggerFactory.getLogger(RandomNumberVerticle.class);
    private final RandomDataGenerator _gen = new RandomDataGenerator();
    private final RandomNumber _errData = new RandomNumber(-1, -1);

    private final String objectName = this.toString();
    
    @Override
    public void start() throws Exception {
        super.start();
        vertx.eventBus().consumer("acquire_random_number", this::acquireRandomNumber);
        _logger.info(String.format("Verticle: started. [%s]", deploymentID()));
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        _logger.info(String.format("Verticle: stopped. [%s]", deploymentID()));
    }

    private void acquireRandomNumber(Message<JsonObject> msg) {
        _logger.info(String.format("msg:[%s] enter. thread: %s <-> object: %s", msg.toString(), Thread.currentThread().getName(), objectName));
        int randomInt = _gen.nextInt(1, 10000);
        queryData(randomInt).compose(this::updateData)
        .onSuccess(rn -> msg.reply(rn.toJsonObject()))
        .onFailure(ar -> msg.reply(_errData.toJsonObject()));
        _logger.info(String.format("msg:[%s] leave. thread: %s <-> object: %s", msg.toString(), Thread.currentThread().getName(), objectName));
    }

    private Future<RandomNumber> queryData(int id) {
        String sql = "select * from number_world where id = $1";
        return Future.future(promise -> {
            getPool().preparedQuery(sql).execute(Tuple.of(id))
            .onSuccess(rows -> {
                if (rows.size()  == 1) {
                    rows.forEach(row -> 
                    promise.complete(new RandomNumber(row.getInteger(0), row.getInteger(1))));
                } else {
                    promise.complete(_errData);
                }
            }).onFailure(promise::fail);
        });
    }

    private Future<RandomNumber> updateData(RandomNumber rn) {
        String sql = "update number_world set hit_count = hit_count + 1 where id = $1";
        return Future.future(promise -> getPool().getConnection()
        .onSuccess(conn -> conn.begin()
            .compose(tx -> conn.preparedQuery(sql).execute(Tuple.of(rn.randomNumber))
            .compose(ret -> tx.commit())
            .eventually(v -> conn.close())
            .onSuccess(v -> promise.complete(rn))
            .onFailure(promise::fail))
        ));
    }

}
