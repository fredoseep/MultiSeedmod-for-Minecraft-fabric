package com.fredoseep.rngmanipulate.mixin.dragon;

import com.fredoseep.rngmanipulate.WorldRNGState;
import com.fredoseep.rngmanipulate.interfaces.EnderDragonPerchTick;


import net.minecraft.entity.EntityType;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.boss.dragon.phase.PhaseManager;
import net.minecraft.entity.boss.dragon.phase.PhaseType;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;

@Mixin({EnderDragonEntity.class})
public class MixinEnderDragonEntity extends MobEntity implements EnderDragonPerchTick {
   @Shadow
   @Final
   private PhaseManager phaseManager;
   @Unique
   private boolean zeroNodeArrived = false;
   @Unique
   private int perchAge = 0;
   @Unique
   private int perchCount = 0;
   @Unique
   private float totalPerchPossibility = 0.0F;
   @Unique
   private float goalPerchPossibility = 0.0F;

   protected MixinEnderDragonEntity(EntityType<? extends MobEntity> entityType, World world) {
      super(entityType, world);
   }



   @Inject(
      method = {"tickMovement"},
      at = {@At(
   value = "INVOKE",
   target = "Lnet/minecraft/entity/boss/dragon/phase/Phase;serverTick()V",
   ordinal = 0,
   shift = Shift.BEFORE
)}
   )
   public void onTickMoveBefore(CallbackInfo ci) {
      if (this.goalPerchPossibility == 0.0F) {
         this.ranked$perched();
      }

   }

   @Inject(
      method = {"tickMovement"},
      at = {@At(
   value = "INVOKE",
   target = "Lnet/minecraft/entity/boss/dragon/phase/Phase;serverTick()V",
   ordinal = 0,
   shift = Shift.AFTER
)}
   )
   public void onTickMove(CallbackInfo ci) {
      PhaseType<?> phaseType = this.phaseManager.getCurrent().getType();
      if (phaseType == PhaseType.LANDING_APPROACH) {
         this.ranked$perched();
      } else {
         if (phaseType.getTypeId() < 2) {
            ++this.perchAge;
         }

      }
   }

   public int ranked$getPerchTick() {
      return this.perchAge;
   }

   public int ranked$getMaxPerchTick() {
      return 2900;
   }

   public void ranked$perched() {
      if (!this.world.isClient()) {
         if (this.totalPerchPossibility != 0.0F) {
            ++this.perchCount;
         }

         this.perchAge = 0;
         this.totalPerchPossibility = 0.0F;
         float perchAdvance = Math.max(1.0F, (float)this.perchCount / 5.0F) - 1.0F;
         this.goalPerchPossibility = WorldRNGState.fromServer((MinecraftServer)Objects.requireNonNull(this.getServer())).getRandom(WorldRNGState.Type.DRAGON_PERCH).nextFloat() * (0.6F + perchAdvance * perchAdvance);
      }
   }

   public boolean ranked$isRequireForcePerch() {
      return this.totalPerchPossibility >= this.goalPerchPossibility;
   }

   public void ranked$addPerchPossibility(float f) {
      this.totalPerchPossibility += f;
   }

   public void ranked$setZeroNodeArrived(boolean b) {
      this.zeroNodeArrived = b;
   }

   public boolean ranked$isZeroNodeArrived() {
      return this.zeroNodeArrived;
   }
}
