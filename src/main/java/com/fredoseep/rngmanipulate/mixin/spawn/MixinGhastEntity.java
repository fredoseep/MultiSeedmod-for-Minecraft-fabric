package com.fredoseep.rngmanipulate.mixin.spawn;


import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.mob.GhastEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Iterator;
import java.util.Random;

@Mixin({GhastEntity.class})
public class MixinGhastEntity {
   @Inject(
      method = {"canSpawn"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private static void onCanSpawn(EntityType<GhastEntity> type, WorldAccess world, SpawnReason spawnReason, BlockPos pos, Random random, CallbackInfoReturnable<Boolean> cir) {
         Iterator var6 = world.getPlayers().iterator();
         while(var6.hasNext()) {
            PlayerEntity player = (PlayerEntity)var6.next();
            if (player.squaredDistanceTo((double)pos.getX(), player.getY(), (double)pos.getZ()) < Math.pow(80.0D, 2.0D)) {
               cir.setReturnValue(false);
            }
         }

   }
}
