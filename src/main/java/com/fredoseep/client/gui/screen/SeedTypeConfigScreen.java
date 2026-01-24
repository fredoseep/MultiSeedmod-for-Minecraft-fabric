package com.fredoseep.client.gui.screen;

import com.fredoseep.client.gui.widget.ConfigCheckbox;
import com.fredoseep.config.SeedTypeConfig;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.CheckboxWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;

import java.util.HashMap;
import java.util.Map;


public class SeedTypeConfigScreen extends Screen {
    private final Screen parent;
    private ConfigCheckbox villageSeed;
    private ConfigCheckbox ruinedPortalSeed;
    private ConfigCheckbox buriedTreasureSeed;
    private ConfigCheckbox shipwreckSeed;
    private ConfigCheckbox templeSeed;
    private ConfigCheckbox housing;
    private ConfigCheckbox stables;
    private ConfigCheckbox bridge;
    private ConfigCheckbox treasure;
    private CheckboxWidget useMatchId;
    private TextFieldWidget matchIdText;
    private CheckboxWidget bastionBasaltDeltas;
    private CheckboxWidget bastionCrimsonForest;
    private CheckboxWidget bastionNetherWastes;
    private CheckboxWidget bastionSoulSandValley;
    private CheckboxWidget bastionWarpedForest;
    private CheckboxWidget fortressBasaltDeltas;
    private CheckboxWidget fortressCrimsonForest;
    private CheckboxWidget fortressNetherWastes;
    private CheckboxWidget fortressSoulSandValley;
    private CheckboxWidget fortressWarpedForest;
    private int scrolledOffset;
    private Map<CheckboxWidget, Integer> widgetDefaultYPosition;



    public SeedTypeConfigScreen(Screen parent) {
        super(new LiteralText("Seed Type Config"));
        this.parent = parent;
    }

    private boolean getLegality() {
        return  (this.villageSeed.isChecked() || this.ruinedPortalSeed.isChecked() || this.templeSeed.isChecked() || this.buriedTreasureSeed.isChecked() || this.shipwreckSeed.isChecked()) && (this.housing.isChecked() || this.stables.isChecked() || this.bridge.isChecked() || this.treasure.isChecked()) && (this.bastionBasaltDeltas.isChecked() || this.bastionWarpedForest.isChecked() || this.bastionCrimsonForest.isChecked() || bastionNetherWastes.isChecked() || this.bastionSoulSandValley.isChecked()) && (this.fortressWarpedForest.isChecked() || this.fortressNetherWastes.isChecked() || this.fortressCrimsonForest.isChecked() || this.fortressBasaltDeltas.isChecked() || this.fortressSoulSandValley.isChecked());

    }

