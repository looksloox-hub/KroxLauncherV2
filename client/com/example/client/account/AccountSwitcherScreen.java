package com.example.client.account;

import com.example.client.mixin.MinecraftClientAccessor;
import com.example.client.ui.render.RoundedRectRenderer;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import net.minecraft.class_1068;
import net.minecraft.class_11905;
import net.minecraft.class_11908;
import net.minecraft.class_11909;
import net.minecraft.class_2561;
import net.minecraft.class_310;
import net.minecraft.class_320;
import net.minecraft.class_332;
import net.minecraft.class_3532;
import net.minecraft.class_437;
import net.minecraft.class_442;
import net.minecraft.class_7532;
import net.minecraft.class_8685;

public class AccountSwitcherScreen extends class_437 {
   private static final int BG_TOP = -16314342;
   private static final int BG_BOTTOM = -15722197;
   private static final int PANEL = -753789149;
   private static final int PANEL_HOVER = -535420366;
   private static final int PANEL_ACTIVE = -300406725;
   private static final int BORDER = 796693976;
   private static final int BORDER_HOVER = 1719440856;
   private static final int ACCENT = -7428609;
   private static final int ACCENT_SOFT = 864986623;
   private static final int SUCCESS = -9313359;
   private static final int SUCCESS_SOFT = 863101873;
   private static final int DANGER = -1086598;
   private static final int DANGER_SOFT = 871328634;
   private static final int TEXT = -1;
   private static final int TEXT_DIM = -690033409;
   private static final int TEXT_FAINT = -2002341177;
   private static final int SEARCH_MAX_LEN = 50;
   private static final int CARD_H = 64;
   private static final int CARD_GAP = 8;
   private static final int HEADER_H = 74;
   private static final int SIDE = 18;
   private final class_437 parent;
   private final List<Row> rows = new ArrayList();
   private final List<TopButton> topButtons = new ArrayList();
   private String searchText = "";
   private boolean searchFocused = false;
   private int scroll = 0;
   private float smoothScroll = 0.0F;
   private float openAnim = 0.0F;
   private String status = "";
   private long statusUntilMs = 0L;
   private int lastMouseX = 0;
   private int lastMouseY = 0;

   public AccountSwitcherScreen(class_437 parent) {
      super(class_2561.method_43470("OptiX Alt Manager"));
      this.parent = parent;
   }

   protected void method_25426() {
      super.method_25426();
      this.rows.clear();
      this.topButtons.clear();
      int panelW = Math.min(690, this.field_22789 - 24);
      int panelX = (this.field_22789 - panelW) / 2;
      this.buildTopButtons(panelX, panelW);

      for(AccountData acc : AccountManager.ACCOUNTS) {
         this.rows.add(new Row(acc));
      }

      this.scroll = class_3532.method_15340(this.scroll, 0, this.maxScroll());
      this.smoothScroll = (float)this.scroll;
      this.openAnim = 0.0F;
      this.status = "";
      this.statusUntilMs = 0L;
      this.searchText = "";
      this.searchFocused = false;
   }

   private void buildTopButtons(int panelX, int panelW) {
      this.topButtons.clear();
      int topY = 14;
      int x = panelX + 18;
      this.topButtons.add(new TopButton("Back", () -> class_310.method_1551().method_1507(this.parent), false, false));
      this.topButtons.add(new TopButton("+ Offline", () -> class_310.method_1551().method_1507(new AddOfflineAccountScreen(this)), true, false));
      this.topButtons.add(new TopButton("↻", () -> {
         AccountManager.refresh();
         this.rebuildRows();
         this.setStatus("Refreshed accounts");
      }, true, false));
      this.topButtons.add(new TopButton("⚙", () -> class_310.method_1551().method_1507(new ThemeEditorScreen(this)), true, false));
      int cursor = x;

      for(int i = 0; i < 2; ++i) {
         TopButton btn = (TopButton)this.topButtons.get(i);
         btn.w = btn.label.equals("Back") ? 56 : 78;
         btn.h = 24;
         btn.x = cursor;
         btn.y = topY;
         cursor += btn.w + 8;
      }

      int right = panelX + panelW - 18;

      for(int i = this.topButtons.size() - 1; i >= 2; --i) {
         TopButton btn = (TopButton)this.topButtons.get(i);
         btn.w = 28;
         btn.h = 24;
         right -= btn.w;
         btn.x = right;
         btn.y = topY;
         right -= 8;
      }

   }

