package com.example.client.config;

import com.example.client.module.Module;
import com.example.client.module.ModuleManager;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import net.minecraft.class_310;

public class HudConfig {
   private static final File FILE;

   public static void save() {
      try {
         JsonObject root = new JsonObject();

         for(Module m : ModuleManager.getModules()) {
            if (m.isHud()) {
               JsonObject obj = new JsonObject();
               obj.addProperty("x", m.getX());
               obj.addProperty("y", m.getY());
               obj.addProperty("scale", m.getScale());
               obj.addProperty("hudColor", m.getHudColor());
               obj.addProperty("rainbow", m.isRainbow());
               obj.addProperty("rgb", m.isRGB());
               obj.addProperty("box", m.isBox());
               obj.addProperty("outline", m.isOutline());
               obj.addProperty("glow", m.isGlow());
               obj.addProperty("vertical", m.isVertical());
               obj.addProperty("draggable", m.isDraggable());
               obj.addProperty("enabled", m.isEnabled());
               obj.addProperty("width", m.getWidth());
               obj.addProperty("height", m.getHeight());
               float scale = m.getScale();
               if (scale < 0.1F) {
                  scale = 0.1F;
               }

               if (scale > 5.0F) {
                  scale = 5.0F;
               }

               obj.addProperty("scale", scale);
               root.add(m.getName(), obj);
            }
         }

         FILE.getParentFile().mkdirs();
         FileWriter writer = new FileWriter(FILE);

         try {
            (new GsonBuilder()).setPrettyPrinting().create().toJson(root, writer);
         } catch (Throwable var6) {
            try {
               writer.close();
            } catch (Throwable var5) {
               var6.addSuppressed(var5);
            }

            throw var6;
         }

         writer.close();
      } catch (Exception e) {
         e.printStackTrace();
      }

   }

   public static void load() {
      try {
         if (!FILE.exists()) {
            return;
         }

         JsonObject root = JsonParser.parseReader(new FileReader(FILE)).getAsJsonObject();

         for(Module m : ModuleManager.getModules()) {
            if (root.has(m.getName())) {
               JsonObject obj = root.getAsJsonObject(m.getName());
               if (obj.has("x") && obj.has("y")) {
                  m.setPosition(obj.get("x").getAsInt(), obj.get("y").getAsInt());
               }

               if (obj.has("scale")) {
                  float scale = obj.get("scale").getAsFloat();
                  if (scale < 0.1F) {
                     scale = 0.1F;
                  }

                  if (scale > 5.0F) {
                     scale = 5.0F;
                  }

                  m.setScale(scale);
               }

               if (obj.has("width") && obj.has("height")) {
                  m.setWidth(obj.get("width").getAsInt());
                  m.setHeight(obj.get("height").getAsInt());
               }

               if (obj.has("hudColor")) {
                  m.setHudColor(obj.get("hudColor").getAsInt());
               }

               if (obj.has("rainbow")) {
                  m.setRainbow(obj.get("rainbow").getAsBoolean());
               }

               if (obj.has("rgb")) {
                  m.setRGB(obj.get("rgb").getAsBoolean());
               }

               if (obj.has("box")) {
                  m.setBox(obj.get("box").getAsBoolean());
               }

               if (obj.has("outline")) {
                  m.setOutline(obj.get("outline").getAsBoolean());
               }

               if (obj.has("glow")) {
                  m.setGlow(obj.get("glow").getAsBoolean());
               }

               if (obj.has("vertical")) {
                  m.setVertical(obj.get("vertical").getAsBoolean());
               }

               if (obj.has("draggable")) {
                  m.setDraggable(obj.get("draggable").getAsBoolean());
               }

               if (obj.has("enabled")) {
                  m.setEnabled(obj.get("enabled").getAsBoolean());
               }
            }
         }
      } catch (Exception e) {
         e.printStackTrace();
      }

   }

   static {
      FILE = new File(class_310.method_1551().field_1697, "config/modid_hud.json");
   }
}
