package com.fredoseep.rngmanipulate;


import com.fredoseep.SeedSeparateHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.PersistentState;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class WorldRNGState extends PersistentState {
   private final long defaultSeed;
   private final HashMap<Integer, AccessibleRandom> randomHashMap = new HashMap();
   private int eyeThrows = 0;

   public static WorldRNGState fromServer(MinecraftServer server) {
      ServerWorld serverWorld = server.getOverworld();
      WorldRNGState state = (WorldRNGState)serverWorld.getPersistentStateManager().getOrCreate(() -> {
         return new WorldRNGState( server.getWorld(World.OVERWORLD).getSeed());
      }, "rng");
      state.markDirty();
      return state;
   }

   public WorldRNGState(long seed) {
      super("rng");
      this.defaultSeed = seed;
      seed += 4262064045L;
      Type[] var3 = Type.values();
      int var4 = var3.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         Type value = var3[var5];
         AccessibleRandom random = new AccessibleRandom();
         random.seed.set(AccessibleRandom.initialScramble(seed + (long)value.ordinal()));
         this.randomHashMap.put(value.ordinal(), random);
      }

   }

   public long getDefaultSeed() {
      return this.defaultSeed;
   }

   public CompoundTag toTag(CompoundTag tag) {
      List<Long> seeds = (List)this.randomHashMap.values().stream().map(AccessibleRandom::getSeed).collect(Collectors.toList());
      tag.putLongArray("seeds", seeds);
      tag.putInt("eyeThrows", this.eyeThrows);
      return tag;
   }

   public void fromTag(CompoundTag tag) {
      long[] seeds = tag.getLongArray("seeds");

      for(int i = 0; i < seeds.length; ++i) {
         AccessibleRandom random = new AccessibleRandom();
         random.seed.set(seeds[i]);
         this.randomHashMap.put(i, random);
      }

      if (tag.contains("eyeThrows")) {
         this.eyeThrows = tag.getInt("eyeThrows");
      }

   }

   public AccessibleRandom getRandom(Type type) {
      this.markDirty();
      if (!this.randomHashMap.containsKey(type.ordinal())) {
         this.randomHashMap.put(type.ordinal(), new AccessibleRandom());
      }

      return (AccessibleRandom)this.randomHashMap.get(type.ordinal());
   }

   public int getEyeThrows() {
      return this.eyeThrows;
   }

   public void addEyeThrows() {
      ++this.eyeThrows;
      this.markDirty();
   }

   public static enum Type {
      BLAZE,
      BLAZE_SPAWN,
      MAGMA_CUBE_SPAWN,
      BARTER,
      ENDERMAN,
      FLINT,
      EYE,
      SUS_STEW,
      HOGLIN,
      FOOD_RANDOM,
      TRADE,
      DRAGON_STANDARD,
      DRAGON_PERCH,
      DRAGON_PATH,
      DRAGON_HEIGHT,
      CHICKEN,
      SHEEP,
      SHEEP_SHEARS,
      COW,
      PIG,
      ENDER_MITE,
      RAIN_WITH_THUNDER,
      SPAWN,
      PHANTOM,
      LEAVES,
      DEAD_BUSH,
      FORTRESS_SPAWN,
      BLAZE_SPAWN_COOLDOWN,
      BLAZE_SPAWN_POSITION,
      SKELETON,
      TRIDENT_DROP,
      TRIDENT_DURATION,
      SHULKER,
      VILLAGE_CURING,
      WITHER_SKELETON_L0,
      WITHER_SKELETON_L1,
      WITHER_SKELETON_L2,
      WITHER_SKELETON_L3,
      CAT_TAME,
      WOLF_TAME,
      HORSE_TAME,
      GHAST,
      SILVERFISH_SPAWN;

      // $FF: synthetic method
      private static Type[] $values() {
         return new Type[]{BLAZE, BLAZE_SPAWN, MAGMA_CUBE_SPAWN, BARTER, ENDERMAN, FLINT, EYE, SUS_STEW, HOGLIN, FOOD_RANDOM, TRADE, DRAGON_STANDARD, DRAGON_PERCH, DRAGON_PATH, DRAGON_HEIGHT, CHICKEN, SHEEP, SHEEP_SHEARS, COW, PIG, ENDER_MITE, RAIN_WITH_THUNDER, SPAWN, PHANTOM, LEAVES, DEAD_BUSH, FORTRESS_SPAWN, BLAZE_SPAWN_COOLDOWN, BLAZE_SPAWN_POSITION, SKELETON, TRIDENT_DROP, TRIDENT_DURATION, SHULKER, VILLAGE_CURING, WITHER_SKELETON_L0, WITHER_SKELETON_L1, WITHER_SKELETON_L2, WITHER_SKELETON_L3, CAT_TAME, WOLF_TAME, HORSE_TAME, GHAST};
      }
   }
}
