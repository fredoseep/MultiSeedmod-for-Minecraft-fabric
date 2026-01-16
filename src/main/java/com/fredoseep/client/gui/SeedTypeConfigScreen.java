package com.fredoseep.client.gui;

import com.fredoseep.config.SeedTypeConfig;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.CheckboxWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;



public class SeedTypeConfigScreen extends Screen {
    private final Screen parent;
    private CheckboxWidget villageSeed;
    private CheckboxWidget ruinedPortalSeed;
    private CheckboxWidget buriedTreasureSeed;
    private CheckboxWidget shipwreckSeed;
    private CheckboxWidget templeSeed;


    public SeedTypeConfigScreen(Screen parent) {
        super(new LiteralText("Seed Type Config"));
        this.parent = parent;
    }

    @Override
    protected void init(){
        int midX = this.width/2;
        this.villageSeed = new CheckboxWidget(midX,60,20,20,new LiteralText("village seed"), SeedTypeConfig.getBoolean("village"));
        this.ruinedPortalSeed = new CheckboxWidget(midX,80,20,20,new LiteralText("ruined portal seed"),SeedTypeConfig.getBoolean("ruined_portal"));
        this.buriedTreasureSeed = new CheckboxWidget(midX,100,20,20,new LiteralText("buried treasure seed"),SeedTypeConfig.getBoolean("buried_treasure"));
        this.shipwreckSeed = new CheckboxWidget(midX,120,20,20,new LiteralText("shipwreck seed"), SeedTypeConfig.getBoolean("shipwreck"));
        this.templeSeed = new CheckboxWidget(midX,140,20,20,new LiteralText("temple seed"),SeedTypeConfig.getBoolean("temple"));

        this.children.add(villageSeed);
        this.children.add(ruinedPortalSeed);
        this.children.add(buriedTreasureSeed);
        this.children.add(shipwreckSeed);
        this.children.add(templeSeed);

        this.addButton(new ButtonWidget(midX,180,80,20,new LiteralText("save"),(button -> {
            saveConfig();
            this.client.openScreen(this.parent);
        })));
    }

    public void saveConfig(){
        SeedTypeConfig.setBoolean("village",this.villageSeed.isChecked());
        SeedTypeConfig.setBoolean("ruined_portal",this.ruinedPortalSeed.isChecked());
        SeedTypeConfig.setBoolean("buried_treasure",this.buriedTreasureSeed.isChecked());
        SeedTypeConfig.setBoolean("shipwreck",this.shipwreckSeed.isChecked());
        SeedTypeConfig.setBoolean("temple",this.templeSeed.isChecked());
        SeedTypeConfig.save();
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta){
        this.renderBackground(matrices);

        drawCenteredText(matrices, this.textRenderer, this.title, this.width / 2, 20, 0xFFFFFF);

        drawStringWithShadow(matrices, this.textRenderer, "Overworld Seed Type", this.width / 2 - 50, 38, 0xA0A0A0);

        this.villageSeed.render(matrices,mouseX,mouseY,delta);
        this.ruinedPortalSeed.render(matrices,mouseX,mouseY,delta);
        this.buriedTreasureSeed.render(matrices,mouseX,mouseY,delta);
        this.shipwreckSeed.render(matrices,mouseX,mouseY,delta);
        this.templeSeed.render(matrices,mouseX,mouseY,delta);

        super.render(matrices,mouseX,mouseY,delta);
    }


}
