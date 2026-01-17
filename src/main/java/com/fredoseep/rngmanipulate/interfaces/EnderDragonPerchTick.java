package com.fredoseep.rngmanipulate.interfaces;

public interface EnderDragonPerchTick {
    default int ranked$getPerchTick() {
        return 0;
    }

    default int ranked$getMaxPerchTick() {
        return 0;
    }

    default void ranked$perched() {
        throw new IllegalArgumentException();
    }

    default void ranked$addPerchPossibility(float f) {
        throw new IllegalArgumentException();
    }

    default boolean ranked$isRequireForcePerch() {
        throw new IllegalArgumentException();
    }

    default void ranked$setZeroNodeArrived(boolean b) {
        throw new IllegalArgumentException();
    }

    default boolean ranked$isZeroNodeArrived() {
        throw new IllegalArgumentException();
    }
}
