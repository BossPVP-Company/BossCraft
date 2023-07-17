package com.bosspvp.api.database.cache.storage;

import com.bosspvp.api.database.cache.RedisObject;
import com.bosspvp.api.database.cache.RedisObjectData;

import java.util.ArrayList;

public class ListRedisObject<K extends RedisObjectData<V>, V extends RedisObject<K>> extends ArrayList<V> implements RedisObject<ListRedisObjectData<K, V>> {

    @Override
    public ListRedisObjectData<K, V> create() {
        ListRedisObjectData<K, V> listRedisObjectData = new ListRedisObjectData<>();
        listRedisObjectData.addAll(this);
        return listRedisObjectData;
    }

    @Override
    public void update(ListRedisObjectData<K, V> data) {
        this.clear();
        this.addAll(data);
    }

}