
package com.fredoseep.rngmanipulate;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.PersistentState;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class WorldBlockRNGState extends PersistentState {
    private final HashMap<Type, ConcurrentHashMap<Long, Integer>> indexMap = new HashMap();

    public WorldBlockRNGState() {
        super("block_rng");

        for(Type value : Type.values()) {
            this.indexMap.put(value, new ConcurrentHashMap());
        }

    }

    public static WorldBlockRNGState fromWorld(ServerWorld world) {
        return (WorldBlockRNGState)world.getPersistentStateManager().getOrCreate(WorldBlockRNGState::new, "block_rng");
    }

    public void fromTag(CompoundTag tag) {
        for(Type value : Type.values()) {
            CompoundTag breakTag = tag.getCompound(value.name().toLowerCase());

            for(String key : breakTag.getKeys()) {
                ((ConcurrentHashMap)this.indexMap.get(value)).put(Long.parseLong(key), breakTag.getInt(key));
            }
        }

    }

    public CompoundTag toTag(CompoundTag tag) {
        for(Type value : Type.values()) {
            CompoundTag indexTag = new CompoundTag();
            for(Map.Entry<Long, Integer> entry : ((ConcurrentHashMap<Long, Integer>)this.indexMap.get(value)).entrySet()) {
                indexTag.putInt(entry.getKey().toString(), entry.getValue());
            }

            tag.put(value.name().toLowerCase(), indexTag);
        }

        return tag;
    }

    public int getAndIncreaseIndex(Type type, BlockPos blockPos) {
        int value = (Integer)((ConcurrentHashMap)this.indexMap.get(type)).getOrDefault(blockPos.asLong(), 0);
        ((ConcurrentHashMap)this.indexMap.get(type)).put(blockPos.asLong(), value + 1);
        this.markDirty();
        return value;
    }

    public static enum Type {
        BREAK_OFFSET,
        BREAK_DROP,
        EXPLODE,
        PRIME_TNT;
    }
}
