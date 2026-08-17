package com.example.client.ui;

import com.example.client.module.Category;
import com.example.client.module.Module;
import com.example.client.module.ModuleManager;
import com.example.client.setting.BooleanSetting;
import com.example.client.setting.ModeSetting;
import com.example.client.setting.NumberSetting;
import com.example.client.setting.Setting;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.Predicate;
import net.minecraft.class_11905;
import net.minecraft.class_11908;
import net.minecraft.class_11909;
import net.minecraft.class_2561;
import net.minecraft.class_310;
import net.minecraft.class_332;
import net.minecraft.class_437;

public class CsClientModulesScreen extends class_437 {
   private static final class_310 MC = class_310.method_1551();
   private final class_437 parent;
   private static final int PANEL_W = 1320;
   private static final int PANEL_H = 760;
   private static final int HEADER_H = 84;
   private static final int TABS_H = 54;
   private static final int CONTENT_TOP = 138;
   private static final int GRID_COLS = 4;
   private static final int CARD_GAP_X = 14;
   private static final int CARD_GAP_Y = 16;
   private static final int CARD_W = 292;
   private static final int CARD_H = 208;
   private static final int TAB_W = 78;
   private static final int TAB_H = 44;
   private static final int TAB_GAP = 10;
   private static final int SEARCH_W = 262;
   private static final int SEARCH_H = 44;
   private static final int OVERLAY = -1442840576;
   private static final int PANEL = -804780016;
   private static final int PANEL_2 = -921365214;
   private static final int CARD = -1340861408;
   private static final int CARD_HOVER = -1340598232;
   private static final int CARD_ACTIVE = -1340861408;
   private static final int BORDER = 570425344;
   private static final int BORDER_LIGHT = 654311423;
   private static final int TEXT = -986896;
   private static final int TEXT_DIM = -6249809;
   private static final int TEXT_FAINT = -8486768;
   private static final int ORANGE = -34262;
   private static final int ORANGE_SOFT = 587168298;
   private static final int ENABLED_BAR = -1708398037;
   private static final int DISABLED_BAR = -1609165278;
   private int panelX;
   private int panelY;
   private int panelW;
   private int panelH;
   private final List<TabSpec> tabs = new ArrayList();
   private final List<ModuleCard> cards = new ArrayList();
   private final Map<String, Float> scrollPositions = new HashMap();
   private final SearchField searchField = new SearchField();
   private boolean searchFocused = false;
   private float targetScroll = 0.0F;
   private float smoothScroll = 0.0F;
   private String lastScrollKey = "ALL";
   private Module popupModule = null;
   private boolean popupOpen = false;
   private String activeTab = "ALL";

   public CsClientModulesScreen(class_437 parent) {
      super(class_2561.method_43470("CS x Optix Client"));
      this.parent = parent;
   }

   public static void open(class_437 parent) {
      MC.method_1507(new CsClientModulesScreen(parent));
   }

   protected void method_25426() {
      super.method_25426();
      ModuleManager.init();
      this.panelW = Math.min(1320, this.field_22789 - 32);
      this.panelH = Math.min(760, this.field_22790 - 32);
      this.panelX = (this.field_22789 - this.panelW) / 2;
      this.panelY = (this.field_22790 - this.panelH) / 2;
      this.searchField.setBounds(this.panelX + this.panelW - 262 - 26, this.panelY + 84 + 18, 262, 44);
      this.tabs.clear();
      this.buildTabs();
      this.scrollPositions.putIfAbsent("ALL", 0.0F);
      this.scrollPositions.putIfAbsent("HUD", 0.0F);
      this.scrollPositions.putIfAbsent("RENDERER", 0.0F);
      this.scrollPositions.putIfAbsent("MECHANICS", 0.0F);
      this.scrollPositions.putIfAbsent("MISC", 0.0F);
   }

