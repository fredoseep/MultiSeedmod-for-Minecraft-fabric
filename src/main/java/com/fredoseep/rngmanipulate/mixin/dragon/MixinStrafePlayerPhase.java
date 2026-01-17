package com.fredoseep.rngmanipulate.mixin.dragon;

import com.fredoseep.rngmanipulate.WorldRNGState;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.boss.dragon.phase.AbstractPhase;
import net.minecraft.entity.boss.dragon.phase.StrafePlayerPhase;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Objects;
import java.util.Random;

@Mixin({StrafePlayerPhase.class})
public abstract class MixinStrafePlayerPhase extends AbstractPhase {
   public MixinStrafePlayerPhase(EnderDragonEntity dragon) {
      super(dragon);
   }

   // 对应 method_6861 (原 followPath)
   @Redirect(
           method = {"method_6861"},
           at = @At(
                   value = "INVOKE",
                   target = "Ljava/util/Random;nextFloat()F"
           )
   )
   public float modifyTargetHeight(Random random) {
      return WorldRNGState.fromServer((MinecraftServer)Objects.requireNonNull(this.dragon.getServer())).getRandom(WorldRNGState.Type.DRAGON_STANDARD).nextFloat();
   }

   // 对应 method_6860 (原 updatePath)
   @Redirect(
           method = {"method_6860"},
           at = @At(
                   value = "INVOKE",
                   target = "Ljava/util/Random;nextInt(I)I"
           )
   )
   public int modifyDragonPath(Random random, int i) {
      return  WorldRNGState.fromServer((MinecraftServer)Objects.requireNonNull(this.dragon.getServer())).getRandom(WorldRNGState.Type.DRAGON_STANDARD).nextInt(i);
   }
}