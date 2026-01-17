package com.fredoseep.rngmanipulate.mixin.spawn;


import net.minecraft.entity.EntityType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.DrownedEntity;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({DrownedEntity.class})
public class MixinDrownedEntity extends ZombieEntity {
   public MixinDrownedEntity(EntityType<? extends ZombieEntity> entityType, World world) {
      super(entityType, world);
   }

   @Inject(
      method = {"initEquipment"},
      at = {@At(
   value = "INVOKE",
   target = "Lnet/minecraft/entity/mob/DrownedEntity;equipStack(Lnet/minecraft/entity/EquipmentSlot;Lnet/minecraft/item/ItemStack;)V",
   ordinal = 0
)},
      cancellable = true
   )
   public void onEquipTrident(LocalDifficulty difficulty, CallbackInfo ci) {
      ci.cancel();
   }



   protected void dropEquipment(DamageSource source, int lootingMultiplier, boolean allowDrops) {
      super.dropEquipment(source, lootingMultiplier, allowDrops);
   }
}