   private void buildTabs() {
      this.tabs.clear();
      int tabY = this.panelY + 84 + 14;
      int startX = this.panelX + 18;
      this.tabs.add(new TabSpec("ALL", "All", startX, tabY, 78, 44, this::matchesAll));
      this.tabs.add(new TabSpec("HUD", "HUD", startX + 88, tabY, 78, 44, this::matchesHud));
      this.tabs.add(new TabSpec("RENDERER", "Renderer", startX + 176, tabY, 78, 44, this::matchesRenderer));
      this.tabs.add(new TabSpec("MECHANICS", "Mechanics", startX + 264, tabY, 78, 44, this::matchesMechanics));
      this.tabs.add(new TabSpec("MISC", "Misc", startX + 352, tabY, 78, 44, this::matchesMisc));
   }

   public boolean method_25421() {
      return false;
   }

   public void method_25393() {
      this.searchField.tick();

      for(TabSpec tab : this.tabs) {
         tab.tick();
      }

      for(ModuleCard card : this.cards) {
         card.tick();
      }

   }

   public void method_25394(class_332 context, int mouseX, int mouseY, float delta) {
      this.renderBackdrop(context);
      this.renderPanel(context);
      this.renderHeader(context, mouseX, mouseY);
      this.renderTabs(context, mouseX, mouseY);
      this.searchField.render(context, mouseX, mouseY);
      this.renderModuleGrid(context, mouseX, mouseY);
      this.renderPopup(context, mouseX, mouseY);
      this.renderFooter(context);
      super.method_25394(context, mouseX, mouseY, delta);
   }

   private void renderBackdrop(class_332 context) {
      context.method_25294(0, 0, this.field_22789, this.field_22790, -1442840576);
   }

   private void renderPanel(class_332 context) {
      context.method_25294(this.panelX, this.panelY, this.panelX + this.panelW, this.panelY + this.panelH, -804780016);
      context.method_25294(this.panelX, this.panelY, this.panelX + this.panelW, this.panelY + 2, -34262);
      drawBorder(context, this.panelX, this.panelY, this.panelW, this.panelH, 654311423);
   }

   private void renderHeader(class_332 context, int mouseX, int mouseY) {
      int backX = this.panelX + 18;
      int backY = this.panelY + 18;
      int backS = 40;
      boolean backHovered = this.isInside((double)mouseX, (double)mouseY, backX, backY, backS, backS);
      context.method_25294(backX, backY, backX + backS, backY + backS, backHovered ? -15460318 : -15592420);
      drawBorder(context, backX, backY, backS, backS, backHovered ? 587168298 : 570425344);
      context.method_27534(this.field_22793, class_2561.method_43470("←"), backX + backS / 2, backY + 12, backHovered ? -34262 : -986896);
      int logoX = this.panelX + 72;
      int logoY = this.panelY + 20;
      this.drawLogo(context, logoX, logoY);
      context.method_51439(this.field_22793, class_2561.method_43470("CS x Optix Client"), this.panelX + 108, this.panelY + 18, -986896, false);
      context.method_51439(this.field_22793, class_2561.method_43470("v2.0 • MC 1.21.11"), this.panelX + 108, this.panelY + 34, -6249809, false);
      String countText = this.getVisibleModules().size() + " modules";
      int pillW = Math.max(112, this.field_22793.method_1727(countText) + 24);
      int pillH = 30;
      int pillX = this.panelX + this.panelW - pillW - 18;
      int pillY = this.panelY + 20;
      context.method_25294(pillX, pillY, pillX + pillW, pillY + pillH, -921365214);
      drawBorder(context, pillX, pillY, pillW, pillH, 654311423);
      context.method_27534(this.field_22793, class_2561.method_43470(countText), pillX + pillW / 2, pillY + 10, -986896);
   }

