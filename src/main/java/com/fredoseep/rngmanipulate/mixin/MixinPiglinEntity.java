package com.fredoseep.rngmanipulate.mixin;

import com.fredoseep.rngmanipulate.RNGConstant;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityData;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.PiglinEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Random;

@Mixin({PiglinEntity.class})
public class MixinPiglinEntity extends MobEntity {
   @Unique
   private Random posRandom = null;

   protected MixinPiglinEntity(EntityType<? extends MobEntity> entityType, World world) {
      super(entityType, world);
   }

   @Inject(
      method = {"initialize"},
      at = {@At("HEAD")}
   )
   public void onInitialize(WorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, EntityData entityData, CompoundTag entityTag, CallbackInfoReturnable<EntityData> cir) {
      this.posRandom = RNGConstant.WHITELISTED_SPAWN_REASONS.contains(spawnReason) ? new Random(this.getBlockPos().asLong() + 4262064045L) : this.random;
   }

   @WrapOperation(
      method = {"initialize"},
      at = {@At(
   value = "INVOKE",
   target = "Ljava/util/Random;nextFloat()F"
)}
   )
   public float onTransferBaby(Random instance, Operation<Float> original) {
      return this.posRandom.nextFloat();
   }

   @WrapOperation(
      method = {"equipAtChance"},
      at = {@At(
   value = "INVOKE",
   target = "Ljava/util/Random;nextFloat()F"
)}
   )
   public float onEquipArmor(Random instance, Operation<Float> original) {
      return this.posRandom.nextFloat();
   }

   @WrapOperation(
      method = {"makeInitialWeapon"},
      at = {@At(
   value = "INVOKE",
   target = "Ljava/util/Random;nextFloat()F"
)}
   )
   public float onEquipWeapon(Random instance, Operation<Float> original) {
      return this.posRandom.nextFloat();
   }

   protected void updateEnchantments(LocalDifficulty difficulty) {
      float f = difficulty.getClampedLocalDifficulty();
      if (!this.getMainHandStack().isEmpty() && this.posRandom.nextFloat() < 0.25F * f) {
         this.equipStack(EquipmentSlot.MAINHAND, EnchantmentHelper.enchant(this.posRandom, this.getMainHandStack(), (int)(5.0F + f * (float)this.posRandom.nextInt(18)), false));
      }

      EquipmentSlot[] var3 = EquipmentSlot.values();
      int var4 = var3.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         EquipmentSlot equipmentSlot = var3[var5];
         if (equipmentSlot.getType() == EquipmentSlot.Type.ARMOR) {
            ItemStack itemStack = this.getEquippedStack(equipmentSlot);
            if (!itemStack.isEmpty() && this.posRandom.nextFloat() < 0.5F * f) {
               this.equipStack(equipmentSlot, EnchantmentHelper.enchant(this.posRandom, itemStack, (int)(5.0F + f * (float)this.posRandom.nextInt(18)), false));
            }
         }
      }

   }
}
