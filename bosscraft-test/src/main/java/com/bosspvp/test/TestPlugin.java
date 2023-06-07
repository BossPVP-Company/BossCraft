package com.bosspvp.test;

import com.bosspvp.api.BossAPI;
import com.bosspvp.api.BossPlugin;
import com.bosspvp.api.commands.BossCommand;
import com.bosspvp.api.config.impl.BossConfigOkaeri;
import com.bosspvp.api.config.impl.ConfigSettings;
import com.bosspvp.api.skills.holder.HolderProvider;
import com.bosspvp.api.skills.holder.provided.ProvidedHolder;
import com.bosspvp.core.BossAPIImpl;
import com.bosspvp.test.commands.CommandTest;
import com.bosspvp.test.config.ConfigFileOkaeri;
import com.bosspvp.test.config.category.CategoryTest;
import eu.okaeri.configs.validator.okaeri.OkaeriValidator;
import eu.okaeri.configs.yaml.bukkit.YamlBukkitConfigurer;
import eu.okaeri.configs.yaml.bukkit.serdes.SerdesBukkit;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class TestPlugin extends BossPlugin {
    //instance
    @Getter
    private static TestPlugin instance;
    @Getter
    private CategoryTest categoryTest;

    public TestPlugin() {
        instance = this;
    }

    @Override
    protected void handleLoad() {
        categoryTest = new CategoryTest(this);
        getConfigManager().addConfigCategory(categoryTest);
    }

    @Override
    protected void handleEnable() {
        getGuiController().enableUpdater(true);
        getSkillsManager().getHolderManager().registerHolderProvider(
                new HolderProvider() {
                    @Override
                    public Collection<ProvidedHolder> provide(Player player) {
                        return Collections.singletonList((ProvidedHolder) categoryTest.getSkillsHolder(player));
                    }
                }
        );

    }

    @Override
    protected void handleDisable() {

    }


    //make your own class for config.yml or lang.yml
    @Override
    protected BossConfigOkaeri createConfig() {
        return getConfigManager().addConfig("config",
                ConfigFileOkaeri.class,
                (it) -> {
                    it.withConfigurer(new OkaeriValidator(new YamlBukkitConfigurer(), true), new SerdesBukkit());
                    it.withBindFile(new File(getDataFolder(), "config.yml"));
                    it.saveDefaults();
                    it.load(true);
                });
    }

    @Override
    public @NotNull ConfigSettings getConfigSettings() {
        //obtain ConfigSettings variable from your config class
        return ((ConfigFileOkaeri) getConfigManager().getConfig("config")).getSettings();
    }


    //commands
    @Override
    protected List<BossCommand> loadPluginCommands() {
        return List.of(new CommandTest(this));
    }


    //load implementation.
    // The plugin which loads an API has to be enabled first
    @Override
    public BossAPI loadAPI() {
        return new BossAPIImpl(this);
    }
}
