package pers.sample.vx;

import io.vertx.core.json.JsonObject;

public class RandomNumber {
    public RandomNumber() {}
    public RandomNumber(int id, int hit) {
        this.randomNumber = id;
        this.hitCount = hit;
    }

    public int randomNumber;
    public int hitCount;

    public JsonObject toJsonObject() {
        JsonObject obj = new JsonObject();
        obj.put("randomNumber", randomNumber);
        obj.put("hitCount", hitCount);
        return obj;
    }
}
