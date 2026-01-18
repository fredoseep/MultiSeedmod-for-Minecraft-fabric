package com.fredoseep.rngmanipulate;

import com.fredoseep.rngmanipulate.mixin.accessor.PiglinBrainAccessor;
import com.google.common.collect.Lists;
import net.minecraft.entity.mob.PiglinEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.PersistentState;

import java.util.Collections;
import java.util.List;


public class WorldPiglinBarterState extends PersistentState {
   private static final int MAX_GUARANTEE = 72;
   private static final int MAX_PEARL_COUNT = 3;
   private static final int MAX_OBSIDIAN_COUNT = 6;
   private final List<Integer> pearlTradeIndexes = Lists.newArrayList();
   private final List<Integer> obsidianTradeIndexes = Lists.newArrayList();
   private int currentTrades = Integer.MAX_VALUE;
   private boolean rolling = false;

   public WorldPiglinBarterState() {
      super("piglin_barters");
   }

   /**
    * 核心逻辑：洗牌算法 (Shuffle Bag)
    * 在新的一轮开始时，生成 0-71 的数字，打乱顺序，然后分配奖池。
    */
   public void refreshTradeIndexes(AccessibleRandom random) {
      this.currentTrades = 0;
      List<Integer> numbers = Lists.newArrayList();

      for(int i = 0; i < MAX_GUARANTEE; ++i) {
         numbers.add(i);
      }

      Collections.shuffle(numbers, random);

      for(int i = 0; i < MAX_PEARL_COUNT; ++i) {
         this.pearlTradeIndexes.add(numbers.remove(0));
      }

      for(int i = 0; i < MAX_OBSIDIAN_COUNT; ++i) {
         this.obsidianTradeIndexes.add(numbers.remove(0));
      }
   }

   /**
    * 外部调用入口：获取经过篡改的交易物品
    */
   public ItemStack guaranteeItem(PiglinEntity piglin, ItemStack itemStack, AccessibleRandom random) {
      ItemStack newItem = this.guaranteeItem2(piglin, itemStack, random);
      if (!this.rolling) {
         ++this.currentTrades;
      }
      return newItem;
   }

   /**
    * 内部逻辑：根据预设的洗牌结果，决定这次给什么
    */
   private ItemStack guaranteeItem2(PiglinEntity piglin, ItemStack itemStack, AccessibleRandom random) {
      if (this.currentTrades >= MAX_GUARANTEE) {
         this.refreshTradeIndexes(random);
      }

      if (itemStack.getItem() == Items.ENDER_PEARL) {
         if (this.pearlTradeIndexes.isEmpty()) {
            this.rolling = true;
            List<ItemStack> newBarterItem = PiglinBrainAccessor.invokeBarterItem(piglin);
            this.rolling = false;
            return newBarterItem.get(0);
         } else {
            this.pearlTradeIndexes.remove(0);
            return itemStack;
         }
      }
      else if (itemStack.getItem() == Items.OBSIDIAN) {
         if (!this.obsidianTradeIndexes.isEmpty()) {
            this.obsidianTradeIndexes.remove(0);
         }
         return itemStack;
      }
      else {
         int pearlIndex = this.pearlTradeIndexes.indexOf(this.currentTrades);

         if (pearlIndex != -1) {
            if (!this.rolling) {
               this.pearlTradeIndexes.remove(pearlIndex);
            }
            return new ItemStack(Items.ENDER_PEARL, random.nextInt(5) + 4);
         } else {
            int obbyIndex = this.obsidianTradeIndexes.indexOf(this.currentTrades);

            if (obbyIndex != -1) {
               if (!this.rolling) {
                  this.obsidianTradeIndexes.remove(obbyIndex);
               }
               return new ItemStack(Items.OBSIDIAN);
            } else {
               return itemStack;
            }
         }
      }
   }


   @Override
   public void fromTag(CompoundTag tag) {
      if (tag.contains("obsidianIndexes")) {
         int[] obsData = tag.getIntArray("obsidianIndexes");
         for (int index : obsData) {
            this.obsidianTradeIndexes.add(index);
         }

         int[] pearlData = tag.getIntArray("pearlIndexes");
         for (int index : pearlData) {
            this.pearlTradeIndexes.add(index);
         }

         this.currentTrades = tag.getInt("currentGuarantee");
         this.rolling = tag.getBoolean("preventIncrease");
      }
   }

   @Override
   public CompoundTag toTag(CompoundTag tag) {
      tag.putIntArray("obsidianIndexes", this.obsidianTradeIndexes);
      tag.putIntArray("pearlIndexes", this.pearlTradeIndexes);
      tag.putInt("currentGuarantee", this.currentTrades);
      tag.putBoolean("preventIncrease", this.rolling);
      return tag;
   }
}