package com.fredoseep.rngmanipulate.pathing;

import com.chocohead.mm.api.ClassTinkerers;
import net.minecraft.entity.ai.pathing.PathNodeType;


public interface ZombiefiedPiglinAvoid {
   PathNodeType BLOCK_ZOMBIEFIED_PIGLIN = (PathNodeType)ClassTinkerers.getEnum(PathNodeType.class, "BLOCK_ZOMBIEFIED_PIGLIN");
}
