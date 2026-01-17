package com.fredoseep;

import com.chocohead.mm.api.ClassTinkerers;
import net.fabricmc.loader.api.entrypoint.PreLaunchEntrypoint;
import net.minecraft.entity.ai.pathing.PathNodeType;

public class MultiSeedPreLaunch implements PreLaunchEntrypoint {
    @Override
    public void onPreLaunch() {
        // 注册 PathNodeType 枚举
        // PathNodeType 的构造函数接受一个 float 参数 (walkMalus)
        // 这里我们给它传 0.0F，或者参考 LAVA(-1.0F) / WATER(8.0F) 根据你的需求填
        ClassTinkerers.enumBuilder(String.valueOf(PathNodeType.class), float.class)
                .addEnum("BLOCK_ZOMBIEFIED_PIGLIN", 0.0F)
                .build();

        System.out.println("[MultiSeed] 成功注入 PathNodeType: BLOCK_ZOMBIEFIED_PIGLIN");
    }
}