package com.fredoseep.rngmanipulate;

import com.fredoseep.rngmanipulate.pathing.WorldPiglinPathState;
import com.google.common.collect.Sets;
import net.minecraft.block.Blocks;
import net.minecraft.entity.SpawnReason;
import net.minecraft.predicate.entity.LocationPredicate;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.SingleStateFeatureConfig;
import net.minecraft.world.gen.feature.StructureFeature;

import java.util.Set;


public class RNGConstant {
   public static final Set<SpawnReason> WHITELISTED_SPAWN_REASONS;
   public static final SingleStateFeatureConfig LAVA_FEATURE_CONFIG;
   private static final LocationPredicate bastionFeature;

   public static boolean shouldCancelBlockEntity(World world, Vec3i blockPos) {
      return Math.abs(blockPos.getX()) < 500 && Math.abs(blockPos.getZ()) < 500;
   }

   public static boolean shouldCancelBastionSpawn(World world, BlockPos blockPos, SpawnReason reason) {
      if (reason != SpawnReason.NATURAL) {
         return false;
      } else if (!world.getDimension().hasCeiling()) {
         return false;
      }else if (world instanceof ServerWorld && bastionFeature.test((ServerWorld)world, (float)blockPos.getX(), (float)blockPos.getY(), (float)blockPos.getZ())) {
         return true;
      } else {
         BlockPos targetPos = new BlockPos(blockPos);
         if (world instanceof ServerWorld) {
            for(int i = 0; i < 3; ++i) {
               if (WorldPiglinPathState.fromWorld((ServerWorld)world).containsBlockPos(targetPos)) {
                  return true;
               }

               targetPos = targetPos.down();
            }
         }

         return false;
      }
   }

   static {
      WHITELISTED_SPAWN_REASONS = Sets.newHashSet(new SpawnReason[]{SpawnReason.NATURAL, SpawnReason.CHUNK_GENERATION, SpawnReason.STRUCTURE});
      LAVA_FEATURE_CONFIG = new SingleStateFeatureConfig(Blocks.LAVA.getDefaultState());
      bastionFeature = LocationPredicate.feature(StructureFeature.BASTION_REMNANT);
   }
}
