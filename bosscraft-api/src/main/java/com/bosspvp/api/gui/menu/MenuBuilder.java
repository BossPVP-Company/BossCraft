package com.bosspvp.api.gui.menu;

import com.bosspvp.api.gui.GuiComponent;
import com.bosspvp.api.gui.GuiLayer;
import com.bosspvp.api.gui.slot.GuiSlot;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public interface MenuBuilder {


    @NotNull MenuBuilder setTitle(@NotNull String title);

    default @NotNull MenuBuilder setSlot(@NotNull final GuiSlot slot){
        return setSlot(GuiLayer.MIDDLE,slot);
    }

    default @NotNull MenuBuilder setSlot(@NotNull GuiLayer guiLayer,
                                         @NotNull final GuiSlot slot){
        for(int pos : slot.getSlotPositions()){
            addComponent(pos, guiLayer, slot);
        }
        return this;
    }

    default @NotNull MenuBuilder addComponent(final int position,
                                     @NotNull final GuiComponent component) {
        return this.addComponent(position, GuiLayer.MIDDLE, component);
    }
    @NotNull MenuBuilder addComponent(final int position,
                             @NotNull GuiLayer guiLayer,
                             @NotNull final GuiComponent component);


    @NotNull GuiMenu buildFor(@NotNull Player player);

}
