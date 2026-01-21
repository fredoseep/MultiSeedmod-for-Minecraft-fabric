package com.fredoseep.net;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.CompletableFuture;

public class SeedNetworkHandler {
    private static final String BASE_URL = "http://43.143.231.104:8001/api/v2/seed";

    private static final Gson gson = new Gson();

    public static class SeedResult {
        public String overworldSeed;
        public String netherSeed;
        public String error;
    }

    public static CompletableFuture<SeedResult> fetchSeeds(String overworldSeedTypeText,String bastionTypeText) {
        SeedResult result = new SeedResult();
        if(overworldSeedTypeText==null){
            Random random = new Random();
            result.overworldSeed = String.valueOf(random.nextLong());
            result.netherSeed = String.valueOf(random.nextLong());
            return CompletableFuture.supplyAsync(()->{return result;});
        }
        return CompletableFuture.supplyAsync(() -> {

            HttpURLConnection conn = null;

            try {
                String API_URL = BASE_URL+"?overworld="+overworldSeedTypeText+"&nether="+bastionTypeText;
                URL url = new URL(API_URL);
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setConnectTimeout(5000);
                conn.setReadTimeout(5000);

                int responseCode = conn.getResponseCode();
                if (responseCode != 200) {
                    result.error = "服务器连接失败: " + responseCode;
                    return result;
                }

                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder content = new StringBuilder();
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    content.append(inputLine);
                }
                in.close();

                JsonObject rootObject = gson.fromJson(content.toString(), JsonObject.class);

                if (rootObject.has("success") && !rootObject.get("success").getAsBoolean()) {
                    result.error = "API返回错误: " + (rootObject.has("message") ? rootObject.get("message").getAsString() : "未知错误");
                    return result;
                }

                if (rootObject.has("data") && rootObject.get("data").isJsonObject()) {
                    JsonObject seedData = rootObject.getAsJsonObject("data");

                    if (seedData.has("overworldSeed")) {
                        result.overworldSeed = seedData.get("overworldSeed").getAsString();
                    }
                    if (seedData.has("netherSeed")) {
                        result.netherSeed = seedData.get("netherSeed").getAsString();
                    }
                } else {
                    result.error = "响应格式错误: 找不到 data 对象";
                }

            } catch (Exception e) {
                e.printStackTrace();
                result.error = "发生错误: " + e.getMessage();
            } finally {
                if (conn != null) {
                    conn.disconnect();
                }
            }
            return result;
        });
    }
    //#4829364
    public static CompletableFuture<SeedResult> fetchSeeds(String matchId) {
        SeedResult result = new SeedResult();
        if(matchId.equals("")){
            Random random = new Random();
            result.overworldSeed = String.valueOf(random.nextLong());
            result.netherSeed = String.valueOf(random.nextLong());
            return CompletableFuture.supplyAsync(()->{return result;});
        }
        return CompletableFuture.supplyAsync(()->{
            HttpURLConnection conn = null;
            try {
                String API_URL = BASE_URL + "/" + matchId;
                URL url = new URL(API_URL);
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setConnectTimeout(5000);
                conn.setReadTimeout(5000);

                int responseCode = conn.getResponseCode();
                if (responseCode != 200) {
                    result.error = "服务器连接失败: " + responseCode;
                    return result;
                }

                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder content = new StringBuilder();
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    content.append(inputLine);
                }
                in.close();

                JsonObject rootObject = gson.fromJson(content.toString(), JsonObject.class);

                if (rootObject.has("success") && !rootObject.get("success").getAsBoolean()) {
                    result.error = "API返回错误: " + (rootObject.has("message") ? rootObject.get("message").getAsString() : "未知错误");
                    return result;
                }

                if (rootObject.has("data") && rootObject.get("data").isJsonObject()) {
                    JsonObject seedData = rootObject.getAsJsonObject("data");
                    if(seedData.has("seeds")&&seedData.get("seeds").isJsonObject()){
                        JsonObject seeds = seedData.getAsJsonObject("seeds");
                        if (seeds.has("overworldSeed")) {
                            result.overworldSeed = seeds.get("overworldSeed").getAsString();
                        }
                        if (seeds.has("netherSeed")) {
                            result.netherSeed = seeds.get("netherSeed").getAsString();
                        }
                    }
                    else{
                        result.error = "no seeds obj";
                    }
                } else {
                    result.error = "响应格式错误: 找不到 data 对象";
                }
            } catch (Exception e) {
            e.printStackTrace();
            result.error = "发生错误: " + e.getMessage();
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
        return result;
        });
    }

}