   private void drawLogo(class_332 context, int x, int y) {
      context.method_25294(x + 2, y + 8, x + 11, y + 16, -34262);
      context.method_25294(x + 8, y + 2, x + 17, y + 10, -19922);
      context.method_25294(x + 10, y + 10, x + 21, y + 20, -28886);
      context.method_25294(x + 5, y + 16, x + 15, y + 24, -11702);
      context.method_25294(x + 14, y + 7, x + 20, y + 13, -1052689);
   }

   private void renderTabs(class_332 context, int mouseX, int mouseY) {
      for(TabSpec tab : this.tabs) {
         tab.render(context, mouseX, mouseY);
      }

   }

   private void renderModuleGrid(class_332 context, int mouseX, int mouseY) {
      this.cards.clear();
      List<Module> visible = this.getVisibleModules();
      visible.sort(Comparator.comparing(Module::getName, String.CASE_INSENSITIVE_ORDER));
      String key = this.viewKey();
      if (!key.equals(this.lastScrollKey)) {
         this.smoothScroll = (Float)this.scrollPositions.getOrDefault(key, 0.0F);
         this.targetScroll = this.smoothScroll;
         this.lastScrollKey = key;
      }

      int gridX = this.panelX + 22;
      int gridTop = this.panelY + 138 + 18;
      int gridRight = this.panelX + this.panelW - 22;
      int usableW = gridRight - gridX;
      int computedCardW = (usableW - 42) / 4;
      int cardW = Math.min(292, computedCardW);
      int totalGridW = 4 * cardW + 42;
      int offsetX = Math.max(0, (usableW - totalGridW) / 2);
      int contentStartX = gridX + offsetX;
      int rows = Math.max(1, (int)Math.ceil((double)visible.size() / (double)4.0F));
      int totalH = rows * 208 + Math.max(0, rows - 1) * 16;
      int viewportH = this.panelH - 138 - 42;
      float maxScroll = (float)Math.max(0, totalH - viewportH);
      this.targetScroll = clamp(this.targetScroll, 0.0F, maxScroll);
      this.smoothScroll = lerp(this.smoothScroll, this.targetScroll, 0.18F);
      int clipL = this.panelX + 12;
      int clipT = this.panelY + 138 + 10;
      int clipR = this.panelX + this.panelW - 18;
      int clipB = this.panelY + this.panelH - 34;
      context.method_44379(clipL, clipT, clipR, clipB);

      for(int i = 0; i < visible.size(); ++i) {
         Module module = (Module)visible.get(i);
         int col = i % 4;
         int row = i / 4;
         int x = contentStartX + col * (cardW + 14);
         int y = (int)((float)(gridTop + row * 224) - this.smoothScroll);
         if (x <= clipR && x + cardW >= clipL && y <= clipB && y + 208 >= clipT) {
            ModuleCard card = new ModuleCard(module, x, y, cardW, 208);
            this.cards.add(card);
            card.render(context, mouseX, mouseY);
         }
      }

      context.method_44380();
      if (maxScroll > 0.5F) {
         this.drawScrollbar(context, maxScroll, this.smoothScroll, clipT, clipB);
      }

   }

   private void drawScrollbar(class_332 context, float maxScroll, float currentScroll, int top, int bottom) {
      int barX = this.panelX + this.panelW - 8;
      int barW = 2;
      int barH = bottom - top;
      context.method_25294(barX, top, barX + barW, bottom, 385875968);
      int handleH = Math.max(24, (int)((float)barH * ((float)barH / ((float)barH + maxScroll))));
      int handleY = top + (int)(currentScroll / maxScroll * (float)(barH - handleH));
      context.method_25294(barX, handleY, barX + barW, handleY + handleH, -34262);
   }

