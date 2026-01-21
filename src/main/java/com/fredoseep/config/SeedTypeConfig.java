package com.fredoseep.config;

import com.fredoseep.client.MultiSeedContext;
import net.fabricmc.loader.api.FabricLoader;

import java.io.*;
import java.nio.file.Path;
import java.util.Properties;

public class SeedTypeConfig {
    private static final Path CONFIG_PATH = FabricLoader.getInstance().getConfigDir().resolve("seedTypeConfig.txt");
    private static final Properties properties = new Properties();
    public static void save(){
      try (FileWriter writer = new FileWriter(CONFIG_PATH.toFile())){
          properties.store(writer,"seedTypeConfig");
      } catch (IOException e) {
          throw new RuntimeException(e);
      }
    }
    public static void load(){
        File file = CONFIG_PATH.toFile();
        if(file.exists()){
            try(FileReader reader = new FileReader(file)){
                properties.load(reader);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
    public static void setBoolean(String key,boolean value){
        properties.setProperty(key,Boolean.toString(value));
    }
    public static boolean getBoolean(String key){
        String value = properties.getProperty(key);
        if(value==null) {
            return !key.equals("usematchid");
        }
        return Boolean.parseBoolean(value);
    }
    public static void setString(String key,String value){properties.setProperty(key,value);}
    public static String getString(String key){
        String value = properties.getProperty(key);
        if(value==null)return "";
        return value;
    }

}
