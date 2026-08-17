package com.example.client.mixin;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.util.Properties;
import net.fabricmc.loader.api.FabricLoader;

public final class OptiXMenuState {
   private static final String KEY_VANILLA_UI = "vanilla_ui";
   private static final Path CONFIG_FILE = FabricLoader.getInstance().getConfigDir().resolve("optix-game-menu.properties");
   private static boolean loaded;
   private static boolean vanillaUiEnabled;

   private OptiXMenuState() {
   }

   public static synchronized boolean isVanillaUiEnabled() {
      if (!loaded) {
         load();
      }

      return vanillaUiEnabled;
   }

   public static synchronized void setVanillaUiEnabled(boolean enabled) {
      if (!loaded) {
         load();
      }

      vanillaUiEnabled = enabled;
      save();
   }

   private static void load() {
      loaded = true;
      vanillaUiEnabled = false;
      if (Files.exists(CONFIG_FILE, new LinkOption[0])) {
         Properties properties = new Properties();

         try {
            BufferedReader reader = Files.newBufferedReader(CONFIG_FILE, StandardCharsets.UTF_8);

            try {
               properties.load(reader);
               vanillaUiEnabled = Boolean.parseBoolean(properties.getProperty("vanilla_ui", "false"));
            } catch (Throwable var5) {
               if (reader != null) {
                  try {
                     reader.close();
                  } catch (Throwable var4) {
                     var5.addSuppressed(var4);
                  }
               }

               throw var5;
            }

            if (reader != null) {
               reader.close();
            }
         } catch (Throwable var6) {
            vanillaUiEnabled = false;
         }

      }
   }

   private static void save() {
      Properties properties = new Properties();
      properties.setProperty("vanilla_ui", Boolean.toString(vanillaUiEnabled));

      try {
         Files.createDirectories(CONFIG_FILE.getParent());
      } catch (Throwable var5) {
      }

      try {
         BufferedWriter writer = Files.newBufferedWriter(CONFIG_FILE, StandardCharsets.UTF_8);

         try {
            properties.store(writer, "OptiX Game Menu State");
         } catch (Throwable var6) {
            if (writer != null) {
               try {
                  writer.close();
               } catch (Throwable var4) {
                  var6.addSuppressed(var4);
               }
            }

            throw var6;
         }

         if (writer != null) {
            writer.close();
         }
      } catch (Throwable var7) {
      }

   }
}
