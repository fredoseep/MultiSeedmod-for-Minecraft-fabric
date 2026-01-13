package com.fredoseep.mixin;

import com.fredoseep.client.MultiSeedContext;
import com.fredoseep.util.SpeedRunIGTHelper;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.gen.GeneratorOptions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(MinecraftServer.class)
public class MixinMinecraftServer {

    /**
     * 在创建 ServerWorld 实例时，拦截构造参数。
     * 第 8 个参数是 ChunkGenerator (地形)。
     * 第 10 个参数是 long seed (装饰)。
     */
    @ModifyArgs(
            method = "createWorlds",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/world/ServerWorld;<init>(Lnet/minecraft/server/MinecraftServer;Ljava/util/concurrent/Executor;Lnet/minecraft/world/level/storage/LevelStorage$Session;Lnet/minecraft/world/level/ServerWorldProperties;Lnet/minecraft/util/registry/RegistryKey;Lnet/minecraft/util/registry/RegistryKey;Lnet/minecraft/world/dimension/DimensionType;Lnet/minecraft/server/WorldGenerationProgressListener;Lnet/minecraft/world/gen/chunk/ChunkGenerator;ZJLjava/util/List;Z)V"
            )
    )
    public void modifyWorldArgs(Args args) {
        long overworldSeed = MultiSeedContext.overworldSeed;
        long netherSeed = MultiSeedContext.netherSeed;
        long endSeed = MultiSeedContext.endSeed;

        DimensionType dimType = args.get(6);

        if (dimType.hasSkyLight() && !dimType.hasCeiling()) {
            if (overworldSeed != 0L) {
                // 替换地形生成器
                args.set(8, GeneratorOptions.createOverworldGenerator(overworldSeed));
                // 替换种子参数
                args.set(10, overworldSeed);
            }
        }

        if (dimType.hasCeiling() && !dimType.hasSkyLight()) {
            if (netherSeed != 0L) {
                args.set(8, DimensionType.createNetherGenerator(netherSeed));
                args.set(10, netherSeed);
            }
        }

        if (dimType.hasEnderDragonFight()) {
            if (endSeed != 0L) {
                args.set(8, DimensionType.createEndGenerator(endSeed));
                args.set(10, endSeed);
            }
        }
    }
}