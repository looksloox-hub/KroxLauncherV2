package com.example.client.module.impl;

import com.example.client.module.Category;
import com.example.client.module.HudModule;
import com.example.client.setting.NumberSetting;
import net.minecraft.class_1297;
import net.minecraft.class_1657;
import net.minecraft.class_1799;
import net.minecraft.class_239;
import net.minecraft.class_2960;
import net.minecraft.class_332;
import net.minecraft.class_3489;
import net.minecraft.class_3966;
import net.minecraft.class_239.class_240;

public class CrosshairStyleModule extends HudModule {
   private float spread = 0.0F;
   private final NumberSetting baseGap = new NumberSetting("Base Gap", (double)3.0F, (double)0.0F, (double)20.0F, (double)1.0F);
   private final NumberSetting armLength = new NumberSetting("Arm Length", (double)4.0F, (double)1.0F, (double)20.0F, (double)1.0F);
   private final NumberSetting thickness = new NumberSetting("Thickness", (double)1.0F, (double)1.0F, (double)8.0F, (double)1.0F);
   private final NumberSetting dotSize = new NumberSetting("Dot Size", (double)1.0F, (double)0.0F, (double)8.0F, (double)1.0F);
   private final NumberSetting opacity = new NumberSetting("Opacity", (double)255.0F, (double)0.0F, (double)255.0F, (double)1.0F);
   private final NumberSetting smoothness = new NumberSetting("Smoothness", (double)0.25F, 0.05, (double)1.0F, 0.05);
   private final NumberSetting cooldownPower = new NumberSetting("Cooldown Power", (double)1.0F, 0.1, (double)3.0F, 0.1);
   private final NumberSetting swordFactor = new NumberSetting("Sword Factor", 0.85, 0.1, (double)2.0F, 0.05);
   private final NumberSetting axeFactor = new NumberSetting("Axe Factor", (double)1.25F, 0.1, (double)3.0F, 0.05);
   private final NumberSetting normalRed = new NumberSetting("Normal Red", (double)255.0F, (double)0.0F, (double)255.0F, (double)1.0F);
   private final NumberSetting normalGreen = new NumberSetting("Normal Green", (double)255.0F, (double)0.0F, (double)255.0F, (double)1.0F);
   private final NumberSetting normalBlue = new NumberSetting("Normal Blue", (double)255.0F, (double)0.0F, (double)255.0F, (double)1.0F);
   private final NumberSetting playerRed = new NumberSetting("Player Red", (double)255.0F, (double)0.0F, (double)255.0F, (double)1.0F);
   private final NumberSetting playerGreen = new NumberSetting("Player Green", (double)85.0F, (double)0.0F, (double)255.0F, (double)1.0F);
   private final NumberSetting playerBlue = new NumberSetting("Player Blue", (double)85.0F, (double)0.0F, (double)255.0F, (double)1.0F);
   private final NumberSetting mobRed = new NumberSetting("Mob Red", (double)85.0F, (double)0.0F, (double)255.0F, (double)1.0F);
   private final NumberSetting mobGreen = new NumberSetting("Mob Green", (double)170.0F, (double)0.0F, (double)255.0F, (double)1.0F);
   private final NumberSetting mobBlue = new NumberSetting("Mob Blue", (double)255.0F, (double)0.0F, (double)255.0F, (double)1.0F);

   public CrosshairStyleModule() {
      super("Crosshair Style", Category.HUD, (class_2960)null);
      this.x = 0;
      this.y = 0;
      this.width = 0;
      this.height = 0;
      this.addSetting(this.baseGap);
      this.addSetting(this.armLength);
      this.addSetting(this.thickness);
      this.addSetting(this.dotSize);
      this.addSetting(this.opacity);
      this.addSetting(this.smoothness);
      this.addSetting(this.cooldownPower);
      this.addSetting(this.swordFactor);
      this.addSetting(this.axeFactor);
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
         this.updateSpread();
         int color = this.getCrosshairColor();
         color = applyAlpha(color, (int)this.opacity.getValue());
         int gap = Math.max(0, Math.round((float)this.baseGap.getValue() + this.spread));
         int size = Math.max(1, (int)this.armLength.getValue());
         int lineThickness = Math.max(1, (int)this.thickness.getValue());
         int dot = Math.max(0, (int)this.dotSize.getValue());
         this.drawCrosshair(context, cx, cy, gap, size, lineThickness, dot, color);
      }
   }

   private void updateSpread() {
      float cooldown = mc.field_1724.method_7261(0.0F);
      float weaponFactor = this.getWeaponFactor();
      float base = (float)this.baseGap.getValue();
      float power = (float)this.cooldownPower.getValue();
      float smooth = clamp01((float)this.smoothness.getValue());
      float targetSpread = base * weaponFactor * (float)Math.pow((double)(1.0F - cooldown), (double)power);
      this.spread += (targetSpread - this.spread) * smooth;
   }

   private float getWeaponFactor() {
      class_1799 stack = mc.field_1724.method_6047();
      if (stack.method_31573(class_3489.field_42611)) {
         return (float)this.swordFactor.getValue();
      } else {
         return stack.method_31573(class_3489.field_42612) ? (float)this.axeFactor.getValue() : 1.0F;
      }
   }

   private void drawCrosshair(class_332 context, int cx, int cy, int gap, int size, int thickness, int dot, int color) {
      int half = thickness / 2;
      int topY = cy - half;
      int bottomY = topY + thickness;
      int leftX = cx - half;
      int rightX = leftX + thickness;
      int leftArmStart = cx - gap - size;
      int leftArmEnd = cx - gap;
      int rightArmStart = cx + gap;
      int rightArmEnd = cx + gap + size;
      int topArmStart = cy - gap - size;
      int topArmEnd = cy - gap;
      int bottomArmStart = cy + gap;
      int bottomArmEnd = cy + gap + size;
      this.fillSafe(context, leftArmStart, topY, leftArmEnd, bottomY, color);
      this.fillSafe(context, rightArmStart, topY, rightArmEnd, bottomY, color);
      this.fillSafe(context, leftX, topArmStart, rightX, topArmEnd, color);
      this.fillSafe(context, leftX, bottomArmStart, rightX, bottomArmEnd, color);
      if (dot > 0) {
         int dotStartX = cx - dot / 2;
         int dotStartY = cy - dot / 2;
         this.fillSafe(context, dotStartX, dotStartY, dotStartX + dot, dotStartY + dot, color);
      }

   }

   private void fillSafe(class_332 context, int x1, int y1, int x2, int y2, int color) {
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
         class_1297 e = entityHitResult.method_17782();
         return e instanceof class_1657 ? rgbColor((int)this.playerRed.getValue(), (int)this.playerGreen.getValue(), (int)this.playerBlue.getValue()) : rgbColor((int)this.mobRed.getValue(), (int)this.mobGreen.getValue(), (int)this.mobBlue.getValue());
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

   private static float clamp01(float value) {
      return Math.max(0.0F, Math.min(1.0F, value));
   }
}
