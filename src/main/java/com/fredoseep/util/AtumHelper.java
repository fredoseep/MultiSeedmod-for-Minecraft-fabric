package com.fredoseep.util;

import java.lang.reflect.Method;

public class AtumHelper {
    public void tryHotkeyGenerateWorld(){
        try {
            Class<?> resetClass = Class.forName("com.redlimerl.speedrunigt.timer.InGameTimer");
            Method resetMethod = resetClass.getMethod("reset");
            resetMethod.invoke(null);
        } catch (ClassNotFoundException e) {
            System.out.println("[MultiSeed] 未检测到 Atum，跳过适配。");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
