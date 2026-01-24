package com.fredoseep.util;

import com.fredoseep.client.MultiSeedContext;
import com.fredoseep.config.SeedTypeConfig;
import com.fredoseep.net.SeedNetworkHandler;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletableFuture; // 记得导入这个

public class FetchSeed {
    private final Random random = new Random();
    public FetchSeed(){}

    public CompletableFuture<Void> fetchASetOfSeeds(){
        return (!SeedTypeConfig.getBoolean("usematchid")?SeedNetworkHandler.fetchSeeds(this.randAnOverworldType(),this.randABastionType(),this.randABastionBiomeType(),this.randAFortressBiomeType()):SeedNetworkHandler.fetchSeeds(SeedTypeConfig.getString("matchIdText"))).thenAccept(result -> {
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

            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        });
    }

    public static String randADetailedBastionType(String bastionTypeText){
        StringBuilder detailedBastionText = new StringBuilder();
        Random random = new Random();
        switch (bastionTypeText) {
            case "bridge": {
                List<String> detailedBastionList = new ArrayList<>();
                if (SeedTypeConfig.getBoolean("bridge_single:1")) detailedBastionList.add("bastion:single:1");
                if (SeedTypeConfig.getBoolean("bridge_single:2")) detailedBastionList.add("bastion:single:2");
                if (SeedTypeConfig.getBoolean("bridge_triple:1")) detailedBastionList.add("bastion:triple:1");
                if (SeedTypeConfig.getBoolean("bridge_triple:2")) detailedBastionList.add("bastion:triple:2");
                if (!detailedBastionList.isEmpty())
                    detailedBastionText.append(",").append(detailedBastionList.get(random.nextInt(detailedBastionList.size())));
                break;
            }
            case "housing": {
                List<String> detailedBastionList = new ArrayList<>();
                if (SeedTypeConfig.getBoolean("housing_single:1")) detailedBastionList.add("bastion:single:1");
                if (SeedTypeConfig.getBoolean("housing_triple:1")) detailedBastionList.add("bastion:triple:1");
                if (SeedTypeConfig.getBoolean("housing_triple:2")) detailedBastionList.add("bastion:triple:2");
                if (!detailedBastionList.isEmpty())
                    detailedBastionText.append(",").append(detailedBastionList.get(random.nextInt(detailedBastionList.size())));
                break;
            }
            case "stables": {
                List<String> detailedBastionList = new ArrayList<>();
                if (SeedTypeConfig.getBoolean("good_gap:1")) detailedBastionList.add("bastion:good_gap:1");
                if (SeedTypeConfig.getBoolean("good_gap:2")) detailedBastionList.add("bastion:good_gap:2");
                if (SeedTypeConfig.getBoolean("stables_single:1")) detailedBastionList.add("bastion:small_single:1");
                if (SeedTypeConfig.getBoolean("stables_single:2")) detailedBastionList.add("bastion:small_single:2");
                if (SeedTypeConfig.getBoolean("stables_single:3")) detailedBastionList.add("bastion:small_single:3");
                if (SeedTypeConfig.getBoolean("stables_double:1")) detailedBastionList.add("bastion:single:1");
                if (SeedTypeConfig.getBoolean("stables_double:2")) detailedBastionList.add("bastion:single:2");
                if (SeedTypeConfig.getBoolean("stables_double:3")) detailedBastionList.add("bastion:single:3");
                if (SeedTypeConfig.getBoolean("stables_triple:1")) detailedBastionList.add("bastion:triple:1");
                if (SeedTypeConfig.getBoolean("stables_triple:2")) detailedBastionList.add("bastion:triple:2");
                if (SeedTypeConfig.getBoolean("stables_triple:3")) detailedBastionList.add("bastion:triple:3");
                if (!detailedBastionList.isEmpty())
                    detailedBastionText.append(",").append(detailedBastionList.get(random.nextInt(detailedBastionList.size())));
                break;
            }
        }
        return detailedBastionText.toString();
    }


