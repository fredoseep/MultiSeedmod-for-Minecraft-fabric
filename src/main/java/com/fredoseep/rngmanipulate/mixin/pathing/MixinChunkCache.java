package com.fredoseep.rngmanipulate.mixin.pathing;


import com.fredoseep.rngmanipulate.interfaces.ChunkWorldAccessor;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.chunk.ChunkCache;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin({ChunkCache.class})
public class MixinChunkCache implements ChunkWorldAccessor {
   @Shadow
   @Final
   protected World world;

   public WorldAccess ranked$getWorld() {
      return this.world;
   }
}
