package com.bosspvp.api.gui.menu.types;

import com.bosspvp.api.BossPlugin;
import com.bosspvp.api.gui.SkullSkin;
import com.bosspvp.api.gui.menu.GuiMenu;
import com.bosspvp.api.gui.menu.SimpleMenu;
import com.bosspvp.api.gui.slot.GuiSlot;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import com.bosspvp.api.inventories.util.ItemBuilder;
import com.bosspvp.api.utils.StringUtils;

public class WarningMenu extends SimpleMenu {
	private final GuiMenu parent;
	private final String message;
	@Getter @Setter @Accessors(chain = true)
	private String title = StringUtils.format("&cAction Warning");
	public WarningMenu(@NotNull BossPlugin plugin,
					   @Nullable GuiMenu parent,
					   @NotNull Player viewer,
					   @NotNull String message) {
		super(plugin, viewer,3);
		this.parent = parent;
		this.message = message;
	}

	@Override
	public void onBuildSlots() {
		GuiSlot backSlot = GuiSlot.builder(13).setItem(
				new ItemBuilder(Material.PLAYER_HEAD).
						makeCustomSkull(SkullSkin.WARNING.getSkin()).setName("&eWarning!").
						setLore(" ","&7Action has been cancelled","",
								"&7warning message:","&8&o"+message, "","&aL Click to return")
						.toItemStack()
		).addClickHandler(slot -> {
			if(parent==null) {
				getViewer().closeInventory();
				return slot.getItem();
			}
			parent.open();
			return slot.getItem();
		}, ClickType.LEFT).build();

		GuiSlot decor = GuiSlot.builder(0,1,2,3,4,5,6,7,8).setItem(
				new ItemBuilder(new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).
						setName("").toItemStack()
		).toItemStack()).build();

		addSlot(backSlot);
		addSlot(decor);

	}



}