    public static String randADetailedOverworldType(String overworldTypeText){
        StringBuilder detailedOverworldText = new StringBuilder();
        Random random = new Random();
        switch (overworldTypeText) {
            case "village":
                List<String> villageBiomeTypeList = new ArrayList<>();
                List<String> blackSmithTypeList = new ArrayList<>();
                if (SeedTypeConfig.getBoolean("desert")) villageBiomeTypeList.add("biome:structure:desert");
                if (SeedTypeConfig.getBoolean("plains")) villageBiomeTypeList.add("biome:structure:plains");
                if (SeedTypeConfig.getBoolean("savanna")) villageBiomeTypeList.add("biome:structure:savanna");
                if (SeedTypeConfig.getBoolean("snowy_tundra")) villageBiomeTypeList.add("biome:structure:snowy_tundra");
                if (SeedTypeConfig.getBoolean("taiga")) villageBiomeTypeList.add("biome:structure:taiga");
                if (!villageBiomeTypeList.isEmpty())
                    detailedOverworldText.append(villageBiomeTypeList.get(random.nextInt(villageBiomeTypeList.size())));
                if (SeedTypeConfig.getBoolean("village_diamond")) blackSmithTypeList.add("chest:structure:diamond");
                if (SeedTypeConfig.getBoolean("obsidian")) blackSmithTypeList.add("chest:structure:obsidian");
                if (!blackSmithTypeList.isEmpty())
                    detailedOverworldText.append(",").append(blackSmithTypeList.get(random.nextInt(blackSmithTypeList.size()))).append(",");
                else detailedOverworldText.append(",");
                break;
            case "temple":
                List<String> templeTypeList = new ArrayList<>();
                if (SeedTypeConfig.getBoolean("diamond")) templeTypeList.add("chest:structure:diamond");
                if (SeedTypeConfig.getBoolean("temple_egap")) templeTypeList.add("chest:structure:egap");
                if (!templeTypeList.isEmpty())
                    detailedOverworldText.append(templeTypeList.get(random.nextInt(templeTypeList.size()))).append(",");
                break;
            case "ruined_portal":
                List<String> ruinedPortalChestTypeList = new ArrayList<>();
                List<String> ruinedPortalTypeList = new ArrayList<>();
                if (SeedTypeConfig.getBoolean("rp_egap")) ruinedPortalChestTypeList.add("chest:structure:egap");
                if (SeedTypeConfig.getBoolean("golden_carrot"))
                    ruinedPortalChestTypeList.add("chest:structure:golden_carrot");
                if (SeedTypeConfig.getBoolean("looting_sword"))
                    ruinedPortalChestTypeList.add("chest:structure:looting_sword");
                if (!ruinedPortalChestTypeList.isEmpty())
                    detailedOverworldText.append(ruinedPortalChestTypeList.get(random.nextInt(ruinedPortalChestTypeList.size())));
                if (SeedTypeConfig.getBoolean("completable")) ruinedPortalTypeList.add("type:structure:completable");
                if (SeedTypeConfig.getBoolean("lava")) ruinedPortalTypeList.add("type:structure:lava");
                if (!ruinedPortalTypeList.isEmpty())
                    detailedOverworldText.append(",").append(ruinedPortalTypeList.get(random.nextInt(ruinedPortalTypeList.size()))).append(",");
                else detailedOverworldText.append(",");
                break;
            case "shipwreck":
                List<String> shipwreckChestTypeList = new ArrayList<>();
                List<String> shipwreckTypeList = new ArrayList<>();
                if (SeedTypeConfig.getBoolean("carrot")) shipwreckChestTypeList.add("chest:structure:carrot");
                if (SeedTypeConfig.getBoolean("shipwreck_diamond"))
                    shipwreckChestTypeList.add("chest:structure:diamond");
                if (SeedTypeConfig.getBoolean("normal")) shipwreckTypeList.add("type:structure:normal");
                if (SeedTypeConfig.getBoolean("sideways")) shipwreckTypeList.add("type:structure:sideways");
                if (SeedTypeConfig.getBoolean("upsidedown")) shipwreckTypeList.add("type:structure:upsidedown");
                if (!shipwreckTypeList.isEmpty())
                    detailedOverworldText.append(shipwreckTypeList.get(random.nextInt(shipwreckTypeList.size())));
                if (!shipwreckChestTypeList.isEmpty())
                    detailedOverworldText.append(",").append(shipwreckChestTypeList.get(random.nextInt(shipwreckTypeList.size()))).append(",");
                else detailedOverworldText.append(",");
                break;
        }
        return detailedOverworldText.toString();
    }


