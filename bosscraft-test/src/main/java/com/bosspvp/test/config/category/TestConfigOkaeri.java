package com.bosspvp.test.config.category;

import com.bosspvp.api.config.impl.BossConfigOkaeri;
import com.bosspvp.api.registry.Registrable;
import com.bosspvp.api.utils.StringUtils;
import eu.okaeri.configs.annotation.CustomKey;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class TestConfigOkaeri extends BossConfigOkaeri implements Registrable {
    //tested this annotation
    @CustomKey(value = "msg")
    private String message;

    @Override
    public @NotNull String getId() {
        return getBindFile().getFileName().toString().split("\\.")[0];
    }

    @Override
    public void onRegister() {
        for(Player player : Bukkit.getOnlinePlayers()){
            player.sendMessage(StringUtils.format(message));
        }
    }
}