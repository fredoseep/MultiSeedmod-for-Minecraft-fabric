package com.fredoseep.mixin;

import com.fredoseep.client.gui.SeedTypeConfigScreen;
import com.fredoseep.util.WorldCreationHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TitleScreen.class)
public abstract class MixinTitleScreen extends Screen {
    @Shadow
    @Nullable
    private String splashText;

    protected MixinTitleScreen(Text title) {
        super(title);
    }
    @Inject(method = "init", at = @At("TAIL"))
    private void changeSplashText(CallbackInfo ci) {
        this.splashText = "Without MCSR Ranked!!";
    }

    @Inject(method = "init", at = @At("RETURN"))
    private void initMultiSeed(CallbackInfo ci) {
        int x = this.width - 100;
        int y = 138;
        int size = 20;

        this.addButton(new ButtonWidget(x, y, size, size, new LiteralText(""), (button) -> {
            if(Screen.hasShiftDown()){
                this.client.openScreen(new SeedTypeConfigScreen(this));
            }
            else {
                WorldCreationHelper.createAutoWorld(
                        MinecraftClient.getInstance(),
                        (TitleScreen) (Object) this
                );
            }
        }) {
            @Override
            public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
                super.render(matrices, mouseX, mouseY, delta);
                MinecraftClient.getInstance().getItemRenderer().renderInGuiWithOverrides(new ItemStack(Items.CARROT), this.x + 2, this.y + 2);
            }
        });
    }
}