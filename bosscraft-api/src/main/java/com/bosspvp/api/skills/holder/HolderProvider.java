package com.bosspvp.api.skills.holder;

import com.bosspvp.api.skills.holder.provided.ProvidedHolder;
import org.bukkit.entity.Player;

import java.util.Collection;

public interface HolderProvider {
    Collection<ProvidedHolder> provide(Player player);
}
