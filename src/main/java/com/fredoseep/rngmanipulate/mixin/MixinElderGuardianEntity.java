package com.fredoseep.rngmanipulate.mixin;


import net.minecraft.entity.mob.ElderGuardianEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.include.com.google.common.collect.Lists;

import java.util.List;
import java.util.function.Predicate;

@Mixin({ElderGuardianEntity.class})
public class MixinElderGuardianEntity {
   @Redirect(
      method = {"mobTick"},
      at = @At(
   value = "INVOKE",
   target = "Lnet/minecraft/server/world/ServerWorld;getPlayers(Ljava/util/function/Predicate;)Ljava/util/List;"
)
   )
   public List<ServerPlayerEntity> emptyPlayers(ServerWorld instance, Predicate<ServerPlayerEntity> predicate) {
      return Lists.newArrayList() ;
   }
}
