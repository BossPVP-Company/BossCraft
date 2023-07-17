package com.bosspvp.api.database.message.system;

import com.bosspvp.api.database.RedisClient;
import com.bosspvp.api.database.cache.RedisObject;
import com.bosspvp.api.database.cache.RedisObjectData;
import com.bosspvp.api.database.message.RedisPairedSubscription;
import com.bosspvp.api.database.message.impl.RedisPairedMessageSystem;
import com.google.gson.Gson;
import org.redisson.api.RMap;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public abstract class RedisMapSystem<T extends RedisObject<V>, V extends RedisObjectData<T>> {

    private final Gson gson;
    private final RedisClient redisClient;
    private final String redisPrefix;

    private final RedisPairedMessageSystem<V> messageSystem;

    private final RMap<String, V> groundData;

    protected final Map<String, T> data = new HashMap<>();

    public RedisMapSystem(Gson gson, RedisClient redisClient, String redisPrefix, Type dataType) {
        this.gson = gson;
        this.redisClient = redisClient;
        this.redisPrefix = redisPrefix;
        this.messageSystem = new RedisPairedMessageSystem<>(
                gson,
                redisClient.getClient(),
                dataType
        );

        this.groundData = this.redisClient.getClient().getMap(this.redisPrefix);

        // Load ground truth data
        loadGroundTruthData();
    }

    private void loadGroundTruthData() {
        Map<String, V> data = fetchDataFromRedis();
        Map<String, T> collected = data.entrySet().stream()
                .collect(
                        Collectors.toMap(
                                Map.Entry::getKey,
                                e -> e.getValue().create()
                        )
                );
        this.data.putAll(collected);
        onGroundTruthDataLoad(data);
    }

    protected void onGroundTruthDataLoad(Map<String, V> data) {}

    private Map<String, V> fetchDataFromRedis() {
        return this.redisClient.getClient().getMap(this.redisPrefix);
    }

    protected void subscribe(RedisPairedSubscription<V> redisSubscription) {
        this.messageSystem.subscribe(
                this.redisPrefix + "-"  + redisSubscription.action(),
                redisSubscription::subscribe
        );
    }

    protected void publish(String action, T data) {
        V message = data.create();
        this.messageSystem.publish(
                this.redisPrefix + "-" + action,
                this.serverId(),
                generateKey(message),
                message
        );
    }

    protected void add(T data) {
        V objectData = data.create();
        this.groundData.put(generateKey(objectData), objectData);
    }

    protected boolean remove(T data) {
        V objectData = data.create();
        return this.groundData.remove(generateKey(objectData)) != null;
    }

    protected void update(T object) {
        V objectData = object.create();
        this.groundData.put(generateKey(objectData), objectData);
    }

    protected abstract String serverId();

    protected abstract String generateKey(V objectData);

    protected RedisClient redisClient() {
        return redisClient;
    }

    protected String redisPrefix() {
        return redisPrefix;
    }

    protected RedisPairedMessageSystem<V> messageSystem() {
        return messageSystem;
    }

    protected RMap<String, V> groundData() {
        return groundData;
    }

    protected Map<String, T> data() {
        return data;
    }

    protected Gson gson() {
        return gson;
    }

}