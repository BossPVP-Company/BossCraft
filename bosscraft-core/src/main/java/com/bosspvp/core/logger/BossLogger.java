package com.bosspvp.core.logger;

import com.bosspvp.api.BossPlugin;
import com.bosspvp.api.utils.StringUtils;
import org.bukkit.Bukkit;

import java.util.function.Supplier;
import java.util.logging.Logger;

public class BossLogger extends Logger {
    private final BossPlugin plugin;
    public BossLogger(BossPlugin plugin) {
        super(plugin.getName(), null);
        this.setParent(Bukkit.getLogger());
        this.plugin = plugin;
    }

    @Override
    public void info(String msg) {
        Bukkit.getConsoleSender()
                .sendMessage(
                        StringUtils.format(
                                String.format("[%s] %s",plugin.getName(),msg)
                        )
                );
    }

    @Override
    public void info(Supplier<String> msgSupplier) {
        String msg = msgSupplier.get();
        if(msg == null) return;
        Bukkit.getConsoleSender()
                .sendMessage(
                        StringUtils.format(
                                String.format("[%s] %s",plugin.getName(),msg)
                        )
                );
    }
}
