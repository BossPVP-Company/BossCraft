package com.bosspvp.api.inventories.manager;

import com.bosspvp.api.inventories.model.BaseInventory;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bukkit.plugin.Plugin;

import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
@Data
public class InventoryRegistry {

    private final Plugin plugin;
    private final Set<BaseInventory> liveInventories = new HashSet<>();

    public InventoryRegistry(Plugin plugin) {
        this.plugin = plugin;
    }

    public void addLiveInventory(BaseInventory baseInventory) {
        this.liveInventories.add(baseInventory);
    }

    public void removeLiveInventory(BaseInventory baseInventory) {
        this.liveInventories.remove(baseInventory);
    }

    public void destroyAllLiveInventories() {
        for (BaseInventory liveInventory : new HashSet<>(this.liveInventories)) {
            liveInventory.destroy();
        }
    }

}