package com.bosspvp.api.database.message.impl;

import com.google.gson.Gson;
import org.redisson.api.RTopic;
import org.redisson.api.RedissonClient;

import java.lang.reflect.Type;
import java.util.function.BiConsumer;

public class RedisMessageSystem<T> {

    private final Gson gson;
    private final RedissonClient redissonClient;
    private final Type typeToken;

    public RedisMessageSystem(Gson gson, RedissonClient redissonClient, Type typeToken) {
        this.gson = gson;
        this.redissonClient = redissonClient;
        this.typeToken = typeToken;
    }

    public void subscribe(String channel, BiConsumer<String, T> messageHandler) {
        new Thread(() -> {
            RTopic rTopic = redissonClient.getTopic(channel);
            rTopic.addListener(String.class, (channel1, message) -> {
                T parsedMessage = gson.fromJson(message, typeToken);
                messageHandler.accept(String.valueOf(channel1), parsedMessage);
            });
        }).start();
    }

    public void publish(String channel, T message) {
        RTopic rTopic = redissonClient.getTopic(channel);
        String jsonMessage = gson.toJson(message, typeToken);
        rTopic.publish(jsonMessage);
    }
}
