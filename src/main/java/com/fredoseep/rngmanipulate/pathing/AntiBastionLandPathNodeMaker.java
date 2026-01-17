package com.fredoseep.rngmanipulate.pathing;


import com.fredoseep.rngmanipulate.interfaces.ChunkWorldAccessor;
import net.minecraft.block.BlockState;
import net.minecraft.entity.ai.pathing.LandPathNodeMaker;
import net.minecraft.entity.ai.pathing.PathNodeType;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.chunk.ChunkCache;


public class AntiBastionLandPathNodeMaker extends LandPathNodeMaker {
   private final MobEntity owner;

   public AntiBastionLandPathNodeMaker(MobEntity owner) {
      this.owner = owner;
   }

   public PathNodeType getDefaultNodeType(BlockView world, int x, int y, int z) {
      return getLandNodeType(world, new BlockPos.Mutable(x, y, z), this.owner.getPathfindingPenalty(ZombiefiedPiglinAvoid.BLOCK_ZOMBIEFIED_PIGLIN) < 0.0F);
   }

   public static PathNodeType getLandNodeType(BlockView blockView, BlockPos.Mutable mutable, boolean active) {
      int i = mutable.getX();
      int j = mutable.getY();
      int k = mutable.getZ();
      PathNodeType pathNodeType = getCommonNodeType(blockView, mutable, active);
      if (pathNodeType == PathNodeType.OPEN && j >= 1) {
         PathNodeType pathNodeType2 = getCommonNodeType(blockView, mutable.set(i, j - 1, k), active);
         pathNodeType = pathNodeType2 != PathNodeType.WALKABLE && pathNodeType2 != PathNodeType.OPEN && pathNodeType2 != PathNodeType.WATER && pathNodeType2 != PathNodeType.LAVA ? PathNodeType.WALKABLE : PathNodeType.OPEN;
         if (pathNodeType2 == PathNodeType.DAMAGE_FIRE) {
            pathNodeType = PathNodeType.DAMAGE_FIRE;
         }

         if (pathNodeType2 == PathNodeType.DAMAGE_CACTUS) {
            pathNodeType = PathNodeType.DAMAGE_CACTUS;
         }

         if (pathNodeType2 == PathNodeType.DAMAGE_OTHER) {
            pathNodeType = PathNodeType.DAMAGE_OTHER;
         }

         if (pathNodeType2 == PathNodeType.STICKY_HONEY) {
            pathNodeType = PathNodeType.STICKY_HONEY;
         }

         if (pathNodeType2 == ZombiefiedPiglinAvoid.BLOCK_ZOMBIEFIED_PIGLIN) {
            pathNodeType = ZombiefiedPiglinAvoid.BLOCK_ZOMBIEFIED_PIGLIN;
         }
      }

      if (pathNodeType == PathNodeType.WALKABLE) {
         pathNodeType = getNodeTypeFromNeighbors(blockView, mutable.set(i, j, k), pathNodeType);
      }

      return pathNodeType;
   }

   protected static PathNodeType getCommonNodeType(BlockView blockView, BlockPos blockPos, boolean active) {
      BlockState blockState = blockView.getBlockState(blockPos);
      if (blockState.isAir()) {
         return PathNodeType.OPEN;
      } else {
         PathNodeType result = LandPathNodeMaker.getCommonNodeType(blockView, blockPos);
         if (active && result == PathNodeType.BLOCKED) {
            Object worldAccess = blockView;
            if (blockView instanceof ChunkCache) {
               worldAccess = ((ChunkWorldAccessor)blockView).ranked$getWorld();
            }

            if (worldAccess instanceof WorldAccess) {
               worldAccess = ((WorldAccess)worldAccess).getWorld();
            }

            if (worldAccess instanceof ServerWorld && WorldPiglinPathState.fromWorld((ServerWorld)worldAccess).containsBlockPos(blockPos)) {
               return ZombiefiedPiglinAvoid.BLOCK_ZOMBIEFIED_PIGLIN;
            }
         }

         return result;
      }
   }
}
