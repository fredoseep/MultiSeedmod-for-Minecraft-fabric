package com.fredoseep.rngmanipulate.mixin.world.gen;

import com.fredoseep.rngmanipulate.WorldSpawnState;
import com.llamalad7.mixinextras.sugar.Local;

import net.minecraft.server.world.ServerWorld;
import net.minecraft.structure.NetherFortressGenerator;
import net.minecraft.structure.StructurePieceType;
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

@Mixin({NetherFortressGenerator.BridgePlatform.class})
public abstract class MixinNetherFortressGenerator extends NetherFortressGenerator.Piece {
   protected MixinNetherFortressGenerator(StructurePieceType structurePieceType, int i) {
      super(structurePieceType, i);
   }

   @Inject(
      method = {"generate"},
      at = {@At(
   value = "INVOKE",
   target = "Lnet/minecraft/world/MobSpawnerLogic;setEntityId(Lnet/minecraft/entity/EntityType;)V"
)}
   )
   public void onGenerate(ServerWorldAccess world, StructureAccessor structureAccessor, ChunkGenerator chunkGenerator, Random random, BlockBox boundingBox, ChunkPos chunkPos, BlockPos blockPos, CallbackInfoReturnable<Boolean> cir, @Local(ordinal = 1) BlockPos spawnerPos) {
      if (this.getFacing() != null) {
         WorldSpawnState.fromWorld((ServerWorld)world.getWorld()).setSpawnerDirection(spawnerPos, this.getFacing());
      }

   }
}