    @Override
    protected void init() {
        int midX = this.width / 2;

        this.scrolledOffset = 0;

        this.widgetDefaultYPosition = new HashMap<>();

        this.useMatchId = new CheckboxWidget(midX - 50, 40, 20, 20, new LiteralText("use match id to generate a seed"), SeedTypeConfig.getBoolean("usematchid"));
        this.matchIdText = new TextFieldWidget(this.textRenderer, midX - 80, 70, 200, 20, new LiteralText("matchIdText"));
        matchIdText.setText(SeedTypeConfig.getString("matchIdText"));

        this.villageSeed = new ConfigCheckbox(30, 80, 20, 20, new LiteralText("village seed"), SeedTypeConfig.getBoolean("village"), this);
        this.ruinedPortalSeed = new ConfigCheckbox(30, 100, 20, 20, new LiteralText("ruined portal seed"), SeedTypeConfig.getBoolean("ruined_portal"), this);
        this.buriedTreasureSeed = new ConfigCheckbox(30, 120, 20, 20, new LiteralText("buried treasure seed"), SeedTypeConfig.getBoolean("buried_treasure"), this);
        this.shipwreckSeed = new ConfigCheckbox(30, 140, 20, 20, new LiteralText("shipwreck seed"), SeedTypeConfig.getBoolean("shipwreck"), this);
        this.templeSeed = new ConfigCheckbox(30, 160, 20, 20, new LiteralText("temple seed"), SeedTypeConfig.getBoolean("temple"), this);

        this.housing = new ConfigCheckbox(230, 80, 20, 20, new LiteralText("housing"), SeedTypeConfig.getBoolean("housing"), this);
        this.stables = new ConfigCheckbox(230, 100, 20, 20, new LiteralText("stables"), SeedTypeConfig.getBoolean("stables"), this);
        this.bridge = new ConfigCheckbox(230, 120, 20, 20, new LiteralText("bridge"), SeedTypeConfig.getBoolean("bridge"), this);
        this.treasure = new ConfigCheckbox(230, 140, 20, 20, new LiteralText("treasure"), SeedTypeConfig.getBoolean("treasure"), this);

        this.bastionBasaltDeltas = new CheckboxWidget(30, 220, 20, 20, new LiteralText("basalt deltas"), SeedTypeConfig.getBoolean("bastion_basalt_deltas"));
        this.bastionCrimsonForest = new CheckboxWidget(30, 240, 20, 20, new LiteralText("crimson forest"), SeedTypeConfig.getBoolean("bastion_crimson_forest"));
        this.bastionNetherWastes = new CheckboxWidget(30, 260, 20, 20, new LiteralText("nether wastes"), SeedTypeConfig.getBoolean("bastion_nether_wastes"));
        this.bastionWarpedForest = new CheckboxWidget(30, 280, 20, 20, new LiteralText("warped forest"), SeedTypeConfig.getBoolean("bastion_warped_forest"));
        this.bastionSoulSandValley = new CheckboxWidget(30, 300, 20, 20, new LiteralText("soul sand valley"), SeedTypeConfig.getBoolean("bastion_soul_sand_valley"));

        this.fortressBasaltDeltas = new CheckboxWidget(230, 220, 20, 20, new LiteralText("basalt deltas"), SeedTypeConfig.getBoolean("fortress_basalt_deltas"));
        this.fortressCrimsonForest = new CheckboxWidget(230, 240, 20, 20, new LiteralText("crimson forest"), SeedTypeConfig.getBoolean("fortress_crimson_forest"));
        this.fortressNetherWastes = new CheckboxWidget(230, 260, 20, 20, new LiteralText("nether wastes"), SeedTypeConfig.getBoolean("fortress_nether_wastes"));
        this.fortressWarpedForest = new CheckboxWidget(230, 280, 20, 20, new LiteralText("warped forest"), SeedTypeConfig.getBoolean("fortress_warped_forest"));
        this.fortressSoulSandValley = new CheckboxWidget(230, 300, 20, 20, new LiteralText("soul sand valley"), SeedTypeConfig.getBoolean("fortress_soul_sand_valley"));




        this.widgetDefaultYPosition.put(villageSeed, 80);
        this.widgetDefaultYPosition.put(ruinedPortalSeed, 100);
        this.widgetDefaultYPosition.put(buriedTreasureSeed, 120);
        this.widgetDefaultYPosition.put(shipwreckSeed, 140);
        this.widgetDefaultYPosition.put(templeSeed, 160);
        this.widgetDefaultYPosition.put(housing, 80);
        this.widgetDefaultYPosition.put(stables, 100);
        this.widgetDefaultYPosition.put(bridge, 120);
        this.widgetDefaultYPosition.put(treasure, 140);
        this.widgetDefaultYPosition.put(bastionBasaltDeltas, 220);
        this.widgetDefaultYPosition.put(bastionCrimsonForest, 240);
        this.widgetDefaultYPosition.put(bastionNetherWastes, 260);
        this.widgetDefaultYPosition.put(bastionWarpedForest, 280);
        this.widgetDefaultYPosition.put(bastionSoulSandValley, 300);
        this.widgetDefaultYPosition.put(fortressBasaltDeltas, 220);
        this.widgetDefaultYPosition.put(fortressCrimsonForest, 240);
        this.widgetDefaultYPosition.put(fortressNetherWastes, 260);
        this.widgetDefaultYPosition.put(fortressWarpedForest, 280);
        this.widgetDefaultYPosition.put(fortressSoulSandValley, 300);

        this.children.add(useMatchId);
        this.children.add(matchIdText);

        this.children.add(villageSeed);
        this.children.add(ruinedPortalSeed);
        this.children.add(buriedTreasureSeed);
        this.children.add(shipwreckSeed);
        this.children.add(templeSeed);

        this.children.add(housing);
        this.children.add(stables);
        this.children.add(bridge);
        this.children.add(treasure);

        this.children.add(bastionBasaltDeltas);
        this.children.add(bastionCrimsonForest);
        this.children.add(bastionNetherWastes);
        this.children.add(bastionWarpedForest);
        this.children.add(bastionSoulSandValley);
        this.children.add(fortressBasaltDeltas);
        this.children.add(fortressCrimsonForest);
        this.children.add(fortressNetherWastes);
        this.children.add(fortressWarpedForest);
        this.children.add(fortressSoulSandValley);
        this.addButton(new ButtonWidget(midX + 120, 180, 80, 20, new LiteralText("save"), (button -> {
            if (this.getLegality()) {
                saveConfig();
                this.client.openScreen(this.parent);
            }

        })));
    }

