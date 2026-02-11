package com.fredoseep.util;

import java.time.LocalDateTime;
import java.time.Month;

public class SpringFestival {
    public static boolean isSpringFestival(){
        LocalDateTime now = LocalDateTime.now();
        return now.getYear() == 2026 && ((now.getMonth() == Month.FEBRUARY && now.getDayOfMonth() >= 16 && now.getDayOfMonth() <= 27) || (now.getMonth() == Month.MARCH && now.getDayOfMonth() <= 3));
    }
}