   private void rebuildRows() {
      this.rows.clear();

      for(AccountData acc : AccountManager.ACCOUNTS) {
         this.rows.add(new Row(acc));
      }

      this.scroll = class_3532.method_15340(this.scroll, 0, this.maxScroll());
   }

   private void setStatus(String message) {
      this.status = message == null ? "" : message;
      this.statusUntilMs = System.currentTimeMillis() + 2500L;
   }

   private boolean isStatusVisible() {
      return !this.status.isBlank() && System.currentTimeMillis() <= this.statusUntilMs;
   }

   private static boolean over(double mx, double my, int x, int y, int w, int h) {
      return mx >= (double)x && mx <= (double)(x + w) && my >= (double)y && my <= (double)(y + h);
   }

   private static float clamp01(float v) {
      return Math.max(0.0F, Math.min(1.0F, v));
   }

   private static float lerp(float current, float target, float speed) {
      return current + (target - current) * speed;
   }

   private static float easeOutCubic(float t) {
      t = clamp01(t);
      float p = 1.0F - t;
      return 1.0F - p * p * p;
   }

   private int lerpColor(int a, int b, float t) {
      t = clamp01(t);
      int aa = a >>> 24 & 255;
      int ar = a >>> 16 & 255;
      int ag = a >>> 8 & 255;
      int ab = a & 255;
      int ba = b >>> 24 & 255;
      int br = b >>> 16 & 255;
      int bg = b >>> 8 & 255;
      int bb = b & 255;
      int ra = (int)((float)aa + (float)(ba - aa) * t);
      int rr = (int)((float)ar + (float)(br - ar) * t);
      int rg = (int)((float)ag + (float)(bg - ag) * t);
      int rb = (int)((float)ab + (float)(bb - ab) * t);
      return ra << 24 | rr << 16 | rg << 8 | rb;
   }

   private void drawPanel(class_332 context, int x, int y, int w, int h, int radius, int color) {
      RoundedRectRenderer.outline(context, (float)x, (float)y, (float)w, (float)h, Math.max(4.0F, (float)radius), 1.0F, color, color);
   }

   private void drawGlow(class_332 context, int x, int y, int w, int h, int radius, int color, int layers) {
      if (w > 0 && h > 0 && layers > 0) {
         int safeLayers = Math.min(layers, 3);
         boolean rounded = w <= 420 && h <= 260;

         for(int i = safeLayers; i >= 1; --i) {
            int pad = i * 2;
            int alpha = (int)((float)(color >>> 24 & 255) * 0.1F / (float)i);
            int c = alpha << 24 | color & 16777215;
            if (rounded) {
               RoundedRectRenderer.outline(context, (float)(x - pad), (float)(y - pad), (float)(w + pad * 2), (float)(h + pad * 2), Math.max(4.0F, (float)(radius + pad)), 1.0F, c, 0);
            } else {
               context.method_25294(x - pad, y - pad, x + w + pad, y - pad + 1, c);
               context.method_25294(x - pad, y + h + pad - 1, x + w + pad, y + h + pad, c);
               context.method_25294(x - pad, y - pad, x - pad + 1, y + h + pad, c);
               context.method_25294(x + w + pad - 1, y - pad, x + w + pad, y + h + pad, c);
            }
         }

      }
   }

   private void fillOutline(class_332 context, int x, int y, int w, int h, int fill, int border) {
      RoundedRectRenderer.outline(context, (float)x, (float)y, (float)w, (float)h, 14.0F, 1.0F, border, fill);
   }

