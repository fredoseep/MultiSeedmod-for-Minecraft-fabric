package com.fredoseep.net;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.CompletableFuture;

public class SeedNetworkHandler {
    private static final String API_URL = "http://43.143.231.104:8000/api/seed";

    private static final Gson gson = new Gson();

    public static class SeedResult {
        public String overworldSeed;
        public String netherSeed;
        public String error;
    }

    public static CompletableFuture<SeedResult> fetchSeeds() {
        return CompletableFuture.supplyAsync(() -> {
            SeedResult result = new SeedResult();
            HttpURLConnection conn = null;

            try {
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
}
