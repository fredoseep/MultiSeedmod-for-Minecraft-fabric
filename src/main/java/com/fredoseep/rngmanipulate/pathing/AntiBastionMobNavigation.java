package com.fredoseep.rngmanipulate.pathing;


import net.minecraft.entity.ai.pathing.MobNavigation;
import net.minecraft.entity.ai.pathing.PathNodeNavigator;
import net.minecraft.entity.ai.pathing.PathNodeType;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.world.World;

public class AntiBastionMobNavigation extends MobNavigation {
   public AntiBastionMobNavigation(MobEntity mobEntity, World world) {
      super(mobEntity, world);
   }

   protected PathNodeNavigator createPathNodeNavigator(int range) {
      this.nodeMaker = new AntiBastionLandPathNodeMaker(this.entity);
      this.nodeMaker.setCanEnterOpenDoors(true);
      return new PathNodeNavigator(this.nodeMaker, range);
   }

   protected boolean canWalkOnPath(PathNodeType pathType) {
      return pathType == ZombiefiedPiglinAvoid.BLOCK_ZOMBIEFIED_PIGLIN && this.entity.getPathfindingPenalty(pathType) < 0.0F ? false : super.canWalkOnPath(pathType);
   }
}
