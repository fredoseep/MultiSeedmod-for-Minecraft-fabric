package com.fredoseep.mixin;

import net.minecraft.client.gui.hud.DebugHud;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(DebugHud.class)
public class DebugHudMixin {

    @Inject(method = "getRightText", at = @At("RETURN"))
    private void addModVersion(CallbackInfoReturnable<List<String>> cir) {
        List<String> list = cir.getReturnValue();
        list.add("5年刷种，3年排位-2.1.1");
    }
}