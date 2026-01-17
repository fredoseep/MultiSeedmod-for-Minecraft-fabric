package com.fredoseep.rngmanipulate;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class AccessibleRandom extends Random {
   private static final long multiplier = 25214903917L;
   private static final long addend = 11L;
   private static final long mask = 281474976710655L;
   public final AtomicLong seed = new AtomicLong((new Random()).nextLong());

   protected int next(int bits) {
      AtomicLong seed = this.seed;

      long oldSeed;
      long nextSeed;
      do {
         oldSeed = seed.get();
         nextSeed = oldSeed * 25214903917L + 11L & 281474976710655L;
      } while(!seed.compareAndSet(oldSeed, nextSeed));

      return (int)(nextSeed >>> 48 - bits);
   }

   public long getSeed() {
      return this.seed.get();
   }

   public static long initialScramble(long seed) {
      return (seed ^ 25214903917L) & 281474976710655L;
   }
}
