package com.fredoseep.client.gui.screen;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack; // 记得导入 MatrixStack
import net.minecraft.text.Text;

public class FallbackScreen extends Screen {
    public FallbackScreen(Text title) {
        super(title);
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);
        drawCenteredText(matrices, this.textRenderer, this.title, this.width / 2, this.height / 2 - 9, 0xFFFFFF);
        super.render(matrices, mouseX, mouseY, delta);
    }
}