   private void drawTopButton(class_332 context, TopButton button, boolean hover) {
      int fill = hover ? this.lerpColor(-753789149, button.danger ? 871328634 : 864986623, 0.18F) : -753789149;
      int border = hover ? (button.danger ? -1086598 : (button.accent ? -7428609 : 1719440856)) : 796693976;
      float radius = !button.label.equals("⚙") && !button.label.equals("↻") ? 12.0F : 11.0F;
      if (hover) {
         this.drawGlow(context, button.x, button.y, button.w, button.h, 12, button.danger ? 871328634 : 864986623, 2);
      }

      RoundedRectRenderer.outline(context, (float)button.x, (float)button.y, (float)button.w, (float)button.h, radius, 1.0F, border, fill);
      String label = button.label;
      int textX = button.x + (button.w - this.field_22793.method_1727(label)) / 2;
      context.method_27535(this.field_22793, class_2561.method_43470(label), textX, button.y + 8, -1);
   }

   private void drawCardButton(class_332 context, int x, int y, int w, int h, String text, int accent, boolean hover, boolean danger) {
      int fill = hover ? -535420366 : -753789149;
      int border = hover ? accent : 796693976;
      if (hover) {
         this.drawGlow(context, x, y, w, h, 12, danger ? 871328634 : 864986623, 2);
      }

      RoundedRectRenderer.outline(context, (float)x, (float)y, (float)w, (float)h, 12.0F, 1.0F, border, fill);
      context.method_27534(this.field_22793, class_2561.method_43470(text), x + w / 2, y + h / 2 - 4, -1);
   }

   private List<AccountData> filteredAccounts() {
      String search = this.searchText.trim().toLowerCase();
      List<AccountData> list = new ArrayList();

      for(AccountData acc : AccountManager.ACCOUNTS) {
         if (search.isBlank() || acc.username.toLowerCase().contains(search)) {
            list.add(acc);
         }
      }

      return list;
   }

   private int maxScroll() {
      int content = this.filteredAccounts().size() * 72;
      int visible = Math.max(0, this.field_22790 - 128);
      return Math.max(0, content - visible);
   }

   private class_8685 getSkinTexturesForCurrentAccount(AccountData acc) {
      class_310 mc = class_310.method_1551();

      UUID uuid;
      try {
         if (acc.uuid != null && !acc.uuid.isBlank()) {
            uuid = UUID.fromString(acc.uuid);
         } else {
            uuid = mc.method_1548().method_44717();
            if (uuid == null) {
               uuid = UUID.nameUUIDFromBytes(("OfflinePlayer:" + acc.username).getBytes(StandardCharsets.UTF_8));
            }
         }
      } catch (Throwable var5) {
         uuid = UUID.nameUUIDFromBytes(("OfflinePlayer:" + acc.username).getBytes(StandardCharsets.UTF_8));
      }

      return class_1068.method_4648(uuid);
   }

   private static boolean sameNullableString(String a, String b) {
      return a == null ? b == null : a.equals(b);
   }

   private Row findRow(AccountData acc) {
      for(Row row : this.rows) {
         if (row.account == acc) {
            return row;
         }

         if (row.account != null && acc != null && row.account.username.equals(acc.username) && sameNullableString(row.account.uuid, acc.uuid)) {
            return row;
         }
      }

      return null;
   }

   private void switchAccount(AccountData acc) {
      try {
         class_310 mc = class_310.method_1551();
         class_320 newSession = acc.toSession();
         boolean applied = false;

         try {
            ((MinecraftClientAccessor)mc).setSession(newSession);
            applied = true;
         } catch (Throwable var7) {
         }

         if (!applied) {
            try {
               Method m = class_310.class.getDeclaredMethod("setSession", class_320.class);
               m.setAccessible(true);
               m.invoke(mc, newSession);
               applied = true;
            } catch (Throwable var6) {
            }
         }

         if (!applied) {
            throw new IllegalStateException("Session could not be applied");
         }

         mc.method_1507(new class_442());
         this.setStatus("Switched to " + acc.username + " — reconnect to apply");
      } catch (Exception e) {
         e.printStackTrace();
         this.setStatus("Switch failed");
      }

   }

