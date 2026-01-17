package com.fredoseep.rngmanipulate.mixin;

import com.fredoseep.rngmanipulate.WorldPiglinBarterState;
import com.fredoseep.rngmanipulate.WorldRNGState;
import com.google.common.collect.Lists;
import com.llamalad7.mixinextras.sugar.Local;

import net.minecraft.entity.mob.PiglinBrain;
import net.minecraft.entity.mob.PiglinEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextType;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin({PiglinBrain.class})
public class MixinPiglinBrain {
   @Redirect(
      method = {"getBarteredItem"},
      at = @At(
   value = "INVOKE",
   target = "Lnet/minecraft/loot/context/LootContext$Builder;build(Lnet/minecraft/loot/context/LootContextType;)Lnet/minecraft/loot/context/LootContext;"
)
   )
   private static LootContext modifyPiglinRandom(LootContext.Builder builder, LootContextType lootContextType, @Local(argsOnly = true) PiglinEntity piglin) {
      MinecraftServer server = piglin.getServer();
      return server == null ? builder.build(lootContextType) : builder.random(WorldRNGState.fromServer(server).getRandom(WorldRNGState.Type.BARTER)).build(lootContextType);
   }

   @Inject(
      method = {"getBarteredItem"},
      at = {@At("RETURN")},
      cancellable = true
   )
   private static void modifyPiglinRandom(PiglinEntity piglin, CallbackInfoReturnable<List<ItemStack>> cir) {
         MinecraftServer server = piglin.getServer();
         List<ItemStack> itemStacks = (List)cir.getReturnValue();
         if (server != null && !itemStacks.isEmpty()) {
            ServerWorld overworld = server.getOverworld();
            WorldRNGState rngState = WorldRNGState.fromServer(server);
            WorldPiglinBarterState piglinBarterState = (WorldPiglinBarterState)overworld.getPersistentStateManager().getOrCreate(WorldPiglinBarterState::new, "piglin_barters");
            cir.setReturnValue(Lists.newArrayList(new ItemStack[]{piglinBarterState.guaranteeItem(piglin, (ItemStack)itemStacks.get(0), rngState.getRandom(WorldRNGState.Type.BARTER))}));
         }
   }
}
