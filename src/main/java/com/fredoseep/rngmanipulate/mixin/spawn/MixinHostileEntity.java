package com.fredoseep.rngmanipulate.mixin.spawn;



import com.fredoseep.rngmanipulate.RNGConstant;
import com.fredoseep.rngmanipulate.WorldSpawnPreventState;
import net.minecraft.block.DoorBlock;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Random;

@Mixin({HostileEntity.class})
public class MixinHostileEntity {
   @Inject(
      method = {"canSpawnInDark"},
      at = {@At("RETURN")},
      cancellable = true
   )
   private static void structureCheck(EntityType<? extends HostileEntity> type, WorldAccess world, SpawnReason spawnReason, BlockPos pos, Random random, CallbackInfoReturnable<Boolean> cir) {
      if (RNGConstant.shouldCancelBastionSpawn(world.getWorld(), pos, spawnReason)) {
         cir.setReturnValue(false);
      }

      if (world.getWorld() instanceof ServerWorld && (Boolean)cir.getReturnValue()) {
         if (WorldSpawnPreventState.fromWorld((ServerWorld)world.getWorld()).containsBlockPos(pos)) {
            cir.setReturnValue(false);
         } else if (world.getBlockState(pos.add(0, 1, 0)).getBlock() instanceof DoorBlock) {
            cir.setReturnValue(false);
         }
      }

   }
}
