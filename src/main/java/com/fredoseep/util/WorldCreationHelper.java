package com.fredoseep.util;

import com.fredoseep.client.gui.screen.FallbackScreen;
import com.mojang.serialization.Lifecycle;
import net.minecraft.client.MinecraftClient;
import net.minecraft.resource.DataPackSettings;
import net.minecraft.util.registry.RegistryTracker;
import net.minecraft.world.Difficulty;
import net.minecraft.world.GameMode;
import net.minecraft.world.GameRules;
import net.minecraft.world.gen.GeneratorOptions;
import net.minecraft.world.level.LevelInfo;
import net.minecraft.world.level.LevelProperties;

import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

public class WorldCreationHelper {

    private static final AtomicBoolean IS_RESETTING = new AtomicBoolean(false);

    public static void createAutoWorld(MinecraftClient client, FallbackScreen fallbackScreen) {
        if (!IS_RESETTING.compareAndSet(false, true)) {
            return;
        }

        client.openScreen(fallbackScreen);
        FetchSeed fetchSeed = new FetchSeed();

        fetchSeed.fetchASetOfSeeds().thenRun(() -> {
            client.execute(() -> {
                try {
                    startWorldGen(client);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    IS_RESETTING.set(false);
                }
            });
        }).exceptionally(e -> {
            e.printStackTrace();
            IS_RESETTING.set(false);
            return null;
        });
    }

    private static void startWorldGen(MinecraftClient client) {

        StandardsettingsHelper.tryResetSettings();

        String worldName = "MultiSeed_Auto_" + System.currentTimeMillis() + "_" + new Random().nextInt(100);
        SpeedRunIGTHelper.startTimer(worldName);

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