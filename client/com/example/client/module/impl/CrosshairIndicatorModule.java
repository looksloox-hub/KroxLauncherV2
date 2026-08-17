package com.example.client.module.impl;

import com.example.client.module.Category;
import com.example.client.module.HudModule;
import com.example.client.setting.NumberSetting;
import net.minecraft.class_1297;
import net.minecraft.class_1657;
import net.minecraft.class_239;
import net.minecraft.class_2960;
import net.minecraft.class_332;
import net.minecraft.class_3966;
import net.minecraft.class_239.class_240;

public class CrosshairIndicatorModule extends HudModule {
   private static final class_2960 ICON = class_2960.method_60655("modid", "textures/gui/icons/crosshair.png");
   private final NumberSetting gap = new NumberSetting("Gap", (double)3.0F, (double)0.0F, (double)10.0F, (double)1.0F);
   private final NumberSetting size = new NumberSetting("Size", (double)4.0F, (double)1.0F, (double)20.0F, (double)1.0F);
   private final NumberSetting thickness = new NumberSetting("Thickness", (double)1.0F, (double)1.0F, (double)6.0F, (double)1.0F);
   private final NumberSetting opacity = new NumberSetting("Opacity", (double)255.0F, (double)0.0F, (double)255.0F, (double)1.0F);
   private final NumberSetting normalRed = new NumberSetting("Normal Red", (double)255.0F, (double)0.0F, (double)255.0F, (double)1.0F);
   private final NumberSetting normalGreen = new NumberSetting("Normal Green", (double)255.0F, (double)0.0F, (double)255.0F, (double)1.0F);
   private final NumberSetting normalBlue = new NumberSetting("Normal Blue", (double)255.0F, (double)0.0F, (double)255.0F, (double)1.0F);
   private final NumberSetting playerRed = new NumberSetting("Player Red", (double)255.0F, (double)0.0F, (double)255.0F, (double)1.0F);
   private final NumberSetting playerGreen = new NumberSetting("Player Green", (double)85.0F, (double)0.0F, (double)255.0F, (double)1.0F);
   private final NumberSetting playerBlue = new NumberSetting("Player Blue", (double)85.0F, (double)0.0F, (double)255.0F, (double)1.0F);
   private final NumberSetting mobRed = new NumberSetting("Mob Red", (double)85.0F, (double)0.0F, (double)255.0F, (double)1.0F);
   private final NumberSetting mobGreen = new NumberSetting("Mob Green", (double)255.0F, (double)0.0F, (double)255.0F, (double)1.0F);
   private final NumberSetting mobBlue = new NumberSetting("Mob Blue", (double)85.0F, (double)0.0F, (double)255.0F, (double)1.0F);

   public CrosshairIndicatorModule() {
      super("Crosshair", Category.HUD, ICON);
      this.x = 0;
      this.y = 0;
      this.width = 0;
      this.height = 0;
      this.addSetting(this.gap);
      this.addSetting(this.size);
      this.addSetting(this.thickness);
      this.addSetting(this.opacity);
      this.addSetting(this.normalRed);
      this.addSetting(this.normalGreen);
      this.addSetting(this.normalBlue);
      this.addSetting(this.playerRed);
      this.addSetting(this.playerGreen);
      this.addSetting(this.playerBlue);
      this.addSetting(this.mobRed);
      this.addSetting(this.mobGreen);
      this.addSetting(this.mobBlue);
   }

   public void render(class_332 context) {
      if (this.isEnabled() && mc != null && mc.field_1724 != null && mc.method_22683() != null) {
         int cx = mc.method_22683().method_4486() / 2;
         int cy = mc.method_22683().method_4502() / 2;
         int g = Math.max(0, (int)this.gap.getValue());
         int s = Math.max(1, (int)this.size.getValue());
         int t = Math.max(1, (int)this.thickness.getValue());
         int color = applyAlpha(this.getCrosshairColor(), (int)this.opacity.getValue());
         this.drawCenteredCrosshair(context, cx, cy, g, s, t, color);
      }
   }

   private void drawCenteredCrosshair(class_332 context, int cx, int cy, int gap, int size, int thickness, int color) {
      int halfThicknessLeft = thickness / 2;
      int var10000 = thickness - halfThicknessLeft;
      int leftStartX = cx - gap - size;
      int leftEndX = cx - gap;
      int rightStartX = cx + gap;
      int rightEndX = cx + gap + size;
      int topStartY = cy - gap - size;
      int topEndY = cy - gap;
      int bottomStartY = cy + gap;
      int bottomEndY = cy + gap + size;
      int verticalStartY = cy - halfThicknessLeft;
      int verticalEndY = verticalStartY + thickness;
      int horizontalStartX = cx - halfThicknessLeft;
      int horizontalEndX = horizontalStartX + thickness;
      this.drawFilledRect(context, leftStartX, verticalStartY, leftEndX, verticalEndY, color);
      this.drawFilledRect(context, rightStartX, verticalStartY, rightEndX, verticalEndY, color);
      this.drawFilledRect(context, horizontalStartX, topStartY, horizontalEndX, topEndY, color);
      this.drawFilledRect(context, horizontalStartX, bottomStartY, horizontalEndX, bottomEndY, color);
      int dotStartX = cx - thickness / 2;
      int dotStartY = cy - thickness / 2;
      int dotEndX = dotStartX + thickness;
      int dotEndY = dotStartY + thickness;
      this.drawFilledRect(context, dotStartX, dotStartY, dotEndX, dotEndY, color);
   }

   private void drawFilledRect(class_332 context, int x1, int y1, int x2, int y2, int color) {
      if (x2 > x1 && y2 > y1) {
         context.method_25294(x1, y1, x2, y2, color);
      }
   }

   private int getCrosshairColor() {
      class_239 hit = mc.field_1765;
      if (hit == null) {
         return rgbColor((int)this.normalRed.getValue(), (int)this.normalGreen.getValue(), (int)this.normalBlue.getValue());
      } else if (hit.method_17783() == class_240.field_1331 && hit instanceof class_3966) {
         class_3966 entityHitResult = (class_3966)hit;
         class_1297 entity = entityHitResult.method_17782();
         return entity instanceof class_1657 ? rgbColor((int)this.playerRed.getValue(), (int)this.playerGreen.getValue(), (int)this.playerBlue.getValue()) : rgbColor((int)this.mobRed.getValue(), (int)this.mobGreen.getValue(), (int)this.mobBlue.getValue());
      } else {
         return rgbColor((int)this.normalRed.getValue(), (int)this.normalGreen.getValue(), (int)this.normalBlue.getValue());
      }
   }

   private static int rgbColor(int r, int g, int b) {
      return -16777216 | clamp255(r) << 16 | clamp255(g) << 8 | clamp255(b);
   }

   private static int applyAlpha(int color, int alpha) {
      return clamp255(alpha) << 24 | color & 16777215;
   }

   private static int clamp255(int value) {
      return Math.max(0, Math.min(255, value));
   }
}
