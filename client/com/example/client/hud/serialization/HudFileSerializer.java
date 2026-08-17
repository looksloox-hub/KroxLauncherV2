package com.example.client.hud.serialization;

import com.example.client.hud.HudAnchor;
import com.example.client.hud.HudBounds;
import com.example.client.hud.HudElement;
import com.example.client.hud.HudLayoutData;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public final class HudFileSerializer implements HudSerializer {
   private static final String HEADER = "# Hud layout data";

   public void save(Path path, Collection<? extends HudElement> elements) throws IOException {
      Objects.requireNonNull(path, "path");
      Objects.requireNonNull(elements, "elements");
      StringBuilder builder = new StringBuilder();
      builder.append("# Hud layout data").append('\n');

      for(HudElement element : elements) {
         HudLayoutData data = element.snapshot();
         builder.append(encode(data)).append('\n');
      }

      Path parent = path.getParent();
      if (parent != null) {
         Files.createDirectories(parent);
      }

      Files.writeString(path, builder.toString(), StandardCharsets.UTF_8);
   }

   public List<HudLayoutData> load(Path path) throws IOException {
      Objects.requireNonNull(path, "path");
      if (!Files.exists(path, new LinkOption[0])) {
         return List.of();
      } else {
         List<HudLayoutData> result = new ArrayList();

         for(String line : Files.readAllLines(path, StandardCharsets.UTF_8)) {
            if (!line.isBlank() && !line.startsWith("#")) {
               result.add(decode(line));
            }
         }

         return result;
      }
   }

   private static String encode(HudLayoutData data) {
      StringBuilder builder = new StringBuilder();
      appendToken(builder, data.id());
      appendToken(builder, data.factoryId());
      appendToken(builder, Double.toString(data.bounds().x()));
      appendToken(builder, Double.toString(data.bounds().y()));
      appendToken(builder, Double.toString(data.bounds().width()));
      appendToken(builder, Double.toString(data.bounds().height()));
      appendToken(builder, data.anchor().name());
      appendToken(builder, Boolean.toString(data.visible()));
      appendToken(builder, Boolean.toString(data.locked()));
      appendToken(builder, Float.toString(data.scale()));
      appendToken(builder, Integer.toString(data.zIndex()));
      appendToken(builder, encodeProperties(data.properties()));
      return builder.toString();
   }

   private static HudLayoutData decode(String line) {
      String[] parts = line.split("\\|", -1);
      if (parts.length < 12) {
         throw new IllegalArgumentException("Invalid HUD layout line: " + line);
      } else {
         String id = decodeToken(parts[0]);
         String factoryId = decodeToken(parts[1]);
         double x = Double.parseDouble(decodeToken(parts[2]));
         double y = Double.parseDouble(decodeToken(parts[3]));
         double width = Double.parseDouble(decodeToken(parts[4]));
         double height = Double.parseDouble(decodeToken(parts[5]));
         HudAnchor anchor = HudAnchor.valueOf(decodeToken(parts[6]));
         boolean visible = Boolean.parseBoolean(decodeToken(parts[7]));
         boolean locked = Boolean.parseBoolean(decodeToken(parts[8]));
         float scale = Float.parseFloat(decodeToken(parts[9]));
         int zIndex = Integer.parseInt(decodeToken(parts[10]));
         Map<String, String> properties = decodeProperties(decodeToken(parts[11]));
         return new HudLayoutData(id, factoryId, new HudBounds(x, y, width, height), anchor, visible, locked, scale, zIndex, properties);
      }
   }

   private static void appendToken(StringBuilder builder, String value) {
      if (builder.length() > 0) {
         builder.append('|');
      }

      builder.append(encodeToken(value));
   }

   private static String encodeToken(String value) {
      if (value == null) {
         value = "";
      }

      return Base64.getUrlEncoder().withoutPadding().encodeToString(value.getBytes(StandardCharsets.UTF_8));
   }

   private static String decodeToken(String token) {
      return token != null && !token.isEmpty() ? new String(Base64.getUrlDecoder().decode(token), StandardCharsets.UTF_8) : "";
   }

   private static String encodeProperties(Map<String, String> properties) {
      if (properties != null && !properties.isEmpty()) {
         StringBuilder raw = new StringBuilder();
         boolean first = true;

         for(Map.Entry<String, String> entry : properties.entrySet()) {
            if (!first) {
               raw.append(';');
            }

            first = false;
            raw.append(entry.getKey() == null ? "" : (String)entry.getKey());
            raw.append('=');
            raw.append(entry.getValue() == null ? "" : (String)entry.getValue());
         }

         return encodeToken(raw.toString());
      } else {
         return encodeToken("");
      }
   }

   private static Map<String, String> decodeProperties(String encoded) {
      String raw = decodeToken(encoded);
      if (raw.isEmpty()) {
         return Map.of();
      } else {
         Map<String, String> result = new LinkedHashMap();
         String[] pairs = raw.split(";", -1);

         for(String pair : pairs) {
            int idx = pair.indexOf(61);
            if (idx > 0) {
               String key = pair.substring(0, idx);
               String value = pair.substring(idx + 1);
               result.put(key, value);
            }
         }

         return result;
      }
   }
}
