package com.fredoseep.util;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.stat.Stats;

public class BlindStateChecker {
    public static boolean isBlindRunReady() {
        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        if (player == null) {
            return false;
        }

        int eyesThrown = player.getStatHandler().getStat(Stats.USED.getOrCreateStat(Items.ENDER_EYE));
        if (eyesThrown > 0) {
            return false;
        }

        boolean hasEnderEye = hasItem(player, Items.ENDER_EYE);

        if (hasEnderEye) {
            return true;
        }

        boolean hasBlazeRod = hasItem(player, Items.BLAZE_ROD);
        boolean hasBlazePowder = hasItem(player, Items.BLAZE_POWDER);
        boolean hasEnderPearl = hasItem(player, Items.ENDER_PEARL);

        boolean hasMaterials = (hasBlazeRod || hasBlazePowder) && hasEnderPearl;

        return hasMaterials;
    }


    private static boolean hasItem(ClientPlayerEntity player, Item item) {
        return player.inventory.contains(new ItemStack(item));
    }
}