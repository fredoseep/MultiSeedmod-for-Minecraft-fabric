package com.fredoseep.rngmanipulate.mixin.world.gen;


import net.minecraft.structure.StructureManager;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.NetherFossilFeature;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({NetherFossilFeature.Start.class})
public class MixinNetherFossilFeature {
   @Inject(
      method = {"init(Lnet/minecraft/world/gen/chunk/ChunkGenerator;Lnet/minecraft/structure/StructureManager;IILnet/minecraft/world/biome/Biome;Lnet/minecraft/world/gen/feature/DefaultFeatureConfig;)V"},
      at = {@At("HEAD")},
      cancellable = true
   )
   public void onGenerate(ChunkGenerator chunkGenerator, StructureManager structureManager, int i, int j, Biome biome, DefaultFeatureConfig defaultFeatureConfig, CallbackInfo ci) {
      if (i == 0 && j == 0) {
         ci.cancel();
      }

   }
}