    private String randAFortressBiomeType(){
        List<String> seedTypeList = new ArrayList<>();
        if(SeedTypeConfig.getBoolean("fortress_basalt_deltas"))seedTypeList.add("biome:fortress:basalt_deltas");
        if(SeedTypeConfig.getBoolean("fortress_crimson_forest"))seedTypeList.add("biome:fortress:crimson_forest");
        if(SeedTypeConfig.getBoolean("fortress_nether_wastes"))seedTypeList.add("biome:fortress:nether_wastes");
        if(SeedTypeConfig.getBoolean("fortress_warped_forest"))seedTypeList.add("biome:fortress:warped_forest");
        if(SeedTypeConfig.getBoolean("fortress_soul_sand_valley"))seedTypeList.add("biome:fortress:soul_sand_valley");
        if(seedTypeList.isEmpty())return null;
        return seedTypeList.get(random.nextInt(seedTypeList.size()));
    }

    private String randABastionBiomeType(){
        List<String> seedTypeList = new ArrayList<>();
        if(SeedTypeConfig.getBoolean("bastion_basalt_deltas"))seedTypeList.add("biome:bastion:basalt_deltas");
        if(SeedTypeConfig.getBoolean("bastion_crimson_forest"))seedTypeList.add("biome:bastion:crimson_forest");
        if(SeedTypeConfig.getBoolean("bastion_nether_wastes"))seedTypeList.add("biome:bastion:nether_wastes");
        if(SeedTypeConfig.getBoolean("bastion_warped_forest"))seedTypeList.add("biome:bastion:warped_forest");
        if(SeedTypeConfig.getBoolean("bastion_soul_sand_valley"))seedTypeList.add("biome:bastion:soul_sand_valley");
        if(seedTypeList.isEmpty())return null;
        return seedTypeList.get(random.nextInt(seedTypeList.size()));
    }


    private String randAnOverworldType(){
        List<String> seedTypeList = new LinkedList<>();
        if(SeedTypeConfig.getBoolean("temple"))seedTypeList.add("desert_temple");
        if(SeedTypeConfig.getBoolean("shipwreck"))seedTypeList.add("shipwreck");
        if(SeedTypeConfig.getBoolean("buried_treasure"))seedTypeList.add("buried_treasure");
        if(SeedTypeConfig.getBoolean("village"))seedTypeList.add("village");
        if(SeedTypeConfig.getBoolean("ruined_portal"))seedTypeList.add("ruined_portal");
        if(seedTypeList.isEmpty())return null;
        return seedTypeList.get(random.nextInt(seedTypeList.size()));
    }
    private String randABastionType(){
        List<String> seedTypeList = new LinkedList<>();
        if(SeedTypeConfig.getBoolean("housing"))seedTypeList.add("housing");
        if(SeedTypeConfig.getBoolean("stables"))seedTypeList.add("stables");
        if(SeedTypeConfig.getBoolean("bridge"))seedTypeList.add("bridge");
        if(SeedTypeConfig.getBoolean("treasure"))seedTypeList.add("treasure");
        if(seedTypeList.isEmpty())return null;
        return seedTypeList.get(random.nextInt(seedTypeList.size()));
    }

}