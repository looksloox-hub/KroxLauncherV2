package com.example.client.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

public class ClientSettings {
   public static float opacity = 1.0F;
   public static String theme = "violet";
   private static final File FILE = new File("config/optix_client_settings.json");
   private static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().create();

   public static void load() {
      try {
         if (!FILE.exists()) {
            return;
         }

         FileReader reader = new FileReader(FILE);
         Data data = (Data)GSON.fromJson(reader, Data.class);
         reader.close();
         if (data != null) {
            opacity = data.opacity;
            theme = data.theme;
         }
      } catch (Exception e) {
         e.printStackTrace();
      }

   }

   public static void save() {
      try {
         FILE.getParentFile().mkdirs();
         Data data = new Data();
         data.opacity = opacity;
         data.theme = theme;
         FileWriter writer = new FileWriter(FILE);
         GSON.toJson(data, writer);
         writer.close();
      } catch (Exception e) {
         e.printStackTrace();
      }

   }

   public static void reset() {
      opacity = 1.0F;
      theme = "violet";
      save();
   }

   public static class Data {
      public float opacity = 1.0F;
      public String theme = "violet";
   }
}
