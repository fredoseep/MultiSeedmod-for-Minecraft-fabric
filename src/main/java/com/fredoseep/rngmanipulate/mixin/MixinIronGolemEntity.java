package com.fredoseep.rngmanipulate.mixin;


import net.minecraft.entity.EntityType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.passive.GolemEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({IronGolemEntity.class})
public class MixinIronGolemEntity extends GolemEntity {
   protected MixinIronGolemEntity(EntityType<? extends GolemEntity> entityType, World world) {
      super(entityType, world);
   }

   protected void dropLoot(DamageSource source, boolean causedByPlayer) {
      this.dropStack(new ItemStack(Items.IRON_INGOT, 4));
   }
}
