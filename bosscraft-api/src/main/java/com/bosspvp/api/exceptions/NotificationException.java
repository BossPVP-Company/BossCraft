package com.bosspvp.api.exceptions;

import com.bosspvp.api.utils.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Predicate;

/**
 * NotificationException
 * <p></p>
 * Useful exception which you can use to simplify
 * condition and null checks in
 * your commands or anywhere else.
 * @see NotificationException#notifyFalse(boolean, String) 
 * @see NotificationException#notifyNull(Object, String) 
 */
public class NotificationException extends Exception{
    private final String message;

    public NotificationException(String msg) {
        super(msg);
        this.message = msg;
    }

    /**
     * Get formatted Notification message
     *
     * @return the message
     */
    @Override
    public String getMessage() {
        return StringUtils.format(message);
    }






    /**
     * Basic notification method, that throws NotificationException
     * @param msg The msg to send
     */
    public static void notify(@NotNull String msg) throws NotificationException {

        throw new NotificationException(msg);
    }
    /**
     * throws NotificationException when obj is null
     * @param obj an object to check
     * @param msg The msg to send
     */
    public static @NotNull <T> T notifyNull(@Nullable T obj,
                                      @NotNull String msg)
            throws NotificationException {

        if (obj==null) notify(msg);
        return obj;
    }
    /**
     * throws NotificationException when predicate#test(obj) returns false
     * @param obj an object to check
     * @param predicate the condition to test on the received object
     * @param msg The msg to send
     */
    public static @NotNull <T> T notifyFalse(@NotNull T obj,
                                             @NotNull Predicate<T> predicate,
                                             @NotNull String msg) throws NotificationException {
        notifyFalse(predicate.test(obj), msg);
        return obj;
    }
    /**
     * throws NotificationException when condition is false
     * @param condition boolean value
     * @param msg The msg to send
     */
    public static boolean notifyFalse(boolean condition,
                                      @NotNull String msg) throws NotificationException {

        if (!condition) notify(msg);

        return true;
    }
}