   private void renderPopup(class_332 context, int mouseX, int mouseY) {
      if (this.popupOpen && this.popupModule != null) {
         int x = this.panelX + this.panelW - 286;
         int y = this.panelY + 138 + 14;
         int w = 254;
         int h = 248;
         context.method_25294(x, y, x + w, y + h, -401468896);
         context.method_25294(x, y, x + w, y + 2, -34262);
         drawBorder(context, x, y, w, h, 654311423);
         context.method_51439(this.field_22793, class_2561.method_43470("Module Config"), x + 16, y + 14, -986896, false);
         context.method_51439(this.field_22793, class_2561.method_43470(this.popupModule.getName()), x + 16, y + 30, -6249809, false);
         int bx = x + 14;
         int by = y + 56;
         int bw = w - 28;
         int bh = 24;
         int gap = 8;
         this.drawPopupButton(context, bx, by, bw, bh, "Close", mouseX, mouseY);
         this.drawPopupButton(context, bx, by + bh + gap, bw, bh, "Toggle", mouseX, mouseY);
         this.drawPopupButton(context, bx, by + 2 * (bh + gap), bw, bh, "Reset Position", mouseX, mouseY);
         this.drawPopupButton(context, bx, by + 3 * (bh + gap), bw, bh, "Reset Scale", mouseX, mouseY);
         int sy = by + 4 * (bh + gap) + 10;
         context.method_51439(this.field_22793, class_2561.method_43470("Settings"), bx, sy, -986896, false);
         sy += 14;
         context.method_44379(x + 12, sy, x + w - 12, y + h - 12);

         for(Setting setting : this.popupModule.getSettings()) {
            String line = setting.getName();
            if (setting instanceof BooleanSetting) {
               BooleanSetting bool = (BooleanSetting)setting;
               line = line + ": " + (bool.getValue() ? "ON" : "OFF");
            } else if (setting instanceof ModeSetting) {
               ModeSetting mode = (ModeSetting)setting;
               line = line + ": " + mode.getMode();
            } else if (setting instanceof NumberSetting) {
               NumberSetting num = (NumberSetting)setting;
               line = line + ": " + String.format(Locale.ROOT, "%.1f", num.getValue());
            }

            context.method_25294(bx, sy - 1, bx + bw, sy + 10, 570425344);
            context.method_51439(this.field_22793, class_2561.method_43470(line), bx + 4, sy, -6249809, false);
            sy += 14;
         }

         context.method_44380();
      }
   }

   private void drawPopupButton(class_332 context, int x, int y, int w, int h, String label, int mouseX, int mouseY) {
      boolean hovered = this.isInside((double)mouseX, (double)mouseY, x, y, w, h);
      context.method_25294(x, y, x + w, y + h, hovered ? -15262442 : -921365214);
      drawBorder(context, x, y, w, h, hovered ? 587168298 : 570425344);
      context.method_27534(this.field_22793, class_2561.method_43470(label), x + w / 2, y + 8, hovered ? -986896 : -6249809);
   }

   private void renderFooter(class_332 context) {
      context.method_27534(this.field_22793, class_2561.method_43470("Right Shift to close • Click module to toggle • ⚙ for settings"), this.panelX + this.panelW / 2, this.panelY + this.panelH - 18, -8486768);
   }

   private List<Module> getVisibleModules() {
      List<Module> out = new ArrayList();

      for(Module module : ModuleManager.getModules()) {
         if (module != null && this.matchesCurrentTab(module) && this.matchesSearch(module)) {
            out.add(module);
         }
      }

      return out;
   }

   private boolean matchesCurrentTab(Module module) {
      for(TabSpec tab : this.tabs) {
         if (tab.key.equals(this.activeTab)) {
            return tab.matcher.test(module);
         }
      }

      return true;
   }

   private boolean matchesSearch(Module module) {
      String q = this.searchField.getText().trim().toLowerCase(Locale.ROOT);
      if (q.isEmpty()) {
         return true;
      } else {
         String name = module.getName().toLowerCase(Locale.ROOT);
         String category = module.getCategory() == null ? "" : module.getCategory().getDisplayName().toLowerCase(Locale.ROOT);
         String raw = module.getCategory() == null ? "" : module.getCategory().name().toLowerCase(Locale.ROOT);
         return name.contains(q) || category.contains(q) || raw.contains(q);
      }
   }

