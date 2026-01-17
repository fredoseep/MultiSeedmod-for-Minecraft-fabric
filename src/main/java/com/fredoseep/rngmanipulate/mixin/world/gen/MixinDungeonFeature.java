package com.fredoseep.rngmanipulate.mixin.world.gen;


import com.fredoseep.rngmanipulate.RNGConstant;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.DungeonFeature;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Random;

@Mixin({DungeonFeature.class})
public class MixinDungeonFeature {
   @Inject(
      method = {"generate(Lnet/minecraft/world/ServerWorldAccess;Lnet/minecraft/world/gen/StructureAccessor;Lnet/minecraft/world/gen/chunk/ChunkGenerator;Ljava/util/Random;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/world/gen/feature/DefaultFeatureConfig;)Z"},
      at = {@At("HEAD")},
      cancellable = true
   )
   public void onGenerateChest(ServerWorldAccess serverWorldAccess, StructureAccessor structureAccessor, ChunkGenerator chunkGenerator, Random random, BlockPos blockPos, DefaultFeatureConfig defaultFeatureConfig, CallbackInfoReturnable<Boolean> cir) {
      if (RNGConstant.shouldCancelBlockEntity(serverWorldAccess.getWorld(), blockPos)) {
         cir.setReturnValue(false);
      }

   }
}
