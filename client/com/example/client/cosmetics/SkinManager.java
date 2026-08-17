package com.example.client.cosmetics;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.Consumer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.class_1011;
import net.minecraft.class_1043;
import net.minecraft.class_12079;
import net.minecraft.class_2960;
import net.minecraft.class_310;
import net.minecraft.class_7920;
import net.minecraft.class_8685;

public final class SkinManager {
   private static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().create();
   private static final class_310 MC = class_310.method_1551();
   private static final Path GAME_DIR = FabricLoader.getInstance().getGameDir();
   private static final Path OPTIX_DIR;
   private static final Path IMPORTED_DIR;
   private static final Path DOWNLOADED_DIR;
   private static final Path CONFIG_FILE;
   private static final Map<String, class_1043> TEXTURE_CACHE;
   private static final List<SkinEntry> IMPORTED;
   private static SkinEntry selectedSkin;
   private static String selectedSkinName;
   private static class_8685 currentSkinTextures;

   private SkinManager() {
   }

   public static class_8685 getCurrentSkinTextures() {
      return currentSkinTextures;
   }

   public static void debugSkin() {
   }

   public static void init() {
      try {
         Files.createDirectories(IMPORTED_DIR);
         Files.createDirectories(DOWNLOADED_DIR);
         Files.createDirectories(OPTIX_DIR);
      } catch (IOException e) {
         throw new RuntimeException("Failed to create skin folders", e);
      }

      loadConfig();
      refreshImportedSkins();
      applySelectedFromConfig();
   }

   public static void refreshImportedSkins() {
      IMPORTED.clear();
      Map<String, SkinEntry> byName = new LinkedHashMap();
      scanFolder(IMPORTED_DIR, SkinManager.SkinSource.IMPORTED, byName);
      scanFolder(DOWNLOADED_DIR, SkinManager.SkinSource.DOWNLOADED, byName);
      IMPORTED.addAll(byName.values());
      IMPORTED.sort(Comparator.comparing((a) -> a.name.toLowerCase(Locale.ROOT)));
   }

   private static void scanFolder(Path folder, SkinSource source, Map<String, SkinEntry> byName) {
      if (Files.exists(folder, new LinkOption[0])) {
         try {
            DirectoryStream<Path> stream = Files.newDirectoryStream(folder, "*.png");

            try {
               for(Path path : stream) {
                  String fileName = path.getFileName().toString();
                  SkinEntry entry = new SkinEntry(fileName, path, source);
                  if (!byName.containsKey(fileName)) {
                     byName.put(fileName, entry);
                  }
               }
            } catch (Throwable var9) {
               if (stream != null) {
                  try {
                     stream.close();
                  } catch (Throwable var8) {
                     var9.addSuppressed(var8);
                  }
               }

               throw var9;
            }

            if (stream != null) {
               stream.close();
            }
         } catch (IOException var10) {
         }

      }
   }

   public static List<SkinEntry> getImportedSkins() {
      return Collections.unmodifiableList(IMPORTED);
   }

   public static SkinEntry getSelectedSkin() {
      return selectedSkin;
   }

   public static boolean isSelected(SkinEntry entry) {
      return entry != null && selectedSkin != null && entry.name.equals(selectedSkin.name);
   }

   public static class_2960 getCurrentTexture() {
      return selectedSkin != null ? selectedSkin.textureId : null;
   }

   private static void rebuildCurrentSkinTextures(SkinEntry entry) {
      class_12079.class_12080 asset = new class_12079.class_12080(entry.textureId, entry.path.toUri().toString());
      currentSkinTextures = class_8685.method_74884(asset, (class_12079.class_12081)null, (class_12079.class_12081)null, class_7920.field_41123);
   }

   public static void applySkin(SkinEntry entry) {
      if (entry != null) {
         try {
            loadTexture(entry);
            selectedSkin = entry;
            selectedSkinName = entry.name;
            rebuildCurrentSkinTextures(entry);
            saveConfig();
         } catch (Exception e) {
            throw new RuntimeException("Failed to apply skin", e);
         }
      }
   }

   public static void clearSelectedSkin() {
      selectedSkin = null;
      selectedSkinName = "";
      currentSkinTextures = null;
      saveConfig();
   }

   public static void downloadAndImportAsync(String username, Consumer<SkinEntry> success, Consumer<String> failure) {
      (new Thread(() -> {
         try {
            SkinEntry entry = SkinDownloader.downloadSkin(username, DOWNLOADED_DIR);
            refreshImportedSkins();
            applySkinByName(entry.name);
            if (success != null) {
               MC.execute(() -> success.accept(entry));
            }
         } catch (Exception e) {
            if (failure != null) {
               MC.execute(() -> failure.accept("Download failed: " + e.getMessage()));
            }
         }

      }, "OptiX-Skin-Downloader")).start();
   }

