package com.example.client.ui;

import com.example.client.config.HudConfig;
import com.example.client.cosmetics.SkinManager;
import com.example.client.module.Category;
import com.example.client.module.Module;
import com.example.client.module.ModuleManager;
import com.example.client.module.impl.MotionBlurModule;
import com.example.client.setting.BooleanSetting;
import com.example.client.setting.ModeSetting;
import com.example.client.setting.NumberSetting;
import com.example.client.setting.Setting;
import com.example.client.ui.config.ModuleConfigScreen;
import com.example.client.ui.render.RoundedRectRenderer;
import com.example.client.update.UpdateManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import javax.imageio.ImageIO;
import net.minecraft.class_1011;
import net.minecraft.class_1043;
import net.minecraft.class_11905;
import net.minecraft.class_11908;
import net.minecraft.class_11909;
import net.minecraft.class_124;
import net.minecraft.class_2561;
import net.minecraft.class_2960;
import net.minecraft.class_310;
import net.minecraft.class_327;
import net.minecraft.class_332;
import net.minecraft.class_437;
import net.minecraft.class_490;

public class ModernClickGUI extends class_437 {
   private static final int PANEL_BLACK = -16052974;
   private static final int PANEL_BLACK_2 = -15658214;
   private float hubHoverAnim = 0.0F;
   private int lastMouseX = 0;
   private int lastMouseY = 0;
   private final List<Ripple> ripples = new ArrayList();
   private final Map<String, Float> hubAnim = new HashMap();
   private final Map<String, Float> hubHoverMap = new HashMap();
   private final Map<String, Long> hubHoverLastNs = new HashMap();
   private float guiOpenAnim = 0.0F;
   private long guiOpenLastNs = 0L;
   private float sidebarIndicatorY = 0.0F;
   private float sidebarIndicatorTargetY = 0.0F;
   private long sidebarAnimLastNs = 0L;
   private int basePanelY = 0;
   private int baseContentY = 0;
   private final Map<String, Float> moduleHoverMap = new HashMap();
   private final Map<String, Long> moduleHoverLastNs = new HashMap();
   private final Map<String, Float> moduleEnableMap = new HashMap();
   private final Map<String, Long> moduleEnableLastNs = new HashMap();
   private static final class_310 MC = class_310.method_1551();
   private static final int SAFE_PADDING = 8;
   private final List<CategoryTab> categoryTabs = new ArrayList();
   private final List<SidebarButton> sidebarButtons = new ArrayList();
   private final List<SettingChip> settingChips = new ArrayList();
   private final List<ModuleCard> cachedCards = new ArrayList();
   private final SearchBar searchBar = new SearchBar();
   private final Map<String, Float> scrollByView = new HashMap();
   private SidebarSection activeSection;
   private Category activeCategory;
   private boolean showAllModules;
   private ThemePreset activeTheme;
   private boolean csClientScreen;
   private float guiOpacity;
   private float panelOpacity;
   private float blurStrength;
   private float moduleOpacity;
   private boolean dragMode;
   private boolean searchFocused;
   private boolean showDescriptions;
   private boolean compactMode;
   private boolean roundedMode;
   private float glowStrength;
   private float targetScroll;
   private float smoothScroll;
   private String lastViewKey;
   private float settingsScroll;
   private float settingsTargetScroll;
   private int settingsDragState;
   private ClientSettingsSnapshot settingsSnapshot;
   private static final Gson SETTINGS_GSON = (new GsonBuilder()).setPrettyPrinting().create();
   private static final File SETTINGS_FILE = new File("config/optix_client_settings.json");
   private SkinTab skinTab;
   private boolean skinManagerOpen;
   private boolean skinSearchFocused;
   private String skinSearchText;
   private String skinStatusText;
   private String skinMessage;
   private SkinManager.SkinEntry selectedSkinEntry;
   public static boolean installed = false;
   private int panelX;
   private int panelY;
   private int panelW;
   private int panelH;
   private int sidebarW;
   private int topAreaH;
   private int contentX;
   private int contentY;
   private int contentW;
   private int contentH;
   private Module selectedModule;
   private boolean configPanelOpen;
   private Module hudDraggingModule;
   private int hudDragOffsetX;
   private int hudDragOffsetY;
   private static final int CONFIG_DRAWER_W = 294;
   private static final int ACCOUNT_CARD_W = 235;
   private static final int ACCOUNT_CARD_H = 54;
   private final Map<String, class_2960> imageCache;
   private final Set<String> loadingImages;

   private void test() {
      this.drawRemoteImage((class_332)null, "", 0, 0, 0, 0);
   }

   private float pulse() {
      return (float)(Math.sin((double)System.currentTimeMillis() * 0.004) * (double)0.5F + (double)0.5F);
   }

   private int accountCardX() {
      return this.contentX + 32;
   }

   private int accountCardY() {
      return this.contentY + this.topAreaH + 190;
   }

   private boolean isAccountCardHovered(double mouseX, double mouseY) {
      return this.isInside(mouseX, mouseY, this.accountCardX(), this.accountCardY(), 235, 54);
   }

   private void openAccountSwitcher() {
      class_310.method_1551().method_1507(new com.example.client.account.AccountSwitcherScreen(this));
   }

   private void openModuleConfig(Module module) {
      if (module != null) {
         class_310.method_1551().method_1507(new ModuleConfigScreen(this, module));
      }
   }

   public ModernClickGUI() {
      super(class_2561.method_43470("OptiX Client"));
      this.activeSection = ModernClickGUI.SidebarSection.MODULES;
      this.activeCategory = null;
      this.showAllModules = true;
      this.activeTheme = ModernClickGUI.ThemePreset.DARK;
      this.csClientScreen = false;
      this.guiOpacity = 1.0F;
      this.panelOpacity = 0.88F;
      this.blurStrength = 0.65F;
      this.moduleOpacity = 1.0F;
      this.dragMode = false;
      this.searchFocused = false;
      this.showDescriptions = true;
      this.compactMode = true;
      this.roundedMode = true;
      this.glowStrength = 1.0F;
      this.targetScroll = 0.0F;
      this.smoothScroll = 0.0F;
      this.lastViewKey = "ALL";
      this.settingsScroll = 0.0F;
      this.settingsTargetScroll = 0.0F;
      this.settingsDragState = 0;
      this.settingsSnapshot = null;
      this.skinTab = ModernClickGUI.SkinTab.IMPORTED;
      this.skinManagerOpen = false;
      this.skinSearchFocused = false;
      this.skinSearchText = "";
      this.skinStatusText = "";
      this.skinMessage = "";
      this.selectedSkinEntry = null;
      this.sidebarW = 60;
      this.topAreaH = 92;
      this.selectedModule = null;
      this.configPanelOpen = false;
      this.hudDraggingModule = null;
      this.hudDragOffsetX = 0;
      this.hudDragOffsetY = 0;
      this.imageCache = new HashMap();
      this.loadingImages = new HashSet();
   }

   private int animatedGradient(float speed, float offset) {
      float time = (float)(System.currentTimeMillis() % 3000L) / 3000.0F;
      float hue = (time * speed + offset) % 1.0F;
      return Color.HSBtoRGB(hue, 0.6F, 1.0F);
   }

   private static float clamp01(float value) {
      return Math.max(0.0F, Math.min(1.0F, value));
   }

   private static int applyAlpha(int color, float alphaMul) {
      int a = (int)((float)(color >>> 24 & 255) * clamp01(alphaMul));
      return color & 16777215 | a << 24;
   }

   private static float easeOutCubic(float t) {
      t = clamp01(t);
      float u = 1.0F - t;
      return 1.0F - u * u * u;
   }

   private static float expLerp(float current, float target, float speed, float dt) {
      float a = 1.0F - (float)Math.exp((double)(-speed * dt));
      return current + (target - current) * a;
   }

   private void updateGuiOpenAnim() {
      long now = System.nanoTime();
      if (this.guiOpenLastNs == 0L) {
         this.guiOpenLastNs = now;
      }

      float dt = Math.min((float)(now - this.guiOpenLastNs) / 1.0E9F, 0.05F);
      this.guiOpenLastNs = now;
      this.guiOpenAnim = expLerp(this.guiOpenAnim, 1.0F, 7.5F, dt);
   }

   private void updateSidebarIndicatorAnim() {
      long now = System.nanoTime();
      if (this.sidebarAnimLastNs == 0L) {
         this.sidebarAnimLastNs = now;
      }

      float dt = Math.min((float)(now - this.sidebarAnimLastNs) / 1.0E9F, 0.05F);
      this.sidebarAnimLastNs = now;
      this.sidebarIndicatorY = expLerp(this.sidebarIndicatorY, this.sidebarIndicatorTargetY, 14.0F, dt);
   }

   private float effectiveOpacity() {
      return clamp01(this.guiOpacity);
   }

   private float effectivePanelOpacity() {
      return clamp01(this.guiOpacity * this.panelOpacity);
   }

   private void captureSettingsSnapshot() {
      this.settingsSnapshot = new ClientSettingsSnapshot(this.activeTheme, this.guiOpacity, this.panelOpacity, this.blurStrength, this.moduleOpacity, this.showDescriptions, this.compactMode, this.roundedMode, this.glowStrength);
   }

   private void applySettingsSnapshot(ClientSettingsSnapshot snapshot) {
      if (snapshot != null) {
         this.activeTheme = snapshot.theme;
         this.csClientScreen = this.activeTheme == ModernClickGUI.ThemePreset.FOREST;
         this.guiOpacity = snapshot.guiOpacity;
         this.panelOpacity = snapshot.panelOpacity;
         this.blurStrength = snapshot.blurStrength;
         this.moduleOpacity = snapshot.moduleOpacity;
         this.showDescriptions = snapshot.showDescriptions;
         this.compactMode = snapshot.compactMode;
         this.roundedMode = snapshot.roundedMode;
         this.glowStrength = snapshot.glowStrength;
         this.saveClientSettings();
      }
   }

   private void loadClientSettings() {
      try {
         if (!SETTINGS_FILE.exists()) {
            this.saveClientSettings();
            return;
         }

         FileReader reader = new FileReader(SETTINGS_FILE);
         ClientSettingsData data = (ClientSettingsData)SETTINGS_GSON.fromJson(reader, ClientSettingsData.class);
         reader.close();
         if (data != null) {
            this.guiOpacity = clamp01(data.guiOpacity);
            this.panelOpacity = clamp01(data.panelOpacity);
            this.blurStrength = clamp01(data.blurStrength);
            this.moduleOpacity = clamp01(data.moduleOpacity);
            this.activeTheme = ModernClickGUI.ThemePreset.fromName(data.theme);
            this.csClientScreen = this.activeTheme == ModernClickGUI.ThemePreset.FOREST;
            this.showDescriptions = data.showDescriptions;
            this.compactMode = data.compactMode;
            this.roundedMode = data.roundedMode;
            this.glowStrength = Math.max(0.2F, Math.min(2.0F, data.glowStrength));
         }
      } catch (Exception e) {
         e.printStackTrace();
      }

   }

   private void saveClientSettings() {
      try {
         SETTINGS_FILE.getParentFile().mkdirs();
         ClientSettingsData data = new ClientSettingsData();
         data.guiOpacity = this.guiOpacity;
         data.panelOpacity = this.panelOpacity;
         data.blurStrength = this.blurStrength;
         data.moduleOpacity = this.moduleOpacity;
         data.theme = this.activeTheme.name();
         data.showDescriptions = this.showDescriptions;
         data.compactMode = this.compactMode;
         data.roundedMode = this.roundedMode;
         data.glowStrength = this.glowStrength;
         FileWriter writer = new FileWriter(SETTINGS_FILE);
         SETTINGS_GSON.toJson(data, writer);
         writer.close();
      } catch (Exception e) {
         e.printStackTrace();
      }

   }

   private void setTheme(ThemePreset themePreset) {
      this.activeTheme = themePreset;
      this.csClientScreen = themePreset == ModernClickGUI.ThemePreset.FOREST;
      this.saveClientSettings();
   }

   private void resetClientSettings() {
      this.guiOpacity = 1.0F;
      this.panelOpacity = 0.88F;
      this.blurStrength = 0.65F;
      this.moduleOpacity = 1.0F;
      this.activeTheme = ModernClickGUI.ThemePreset.DARK;
      this.csClientScreen = false;
      this.showDescriptions = true;
      this.compactMode = true;
      this.roundedMode = true;
      this.glowStrength = 1.0F;
      this.saveClientSettings();
   }

   private void renderGlassBackdrop(class_332 context, Theme theme) {
      int baseAlpha = (int)(170.0F * this.effectiveOpacity());
      context.method_25294(0, 0, this.field_22789, this.field_22790, baseAlpha << 24 | 329483);
      int veilAlpha = (int)(90.0F * this.effectiveOpacity() * (0.35F + this.blurStrength * 0.3F));
      context.method_25294(0, 0, this.field_22789, this.field_22790, veilAlpha << 24 | 725024);

      for(int i = 0; i < 10; ++i) {
         int y = i * Math.max(1, this.field_22790 / 10);
         int alpha = (int)(6.0F + this.blurStrength * 14.0F);
         context.method_25294(0, y, this.field_22789, y + 1, alpha << 24 | 16777215);
      }

   }

