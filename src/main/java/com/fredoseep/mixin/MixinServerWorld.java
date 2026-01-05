package com.fredoseep.mixin;

import com.fredoseep.client.MultiSeedContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.gen.GeneratorOptions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerWorld.class)
public class MixinServerWorld {


    @Inject(method = "getSeed", at = @At("HEAD"), cancellable = true)
    private void overrideDimensionSeed(CallbackInfoReturnable<Long> cir) {
        World self = (World) (Object) this;
        DimensionType dimType = self.getDimension();

        long customOverworld = MultiSeedContext.overworldSeed;
        long customNether = MultiSeedContext.netherSeed;
        long customEnd = MultiSeedContext.endSeed;

        if (dimType.hasEnderDragonFight()) {
            if (customEnd != 0L) {
                cir.setReturnValue(customEnd);
            }
        }

        else if (dimType.hasCeiling() && !dimType.hasSkyLight()) {
            if (customNether != 0L) {
                cir.setReturnValue(customNether);
            }
        }

        else if (dimType.hasSkyLight() && !dimType.hasCeiling()) {
            if (customOverworld != 0L) {
                cir.setReturnValue(customOverworld);
            }
        }
    }


    @Redirect(
            method = "<init>",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/gen/GeneratorOptions;getSeed()J"
            )
    )
    private long redirectDragonFightSeed(GeneratorOptions options) {
        if (MultiSeedContext.endSeed != 0L) {
            return MultiSeedContext.endSeed;
        }
        return options.getSeed();
    }
}