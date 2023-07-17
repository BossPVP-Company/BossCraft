package com.bosspvp.api.database.message.impl;

import com.bosspvp.api.database.message.ChannelMessage;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.redisson.api.RTopic;
import org.redisson.api.RedissonClient;

import java.lang.reflect.Type;
import java.util.function.BiConsumer;

public class RedisPairedMessageSystem<V> {

    private final Gson gson;
    private final RedissonClient redissonClient;
    private final Type typeToken;

    public RedisPairedMessageSystem(Gson gson, RedissonClient redissonClient, Type typeToken) {
        this.gson = gson;
        this.redissonClient = redissonClient;
        this.typeToken = typeToken;
    }

    public void subscribe(String channel, BiConsumer<String, ChannelMessage<V>> messageHandler) {
        new Thread(() -> {
            RTopic rTopic = redissonClient.getTopic(channel);
            rTopic.addListener(String.class, (channel1, message) -> {
                // Create a TypeToken with specific type information for the generic parameter V
                Type type = TypeToken.getParameterized(ChannelMessage.class, typeToken).getType();
                ChannelMessage<V> parsedMessage = gson.fromJson(message, type);
                messageHandler.accept(String.valueOf(channel1), parsedMessage);
            });
        }).start();
    }

    public void publish(String channel, String serverId, String key, V message) {
        RTopic rTopic = redissonClient.getTopic(channel);
        ChannelMessage<V> keyValueMessage = new ChannelMessage<>(serverId, key, message);
        Type type = TypeToken.getParameterized(ChannelMessage.class, typeToken).getType();
        String jsonMessage = gson.toJson(keyValueMessage, type);
        rTopic.publish(jsonMessage);
    }

}