   private void drawBackground(class_332 context) {
      context.method_25296(0, 0, this.field_22789, this.field_22790, -16314342, -15722197);
      context.method_25294(0, 0, this.field_22789, this.field_22790, 1510082578);
      this.drawGlow(context, 0, 0, this.field_22789, this.field_22790, 0, 271268711, 1);
   }

   private void updateAnimation(float delta) {
      float dt = Math.max(0.0F, Math.min(delta, 0.05F));
      this.openAnim = class_3532.method_15363(this.openAnim + dt * 6.5F, 0.0F, 1.0F);
      this.smoothScroll = lerp(this.smoothScroll, (float)this.scroll, 0.18F);
      if (Math.abs(this.smoothScroll - (float)this.scroll) < 0.15F) {
         this.smoothScroll = (float)this.scroll;
      }

   }

   public void method_25394(class_332 context, int mouseX, int mouseY, float delta) {
      this.lastMouseX = mouseX;
      this.lastMouseY = mouseY;
      this.updateAnimation(delta);
      float ui = easeOutCubic(this.openAnim);
      float popupLift = (1.0F - ui) * 12.0F;
      float popupScale = 0.975F + ui * 0.025F;
      this.drawBackground(context);
      int panelW = Math.min(690, this.field_22789 - 24);
      int panelH = Math.min(400, this.field_22790 - 24);
      int panelX = (this.field_22789 - panelW) / 2;
      int panelY = (this.field_22790 - panelH) / 2;
      int drawW = Math.round((float)panelW * popupScale);
      int drawH = Math.round((float)panelH * popupScale);
      int drawX = Math.round((float)panelX + (float)panelW * (1.0F - popupScale) / 2.0F);
      int drawY = Math.round((float)panelY + (float)panelH * (1.0F - popupScale) / 2.0F + popupLift);
      this.drawGlow(context, drawX, drawY, drawW, drawH, 18, 286990641, 2);
      this.fillOutline(context, drawX, drawY, drawW, drawH, -753789149, 796693976);
      context.method_27535(this.field_22793, class_2561.method_43470("OptiX Alt Manager"), drawX + 18, drawY + 10, -1);
      context.method_27535(this.field_22793, class_2561.method_43470(""), drawX + 18, drawY + 24, -690033409);
      int var10000 = this.filteredAccounts().size();
      String countText = var10000 + " account" + (this.filteredAccounts().size() == 1 ? "" : "s");
      int countW = this.field_22793.method_1727(countText);
      context.method_27535(this.field_22793, class_2561.method_43470(countText), drawX + drawW - 18 - countW, drawY + 12, -690033409);

      for(TopButton btn : this.topButtons) {
         this.drawTopButton(context, btn, over((double)mouseX, (double)mouseY, btn.x, btn.y, btn.w, btn.h));
      }

      int searchX = drawX + 18;
      int searchY = drawY + 42;
      int searchW = drawW - 36;
      int searchH = 24;
      boolean searchHover = over((double)mouseX, (double)mouseY, searchX, searchY, searchW, searchH);
      int searchFill = this.searchFocused ? -300406725 : -535420366;
      int searchBorder = this.searchFocused ? -7428609 : 796693976;
      if (searchHover || this.searchFocused) {
         this.drawGlow(context, searchX, searchY, searchW, searchH, 12, 864986623, 2);
      }

      RoundedRectRenderer.outline(context, (float)searchX, (float)searchY, (float)searchW, (float)searchH, 12.0F, 1.0F, searchBorder, searchFill);
      context.method_27535(this.field_22793, class_2561.method_43470("⌕"), searchX + 10, searchY + 8, -2002341177);
      if (this.searchText.isEmpty()) {
         context.method_27535(this.field_22793, class_2561.method_43470("Search accounts..."), searchX + 26, searchY + 8, -2002341177);
      } else {
         context.method_27535(this.field_22793, class_2561.method_43470(this.searchText), searchX + 26, searchY + 8, -1);
      }

      if (this.searchFocused && System.currentTimeMillis() / 350L % 2L == 0L) {
         int caretX = searchX + 26 + this.field_22793.method_1727(this.searchText);
         context.method_25294(caretX + 1, searchY + 6, caretX + 2, searchY + 18, -1);
      }

      int listTop = drawY + 74 + 8;
      int listBottom = drawY + drawH - 18;
      int listX = drawX + 18;
      int listW = drawW - 36;
      int max = this.maxScroll();
      this.scroll = class_3532.method_15340(this.scroll, 0, max);
      List<AccountData> accounts = this.filteredAccounts();
      int startY = listTop - Math.round(this.smoothScroll);
      context.method_44379(listX, listTop, listX + listW, listBottom);
      if (accounts.isEmpty()) {
         int emptyY = listTop + 38;
         RoundedRectRenderer.outline(context, (float)listX, (float)emptyY, (float)listW, 76.0F, 16.0F, 1.0F, 796693976, -535420366);
         context.method_27534(this.field_22793, class_2561.method_43470("No matching accounts"), drawX + drawW / 2, emptyY + 20, -1);
         context.method_27534(this.field_22793, class_2561.method_43470("Try a different search term or add a new account"), drawX + drawW / 2, emptyY + 36, -2002341177);
      }

      String currentUser = class_310.method_1551().method_1548().method_1676();

      for(int i = 0; i < accounts.size(); ++i) {
         AccountData acc = (AccountData)accounts.get(i);
         Row row = this.findRow(acc);
         if (row != null) {
            int y = startY + i * 72;
            boolean visible = y + 64 >= listTop - 2 && y <= listBottom + 2;
            row.visible = visible;
            if (visible) {
               boolean hovered = over((double)mouseX, (double)mouseY, listX, y, listW, 64);
               boolean active = currentUser != null && currentUser.equalsIgnoreCase(acc.username);
               this.renderAccountRow(context, row, acc, listX, y, listW, 64, hovered, active);
            }
         }
      }

      context.method_44380();
      if (this.isStatusVisible()) {
         int statusW = Math.max(180, this.field_22793.method_1727(this.status) + 24);
         int sx = drawX + 18;
         int sy = drawY + drawH - 30;
         RoundedRectRenderer.outline(context, (float)sx, (float)sy, (float)statusW, 20.0F, 10.0F, 1.0F, 796693976, -1290527188);
         context.method_27535(this.field_22793, class_2561.method_43470(this.status), sx + 10, sy + 7, -690033409);
      }

      super.method_25394(context, mouseX, mouseY, delta);
   }

