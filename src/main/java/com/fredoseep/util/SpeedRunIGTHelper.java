package com.fredoseep.util;

import com.redlimerl.speedrunigt.timer.InGameTimer;
import com.redlimerl.speedrunigt.timer.running.RunType;
import net.fabricmc.loader.api.FabricLoader;

import java.lang.reflect.Method;

public class SpeedRunIGTHelper {

    public static void startTimer(String worldName) {
        if (FabricLoader.getInstance().isModLoaded("speedrunigt")) {
            try {
                InGameTimer.start(worldName, RunType.RANDOM_SEED);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    public static void stopTimer() {
        try {
            Class<?> timerClass = Class.forName("com.redlimerl.speedrunigt.timer.InGameTimer", false, Thread.currentThread().getContextClassLoader());

            Method getInstanceMethod = timerClass.getMethod("getInstance");
            Object timerInstance = getInstanceMethod.invoke(null);

            Method completeMethod = timerClass.getMethod("complete");
            completeMethod.invoke(timerInstance);

        } catch (NoSuchMethodException e) {
            System.err.println("[MultiSeed] Failed to find complete() method in SpeedRunIGT. Trying stop() instead...");
            tryStopInstead();
        } catch (Exception e) {
            System.out.println("[MultiSeed] SpeedRunIGT not found or failed to stop: " + e.getMessage());
        }
    }

    private static void tryStopInstead() {
        try {
            Class<?> timerClass = Class.forName("com.redlimerl.speedrunigt.timer.InGameTimer", false, Thread.currentThread().getContextClassLoader());
            Method getInstanceMethod = timerClass.getMethod("getInstance");
            Object timerInstance = getInstanceMethod.invoke(null);
            Method stopMethod = timerClass.getMethod("stop"); // stop 通常是存在的
            stopMethod.invoke(timerInstance);
        } catch (Exception ignored) {
        }
    }
    /**
     * 尝试重置 SpeedRunIGT 的计时器
     */
    public static void tryResetTimer() {
        try {
Class<?> timerClass = Class.forName("com.redlimerl.speedrunigt.timer.InGameTimer");
            Method resetMethod = timerClass.getMethod("reset");
            resetMethod.invoke(null);

        } catch (ClassNotFoundException e) {
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}