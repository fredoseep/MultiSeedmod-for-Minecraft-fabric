package com.fredoseep.rngmanipulate.mixin.pathing;


import com.fredoseep.rngmanipulate.pathing.AntiBastionMobNavigation;
import com.fredoseep.rngmanipulate.pathing.ZombiefiedPiglinAvoid;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.pathing.EntityNavigation;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.entity.mob.ZombifiedPiglinEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.UUID;

@Mixin({ZombifiedPiglinEntity.class})
public class MixinZombiefiedPiglinEntity extends ZombieEntity {
   @Shadow
   private UUID targetUuid;

   public MixinZombiefiedPiglinEntity(EntityType<? extends ZombieEntity> entityType, World world) {
      super(entityType, world);
   }

   @Inject(
      method = {"mobTick"},
      at = {@At("HEAD")}
   )
   public void onTick(CallbackInfo ci) {
         this.setPathfindingPenalty(ZombiefiedPiglinAvoid.BLOCK_ZOMBIEFIED_PIGLIN, this.targetUuid != null ? 0.0F : -1.0F);
   }

   protected EntityNavigation createNavigation(World world) {
      return new AntiBastionMobNavigation(this, world);
   }
}