   private void renderAccountRow(class_332 context, Row row, AccountData acc, int x, int y, int w, int h, boolean hovered, boolean active) {
      row.x = x;
      row.y = y;
      row.w = w;
      row.h = h;
      int bg = active ? -300406725 : (hovered ? -535420366 : -753789149);
      int border = active ? -7428609 : (hovered ? 1719440856 : 796693976);
      if (hovered) {
         this.drawGlow(context, x, y, w, h, 12, 864986623, 2);
      }

      RoundedRectRenderer.outline(context, (float)x, (float)y, (float)w, (float)h, 16.0F, 1.0F, border, bg);
      int avatarSize = 30;
      int avatarX = x + 12;
      int avatarY = y + (h - avatarSize) / 2;

      try {
         class_8685 textures = this.getSkinTexturesForCurrentAccount(acc);
         class_7532.method_52722(context, textures, avatarX, avatarY, avatarSize);
      } catch (Throwable var18) {
         int avatarFill = acc.displayType().toLowerCase().contains("micro") ? -13804545 : -10984845;
         RoundedRectRenderer.outline(context, (float)avatarX, (float)avatarY, (float)avatarSize, (float)avatarSize, 8.0F, 1.0F, border, avatarFill);
         String letter = acc.username.isBlank() ? "?" : acc.username.substring(0, 1).toUpperCase();
         context.method_27534(this.field_22793, class_2561.method_43470(letter), avatarX + avatarSize / 2, avatarY + 10, -1);
      }

      context.method_27535(this.field_22793, class_2561.method_43470(acc.username), x + 52, y + 10, -1);
      context.method_27535(this.field_22793, class_2561.method_43470(acc.displayType()), x + 52, y + 25, active ? -9313359 : -690033409);
      String uuid = acc.uuid == null ? "" : acc.uuid;
      String shortUuid = uuid.length() > 18 ? uuid.substring(0, 18) + "…" : uuid;
      context.method_27535(this.field_22793, class_2561.method_43470(shortUuid), x + 52, y + 40, -2002341177);
      if (active) {
         RoundedRectRenderer.outline(context, (float)(x + w - 92), (float)(y + 10), 78.0F, 16.0F, 8.0F, 1.0F, -9313359, 863101873);
         context.method_27534(this.field_22793, class_2561.method_43470("ACTIVE"), x + w - 53, y + 14, -9313359);
      }

      row.loginW = 58;
      row.loginH = 24;
      row.editW = 46;
      row.editH = 24;
      row.deleteW = 22;
      row.deleteH = 24;
      row.loginX = x + w - 162;
      row.loginY = y + 20;
      row.editX = row.loginX + row.loginW + 6;
      row.editY = row.loginY;
      row.deleteX = row.editX + row.editW + 6;
      row.deleteY = row.loginY;
      this.drawCardButton(context, row.loginX, row.loginY, row.loginW, row.loginH, "Login", -7428609, hovered && over((double)this.lastMouseX, (double)this.lastMouseY, row.loginX, row.loginY, row.loginW, row.loginH), false);
      this.drawCardButton(context, row.editX, row.editY, row.editW, row.editH, "Edit", -10062710, hovered && over((double)this.lastMouseX, (double)this.lastMouseY, row.editX, row.editY, row.editW, row.editH), false);
      this.drawCardButton(context, row.deleteX, row.deleteY, row.deleteW, row.deleteH, "×", -1086598, hovered && over((double)this.lastMouseX, (double)this.lastMouseY, row.deleteX, row.deleteY, row.deleteW, row.deleteH), true);
   }

