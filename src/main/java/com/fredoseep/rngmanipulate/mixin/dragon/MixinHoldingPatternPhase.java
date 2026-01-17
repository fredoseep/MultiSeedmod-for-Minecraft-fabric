package com.fredoseep.rngmanipulate.mixin.dragon;


import com.fredoseep.rngmanipulate.AccessibleRandom;
import com.fredoseep.rngmanipulate.WorldRNGState;
import com.fredoseep.rngmanipulate.interfaces.EnderDragonPerchTick;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.boss.dragon.phase.AbstractPhase;
import net.minecraft.entity.boss.dragon.phase.HoldingPatternPhase;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;
import java.util.Random;

@Mixin({HoldingPatternPhase.class})
public abstract class MixinHoldingPatternPhase extends AbstractPhase {
   public MixinHoldingPatternPhase(EnderDragonEntity dragon) {
      super(dragon);
   }

   // 对应 method_6841 (原 tickInRange)
   @Redirect(
           method = {"method_6841"},
           at = @At(
                   value = "INVOKE",
                   target = "Ljava/util/Random;nextInt(I)I",
                   ordinal = 0
           )
   )
   public int randomRedirect(Random random, int i) {
     if (((EnderDragonPerchTick)this.dragon).ranked$getPerchTick() > ((EnderDragonPerchTick)this.dragon).ranked$getMaxPerchTick()) {
         return 0;
      } else {
        ((EnderDragonPerchTick) this.dragon).ranked$addPerchPossibility(1.0F / (float) i);
        return ((EnderDragonPerchTick) this.dragon).ranked$isRequireForcePerch() ? 0 : 1;
     }
   }

   // 对应 method_6841 (原 tickInRange)
   @Redirect(
           method = {"method_6841"},
           at = @At(
                   value = "INVOKE",
                   target = "Ljava/util/Random;nextInt(I)I"
           ),
           slice = @Slice(
                   from = @At(
                           value = "INVOKE",
                           target = "Lnet/minecraft/util/math/BlockPos;getSquaredDistance(Lnet/minecraft/util/math/Position;Z)D"
                   ),
                   to = @At(
                           value = "INVOKE",
                           // 这里原来的 strafePlayer 也需要改为 method_6843
                           target = "Lnet/minecraft/entity/boss/dragon/phase/HoldingPatternPhase;method_6843(Lnet/minecraft/entity/player/PlayerEntity;)V"
                   )
           )
   )
   public int randomRedirect2(Random random, int i) {
      return WorldRNGState.fromServer((MinecraftServer)Objects.requireNonNull(this.dragon.getServer())).getRandom(WorldRNGState.Type.DRAGON_PATH).nextInt(i);
   }

   // 对应 method_6841 (原 tickInRange)
   @Redirect(
           method = {"method_6841"},
           at = @At(
                   value = "INVOKE",
                   target = "Ljava/util/Random;nextInt(I)I",
                   ordinal = 3
           )
   )
   public int randomRedirectPath(Random random, int i) {
      return WorldRNGState.fromServer((MinecraftServer)Objects.requireNonNull(this.dragon.getServer())).getRandom(WorldRNGState.Type.DRAGON_PATH).nextInt(i);
   }

   // 对应 method_6842 (原 followPath)
   @Redirect(
           method = {"method_6842"},
           at = @At(
                   value = "INVOKE",
                   target = "Ljava/util/Random;nextFloat()F"
           )
   )
   public float onRefreshed(Random instance) {
      AccessibleRandom accessibleRandom = WorldRNGState.fromServer((MinecraftServer)Objects.requireNonNull(this.dragon.getServer())).getRandom(WorldRNGState.Type.DRAGON_HEIGHT);
      accessibleRandom.nextFloat();
      float value = accessibleRandom.nextFloat();
      return !((EnderDragonPerchTick) this.dragon).ranked$isZeroNodeArrived() ? value * 0.75F : value;
   }

   // 对应 method_6842 (原 followPath)
   @Inject(
           method = {"method_6842"},
           at = {@At(
                   value = "INVOKE",
                   target = "Lnet/minecraft/util/math/Vec3d;<init>(DDD)V",
                   shift = Shift.AFTER
           )}
   )
   public void onEndRefresh(CallbackInfo ci) {
      ((EnderDragonPerchTick)this.dragon).ranked$setZeroNodeArrived(true);
   }
}