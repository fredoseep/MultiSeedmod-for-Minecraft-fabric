package com.fredoseep.rngmanipulate.mixin.world.gen;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.SpringFeature;
import net.minecraft.world.gen.feature.SpringFeatureConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import java.util.Random;

@Mixin({SpringFeature.class})
public class MixinSpringFeature {
   @Overwrite
   public boolean generate(ServerWorldAccess serverWorldAccess, StructureAccessor structureAccessor, ChunkGenerator chunkGenerator, Random random, BlockPos blockPos, SpringFeatureConfig springFeatureConfig) {
      if (!springFeatureConfig.validBlocks.contains(serverWorldAccess.getBlockState(blockPos.up()).getBlock())) {
         return false;
      } else if (springFeatureConfig.requiresBlockBelow && !springFeatureConfig.validBlocks.contains(serverWorldAccess.getBlockState(blockPos.down()).getBlock())) {
         return false;
      } else {
         BlockState blockState = serverWorldAccess.getBlockState(blockPos);
         if (!blockState.isAir() && !springFeatureConfig.validBlocks.contains(blockState.getBlock())) {
            return false;
         } else {
            int rock = 0;
            int hole = 0;
            ChunkPos mainChunk = new ChunkPos(blockPos);
            Direction[] var11 = Direction.values();
            int var12 = var11.length;

            for(int var13 = 0; var13 < var12; ++var13) {
               Direction direction = var11[var13];
               if (direction != Direction.UP) {
                  BlockPos sidePos = blockPos.offset(direction);
                  if (mainChunk.equals(new ChunkPos(sidePos))) {
                     BlockState sideBlockState = serverWorldAccess.getBlockState(sidePos);
                     if (springFeatureConfig.validBlocks.contains(sideBlockState.getBlock())) {
                        ++rock;
                     } else if (sideBlockState.isAir()) {
                        ++hole;
                     }
                  }
               }
            }

            if (rock == springFeatureConfig.rockCount && hole == springFeatureConfig.holeCount) {
               serverWorldAccess.setBlockState(blockPos, springFeatureConfig.state.getBlockState(), 2);
               serverWorldAccess.getFluidTickScheduler().schedule(blockPos, springFeatureConfig.state.getFluid(), 0);
               return true;
            } else {
               return false;
            }
         }
      }
   }
}
