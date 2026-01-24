package com.fredoseep.client.gui.widget;

import com.fredoseep.client.gui.screen.DetailConfigScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.CheckboxWidget;
import net.minecraft.text.Text;
import org.lwjgl.system.CallbackI;

public class ConfigCheckbox extends CheckboxWidget {
    private final Screen parent;
    private final String text;
    public ConfigCheckbox(int x, int y, int width, int height, Text text, boolean checked,Screen parent){
        super(x,y,width,height,text,checked);
        this.parent = parent;
        this.text = text.getString();
    }

    @Override
    public void onPress(){
        if(Screen.hasShiftDown())this.parent.client.openScreen(new DetailConfigScreen(parent,this.text));
        else super.onPress();

    }

}
