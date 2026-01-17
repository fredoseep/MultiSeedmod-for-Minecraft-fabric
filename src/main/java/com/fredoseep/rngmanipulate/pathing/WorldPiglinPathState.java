package com.fredoseep.rngmanipulate.pathing;


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


public class WorldPiglinPathState extends PersistentState implements CloneableState<WorldPiglinPathState> {
   private final List<BlockBox> piglinPathBlocks = new CopyOnWriteArrayList();

   public static WorldPiglinPathState fromWorld(ServerWorld world) {
      return (WorldPiglinPathState)world.getPersistentStateManager().getOrCreate(WorldPiglinPathState::new, "piglin_path");
   }

   public WorldPiglinPathState() {
      super("piglin_path");
   }

   public void addBlockBox(BlockBox box) {
      this.piglinPathBlocks.add(box);
      this.markDirty();
   }

   public List<BlockBox> getPiglinPathBlocks() {
      return this.piglinPathBlocks;
   }

   public boolean containsBlockPos(BlockPos pos) {
      Iterator var2 = this.piglinPathBlocks.iterator();

      BlockBox piglinPathBlock;
      do {
         if (!var2.hasNext()) {
            return false;
         }

         piglinPathBlock = (BlockBox)var2.next();
      } while(!piglinPathBlock.contains(pos));

      return true;
   }

   public void fromTag(CompoundTag tag) {
      Iterator var2 = tag.getList("data", 10).iterator();

      while(var2.hasNext()) {
         Tag data = (Tag)var2.next();
         CompoundTag boxTag = (CompoundTag)data;
         this.piglinPathBlocks.add(new BlockBox(boxTag.getInt("minX"), boxTag.getInt("minY"), boxTag.getInt("minZ"), boxTag.getInt("maxX"), boxTag.getInt("maxY"), boxTag.getInt("maxZ")));
      }

   }

   public CompoundTag toTag(CompoundTag tag) {
      ListTag listTag = new ListTag();
      Iterator var3 = this.piglinPathBlocks.iterator();

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

   public WorldPiglinPathState copy() {
      WorldPiglinPathState state = new WorldPiglinPathState();
      state.piglinPathBlocks.addAll(this.piglinPathBlocks);
      return state;
   }
}
