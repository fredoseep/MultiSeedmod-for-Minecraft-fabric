package com.fredoseep.util;

import java.lang.reflect.Method;

public class StandardsettingsHelper {
    public static void tryResetSettings(){
        try {
            Class<?> resetClass = Class.forName("me.contaria.standardsettings.StandardSettings");
            Method resetMethod = resetClass.getMethod("reset");
            resetMethod.invoke(null);
        } catch (ClassNotFoundException e) {
            System.out.println("[MultiSeed] 未检测到 StandardSettings，跳过适配。");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
