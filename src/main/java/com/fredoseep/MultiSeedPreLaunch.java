package com.fredoseep;

import com.chocohead.mm.api.ClassTinkerers;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.MappingResolver;

/**
 * 负责在游戏启动的最早期（Mixin应用之前）注册新的枚举值。
 * 实现 Runnable 接口是为了配合 "mm:early_risers" 入口点。
 */
public class MultiSeedPreLaunch implements Runnable {

    @Override
    public void run() {

        MappingResolver resolver = FabricLoader.getInstance().getMappingResolver();

        String pathNodeTypeName = resolver.mapClassName("intermediary", "net.minecraft.class_7");

        ClassTinkerers.enumBuilder(pathNodeTypeName, float.class)
                .addEnum("BLOCK_ZOMBIEFIED_PIGLIN", 0.0F) // 添加枚举：名称，参数值
                .build();

    }
}