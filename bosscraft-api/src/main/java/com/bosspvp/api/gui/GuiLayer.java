package com.bosspvp.api.gui;

import lombok.Getter;

public enum GuiLayer {
    BACKGROUND(0),
    MIDDLE(1),
    FOREGROUND(2);

    @Getter
    private  final int layer;
    GuiLayer(int layer){
        this.layer = layer;
    }
}
