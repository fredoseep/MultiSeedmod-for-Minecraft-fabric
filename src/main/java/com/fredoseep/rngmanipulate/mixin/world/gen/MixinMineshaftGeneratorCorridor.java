package com.fredoseep.rngmanipulate.mixin.world.gen;

import com.fredoseep.rngmanipulate.RNGConstant;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;

import net.minecraft.structure.MineshaftGenerator;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.WorldAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Random;

@Mixin({MineshaftGenerator.MineshaftCorridor.class})
public class MixinMineshaftGeneratorCorridor {
   @Inject(
      method = {"addChest"},
      at = {@At(
   value = "INVOKE",
   target = "Lnet/minecraft/util/math/BlockBox;contains(Lnet/minecraft/util/math/Vec3i;)Z"
)},
      cancellable = true
   )
   public void onAddChestCart(WorldAccess world, BlockBox boundingBox, Random random, int x, int y, int z, Identifier lootTableId, CallbackInfoReturnable<Boolean> cir, @Local BlockPos blockPos) {
      if (RNGConstant.shouldCancelBlockEntity(world.getWorld(), blockPos)) {
         cir.setReturnValue(false);
      }

   }

   @WrapOperation(
      method = {"generate"},
      at = {@At(
   value = "INVOKE",
   target = "Lnet/minecraft/util/math/BlockBox;contains(Lnet/minecraft/util/math/Vec3i;)Z"
)}
   )
   public boolean checkSpawnerPos(BlockBox instance, Vec3i blockPos, Operation<Boolean> operation, @Local(argsOnly = true) ServerWorldAccess world) {
      return RNGConstant.shouldCancelBlockEntity(world.getWorld(), blockPos) ? false : (Boolean)operation.call(instance, blockPos);
   }
}