   private boolean handleSettingsControls(double mouseX, double mouseY, int button) {
      int x = this.contentX + 14;
      int y = this.contentY + this.topAreaH + 14;
      int w = this.contentW - 28;
      int innerX = x + 16;
      int currentY = y + 58 - (int)this.settingsScroll;
      int tileW = 132;
      int tileH = 32;
      int gap = 8;
      int themeRowY = currentY + 18;
      ThemePreset[] themes = new ThemePreset[]{ModernClickGUI.ThemePreset.DEFAULT, ModernClickGUI.ThemePreset.DARK, ModernClickGUI.ThemePreset.OCEAN, ModernClickGUI.ThemePreset.RED, ModernClickGUI.ThemePreset.VIOLET, ModernClickGUI.ThemePreset.FOREST};

      for(int i = 0; i < themes.length; ++i) {
         int col = i % 3;
         int row = i / 3;
         int bx = innerX + col * (tileW + gap);
         int by = themeRowY + row * (tileH + 10);
         if (button == 0 && this.isInside(mouseX, mouseY, bx, by, tileW, tileH)) {
            this.activeTheme = themes[i];
            this.csClientScreen = themes[i] == ModernClickGUI.ThemePreset.FOREST;
            this.saveClientSettings();
            if (themes[i] == ModernClickGUI.ThemePreset.FOREST) {
            }

            return true;
         }
      }

      int sliderW = Math.max(220, w - 250);
      int sliderY1 = themeRowY + 2 * (tileH + 10) + 18;
      int sliderY2 = sliderY1 + 44;
      int sliderY3 = sliderY2 + 44;
      int sliderY4 = sliderY3 + 44;
      if (button == 0 && this.isInside(mouseX, mouseY, innerX, sliderY1 - 6, sliderW, 24)) {
         this.settingsDragState = 1;
         this.updateSettingsSlider(mouseX, innerX, sliderW, this.settingsDragState);
         return true;
      } else if (button == 0 && this.isInside(mouseX, mouseY, innerX, sliderY2 - 6, sliderW, 24)) {
         this.settingsDragState = 2;
         this.updateSettingsSlider(mouseX, innerX, sliderW, this.settingsDragState);
         return true;
      } else if (button == 0 && this.isInside(mouseX, mouseY, innerX, sliderY3 - 6, sliderW, 24)) {
         this.settingsDragState = 3;
         this.updateSettingsSlider(mouseX, innerX, sliderW, this.settingsDragState);
         return true;
      } else if (button == 0 && this.isInside(mouseX, mouseY, innerX, sliderY4 - 6, sliderW, 24)) {
         this.settingsDragState = 4;
         this.updateSettingsSlider(mouseX, innerX, sliderW, this.settingsDragState);
         return true;
      } else {
         int toggleY = sliderY4 + 54;
         int btnW = 130;
         int btnH = 26;
         if (button == 0 && this.isInside(mouseX, mouseY, innerX, toggleY, btnW, btnH)) {
            this.compactMode = !this.compactMode;
            this.saveClientSettings();
            return true;
         } else if (button == 0 && this.isInside(mouseX, mouseY, innerX + btnW + gap, toggleY, btnW, btnH)) {
            this.roundedMode = !this.roundedMode;
            this.saveClientSettings();
            return true;
         } else if (button == 0 && this.isInside(mouseX, mouseY, innerX + 2 * (btnW + gap), toggleY, btnW, btnH)) {
            this.showDescriptions = !this.showDescriptions;
            this.saveClientSettings();
            return true;
         } else if (button == 0 && this.isInside(mouseX, mouseY, innerX + 3 * (btnW + gap), toggleY, btnW, btnH)) {
            this.glowStrength = this.glowStrength > 1.0F ? 1.0F : 1.4F;
            this.saveClientSettings();
            return true;
         } else {
            int actionsY = toggleY + 40;
            if (button == 0 && this.isInside(mouseX, mouseY, innerX, actionsY, 160, 26)) {
               this.resetClientSettings();
               return true;
            } else if (button == 0 && this.isInside(mouseX, mouseY, innerX + 172, actionsY, 160, 26)) {
               this.applySettingsSnapshot(this.settingsSnapshot);
               return true;
            } else {
               int miscY = actionsY + 54;
               if (button == 0 && this.isInside(mouseX, mouseY, innerX, miscY, 220, 26)) {
                  this.activeSection = ModernClickGUI.SidebarSection.DRAG;
                  this.dragMode = true;
                  this.saveClientSettings();
                  return true;
               } else {
                  return false;
               }
            }
         }
      }
   }

   private void updateSettingsSlider(double mouseX, int sliderX, int sliderW, int sliderType) {
      float value = clamp01((float)((mouseX - (double)sliderX) / (double)sliderW));
      if (sliderType == 1) {
         this.guiOpacity = value;
      } else if (sliderType == 2) {
         this.panelOpacity = value;
      } else if (sliderType == 3) {
         this.blurStrength = value;
      } else if (sliderType == 4) {
         this.moduleOpacity = value;
      }

      this.saveClientSettings();
   }

   protected void method_25426() {
      super.method_25426();
      ModuleManager.init();
      this.panelW = Math.min(860, this.field_22789 - 220);
      this.panelH = Math.min(560, this.field_22790 - 180);
      this.panelX = (this.field_22789 - this.panelW) / 2;
      this.panelY = (this.field_22790 - this.panelH) / 2;
      this.contentX = this.panelX + this.sidebarW + 18;
      this.contentY = this.panelY;
      this.contentW = this.panelW - this.sidebarW - 18;
      this.contentH = this.panelH;
      this.basePanelY = this.panelY;
      this.baseContentY = this.contentY;
      this.guiOpenAnim = 0.0F;
      this.guiOpenLastNs = 0L;
      this.sidebarIndicatorY = 0.0F;
      this.sidebarIndicatorTargetY = 0.0F;
      this.sidebarAnimLastNs = 0L;
      this.moduleHoverMap.clear();
      this.moduleHoverLastNs.clear();
      this.moduleEnableMap.clear();
      this.moduleEnableLastNs.clear();
      this.loadClientSettings();
      this.scrollByView.putIfAbsent("ALL", 0.0F);

      for(Category category : Category.values()) {
         if (category != Category.ALL) {
            this.scrollByView.putIfAbsent(category.name(), 0.0F);
         }
      }

      this.buildCategoryTabs();
      this.buildSidebarButtons();
      if (!this.sidebarButtons.isEmpty()) {
         int sidebarIndex = Math.max(0, Math.min(this.activeSection.ordinal(), this.sidebarButtons.size() - 1));
         this.sidebarIndicatorY = (float)(((SidebarButton)this.sidebarButtons.get(sidebarIndex)).y + 6);
         this.sidebarIndicatorTargetY = this.sidebarIndicatorY;
      }

      this.buildSettingChips();
      SkinManager.init();
      this.refreshSkinSelection();
      this.activeCategory = null;
      this.showAllModules = true;
      this.lastViewKey = this.viewKey();
      this.targetScroll = (Float)this.scrollByView.getOrDefault(this.lastViewKey, 0.0F);
      this.smoothScroll = this.targetScroll;
      this.searchBar.setBounds(this.contentX + this.contentW - 290, this.contentY + 10, 250, 26);
      this.captureSettingsSnapshot();
   }

   public boolean method_25421() {
      return false;
   }

   public void method_25393() {
      for(CategoryTab tab : this.categoryTabs) {
         tab.tick();
      }

      for(SidebarButton button : this.sidebarButtons) {
         button.tick();
      }

      for(SettingChip chip : this.settingChips) {
         chip.tick();
      }

      this.searchBar.tick();
   }

   public boolean method_25404(class_11908 input) {
      int keyCode = input.comp_4795();
      int scanCode = input.comp_4796();
      int modifiers = input.comp_4797();
      if (this.activeSection == ModernClickGUI.SidebarSection.MODULES && this.searchFocused && this.searchBar.keyPressed(input)) {
         return true;
      } else {
         if (this.activeSection == ModernClickGUI.SidebarSection.COSMETICS && this.skinManagerOpen && this.skinTab == ModernClickGUI.SkinTab.DOWNLOAD && this.skinSearchFocused) {
            if (keyCode == 259 && !this.skinSearchText.isEmpty()) {
               this.skinSearchText = this.skinSearchText.substring(0, this.skinSearchText.length() - 1);
               return true;
            }

            if (keyCode == 257 || keyCode == 335) {
               this.triggerSkinDownload();
               return true;
            }

            if (input.method_74231()) {
               this.skinSearchFocused = false;
               return true;
            }
         }

         if (input.method_74231()) {
            if (this.searchFocused) {
               this.searchFocused = false;
               this.searchBar.setFocused(false);
               return true;
            } else if (this.activeSection == ModernClickGUI.SidebarSection.COSMETICS && this.skinManagerOpen) {
               this.closeSkinManager();
               return true;
            } else {
               this.method_25419();
               return true;
            }
         } else {
            return super.method_25404(input);
         }
      }
   }

   public boolean method_25400(class_11905 input) {
      if (this.activeSection == ModernClickGUI.SidebarSection.MODULES && this.searchFocused && this.searchBar.charTyped(input)) {
         return true;
      } else {
         if (this.activeSection == ModernClickGUI.SidebarSection.COSMETICS && this.skinManagerOpen && this.skinTab == ModernClickGUI.SkinTab.DOWNLOAD && this.skinSearchFocused) {
            String s = input.method_74226();
            if (s != null && !s.isEmpty()) {
               char c = s.charAt(0);
               if (c >= ' ' && c != 127 && this.skinSearchText.length() < 32) {
                  this.skinSearchText = this.skinSearchText + c;
                  return true;
               }
            }
         }

         return super.method_25400(input);
      }
   }

   private String viewKey() {
      return !this.showAllModules && this.activeCategory != null && this.activeCategory != Category.ALL ? this.activeCategory.name() : "ALL";
   }

   private int moduleAreaRight() {
      return this.contentX + this.contentW - (this.configPanelOpen && this.selectedModule != null ? 294 : 0);
   }

   private boolean isInsideModulesArea(double mouseX, double mouseY) {
      return mouseX >= (double)this.contentX && mouseX <= (double)this.moduleAreaRight() && mouseY >= (double)(this.contentY + this.topAreaH) && mouseY <= (double)(this.contentY + this.contentH);
   }

   private List<Module> getVisibleModules() {
      List<Module> visible = new ArrayList();

      for(Module module : ModuleManager.getModules()) {
         if (module != null) {
            boolean matchesCategory = this.showAllModules || this.activeCategory == null || this.activeCategory == Category.ALL || module.getCategory() == this.activeCategory;
            if (matchesCategory && this.matchesSearch(module)) {
               visible.add(module);
            }
         }
      }

      visible.sort(Comparator.comparing(Module::getName, String.CASE_INSENSITIVE_ORDER));
      return visible;
   }

   private float getMaxScroll(List<Module> visibleModules, int cardH, int gapY) {
      int rows = Math.max(1, (int)Math.ceil((double)visibleModules.size() / (double)4.0F));
      int viewportH = this.contentH - this.topAreaH - 30;
      int totalH = rows * (cardH + gapY);
      return (float)Math.max(0, totalH - viewportH);
   }

   public boolean method_25401(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
      if (this.activeSection == ModernClickGUI.SidebarSection.DRAG) {
         Module hovered = this.getHoveredHudModule(mouseX, mouseY);
         if (hovered != null) {
            float next = hovered.getScale() + (float)(verticalAmount * (double)0.05F);
            hovered.setScale(clampFloat(next, 0.5F, 2.0F));
            HudConfig.save();
         }

         return true;
      } else if (this.activeSection == ModernClickGUI.SidebarSection.SETTINGS) {
         this.settingsTargetScroll -= (float)verticalAmount * 36.0F;
         if (this.settingsTargetScroll < 0.0F) {
            this.settingsTargetScroll = 0.0F;
         }

         return true;
      } else if (this.activeSection != ModernClickGUI.SidebarSection.MODULES) {
         return super.method_25401(mouseX, mouseY, horizontalAmount, verticalAmount);
      } else if (!this.isInsideModulesArea(mouseX, mouseY)) {
         return super.method_25401(mouseX, mouseY, horizontalAmount, verticalAmount);
      } else {
         String key = this.viewKey();
         List<Module> visibleModules = this.getVisibleModules();
         int cardH = this.compactMode ? 112 : 118;
         int gapY = 12;
         float maxScroll = this.getMaxScroll(visibleModules, cardH, gapY);
         float current = (Float)this.scrollByView.getOrDefault(key, 0.0F);
         current = clampFloat(current - (float)verticalAmount * 30.0F, 0.0F, maxScroll);
         this.scrollByView.put(key, current);
         this.targetScroll = current;
         this.lastViewKey = key;
         return true;
      }
   }

   public boolean method_25402(class_11909 click, boolean doubled) {
      double mouseX = click.comp_4798();
      double mouseY = click.comp_4799();
      int button = click.method_74245();
      if (this.activeSection == ModernClickGUI.SidebarSection.DOWNLOADS) {
         int x = this.contentX + 14;
         int y = this.contentY + this.topAreaH + 14;
         int w = this.contentW - 28;
         int btnW = 220;
         int btnH = 30;
         int centerX = x + w / 2 - btnW / 2;
         int resY = y + 80;
         int shaderY = resY + 40;
         int modY = shaderY + 40;
         class_310 mc = class_310.method_1551();
         if (this.isInside(mouseX, mouseY, centerX, resY, btnW, btnH)) {
            mc.method_1507(new DownloadManagerScreen("resourcepacks"));
            return true;
         }

         if (this.isInside(mouseX, mouseY, centerX, shaderY, btnW, btnH)) {
            mc.method_1507(new DownloadManagerScreen("shaders"));
            return true;
         }

         if (this.isInside(mouseX, mouseY, centerX, modY, btnW, btnH)) {
            mc.method_1507(new DownloadManagerScreen("mods"));
            return true;
         }
      }

      if (this.activeSection == ModernClickGUI.SidebarSection.COSMETICS && this.handleSkinManagerClick(mouseX, mouseY, button)) {
         return true;
      } else if (this.activeSection == ModernClickGUI.SidebarSection.SETTINGS && this.handleSettingsControls(mouseX, mouseY, button)) {
         return true;
      } else {
         Ripple r = new Ripple();
         r.x = (float)mouseX;
         r.y = (float)mouseY;
         r.radius = 0.0F;
         r.alpha = 0.6F;
         this.ripples.add(r);
         if (this.activeSection == ModernClickGUI.SidebarSection.MODULES && this.searchBar.mouseClicked(mouseX, mouseY, button)) {
            this.searchFocused = this.searchBar.isFocused();
            return true;
         } else {
            for(SidebarButton side : this.sidebarButtons) {
               if (side.mouseClicked(mouseX, mouseY, button)) {
                  this.activeSection = side.section;
                  this.searchFocused = false;
                  this.searchBar.setFocused(false);
                  this.configPanelOpen = false;
                  this.selectedModule = null;
                  this.hudDraggingModule = null;
                  if (side.section == ModernClickGUI.SidebarSection.DRAG) {
                     this.dragMode = true;
                     this.skinManagerOpen = false;
                     return true;
                  }

                  this.dragMode = false;
                  if (side.section == ModernClickGUI.SidebarSection.COSMETICS) {
                     this.closeSkinManager();
                     this.skinTab = ModernClickGUI.SkinTab.IMPORTED;
                     this.refreshSkinSelection();
                  }

                  if (side.section == ModernClickGUI.SidebarSection.SETTINGS) {
                     this.buildSettingChips();
                     this.captureSettingsSnapshot();
                  }

                  return true;
               }
            }

            if (this.activeSection == ModernClickGUI.SidebarSection.DRAG) {
               return this.handleDragEditorClick(mouseX, mouseY, button) ? true : true;
            } else {
               if (this.activeSection == ModernClickGUI.SidebarSection.DOWNLOADS) {
                  int x = this.contentX + 14;
                  int y = this.contentY + this.topAreaH + 14;
                  int w = this.contentW - 28;
                  int btnW = 220;
                  int btnH = 30;
                  int centerX = x + w / 2 - btnW / 2;
                  int resY = y + 80;
                  int shaderY = resY + 40;
                  int modY = shaderY + 40;
                  if (this.isInside(mouseX, mouseY, centerX, resY, btnW, btnH)) {
                     System.out.println("Resource Packs clicked");
                     return true;
                  }

                  if (this.isInside(mouseX, mouseY, centerX, shaderY, btnW, btnH)) {
                     System.out.println("Shaders clicked");
                     return true;
                  }

                  if (this.isInside(mouseX, mouseY, centerX, modY, btnW, btnH)) {
                     System.out.println("Mods clicked");
                     return true;
                  }
               }

               if (this.activeSection == ModernClickGUI.SidebarSection.MODULES) {
                  for(CategoryTab tab : this.categoryTabs) {
                     if (tab.mouseClicked(mouseX, mouseY, button)) {
                        if (tab.allTab) {
                           this.showAllModules = true;
                           this.activeCategory = null;
                        } else {
                           this.showAllModules = false;
                           this.activeCategory = tab.category;
                        }

                        this.configPanelOpen = false;
                        this.selectedModule = null;
                        String key = this.viewKey();
                        this.targetScroll = (Float)this.scrollByView.getOrDefault(key, 0.0F);
                        this.smoothScroll = this.targetScroll;
                        this.lastViewKey = key;
                        return true;
                     }
                  }

                  if (this.configPanelOpen && this.selectedModule != null && this.handleConfigPanelClick(mouseX, mouseY, button)) {
                     return true;
                  }

                  for(ModuleCard card : this.cachedCards) {
                     if (card.visible && card.contains(mouseX, mouseY)) {
                        if (card.containsConfig(mouseX, mouseY)) {
                           this.openModuleConfig(card.module);
                           return true;
                        }

                        if (button == 0) {
                           card.module.toggle();
                           return true;
                        }

                        if (button == 1) {
                           this.openModuleConfig(card.module);
                           return true;
                        }
                     }
                  }
               } else if (this.activeSection == ModernClickGUI.SidebarSection.SETTINGS) {
                  int updaterW = 250;
                  int updaterX = this.contentX + 24;
                  int updaterY = this.contentY + this.contentH - 208;
                  int checkX = updaterX + 12;
                  int checkY = updaterY + 58;
                  int checkW = 92;
                  int checkH = 20;
                  if (mouseX >= (double)checkX && mouseX <= (double)(checkX + checkW) && mouseY >= (double)checkY && mouseY <= (double)(checkY + checkH)) {
                     UpdateManager.checkForUpdates();
                     return true;
                  }

                  int restartW = 92;
                  int restartX = updaterX + updaterW - restartW - 12;
                  int restartY = updaterY + 58;
                  if (mouseX >= (double)restartX && mouseX <= (double)(restartX + 92) && mouseY >= (double)restartY && mouseY <= (double)(restartY + 20)) {
                     UpdateManager.restartAndInstall();
                     return true;
                  }

                  int x = this.contentX + 14;
                  int y = this.contentY + this.topAreaH + 14;
                  int accBtnX = x + 16;
                  int accBtnY = y + 58 + 600 - (int)this.settingsScroll;
                  if (mouseX >= (double)accBtnX && mouseX <= (double)(accBtnX + 220) && mouseY >= (double)accBtnY && mouseY <= (double)(accBtnY + 26)) {
                     this.openAccountSwitcher();
                     return true;
                  }

                  for(SettingChip chip : this.settingChips) {
                     if (chip.mouseClicked(mouseX, mouseY, button)) {
                        chip.action.run();
                        this.buildSettingChips();
                        return true;
                     }
                  }
               }

               return super.method_25402(click, doubled);
            }
         }
      }
   }

