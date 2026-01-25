package com.fredoseep.client;

import com.fredoseep.config.SeedTypeConfig;

public class MultiSeedContext {
    // 主世界种子 (默认为 0，表示跟随原版输入框或随机)
    public static long overworldSeed = 0L;

    public static long netherSeed = 0L;
    public static long endSeed = 0L;

    public static long netherTime = -1L;
    public static long bastionTime = -1L;
    public static long fortressTime = -1L;
    public static long blindTime = -1L;
    public static long strongholdTime = -1L;
    public static long endTime = -1L;

    public static void print(){
        System.out.println("Timer sout debug"+netherTime+" "+bastionTime+" "+fortressTime+" "+blindTime+" "+strongholdTime+" "+endTime);
    }

    public static void reset() {
        overworldSeed = 0L;
        netherSeed = 0L;
        endSeed = 0L;

        netherTime = -1L;
        bastionTime = -1L;
        fortressTime = -1L;
        blindTime = -1L;
        strongholdTime = -1L;
        endTime = -1L;
    }
}