   public boolean method_25401(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
      int panelW = Math.min(690, this.field_22789 - 24);
      int panelH = Math.min(400, this.field_22790 - 24);
      int panelX = (this.field_22789 - panelW) / 2;
      int panelY = (this.field_22790 - panelH) / 2;
      float ui = easeOutCubic(this.openAnim);
      float popupLift = (1.0F - ui) * 12.0F;
      float popupScale = 0.975F + ui * 0.025F;
      int drawW = Math.round((float)panelW * popupScale);
      int drawH = Math.round((float)panelH * popupScale);
      int drawX = Math.round((float)panelX + (float)panelW * (1.0F - popupScale) / 2.0F);
      int drawY = Math.round((float)panelY + (float)panelH * (1.0F - popupScale) / 2.0F + popupLift);
      int listTop = drawY + 74 + 8;
      int listBottom = drawY + drawH - 18;
      int listX = drawX + 18;
      int listW = drawW - 36;
      if (over(mouseX, mouseY, listX, listTop, listW, listBottom - listTop)) {
         this.scroll = class_3532.method_15340(this.scroll - (int)(verticalAmount * (double)24.0F), 0, this.maxScroll());
         return true;
      } else {
         return super.method_25401(mouseX, mouseY, horizontalAmount, verticalAmount);
      }
   }

