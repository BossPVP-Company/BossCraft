package com.bosspvp.api.gui.menu.types;

import com.bosspvp.api.BossPlugin;
import com.bosspvp.api.gui.menu.GuiMenu;
import com.bosspvp.api.gui.menu.SimpleMenu;
import com.bosspvp.api.gui.slot.GuiSlot;
import com.bosspvp.api.inventories.util.ItemBuilder;
import com.bosspvp.api.utils.StringUtils;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;



public class ConfirmationMenu extends SimpleMenu {

    private final GuiMenu parent;
    private final Runnable onConfirm;
    @Setter @Accessors(chain = true)
    private String confirmName = StringUtils.format("&aConfirm action");
    @Setter @Accessors(chain = true)
    private String cancelName = StringUtils.format("&cCancel action");
    @Getter @Setter @Accessors(chain = true)
    private String title = StringUtils.format("&6Action Confirmation");
    public ConfirmationMenu(@NotNull BossPlugin plugin,
                            @Nullable GuiMenu parent,
                            @NotNull Player viewer,
                            @NotNull Runnable onConfirm) {
        super(plugin, viewer,3);
        this.onConfirm = onConfirm;
        this.parent = parent;
    }


    @Override
    protected void onBuildSlots() {

        GuiSlot slotConfirm = GuiSlot.builder(12).setItem(
                new ItemBuilder(Material.LIME_WOOL)
                        .setName(confirmName).toItemStack()
        ).addClickHandler(slot -> {
            onConfirm.run();
            if(parent==null) {
                getViewer().closeInventory();
                return slot.getItem();
            }
            parent.open();
            return slot.getItem();
        }, ClickType.LEFT).build();

        GuiSlot slotDeny = GuiSlot.builder(14).setItem(
                new ItemBuilder(Material.RED_WOOL)
                        .setName(cancelName).toItemStack()
        ).addClickHandler(slot -> {
            if(parent==null) {
                getViewer().closeInventory();
                return slot.getItem();
            }
            parent.open();
            return slot.getItem();
        }, ClickType.LEFT).build();

        addSlot(slotConfirm);
        addSlot(slotDeny);
    }
}