    public void saveConfig() {
        SeedTypeConfig.setBoolean("usematchid", this.useMatchId.isChecked());
        if (this.matchIdText.getText() == null) SeedTypeConfig.setString("matchIdText", "");
        else SeedTypeConfig.setString("matchIdText", this.matchIdText.getText());

        SeedTypeConfig.setBoolean("village", this.villageSeed.isChecked());
        SeedTypeConfig.setBoolean("ruined_portal", this.ruinedPortalSeed.isChecked());
        SeedTypeConfig.setBoolean("buried_treasure", this.buriedTreasureSeed.isChecked());
        SeedTypeConfig.setBoolean("shipwreck", this.shipwreckSeed.isChecked());
        SeedTypeConfig.setBoolean("temple", this.templeSeed.isChecked());

        SeedTypeConfig.setBoolean("housing", this.housing.isChecked());
        SeedTypeConfig.setBoolean("stables", this.stables.isChecked());
        SeedTypeConfig.setBoolean("bridge", this.bridge.isChecked());
        SeedTypeConfig.setBoolean("treasure", this.treasure.isChecked());

        SeedTypeConfig.setBoolean("bastion_basalt_deltas", this.bastionBasaltDeltas.isChecked());
        SeedTypeConfig.setBoolean("bastion_crimson_forest", this.bastionCrimsonForest.isChecked());
        SeedTypeConfig.setBoolean("bastion_nether_wastes", this.bastionCrimsonForest.isChecked());
        SeedTypeConfig.setBoolean("bastion_warped_forest", this.bastionWarpedForest.isChecked());
        SeedTypeConfig.setBoolean("bastion_soul_sand_valley", this.bastionSoulSandValley.isChecked());

        SeedTypeConfig.setBoolean("fortress_basalt_deltas", this.fortressBasaltDeltas.isChecked());
        SeedTypeConfig.setBoolean("fortress_crimson_forest", this.fortressCrimsonForest.isChecked());
        SeedTypeConfig.setBoolean("fortress_nether_wastes", this.fortressNetherWastes.isChecked());
        SeedTypeConfig.setBoolean("fortress_warped_forest", this.fortressWarpedForest.isChecked());
        SeedTypeConfig.setBoolean("fortress_soul_sand_valley", this.fortressSoulSandValley.isChecked());

        SeedTypeConfig.save();
    }

