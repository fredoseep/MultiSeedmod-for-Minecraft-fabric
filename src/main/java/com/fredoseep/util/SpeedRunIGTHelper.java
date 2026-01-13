package com.fredoseep.util;

import java.lang.reflect.Method;

public class SpeedRunIGTHelper {

    /**
     * 尝试重置 SpeedRunIGT 的计时器
     * 使用反射机制，即使没装该模组也不会报错
     */
    public static void tryResetTimer() {
        try {
Class<?> timerClass = Class.forName("com.redlimerl.speedrunigt.timer.InGameTimer");
            Method resetMethod = timerClass.getMethod("reset");
            resetMethod.invoke(null);

        } catch (ClassNotFoundException e) {
            System.out.println("[MultiSeed] 未检测到 SpeedRunIGT，跳过适配。");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}