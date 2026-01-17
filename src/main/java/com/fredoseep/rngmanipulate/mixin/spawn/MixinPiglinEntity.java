package com.fredoseep.rngmanipulate.mixin.spawn;


import com.fredoseep.rngmanipulate.RNGConstant;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.PiglinEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Random;

@Mixin({PiglinEntity.class})
public class MixinPiglinEntity {
   @Inject(
      method = {"canSpawn"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private static void bastionCheck(EntityType<? extends HostileEntity> type, WorldAccess world, SpawnReason spawnReason, BlockPos pos, Random random, CallbackInfoReturnable<Boolean> cir) {
      if (RNGConstant.shouldCancelBastionSpawn(world.getWorld(), pos, spawnReason)) {
         cir.setReturnValue(false);
      }

   }
}