   private String viewKey() {
      return this.activeTab == null ? "ALL" : this.activeTab;
   }

   private void setTab(String key) {
      this.activeTab = key;
      this.popupOpen = false;
      this.popupModule = null;
      String scrollKey = this.viewKey();
      this.targetScroll = (Float)this.scrollPositions.getOrDefault(scrollKey, 0.0F);
      this.smoothScroll = this.targetScroll;
      this.lastScrollKey = scrollKey;
   }

   public boolean method_25401(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
      if (this.searchFocused) {
         return super.method_25401(mouseX, mouseY, horizontalAmount, verticalAmount);
      } else if (!this.isInsideGrid(mouseX, mouseY)) {
         return super.method_25401(mouseX, mouseY, horizontalAmount, verticalAmount);
      } else {
         List<Module> visible = this.getVisibleModules();
         int rows = Math.max(1, (int)Math.ceil((double)visible.size() / (double)4.0F));
         int gridX = this.panelX + 22;
         int gridRight = this.panelX + this.panelW - 22;
         int usableW = gridRight - gridX;
         int computedCardW = (usableW - 42) / 4;
         int cardW = Math.min(292, computedCardW);
         int totalH = rows * 208 + Math.max(0, rows - 1) * 16;
         int viewportH = this.panelH - 138 - 42;
         float maxScroll = (float)Math.max(0, totalH - viewportH);
         String key = this.viewKey();
         float current = (Float)this.scrollPositions.getOrDefault(key, 0.0F);
         current = clamp(current - (float)verticalAmount * 30.0F, 0.0F, maxScroll);
         this.scrollPositions.put(key, current);
         this.targetScroll = current;
         this.lastScrollKey = key;
         return true;
      }
   }

   private boolean isInsideGrid(double mouseX, double mouseY) {
      return mouseX >= (double)this.panelX && mouseX <= (double)(this.panelX + this.panelW) && mouseY >= (double)(this.panelY + 138) && mouseY <= (double)(this.panelY + this.panelH);
   }

   public boolean method_25402(class_11909 click, boolean doubled) {
      double mouseX = click.comp_4798();
      double mouseY = click.comp_4799();
      int button = click.method_74245();
      if (button == 0 && this.isInside(mouseX, mouseY, this.panelX + 18, this.panelY + 18, 40, 40)) {
         MC.method_1507(this.parent);
         return true;
      } else if (this.searchField.mouseClicked(mouseX, mouseY, button)) {
         this.searchFocused = this.searchField.isFocused();
         return true;
      } else {
         for(TabSpec tab : this.tabs) {
            if (tab.mouseClicked(mouseX, mouseY, button)) {
               this.setTab(tab.key);
               return true;
            }
         }

         if (this.popupOpen && this.popupModule != null && this.handlePopupClick(mouseX, mouseY, button)) {
            return true;
         } else {
            for(ModuleCard card : this.cards) {
               if (card.mouseClicked(mouseX, mouseY, button)) {
                  return true;
               }
            }

            return super.method_25402(click, doubled);
         }
      }
   }

   private boolean handlePopupClick(double mouseX, double mouseY, int button) {
      int x = this.panelX + this.panelW - 286;
      int y = this.panelY + 138 + 14;
      int w = 254;
      int bh = 24;
      int gap = 8;
      int bx = x + 14;
      int by = y + 56;
      if (button != 0) {
         return false;
      } else if (this.isInside(mouseX, mouseY, bx, by, w - 28, bh)) {
         this.popupOpen = false;
         this.popupModule = null;
         return true;
      } else if (this.isInside(mouseX, mouseY, bx, by + bh + gap, w - 28, bh)) {
         this.popupModule.toggle();
         return true;
      } else if (this.isInside(mouseX, mouseY, bx, by + 2 * (bh + gap), w - 28, bh)) {
         this.popupModule.setPosition(20, 20);
         return true;
      } else if (this.isInside(mouseX, mouseY, bx, by + 3 * (bh + gap), w - 28, bh)) {
         this.popupModule.setScale(1.0F);
         return true;
      } else {
         return false;
      }
   }

