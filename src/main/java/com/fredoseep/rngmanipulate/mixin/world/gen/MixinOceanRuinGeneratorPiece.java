package com.fredoseep.rngmanipulate.mixin.world.gen;


import com.fredoseep.rngmanipulate.RNGConstant;
import net.minecraft.structure.OceanRuinGenerator;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Random;

@Mixin({OceanRuinGenerator.Piece.class})
public class MixinOceanRuinGeneratorPiece {
   @Inject(
      method = {"handleMetadata"},
      at = {@At("HEAD")},
      cancellable = true
   )
   public void onGenerateChest(String metadata, BlockPos pos, WorldAccess world, Random random, BlockBox boundingBox, CallbackInfo ci) {
      if ("chest".equals(metadata) && RNGConstant.shouldCancelBlockEntity(world.getWorld(), pos)) {
         ci.cancel();
      }

   }
}
