package com.bosspvp.api.database;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.codec.MsgPackJacksonCodec;
import org.redisson.config.Config;

public class RedisClient {
    private final RedissonClient client;

    public RedisClient() {
        //default for tests
        this("127.0.0.1", 6379);
    }

    public RedisClient(String host, int port) {
        Config config = new Config();
        config.setCodec(new MsgPackJacksonCodec());
        config.useSingleServer().setAddress("redis://" + host + ":" + port);
        // use "rediss://" for SSL connection

        this.client = Redisson.create(config);
    }

    public RedissonClient getClient() {
        return client;
    }
}
