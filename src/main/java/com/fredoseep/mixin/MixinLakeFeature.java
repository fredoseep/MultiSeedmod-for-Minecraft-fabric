package com.fredoseep.mixin;

import com.google.common.collect.Maps;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import java.util.Map;
import java.util.Random;
import java.util.stream.Stream;

import net.minecraft.block.*;
import net.minecraft.block.Material;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.structure.StructureStart;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkSectionPos;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.LakeFeature;
import net.minecraft.world.gen.feature.SingleStateFeatureConfig;
import net.minecraft.world.gen.feature.StructureFeature;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({LakeFeature.class})
public class MixinLakeFeature {

    @Unique
    private Map<BlockPos, Integer> lavaCountMap;

    @Unique
    private void ensureInitialized() {
        if (this.lavaCountMap == null) {
            this.lavaCountMap = Maps.newConcurrentMap();
        }
    }

    @Unique
    private boolean betterCondition(SingleStateFeatureConfig config) {
        return config.state.getBlock() == Blocks.LAVA;
    }

    @Inject(method = "generate", at = @At("HEAD"))
    private void init(ServerWorldAccess serverWorldAccess, StructureAccessor structureAccessor, ChunkGenerator chunkGenerator, Random random, BlockPos blockPos, SingleStateFeatureConfig singleStateFeatureConfig, CallbackInfoReturnable<Boolean> cir) {
        this.ensureInitialized();
    }

