package com.fredoseep.rngmanipulate;


import com.fredoseep.util.CloneableState;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.PersistentState;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;


public class WorldSpawnPreventState extends PersistentState implements CloneableState<WorldSpawnPreventState> {
   private final List<BlockBox> preventionBoxes = new CopyOnWriteArrayList();

   public static WorldSpawnPreventState fromWorld(ServerWorld world) {
      return (WorldSpawnPreventState)world.getPersistentStateManager().getOrCreate(WorldSpawnPreventState::new, "spawn_prevent");
   }

   public WorldSpawnPreventState() {
      super("spawn_prevent");
   }

   public void addBlockBox(BlockBox box) {
      this.preventionBoxes.add(box);
      this.markDirty();
   }

   public boolean containsBlockPos(BlockPos pos) {
      Iterator var2 = this.preventionBoxes.iterator();

      BlockBox piglinPathBlock;
      do {
         if (!var2.hasNext()) {
            return false;
         }

         piglinPathBlock = (BlockBox)var2.next();
      } while(!piglinPathBlock.contains(pos));

      return true;
   }

   public WorldSpawnPreventState copy() {
      WorldSpawnPreventState state = new WorldSpawnPreventState();
      state.preventionBoxes.addAll(this.preventionBoxes);
      return state;
   }

   public void fromTag(CompoundTag tag) {
      Iterator var2 = tag.getList("data", 10).iterator();

      while(var2.hasNext()) {
         Tag data = (Tag)var2.next();
         CompoundTag boxTag = (CompoundTag)data;
         this.preventionBoxes.add(new BlockBox(boxTag.getInt("minX"), boxTag.getInt("minY"), boxTag.getInt("minZ"), boxTag.getInt("maxX"), boxTag.getInt("maxY"), boxTag.getInt("maxZ")));
      }

   }

   public CompoundTag toTag(CompoundTag tag) {
      ListTag listTag = new ListTag();
      Iterator var3 = this.preventionBoxes.iterator();

      while(var3.hasNext()) {
         BlockBox box = (BlockBox)var3.next();
         CompoundTag boxTag = new CompoundTag();
         boxTag.putInt("minX", box.minX);
         boxTag.putInt("minY", box.minY);
         boxTag.putInt("minZ", box.minZ);
         boxTag.putInt("maxX", box.maxX);
         boxTag.putInt("maxY", box.maxY);
         boxTag.putInt("maxZ", box.maxZ);
         listTag.add(boxTag);
      }

      tag.put("data", listTag);
      return tag;
   }
}