   public boolean method_25402(class_11909 click, boolean doubled) {
      double mouseX = click.comp_4798();
      double mouseY = click.comp_4799();
      int button = click.method_74245();
      this.lastMouseX = (int)mouseX;
      this.lastMouseY = (int)mouseY;
      int panelW = Math.min(690, this.field_22789 - 24);
      int panelH = Math.min(400, this.field_22790 - 24);
      int panelX = (this.field_22789 - panelW) / 2;
      int panelY = (this.field_22790 - panelH) / 2;
      float ui = easeOutCubic(this.openAnim);
      float popupLift = (1.0F - ui) * 12.0F;
      float popupScale = 0.975F + ui * 0.025F;
      int drawW = Math.round((float)panelW * popupScale);
      Math.round((float)panelH * popupScale);
      int drawX = Math.round((float)panelX + (float)panelW * (1.0F - popupScale) / 2.0F);
      int drawY = Math.round((float)panelY + (float)panelH * (1.0F - popupScale) / 2.0F + popupLift);

      for(TopButton btn : this.topButtons) {
         if (over(mouseX, mouseY, btn.x, btn.y, btn.w, btn.h)) {
            btn.action.run();
            return true;
         }
      }

      int searchX = drawX + 18;
      int searchY = drawY + 42;
      int searchW = drawW - 36;
      int searchH = 24;
      if (over(mouseX, mouseY, searchX, searchY, searchW, searchH)) {
         this.searchFocused = true;
         return true;
      } else {
         this.searchFocused = false;
         List<AccountData> accounts = this.filteredAccounts();
         int listTop = drawY + 74 + 8;
         int listX = drawX + 18;
         int listW = drawW - 36;
         int startY = listTop - Math.round(this.smoothScroll);

         for(int i = 0; i < accounts.size(); ++i) {
            AccountData acc = (AccountData)accounts.get(i);
            Row row = this.findRow(acc);
            if (row != null && row.visible) {
               int y = startY + i * 72;
               if (over(mouseX, mouseY, listX, y, listW, 64)) {
                  if (over(mouseX, mouseY, row.loginX, row.loginY, row.loginW, row.loginH)) {
                     this.switchAccount(acc);
                     return true;
                  }

                  if (over(mouseX, mouseY, row.editX, row.editY, row.editW, row.editH)) {
                     class_310.method_1551().method_1507(new RenameAccountScreen(this, acc));
                     return true;
                  }

                  if (over(mouseX, mouseY, row.deleteX, row.deleteY, row.deleteW, row.deleteH)) {
                     AccountManager.remove(acc);
                     this.rebuildRows();
                     this.scroll = class_3532.method_15340(this.scroll, 0, this.maxScroll());
                     this.setStatus("Removed account");
                     return true;
                  }

                  if (button == 0) {
                     this.switchAccount(acc);
                     return true;
                  }
               }
            }
         }

         return super.method_25402(click, doubled);
      }
   }

   public boolean method_25404(class_11908 input) {
      int keyCode = input.comp_4795();
      if (this.searchFocused) {
         if (keyCode == 259) {
            if (!this.searchText.isEmpty()) {
               this.searchText = this.searchText.substring(0, this.searchText.length() - 1);
            }

            return true;
         }

         if (keyCode == 257 || keyCode == 335 || keyCode == 256) {
            this.searchFocused = false;
            return true;
         }
      }

      if (keyCode == 256) {
         class_310.method_1551().method_1507(this.parent);
         return true;
      } else {
         return super.method_25404(input);
      }
   }

   public boolean method_25400(class_11905 input) {
      if (this.searchFocused) {
         String s = input.method_74226();
         if (s != null && !s.isEmpty()) {
            char c = s.charAt(0);
            if (c >= ' ' && c != 127 && this.searchText.length() < 50) {
               this.searchText = this.searchText + c;
               this.scroll = class_3532.method_15340(this.scroll, 0, this.maxScroll());
               return true;
            }
         }
      }

      return super.method_25400(input);
   }

   public void method_25419() {
      class_310.method_1551().method_1507(this.parent);
   }

   public boolean method_25421() {
      return false;
   }

   private static class Row {
      final AccountData account;
      int x;
      int y;
      int w;
      int h;
      int loginX;
      int loginY;
      int loginW;
      int loginH;
      int editX;
      int editY;
      int editW;
      int editH;
      int deleteX;
      int deleteY;
      int deleteW;
      int deleteH;
      boolean visible;

      Row(AccountData account) {
         this.account = account;
      }
   }

   private static class TopButton {
      final String label;
      final Runnable action;
      final boolean accent;
      final boolean danger;
      int x;
      int y;
      int w;
      int h;

      TopButton(String label, Runnable action, boolean accent, boolean danger) {
         this.label = label;
         this.action = action;
         this.accent = accent;
         this.danger = danger;
      }
   }
}