   public boolean method_25404(class_11908 input) {
      if (this.searchFocused && this.searchField.keyPressed(input)) {
         return true;
      } else if (input.method_74231()) {
         if (this.searchFocused) {
            this.searchFocused = false;
            this.searchField.setFocused(false);
            return true;
         } else {
            MC.method_1507(this.parent);
            return true;
         }
      } else {
         return super.method_25404(input);
      }
   }

   public boolean method_25400(class_11905 input) {
      return this.searchFocused && this.searchField.charTyped(input) ? true : super.method_25400(input);
   }

   private boolean isInside(double mx, double my, int x, int y, int w, int h) {
      return mx >= (double)x && mx <= (double)(x + w) && my >= (double)y && my <= (double)(y + h);
   }

   private static float lerp(float current, float target, float speed) {
      return current + (target - current) * speed;
   }

   private static float clamp(float value, float min, float max) {
      return Math.max(min, Math.min(max, value));
   }

   private static void drawBorder(class_332 context, int x, int y, int w, int h, int color) {
      context.method_25294(x, y, x + w, y + 1, color);
      context.method_25294(x, y + h - 1, x + w, y + h, color);
      context.method_25294(x, y, x + 1, y + h, color);
      context.method_25294(x + w - 1, y, x + w, y + h, color);
   }

   private boolean matchesAll(Module module) {
      return true;
   }

   private boolean matchesHud(Module module) {
      return this.categoryMatches(module, "hud");
   }

   private boolean matchesRenderer(Module module) {
      return this.categoryMatches(module, "render");
   }

   private boolean matchesMechanics(Module module) {
      return this.categoryMatches(module, "mechanic") || this.categoryMatches(module, "movement");
   }

   private boolean matchesMisc(Module module) {
      return this.categoryMatches(module, "misc") || this.categoryMatches(module, "player") || this.categoryMatches(module, "utility");
   }

   private boolean categoryMatches(Module module, String needle) {
      Category c = module.getCategory();
      if (c == null) {
         return false;
      } else {
         String display = c.getDisplayName() == null ? "" : c.getDisplayName().toLowerCase(Locale.ROOT);
         String raw = c.name() == null ? "" : c.name().toLowerCase(Locale.ROOT);
         return display.contains(needle) || raw.contains(needle);
      }
   }

   private String getDescription(Module module) {
      Category c = module.getCategory();
      if (c == null) {
         return "Module";
      } else {
         String var10000;
         switch (c) {
            case HUD -> var10000 = "Displays HUD information";
            case RENDER -> var10000 = "Visual client module";
            case MECHANICS -> var10000 = "Movement enhancement";
            case MISC -> var10000 = "General utility module";
            case ALL -> var10000 = "All modules";
            default -> throw new MatchException((String)null, (Throwable)null);
         }

         return var10000;
      }
   }

   public void resetScroll() {
      this.targetScroll = 0.0F;
      this.smoothScroll = 0.0F;
      this.scrollPositions.put(this.viewKey(), 0.0F);
   }

   private final class TabSpec {
      private final String key;
      private final String label;
      private final int x;
      private final int y;
      private final int w;
      private final int h;
      private final Predicate<Module> matcher;
      private float hoverAnim = 0.0F;

      private TabSpec(String key, String label, int x, int y, int w, int h, Predicate<Module> matcher) {
         this.key = key;
         this.label = label;
         this.x = x;
         this.y = y;
         this.w = w;
         this.h = h;
         this.matcher = matcher;
      }

      void tick() {
         this.hoverAnim = CsClientModulesScreen.lerp(this.hoverAnim, 0.0F, 0.12F);
      }

