package com.example.client.cosmetics;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Base64;

public final class SkinDownloader {
   private SkinDownloader() {
   }

   public static SkinManager.SkinEntry downloadSkin(String username, Path targetDir) throws IOException {
      Files.createDirectories(targetDir);
      String safeName = username.trim();
      if (safeName.isEmpty()) {
         throw new IOException("Username is empty");
      } else {
         String uuid = fetchUuid(safeName);
         String textureUrl = fetchSkinUrl(uuid);
         if (textureUrl != null && !textureUrl.isEmpty()) {
            Path out = targetDir.resolve(safeName + ".png");
            downloadFile(textureUrl, out);
            return new SkinManager.SkinEntry(out.getFileName().toString(), out, SkinManager.SkinSource.DOWNLOADED);
         } else {
            throw new IOException("No skin found for that player");
         }
      }
   }

   private static String fetchUuid(String username) throws IOException {
      String endpoint = "https://api.mojang.com/users/profiles/minecraft/" + username;
      JsonObject obj = readJson(endpoint);
      if (obj != null && obj.has("id")) {
         return obj.get("id").getAsString();
      } else {
         throw new IOException("Player not found");
      }
   }

   private static String fetchSkinUrl(String uuid) throws IOException {
      String endpoint = "https://sessionserver.mojang.com/session/minecraft/profile/" + uuid + "?unsigned=false";
      JsonObject obj = readJson(endpoint);
      if (obj != null && obj.has("properties")) {
         JsonArray properties = obj.getAsJsonArray("properties");
         if (properties.isEmpty()) {
            return null;
         } else {
            String value = properties.get(0).getAsJsonObject().get("value").getAsString();
            String decoded = new String(Base64.getDecoder().decode(value), StandardCharsets.UTF_8);
            JsonObject decodedJson = JsonParser.parseString(decoded).getAsJsonObject();
            if (!decodedJson.has("textures")) {
               return null;
            } else {
               JsonObject textures = decodedJson.getAsJsonObject("textures");
               return !textures.has("SKIN") ? null : textures.getAsJsonObject("SKIN").get("url").getAsString();
            }
         }
      } else {
         return null;
      }
   }

   private static JsonObject readJson(String url) throws IOException {
      HttpURLConnection conn = (HttpURLConnection)URI.create(url).toURL().openConnection();
      conn.setConnectTimeout(10000);
      conn.setReadTimeout(10000);
      conn.setRequestProperty("User-Agent", "OptiX-Skin-Downloader");
      conn.setRequestMethod("GET");
      int code = conn.getResponseCode();
      if (code >= 200 && code < 300) {
         InputStream in = conn.getInputStream();

         JsonObject var4;
         try {
            var4 = JsonParser.parseString(new String(in.readAllBytes(), StandardCharsets.UTF_8)).getAsJsonObject();
         } catch (Throwable var7) {
            if (in != null) {
               try {
                  in.close();
               } catch (Throwable var6) {
                  var7.addSuppressed(var6);
               }
            }

            throw var7;
         }

         if (in != null) {
            in.close();
         }

         return var4;
      } else {
         return null;
      }
   }

   private static void downloadFile(String url, Path out) throws IOException {
      HttpURLConnection conn = (HttpURLConnection)(new URL(url)).openConnection();
      conn.setConnectTimeout(10000);
      conn.setReadTimeout(10000);
      InputStream in = conn.getInputStream();

      try {
         Files.copy(in, out, new CopyOption[]{StandardCopyOption.REPLACE_EXISTING});
      } catch (Throwable var7) {
         if (in != null) {
            try {
               in.close();
            } catch (Throwable var6) {
               var7.addSuppressed(var6);
            }
         }

         throw var7;
      }

      if (in != null) {
         in.close();
      }

   }
}
