package com.fredoseep.rngmanipulate;


import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.PersistentState;

public class WorldPortalState extends PersistentState {
   private Vec3i spawnPos = null;
   private Vec3i actualPos = null;
   private Vec3i netherPos = null;

   public static WorldPortalState fromServer(MinecraftServer server) {
      return (WorldPortalState)server.getOverworld().getPersistentStateManager().getOrCreate(WorldPortalState::new, "portals");
   }

   public WorldPortalState() {
      super("portals");
   }

   public void fromTag(CompoundTag tag) {
      if (tag.contains("spawnX")) {
         this.setSpawnPos(new Vec3i(tag.getInt("spawnX"), tag.getInt("spawnY"), tag.getInt("spawnZ")));
      }

      if (tag.contains("actualX")) {
         this.setActualPos(new Vec3i(tag.getInt("actualX"), tag.getInt("actualY"), tag.getInt("actualZ")));
      }

      if (tag.contains("netherX")) {
         this.setNetherPos(new Vec3i(tag.getInt("netherX"), tag.getInt("netherY"), tag.getInt("netherZ")));
      }

   }

   public CompoundTag toTag(CompoundTag tag) {
      if (this.getSpawnPos() != null) {
         tag.putInt("spawnX", this.getSpawnPos().getX());
         tag.putInt("spawnY", this.getSpawnPos().getY());
         tag.putInt("spawnZ", this.getSpawnPos().getZ());
      }

      if (this.getActualPos() != null) {
         tag.putInt("actualX", this.getActualPos().getX());
         tag.putInt("actualY", this.getActualPos().getY());
         tag.putInt("actualZ", this.getActualPos().getZ());
      }

      if (this.getNetherPos() != null) {
         tag.putInt("netherX", this.getNetherPos().getX());
         tag.putInt("netherY", this.getNetherPos().getY());
         tag.putInt("netherZ", this.getNetherPos().getZ());
      }

      return tag;
   }

   public Vec3i getSpawnPos() {
      return this.spawnPos;
   }

   public void setSpawnPos(Vec3i spawnPos) {
      this.markDirty();
      this.spawnPos = spawnPos;
   }

   public Vec3i getActualPos() {
      return this.actualPos;
   }

   public void setActualPos(Vec3i actualPos) {
      this.markDirty();
      this.actualPos = actualPos;
   }

   public Vec3i getNetherPos() {
      return this.netherPos;
   }

   public void setNetherPos(Vec3i netherPos) {
      this.markDirty();
      this.netherPos = netherPos;
   }
}
