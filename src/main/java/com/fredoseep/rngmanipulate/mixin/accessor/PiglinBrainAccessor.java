package com.fredoseep.rngmanipulate.mixin.accessor;

import java.util.List;

import net.minecraft.entity.mob.PiglinBrain;
import net.minecraft.entity.mob.PiglinEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin({PiglinBrain.class})
public interface PiglinBrainAccessor {
    @Invoker("getBarteredItem")
    static List<ItemStack> invokeBarterItem(PiglinEntity piglin) {
        throw new AssertionError();
    }
}
