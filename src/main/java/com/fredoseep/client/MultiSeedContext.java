package com.fredoseep.client;

public class MultiSeedContext {
    // 主世界种子 (默认为 0，表示跟随原版输入框或随机)
    public static long overworldSeed = 0L;

    public static long netherSeed = 0L;
    public static long endSeed = 0L;

    public static void reset() {
        overworldSeed = 0L;
        netherSeed = 0L;
        endSeed = 0L;
    }
}