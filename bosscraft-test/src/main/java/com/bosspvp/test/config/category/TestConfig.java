package com.bosspvp.test.config.category;

import com.bosspvp.api.config.BossConfig;
import com.bosspvp.api.registry.Registrable;
import com.bosspvp.api.utils.StringUtils;
import eu.okaeri.configs.annotation.CustomKey;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class TestConfig extends BossConfig implements Registrable {
    //tested this annotation
    @CustomKey(value = "msg")
    private String message;

    @Override
    public @NotNull String getID() {
        return getBindFile().getFileName().toString().split("\\.")[0];
    }

    @Override
    public void onRegister() {
        for(Player player : Bukkit.getOnlinePlayers()){
            player.sendMessage(StringUtils.format(message));
        }
    }
}
