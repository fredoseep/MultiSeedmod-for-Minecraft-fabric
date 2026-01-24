package com.fredoseep.client.gui.screen;

import com.fredoseep.client.gui.DetailedType;
import com.fredoseep.config.SeedTypeConfig;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.CheckboxWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;


import java.util.ArrayList;
import java.util.List;

public class DetailConfigScreen extends Screen {
    private final Screen parent;
    public List<CheckboxWidget> checkboxWidgetList;
    private final String detailType;

    public DetailConfigScreen(Screen parent, String detailType) {
        super(new LiteralText(detailType));
        this.parent = parent;
        this.detailType = detailType;
    }

    private boolean getLegality() {
        boolean legality = false;
        switch (detailType) {
            case "village seed":
                for (CheckboxWidget checkboxWidget : checkboxWidgetList) {
                    if (checkboxWidget.getMessage().getString().equals("dessert"))
                        legality |= checkboxWidget.isChecked();
                    else if (checkboxWidget.getMessage().getString().equals("plains"))
                        legality |= checkboxWidget.isChecked();
                    else if (checkboxWidget.getMessage().getString().equals("savanna"))
                        legality |= checkboxWidget.isChecked();
                    else if (checkboxWidget.getMessage().getString().equals("snowy_tundra"))
                        legality |= checkboxWidget.isChecked();
                    else if (checkboxWidget.getMessage().getString().equals("taiga"))
                        legality |= checkboxWidget.isChecked();

                }
                break;
            case "ruined portal seed":
                for (CheckboxWidget checkboxWidget : checkboxWidgetList) {
                    if (checkboxWidget.getMessage().getString().equals("completable"))
                        legality |= checkboxWidget.isChecked();
                    else if (checkboxWidget.getMessage().getString().equals("lava"))
                        legality |= checkboxWidget.isChecked();
                }
                break;
            case "shipwreck seed":
                for (CheckboxWidget checkboxWidget : checkboxWidgetList) {
                    if (checkboxWidget.getMessage().getString().equals("normal"))
                        legality |= checkboxWidget.isChecked();
                    else if (checkboxWidget.getMessage().getString().equals("side"))
                        legality |= checkboxWidget.isChecked();
                    else if (checkboxWidget.getMessage().getString().equals("upsidedown"))
                        legality |= checkboxWidget.isChecked();
                }
                break;
            case "housing":
            case "bridge":
                for (CheckboxWidget checkboxWidget : checkboxWidgetList) {
                    legality |= checkboxWidget.isChecked();
                }
                break;
            case "stables":
                boolean tempLegalityOne = false, tempLegalityTwo = false;
                for (CheckboxWidget checkboxWidget : checkboxWidgetList) {
                    if (checkboxWidget.getMessage().getString().equals("good_gap:1"))
                        tempLegalityOne |= checkboxWidget.isChecked();
                    else if (checkboxWidget.getMessage().getString().equals("good_gap:2"))
                        tempLegalityOne |= checkboxWidget.isChecked();
                    else tempLegalityTwo |= checkboxWidget.isChecked();
                }
                legality |= (tempLegalityOne && tempLegalityTwo);
                break;
            default:
                legality = true;
        }
        return legality;
    }

    @Override
    public void init() {
        this.checkboxWidgetList = new ArrayList<>();
        if (detailType.equals("village seed")) initCheckbox(DetailedType.villageConfigList);
        if (detailType.equals("ruined portal seed")) initCheckbox(DetailedType.ruinedPortalConfigList);
        if (detailType.equals("buried treasure seed")) initCheckbox(DetailedType.buriedTreasureConfigList);
        if (detailType.equals("shipwreck seed")) initCheckbox(DetailedType.shipwreckConfigList);
        if (detailType.equals("temple seed")) initCheckbox(DetailedType.templeConfigList);

        if (detailType.equals("housing")) initCheckbox(DetailedType.housingConfigList);
        if (detailType.equals("stables")) initCheckbox(DetailedType.stablesConfigList);
        if (detailType.equals("bridge")) initCheckbox(DetailedType.bridgeConfigList);
        if (detailType.equals("treasure")) initCheckbox(DetailedType.treasureConfigList);
        this.addButton(new ButtonWidget(this.width / 2, 180, 80, 20, new LiteralText("save"), (button -> {
            if (this.getLegality()) {
                saveConfig();
                this.client.openScreen(this.parent);
            }
        })));
    }

    private void initCheckbox(List<String> checkboxTypeList) {
        int currentY = 40;
        int midX = this.width / 2;
        for (String inner : checkboxTypeList) {
            if (currentY == 180) {
                currentY = 40;
                midX += 120;
            }
            String innerToShow = inner;
            if (inner.equals("village_diamond") || inner.equals("temple_diamond") || inner.equals("shipwreck_diamond"))
                innerToShow = "diamond";
            else if (inner.equals("temple_egap") || inner.equals("rp_egap")) innerToShow = "egap";
            CheckboxWidget checkboxWidget = new CheckboxWidget(midX - 100, currentY, 20, 20, new LiteralText(innerToShow), SeedTypeConfig.getBoolean(inner));
            this.checkboxWidgetList.add(checkboxWidget);
            this.children.add(checkboxWidget);
            currentY += 20;
        }
    }

    private void saveConfig() {
        if (detailType.equals("village seed")) saveConfigHelper(DetailedType.villageConfigList);
        if (detailType.equals("ruined portal seed")) saveConfigHelper(DetailedType.ruinedPortalConfigList);
        if (detailType.equals("buried treasure seed")) saveConfigHelper(DetailedType.buriedTreasureConfigList);
        if (detailType.equals("shipwreck seed")) saveConfigHelper(DetailedType.shipwreckConfigList);
        if (detailType.equals("temple seed")) saveConfigHelper(DetailedType.templeConfigList);

        if (detailType.equals("housing")) saveConfigHelper(DetailedType.housingConfigList);
        if (detailType.equals("stables")) saveConfigHelper(DetailedType.stablesConfigList);
        if (detailType.equals("bridge")) saveConfigHelper(DetailedType.bridgeConfigList);
        if (detailType.equals("treasure")) saveConfigHelper(DetailedType.treasureConfigList);
        SeedTypeConfig.save();
    }

    private void saveConfigHelper(List<String> checkboxTypeList) {
        for (int i = 0; i < checkboxTypeList.size(); i++) {
            SeedTypeConfig.setBoolean(checkboxTypeList.get(i), this.checkboxWidgetList.get(i).isChecked());
        }

    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);
        drawCenteredText(matrices, this.textRenderer, new LiteralText(detailType + " Detailed Config"), this.width / 2, 20, 0xffffff);
        for (CheckboxWidget checkboxWidget : this.checkboxWidgetList) {
            checkboxWidget.render(matrices, mouseX, mouseY, delta);
        }
        if (this.checkboxWidgetList.isEmpty())
            drawCenteredString(matrices, this.textRenderer, "there's nothing to config for this cat", this.width / 2, this.height / 2, 0xffffff);
        if (!this.getLegality() && this.buttons.get(0).isHovered())
            drawStringWithShadow(matrices, this.textRenderer, "Illegal Config Detected", this.width / 2 + 100, 160, 0xFF0000);
        super.render(matrices, mouseX, mouseY, delta);
    }
}
