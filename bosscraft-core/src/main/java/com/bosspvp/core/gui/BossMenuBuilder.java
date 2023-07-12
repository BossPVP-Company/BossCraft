package com.bosspvp.core.gui;

import com.bosspvp.api.BossPlugin;
import com.bosspvp.api.gui.GuiComponent;
import com.bosspvp.api.gui.GuiLayer;
import com.bosspvp.api.gui.menu.GuiMenu;
import com.bosspvp.api.gui.menu.MenuBuilder;
import com.bosspvp.api.utils.StringUtils;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BossMenuBuilder implements MenuBuilder {
    private HashMap<Integer, GuiComponent[]> components = new HashMap<>();

    private final BossPlugin plugin;
    private String title = "Empty title";
    private int rows;

    public BossMenuBuilder(BossPlugin plugin, int rows){
        this.plugin = plugin;
        this.rows = rows;
    }

    @Override
    public @NotNull MenuBuilder setTitle(@NotNull String title) {
        this.title = StringUtils.format(title);
        return this;
    }

    @Override
    public @NotNull MenuBuilder addComponent(int position,
                                             @NotNull GuiLayer guiLayer,
                                             @NotNull GuiComponent component) {
        GuiComponent[] list = components.get(position);
        if(list==null){
            //to have empty layers
            list = new GuiComponent[GuiLayer.values().length];
        }
        list[guiLayer.getLayer()] = component;
        components.put(position, list);
        return this;
    }

    @Override
    public @NotNull GuiMenu buildFor(@NotNull Player player) {
        return new BossGuiMenu(plugin,player,title,rows,components);
    }
}
