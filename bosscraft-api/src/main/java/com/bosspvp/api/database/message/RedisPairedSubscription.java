package com.bosspvp.api.database.message;

public interface RedisPairedSubscription<T> extends IRedisSubscription {

    void subscribe(String channel, ChannelMessage<T> message);

}