      boolean mouseClicked(double mx, double my, int button) {
         return button == 0 && CsClientModulesScreen.this.isInside(mx, my, this.x, this.y, this.w, this.h);
      }

      void render(class_332 context, int mouseX, int mouseY) {
         boolean active = this.key.equals(CsClientModulesScreen.this.activeTab);
         boolean hovered = CsClientModulesScreen.this.isInside((double)mouseX, (double)mouseY, this.x, this.y, this.w, this.h);
         this.hoverAnim = CsClientModulesScreen.lerp(this.hoverAnim, hovered ? 1.0F : 0.0F, 0.15F);
         int fill = active ? -34262 : (hovered ? -15066584 : -921365214);
         int border = active ? -34262 : (hovered ? 654311423 : 570425344);
         context.method_25294(this.x, this.y, this.x + this.w, this.y + this.h, fill);
         CsClientModulesScreen.drawBorder(context, this.x, this.y, this.w, this.h, border);
         int color = active ? -1 : -6249809;
         context.method_27534(CsClientModulesScreen.this.field_22793, class_2561.method_43470(this.label), this.x + this.w / 2, this.y + 14, color);
      }
   }

   private final class ModuleCard {
      private final Module module;
      private final int x;
      private final int y;
      private final int w;
      private final int h;
      private float hoverAnim = 0.0F;

      private ModuleCard(Module module, int x, int y, int w, int h) {
         this.module = module;
         this.x = x;
         this.y = y;
         this.w = w;
         this.h = h;
      }

      void tick() {
         this.hoverAnim = CsClientModulesScreen.lerp(this.hoverAnim, 0.0F, 0.12F);
      }

      boolean mouseClicked(double mx, double my, int button) {
         if (!CsClientModulesScreen.this.isInside(mx, my, this.x, this.y, this.w, this.h)) {
            return false;
         } else if (button == 0 && CsClientModulesScreen.this.isInside(mx, my, this.x + 12, this.y + 12, 26, 26)) {
            CsClientModulesScreen.this.popupModule = this.module;
            CsClientModulesScreen.this.popupOpen = true;
            return true;
         } else if (button == 0) {
            this.module.toggle();
            return true;
         } else if (button == 1) {
            CsClientModulesScreen.this.popupModule = this.module;
            CsClientModulesScreen.this.popupOpen = true;
            return true;
         } else {
            return false;
         }
      }

      void render(class_332 context, int mouseX, int mouseY) {
         boolean hovered = CsClientModulesScreen.this.isInside((double)mouseX, (double)mouseY, this.x, this.y, this.w, this.h);
         this.hoverAnim = CsClientModulesScreen.lerp(this.hoverAnim, hovered ? 1.0F : 0.0F, 0.16F);
         boolean enabled = this.module.isEnabled();
         int fill = hovered ? -1340598232 : -1340861408;
         context.method_25294(this.x, this.y, this.x + this.w, this.y + this.h, fill);
         CsClientModulesScreen.drawBorder(context, this.x, this.y, this.w, this.h, 654311423);
         if (enabled) {
            context.method_25294(this.x, this.y, this.x + this.w, this.y + 2, -34262);
         }

         if (hovered && !enabled) {
            context.method_25294(this.x, this.y, this.x + this.w, this.y + this.h, 135533588);
         }

         int gearX = this.x + 12;
         int gearY = this.y + 12;
         boolean gearHover = CsClientModulesScreen.this.isInside((double)mouseX, (double)mouseY, gearX, gearY, 26, 26);
         context.method_25294(gearX, gearY, gearX + 26, gearY + 26, gearHover ? -14671822 : -15066584);
         context.method_27534(CsClientModulesScreen.this.field_22793, class_2561.method_43470("⚙"), gearX + 13, gearY + 9, gearHover ? -34262 : -6249809);
         int centerX = this.x + this.w / 2;
         context.method_27534(CsClientModulesScreen.this.field_22793, class_2561.method_43470(this.module.getName()), centerX, this.y + 84, -986896);
         context.method_27534(CsClientModulesScreen.this.field_22793, class_2561.method_43470(CsClientModulesScreen.this.getDescription(this.module)), centerX, this.y + 102, -6249809);
         int barX = this.x + 12;
         int barY = this.y + this.h - 40;
         int barW = this.w - 24;
         int barH = 28;
         context.method_25294(barX, barY, barX + barW, barY + barH, enabled ? -1708398037 : -1609165278);
         CsClientModulesScreen.drawBorder(context, barX, barY, barW, barH, 570425344);
         context.method_27534(CsClientModulesScreen.this.field_22793, class_2561.method_43470(enabled ? "Enabled" : "Disabled"), barX + barW / 2, barY + 9, enabled ? -1 : -6249809);
      }
   }

