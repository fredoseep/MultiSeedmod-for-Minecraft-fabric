package com.fredoseep.client.gui;

import com.fredoseep.client.MultiSeedContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.TranslatableText;

import java.util.Random;

public class MultiSeedConfigScreen extends Screen {
    private final Screen parent;
    private TextFieldWidget overworldField;
    private TextFieldWidget netherField;
    private TextFieldWidget endField;


    public MultiSeedConfigScreen(Screen parent) {
        super(new LiteralText("Dimension Seeds Config"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        int midX = this.width / 2;


        this.overworldField = new TextFieldWidget(this.textRenderer, midX - 100, 50, 200, 20, new LiteralText("Overworld Seed"));
        this.netherField = new TextFieldWidget(this.textRenderer, midX - 100, 90, 200, 20, new LiteralText("Nether Seed"));
        this.endField = new TextFieldWidget(this.textRenderer, midX - 100, 130, 200, 20, new LiteralText("End Seed"));

        if (MultiSeedContext.overworldSeed != 0L) this.overworldField.setText(String.valueOf(MultiSeedContext.overworldSeed));
        if (MultiSeedContext.netherSeed != 0L) this.netherField.setText(String.valueOf(MultiSeedContext.netherSeed));
        if (MultiSeedContext.endSeed != 0L) this.endField.setText(String.valueOf(MultiSeedContext.endSeed));

        this.children.add(this.overworldField);
        this.children.add(this.netherField);
        this.children.add(this.endField);

        this.addButton(new ButtonWidget(midX - 100, 170, 200, 20, new TranslatableText("gui.done"), (button -> {
            parseAndSave();
            this.client.openScreen(this.parent);
        })));
    }


    private void parseAndSave() {
        Random random = new Random();
        long seed = random.nextLong();
        String owText = this.overworldField.getText().trim();
        if (owText.isEmpty()) {
            MultiSeedContext.overworldSeed = seed;
        } else {
            MultiSeedContext.overworldSeed = parseSeed(owText);
        }

        String netherText = this.netherField.getText().trim();
        if (netherText.isEmpty()) {
            MultiSeedContext.netherSeed = seed;
        } else {
            MultiSeedContext.netherSeed = parseSeed(netherText);
        }

        String endText = this.endField.getText().trim();
        if (endText.isEmpty()) {
            MultiSeedContext.endSeed = seed;
        } else {
            MultiSeedContext.endSeed = parseSeed(endText);
        }
    }


    /**
     * 辅助方法：像原版一样解析种子
     * 1. 如果为空 -> 返回 0 (代表未设置)
     * 2. 如果是数字 -> 返回 long
     * 3. 如果是文字 -> 返回 hashCode
     */
    private long parseSeed(String text) {
        if (text == null || text.isEmpty()) {
            return 0L; // 0L 代表“未设置/跟随主世界”
        }
        try {
            return Long.parseLong(text);
        } catch (NumberFormatException e) {
            return text.hashCode(); // 模仿原版行为：输入单词自动转 Hash
        }
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);


        drawCenteredText(matrices, this.textRenderer, this.title, this.width / 2, 20, 0xFFFFFF);


        drawStringWithShadow(matrices, this.textRenderer, "Overworld Seed", this.width / 2 - 100, 38, 0xA0A0A0);
        this.overworldField.render(matrices, mouseX, mouseY, delta);

        drawStringWithShadow(matrices, this.textRenderer, "Nether Seed", this.width / 2 - 100, 78, 0xA0A0A0);
        this.netherField.render(matrices, mouseX, mouseY, delta);

        drawStringWithShadow(matrices, this.textRenderer, "End Seed", this.width / 2 - 100, 118, 0xA0A0A0);
        this.endField.render(matrices, mouseX, mouseY, delta);

        super.render(matrices, mouseX, mouseY, delta);
    }

    @Override
    public void tick() {
        this.overworldField.tick();
        this.netherField.tick();
        this.endField.tick();
    }
}