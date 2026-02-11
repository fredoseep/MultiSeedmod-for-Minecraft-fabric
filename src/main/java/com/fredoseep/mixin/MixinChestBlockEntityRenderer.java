package com.fredoseep.mixin;

import com.fredoseep.util.SpringFestival;
import net.minecraft.client.render.block.entity.ChestBlockEntityRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChestBlockEntityRenderer.class)
public class MixinChestBlockEntityRenderer {
    @Shadow private boolean christmas;

    @Inject(method = "<init>", at = @At("RETURN"))
    public void setFestivalTheme(CallbackInfo ci){
        christmas |= SpringFestival.isSpringFestival();
    }
}