   private final class SearchField {
      private int x;
      private int y;
      private int w;
      private int h;
      private String text = "";
      private boolean focused = false;
      private float cursorBlink = 0.0F;

      void setBounds(int x, int y, int w, int h) {
         this.x = x;
         this.y = y;
         this.w = w;
         this.h = h;
      }

      void tick() {
         this.cursorBlink += 0.08F;
      }

      boolean mouseClicked(double mx, double my, int button) {
         if (button != 0) {
            return false;
         } else {
            this.focused = CsClientModulesScreen.this.isInside(mx, my, this.x, this.y, this.w, this.h);
            return this.focused;
         }
      }

      boolean keyPressed(class_11908 input) {
         if (!this.focused) {
            return false;
         } else {
            int key = input.comp_4795();
            if (key == 259 && !this.text.isEmpty()) {
               this.text = this.text.substring(0, this.text.length() - 1);
               return true;
            } else if (key != 257 && key != 335 && !input.method_74231()) {
               return false;
            } else {
               this.focused = false;
               CsClientModulesScreen.this.searchFocused = false;
               return true;
            }
         }
      }

      boolean charTyped(class_11905 input) {
         if (!this.focused) {
            return false;
         } else {
            String s = input.method_74226();
            if (s != null && !s.isEmpty()) {
               char c = s.charAt(0);
               if (c >= ' ' && c != 127 && this.text.length() < 32) {
                  this.text = this.text + c;
                  return true;
               } else {
                  return false;
               }
            } else {
               return false;
            }
         }
      }

      void render(class_332 context, int mouseX, int mouseY) {
         boolean hovered = CsClientModulesScreen.this.isInside((double)mouseX, (double)mouseY, this.x, this.y, this.w, this.h);
         context.method_25294(this.x, this.y, this.x + this.w, this.y + this.h, !hovered && !this.focused ? -921365214 : -15263964);
         CsClientModulesScreen.drawBorder(context, this.x, this.y, this.w, this.h, this.focused ? -34262 : 654311423);
         context.method_51439(CsClientModulesScreen.this.field_22793, class_2561.method_43470("⌕"), this.x + 12, this.y + 14, -8486768, false);
         String shown = this.text.isEmpty() ? "Search" : this.text;
         int color = this.text.isEmpty() ? -8486768 : -986896;
         context.method_51439(CsClientModulesScreen.this.field_22793, class_2561.method_43470(shown), this.x + 30, this.y + 14, color, false);
         if (this.focused && (int)(this.cursorBlink * 10.0F) % 2 == 0) {
            int cursorX = this.x + 30 + CsClientModulesScreen.this.field_22793.method_1727(shown);
            context.method_25294(cursorX + 1, this.y + 12, cursorX + 2, this.y + 26, -986896);
         }

      }

      String getText() {
         return this.text;
      }

      boolean isFocused() {
         return this.focused;
      }

      void setFocused(boolean focused) {
         this.focused = focused;
      }
   }
}
