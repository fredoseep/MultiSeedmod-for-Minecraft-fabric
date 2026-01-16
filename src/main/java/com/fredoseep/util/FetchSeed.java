package com.fredoseep.util;

import com.fredoseep.client.MultiSeedContext;
import com.fredoseep.config.SeedTypeConfig;
import com.fredoseep.net.SeedNetworkHandler;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletableFuture; // 记得导入这个

public class FetchSeed {
    public FetchSeed(){}

    public CompletableFuture<Void> fetchASetOfSeeds(){
        return SeedNetworkHandler.fetchSeeds(this.randAnOverworldType()).thenAccept(result -> {
            if (result.error != null) {
                System.out.println("fetch failed: " + result.error);
                return;
            }
            try {
                String overworldSeed = result.overworldSeed;
                String netherSeed = result.netherSeed;

                if (overworldSeed != null) MultiSeedContext.overworldSeed = Long.parseLong(overworldSeed);
                if (netherSeed != null) MultiSeedContext.netherSeed = Long.parseLong(netherSeed);
                if (overworldSeed != null) MultiSeedContext.endSeed = Long.parseLong(overworldSeed);

                System.out.println("[MultiSeed] 种子已更新: " + overworldSeed);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        });
    }

    public String randAnOverworldType(){
        List<String> seedTypeList = new LinkedList<>();
        if(SeedTypeConfig.getBoolean("temple"))seedTypeList.add("desert_temple");
        if(SeedTypeConfig.getBoolean("shipwreck"))seedTypeList.add("shipwreck");
        if(SeedTypeConfig.getBoolean("buried_treasure"))seedTypeList.add("buried_treasure");
        if(SeedTypeConfig.getBoolean("village"))seedTypeList.add("village");
        if(SeedTypeConfig.getBoolean("ruined_portal"))seedTypeList.add("ruined_portal");
        if(seedTypeList.isEmpty())return null;
        Random random = new Random();
        return seedTypeList.get(random.nextInt(seedTypeList.size()));
    }
}