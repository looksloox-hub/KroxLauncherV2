package com.example.client.account;

import net.minecraft.class_2561;
import net.minecraft.class_310;
import net.minecraft.class_332;
import net.minecraft.class_342;
import net.minecraft.class_4185;
import net.minecraft.class_437;

public class RenameAccountScreen extends class_437 {
   private final class_437 parent;
   private final AccountData account;
   private class_342 nameField;

   public RenameAccountScreen(class_437 parent, AccountData account) {
      super(class_2561.method_43470("Rename Account"));
      this.parent = parent;
      this.account = account;
   }

   protected void method_25426() {
      int cx = this.field_22789 / 2;
      this.nameField = new class_342(this.field_22793, cx - 100, this.field_22790 / 2 - 18, 200, 20, class_2561.method_43470("Username"));
      this.nameField.method_1880(16);
      this.nameField.method_1852(this.account.username == null ? "" : this.account.username);
      this.method_37063(this.nameField);
      class_4185 save = class_4185.method_46430(class_2561.method_43470("Save"), (b) -> {
         AccountManager.rename(this.account, this.nameField.method_1882());
         class_310.method_1551().method_1507(this.parent);
      }).method_46434(cx - 100, this.field_22790 / 2 + 10, 96, 20).method_46431();
      class_4185 back = class_4185.method_46430(class_2561.method_43470("Back"), (b) -> class_310.method_1551().method_1507(this.parent)).method_46434(cx + 4, this.field_22790 / 2 + 10, 96, 20).method_46431();
      save.method_25350(0.0F);
      back.method_25350(0.0F);
      this.method_37063(save);
      this.method_37063(back);
   }

   private void drawRoundedRect(class_332 context, int x, int y, int w, int h, int color) {
      context.method_25294(x + 1, y, x + w - 1, y + h, color);
      context.method_25294(x, y + 1, x + w, y + h - 1, color);
   }

   private void drawCard(class_332 context, int x, int y, int w, int h, int bg, int border) {
      this.drawRoundedRect(context, x + 2, y + 2, w, h, 855638016);
      this.drawRoundedRect(context, x, y, w, h, bg);
      context.method_25294(x + 1, y, x + w - 1, y + 1, border);
      context.method_25294(x + 1, y + h - 1, x + w - 1, y + h, border);
   }

   private void drawButton(class_332 context, int x, int y, int w, int h, String text, int accent) {
      this.drawRoundedRect(context, x + 1, y + 1, w, h, 1140850688);
      this.drawRoundedRect(context, x, y, w, h, -15525597);
      context.method_25294(x + 1, y, x + w - 1, y + 1, accent);
      context.method_27534(this.field_22793, class_2561.method_43470(text), x + w / 2, y + 7, -1);
   }

   public void method_25394(class_332 context, int mouseX, int mouseY, float delta) {
      context.method_25296(0, 0, this.field_22789, this.field_22790, ThemeConfig.BG_TOP, ThemeConfig.BG_BOTTOM);
      int x = this.field_22789 / 2 - 170;
      int y = this.field_22790 / 2 - 58;
      int w = 340;
      int h = 116;
      this.drawCard(context, x, y, w, h, ThemeConfig.PANEL, ThemeConfig.ACCENT);
      context.method_27534(this.field_22793, class_2561.method_43470("Rename account"), this.field_22789 / 2, y + 10, -1);
      context.method_27534(this.field_22793, class_2561.method_43470("Pick a new username"), this.field_22789 / 2, y + 22, -1711276033);
      context.method_25303(this.field_22793, "Username", x + 18, y + 42, -7102027);
      this.drawButton(context, this.field_22789 / 2 - 100, y + 74, 96, 20, "Save", ThemeConfig.ACCENT);
      this.drawButton(context, this.field_22789 / 2 + 4, y + 74, 96, 20, "Back", -12102295);
      super.method_25394(context, mouseX, mouseY, delta);
   }

   public boolean method_25421() {
      return false;
   }
}
