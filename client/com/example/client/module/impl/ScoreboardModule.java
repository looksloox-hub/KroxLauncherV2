package com.example.client.module.impl;

import com.example.client.module.Category;
import com.example.client.module.HudModule;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import net.minecraft.class_2561;
import net.minecraft.class_266;
import net.minecraft.class_269;
import net.minecraft.class_2960;
import net.minecraft.class_332;
import net.minecraft.class_8646;

public class ScoreboardModule extends HudModule {
   public ScoreboardModule() {
      super("Scoreboard", Category.HUD, class_2960.method_60655("modid", "textures/gui/icons/list.png"));
      this.setHudPosition(600, 20);
      this.setHudSize(170, 42);
   }

   public void render(class_332 context) {
      if (mc.field_1687 != null) {
         class_269 scoreboard = mc.field_1687.method_8428();
         class_266 objective = scoreboard.method_1189(class_8646.field_45157);
         if (objective != null) {
            Collection<?> entries = scoreboard.method_1184(objective);
            if (entries != null && !entries.isEmpty()) {
               List<Object> sorted = new ArrayList(entries);
               sorted.sort(Comparator.comparingInt(this::readScore).reversed());
               int x = this.getHudX();
               int y = this.getHudY();
               int width = this.getHudWidth();
               int height = 20 + sorted.size() * 12;
               this.setHudSize(width, height);
               context.method_25294(x, y, x + width, y + height, -15723747);
               context.method_25294(x, y, x + width, y + 1, -7643914);
               String title = objective.method_1114().getString();
               context.method_51433(mc.field_1772, title, x + 10, y + 7, -1, false);
               int lineY = y + 20;

               for(Object entry : sorted) {
                  String name = this.readName(entry);
                  if (!name.isEmpty()) {
                     int score = this.readScore(entry);
                     String line = name + " : " + score;
                     context.method_51433(mc.field_1772, line, x + 10, lineY, -4944641, false);
                     lineY += 12;
                  }
               }

            }
         }
      }
   }

   private int readScore(Object entry) {
      for(String methodName : new String[]{"value", "score", "getScore"}) {
         try {
            Method method = entry.getClass().getMethod(methodName);
            Object out = method.invoke(entry);
            if (out instanceof Number number) {
               return number.intValue();
            }
         } catch (Throwable var9) {
         }
      }

      return 0;
   }

   private String readName(Object entry) {
      for(String methodName : new String[]{"owner", "name", "getName", "getPlayerName"}) {
         try {
            Method method = entry.getClass().getMethod(methodName);
            Object out = method.invoke(entry);
            if (out != null) {
               if (out instanceof class_2561) {
                  class_2561 text = (class_2561)out;
                  return text.getString();
               }

               return String.valueOf(out);
            }
         } catch (Throwable var9) {
         }
      }

      return "";
   }
}
