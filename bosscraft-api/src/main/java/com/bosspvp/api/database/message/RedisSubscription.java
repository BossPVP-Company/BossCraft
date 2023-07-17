package com.bosspvp.api.database.message;

public interface RedisSubscription<T> extends IRedisSubscription {

    void subscribe(String channelId, T source);

}