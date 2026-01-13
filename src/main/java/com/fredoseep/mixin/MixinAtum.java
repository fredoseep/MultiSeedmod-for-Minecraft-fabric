package com.fredoseep.mixin;

import com.fredoseep.util.WorldCreationHelper;
import me.voidxwalker.autoreset.Atum;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.TitleScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(Atum.class)
public class MixinAtum {
    @Inject(method = "createNewWorld", at = @At("HEAD"), cancellable = true, remap = false)
    private static void hijackCreateWorld(CallbackInfo ci) {
        System.out.println("[MultiSeed] 正在劫持 Atum 重置请求...");

        Atum.stopRunning();

        WorldCreationHelper.createAutoWorld(MinecraftClient.getInstance(), new TitleScreen());
        ci.cancel();
    }
}