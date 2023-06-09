package com.bosspvp.api.worlds;

import com.bosspvp.api.worlds.location.WorldPosition;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.generator.ChunkGenerator;

//made it protected cause otherwise BaseWorld class
// won't be able to getEffectBuilder access to constructor

@NoArgsConstructor(force = true, access = AccessLevel.PROTECTED)
@Data
public class BaseWorldProperties {

    private WorldPosition spawnPosition;
    private long seed;
    private World.Environment environment;
    private transient ChunkGenerator chunkGenerator;


    public WorldCreator getWorldCreator(String name) {
        WorldCreator worldCreator = WorldCreator.name(name);
        if (this.seed != 0)
            worldCreator.seed(this.seed);
        if (this.environment != null)
            worldCreator.environment(this.environment);
        if (this.chunkGenerator != null)
            worldCreator.generator(this.chunkGenerator);
        return worldCreator;
    }

}