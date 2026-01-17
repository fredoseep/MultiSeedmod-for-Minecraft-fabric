package com.fredoseep.rngmanipulate.interfaces;

import java.util.function.Consumer;
import net.minecraft.server.MinecraftServer;

public interface PreInitWorldData {
    void ranked$setUpdater(Consumer<MinecraftServer> var1);
}
