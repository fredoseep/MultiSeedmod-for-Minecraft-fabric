package com.fredoseep.util;

import com.fredoseep.client.MultiSeedContext;
import com.fredoseep.net.SeedNetworkHandler;

public class FetchSeed {
    public FetchSeed(){}

    public void fetchASetOfSeeds(){
        SeedNetworkHandler.fetchSeeds().thenAccept(result -> {

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
}