    @WrapOperation(
            method = {"generate(Lnet/minecraft/world/ServerWorldAccess;Lnet/minecraft/world/gen/StructureAccessor;Lnet/minecraft/world/gen/chunk/ChunkGenerator;Ljava/util/Random;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/world/gen/feature/SingleStateFeatureConfig;)Z"},
            at = {@At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/gen/StructureAccessor;getStructuresWithChildren(Lnet/minecraft/util/math/ChunkSectionPos;Lnet/minecraft/world/gen/feature/StructureFeature;)Ljava/util/stream/Stream;"
            )}
    )
    public Stream<? extends StructureStart<?>> onCheckStructure(StructureAccessor instance, ChunkSectionPos pos, StructureFeature<?> feature, Operation<Stream<? extends StructureStart<?>>> original, @Local(argsOnly = true) SingleStateFeatureConfig featureConfig, @Local(argsOnly = true) BlockPos targetPos) {
        return !this.checkStructures(instance, targetPos) ? Stream.of(StructureStart.DEFAULT) : Stream.empty();
    }

    @WrapOperation(
            method = {"generate(Lnet/minecraft/world/ServerWorldAccess;Lnet/minecraft/world/gen/StructureAccessor;Lnet/minecraft/world/gen/chunk/ChunkGenerator;Ljava/util/Random;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/world/gen/feature/SingleStateFeatureConfig;)Z"},
            at = {@At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/block/Material;isSolid()Z",
                    ordinal = 0
            )}
    )
    public boolean onCheckSolid(Material instance, Operation<Boolean> original, @Local(argsOnly = true) SingleStateFeatureConfig featureConfig) {
        if (!this.betterCondition(featureConfig)) {
            return original.call(instance);
        } else {
            return instance != Material.AIR && !instance.isLiquid();
        }
    }

    @WrapOperation(
            method = {"generate(Lnet/minecraft/world/ServerWorldAccess;Lnet/minecraft/world/gen/StructureAccessor;Lnet/minecraft/world/gen/chunk/ChunkGenerator;Ljava/util/Random;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/world/gen/feature/SingleStateFeatureConfig;)Z"},
            at = {@At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/ServerWorldAccess;isAir(Lnet/minecraft/util/math/BlockPos;)Z"
            )}
    )
    public boolean checkPosDown(ServerWorldAccess instance, BlockPos blockPos, Operation<Boolean> original, @Local(argsOnly = true) SingleStateFeatureConfig featureConfig) {
        if (!this.betterCondition(featureConfig)) {
            return original.call(instance, blockPos);
        } else {
            BlockState blockState = instance.getBlockState(blockPos);
            Material material = blockState.getMaterial();
            if (material.isLiquid()) {
                return false;
            } else if (blockState.isAir() || material.getColor() == MaterialColor.ICE || material.getColor() == MaterialColor.FOLIAGE) {
                return true;
            } else {
                return material.isBurnable() || material.isReplaceable() || !material.blocksLight();
            }
        }
    }

    @WrapOperation(
            method = {"generate(Lnet/minecraft/world/ServerWorldAccess;Lnet/minecraft/world/gen/StructureAccessor;Lnet/minecraft/world/gen/chunk/ChunkGenerator;Ljava/util/Random;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/world/gen/feature/SingleStateFeatureConfig;)Z"},
            at = {@At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/ServerWorldAccess;setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;I)Z"
            )}
    )
    public boolean cleanUpdate(ServerWorldAccess instance, BlockPos blockPos, BlockState state, int i, Operation<Boolean> original, @Local(argsOnly = true) BlockPos startPos, @Local(argsOnly = true) SingleStateFeatureConfig featureConfig) {
        this.ensureInitialized();

        if (state.getBlock() == Blocks.CAVE_AIR && this.betterCondition(featureConfig)) {
            for(int j = 1; j < 16; ++j) {
                BlockPos airPos = blockPos.add(0, j, 0);
                BlockState originalState = instance.getBlockState(airPos);
                if (originalState.getBlock().getSoundGroup(originalState) == BlockSoundGroup.GRAVEL || originalState.isToolRequired() || originalState.getMaterial() == Material.AGGREGATE) {
                    break;
                }

                if (originalState.getMaterial().getColor() == MaterialColor.ICE || originalState.getBlock() instanceof PillarBlock || originalState.getBlock() instanceof LeavesBlock || originalState.getBlock() instanceof AbstractPlantPartBlock) {
                    instance.setBlockState(airPos, Blocks.AIR.getDefaultState(), 3);
                }
            }
        }

        Boolean result = original.call(instance, blockPos, state, i);

        if (this.betterCondition(featureConfig) && state.getBlock() == Blocks.LAVA) {
            this.lavaCountMap.put(startPos, this.lavaCountMap.getOrDefault(startPos, 0) + 1);
        }

        return result;
    }

    @Inject(
            method = {"generate(Lnet/minecraft/world/ServerWorldAccess;Lnet/minecraft/world/gen/StructureAccessor;Lnet/minecraft/world/gen/chunk/ChunkGenerator;Ljava/util/Random;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/world/gen/feature/SingleStateFeatureConfig;)Z"},
            at = {@At("RETURN")},
            cancellable = true
    )
    public void injectResult(ServerWorldAccess serverWorldAccess, StructureAccessor structureAccessor, ChunkGenerator chunkGenerator, Random random, BlockPos blockPos, SingleStateFeatureConfig singleStateFeatureConfig, CallbackInfoReturnable<Boolean> cir) {
        this.ensureInitialized();

        int placedCount = this.lavaCountMap.getOrDefault(blockPos, 0);
        this.lavaCountMap.remove(blockPos);

        if (cir.getReturnValue() && this.betterCondition(singleStateFeatureConfig)) {
            cir.setReturnValue(placedCount > 0);
        }
    }


    @Unique
    private boolean checkStructures(StructureAccessor structureAccessor, BlockPos blockPos) {
        if (this.checkStructure(structureAccessor, blockPos, StructureFeature.RUINED_PORTAL)) {
            return false;
        } else {
            return !this.checkStructure(structureAccessor, blockPos, StructureFeature.VILLAGE);
        }
    }

    @Unique
    private boolean checkStructure(StructureAccessor structureAccessor, BlockPos blockPos, StructureFeature<?> feature) {
        BlockBox poolBox = new BlockBox(blockPos.getX(), blockPos.getY() - 2, blockPos.getZ(), blockPos.getX() + 16, blockPos.getY() + 4 + 2, blockPos.getZ() + 16);
        Stream<? extends StructureStart<?>> stream = Stream.empty();

        for(int x = 0; x < 3; ++x) {
            for(int z = 0; z < 3; ++z) {
                stream = Stream.concat(stream, structureAccessor.getStructuresWithChildren(ChunkSectionPos.from(blockPos.add(16 * (x - 1), 0, 16 * (z - 1))), feature));
            }
        }

        return stream.filter((start) -> {
            return start.getBoundingBox().intersects(poolBox);
        }).anyMatch((start) -> {
            return start.getChildren().stream().anyMatch((child) -> {
                return child.getBoundingBox().intersects(poolBox);
            });
        });
    }
}