   private boolean handleConfigPanelClick(double mouseX, double mouseY, int button) {
      if (this.selectedModule == null) {
         return false;
      } else {
         int x = this.moduleAreaRight() + 14;
         int y = this.contentY + this.topAreaH + 14;
         int w = 280;
         int bh = 26;
         int gap = 8;
         int bx = x + 16;
         int by = y + 58;
         if (button == 0 && this.isInside(mouseX, mouseY, bx, by, w - 32, bh)) {
            this.activeSection = ModernClickGUI.SidebarSection.DRAG;
            this.dragMode = true;
            this.configPanelOpen = false;
            return true;
         } else if (button == 0 && this.isInside(mouseX, mouseY, bx, by + bh + gap, w - 32, bh)) {
            int cx = class_310.method_1551().method_22683().method_4486() / 2;
            int cy = class_310.method_1551().method_22683().method_4502() / 2;
            this.selectedModule.setPosition(cx - this.selectedModule.getWidth() / 2, cy - this.selectedModule.getHeight() / 2);
            HudConfig.save();
            return true;
         } else if (button == 0 && this.isInside(mouseX, mouseY, bx, by + 2 * (bh + gap), w - 32, bh)) {
            this.selectedModule.setPosition(20, 20);
            HudConfig.save();
            return true;
         } else if (button == 0 && this.isInside(mouseX, mouseY, bx, by + 3 * (bh + gap), w - 32, bh)) {
            this.selectedModule.setScale(1.0F);
            HudConfig.save();
            return true;
         } else if (button == 0 && this.isInside(mouseX, mouseY, bx, by + 4 * (bh + gap), w - 32, bh)) {
            this.configPanelOpen = false;
            this.selectedModule = null;
            return true;
         } else {
            Module cy = this.selectedModule;
            if (cy instanceof MotionBlurModule) {
               MotionBlurModule motionBlur = (MotionBlurModule)cy;
               if (button == 0 && this.isInside(mouseX, mouseY, bx, by + 5 * (bh + gap), w - 32, bh)) {
                  class_310.method_1551().method_1507(new MotionBlurSettingsScreen(motionBlur));
                  return true;
               }
            }

            int settingsStartY = by + 5 * (bh + gap) + 24;
            int currentY = settingsStartY;

            for(Setting setting : this.selectedModule.getSettings()) {
               if (setting instanceof BooleanSetting) {
                  BooleanSetting bool = (BooleanSetting)setting;
                  if (this.isInside(mouseX, mouseY, bx, currentY, w - 32, 12) && button == 0) {
                     bool.toggle();
                     HudConfig.save();
                     return true;
                  }
               } else if (setting instanceof ModeSetting) {
                  ModeSetting mode = (ModeSetting)setting;
                  if (this.isInside(mouseX, mouseY, bx, currentY, w - 32, 12)) {
                     if (button == 0) {
                        mode.cycle();
                        HudConfig.save();
                        return true;
                     }

                     if (button == 1) {
                        mode.previous();
                        HudConfig.save();
                        return true;
                     }
                  }
               } else if (setting instanceof NumberSetting) {
                  NumberSetting num = (NumberSetting)setting;
                  if (this.isInside(mouseX, mouseY, bx, currentY, w - 32, 12)) {
                     if (button == 0) {
                        num.setValue(num.getValue() + num.getStep());
                        HudConfig.save();
                        return true;
                     }

                     if (button == 1) {
                        num.setValue(num.getValue() - num.getStep());
                        HudConfig.save();
                        return true;
                     }
                  }
               }

               currentY += 14;
            }

            return false;
         }
      }
   }

   private boolean isInside(double mx, double my, int x, int y, int w, int h) {
      return mx >= (double)x && mx <= (double)(x + w) && my >= (double)y && my <= (double)(y + h);
   }

   public boolean method_25406(class_11909 click) {
      if (click.method_74245() == 0 && this.settingsDragState != 0) {
         this.settingsDragState = 0;
         return true;
      } else if (click.method_74245() == 0 && this.hudDraggingModule != null) {
         this.hudDraggingModule = null;
         HudConfig.save();
         return true;
      } else {
         return super.method_25406(click);
      }
   }

   public boolean method_25403(class_11909 click, double deltaX, double deltaY) {
      double mouseX = click.comp_4798();
      double mouseY = click.comp_4799();
      int button = click.method_74245();
      if (this.activeSection == ModernClickGUI.SidebarSection.SETTINGS && button == 0 && this.settingsDragState != 0) {
         int x = this.contentX + 14;
         int y = this.contentY + this.topAreaH + 14;
         int w = this.contentW - 28;
         int innerX = x + 16;
         int sliderW = Math.max(220, w - 250);
         this.updateSettingsSlider(mouseX, innerX, sliderW, this.settingsDragState);
         return true;
      } else if (this.activeSection == ModernClickGUI.SidebarSection.DRAG && button == 0 && this.hudDraggingModule != null) {
         int screenW = class_310.method_1551().method_22683().method_4486();
         int screenH = class_310.method_1551().method_22683().method_4502();
         int mw = Math.max(20, this.hudDraggingModule.getWidth());
         int mh = Math.max(20, this.hudDraggingModule.getHeight());
         double scaleX = (double)screenW / (double)this.contentW;
         double scaleY = (double)screenH / (double)(this.contentH - this.topAreaH);
         int mx = (int)((mouseX - (double)this.contentX) * scaleX);
         int my = (int)((mouseY - (double)this.contentY - (double)this.topAreaH) * scaleY);
         int newX = mx - this.hudDragOffsetX;
         int newY = my - this.hudDragOffsetY;
         int padding = 8;
         newX = Math.max(padding, Math.min(newX, screenW - mw - padding));
         newY = Math.max(padding, Math.min(newY, screenH - mh - padding));
         this.hudDraggingModule.setPosition(newX, newY);
         return true;
      } else {
         return super.method_25403(click, deltaX, deltaY);
      }
   }

   public void method_25394(class_332 context, int mouseX, int mouseY, float delta) {
      this.lastMouseX = mouseX;
      this.lastMouseY = mouseY;
      this.updateGuiOpenAnim();
      this.updateSidebarIndicatorAnim();
      int oldPanelY = this.panelY;
      int oldContentY = this.contentY;
      int oldContentH = this.contentH;
      float open = easeOutCubic(this.guiOpenAnim);
      int popupLift = Math.round((1.0F - open) * 16.0F);
      this.panelY = this.basePanelY + popupLift;
      this.contentY = this.baseContentY + popupLift;
      this.contentH = oldContentH;
      this.searchBar.setBounds(this.contentX + this.contentW - 290, this.contentY + 10, 250, 26);
      Theme theme = this.theme();
      this.renderGlassBackdrop(context, theme);
      this.drawGlow(context, this.panelX, this.panelY, this.panelW, this.panelH, 18, theme.glowStrong, (int)(12.0F * this.glowStrength));
      this.drawPanel(context, this.panelX, this.panelY, this.panelW, this.panelH, 18, theme.panel);
      this.drawHorizontalDivider(context, this.contentX, this.contentY + this.topAreaH, this.contentW, theme.borderSoft);
      context.method_51439(this.field_22793, class_2561.method_43470("OptiX Client"), this.contentX + 20, this.contentY + 14, theme.textStrong, true);
      context.method_51439(this.field_22793, class_2561.method_43470(this.activeSection.label), this.contentX + 20, this.contentY + 32, theme.textDim, true);

      for(SidebarButton button : this.sidebarButtons) {
         button.render(context, mouseX, mouseY, theme, this.activeSection, this.dragMode);
      }

      if (this.activeSection == ModernClickGUI.SidebarSection.MODULES) {
         if (this.csClientScreen) {
            this.renderCSClientModules(context, mouseX, mouseY, theme);
         } else {
            this.searchBar.render(context, mouseX, mouseY, theme, this.searchFocused);

            for(CategoryTab tab : this.categoryTabs) {
               tab.render(context, mouseX, mouseY, theme);
            }

            this.renderModules(context, mouseX, mouseY, theme);
         }
      } else if (this.activeSection == ModernClickGUI.SidebarSection.SETTINGS) {
         this.renderSettings(context, mouseX, mouseY, theme);
      } else if (this.activeSection == ModernClickGUI.SidebarSection.DOWNLOADS) {
         int x = this.contentX + 14;
         int y = this.contentY + this.topAreaH + 14;
         int w = this.contentW - 28;
         int h = this.contentH - this.topAreaH - 28;
         this.drawGlow(context, x, y, w, h, 18, theme.accentSoft, (int)(6.0F + this.pulse() * 6.0F));
         this.drawPanel(context, x, y, w, h, 18, theme.panel);
         this.drawBorder(context, x, y, w, h, 18, theme.borderSoft);
         context.method_51439(this.field_22793, class_2561.method_43470("Resource Packs • Shaders • Mods"), x + 16, y + 20, theme.textStrong, false);
         int btnW = 220;
         int btnH = 30;
         int centerX = x + w / 2 - btnW / 2;
         int resY = y + 80;
         int shaderY = resY + 40;
         int modY = shaderY + 40;
         this.drawHubButton(context, centerX, resY, btnW, btnH, "Resource Packs", theme, mouseX, mouseY);
         this.drawHubButton(context, centerX, shaderY, btnW, btnH, "Shaders", theme, mouseX, mouseY);
         this.drawHubButton(context, centerX, modY, btnW, btnH, "Mods (Fabric)", theme, mouseX, mouseY);
         context.method_51439(this.field_22793, class_2561.method_43470("Auto-install • Modrinth Powered"), x + 16, y + h - 18, theme.textDim, false);
      } else if (this.activeSection == ModernClickGUI.SidebarSection.COSMETICS) {
         this.renderSkinManagerPane(context, mouseX, mouseY, theme);
      } else if (this.activeSection == ModernClickGUI.SidebarSection.DRAG) {
         this.renderDragEditor(context, mouseX, mouseY, theme);
      }

      super.method_25394(context, mouseX, mouseY, delta);
      this.panelY = oldPanelY;
      this.contentY = oldContentY;
      this.contentH = oldContentH;
   }

   private void renderCSClientModules(class_332 context, int mouseX, int mouseY, Theme theme) {
      this.searchBar.render(context, mouseX, mouseY, theme, this.searchFocused);

      for(CategoryTab tab : this.categoryTabs) {
         tab.render(context, mouseX, mouseY, theme);
      }

      this.renderModules(context, mouseX, mouseY, theme);
   }

   private void renderModules(class_332 context, int mouseX, int mouseY, Theme theme) {
      this.cachedCards.clear();
      List<Module> visibleModules = this.getVisibleModules();
      String key = this.viewKey();
      int cardH = this.compactMode ? 102 : 108;
      int gapX = 10;
      int gapY = 10;
      int moduleRight = this.moduleAreaRight() - 12;
      int moduleLeft = this.contentX + 14;
      int availableW = Math.max(260, moduleRight - moduleLeft);
      int cardW = Math.max(120, (availableW - gapX * 3) / 4);
      int startX = moduleLeft;
      int startY = this.contentY + this.topAreaH + 14;
      int clipLeft = this.contentX + 12;
      int clipTop = this.contentY + this.topAreaH + 10;
      int clipRight = this.moduleAreaRight() - 8;
      int clipBottom = this.contentY + this.contentH - 12;
      float maxScroll = this.getMaxScroll(visibleModules, cardH, gapY);
      if (!key.equals(this.lastViewKey)) {
         this.smoothScroll = (Float)this.scrollByView.getOrDefault(key, 0.0F);
         this.targetScroll = this.smoothScroll;
         this.lastViewKey = key;
      }

      this.targetScroll = clampFloat(this.targetScroll, 0.0F, maxScroll);
      this.smoothScroll = lerp(this.smoothScroll, this.targetScroll, 0.2F);
      context.method_44379(clipLeft, clipTop, clipRight, clipBottom);
      int index = 0;

      for(Module module : visibleModules) {
         int col = index % 4;
         int row = index / 4;
         int x = startX + col * (cardW + gapX);
         int y = (int)((float)(startY + row * (cardH + gapY)) - this.smoothScroll);
         if (x + cardW >= clipLeft && x <= clipRight && y + cardH >= clipTop && y <= clipBottom) {
            ModuleCard card = new ModuleCard(module, x, y, cardW, cardH);
            card.visible = true;
            this.cachedCards.add(card);
            card.render(context, mouseX, mouseY, theme, this.dragMode);
            ++index;
         } else {
            ++index;
         }
      }

      context.method_44380();
      if (maxScroll > 0.5F) {
         int barX = this.moduleAreaRight() - 6;
         int barH = clipBottom - clipTop;
         int handleH = Math.max(20, (int)((float)barH * ((float)barH / ((float)barH + maxScroll))));
         int handleY = (int)((float)clipTop + this.smoothScroll / maxScroll * (float)(barH - handleH));
         context.method_25294(barX, clipTop, barX + 2, clipBottom, 570425344);
         context.method_25294(barX, handleY, barX + 2, handleY + handleH, theme.accentStrong);
      }

      if (this.configPanelOpen && this.selectedModule != null) {
         this.renderConfigPanel(context, mouseX, mouseY, theme);
      }

   }

