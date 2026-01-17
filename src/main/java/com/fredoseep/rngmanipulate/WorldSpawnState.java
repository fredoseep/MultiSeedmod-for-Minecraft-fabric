package com.fredoseep.rngmanipulate;

import com.fredoseep.util.CloneableState;
import com.google.common.collect.Lists;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.PersistentState;
import net.minecraft.world.World;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class WorldSpawnState extends PersistentState {
   private final AccessibleRandom entryRandom = new AccessibleRandom();
   private final long rawSeed;
   private long entrySeed;
   private final Map<BlockPos, Direction> spawnerDirections = new ConcurrentHashMap();


   public WorldSpawnState(long seed) {
      super("spawn_rng");
      this.rawSeed = seed;
      this.entrySeed = seed + 3942244525L + 2831583200L;
   }

   public static WorldSpawnState fromWorld(ServerWorld world) {
      return (WorldSpawnState)world.getPersistentStateManager().getOrCreate(() -> {
         return new WorldSpawnState(world.getSeed());
      }, "spawn_rng");
   }

   public void fromTag(CompoundTag tag) {
      this.entrySeed = tag.getLong("entrySeed");
      this.entryRandom.seed.set(this.entrySeed);
   }

   public CompoundTag toTag(CompoundTag tag) {
      tag.putLong("entrySeed", this.entrySeed);
      return tag;
   }

   public AccessibleRandom getEntryRandom() {
      return this.entryRandom;
   }

   public long getEntrySeed() {
      return this.entrySeed;
   }

   public void setEntrySeed(long entrySeed) {
      this.entrySeed = entrySeed;
   }


   public Direction getSpawnerDirection(BlockPos blockPos) {
      return (Direction)this.spawnerDirections.getOrDefault(blockPos, Direction.NORTH);
   }

   public void setSpawnerDirection(BlockPos blockPos, Direction direction) {
      this.spawnerDirections.put(blockPos, direction);
      this.markDirty();
   }

}
