package com.fredoseep.mixin;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import net.minecraft.loot.LootGsons;
import net.minecraft.loot.LootManager;
import net.minecraft.loot.LootTable;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.profiler.Profiler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Mixin(value = LootManager.class, priority = 10000)
public class ForceRuinedPortalMixin {

    @Shadow private Map<Identifier, LootTable> tables;

    @Inject(method = "apply", at = @At("RETURN"))
    private void forceInjectRuinedPortal(Map<Identifier, JsonElement> map, ResourceManager resourceManager, Profiler profiler, CallbackInfo ci) {
        try {

            InputStream stream = getClass().getResourceAsStream("/data/minecraft/loot_tables/chests/ruined_portal.json");

            if (stream == null) {
                System.out.println("cannot find loot table file");
                return;
            }
            Gson gson = LootGsons.getTableGsonBuilder().create();

            LootTable customTable = gson.fromJson(new InputStreamReader(stream, StandardCharsets.UTF_8), LootTable.class);

            Identifier targetId = new Identifier("minecraft", "chests/ruined_portal");
            this.tables.put(targetId, customTable);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}