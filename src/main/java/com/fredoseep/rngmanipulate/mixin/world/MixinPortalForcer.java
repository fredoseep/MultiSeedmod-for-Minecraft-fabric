

package com.fredoseep.rngmanipulate.mixin.world;

import com.fredoseep.rngmanipulate.WorldPortalState;
import com.fredoseep.util.BlindStateChecker;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.Heightmap;
import net.minecraft.world.PortalForcer;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.Constant.Condition;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Objects;

@Mixin({PortalForcer.class})
public class MixinPortalForcer {
   @Shadow
   @Final
   private ServerWorld world;
   @Unique
   private int modifyX = 0;
   @Unique
   private int modifyY = 0;
   @Unique
   private int modifyZ = 0;
   @Unique
   private Entity entity;

   @Inject(
           method = {"createPortal"},
           at = {@At("HEAD")}
   )
   public void onCreatePortal(Entity entity, CallbackInfoReturnable<Boolean> cir) {
      this.entity = entity;
      if (entity.world.getRegistryKey() == World.NETHER) {
         if (BlindStateChecker.isBlindRunReady() && entity.getBlockPos().getY() >= 48) {
            this.modifyY = MathHelper.clamp(entity.getBlockPos().getY() - 5, 0, 70);
            return;
         }
      }

      this.modifyY = 0;
   }

   @ModifyVariable(
           method = {"createPortal"},
           at = @At("STORE"),
           ordinal = 3
   )
   public double onCreatePortal91(double constant, @Local(argsOnly = true) Entity entity) {
      return this.modifyY != 0 ? (double)0.0F : constant;
   }

   @ModifyConstant(
           method = {"createPortal"},
           constant = {@Constant(
                   intValue = 0,
                   ordinal = 1,
                   expandZeroConditions = {Condition.GREATER_THAN_OR_EQUAL_TO_ZERO}
           )}
   )
   public int onCreatePortal1(int constant, @Local BlockPos.Mutable mutable) {
      if (this.modifyY != 0) {
         int maxHeight = this.world.getChunk(mutable).sampleHeightmap(Heightmap.Type.MOTION_BLOCKING, mutable.getX(), mutable.getZ());
         return maxHeight - 3;
      } else {
         return constant;
      }
   }

   @ModifyConstant(
           method = {"createPortal"},
           constant = {@Constant(
                   intValue = 0,
                   ordinal = 6,
                   expandZeroConditions = {Condition.GREATER_THAN_OR_EQUAL_TO_ZERO}
           )}
   )
   public int onCreatePortal7(int constant, @Local BlockPos.Mutable mutable) {
      if (this.modifyY != 0) {
         int maxHeight = this.world.getChunk(mutable).sampleHeightmap(Heightmap.Type.MOTION_BLOCKING, mutable.getX(), mutable.getZ());
         return maxHeight - 3;
      } else {
         return constant;
      }
   }

   @ModifyConstant(
           method = {"createPortal"},
           constant = {@Constant(
                   intValue = 0,
                   ordinal = 1,
                   expandZeroConditions = {Condition.GREATER_THAN_ZERO}
           )}
   )
   public int onCreatePortal2(int constant, @Local BlockPos.Mutable mutable) {
      if (this.modifyY != 0) {
         int maxHeight = this.world.getChunk(mutable).sampleHeightmap(Heightmap.Type.MOTION_BLOCKING, mutable.getX(), mutable.getZ());
         return maxHeight - 1;
      } else {
         return constant;
      }
   }

   @ModifyConstant(
           method = {"createPortal"},
           constant = {@Constant(
                   intValue = 0,
                   ordinal = 4,
                   expandZeroConditions = {Condition.GREATER_THAN_ZERO}
           )}
   )
   public int onCreatePortal8(int constant, @Local BlockPos.Mutable mutable) {
      if (this.modifyY != 0) {
         int maxHeight = this.world.getChunk(mutable).sampleHeightmap(Heightmap.Type.MOTION_BLOCKING, mutable.getX(), mutable.getZ());
         return maxHeight - 1;
      } else {
         return constant;
      }
   }

   @ModifyVariable(
           method = {"createPortal"},
           at = @At(
                   value = "STORE",
                   ordinal = 2
           ),
           ordinal = 10
   )
   public int onCreatePortal91(int constant) {
      this.modifyX = constant;
      return constant;
   }

   @ModifyVariable(
           method = {"createPortal"},
           at = @At(
                   value = "STORE",
                   ordinal = 2
           ),
           ordinal = 12
   )
   public int onCreatePortal93(int constant) {
      this.modifyZ = constant;
      return constant;
   }

   @ModifyVariable(
           method = {"createPortal"},
           at = @At(
                   value = "STORE",
                   ordinal = 3
           ),
           ordinal = 11
   )
   public int onCreatePortal92(int constant) {
      if (this.modifyY != 0) {
         BlockPos blockPos = new BlockPos(this.modifyX, constant, this.modifyZ);
         int maxHeight = this.world.getChunk(blockPos).sampleHeightmap(Heightmap.Type.MOTION_BLOCKING, blockPos.getX(), blockPos.getZ());
         int oceanMaxHeight = this.world.getChunk(blockPos).sampleHeightmap(Heightmap.Type.OCEAN_FLOOR, blockPos.getX(), blockPos.getZ());
         return maxHeight >= this.modifyY && maxHeight - oceanMaxHeight < 2 ? maxHeight : 70;
      } else {
         return constant;
      }
   }

   @WrapOperation(
           method = {"createPortal"},
           at = {@At(
                   value = "INVOKE",
                   target = "Lnet/minecraft/server/world/ServerWorld;setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;I)Z",
                   ordinal = 1
           )}
   )
   public boolean onCreatePortalEnd(ServerWorld instance, BlockPos blockPos, BlockState state, int i, Operation<Boolean> original) {
      if (this.entity.world.getRegistryKey() == World.OVERWORLD) {
         WorldPortalState portalState = WorldPortalState.fromServer(instance.getServer());
         if (portalState.getNetherPos() == null) {
            portalState.setNetherPos(blockPos);
         }
      }

      return (Boolean)original.call(instance, blockPos, state, i);
   }

   @Inject(
           method = {"createPortal"},
           at = {@At("RETURN")}
   )
   public void onCreatePortalReturn(Entity entity, CallbackInfoReturnable<Boolean> cir) {
      this.entity = null;
   }
}
