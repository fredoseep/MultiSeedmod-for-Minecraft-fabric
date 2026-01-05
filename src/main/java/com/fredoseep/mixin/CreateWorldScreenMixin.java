package com.fredoseep.mixin;

import com.fredoseep.SeedSeparateHelper;
import com.fredoseep.client.MultiSeedContext;
import com.fredoseep.client.gui.MultiSeedConfigScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.world.CreateWorldScreen;
import net.minecraft.client.gui.screen.world.MoreOptionsDialog;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.world.gen.GeneratorOptions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CreateWorldScreen.class)
public abstract class CreateWorldScreenMixin extends Screen {

    @Shadow public MoreOptionsDialog moreOptionsDialog;

    protected CreateWorldScreenMixin(Text title) { super(title); }


    @Inject(method = "<init>(Lnet/minecraft/client/gui/screen/Screen;)V", at = @At("RETURN"))
    private void onOpenScreen(Screen parent, CallbackInfo ci) {
        MultiSeedContext.reset();
    }


    @Inject(method = "init", at = @At("TAIL"))
    private void addMultiSeedButton(CallbackInfo ci) {
        this.addButton(new ButtonWidget(this.width / 2 + 110, 60, 50, 20, new LiteralText("Seeds..."), (button -> {
            this.client.openScreen(new MultiSeedConfigScreen(this));
        })));
    }

    @Redirect(
            method = "createLevel",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/screen/world/MoreOptionsDialog;getGeneratorOptions(Z)Lnet/minecraft/world/gen/GeneratorOptions;"
            )
    )
    private GeneratorOptions injectCustomSeeds(MoreOptionsDialog dialog, boolean hardcore) {
        GeneratorOptions options = dialog.getGeneratorOptions(hardcore);

        if (options instanceof SeedSeparateHelper) {
            if (MultiSeedContext.netherSeed != 0L) {
                ((SeedSeparateHelper) options).ss$setNetherSeed(MultiSeedContext.netherSeed);
            }
            if (MultiSeedContext.endSeed != 0L) {
                ((SeedSeparateHelper) options).ss$setTheEndSeed(MultiSeedContext.endSeed);
            }
        }
        return options;
    }
}