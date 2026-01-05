package com.fredoseep.mixin;

import com.fredoseep.SeedSeparateHelper;
import com.fredoseep.client.MultiSeedContext;
import net.minecraft.world.gen.GeneratorOptions;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(GeneratorOptions.class)
public class MixinGeneratorOptions implements SeedSeparateHelper {

    @Shadow @Final private long seed;

    private long netherSeed = 0L;
    private long theEndSeed = 0L;


    @Inject(method = "getSeed", at = @At("HEAD"), cancellable = true)
    private void overrideMainSeed(CallbackInfoReturnable<Long> cir) {
        if (MultiSeedContext.overworldSeed != 0L) {
            cir.setReturnValue(MultiSeedContext.overworldSeed);
        }
    }



    @Override public void ss$setNetherSeed(long seed) { this.netherSeed = seed; }
    @Override public void ss$setTheEndSeed(long seed) { this.theEndSeed = seed; }
}