package com.bosspvp.api.items.exception;

public class ItemRegisteredException extends Exception {

    public ItemRegisteredException() {
        super("Item with same id already registered!");
    }

}
