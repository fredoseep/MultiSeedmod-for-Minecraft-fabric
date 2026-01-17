package com.fredoseep.util;


import net.minecraft.world.PersistentState;

public interface CloneableState<T extends PersistentState & CloneableState<T>> {
    T copy();
}
