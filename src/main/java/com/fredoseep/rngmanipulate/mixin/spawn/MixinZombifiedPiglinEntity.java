package com.fredoseep.rngmanipulate.mixin.spawn;

import com.fredoseep.rngmanipulate.RNGConstant;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.mob.ZombifiedPiglinEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Random;

@Mixin({ZombifiedPiglinEntity.class})
public class MixinZombifiedPiglinEntity {
   @Inject(
      method = {"canSpawn(Lnet/minecraft/entity/EntityType;Lnet/minecraft/world/WorldAccess;Lnet/minecraft/entity/SpawnReason;Lnet/minecraft/util/math/BlockPos;Ljava/util/Random;)Z"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private static void onCanSpawn(EntityType<ZombifiedPiglinEntity> type, WorldAccess world, SpawnReason spawnReason, BlockPos pos, Random random, CallbackInfoReturnable<Boolean> cir) {
      if (RNGConstant.shouldCancelBastionSpawn(world.getWorld(), pos, spawnReason)) {
         cir.setReturnValue(false);
      }

   }
}
