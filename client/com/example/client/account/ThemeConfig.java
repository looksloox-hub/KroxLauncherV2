package com.example.client.account;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import net.fabricmc.loader.api.FabricLoader;

public class ThemeConfig {
   public static int BG_TOP = -16315630;
   public static int BG_BOTTOM = -15788251;
   public static int PANEL = -871558112;
   public static int CARD = -15656665;
   public static int CARD_HOVER = -15261132;
   public static int BORDER = -14405050;
   public static int BORDER_HOVER = -12877066;
   public static int ACCENT = -12877066;
   public static int ACTIVE_GLOW = 1429963510;
   private static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().create();
   private static final Path FILE = FabricLoader.getInstance().getGameDir().resolve("OptiXClient").resolve("theme.json");

   public static void load() {
      try {
         if (!Files.exists(FILE, new LinkOption[0])) {
            save();
         } else {
            Reader reader = Files.newBufferedReader(FILE);

            label59: {
               try {
                  ThemeData data = (ThemeData)GSON.fromJson(reader, ThemeData.class);
                  if (data == null) {
                     break label59;
                  }

                  BG_TOP = data.BG_TOP;
                  BG_BOTTOM = data.BG_BOTTOM;
                  PANEL = data.PANEL;
                  CARD = data.CARD;
                  CARD_HOVER = data.CARD_HOVER;
                  BORDER = data.BORDER;
                  BORDER_HOVER = data.BORDER_HOVER;
                  ACCENT = data.ACCENT;
                  ACTIVE_GLOW = data.ACTIVE_GLOW;
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

               return;
            }

            if (reader != null) {
               reader.close();
            }

         }
      } catch (Exception e) {
         e.printStackTrace();
      }
   }

   public static void save() {
      try {
         Files.createDirectories(FILE.getParent());
         ThemeData data = new ThemeData();
         data.BG_TOP = BG_TOP;
         data.BG_BOTTOM = BG_BOTTOM;
         data.PANEL = PANEL;
         data.CARD = CARD;
         data.CARD_HOVER = CARD_HOVER;
         data.BORDER = BORDER;
         data.BORDER_HOVER = BORDER_HOVER;
         data.ACCENT = ACCENT;
         data.ACTIVE_GLOW = ACTIVE_GLOW;
         Writer writer = Files.newBufferedWriter(FILE);

         try {
            GSON.toJson(data, writer);
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
      } catch (Exception e) {
         e.printStackTrace();
      }

   }

   public static void applyPreset(String preset) {
      switch (preset) {
         case "Ocean":
            BG_TOP = -16379619;
            BG_BOTTOM = -16115158;
            PANEL = -871557341;
            CARD = -15787480;
            CARD_HOVER = -15457227;
            BORDER = -14335137;
            BORDER_HOVER = -13058568;
            ACCENT = -13058568;
            ACTIVE_GLOW = 1429782008;
            break;
         case "Violet":
            BG_TOP = -15595242;
            BG_BOTTOM = -15069149;
            PANEL = -870970081;
            CARD = -15396834;
            CARD_HOVER = -14674900;
            BORDER = -12441762;
            BORDER_HOVER = -5745161;
            ACCENT = -5745161;
            ACTIVE_GLOW = 1437095415;
            break;
         case "Mint":
            BG_TOP = -16313071;
            BG_BOTTOM = -16048102;
            PANEL = -871557100;
            CARD = -15721962;
            CARD_HOVER = -15391455;
            BORDER = -14067126;
            BORDER_HOVER = -13315175;
            ACCENT = -13315175;
            ACTIVE_GLOW = 1429525401;
            break;
         case "Rose":
            BG_TOP = -15267310;
            BG_BOTTOM = -14479334;
            PANEL = -870707946;
            CARD = -15068905;
            CARD_HOVER = -14150626;
            BORDER = -9819832;
            BORDER_HOVER = -757066;
            ACCENT = -757066;
            ACTIVE_GLOW = 1442083510;
            break;
         case "Amber":
            BG_TOP = -15200246;
            BG_BOTTOM = -14412019;
            PANEL = -870641139;
            CARD = -14936817;
            CARD_HOVER = -14016749;
            BORDER = -9746912;
            BORDER_HOVER = -680437;
            ACCENT = -680437;
            ACTIVE_GLOW = 1442160139;
            break;
         default:
            BG_TOP = -16315630;
            BG_BOTTOM = -15788251;
            PANEL = -871558112;
            CARD = -15656665;
            CARD_HOVER = -15261132;
            BORDER = -14405050;
            BORDER_HOVER = -12877066;
            ACCENT = -12877066;
            ACTIVE_GLOW = 1429963510;
      }

      save();
   }

   public static class ThemeData {
      public int BG_TOP;
      public int BG_BOTTOM;
      public int PANEL;
      public int CARD;
      public int CARD_HOVER;
      public int BORDER;
      public int BORDER_HOVER;
      public int ACCENT;
      public int ACTIVE_GLOW;
   }
}
