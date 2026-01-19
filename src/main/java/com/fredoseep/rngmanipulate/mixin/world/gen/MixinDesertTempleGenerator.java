package com.fredoseep.rngmanipulate.mixin.world.gen;


import com.fredoseep.rngmanipulate.WorldSpawnPreventState;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.structure.DesertTempleGenerator;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Random;

@Mixin({DesertTempleGenerator.class})
public class MixinDesertTempleGenerator {
   @Inject(
      method = {"generate"},
      at = {@At("RETURN")}
   )
   public void onGenerate(ServerWorldAccess world, StructureAccessor structureAccessor, ChunkGenerator chunkGenerator, Random random, BlockBox boundingBox, ChunkPos chunkPos, BlockPos blockPos, CallbackInfoReturnable<Boolean> cir) {
      WorldSpawnPreventState spawnPrevent = WorldSpawnPreventState.fromWorld((ServerWorld)world.getWorld());
      if (!spawnPrevent.containsBlockPos(blockPos)) {
         WorldSpawnPreventState.fromWorld((ServerWorld)world.getWorld()).addBlockBox(BlockBox.create(blockPos.getX() - 11, blockPos.getY() - 30, blockPos.getZ() - 11, blockPos.getX() + 11, blockPos.getY() + 14, blockPos.getZ() + 11));
      }

   }
}