   private static void applySelectedFromConfig() {
      if (selectedSkinName != null && !selectedSkinName.isBlank()) {
         applySkinByName(selectedSkinName);
      }
   }

   private static void applySkinByName(String name) {
      if (name != null && !name.isBlank()) {
         for(SkinEntry entry : IMPORTED) {
            if (entry.name.equalsIgnoreCase(name)) {
               try {
                  loadTexture(entry);
                  selectedSkin = entry;
                  selectedSkinName = entry.name;
                  rebuildCurrentSkinTextures(entry);
               } catch (Exception var4) {
               }

               return;
            }
         }

      }
   }

   private static void loadTexture(SkinEntry entry) throws IOException {
      if (entry.textureId == null) {
         if (!TEXTURE_CACHE.containsKey(entry.name) || entry.textureId == null) {
            InputStream in = Files.newInputStream(entry.path);

            try {
               class_1011 image = class_1011.method_4309(in);
               class_1043 texture = new class_1043(() -> "optix_skin_" + entry.name, image);
               class_2960 id = class_2960.method_60655("optix", "skin/" + safeId(entry.name));
               entry.textureId = id;
               TEXTURE_CACHE.put(entry.name, texture);
               MC.execute(() -> MC.method_1531().method_4616(id, texture));
            } catch (Throwable var6) {
               if (in != null) {
                  try {
                     in.close();
                  } catch (Throwable var5) {
                     var6.addSuppressed(var5);
                  }
               }

               throw var6;
            }

            if (in != null) {
               in.close();
            }

         }
      }
   }

   private static String safeId(String name) {
      return name.toLowerCase(Locale.ROOT).replaceAll("[^a-z0-9/_-]", "_");
   }

   private static void loadConfig() {
      if (Files.exists(CONFIG_FILE, new LinkOption[0])) {
         try {
            Reader reader = Files.newBufferedReader(CONFIG_FILE);

            try {
               SkinConfig config = (SkinConfig)GSON.fromJson(reader, SkinConfig.class);
               if (config != null) {
                  selectedSkinName = config.selectedSkinName == null ? "" : config.selectedSkinName;
               }
            } catch (Throwable var4) {
               if (reader != null) {
                  try {
                     reader.close();
                  } catch (Throwable var3) {
                     var4.addSuppressed(var3);
                  }
               }

               throw var4;
            }

            if (reader != null) {
               reader.close();
            }
         } catch (Exception var5) {
         }

      }
   }

   public static void saveConfig() {
      try {
         Files.createDirectories(CONFIG_FILE.getParent());
         SkinConfig config = new SkinConfig();
         config.selectedSkinName = selectedSkinName;
         BufferedWriter writer = Files.newBufferedWriter(CONFIG_FILE, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);

         try {
            GSON.toJson(config, writer);
         } catch (Throwable var5) {
            if (writer != null) {
               try {
                  writer.close();
               } catch (Throwable var4) {
                  var5.addSuppressed(var4);
               }
            }

            throw var5;
         }

         if (writer != null) {
            writer.close();
         }
      } catch (Exception var6) {
      }

   }

   static {
      OPTIX_DIR = GAME_DIR.resolve("OptiX");
      IMPORTED_DIR = GAME_DIR.resolve("skins");
      DOWNLOADED_DIR = OPTIX_DIR.resolve("skins");
      CONFIG_FILE = OPTIX_DIR.resolve("skin-config.json");
      TEXTURE_CACHE = new HashMap();
      IMPORTED = new ArrayList();
      selectedSkinName = "";
   }

   public static enum SkinSource {
      IMPORTED("Imported"),
      DOWNLOADED("Downloaded");

      private final String label;

      private SkinSource(String label) {
         this.label = label;
      }

      public String label() {
         return this.label;
      }

      // $FF: synthetic method
      private static SkinSource[] $values() {
         return new SkinSource[]{IMPORTED, DOWNLOADED};
      }
   }

   public static final class SkinEntry {
      public final String name;
      public final Path path;
      public final SkinSource source;
      public class_2960 textureId;

      public SkinEntry(String name, Path path, SkinSource source) {
         this.name = name;
         this.path = path;
         this.source = source;
      }

      public String sourceLabel() {
         return this.source.label();
      }
   }
}
