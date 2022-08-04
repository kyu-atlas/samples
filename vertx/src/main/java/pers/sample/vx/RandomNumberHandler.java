package pers.sample.vx;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.eventbus.Message;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

public class RandomNumberHandler implements Handler<RoutingContext> {

    @Override
    public void handle(RoutingContext context) {
        context.vertx().eventBus().request("acquire_random_number", "", (AsyncResult<Message<JsonObject>> ar) -> {
            if (ar.succeeded()) {
                HttpServerResponse response = context.response();
                response.putHeader("content-type", "application/json");
                response.end(ar.result().body().toString());
            } else {
                context.fail(ar.cause());
            }
        });
    }
    
}
