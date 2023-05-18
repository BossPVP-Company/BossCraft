package com.bosspvp.api.exceptions;


import com.bosspvp.api.BossPlugin;
import com.bosspvp.api.utils.StringUtils;
import lombok.Getter;

public class NotificationException extends Exception{
    private final String message;
    private final boolean langKey;

    public NotificationException(String msg, boolean langKey) {
        super(msg);
        this.message = msg;
        this.langKey=langKey;
    }

    /**
     * Returns formatted message
     *
     * @return the message
     */
    public String getFormattedMessage(BossPlugin plugin){
        if(langKey){

        }
        return StringUtils.format(message);
    }

    /**
     * Returns the original message
     *
     * @return the message
     */
    public String getOriginalMessage(){
        return message;
    }

    /**
     * If message is a lang key
     *
     * @return is lang key
     */
    public boolean isLangKey() {
        return langKey;
    }
}
