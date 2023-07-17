package com.bosspvp.api.database.cache.storage;

import com.bosspvp.api.database.cache.RedisObject;
import com.bosspvp.api.database.cache.RedisObjectData;

import java.util.ArrayList;

public class ListRedisObjectData<K extends RedisObjectData<V>, V extends RedisObject<K>> extends ArrayList<V> implements RedisObjectData<ListRedisObject<K, V>> {

    @Override
    public ListRedisObject<K, V> create() {
        ListRedisObject<K, V> listRedisObject = new ListRedisObject<>();
        listRedisObject.addAll(this);
        return listRedisObject;
    }

}
