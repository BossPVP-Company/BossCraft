package com.bosspvp.api.gui.components;

import com.bosspvp.api.gui.GuiComponent;
import com.bosspvp.api.gui.slot.GuiSlot;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public abstract class GuiPage implements GuiComponent {

    @Getter
    private List<GuiSlot> elements;
    @Getter
    private int currentPage;

    @Getter
    private int columnsSize;
    @Getter
    private int rowsSize;
    @Getter @Setter
    private int maxPage;

    public GuiPage(int columnsSize, int rowsSize, int maxPage){
        this.columnsSize = columnsSize;
        this.rowsSize = rowsSize;
        this.maxPage = maxPage;
    }

    public boolean changePage(int value){
        if(value>maxPage||value<0) return false;
        currentPage = value;
        elements = getElements(currentPage);
        return true;
    }
    public boolean nextPage(){
        return changePage(currentPage + 1);
    }
    public boolean previousPage(){
        return changePage(currentPage - 1);
    }

    @Override
    public boolean isOnPlayerInventory() {
        return false;
    }

    abstract List<GuiSlot> getElements(int page);
    @Override
    public @Nullable GuiSlot getSlotAt(int row, int column) {
        return elements.get(column+row*getRowsSize());
    }

}
