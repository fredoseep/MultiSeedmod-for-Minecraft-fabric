package com.fredoseep.util;

import java.lang.reflect.Method;

public class StandardsettingsHelper {
    public static void tryResetSettings(){
        try {
            Class<?> resetClass = Class.forName("me.contaria.standardsettings.StandardSettings");
            Method resetMethod = resetClass.getMethod("reset");
            resetMethod.invoke(null);
        } catch (ClassNotFoundException e) {
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
