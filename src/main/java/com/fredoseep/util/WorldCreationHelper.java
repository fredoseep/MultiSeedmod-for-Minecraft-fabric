package com.fredoseep.util;

import com.mojang.serialization.Lifecycle;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.resource.DataPackSettings;
import net.minecraft.util.registry.RegistryTracker;
import net.minecraft.world.Difficulty;
import net.minecraft.world.GameMode;
import net.minecraft.world.GameRules;
import net.minecraft.world.gen.GeneratorOptions;
import net.minecraft.world.level.LevelInfo;
import net.minecraft.world.level.LevelProperties;

import java.util.Random;

public class WorldCreationHelper {

    public static void createAutoWorld(MinecraftClient client, Screen fallbackScreen) {
        FetchSeed fetchSeed = new FetchSeed();
        fetchSeed.fetchASetOfSeeds().thenRun(() -> {
            client.execute(() -> {
                startWorldGen(client);
            });
        });
    }

    private static void startWorldGen(MinecraftClient client) {
        SpeedRunIGTHelper.tryResetTimer();
        StandardsettingsHelper.tryResetSettings();

        String worldName = "MultiSeed_Auto_" + System.currentTimeMillis() + "_" + new Random().nextInt(100);

        LevelInfo levelInfo = new LevelInfo(
                worldName,
                GameMode.SURVIVAL,
                false,
                Difficulty.EASY,
                false,
                new GameRules(),
                DataPackSettings.SAFE_MODE
        );

        RegistryTracker.Modifiable registryTracker = RegistryTracker.create();
        GeneratorOptions generatorOptions = GeneratorOptions.getDefaultOptions();

        try {
            client.startIntegratedServer(
                    worldName,
                    registryTracker,
                    (session) -> DataPackSettings.SAFE_MODE,
                    (session, registry, resourceManager, dataPackSettings) ->
                            new LevelProperties(levelInfo, generatorOptions, Lifecycle.stable()),
                    false,
                    MinecraftClient.WorldLoadAction.NONE
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}