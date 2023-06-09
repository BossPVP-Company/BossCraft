package com.bosspvp.test.commands;

import com.bosspvp.api.BossPlugin;
import com.bosspvp.api.commands.BossSubcommand;
import com.bosspvp.api.commands.CommandBase;
import com.bosspvp.api.exceptions.NotificationException;
import com.bosspvp.api.gui.GuiLayer;
import com.bosspvp.api.gui.components.FillerMask;
import com.bosspvp.api.gui.components.LevelComponent;
import com.bosspvp.api.gui.menu.GuiMenu;
import com.bosspvp.api.gui.menu.MenuBuilder;
import com.bosspvp.api.gui.slot.GuiSlot;
import com.bosspvp.api.inventories.util.ItemBuilder;
import com.bosspvp.api.utils.PluginUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SubcommandGui extends BossSubcommand {
    protected SubcommandGui(@NotNull BossPlugin plugin, @NotNull CommandBase parent) {
        super(plugin, "gui", parent);
    }


    @Override
    protected void onCommandExecute(@NotNull CommandSender sender, @NotNull List<String> args) throws NotificationException {
        MenuBuilder menuBuilder = GuiMenu.builder(getPlugin(),6);
        menuBuilder.setTitle("&aTest GUI");
        //click test
       /* menuBuilder.setSlot(GuiSlot.builder(0)
                .setItem(new ItemBuilder(Material.GREEN_WOOL).setName("&bClick test").toItemStack())
                .addClickHandler((it)->{
                            sender.sendMessage("Hi");
                            return it.getItem();
                        },
                        true,
                        ClickType.LEFT
                )
                .build());*/
        //update test
       /* menuBuilder.setSlot(GuiSlot.builder(8)
                .setItem(new ItemBuilder(Material.BLACK_TERRACOTTA).setName("&bUpdatable item test").toItemStack())
                .setUpdater(5,
                        (it)->{
                            it.setAmount(it.getAmount()>30?1:it.getAmount()+1);
                            return it;
                        })
                .build());*/
        //mask test
      /*  ItemStack[] maskItems = new ItemStack[2];
        maskItems[0] = new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).setName("MASK 1").toItemStack();
        maskItems[1] = new ItemBuilder(Material.RED_MUSHROOM).setName("MASK 2").toItemStack();
        menuBuilder.addComponent(1, GuiLayer.BACKGROUND,
                new FillerMask(maskItems,false,"0011100","0122210","1222221"));
        menuBuilder.buildFor((Player) sender).open();*/

        //level component test
        var levelComponent = new LevelComponent(List.of("109ab0jkl","2080c0i0m",
                "3070d0h0n","4560efg0o",
                "00000000p","00000000q"),
                60) {

            @Override
            public ItemStack getLevelItem(int level, LevelState levelState) {
                if(levelState == LevelState.UNLOCKED){
                    return new ItemBuilder(Material.GREEN_STAINED_GLASS_PANE).toItemStack();
                }else if(levelState == LevelState.IN_PROGRESS){
                    return new ItemBuilder(Material.ORANGE_STAINED_GLASS_PANE).toItemStack();
                }
                return new ItemBuilder(Material.RED_STAINED_GLASS_PANE).toItemStack();
            }

            @Override
            public LevelState getLevelState(int level) {
                if(level<10) {
                    return LevelState.UNLOCKED;
                }else return LevelState.IN_PROGRESS;
            }
        };
        menuBuilder.addComponent(1, GuiLayer.BACKGROUND, levelComponent);
        menuBuilder.setSlot(GuiSlot.builder(5*9).setItem(
                new ItemBuilder(Material.ORANGE_WOOL).toItemStack()
                ).addClickHandler(it->{
                    if(levelComponent.previousPage()){
                        menuBuilder.buildFor((Player) sender).open();
                    }
                    return it.getItem();
                }, ClickType.LEFT).build()
        );
        menuBuilder.setSlot(GuiSlot.builder(6*9-1).setItem(
                        new ItemBuilder(Material.GREEN_WOOL).toItemStack()
                ).addClickHandler(it->{
                    if(levelComponent.nextPage()){
                        menuBuilder.buildFor((Player) sender).open();
                    }
                    return it.getItem();
                }, ClickType.LEFT).build()
        );
        menuBuilder.buildFor((Player) sender).open();

    }
    @Override
    protected @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull List<String> args) {
        return null;
    }

    @Override
    public @NotNull String getDescription() {
        return "gui test";
    }

    @Override
    public @NotNull String getUsage() {
        return "/test gui";
    }

}
