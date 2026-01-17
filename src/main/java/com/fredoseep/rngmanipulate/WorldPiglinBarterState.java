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
   // === 核心常量定义 ===
   // 定义一个完整的随机周期为 72 次交易
   private static final int MAX_GUARANTEE = 72;
   // 在这 72 次中，强制包含 3 次珍珠
   private static final int MAX_PEARL_COUNT = 3;
   // 在这 72 次中，强制包含 6 次黑曜石
   private static final int MAX_OBSIDIAN_COUNT = 6;

   // 存储本轮循环中，哪几次交易应该给珍珠 (例如: 第5次, 第20次, 第66次)
   private final List<Integer> pearlTradeIndexes = Lists.newArrayList();
   // 存储本轮循环中，哪几次交易应该给黑曜石
   private final List<Integer> obsidianTradeIndexes = Lists.newArrayList();

   // 当前是本轮的第几次交易
   private int currentTrades = Integer.MAX_VALUE;
   // 防止递归调用的锁标记
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

      // 初始化 0 到 71 的列表
      for(int i = 0; i < MAX_GUARANTEE; ++i) {
         numbers.add(i);
      }

      // 打乱顺序（洗牌）
      Collections.shuffle(numbers, random);

      // 提取前 3 个数字作为珍珠的中奖位置
      for(int i = 0; i < MAX_PEARL_COUNT; ++i) {
         this.pearlTradeIndexes.add(numbers.remove(0));
      }

      // 提取接下来的 6 个数字作为黑曜石的中奖位置
      for(int i = 0; i < MAX_OBSIDIAN_COUNT; ++i) {
         this.obsidianTradeIndexes.add(numbers.remove(0));
      }
   }

   /**
    * 外部调用入口：获取经过篡改的交易物品
    */
   public ItemStack guaranteeItem(PiglinEntity piglin, ItemStack itemStack, AccessibleRandom random) {
      ItemStack newItem = this.guaranteeItem2(piglin, itemStack, random);
      // 如果不是在内部递归重随的过程中，计数器+1
      if (!this.rolling) {
         ++this.currentTrades;
      }
      return newItem;
   }

   /**
    * 内部逻辑：根据预设的洗牌结果，决定这次给什么
    */
   private ItemStack guaranteeItem2(PiglinEntity piglin, ItemStack itemStack, AccessibleRandom random) {
      // 如果当前轮次超过 72 次，开启新的一轮洗牌
      if (this.currentTrades >= MAX_GUARANTEE) {
         this.refreshTradeIndexes(random);
      }

      // === 情况 1：原版随机到了珍珠 ===
      if (itemStack.getItem() == Items.ENDER_PEARL) {
         if (this.pearlTradeIndexes.isEmpty()) {
            // 如果本轮 3 次珍珠配额已经用光了，不能再给珍珠了
            // 强制猪灵重新随机一次（rolling锁防止死循环）
            this.rolling = true;
            List<ItemStack> newBarterItem = PiglinBrainAccessor.invokeBarterItem(piglin);
            this.rolling = false;
            // 返回重随后的结果（大概率是垃圾）
            return newBarterItem.get(0);
         } else {
            // 还有配额，允许这次珍珠掉落，并消耗一次配额
            this.pearlTradeIndexes.remove(0);
            return itemStack;
         }
      }
      // === 情况 2：原版随机到了黑曜石 ===
      else if (itemStack.getItem() == Items.OBSIDIAN) {
         if (!this.obsidianTradeIndexes.isEmpty()) {
            this.obsidianTradeIndexes.remove(0);
         }
         return itemStack;
      }
      // === 情况 3：原版随机到了其他物品（杂物） ===
      else {
         // 检查当前交易次数是否命中“珍珠奖池”
         int pearlIndex = this.pearlTradeIndexes.indexOf(this.currentTrades);

         if (pearlIndex != -1) {
            // 命中珍珠！强制替换为 4-8 个珍珠
            if (!this.rolling) {
               this.pearlTradeIndexes.remove(pearlIndex);
            }
            return new ItemStack(Items.ENDER_PEARL, random.nextInt(5) + 4);
         } else {
            // 检查当前交易次数是否命中“黑曜石奖池”
            int obbyIndex = this.obsidianTradeIndexes.indexOf(this.currentTrades);

            if (obbyIndex != -1) {
               // 命中黑曜石！强制替换为 1 个黑曜石
               if (!this.rolling) {
                  this.obsidianTradeIndexes.remove(obbyIndex);
               }
               return new ItemStack(Items.OBSIDIAN);
            } else {
               // 没中奖，原版给什么就是什么
               return itemStack;
            }
         }
      }
   }

   // === NBT 数据保存与读取 (用于存读档) ===

   @Override
   public void fromTag(CompoundTag tag) {
      if (tag.contains("obsidianIndexes")) {
         // 读取黑曜石索引
         int[] obsData = tag.getIntArray("obsidianIndexes");
         for (int index : obsData) {
            this.obsidianTradeIndexes.add(index);
         }

         // 读取珍珠索引
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