package com.bosspvp.api.skills.triggers.placeholders.types;

import com.bosspvp.api.BossPlugin;
import com.bosspvp.api.placeholders.InjectablePlaceholder;
import com.bosspvp.api.placeholders.types.injectable.StaticPlaceholder;
import com.bosspvp.api.skills.triggers.TriggerData;
import com.bosspvp.api.skills.triggers.placeholders.TriggerPlaceholder;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class TriggerPlaceholderLocation extends TriggerPlaceholder {
    public TriggerPlaceholderLocation() {
        super("location");
    }

    @Override
    public Collection<InjectablePlaceholder> createPlaceholders(TriggerData data) {
        Location location = data.location();
        if(location==null) return new ArrayList<>();
        return List.of(
                new StaticPlaceholder("location_x",()->String.valueOf(location.getX())),
                new StaticPlaceholder("loc_x",()->String.valueOf(location.getX())),
                new StaticPlaceholder("x",()->String.valueOf(location.getX())),

                new StaticPlaceholder("location_y",()->String.valueOf(location.getY())),
                new StaticPlaceholder("loc_y",()->String.valueOf(location.getX())),
                new StaticPlaceholder("y",()->String.valueOf(location.getX())),

                new StaticPlaceholder("location_z",()->String.valueOf(location.getZ())),
                new StaticPlaceholder("loc_z",()->String.valueOf(location.getX())),
                new StaticPlaceholder("z",()->String.valueOf(location.getX())),

                new StaticPlaceholder("location_block_x",()->String.valueOf(location.getBlockX())),
                new StaticPlaceholder("location_b_x",()->String.valueOf(location.getBlockX())),
                new StaticPlaceholder("block_x",()->String.valueOf(location.getBlockX())),
                new StaticPlaceholder("bx",()->String.valueOf(location.getBlockX())),

                new StaticPlaceholder("location_block_y",()->String.valueOf(location.getBlockY())),
                new StaticPlaceholder("location_b_y",()->String.valueOf(location.getBlockX())),
                new StaticPlaceholder("block_y",()->String.valueOf(location.getBlockX())),
                new StaticPlaceholder("by",()->String.valueOf(location.getBlockX())),

                new StaticPlaceholder("location_block_z",()->String.valueOf(location.getBlockZ())),
                new StaticPlaceholder("location_b_z",()->String.valueOf(location.getBlockX())),
                new StaticPlaceholder("block_z",()->String.valueOf(location.getBlockX())),
                new StaticPlaceholder("bz",()->String.valueOf(location.getBlockX())),

                new StaticPlaceholder("location_world",()-> location.getWorld()!=null? location.getWorld().getName():""),
                new StaticPlaceholder("loc_w",()-> location.getWorld()!=null? location.getWorld().getName():""),
                new StaticPlaceholder("world",()-> location.getWorld()!=null? location.getWorld().getName():"")
        );
    }
    /* NamedValue(
                listOf("location_x", "loc_x", "x"),
                location.x
            ),
            NamedValue(
                listOf("location_block_x", "loc_b_x", "block_x", "bx"),
                location.blockX
            ),
            NamedValue(
                listOf("location_y", "loc_y", "y"),
                location.y
            ),
            NamedValue(
                listOf("location_block_y", "loc_b_y", "block_y", "by"),
                location.blockY
            ),
            NamedValue(
                listOf("location_z", "loc_z", "z"),
                location.z
            ),
            NamedValue(
                listOf("location_block_z", "loc_b_z", "block_z", "bz"),
                location.blockZ
            ),
            NamedValue(
                listOf("location_world", "loc_w", "world"),
                location.world?.name ?: ""
            )*/
}
