
package com.fredoseep.rngmanipulate.mixin.spawn;

import com.fredoseep.rngmanipulate.WorldBlockRNGState;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.TntEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import net.minecraft.world.gen.ChunkRandom;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Random;

@Mixin({TntEntity.class})
public abstract class MixinTNTEntity extends Entity {
    public MixinTNTEntity(EntityType<?> type, World world) {
        super(type, world);
    }

    @WrapOperation(
            method = {"<init>(Lnet/minecraft/world/World;DDDLnet/minecraft/entity/LivingEntity;)V"},
            at = {@At(
                    value = "FIELD",
                    target = "Lnet/minecraft/world/World;random:Ljava/util/Random;"
            )}
    )
    public Random modifyRandom(World instance, Operation<Random> original) {
        if (instance instanceof ServerWorld) {
            ChunkRandom newRand = new ChunkRandom();
            long popSeed = newRand.setPopulationSeed(((ServerWorld)instance).getSeed(), this.getBlockPos().getX(), this.getBlockPos().getZ());
            newRand.setDecoratorSeed(popSeed, 30, WorldBlockRNGState.fromWorld((ServerWorld)instance).getAndIncreaseIndex(WorldBlockRNGState.Type.PRIME_TNT, this.getBlockPos()));
            return newRand;
        } else {
            return (Random)original.call(instance);
        }
    }
}