   private void renderDragEditor(class_332 context, int mouseX, int mouseY, Theme theme) {
      context.method_25294(0, 0, this.field_22789, this.field_22790, -872085749);
      this.drawNoise(context);
      int padding = 8;
      int safeX = padding;
      int safeY = padding;
      int safeW = this.field_22789 - padding * 2;
      int safeH = this.field_22790 - padding * 2;
      context.method_25294(padding, padding, padding + safeW, padding + 1, 587202559);
      context.method_25294(padding, padding + safeH, padding + safeW, padding + safeH + 1, 587202559);
      context.method_25294(padding, padding, padding + 1, padding + safeH, 587202559);
      context.method_25294(padding + safeW, padding, padding + safeW + 1, padding + safeH, 587202559);
      this.drawGlow(context, this.panelX, this.panelY, this.panelW, this.panelH, 18, theme.glowStrong, (int)(12.0F * this.glowStrength));
      this.drawVerticalDivider(context, this.contentX, this.contentY + 8, this.contentH - 16, theme.borderSoft);
      this.drawHorizontalDivider(context, this.contentX, this.contentY + this.topAreaH, this.contentW, theme.borderSoft);
      context.method_51439(this.field_22793, class_2561.method_43470("HUD Editor"), this.contentX + 20, this.contentY + 14, theme.textStrong, false);
      context.method_51439(this.field_22793, class_2561.method_43470("Left click to drag. Scroll to change scale."), this.contentX + 20, this.contentY + 32, theme.textDim, false);

      for(Module module : ModuleManager.getModules()) {
         if (module != null && module.getCategory() == Category.HUD) {
            int mx = module.getX();
            int my = module.getY();
            int mw = Math.max(40, module.getWidth());
            int mh = Math.max(20, module.getHeight());
            boolean hovered = module.isHovering((double)mouseX, (double)mouseY);
            boolean selected = module == this.selectedModule || module == this.hudDraggingModule;
            mx = Math.max(safeX, Math.min(mx, safeX + safeW - mw));
            my = Math.max(safeY, Math.min(my, safeY + safeH - mh));
            if (module.isEnabled()) {
               module.renderAt(context, mx, my);
            } else {
               context.method_25294(mx, my, mx + mw, my + mh, 1427182112);
               context.method_51439(this.field_22793, class_2561.method_43470(module.getName() + " (OFF)"), mx + 6, my + 6, theme.textDim, false);
            }

            int outline = selected ? theme.accentStrong : (hovered ? theme.glowStrong : theme.borderSoft);
            context.method_25294(mx - 2, my - 2, mx + mw + 2, my - 1, outline);
            context.method_25294(mx - 2, my + mh + 1, mx + mw + 2, my + mh + 2, outline);
            context.method_25294(mx - 2, my - 2, mx - 1, my + mh + 2, outline);
            context.method_25294(mx + mw + 1, my - 2, mx + mw + 2, my + mh + 2, outline);
            context.method_51439(this.field_22793, class_2561.method_43470(module.getName()), mx, my - 10, theme.textStrong, false);
         }
      }

      int bx = this.field_22789 / 2 - 90;
      int by = this.field_22790 / 2 - 18;
      this.drawMiniButton(context, bx, by, 180, 36, "Back to Mods", theme);
   }

   private boolean handleDragEditorClick(double mouseX, double mouseY, int button) {
      int bx = this.field_22789 / 2 - 90;
      int by = this.field_22790 / 2 - 18;
      if (button == 0 && this.isInside(mouseX, mouseY, bx, by, 180, 36)) {
         this.activeSection = ModernClickGUI.SidebarSection.MODULES;
         this.dragMode = false;
         this.configPanelOpen = false;
         this.selectedModule = null;
         this.hudDraggingModule = null;
         HudConfig.save();
         return true;
      } else {
         if (button == 0) {
            Module hovered = this.getHoveredHudModule(mouseX, mouseY);
            if (hovered != null) {
               this.selectedModule = hovered;
               this.hudDraggingModule = hovered;
               int screenW = class_310.method_1551().method_22683().method_4486();
               int screenH = class_310.method_1551().method_22683().method_4502();
               double scaleX = (double)screenW / (double)this.contentW;
               double scaleY = (double)screenH / (double)(this.contentH - this.topAreaH);
               int mx = (int)((mouseX - (double)this.contentX) * scaleX);
               int my = (int)((mouseY - (double)this.contentY - (double)this.topAreaH) * scaleY);
               this.hudDragOffsetX = mx - hovered.getX();
               this.hudDragOffsetY = my - hovered.getY();
               return true;
            }
         }

         return false;
      }
   }

   private Module getHoveredHudModule(double mouseX, double mouseY) {
      for(Module module : ModuleManager.getModules()) {
         if (module != null && module.getCategory() == Category.HUD && module.isHovering(mouseX, mouseY)) {
            return module;
         }
      }

      return null;
   }

   private void renderConfigPanel(class_332 context, int mouseX, int mouseY, Theme theme) {
      if (this.selectedModule != null) {
         int x = this.moduleAreaRight() + 14;
         int y = this.contentY + this.topAreaH + 14;
         int w = 280;
         int h = this.contentH - this.topAreaH - 28;
         this.drawGlow(context, x, y, w, h, 18, theme.accentSoft, 10);
         this.drawPanel(context, x, y, w, h, 18, theme.panel2);
         int grad = this.animatedGradient(1.2F, 0.0F);
         this.drawBorder(context, x, y, w, h, 18, grad);
         context.method_51439(this.field_22793, class_2561.method_43470("Module Config"), x + 16, y + 14, theme.textStrong, false);
         context.method_51439(this.field_22793, class_2561.method_43470(this.selectedModule.getName()), x + 16, y + 32, theme.textDim, false);
         int bx = x + 16;
         int by = y + 58;
         int bw = w - 32;
         int bh = 26;
         int gap = 8;
         this.drawMiniButton(context, bx, by, bw, bh, "Edit HUD", theme);
         this.drawMiniButton(context, bx, by + bh + gap, bw, bh, "Center", theme);
         this.drawMiniButton(context, bx, by + 2 * (bh + gap), bw, bh, "Reset Position", theme);
         this.drawMiniButton(context, bx, by + 3 * (bh + gap), bw, bh, "Reset Scale", theme);
         this.drawMiniButton(context, bx, by + 4 * (bh + gap), bw, bh, "Close", theme);
         int ty = by + 5 * (bh + gap) + 10;
         if (this.selectedModule instanceof MotionBlurModule) {
            this.drawMiniButton(context, bx, by + 5 * (bh + gap), bw, bh, "Motion Blur Settings", theme);
         }

         int clipY1 = y + 48;
         int clipY2 = y + h - 12;
         context.method_44379(x + 12, clipY1, x + w - 12, clipY2);
         context.method_51439(this.field_22793, class_2561.method_43470("Settings:"), bx, ty, theme.textStrong, false);
         ty += 14;

         for(Setting setting : this.selectedModule.getSettings()) {
            String text = setting.getName();
            if (setting instanceof BooleanSetting) {
               BooleanSetting bool = (BooleanSetting)setting;
               text = text + ": " + (bool.getValue() ? "ON" : "OFF");
            } else if (setting instanceof ModeSetting) {
               ModeSetting mode = (ModeSetting)setting;
               text = text + ": " + mode.getMode();
            } else if (setting instanceof NumberSetting) {
               NumberSetting num = (NumberSetting)setting;
               text = text + ": " + String.format(Locale.ROOT, "%.1f", num.getValue());
            }

            context.method_25294(bx, ty - 2, bx + bw, ty + 10, 570425344);
            context.method_51439(this.field_22793, class_2561.method_43470(text), bx + 4, ty, theme.textStrong, false);
            ty += 14;
            if (ty > clipY2 - 14) {
               break;
            }
         }

         context.method_44380();
      }
   }

   private void drawMiniButton(class_332 context, int x, int y, int w, int h, String label, Theme theme) {
      boolean hovered = this.isInside((double)this.lastMouseX, (double)this.lastMouseY, x, y, w, h);
      int fill = hovered ? this.lerpColor(theme.panel, theme.accentSoft, 0.14F) : theme.panel;
      int border = hovered ? theme.accentStrong : theme.borderSoft;
      if (hovered) {
         this.drawGlow(context, x - 1, y - 1, w + 2, h + 2, h / 2, theme.accentSoft, 1);
      }

      RoundedRectRenderer.outline(context, (float)x, (float)y, (float)w, (float)h, (float)h / 2.0F, 1.0F, border, fill);
      context.method_51439(this.field_22793, class_2561.method_43470(label), x + 10, y + 8, theme.textStrong, false);
   }

   private void drawToggleButton(class_332 context, int x, int y, int w, int h, String label, Theme theme, boolean active) {
      boolean hovered = this.isInside((double)this.lastMouseX, (double)this.lastMouseY, x, y, w, h);
      int fill = active ? this.lerpColor(theme.panel2, theme.accentSoft, 0.22F) : theme.panel2;
      if (hovered) {
         fill = this.lerpColor(fill, theme.accentSoft, 0.12F);
      }

      int border = active ? theme.accentStrong : (hovered ? theme.glowStrong : theme.borderSoft);
      if (hovered || active) {
         this.drawGlow(context, x - 1, y - 1, w + 2, h + 2, Math.max(6, h / 2), theme.accentSoft, active ? 2 : 1);
      }

      RoundedRectRenderer.outline(context, (float)x, (float)y, (float)w, (float)h, (float)h / 2.0F, 1.0F, border, fill);
      int textX = x + Math.max(8, (w - this.field_22793.method_1727(label)) / 2);
      context.method_51439(this.field_22793, class_2561.method_43470(label), textX, y + 8, active ? -1 : theme.textStrong, false);
   }

   private void drawSettingsSlider(class_332 context, int x, int y, int width, String label, int percent, Theme theme) {
      int clampedPercent = Math.max(0, Math.min(100, percent));
      int barH = 6;
      int radius = 3;
      int fillW = Math.max(0, Math.min(width, Math.round((float)width * ((float)clampedPercent / 100.0F))));
      context.method_51439(this.field_22793, class_2561.method_43470(label), x, y - 12, theme.textStrong, false);
      this.drawPanel(context, x, y, width, barH, radius, theme.panel2);
      this.drawBorder(context, x, y, width, barH, radius, theme.borderSoft);
      if (fillW > 0) {
         this.drawPanel(context, x, y, fillW, barH, radius, theme.accentStrong);
      }

      int knobX = x + fillW;
      context.method_25294(knobX - 2, y - 4, knobX + 2, y + barH + 4, theme.accentSoft);
      context.method_51439(this.field_22793, class_2561.method_43470(clampedPercent + "%"), x + width + 8, y - 5, theme.textDim, false);
   }

   private void renderSettings(class_332 context, int mouseX, int mouseY, Theme theme) {
      int x = this.contentX + 14;
      int y = this.contentY + this.topAreaH + 14;
      int w = this.contentW - 28;
      int h = this.contentH - this.topAreaH - 28;
      this.drawGlow(context, x, y, w, h, 18, theme.accentSoft, 8);
      this.drawPanel(context, x, y, w, h, 18, theme.panel2);
      this.drawBorder(context, x, y, w, h, 18, theme.borderSoft);
      context.method_51439(this.field_22793, class_2561.method_43470("Client Settings"), x + 16, y + 16, theme.textStrong, false);
      context.method_51439(this.field_22793, class_2561.method_43470("Glass UI, opacity, theme, and permanent autosave."), x + 16, y + 34, theme.textDim, false);
      int clipX1 = x + 12;
      int clipY1 = y + 48;
      int clipX2 = x + w - 12;
      int clipY2 = y + h - 12;
      if (clipX2 > clipX1 && clipY2 > clipY1) {
         int innerX = x + 16;
         int baseY = y + 58 - (int)this.settingsScroll;
         context.method_44379(clipX1, clipY1, clipX2, clipY2);
         context.method_51439(this.field_22793, class_2561.method_43470("Themes"), innerX, baseY, theme.textStrong, false);
         int tileW = 132;
         int tileH = 32;
         int gap = 8;
         int themesStartY = baseY + 18;
         ThemePreset[] themes = new ThemePreset[]{ModernClickGUI.ThemePreset.DEFAULT, ModernClickGUI.ThemePreset.DARK, ModernClickGUI.ThemePreset.OCEAN, ModernClickGUI.ThemePreset.RED, ModernClickGUI.ThemePreset.VIOLET, ModernClickGUI.ThemePreset.FOREST};

         for(int i = 0; i < themes.length; ++i) {
            int col = i % 3;
            int row = i / 3;
            int bx = innerX + col * (tileW + gap);
            int by = themesStartY + row * (tileH + 10) - (int)this.settingsScroll;
            boolean selected = this.activeTheme == themes[i];
            this.drawPanel(context, bx, by, tileW, tileH, 10, selected ? theme.accentStrong : theme.panel);
            this.drawBorder(context, bx, by, tileW, tileH, 10, selected ? theme.glowStrong : theme.borderSoft);
            context.method_27534(this.field_22793, class_2561.method_43470(themes[i].name().toLowerCase(Locale.ROOT)), bx + tileW / 2, by + 10, selected ? -1 : theme.textStrong);
         }

         int sliderW = Math.max(220, w - 250);
         int sliderY1 = themesStartY + 2 * (tileH + 10) + 18;
         int sliderY2 = sliderY1 + 44;
         int sliderY3 = sliderY2 + 44;
         int sliderY4 = sliderY3 + 44;
         this.drawSettingsSlider(context, innerX, sliderY1, sliderW, "GUI Opacity", (int)(this.guiOpacity * 100.0F), theme);
         this.drawSettingsSlider(context, innerX, sliderY2, sliderW, "Panel Opacity", (int)(this.panelOpacity * 100.0F), theme);
         this.drawSettingsSlider(context, innerX, sliderY3, sliderW, "Glass Blur", (int)(this.blurStrength * 100.0F), theme);
         this.drawSettingsSlider(context, innerX, sliderY4, sliderW, "Module Opacity", (int)(this.moduleOpacity * 100.0F), theme);
         int toggleY = sliderY4 + 54;
         int btnW = 130;
         int btnH = 26;
         this.drawToggleButton(context, innerX, toggleY, btnW, btnH, this.compactMode ? "Compact: ON" : "Compact: OFF", theme, this.compactMode);
         this.drawToggleButton(context, innerX + btnW + gap, toggleY, btnW, btnH, this.roundedMode ? "Rounded: ON" : "Rounded: OFF", theme, this.roundedMode);
         this.drawToggleButton(context, innerX + 2 * (btnW + gap), toggleY, btnW, btnH, this.showDescriptions ? "Labels: ON" : "Labels: OFF", theme, this.showDescriptions);
         this.drawToggleButton(context, innerX + 3 * (btnW + gap), toggleY, btnW, btnH, this.glowStrength > 1.0F ? "Glow: HIGH" : "Glow: NORMAL", theme, this.glowStrength > 1.0F);
         int actionsY = toggleY + 40;
         this.drawMiniButton(context, innerX, actionsY, 160, 26, "Reset Settings", theme);
         this.drawMiniButton(context, innerX + 172, actionsY, 160, 26, "Cancel Changes", theme);
         this.drawMiniButton(context, innerX + 344, actionsY, 160, 26, "Open HUD Editor", theme);
         int infoY = actionsY + 42;
         this.drawMiniButton(context, innerX, infoY, 220, 26, "Auto-save is enabled", theme);
         this.drawMiniButton(context, innerX + 232, infoY, 220, 26, "Permanent on restart", theme);
         int bottomY = infoY + 42;
         this.drawPanel(context, innerX, bottomY, 380, 78, 14, theme.panel);
         this.drawBorder(context, innerX, bottomY, 380, 78, 14, theme.borderSoft);
         context.method_51439(this.field_22793, class_2561.method_43470("Settings are saved instantly to config/optix_client_settings.json"), innerX + 12, bottomY + 12, theme.textStrong, false);
         context.method_51439(this.field_22793, class_2561.method_43470("Use Cancel to revert to the values you had when the page opened."), innerX + 12, bottomY + 30, theme.textDim, false);
         int contentHeight = bottomY + 92 - (y + 58);
         float maxScroll = Math.max(0.0F, (float)(contentHeight - (h - 90)));
         this.settingsTargetScroll = clampFloat(this.settingsTargetScroll, 0.0F, maxScroll);
         this.settingsScroll = lerp(this.settingsScroll, this.settingsTargetScroll, 0.2F);
         this.settingsScroll = clampFloat(this.settingsScroll, 0.0F, maxScroll);
         int barX = x + w - 8;
         int barH = clipY2 - clipY1;
         int handleH = Math.max(20, (int)((float)barH * ((float)barH / ((float)barH + maxScroll + 1.0F))));
         float ratio = maxScroll > 0.001F ? this.settingsScroll / maxScroll : 0.0F;
         int handleY = this.settingsTargetScroll <= 0.5F ? clipY1 : (int)((float)clipY1 + ratio * (float)Math.max(0, barH - handleH));
         context.method_25294(barX, clipY1, barX + 2, clipY2, 570425344);
         context.method_25294(barX, handleY, barX + 2, handleY + handleH, theme.accentStrong);
         int accBtnY = y + 58 + 600 - (int)this.settingsScroll;
         this.drawMiniButton(context, innerX, accBtnY, 220, 26, "Open Account Switcher", theme);
         context.method_44380();
      }
   }

