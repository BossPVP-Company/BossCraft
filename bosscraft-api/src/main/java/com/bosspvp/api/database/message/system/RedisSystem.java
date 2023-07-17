package com.bosspvp.api.database.message.system;

import com.bosspvp.api.database.RedisClient;
import com.bosspvp.api.database.cache.Creatable;
import com.bosspvp.api.database.cache.RedisObject;
import com.bosspvp.api.database.cache.RedisObjectData;
import com.bosspvp.api.database.message.RedisSubscription;
import com.bosspvp.api.database.message.impl.RedisMessageSystem;
import com.google.gson.Gson;
import org.redisson.api.RList;

import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Predicate;

public abstract class RedisSystem<T extends RedisObject<V>, V extends RedisObjectData<T>> {

    private final Gson gson;
    private final RedisClient redisClient;
    private final String redisPrefix;

    private final RedisMessageSystem<V> messageSystem;

    private final RList<V> groundData;

    protected final List<T> data = new LinkedList<>();

    public RedisSystem(Gson gson, RedisClient redisClient, String redisPrefix, Type dataType) {
        this.gson = gson;
        this.redisClient = redisClient;
        this.redisPrefix = redisPrefix;
        this.messageSystem = new RedisMessageSystem<>(gson, redisClient.getClient(), dataType);

        this.groundData = this.redisClient.getClient().getList(this.redisPrefix);

        // Load ground truth data
        loadGroundTruthData();
    }

    private void loadGroundTruthData() {
        List<V> data = fetchDataFromRedis();
        this.data.addAll(data.stream().map(Creatable::create).toList());
    }

    private List<V> fetchDataFromRedis() {
        return this.redisClient.getClient().getList(this.redisPrefix);
    }

    protected void subscribe(RedisSubscription<V> redisSubscription) {
        this.messageSystem.subscribe(this.redisPrefix + "-"  + redisSubscription.action(), redisSubscription::subscribe);
    }

    protected void publish(String action, T data) {
        this.messageSystem.publish(this.redisPrefix + "-" + action, data.create());
    }

    protected void add(T data) {
        this.groundData.add(data.create());
    }

    protected boolean remove(T data) {
        List<V> list = this.groundData
                .stream()
                .filter(groundData -> groundData.equals(data.create()))
                .toList();
        boolean removed = false;
        for (V v : list) {
            removed = this.groundData.remove(v);
        }
        return removed;
    }

    protected void update(T object, Predicate<V> predicate) {
        this.groundData.replaceAll(data -> {
            if (predicate.test(data)) {
                return object.create();
            }
            return data;
        });
    }

    protected abstract String serverId();

    protected RedisClient redisClient() {
        return redisClient;
    }

    protected String redisPrefix() {
        return redisPrefix;
    }

    protected RedisMessageSystem<V> messageSystem() {
        return messageSystem;
    }

    protected RList<V> groundData() {
        return groundData;
    }

    protected List<T> data() {
        return data;
    }

    protected Gson gson() {
        return gson;
    }

}
