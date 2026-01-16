package com.fredoseep.util;

import com.fredoseep.client.MultiSeedContext;
import com.fredoseep.config.SeedTypeConfig;
import com.fredoseep.net.SeedNetworkHandler;


import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class FetchSeed {
    public FetchSeed(){}

    public void fetchASetOfSeeds(){
        SeedNetworkHandler.fetchSeeds(this.randAOverworldType()).thenAccept(result -> {

            if (result.error != null) {
                System.out.println("fetch failed");
                return;
            }
            String overworldSeed = result.overworldSeed;
            String netherSeed = result.netherSeed;

            MultiSeedContext.overworldSeed = Long.parseLong(overworldSeed);
            MultiSeedContext.netherSeed = Long.parseLong(netherSeed);
            MultiSeedContext.endSeed = Long.parseLong(overworldSeed);

        });
    }

    public String randAOverworldType(){
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