   private boolean handleSkinManagerClick(double mouseX, double mouseY, int button) {
      int x = this.contentX + 16;
      int y = this.contentY + this.topAreaH + 2;
      int w = this.contentW - 32;
      int h = this.contentH - this.topAreaH - 14;
      if (!this.skinManagerOpen) {
         int previewBoxX = x + 12;
         int previewBoxY = y + 36;
         int previewBoxW = Math.max(230, w - 24);
         int previewBoxH = Math.min(300, Math.max(220, h - 58));
         int btnX = previewBoxX + 14;
         int btnY = previewBoxY + previewBoxH - 30;
         int btnW = previewBoxW - 28;
         int btnH = 24;
         if (button == 0 && this.isInside(mouseX, mouseY, btnX, btnY, btnW, btnH)) {
            this.openSkinManager();
            return true;
         } else {
            return true;
         }
      } else {
         int tabY = y + 38;
         int tabW = 96;
         int tabH = 24;
         int tabGap = 8;
         int tabX = x + 16;
         if (button == 0 && this.isInside(mouseX, mouseY, tabX, tabY, tabW, tabH)) {
            this.skinTab = ModernClickGUI.SkinTab.IMPORTED;
            this.skinSearchFocused = false;
            return true;
         } else if (button == 0 && this.isInside(mouseX, mouseY, tabX + tabW + tabGap, tabY, tabW, tabH)) {
            this.skinTab = ModernClickGUI.SkinTab.DOWNLOAD;
            this.skinSearchFocused = false;
            return true;
         } else if (button == 0 && this.isInside(mouseX, mouseY, tabX + 2 * (tabW + tabGap), tabY, tabW, tabH)) {
            this.skinTab = ModernClickGUI.SkinTab.SETTINGS;
            this.skinSearchFocused = false;
            return true;
         } else {
            int backX = x + w - 104;
            if (button == 0 && this.isInside(mouseX, mouseY, backX, y + 12, 82, 22)) {
               this.closeSkinManager();
               return true;
            } else {
               int contentStartX = x + 16;
               int contentStartY = y + 100;
               int contentW2 = w - 32;
               int contentH2 = h - 118;
               switch (this.skinTab.ordinal()) {
                  case 0:
                     int listX = contentStartX;
                     int listY = contentStartY;
                     int listW = Math.min(294, Math.max(250, (contentW2 - 12) / 2));
                     int listH = Math.max(176, contentH2);
                     int previewX = contentStartX + listW + 12;
                     int previewW = contentW2 - listW - 12;
                     int previewY = contentStartY;
                     int previewH = listH;
                     boolean stacked = previewW < 220;
                     if (stacked) {
                        listW = contentW2;
                        listH = Math.max(150, Math.min(190, contentH2 / 2));
                        previewX = contentStartX;
                        previewY = contentStartY + listH + 10;
                        previewH = Math.max(130, contentH2 - listH - 10);
                     }

                     List<SkinManager.SkinEntry> skins = this.safeImportedSkins();
                     int itemH = 34;
                     int gap2 = 8;
                     int itemY = contentStartY + 34;

                     for(SkinManager.SkinEntry entry : skins) {
                        if (itemY + itemH > listY + listH - 12) {
                           break;
                        }

                        int itemX = listX + 10;
                        int itemW = listW - 20;
                        if (button == 0 && this.isInside(mouseX, mouseY, itemX, itemY, itemW, itemH)) {
                           this.selectedSkinEntry = entry;
                           this.skinStatusText = "Selected " + skinEntryName(entry) + ".";
                           this.skinMessage = this.skinStatusText;
                           return true;
                        }

                        itemY += itemH + gap2;
                     }

                     int btnW = 120;
                     int btnH = 24;
                     int applyY = previewY + previewH - 40;
                     int applyX = previewX + 12;
                     int reloadX = previewX + 20 + btnW;
                     if (button == 0 && this.isInside(mouseX, mouseY, applyX, applyY, btnW, btnH)) {
                        if (this.selectedSkinEntry != null) {
                           SkinManager.applySkin(this.selectedSkinEntry);
                           this.skinStatusText = "Applied " + skinEntryName(this.selectedSkinEntry) + ".";
                           this.skinMessage = this.skinStatusText;
                        } else {
                           this.skinStatusText = "No skin selected.";
                           this.skinMessage = this.skinStatusText;
                        }

                        return true;
                     }

                     if (button == 0 && this.isInside(mouseX, mouseY, reloadX, applyY, btnW, btnH)) {
                        SkinManager.refreshImportedSkins();
                        this.refreshSkinSelection();
                        this.skinStatusText = "Reloaded skins.";
                        this.skinMessage = this.skinStatusText;
                        return true;
                     }
                     break;
                  case 1:
                     int boxW = Math.min(320, w - 32);
                     int boxX = x + 16;
                     int boxY = y + 100;
                     int inputX = boxX + 12;
                     int inputY = boxY + 34;
                     int inputW = boxW - 24;
                     int inputH = 24;
                     if (button == 0 && this.isInside(mouseX, mouseY, inputX, inputY, inputW, inputH)) {
                        this.skinSearchFocused = true;
                        return true;
                     }

                     if (button == 0 && this.isInside(mouseX, mouseY, inputX, inputY + 32, 110, 24)) {
                        this.triggerSkinDownload();
                        return true;
                     }

                     if (button == 0 && this.isInside(mouseX, mouseY, inputX + 118, inputY + 32, 110, 24)) {
                        this.skinSearchText = "";
                        this.skinStatusText = "";
                        this.skinMessage = "";
                        this.skinSearchFocused = true;
                        return true;
                     }
                     break;
                  case 2:
                     int boxW = Math.min(440, w - 32);
                     int boxX = x + 16;
                     int boxY = y + 100;
                     if (button == 0 && this.isInside(mouseX, mouseY, boxX + 12, boxY + 98, 120, 24)) {
                        if (this.selectedSkinEntry != null) {
                           SkinManager.applySkin(this.selectedSkinEntry);
                           this.skinStatusText = "Saved.";
                           this.skinMessage = this.skinStatusText;
                        } else {
                           this.skinStatusText = "No skin selected.";
                           this.skinMessage = this.skinStatusText;
                        }

                        return true;
                     }

                     if (button == 0 && this.isInside(mouseX, mouseY, boxX + 140, boxY + 98, 120, 24)) {
                        SkinManager.refreshImportedSkins();
                        this.refreshSkinSelection();
                        this.skinStatusText = "Reloaded skins.";
                        this.skinMessage = this.skinStatusText;
                        return true;
                     }

                     if (button == 0 && this.isInside(mouseX, mouseY, boxX + 268, boxY + 98, 90, 24)) {
                        SkinManager.clearSelectedSkin();
                        this.selectedSkinEntry = null;
                        this.skinStatusText = "Reset to default.";
                        this.skinMessage = this.skinStatusText;
                        return true;
                     }
               }

               return true;
            }
         }
      }
   }

   private void renderSkinManagerPane(class_332 context, int mouseX, int mouseY, Theme theme) {
      int x = this.contentX + 16;
      int y = this.contentY + this.topAreaH + 2;
      int w = this.contentW - 32;
      int h = this.contentH - this.topAreaH - 14;
      this.drawGlow(context, x, y, w, h, 18, theme.accentSoft, 8);
      this.drawPanel(context, x, y, w, h, 18, theme.panel2);
      this.drawBorder(context, x, y, w, h, 18, theme.borderSoft);
      context.method_51439(this.field_22793, class_2561.method_43470("Skin Manager"), x + 16, y + 14, theme.textStrong, false);
      context.method_51439(this.field_22793, class_2561.method_43470(""), x + 16, y + 32, theme.textDim, false);
      if (!this.skinStatusText.isEmpty()) {
         context.method_51439(this.field_22793, class_2561.method_43470(this.skinStatusText), x + 16, y + 44, theme.textDim, false);
      }

      if (!this.skinManagerOpen) {
         int previewBoxX = x + 12;
         int previewBoxY = y + 36;
         int previewBoxW = Math.max(230, w - 24);
         int previewBoxH = Math.min(300, Math.max(220, h - 58));
         this.drawPanel(context, previewBoxX, previewBoxY, previewBoxW, previewBoxH, 16, theme.panel);
         this.drawBorder(context, previewBoxX, previewBoxY, previewBoxW, previewBoxH, 16, theme.borderSoft);
         int viewportX = previewBoxX + 14;
         int viewportY = previewBoxY + 28;
         int viewportW = previewBoxW - 28;
         int viewportH = Math.max(116, previewBoxH - 86);
         this.drawPanel(context, viewportX, viewportY, viewportW, viewportH, 14, theme.panel2);
         this.drawBorder(context, viewportX, viewportY, viewportW, viewportH, 14, theme.borderSoft);
         context.method_51439(this.field_22793, class_2561.method_43470("3D Preview"), previewBoxX + 14, previewBoxY + 12, theme.textStrong, false);
         if (MC.field_1724 != null) {
            int entityLeft = viewportX + 16;
            int entityTop = viewportY + 6;
            int entityRight = viewportX + viewportW - 16;
            int entityBottom = viewportY + viewportH - 18;
            int entityWidth = entityRight - entityLeft;
            int entityHeight = entityBottom - entityTop;
            if (entityWidth > 40 && entityHeight > 80) {
               context.method_44379(viewportX, viewportY, viewportX + viewportW, viewportY + viewportH);
               class_490.method_2486(context, entityLeft, entityTop, entityRight, entityBottom, 52, 1.0F, (float)(mouseX - (entityLeft + entityWidth / 2)) * 0.015F, (float)(mouseY - (entityTop + entityHeight / 2)) * 0.015F, MC.field_1724);
               context.method_44380();
            }
         }

         context.method_51439(this.field_22793, class_2561.method_43470("Press Change Skin to open manager"), previewBoxX + 14, previewBoxY + previewBoxH - 50, theme.textDim, false);
         if (!this.skinMessage.isEmpty()) {
            context.method_51439(this.field_22793, class_2561.method_43470(this.skinMessage), previewBoxX + 14, previewBoxY + previewBoxH - 36, theme.textDim, false);
         }

         this.drawMiniButton(context, previewBoxX + 14, previewBoxY + previewBoxH - 28, previewBoxW - 28, 24, "Change Skin", theme);
      } else {
         int tabY = y + 38;
         int tabW = 96;
         int tabH = 24;
         int tabGap = 8;
         int tabX = x + 16;
         this.drawToggleButton(context, tabX, tabY, tabW, tabH, "Imported", theme, this.skinTab == ModernClickGUI.SkinTab.IMPORTED);
         this.drawToggleButton(context, tabX + tabW + tabGap, tabY, tabW, tabH, "Download", theme, this.skinTab == ModernClickGUI.SkinTab.DOWNLOAD);
         this.drawToggleButton(context, tabX + 2 * (tabW + tabGap), tabY, tabW, tabH, "Settings", theme, this.skinTab == ModernClickGUI.SkinTab.SETTINGS);
         int backX = x + w - 104;
         this.drawMiniButton(context, backX, y + 12, 82, 22, "Back", theme);
         context.method_51439(this.field_22793, class_2561.method_43470("Tab: " + this.skinTab.label), x + 16, y + 78, theme.textDim, false);
         switch (this.skinTab.ordinal()) {
            case 0:
               int contentStartX = x + 16;
               int contentStartY = y + 100;
               int contentW2 = w - 32;
               int contentH2 = h - 118;
               int listX = contentStartX;
               int listY = contentStartY;
               int listW = Math.min(294, Math.max(250, (contentW2 - 12) / 2));
               int listH = Math.max(176, contentH2);
               int previewX = contentStartX + listW + 12;
               int previewW = contentW2 - listW - 12;
               int previewY = contentStartY;
               int previewH = listH;
               boolean stacked = previewW < 220;
               if (stacked) {
                  listW = contentW2;
                  listH = Math.max(150, Math.min(190, contentH2 / 2));
                  previewX = contentStartX;
                  previewY = contentStartY + listH + 10;
                  previewW = contentW2;
                  previewH = Math.max(130, contentH2 - listH - 10);
               }

               this.drawPanel(context, contentStartX, contentStartY, listW, listH, 14, theme.panel);
               this.drawBorder(context, contentStartX, contentStartY, listW, listH, 14, theme.borderSoft);
               context.method_51439(this.field_22793, class_2561.method_43470("Imported skins"), contentStartX + 12, contentStartY + 10, theme.textStrong, false);
               List<SkinManager.SkinEntry> skins = this.safeImportedSkins();
               int itemH = 34;
               int gap2 = 8;
               int itemY = contentStartY + 34;
               context.method_44379(contentStartX, contentStartY, contentStartX + listW, contentStartY + listH);

               for(SkinManager.SkinEntry entry : skins) {
                  if (itemY + itemH > listY + listH - 12) {
                     break;
                  }

                  int itemX = listX + 10;
                  int itemW = listW - 20;
                  boolean active = this.selectedSkinEntry != null && skinEntryName(this.selectedSkinEntry).equalsIgnoreCase(skinEntryName(entry));
                  this.drawPanel(context, itemX, itemY, itemW, itemH, 10, active ? theme.accentSoft : theme.panel2);
                  this.drawBorder(context, itemX, itemY, itemW, itemH, 10, active ? theme.accentStrong : theme.borderSoft);
                  context.method_51439(this.field_22793, class_2561.method_43470(skinEntryName(entry)), itemX + 10, itemY + 10, theme.textStrong, false);
                  context.method_51439(this.field_22793, class_2561.method_43470(entry.sourceLabel()), itemX + itemW - 90, itemY + 10, theme.textDim, false);
                  itemY += itemH + gap2;
               }

               context.method_44380();
               this.drawPanel(context, previewX, previewY, previewW, previewH, 14, theme.panel);
               this.drawBorder(context, previewX, previewY, previewW, previewH, 14, theme.borderSoft);
               context.method_51439(this.field_22793, class_2561.method_43470("Preview"), previewX + 12, previewY + 10, theme.textStrong, false);
               if (MC.field_1724 != null) {
                  int entityLeft = previewX + 12;
                  int entityTop = previewY + 28;
                  int entityRight = previewX + previewW - 12;
                  int entityBottom = previewY + previewH - 70;
                  int entityWidth = entityRight - entityLeft;
                  int entityHeight = entityBottom - entityTop;
                  if (entityWidth > 40 && entityHeight > 80) {
                     context.method_44379(previewX, previewY, previewX + previewW, previewY + previewH);
                     class_490.method_2486(context, entityLeft, entityTop, entityRight, entityBottom, 52, 1.0F, (float)(mouseX - (entityLeft + entityWidth / 2)) * 0.015F, (float)(mouseY - (entityTop + entityHeight / 2)) * 0.015F, MC.field_1724);
                     context.method_44380();
                  }
               }

               int buttonY = previewY + previewH - 40;
               int buttonW = Math.max(90, (previewW - 36) / 2);
               this.drawMiniButton(context, previewX + 12, buttonY, buttonW, 24, "Apply", theme);
               this.drawMiniButton(context, previewX + 20 + buttonW, buttonY, buttonW, 24, "Reload", theme);
               break;
            case 1:
               int boxW = Math.min(320, w - 32);
               int boxX = x + 16;
               int boxY = y + 100;
               int boxH = 122;
               this.drawPanel(context, boxX, boxY, boxW, boxH, 14, theme.panel);
               this.drawBorder(context, boxX, boxY, boxW, boxH, 14, theme.borderSoft);
               context.method_51439(this.field_22793, class_2561.method_43470("Search a player name"), boxX + 12, boxY + 10, theme.textStrong, false);
               int inputX = boxX + 12;
               int inputY = boxY + 34;
               int inputW = boxW - 24;
               int inputH = 24;
               this.drawPanel(context, inputX, inputY, inputW, inputH, 8, this.skinSearchFocused ? theme.accentSoft : theme.panel2);
               this.drawBorder(context, inputX, inputY, inputW, inputH, 8, this.skinSearchFocused ? theme.accentStrong : theme.borderSoft);
               String display = this.skinSearchText != null && !this.skinSearchText.isEmpty() ? this.skinSearchText : "Type username here...";
               context.method_51439(this.field_22793, class_2561.method_43470(display), inputX + 8, inputY + 8, this.skinSearchText != null && !this.skinSearchText.isEmpty() ? theme.textStrong : theme.textDim, false);
               this.drawMiniButton(context, inputX, inputY + 32, 110, 24, "Download", theme);
               this.drawMiniButton(context, inputX + 118, inputY + 32, 110, 24, "Clear", theme);
               if (!this.skinMessage.isEmpty()) {
                  context.method_51439(this.field_22793, class_2561.method_43470(this.skinMessage), boxX + 12, boxY + 88, theme.textDim, false);
               }
               break;
            case 2:
               int boxW = Math.min(440, w - 32);
               int boxX = x + 16;
               int boxY = y + 100;
               int boxH = 160;
               this.drawPanel(context, boxX, boxY, boxW, boxH, 14, theme.panel);
               this.drawBorder(context, boxX, boxY, boxW, boxH, 14, theme.borderSoft);
               context.method_51439(this.field_22793, class_2561.method_43470("Settings"), boxX + 12, boxY + 10, theme.textStrong, false);
               context.method_51439(this.field_22793, class_2561.method_43470("Folder: .minecraft/skins"), boxX + 12, boxY + 34, theme.textDim, false);
               context.method_51439(this.field_22793, class_2561.method_43470("Downloads: .minecraft/OptiX/skins"), boxX + 12, boxY + 52, theme.textDim, false);
               context.method_51439(this.field_22793, class_2561.method_43470("Selected skin is saved in config automatically."), boxX + 12, boxY + 70, theme.textDim, false);
               this.drawMiniButton(context, boxX + 12, boxY + 98, 120, 24, "Save now", theme);
               this.drawMiniButton(context, boxX + 140, boxY + 98, 120, 24, "Reload skins", theme);
               this.drawMiniButton(context, boxX + 268, boxY + 98, 90, 24, "Default", theme);
         }

      }
   }