    private void setActStatus(boolean status) {
        this.villageSeed.active = status;
        this.ruinedPortalSeed.active = status;
        this.buriedTreasureSeed.active = status;
        this.shipwreckSeed.active = status;
        this.templeSeed.active = status;

        this.housing.active = status;
        this.treasure.active = status;
        this.bridge.active = status;
        this.stables.active = status;

        this.bastionBasaltDeltas.active = status;
        this.bastionCrimsonForest.active = status;
        this.bastionNetherWastes.active = status;
        this.bastionWarpedForest.active = status;
        this.bastionSoulSandValley.active = status;

        this.fortressBasaltDeltas.active = status;
        this.fortressCrimsonForest.active = status;
        this.fortressNetherWastes.active = status;
        this.fortressWarpedForest.active = status;
        this.fortressSoulSandValley.active = status;
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
        int currentOffset = (int) (this.scrolledOffset + amount * 20);
        if (currentOffset < 20 && currentOffset >= -100) this.scrolledOffset = currentOffset;
        return true;
    }

    private void scrollYPosition() {
        this.villageSeed.y = this.widgetDefaultYPosition.get(villageSeed) + this.scrolledOffset;
        this.ruinedPortalSeed.y = this.widgetDefaultYPosition.get(ruinedPortalSeed) + this.scrolledOffset;
        this.shipwreckSeed.y = this.widgetDefaultYPosition.get(shipwreckSeed) + this.scrolledOffset;
        this.buriedTreasureSeed.y = this.widgetDefaultYPosition.get(buriedTreasureSeed) + this.scrolledOffset;
        this.templeSeed.y = this.widgetDefaultYPosition.get(templeSeed) + this.scrolledOffset;
        this.housing.y = this.widgetDefaultYPosition.get(housing) + this.scrolledOffset;
        this.stables.y = this.widgetDefaultYPosition.get(stables) + this.scrolledOffset;
        this.treasure.y = this.widgetDefaultYPosition.get(treasure) + this.scrolledOffset;
        this.bridge.y = this.widgetDefaultYPosition.get(bridge) + this.scrolledOffset;

        this.bastionBasaltDeltas.y = this.widgetDefaultYPosition.get(bastionBasaltDeltas) + this.scrolledOffset;
        this.bastionCrimsonForest.y = this.widgetDefaultYPosition.get(bastionCrimsonForest) + this.scrolledOffset;
        this.bastionNetherWastes.y = this.widgetDefaultYPosition.get(bastionNetherWastes) + this.scrolledOffset;
        this.bastionWarpedForest.y = this.widgetDefaultYPosition.get(bastionWarpedForest) + this.scrolledOffset;
        this.bastionSoulSandValley.y = this.widgetDefaultYPosition.get(bastionSoulSandValley) + this.scrolledOffset;

        this.fortressBasaltDeltas.y = this.widgetDefaultYPosition.get(fortressBasaltDeltas) + this.scrolledOffset;
        this.fortressCrimsonForest.y = this.widgetDefaultYPosition.get(fortressCrimsonForest) + this.scrolledOffset;
        this.fortressNetherWastes.y = this.widgetDefaultYPosition.get(fortressNetherWastes) + this.scrolledOffset;
        this.fortressWarpedForest.y = this.widgetDefaultYPosition.get(fortressWarpedForest) + this.scrolledOffset;
        this.fortressSoulSandValley.y = this.widgetDefaultYPosition.get(fortressSoulSandValley) + this.scrolledOffset;
    }


    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);

        drawCenteredText(matrices, this.textRenderer, this.title, this.width / 2, 20, 0xFFFFFF);


        this.useMatchId.render(matrices, mouseX, mouseY, delta);

        this.setActStatus(!this.useMatchId.isChecked());

        scrollYPosition();
        if (!this.useMatchId.isChecked()) {
            int UPPER_BORDER = 60;
            if (this.widgetDefaultYPosition.get(villageSeed) + this.scrolledOffset > UPPER_BORDER) {
                this.villageSeed.active = true;
                this.villageSeed.render(matrices, mouseX, mouseY, delta);
            } else this.villageSeed.active = false;
            if (this.widgetDefaultYPosition.get(ruinedPortalSeed) + this.scrolledOffset > UPPER_BORDER) {
                this.ruinedPortalSeed.active = true;
                this.ruinedPortalSeed.render(matrices, mouseX, mouseY, delta);
            } else this.ruinedPortalSeed.active = false;
            if (this.widgetDefaultYPosition.get(buriedTreasureSeed) + this.scrolledOffset > UPPER_BORDER) {
                this.buriedTreasureSeed.active = true;
                this.buriedTreasureSeed.render(matrices, mouseX, mouseY, delta);
            } else this.buriedTreasureSeed.active = false;
            if (this.widgetDefaultYPosition.get(shipwreckSeed) + this.scrolledOffset > UPPER_BORDER) {
                this.shipwreckSeed.active = true;
                this.shipwreckSeed.render(matrices, mouseX, mouseY, delta);
            } else this.shipwreckSeed.active = false;
            if (this.widgetDefaultYPosition.get(templeSeed) + this.scrolledOffset > UPPER_BORDER) {
                this.templeSeed.active = true;
                this.templeSeed.render(matrices, mouseX, mouseY, delta);
            } else this.templeSeed.active = false;

            if (this.widgetDefaultYPosition.get(housing) + this.scrolledOffset > UPPER_BORDER) {
                this.housing.active = true;
                this.housing.render(matrices, mouseX, mouseY, delta);
            } else this.housing.active = false;
            if (this.widgetDefaultYPosition.get(stables) + this.scrolledOffset > UPPER_BORDER) {
                this.stables.active = true;
                this.stables.render(matrices, mouseX, mouseY, delta);
            } else this.stables.active = false;
            if (this.widgetDefaultYPosition.get(bridge) + this.scrolledOffset > UPPER_BORDER) {
                this.bridge.active = true;
                this.bridge.render(matrices, mouseX, mouseY, delta);
            } else this.bridge.active = false;
            if (this.widgetDefaultYPosition.get(treasure) + this.scrolledOffset > UPPER_BORDER) {
                this.treasure.active = true;
                this.treasure.render(matrices, mouseX, mouseY, delta);
            } else this.treasure.active = false;
            if (68 + this.scrolledOffset > UPPER_BORDER)
                drawStringWithShadow(matrices, this.textRenderer, "Overworld Seed Type", 30, 68 + this.scrolledOffset, 0xA0A0A0);
            if (68 + this.scrolledOffset > UPPER_BORDER)
                drawStringWithShadow(matrices, this.textRenderer, "Bastion Type", 230, 68 + this.scrolledOffset, 0xA0A0A0);

            this.bastionBasaltDeltas.render(matrices, mouseX, mouseY, delta);
            this.bastionCrimsonForest.render(matrices, mouseX, mouseY, delta);
            this.bastionNetherWastes.render(matrices, mouseX, mouseY, delta);
            this.bastionWarpedForest.render(matrices, mouseX, mouseY, delta);
            this.bastionSoulSandValley.render(matrices, mouseX, mouseY, delta);

            this.fortressBasaltDeltas.render(matrices, mouseX, mouseY, delta);
            this.fortressCrimsonForest.render(matrices, mouseX, mouseY, delta);
            this.fortressNetherWastes.render(matrices, mouseX, mouseY, delta);
            this.fortressWarpedForest.render(matrices, mouseX, mouseY, delta);
            this.fortressSoulSandValley.render(matrices, mouseX, mouseY, delta);

            drawStringWithShadow(matrices, this.textRenderer, "Bastion Biome", 30, 208 + this.scrolledOffset, 0xffffff);
            drawStringWithShadow(matrices, this.textRenderer, "Fortress Biome", 230, 208 + this.scrolledOffset, 0xffffff);
        } else {
            drawStringWithShadow(matrices, this.textRenderer, "Match ID:", 80, 75, 0xFFFFFF);
            this.matchIdText.render(matrices, mouseX, mouseY, delta);
        }
        if (buttons.get(0).isHovered()&&!this.getLegality())
            drawStringWithShadow(matrices, this.textRenderer, "Illegal Config Detected", this.width / 2 + 100, 160, 0xFF0000);
        super.render(matrices, mouseX, mouseY, delta);
    }


}
