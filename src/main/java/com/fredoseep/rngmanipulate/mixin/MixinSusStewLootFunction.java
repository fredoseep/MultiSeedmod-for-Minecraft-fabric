package com.fredoseep.rngmanipulate.mixin;


import com.fredoseep.rngmanipulate.WorldRNGState;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SuspiciousStewItem;
import net.minecraft.loot.UniformLootTableRange;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.function.SetStewEffectLootFunction;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.stream.Collectors;

@Mixin({SetStewEffectLootFunction.class})
public class MixinSusStewLootFunction {
   @Shadow
   @Final
   private Map<StatusEffect, UniformLootTableRange> effects;
   @Unique
   private LootContext lootContext;

   @Inject(
      method = {"process"},
      at = {@At("HEAD")}
   )
   public void onProcess(ItemStack stack, LootContext context, CallbackInfoReturnable<ItemStack> cir) {
      this.lootContext = context;
   }

   @Redirect(
      method = {"process"},
      at = @At(
   value = "INVOKE",
   target = "Lnet/minecraft/item/SuspiciousStewItem;addEffectToStew(Lnet/minecraft/item/ItemStack;Lnet/minecraft/entity/effect/StatusEffect;I)V"
)
   )
   public void onAddEffectSusStew(ItemStack stew, StatusEffect effect, int duration) {
      WorldRNGState rngState = WorldRNGState.fromServer(this.lootContext.getWorld().getServer());
      Random random = rngState.getRandom(WorldRNGState.Type.SUS_STEW);
      List<Entry<StatusEffect, UniformLootTableRange>> sortedEffects = (List)this.effects.entrySet().stream().filter((entryx) -> {
         return entryx.getKey() != StatusEffects.POISON;
      }).sorted(Comparator.comparingInt((o) -> {
         return ((StatusEffect)o.getKey()).getColor();
      })).collect(Collectors.toList());
      int i = random.nextInt(sortedEffects.size());
      Entry<StatusEffect, UniformLootTableRange> entry = (Entry)sortedEffects.get(i);
      StatusEffect statusEffect = (StatusEffect)entry.getKey();
      int j = ((UniformLootTableRange)entry.getValue()).next(random);
      if (!statusEffect.isInstant()) {
         j *= 20;
      }

      SuspiciousStewItem.addEffectToStew(stew, statusEffect, j);
   }
}