   private void refreshSkinSelection() {
      List<SkinManager.SkinEntry> skins = this.safeImportedSkins();
      if (skins.isEmpty()) {
         this.selectedSkinEntry = null;
      } else {
         if (this.selectedSkinEntry != null) {
            String selectedName = skinEntryName(this.selectedSkinEntry);

            for(SkinManager.SkinEntry entry : skins) {
               if (entry == this.selectedSkinEntry || skinEntryName(entry).equalsIgnoreCase(selectedName)) {
                  this.selectedSkinEntry = entry;
                  return;
               }
            }
         }

         this.selectedSkinEntry = (SkinManager.SkinEntry)skins.get(0);
      }
   }

   private void openSkinManager() {
      this.skinManagerOpen = true;
      this.skinTab = ModernClickGUI.SkinTab.IMPORTED;
      this.skinSearchFocused = false;
      this.skinStatusText = "";
      this.skinMessage = "";
      this.refreshSkinSelection();
   }

   private void closeSkinManager() {
      this.skinManagerOpen = false;
      this.skinSearchFocused = false;
      this.skinStatusText = "";
      this.skinMessage = "";
   }

   private void triggerSkinDownload() {
      String query = this.skinSearchText == null ? "" : this.skinSearchText.trim();
      if (query.isEmpty()) {
         this.skinStatusText = "Type a username first.";
         this.skinMessage = this.skinStatusText;
      } else {
         this.skinStatusText = "Downloading...";
         this.skinMessage = this.skinStatusText;
         if (!this.invokeSkinDownloadReflectively(query)) {
            try {
               SkinManager.downloadAndImportAsync(query, (entry) -> {
                  this.selectedSkinEntry = entry;
                  SkinManager.refreshImportedSkins();
                  this.refreshSkinSelection();
                  this.skinTab = ModernClickGUI.SkinTab.IMPORTED;
                  this.skinStatusText = "Downloaded " + skinEntryName(entry) + ".";
                  this.skinMessage = this.skinStatusText;
               }, (error) -> {
                  this.skinStatusText = error != null && !error.isBlank() ? error : "Download failed.";
                  this.skinMessage = this.skinStatusText;
               });
            } catch (Throwable var3) {
               this.skinStatusText = "Download failed.";
               this.skinMessage = this.skinStatusText;
            }

         }
      }
   }

   private boolean invokeSkinDownloadReflectively(String query) {
      try {
         Method target = null;

         for(Method method : SkinManager.class.getDeclaredMethods()) {
            if ("downloadSkin".equals(method.getName())) {
               Class<?>[] params = method.getParameterTypes();
               if (params.length == 1 && params[0] == String.class) {
                  target = method;
                  break;
               }
            }
         }

         if (target == null) {
            return false;
         } else {
            target.setAccessible(true);
            Object result = target.invoke((Object)null, query);
            if (result instanceof SkinManager.SkinEntry) {
               SkinManager.SkinEntry entry = (SkinManager.SkinEntry)result;
               this.selectedSkinEntry = entry;
               SkinManager.refreshImportedSkins();
               this.refreshSkinSelection();
               this.skinTab = ModernClickGUI.SkinTab.IMPORTED;
               this.skinStatusText = "Downloaded " + skinEntryName(entry) + ".";
               this.skinMessage = this.skinStatusText;
               return true;
            } else if (result instanceof Boolean) {
               Boolean ok = (Boolean)result;
               if (ok) {
                  SkinManager.refreshImportedSkins();
                  this.refreshSkinSelection();
                  this.skinTab = ModernClickGUI.SkinTab.IMPORTED;
                  this.skinStatusText = "Downloaded skin.";
                  this.skinMessage = this.skinStatusText;
               } else {
                  this.skinStatusText = "Download failed.";
                  this.skinMessage = this.skinStatusText;
               }

               return true;
            } else {
               if (result instanceof String) {
                  String msg = (String)result;
                  if (!msg.isBlank()) {
                     this.skinStatusText = msg;
                     this.skinMessage = msg;
                     return true;
                  }
               }

               SkinManager.refreshImportedSkins();
               this.refreshSkinSelection();
               this.skinStatusText = "Download started.";
               this.skinMessage = this.skinStatusText;
               return true;
            }
         }
      } catch (Throwable var8) {
         return false;
      }
   }

   private List<SkinManager.SkinEntry> safeImportedSkins() {
      try {
         List<SkinManager.SkinEntry> skins = SkinManager.getImportedSkins();
         return skins != null ? skins : Collections.emptyList();
      } catch (Throwable var2) {
         return Collections.emptyList();
      }
   }

   private static String skinEntryName(SkinManager.SkinEntry entry) {
      return entry != null && entry.name != null ? entry.name.trim() : "";
   }

   private void buildCategoryTabs() {
      this.categoryTabs.clear();
      int y = this.contentY + 60;
      int gap = 6;
      List<Category> categories = new ArrayList();

      for(Category category : Category.values()) {
         if (category != Category.ALL) {
            categories.add(category);
         }
      }

      int tabCount = categories.size() + 1;
      int available = Math.max(320, this.moduleAreaRight() - (this.contentX + 16));
      int tabW = Math.max(76, (available - gap * (tabCount - 1)) / tabCount);
      tabW = Math.min(tabW, 104);
      int tabH = 24;
      int startX = this.contentX + 16;
      this.categoryTabs.add(new CategoryTab((Category)null, true, startX, y, tabW, tabH));
      int index = 1;

      for(Category category : categories) {
         this.categoryTabs.add(new CategoryTab(category, false, startX + index * (tabW + gap), y, tabW, tabH));
         ++index;
      }

   }

   private void buildSidebarButtons() {
      this.sidebarButtons.clear();
      int x = this.panelX + 10;
      int size = 38;
      int startY = this.contentY + 102;
      int gap = 8;
      SidebarSection[] sections = ModernClickGUI.SidebarSection.values();

      for(int i = 0; i < sections.length; ++i) {
         this.sidebarButtons.add(new SidebarButton(sections[i], x, startY + i * (size + gap), size, size));
      }

   }

   private void buildSettingChips() {
      this.settingChips.clear();
      int x = this.contentX + 14;
      int y = this.contentY + this.topAreaH + 14;
      int chipW = 136;
      int chipH = 34;
      int gap = 10;
      int row1 = y + 52;
      int row2 = row1 + 44;
      int row3 = row2 + 44;
      this.settingChips.add(new SettingChip("Theme: Default", x + 10, row1, chipW, chipH, () -> this.activeTheme = ModernClickGUI.ThemePreset.DEFAULT));
      this.settingChips.add(new SettingChip("Theme: Dark", x + 10 + chipW + gap, row1, chipW, chipH, () -> this.activeTheme = ModernClickGUI.ThemePreset.DARK));
      this.settingChips.add(new SettingChip("Theme: Ocean", x + 10 + 2 * (chipW + gap), row1, chipW, chipH, () -> this.activeTheme = ModernClickGUI.ThemePreset.OCEAN));
      this.settingChips.add(new SettingChip("Theme: Red", x + 10 + 3 * (chipW + gap), row1, chipW, chipH, () -> this.activeTheme = ModernClickGUI.ThemePreset.RED));
      this.settingChips.add(new SettingChip("Theme: Violet", x + 10, row2, chipW, chipH, () -> this.activeTheme = ModernClickGUI.ThemePreset.VIOLET));
      this.settingChips.add(new SettingChip("Theme: Forest", x + 10 + chipW + gap, row2, chipW, chipH, () -> {
         this.activeTheme = ModernClickGUI.ThemePreset.FOREST;
         this.csClientScreen = true;
         this.saveClientSettings();
      }));
      this.settingChips.add(new SettingChip(this.dragMode ? "Disable Drag" : "Enable Drag", x + 10 + 2 * (chipW + gap), row2, chipW, chipH, () -> {
         this.activeSection = ModernClickGUI.SidebarSection.DRAG;
         this.dragMode = true;
         this.configPanelOpen = false;
         this.selectedModule = null;
         this.hudDraggingModule = null;
      }));
      this.settingChips.add(new SettingChip(this.compactMode ? "Normal Cards" : "Compact Cards", x + 10 + 3 * (chipW + gap), row2, chipW, chipH, () -> this.compactMode = !this.compactMode));
      this.settingChips.add(new SettingChip(this.showDescriptions ? "Hide Descriptions" : "Show Descriptions", x + 10, row3, chipW, chipH, () -> this.showDescriptions = !this.showDescriptions));
      this.settingChips.add(new SettingChip(this.roundedMode ? "Sharper Corners" : "Rounded Corners", x + 10 + chipW + gap, row3, chipW, chipH, () -> this.roundedMode = !this.roundedMode));
      this.settingChips.add(new SettingChip("Glow +", x + 10 + 2 * (chipW + gap), row3, chipW, chipH, () -> this.glowStrength = Math.min(2.0F, this.glowStrength + 0.15F)));
      this.settingChips.add(new SettingChip("Reload", x + 10 + 3 * (chipW + gap), row3, chipW, chipH, () -> {
      }));
      Module var10 = this.selectedModule;
      if (var10 instanceof MotionBlurModule motionBlur) {
         this.settingChips.add(new SettingChip("Motion Blur: " + motionBlur.getBlurLevel(), 0, 0, 110, 20, () -> motionBlur.cycleBlurLevel()));
      }

   }

   private boolean matchesSearch(Module module) {
      String q = this.searchBar.getText().trim().toLowerCase(Locale.ROOT);
      if (q.isEmpty()) {
         return true;
      } else {
         return module.getName().toLowerCase(Locale.ROOT).contains(q) || module.getCategory().getDisplayName().toLowerCase(Locale.ROOT).contains(q) || module.getCategory().name().toLowerCase(Locale.ROOT).contains(q);
      }
   }

