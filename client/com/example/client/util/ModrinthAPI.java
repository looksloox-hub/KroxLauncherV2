package com.example.client.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class ModrinthAPI {
   private static final String BASE = "https://api.modrinth.com/v2";

   public static List<Project> search(String query, String type) {
      List<Project> list = new ArrayList();

      try {
         String q = URLEncoder.encode(query == null ? "" : query, StandardCharsets.UTF_8);
         String urlStr = "https://api.modrinth.com/v2/search?query=" + q + "&limit=100";
         if (type != null && !type.isBlank()) {
            String facets = "[[\"project_type:" + type + "\"]]";
            urlStr = urlStr + "&facets=" + URLEncoder.encode(facets, StandardCharsets.UTF_8);
         }

         HttpURLConnection conn = (HttpURLConnection)(new URL(urlStr)).openConnection();
         conn.setRequestProperty("User-Agent", "OptiXClient");
         conn.setConnectTimeout(10000);
         conn.setReadTimeout(10000);
         JsonObject root = JsonParser.parseReader(new InputStreamReader(conn.getInputStream())).getAsJsonObject();
         JsonArray hits = root.getAsJsonArray("hits");
         if (hits == null) {
            return list;
         }

         for(JsonElement e : hits) {
            JsonObject obj = e.getAsJsonObject();
            Project p = new Project();
            p.id = getString(obj, "project_id");
            if (p.id.isBlank()) {
               p.id = getString(obj, "id");
            }

            p.slug = getString(obj, "slug");
            p.title = getString(obj, "title");
            if (p.title.isBlank()) {
               p.title = getString(obj, "name");
            }

            p.description = getString(obj, "description");
            if (p.description.isBlank()) {
               p.description = getString(obj, "summary");
            }

            p.author = getString(obj, "author");
            p.iconUrl = getString(obj, "icon_url");
            p.downloads = getInt(obj, "downloads");
            if (obj.has("categories") && obj.get("categories").isJsonArray()) {
               for(JsonElement c : obj.getAsJsonArray("categories")) {
                  p.categories.add(c.getAsString());
               }
            }

            list.add(p);
         }
      } catch (Exception e) {
         e.printStackTrace();
      }

      return list;
   }

   public static String getDownloadUrl(String projectId, String gameVersion, String loader) {
      try {
         String urlStr = "https://api.modrinth.com/v2/project/" + projectId + "/version";
         HttpURLConnection conn = (HttpURLConnection)(new URL(urlStr)).openConnection();
         conn.setRequestProperty("User-Agent", "OptiXClient");
         conn.setConnectTimeout(10000);
         conn.setReadTimeout(10000);
         JsonArray versions = JsonParser.parseReader(new InputStreamReader(conn.getInputStream())).getAsJsonArray();
         if (versions != null && versions.size() != 0) {
            for(JsonElement e : versions) {
               JsonObject v = e.getAsJsonObject();
               if (matches(v, loader, gameVersion)) {
                  return getPrimaryFileUrl(v);
               }
            }

            for(JsonElement e : versions) {
               JsonObject v = e.getAsJsonObject();
               if (matches(v, (String)null, gameVersion)) {
                  return getPrimaryFileUrl(v);
               }
            }

            return getPrimaryFileUrl(versions.get(0).getAsJsonObject());
         } else {
            return null;
         }
      } catch (Exception e) {
         e.printStackTrace();
         return null;
      }
   }

   public static List<String> getDependencies(String projectId) {
      List<String> deps = new ArrayList();

      try {
         String urlStr = "https://api.modrinth.com/v2/project/" + projectId + "/dependencies";
         HttpURLConnection conn = (HttpURLConnection)(new URL(urlStr)).openConnection();
         conn.setRequestProperty("User-Agent", "OptiXClient");
         conn.setConnectTimeout(10000);
         conn.setReadTimeout(10000);
         JsonObject root = JsonParser.parseReader(new InputStreamReader(conn.getInputStream())).getAsJsonObject();
         JsonArray projects = root.getAsJsonArray("projects");
         if (projects == null) {
            return deps;
         }

         for(JsonElement e : projects) {
            JsonObject obj = e.getAsJsonObject();
            String id = getString(obj, "id");
            if (!id.isBlank()) {
               deps.add(id);
            }
         }
      } catch (Exception e) {
         e.printStackTrace();
      }

      return deps;
   }

   private static boolean matches(JsonObject version, String loader, String gameVersion) {
      JsonArray loaders = version.has("loaders") ? version.getAsJsonArray("loaders") : null;
      JsonArray games = version.has("game_versions") ? version.getAsJsonArray("game_versions") : null;
      boolean loaderMatch = loader == null || loader.isBlank() || "any".equalsIgnoreCase(loader);
      boolean gameMatch = false;
      if (!loaderMatch && loaders != null) {
         for(JsonElement l : loaders) {
            if (l.getAsString().equalsIgnoreCase(loader)) {
               loaderMatch = true;
               break;
            }
         }
      }

      if (games != null) {
         for(JsonElement g : games) {
            String gv = g.getAsString();
            if (gv.equalsIgnoreCase(gameVersion)) {
               gameMatch = true;
               break;
            }
         }

         if (!gameMatch) {
            String[] parts = gameVersion.split("\\.");
            if (parts.length >= 2) {
               String base = parts[0] + "." + parts[1];

               for(JsonElement g : games) {
                  String gv = g.getAsString();
                  if (gv.equalsIgnoreCase(base)) {
                     gameMatch = true;
                     break;
                  }
               }
            }
         }
      }

      return loaderMatch && gameMatch;
   }

   private static String getPrimaryFileUrl(JsonObject version) {
      if (version.has("files") && version.get("files").isJsonArray()) {
         JsonArray files = version.getAsJsonArray("files");
         if (files.size() == 0) {
            return null;
         } else {
            for(JsonElement f : files) {
               JsonObject file = f.getAsJsonObject();
               if (file.has("primary") && file.get("primary").getAsBoolean() && file.has("url")) {
                  return file.get("url").getAsString();
               }
            }

            JsonObject first = files.get(0).getAsJsonObject();
            return first.has("url") ? first.get("url").getAsString() : null;
         }
      } else {
         return null;
      }
   }

   private static String getString(JsonObject obj, String key) {
      return obj.has(key) && !obj.get(key).isJsonNull() ? obj.get(key).getAsString() : "";
   }

   private static int getInt(JsonObject obj, String key) {
      return obj.has(key) && !obj.get(key).isJsonNull() ? obj.get(key).getAsInt() : 0;
   }

   public static class Project {
      public String id;
      public String slug;
      public String title;
      public String description;
      public String author;
      public String iconUrl;
      public int downloads;
      public List<String> categories = new ArrayList();
   }
}
