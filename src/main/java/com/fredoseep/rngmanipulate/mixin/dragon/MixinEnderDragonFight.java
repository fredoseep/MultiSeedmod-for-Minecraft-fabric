package com.fredoseep.rngmanipulate.mixin.dragon;

import com.fredoseep.rngmanipulate.WorldRNGState;
import com.llamalad7.mixinextras.sugar.Local;


import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.boss.dragon.EnderDragonFight;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Objects;

@Mixin({EnderDragonFight.class})
public class MixinEnderDragonFight {
   @Shadow
   @Final
   private ServerWorld world;

   @Inject(
      method = {"createDragon"},
      at = {@At(
   value = "INVOKE",
   target = "Lnet/minecraft/entity/boss/dragon/EnderDragonEntity;refreshPositionAndAngles(DDDFF)V",
   shift = Shift.AFTER
)}
   )
   public void onSpawnDragon(CallbackInfoReturnable<EnderDragonEntity> cir, @Local EnderDragonEntity enderDragonEntity) {
         enderDragonEntity.refreshPositionAndAngles(0.0D, 128.0D, 0.0D, WorldRNGState.fromServer((MinecraftServer)Objects.requireNonNull(this.world.getServer())).getRandom(WorldRNGState.Type.DRAGON_STANDARD).nextFloat() * 360.0F, 0.0F);
   }
}
