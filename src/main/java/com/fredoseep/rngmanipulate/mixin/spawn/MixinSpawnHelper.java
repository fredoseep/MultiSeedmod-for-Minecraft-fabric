package com.fredoseep.rngmanipulate.mixin.spawn;

import com.fredoseep.rngmanipulate.WorldSpawnState;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.collection.WeightedPicker;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.SpawnHelper;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.feature.StructureFeature;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.Random;

@Mixin({SpawnHelper.class})
public abstract class MixinSpawnHelper {
   @Unique
   private static boolean shouldTakeNextSeed = false;

   @Inject(
      method = {"spawnEntitiesInChunk(Lnet/minecraft/entity/SpawnGroup;Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/world/chunk/Chunk;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/world/SpawnHelper$Checker;Lnet/minecraft/world/SpawnHelper$Runner;)V"},
      at = {@At(
   value = "INVOKE",
   target = "Lnet/minecraft/util/math/MathHelper;ceil(F)I"
)}
   )
   private static void spawnRework(SpawnGroup group, ServerWorld world, Chunk chunk, BlockPos pos, SpawnHelper.Checker checker, SpawnHelper.Runner runner, CallbackInfo ci) {
      shouldTakeNextSeed = false;
      WorldSpawnState spawnState = WorldSpawnState.fromWorld(world);
      spawnState.getEntryRandom().seed.set(spawnState.getEntrySeed());
   }

   @WrapOperation(
      method = {"pickRandomSpawnEntry"},
      at = {@At(
   value = "INVOKE",
   target = "Lnet/minecraft/util/collection/WeightedPicker;getRandom(Ljava/util/Random;Ljava/util/List;)Lnet/minecraft/util/collection/WeightedPicker$Entry;"
)}
   )
   private static <T extends WeightedPicker.Entry> T onPickEntry(Random random, List<T> list, Operation<T> original, @Local(argsOnly = true) ServerWorld world, @Local(argsOnly = true) BlockPos blockPos) {
      if (list == StructureFeature.FORTRESS.getMonsterSpawns()) {
         PlayerEntity player = world.getClosestPlayer((double)blockPos.getX() + 0.5D, (double)blockPos.getY(), (double)blockPos.getZ() + 0.5D, 88.0D, false);
         if (player != null) {
            shouldTakeNextSeed = true;
            return (T) original.call(random, list);
         }
      }

      return (T) original.call(random, list);
   }

   @WrapOperation(
      method = {"spawnEntitiesInChunk(Lnet/minecraft/entity/SpawnGroup;Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/world/chunk/Chunk;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/world/SpawnHelper$Checker;Lnet/minecraft/world/SpawnHelper$Runner;)V"},
      at = {@At(
   value = "FIELD",
   target = "Lnet/minecraft/server/world/ServerWorld;random:Ljava/util/Random;",
   ordinal = 6
)}
   )
   private static Random onPickCount(ServerWorld instance, Operation<Random> original, @Local(argsOnly = true) SpawnGroup group, @Local(argsOnly = true) Chunk chunk) {
      return (Random)(!shouldTakeNextSeed ? (Random)original.call(instance) : WorldSpawnState.fromWorld(instance).getEntryRandom());
   }

   @Inject(
      method = {"spawnEntitiesInChunk(Lnet/minecraft/entity/SpawnGroup;Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/world/chunk/Chunk;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/world/SpawnHelper$Checker;Lnet/minecraft/world/SpawnHelper$Runner;)V"},
      at = {@At(
   value = "INVOKE",
   target = "Lnet/minecraft/world/SpawnHelper$Runner;run(Lnet/minecraft/entity/mob/MobEntity;Lnet/minecraft/world/chunk/Chunk;)V"
)}
   )
   private static void spawnReworkEnd(SpawnGroup group, ServerWorld world, Chunk chunk, BlockPos pos, SpawnHelper.Checker checker, SpawnHelper.Runner runner, CallbackInfo ci, @Local MobEntity mobEntity) {
      if (shouldTakeNextSeed) {
         WorldSpawnState spawnState = WorldSpawnState.fromWorld(world);
         spawnState.setEntrySeed(spawnState.getEntryRandom().getSeed());
      }

   }
}
