package com.fredoseep.mixin;

import com.fredoseep.client.MultiSeedContext;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.Lifecycle;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.util.registry.RegistryTracker;
import net.minecraft.world.gen.GeneratorOptions;
import net.minecraft.world.level.LevelInfo;
import net.minecraft.world.level.LevelProperties;
import net.minecraft.world.level.storage.SaveVersionInfo;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LevelProperties.class)
public class MixinLevelProperties {

    @Inject(method = "method_29029", at = @At("HEAD"))
    private static void onReadProperties(Dynamic<Tag> dynamic, com.mojang.datafixers.DataFixer dataFixer, int i, CompoundTag compoundTag, LevelInfo levelInfo, SaveVersionInfo saveVersionInfo, GeneratorOptions generatorOptions, Lifecycle lifecycle, CallbackInfoReturnable<LevelProperties> cir) {
        MultiSeedContext.reset();

        long overworld = dynamic.get("MultiSeed_Overworld").asLong(0L);
        long nether = dynamic.get("MultiSeed_Nether").asLong(0L);
        long end = dynamic.get("MultiSeed_End").asLong(0L);

        if (overworld != 0L || nether != 0L || end != 0L) {
            MultiSeedContext.overworldSeed = overworld;
            MultiSeedContext.netherSeed = nether;
            MultiSeedContext.endSeed = end;
        }
    }

    @Inject(method = "updateProperties", at = @At("HEAD"))
    private void onUpdateProperties(RegistryTracker registryTracker, CompoundTag rootTag, CompoundTag playerTag, CallbackInfo ci) {
        if (MultiSeedContext.overworldSeed != 0L) rootTag.putLong("MultiSeed_Overworld", MultiSeedContext.overworldSeed);
        if (MultiSeedContext.netherSeed != 0L) rootTag.putLong("MultiSeed_Nether", MultiSeedContext.netherSeed);
        if (MultiSeedContext.endSeed != 0L) rootTag.putLong("MultiSeed_End", MultiSeedContext.endSeed);
    }


    @Inject(method = "method_29588", at = @At("HEAD"), cancellable = true)
    private void forceStableLifecycle(CallbackInfoReturnable<Lifecycle> cir) {
        cir.setReturnValue(Lifecycle.stable());
    }
}