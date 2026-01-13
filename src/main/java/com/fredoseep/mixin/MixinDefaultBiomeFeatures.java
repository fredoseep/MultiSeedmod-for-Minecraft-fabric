package com.fredoseep.mixin;


import net.minecraft.world.gen.feature.DefaultBiomeFeatures;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(DefaultBiomeFeatures.class)
public class MixinDefaultBiomeFeatures {

    /**
     * 拦截 addDefaultLakes 和 addDesertLakes
     * 目标：找到里面那个 new ChanceDecoratorConfig(80) 的 "80"
     */
    @ModifyArg(
            method = {"addDefaultLakes", "addDesertLakes"},
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/gen/decorator/ChanceDecoratorConfig;<init>(I)V"
            ),
            index = 0
    )
    private static int increaseLavaFrequency(int originalChance) {
        if (originalChance == 80) {
            return 30;
        }
        return originalChance;
    }
}