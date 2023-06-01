package com.bosspvp.api.config.impl;

import com.bosspvp.api.utils.StringUtils;
import eu.okaeri.configs.OkaeriConfig;

import java.util.Objects;

public class LangSettings extends BossConfigOkaeri {
    private String prefix = "&6BossPlugin ";

    private String errorOnCommand = "&cUnexpected error occurred while trying to execute the command!";
    private String noPermission = "&cYou don't have the required permission to execute this command";
    private String consoleOnly = "&cCommand is for console only";
    private String playersOnly = "&cCommand is for players only";


    public String getPrefix(){
        return StringUtils.format(
                Objects.requireNonNullElse(prefix,
                "&cPrefix not found")
        );
    }

    public String getErrorOnCommand() {
        return StringUtils.format(
                Objects.requireNonNullElse(errorOnCommand,
                        "&cMessage not found")
        );
    }

    public String getNoPermission() {
        return StringUtils.format(
                Objects.requireNonNullElse(noPermission,
                        "&cMessage not found")
        );
    }

    public String getConsoleOnly() {
        return StringUtils.format(
                Objects.requireNonNullElse(consoleOnly,
                        "&cMessage not found")
        );
    }

    public String getPlayersOnly() {
        return StringUtils.format(
                Objects.requireNonNullElse(playersOnly,
                        "&cMessage not found")
        );
    }
}
