package com.bosspvp.api.exceptions;

import com.bosspvp.api.utils.StringUtils;

public class NotificationException extends Exception{
    private final String message;

    public NotificationException(String msg) {
        super(msg);
        this.message = msg;
    }

    @Override
    public String getMessage() {
        return StringUtils.format(message);
    }
}