   private Theme theme() {
      Theme var10000;
      switch (this.activeTheme.ordinal()) {
         case 0 -> var10000 = new Theme(-16052716, -15591386, -16184302, -11838086, -14800581, -8614657, 863800575, -1434678017, -657409, -6774088);
         case 1 -> var10000 = new Theme(-16447992, -15986665, -16513785, -12958118, -15328476, -7643914, 864771318, -1433707274, -1, -6511697);
         case 2 -> var10000 = new Theme(-16315630, -15919840, -16183786, -12877066, -14796150, -10443270, 861971962, -1436506630, -1, -5721145);
         case 3 -> var10000 = new Theme(-15595768, -15069936, -15858426, -8446691, -12252662, -45715, 872369517, -1426109075, -1, -3692112);
         case 4 -> var10000 = new Theme(-16448249, -15921900, -16316662, -8758017, -13951147, -45569, 1157582335, -1426108929, -1, -4671288);
         case 5 -> var10000 = new Theme(-16119286, -15855346, -16250872, -15066598, -14342875, -15411180, 571791380, 1427429396, -1, -6645094);
         default -> throw new MatchException((String)null, (Throwable)null);
      }

      return var10000;
   }

   private void drawPanel(class_332 context, int x, int y, int w, int h, int radius, int color) {
      int fill = applyAlpha(color | -16777216, this.effectivePanelOpacity());
      int border = applyAlpha(color, this.effectivePanelOpacity());
      RoundedRectRenderer.outline(context, (float)x, (float)y, (float)w, (float)h, Math.max(4.0F, (float)radius), 1.0F, border, fill);
   }

   private int lerpColor(int a, int b, float t) {
      int ar = a >> 16 & 255;
      int ag = a >> 8 & 255;
      int ab = a & 255;
      int br = b >> 16 & 255;
      int bg = b >> 8 & 255;
      int bb = b & 255;
      int rr = (int)((float)ar + (float)(br - ar) * t);
      int rg = (int)((float)ag + (float)(bg - ag) * t);
      int rb = (int)((float)ab + (float)(bb - ab) * t);
      return -16777216 | rr << 16 | rg << 8 | rb;
   }

   private void drawHubButton(class_332 ctx, int x, int y, int w, int h, String text, Theme theme, int mouseX, int mouseY) {
      boolean hover = this.isInside((double)mouseX, (double)mouseY, x, y, w, h);
      long now = System.nanoTime();
      long lastNs = (Long)this.hubHoverLastNs.getOrDefault(text, 0L);
      if (lastNs == 0L) {
         lastNs = now;
      }

      float dt = Math.min((float)(now - lastNs) / 1.0E9F, 0.05F);
      this.hubHoverLastNs.put(text, now);
      float anim = (Float)this.hubHoverMap.getOrDefault(text, 0.0F);
      float target = hover ? 1.0F : 0.0F;
      anim = expLerp(anim, target, 16.0F, dt);
      this.hubHoverMap.put(text, anim);
      int base = theme.panel2;
      int glow = theme.accentStrong;
      int color = this.lerpColor(base, glow, anim * 0.35F);
      if (hover) {
         this.drawGlow(ctx, x - 2, y - 2, w + 4, h + 4, 10, theme.accentSoft, 6);
      }

      ctx.method_25294(x, y, x + w, y + h, color);
      int borderColor = this.lerpColor(theme.borderSoft, theme.accentStrong, anim);
      this.drawBorder(ctx, x, y, w, h, 8, borderColor);
      ctx.method_51439(this.field_22793, class_2561.method_43470(text), x + w / 2 - this.field_22793.method_1727(text) / 2, y + h / 2 - 4, -1, true);
   }

   private void drawBorder(class_332 context, int x, int y, int w, int h, int radius, int color) {
      int c = applyAlpha(color, this.effectivePanelOpacity());
      RoundedRectRenderer.outline(context, (float)x, (float)y, (float)w, (float)h, Math.max(4.0F, (float)radius), 1.0F, c, 0);
   }

   private void drawVerticalDivider(class_332 context, int x, int y, int h, int color) {
      context.method_25294(x, y, x + 1, y + h, color);
   }

   private void drawHorizontalDivider(class_332 context, int x, int y, int w, int color) {
      context.method_25294(x, y, x + w, y + 1, color);
   }

   private void drawGlow(class_332 context, int x, int y, int w, int h, int radius, int color, int layers) {
      if (w > 0 && h > 0 && layers > 0) {
         int safeLayers = Math.min(layers, 3);
         int maxPad = 6;
         boolean useRoundedGlow = w <= 360 && h <= 220;

         for(int i = safeLayers; i >= 1; --i) {
            int pad = Math.min(maxPad, i * 2);
            int alpha = (int)((float)(color >>> 24 & 255) * 0.12F / (float)i);
            int c = alpha << 24 | color & 16777215;
            if (useRoundedGlow) {
               RoundedRectRenderer.outline(context, (float)(x - pad), (float)(y - pad), (float)(w + pad * 2), (float)(h + pad * 2), Math.max(6.0F, (float)(radius + pad)), 1.0F, c, 0);
            } else {
               context.method_25294(x - pad, y - pad, x + w + pad, y - pad + 1, c);
               context.method_25294(x - pad, y + h + pad - 1, x + w + pad, y + h + pad, c);
               context.method_25294(x - pad, y - pad, x - pad + 1, y + h + pad, c);
               context.method_25294(x + w + pad - 1, y - pad, x + w + pad, y + h + pad, c);
            }
         }

      }
   }

   private void drawNoise(class_332 context) {
      for(int y = 0; y < this.field_22790; y += 6) {
         context.method_25294(0, y, this.field_22789, y + 1, 150994943);
      }

   }

   private static float lerp(float current, float target, float speed) {
      float diff = target - current;
      return Math.abs(diff) < 0.001F ? target : current + diff * speed;
   }

   private static float clampFloat(float value, float min, float max) {
      return Math.max(min, Math.min(max, value));
   }

   private boolean useHiFiEffect(int x, int y, int w, int h, int clipLeft, int clipTop, int clipRight, int clipBottom, boolean active) {
      if (!active) {
         return false;
      } else if (w > 0 && h > 0) {
         return x + w >= clipLeft && x <= clipRight && y + h >= clipTop && y <= clipBottom;
      } else {
         return false;
      }
   }

   private static int clamp(int value, int min, int max) {
      return Math.max(min, Math.min(max, value));
   }

   private void drawConfigDiamond(class_332 context, int x, int y, int color) {
      RoundedRectRenderer.fill(context, (float)x, (float)y, 10.0F, 10.0F, 5.0F, color);
   }

   private static String pretty(Category category) {
      return category.getDisplayName();
   }

   private static String iconFor(Module module) {
      class_2960 icon = module.getIcon();
      if (icon != null) {
         return "◆";
      } else {
         String n = module.getName().toLowerCase(Locale.ROOT);
         if (n.contains("fps")) {
            return "◌";
         } else if (n.contains("coord")) {
            return "⌖";
         } else if (n.contains("armor")) {
            return "▣";
         } else if (n.contains("cps")) {
            return "☉";
         } else if (n.contains("key")) {
            return "⌨";
         } else if (n.contains("mouse")) {
            return "▤";
         } else if (n.contains("shield")) {
            return "⛨";
         } else if (n.contains("bright")) {
            return "☀";
         } else if (n.contains("effect")) {
            return "✦";
         } else if (n.contains("cross")) {
            return "◎";
         } else if (n.contains("waypoint")) {
            return "⌂";
         } else {
            return n.contains("score") ? "▤" : "◆";
         }
      }
   }

   private static String moduleAnimKey(Module module) {
      String var10000 = module.getName();
      return var10000 + "|" + module.getCategory().name();
   }

   private void drawRemoteImage(class_332 context, String url, int x, int y, int w, int h) {
      context.method_25294(x, y, x + w, y + h, -15066598);
      if (url != null && !url.isEmpty()) {
         class_2960 id = (class_2960)this.imageCache.get(url);
         if (id != null) {
            try {
               context.method_25294(x, y, x + w, y + h, -1);
            } catch (Exception var9) {
            }

         } else if (!this.loadingImages.contains(url)) {
            this.loadingImages.add(url);
            (new Thread(() -> {
               try {
                  URL u = new URL(url);
                  BufferedImage img = ImageIO.read(u);
                  if (img == null) {
                     return;
                  }

                  class_1011 nativeImg = new class_1011(img.getWidth(), img.getHeight(), true);

                  for(int px = 0; px < img.getWidth(); ++px) {
                     for(int py = 0; py < img.getHeight(); ++py) {
                        nativeImg.method_4305(px, py, img.getRGB(px, py));
                     }
                  }

                  class_2960 newId = class_2960.method_60655("client", "img_" + Math.abs(url.hashCode()));
                  class_1043 tex = new class_1043(() -> "optix_texture", nativeImg);
                  class_310.method_1551().execute(() -> {
                     class_310.method_1551().method_1531().method_4616(newId, tex);
                     this.imageCache.put(url, newId);
                  });
               } catch (Exception var7) {
               }

            })).start();
         }
      }
   }

   private void renderUpdatePopup(class_332 context, Theme theme) {
      if (UpdateManager.hasPopup()) {
         String msg = UpdateManager.getPopupMessage();
         int boxW = Math.min(300, this.field_22793.method_1727(msg) + 24);
         int boxH = 26;
         int x = this.contentX + 24;
         int y = this.contentY + this.contentH - 252;
         this.drawPanel(context, x, y, boxW, boxH, 10, theme.panel2);
         this.drawBorder(context, x, y, boxW, boxH, 10, theme.accentStrong);
         context.method_51439(this.field_22793, class_2561.method_43470(msg), x + 12, y + 8, theme.textStrong, false);
      }
   }

   private boolean handleUpdaterPanelClick(double mouseX, double mouseY, int button) {
      int x = this.contentX + 32;
      int y = this.contentY + this.topAreaH + 190;
      int w = 235;
      int h = 96;
      if (button != 0) {
         return false;
      } else if (this.isInside(mouseX, mouseY, x + 12, y + 58, 94, 22)) {
         UpdateManager.checkForUpdates();
         return true;
      } else if (this.isInside(mouseX, mouseY, x + 129, y + 58, 94, 22)) {
         class_310.method_1551().method_1592();
         return true;
      } else {
         return false;
      }
   }

   private static enum SidebarSection {
      COSMETICS("Cosmetics"),
      MODULES("Modules"),
      DOWNLOADS("Downloads"),
      DRAG("Drag"),
      SETTINGS("Client Settings");

      final String label;

      private SidebarSection(String label) {
         this.label = label;
      }

      // $FF: synthetic method
      private static SidebarSection[] $values() {
         return new SidebarSection[]{COSMETICS, MODULES, DOWNLOADS, DRAG, SETTINGS};
      }
   }

   private static enum ThemePreset {
      DEFAULT,
      DARK,
      OCEAN,
      RED,
      VIOLET,
      FOREST;

      static ThemePreset fromName(String value) {
         if (value != null && !value.isBlank()) {
            try {
               return valueOf(value.toUpperCase(Locale.ROOT));
            } catch (Exception var2) {
               return VIOLET;
            }
         } else {
            return VIOLET;
         }
      }

      // $FF: synthetic method
      private static ThemePreset[] $values() {
         return new ThemePreset[]{DEFAULT, DARK, OCEAN, RED, VIOLET, FOREST};
      }
   }

   private static final class ClientSettingsData {
      float guiOpacity = 1.0F;
      float panelOpacity = 0.88F;
      float blurStrength = 0.65F;
      float moduleOpacity = 1.0F;
      String theme;
      boolean showDescriptions;
      boolean compactMode;
      boolean roundedMode;
      float glowStrength;

      private ClientSettingsData() {
         this.theme = ModernClickGUI.ThemePreset.DARK.name();
         this.showDescriptions = true;
         this.compactMode = true;
         this.roundedMode = true;
         this.glowStrength = 1.0F;
      }
   }

   private static final class ClientSettingsSnapshot {
      final ThemePreset theme;
      final float guiOpacity;
      final float panelOpacity;
      final float blurStrength;
      final float moduleOpacity;
      final boolean showDescriptions;
      final boolean compactMode;
      final boolean roundedMode;
      final float glowStrength;

      ClientSettingsSnapshot(ThemePreset theme, float guiOpacity, float panelOpacity, float blurStrength, float moduleOpacity, boolean showDescriptions, boolean compactMode, boolean roundedMode, float glowStrength) {
         this.theme = theme;
         this.guiOpacity = guiOpacity;
         this.panelOpacity = panelOpacity;
         this.blurStrength = blurStrength;
         this.moduleOpacity = moduleOpacity;
         this.showDescriptions = showDescriptions;
         this.compactMode = compactMode;
         this.roundedMode = roundedMode;
         this.glowStrength = glowStrength;
      }
   }

   private static final class SettingsSliderState {
      static final int NONE = 0;
      static final int GUI_OPACITY = 1;
      static final int PANEL_OPACITY = 2;
      static final int BLUR_STRENGTH = 3;
      static final int MODULE_OPACITY = 4;
   }

   private static class Ripple {
      float x;
      float y;
      float radius;
      float alpha;
   }

   private static enum SkinTab {
      IMPORTED("Imported"),
      DOWNLOAD("Download"),
      SETTINGS("Settings");

      final String label;

      private SkinTab(String label) {
         this.label = label;
      }

      // $FF: synthetic method
      private static SkinTab[] $values() {
         return new SkinTab[]{IMPORTED, DOWNLOAD, SETTINGS};
      }
   }

   private static final class Theme {
      final int panel;
      final int panel2;
      final int sidebar;
      final int border;
      final int borderSoft;
      final int accentStrong;
      final int accentSoft;
      final int glowStrong;
      final int textStrong;
      final int textDim;

      Theme(int panel, int panel2, int sidebar, int border, int borderSoft, int accentStrong, int accentSoft, int glowStrong, int textStrong, int textDim) {
         this.panel = panel;
         this.panel2 = panel2;
         this.sidebar = sidebar;
         this.border = border;
         this.borderSoft = borderSoft;
         this.accentStrong = accentStrong;
         this.accentSoft = accentSoft;
         this.glowStrong = glowStrong;
         this.textStrong = textStrong;
         this.textDim = textDim;
      }
   }

   private final class CategoryTab {
      private final Category category;
      private final boolean allTab;
      private final int x;
      private final int y;
      private final int w;
      private final int h;
      private float activeAnim = 0.0F;
      private float hoverAnim = 0.0F;

      private CategoryTab(Category category, boolean allTab, int x, int y, int w, int h) {
         this.category = category;
         this.allTab = allTab;
         this.x = x;
         this.y = y;
         this.w = w;
         this.h = h;
      }

      void tick() {
         this.hoverAnim = ModernClickGUI.lerp(this.hoverAnim, 0.0F, 0.08F);
      }

      boolean mouseClicked(double mx, double my, int button) {
         return button == 0 && this.contains(mx, my);
      }

