package com.bosspvp.api.database.cache;

import java.io.Serializable;

public interface RedisObject<T> extends Creatable<T>, Serializable {
    void update(T data);
}