      void render(class_332 context, int mouseX, int mouseY, Theme theme) {
         boolean selected = this.allTab ? ModernClickGUI.this.showAllModules : !ModernClickGUI.this.showAllModules && ModernClickGUI.this.activeCategory == this.category;
         boolean hovered = this.contains((double)mouseX, (double)mouseY);
         this.activeAnim = ModernClickGUI.lerp(this.activeAnim, selected ? 1.0F : 0.0F, 0.15F);
         this.hoverAnim = ModernClickGUI.lerp(this.hoverAnim, hovered ? 1.0F : 0.0F, 0.15F);
         int fill = selected ? theme.accentStrong : theme.panel2;
         int border = selected ? theme.accentStrong : theme.borderSoft;
         ModernClickGUI.this.drawGlow(context, this.x, this.y, this.w, this.h, this.h / 2, selected ? theme.glowStrong : theme.borderSoft, selected ? 2 : 1);
         RoundedRectRenderer.outline(context, (float)this.x, (float)this.y, (float)this.w, (float)this.h, (float)this.h / 2.0F, 1.0F, border, fill);
         String label = this.allTab ? "All" : this.category.getDisplayName();
         class_2561 display = class_2561.method_43470(label).method_27692(class_124.field_1067);
         context.method_27534(ModernClickGUI.this.field_22793, display, this.x + this.w / 2, this.y + 10, selected ? -1 : theme.textStrong);
         if (this.activeAnim > 0.01F) {
            RoundedRectRenderer.fill(context, (float)(this.x + 10), (float)(this.y + this.h - 4), (float)(this.w - 20), 2.0F, 1.0F, theme.accentStrong);
         }

      }

      private boolean contains(double mx, double my) {
         return mx >= (double)this.x && mx <= (double)(this.x + this.w) && my >= (double)this.y && my <= (double)(this.y + this.h);
      }
   }

   private final class SidebarButton {
      private final SidebarSection section;
      private final int x;
      private final int y;
      private final int size;
      private float hoverAnim = 0.0F;
      private long hoverLastNs = 0L;

      private SidebarButton(SidebarSection section, int x, int y, int size, int ignored) {
         this.section = section;
         this.x = x;
         this.y = y;
         this.size = size;
      }

      void tick() {
         this.hoverAnim = ModernClickGUI.lerp(this.hoverAnim, 0.0F, 0.08F);
      }

      boolean mouseClicked(double mx, double my, int button) {
         return button == 0 && this.contains(mx, my);
      }

      void render(class_332 context, int mouseX, int mouseY, Theme theme, SidebarSection active, boolean dragModeNow) {
         boolean selected = active == this.section || this.section == ModernClickGUI.SidebarSection.DRAG && dragModeNow;
         boolean hovered = this.contains((double)mouseX, (double)mouseY);
         long now = System.nanoTime();
         if (this.hoverLastNs == 0L) {
            this.hoverLastNs = now;
         }

         float dt = Math.min((float)(now - this.hoverLastNs) / 1.0E9F, 0.05F);
         this.hoverLastNs = now;
         this.hoverAnim = ModernClickGUI.expLerp(this.hoverAnim, hovered ? 1.0F : 0.0F, 15.0F, dt);
         int fill = selected ? theme.accentStrong : theme.panel2;
         int border = selected ? theme.accentStrong : theme.borderSoft;
         int glowColor = selected ? theme.glowStrong : theme.borderSoft;
         ModernClickGUI.this.drawGlow(context, this.x - 2, this.y - 2, this.size + 4, this.size + 4, 8, glowColor, selected ? 2 : 1);
         RoundedRectRenderer.outline(context, (float)this.x, (float)this.y, (float)this.size, (float)this.size, 8.0F, 1.0F, border, fill);
         String var10000;
         switch (this.section.ordinal()) {
            case 0 -> var10000 = "✦";
            case 1 -> var10000 = "▣";
            case 2 -> var10000 = "⌂";
            case 3 -> var10000 = "⌖";
            case 4 -> var10000 = "⚙";
            default -> throw new MatchException((String)null, (Throwable)null);
         }

         String label = var10000;
         class_2561 display = class_2561.method_43470(label).method_27692(class_124.field_1067);
         context.method_27534(ModernClickGUI.this.field_22793, display, this.x + this.size / 2, this.y + 17, selected ? -1 : theme.textStrong);
         if (selected) {
            ModernClickGUI.this.sidebarIndicatorTargetY = (float)(this.y + 6);
         }

      }

      private boolean contains(double mx, double my) {
         return mx >= (double)this.x && mx <= (double)(this.x + this.size) && my >= (double)this.y && my <= (double)(this.y + this.size);
      }
   }

   private final class SearchBar {
      private int x;
      private int y;
      private int w;
      private int h;
      private boolean focused;
      private String text = "";
      private float blink;

      void setBounds(int x, int y, int w, int h) {
         this.x = x;
         this.y = y;
         this.w = w;
         this.h = h;
      }

      void tick() {
         this.blink += 0.03F;
      }

      boolean mouseClicked(double mx, double my, int button) {
         if (button != 0) {
            return false;
         } else {
            this.focused = this.contains(mx, my);
            return this.focused;
         }
      }

      boolean keyPressed(class_11908 input) {
         if (!this.focused) {
            return false;
         } else if (input.comp_4795() == 259 && !this.text.isEmpty()) {
            this.text = this.text.substring(0, this.text.length() - 1);
            return true;
         } else if (input.method_74230()) {
            this.focused = false;
            ModernClickGUI.this.searchFocused = false;
            return true;
         } else if (input.method_74231()) {
            this.focused = false;
            ModernClickGUI.this.searchFocused = false;
            return true;
         } else {
            return false;
         }
      }

      boolean charTyped(class_11905 input) {
         if (!this.focused) {
            return false;
         } else if (!input.method_74227()) {
            return false;
         } else {
            String typed = input.method_74226();
            if (typed.isEmpty()) {
               return false;
            } else if (this.text.length() >= 48) {
               return true;
            } else {
               String var10001 = this.text;
               this.text = var10001 + typed.charAt(0);
               return true;
            }
         }
      }

      void render(class_332 context, int mouseX, int mouseY, Theme theme, boolean focusedNow) {
         boolean hovered = this.contains((double)mouseX, (double)mouseY);
         ModernClickGUI.this.drawGlow(context, this.x, this.y, this.w, this.h, this.h / 2, focusedNow ? theme.glowStrong : theme.borderSoft, hovered ? 2 : 1);
         RoundedRectRenderer.outline(context, (float)this.x, (float)this.y, (float)this.w, (float)this.h, (float)this.h / 2.0F, 1.0F, focusedNow ? theme.accentStrong : theme.borderSoft, focusedNow ? theme.panel2 : theme.panel);
         context.method_51439(ModernClickGUI.this.field_22793, class_2561.method_43470("⌕"), this.x + 10, this.y + 8, theme.textStrong, true);
         String shown = this.text.isEmpty() && !focusedNow ? "Search module" : this.text;
         int color = this.text.isEmpty() && !focusedNow ? theme.textDim : theme.textStrong;
         context.method_51439(ModernClickGUI.this.field_22793, class_2561.method_43470(shown), this.x + 28, this.y + 8, color, true);
         if (focusedNow && (int)(this.blink * 6.0F) % 2 == 0) {
            int cursorX = this.x + 28 + ModernClickGUI.this.field_22793.method_1727(shown);
            context.method_25294(cursorX + 1, this.y + 8, cursorX + 2, this.y + 20, theme.textStrong);
         }

      }

      boolean contains(double mx, double my) {
         return mx >= (double)this.x && mx <= (double)(this.x + this.w) && my >= (double)this.y && my <= (double)(this.y + this.h);
      }

      boolean isFocused() {
         return this.focused;
      }

      void setFocused(boolean focused) {
         this.focused = focused;
      }

      String getText() {
         return this.text;
      }
   }

   private final class SettingChip {
      private final String label;
      private final int x;
      private final int y;
      private final int w;
      private final int h;
      private final Runnable action;
      private float hoverAnim = 0.0F;

      private SettingChip(String label, int x, int y, int w, int h, Runnable action) {
         this.label = label;
         this.x = x;
         this.y = y;
         this.w = w;
         this.h = h;
         this.action = action;
      }

      void tick() {
         this.hoverAnim = ModernClickGUI.lerp(this.hoverAnim, 0.0F, 0.08F);
      }

      boolean mouseClicked(double mx, double my, int button) {
         return button == 0 && mx >= (double)this.x && mx <= (double)(this.x + this.w) && my >= (double)this.y && my <= (double)(this.y + this.h);
      }

      void render(class_332 context, int mouseX, int mouseY, Theme theme) {
         boolean hovered = mouseX >= this.x && mouseX <= this.x + this.w && mouseY >= this.y && mouseY <= this.y + this.h;
         this.hoverAnim = ModernClickGUI.lerp(this.hoverAnim, hovered ? 1.0F : 0.0F, 0.12F);
         ModernClickGUI.this.drawGlow(context, this.x, this.y, this.w, this.h, 8, hovered ? theme.glowStrong : theme.borderSoft, hovered ? 5 : 2);
         ModernClickGUI.this.drawPanel(context, this.x, this.y, this.w, this.h, 8, theme.panel);
         ModernClickGUI.this.drawBorder(context, this.x, this.y, this.w, this.h, 8, hovered ? theme.accentStrong : theme.borderSoft);
         context.method_51439(ModernClickGUI.this.field_22793, class_2561.method_43470(this.label), this.x + 10, this.y + 10, theme.textStrong, true);
      }
   }

   private final class ModuleCard {
      private final Module module;
      private int x;
      private int y;
      private int w;
      private int h;
      private boolean visible = true;
      private float hoverAnim = 0.0F;

      private ModuleCard(Module module, int x, int y, int w, int h) {
         this.module = module;
         this.x = x;
         this.y = y;
         this.w = w;
         this.h = h;
      }

      void render(class_332 context, int mouseX, int mouseY, Theme theme, boolean dragModeNow) {
         boolean hovered = this.contains((double)mouseX, (double)mouseY);
         String key = ModernClickGUI.moduleAnimKey(this.module);
         long now = System.nanoTime();
         long hoverLast = (Long)ModernClickGUI.this.moduleHoverLastNs.getOrDefault(key, 0L);
         if (hoverLast == 0L) {
            hoverLast = now;
         }

         float hoverDt = Math.min((float)(now - hoverLast) / 1.0E9F, 0.05F);
         ModernClickGUI.this.moduleHoverLastNs.put(key, now);
         float hoverTarget = hovered ? 1.0F : 0.0F;
         this.hoverAnim = ModernClickGUI.expLerp((Float)ModernClickGUI.this.moduleHoverMap.getOrDefault(key, 0.0F), hoverTarget, 17.0F, hoverDt);
         ModernClickGUI.this.moduleHoverMap.put(key, this.hoverAnim);
         long enableLast = (Long)ModernClickGUI.this.moduleEnableLastNs.getOrDefault(key, 0L);
         if (enableLast == 0L) {
            enableLast = now;
         }

         float enableDt = Math.min((float)(now - enableLast) / 1.0E9F, 0.05F);
         ModernClickGUI.this.moduleEnableLastNs.put(key, now);
         float enableAnim = ModernClickGUI.expLerp((Float)ModernClickGUI.this.moduleEnableMap.getOrDefault(key, this.module.isEnabled() ? 1.0F : 0.0F), this.module.isEnabled() ? 1.0F : 0.0F, 9.5F, enableDt);
         ModernClickGUI.this.moduleEnableMap.put(key, enableAnim);
         int fill = ModernClickGUI.this.lerpColor(theme.panel, theme.panel2, enableAnim);
         int border = ModernClickGUI.this.lerpColor(theme.borderSoft, theme.accentStrong, enableAnim);
         int glow = ModernClickGUI.this.lerpColor(theme.borderSoft, theme.glowStrong, enableAnim);
         if (hovered) {
            glow = ModernClickGUI.this.lerpColor(glow, theme.glowStrong, 0.35F);
         }

         int radius = ModernClickGUI.this.roundedMode ? Math.max(20, this.h / 3) : 12;
         boolean hiFi = ModernClickGUI.this.useHiFiEffect(this.x, this.y, this.w, this.h, ModernClickGUI.this.contentX + 12, ModernClickGUI.this.contentY + ModernClickGUI.this.topAreaH + 10, ModernClickGUI.this.moduleAreaRight() - 8, ModernClickGUI.this.contentY + ModernClickGUI.this.contentH - 12, hovered || this.module.isEnabled());
         if (hiFi) {
            ModernClickGUI.this.drawGlow(context, this.x, this.y, this.w, this.h, radius, glow, this.module.isEnabled() ? 2 : 1);
         }

         int cardFill = ModernClickGUI.applyAlpha(fill, ModernClickGUI.this.moduleOpacity);
         int cardBorder = ModernClickGUI.applyAlpha(border, ModernClickGUI.this.moduleOpacity);
         RoundedRectRenderer.outline(context, (float)this.x, (float)this.y, (float)this.w, (float)this.h, (float)radius, 1.0F, cardBorder, cardFill);
         class_327 tr = ModernClickGUI.this.field_22793;
         context.method_51439(tr, class_2561.method_43470(this.module.getName()), this.x + 12, this.y + 12, ModernClickGUI.this.lerpColor(theme.textDim, theme.textStrong, enableAnim), true);
         if (ModernClickGUI.this.showDescriptions) {
            context.method_51439(tr, class_2561.method_43470(ModernClickGUI.pretty(this.module.getCategory())), this.x + 12, this.y + 28, ModernClickGUI.this.lerpColor(theme.textDim, theme.textStrong, enableAnim * 0.65F), true);
         }

         int stripY = this.y + this.h - 28;
         int stripFill = ModernClickGUI.applyAlpha(ModernClickGUI.this.lerpColor(theme.panel2, theme.accentStrong, enableAnim), ModernClickGUI.this.moduleOpacity);
         int stripBorder = ModernClickGUI.applyAlpha(ModernClickGUI.this.lerpColor(theme.borderSoft, theme.accentStrong, enableAnim), ModernClickGUI.this.moduleOpacity);
         RoundedRectRenderer.outline(context, (float)(this.x + 10), (float)stripY, (float)(this.w - 20), 18.0F, 9.0F, 1.0F, stripBorder, stripFill);
         int statusColor = ModernClickGUI.this.lerpColor(theme.textDim, -1, enableAnim);
         context.method_51439(tr, class_2561.method_43470(this.module.isEnabled() ? "Enabled" : "Disabled"), this.x + 28, stripY + 5, statusColor, true);
         int dx = this.x + this.w - 24;
         int dy = this.y + 10;
         int c = hovered ? theme.accentStrong : ModernClickGUI.this.lerpColor(theme.textDim, theme.accentStrong, enableAnim);
         ModernClickGUI.this.drawConfigDiamond(context, dx, dy, c);
         if (dragModeNow) {
            RoundedRectRenderer.outline(context, (float)(this.x + this.w - 72), (float)(this.y + 10), 52.0F, 18.0F, 9.0F, 1.0F, theme.borderSoft, theme.panel2);
            context.method_51439(tr, class_2561.method_43470("Drag"), this.x + this.w - 61, this.y + 15, theme.textDim, true);
         }

      }

      boolean contains(double mx, double my) {
         return mx >= (double)this.x && mx <= (double)(this.x + this.w) && my >= (double)this.y && my <= (double)(this.y + this.h);
      }

      boolean containsConfig(double mx, double my) {
         return mx >= (double)(this.x + this.w - 28) && mx <= (double)(this.x + this.w - 8) && my >= (double)(this.y + 8) && my <= (double)(this.y + 24);
      }
   }
}
