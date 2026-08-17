package com.example.client.ui.config;

import com.example.client.module.Module;
import com.example.client.setting.BooleanSetting;
import com.example.client.setting.ModeSetting;
import com.example.client.setting.NumberSetting;
import com.example.client.setting.Setting;
import com.example.client.ui.ModernClickGUI;
import com.example.client.ui.render.RoundedRectRenderer;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import net.minecraft.class_11905;
import net.minecraft.class_11908;
import net.minecraft.class_11909;
import net.minecraft.class_2561;
import net.minecraft.class_310;
import net.minecraft.class_332;
import net.minecraft.class_437;

public class ModuleConfigScreen extends class_437 {
   private static final int PANEL_BG = -15986665;
   private static final int ROW_BG = -15328476;
   private static final int ROW_BG_HOVER = -14998992;
   private static final int BORDER = -12958118;
   private static final int BORDER_SOFT = -14801615;
   private static final int TEXT = -1;
   private static final int TEXT_DIM = -6511697;
   private static final int ACCENT = -7643914;
   private static final int SLIDER_TRACK = -15789027;
   private static final int SLIDER_FILL = -7643914;
   private static final int KNOB = -460036;
   private static final int POPUP_BG = -200667628;
   private static final class_2561 BACK_TEXT = class_2561.method_43470("← Back");
   private static final class_2561 COLOR_TITLE = class_2561.method_43470("Color");
   private static final class_2561 R_TEXT = class_2561.method_43470("R");
   private static final class_2561 G_TEXT = class_2561.method_43470("G");
   private static final class_2561 B_TEXT = class_2561.method_43470("B");
   private final ModernClickGUI parent;
   private final Module module;
   private final List<Row> rows = new ArrayList();
   private final ScrollState scroll = new ScrollState();
   private int panelX;
   private int panelY;
   private int panelW;
   private int panelH;
   private int contentX;
   private int contentY;
   private int contentW;
   private int contentH;
   private float openAnim = 0.0F;
   private long lastTickNs = 0L;
   private int mouseX;
   private int mouseY;
   private boolean draggingScrollbar = false;
   private float scrollbarDragStartScroll = 0.0F;
   private int scrollbarDragStartMouseY = 0;
   private OverlayPopup activePopup;
   private class_2561 titleText = class_2561.method_43470("Module Config");
   private int moduleAccentColor = -7643914;
   private boolean moduleAccentColorLoaded = false;

   public ModuleConfigScreen(ModernClickGUI parent, Module module) {
      super(class_2561.method_43470(module == null ? "Module Config" : safeName(module.getName(), "Module Config")));
      this.parent = parent;
      this.module = module;
      if (module != null && module.getName() != null) {
         this.titleText = class_2561.method_43470(module.getName());
      }

   }

   protected void method_25426() {
      this.rebuildRows();
      this.layout();
      this.openAnim = 0.0F;
      this.lastTickNs = 0L;
      this.scroll.reset();
      this.activePopup = null;
   }

   protected void method_48640() {
      this.layout();
   }

   public boolean method_25422() {
      return this.activePopup == null && !this.hasAnyOpenOverlay();
   }

   public void method_25419() {
      class_310 client = this.field_22787;
      if (client != null) {
         client.method_1507(this.parent);
      }

   }

   public boolean method_25421() {
      return false;
   }

   public void method_25393() {
      long now = System.nanoTime();
      if (this.lastTickNs == 0L) {
         this.lastTickNs = now;
      }

      float dt = Math.min((float)(now - this.lastTickNs) * 1.0E-9F, 0.05F);
      this.lastTickNs = now;
      this.openAnim = lerp(this.openAnim, 1.0F, 0.14F + dt * 0.25F);
      this.scroll.tick(dt);

      for(Row row : this.rows) {
         row.tick(dt);
      }

      if (this.activePopup != null) {
         this.activePopup.tick(dt);
         if (this.activePopup.isClosed()) {
            this.activePopup = null;
         }
      }

   }

   public boolean method_25404(class_11908 input) {
      if (this.activePopup != null && this.activePopup.keyPressed(input)) {
         return true;
      } else if (input.method_74231()) {
         if (this.activePopup != null) {
            this.activePopup.close();
            this.activePopup = null;
            return true;
         } else if (this.hasAnyOpenOverlay()) {
            this.closeAllOverlays();
            return true;
         } else {
            this.method_25419();
            return true;
         }
      } else {
         for(Row row : this.rows) {
            if (row.keyPressed(input)) {
               return true;
            }
         }

         return super.method_25404(input);
      }
   }

   public boolean method_25400(class_11905 input) {
      if (this.activePopup != null && this.activePopup.charTyped(input)) {
         return true;
      } else {
         for(Row row : this.rows) {
            if (row.charTyped(input)) {
               return true;
            }
         }

         return super.method_25400(input);
      }
   }

   public boolean method_25401(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
      if (this.activePopup != null && this.activePopup.mouseScrolled(mouseX, mouseY, verticalAmount)) {
         return true;
      } else {
         if (this.hasAnyOpenOverlay()) {
            for(Row row : this.rows) {
               if (row.hasOpenOverlay() && row.mouseScrolled(mouseX, mouseY, verticalAmount)) {
                  return true;
               }
            }

            if (isInside(mouseX, mouseY, this.panelX, this.panelY, this.panelW, this.panelH)) {
               return true;
            }
         }

         return this.scroll.mouseScrolled(mouseX, mouseY, verticalAmount);
      }
   }

   public boolean method_25402(class_11909 click, boolean doubled) {
      double mx = click.comp_4798();
      double my = click.comp_4799();
      int button = click.method_74245();
      if (button == 0 && isInside(mx, my, this.panelX + 14, this.panelY + 12, 100, 28)) {
         this.method_25419();
         return true;
      } else {
         if (this.activePopup != null) {
            if (this.activePopup.mouseClicked(mx, my, button)) {
               return true;
            }

            if (button == 0 && !this.activePopup.isInside(mx, my)) {
               this.activePopup.close();
               this.activePopup = null;
               return true;
            }
         }

         if (this.hasAnyOpenOverlay()) {
            for(Row row : this.rows) {
               if (row.hasOpenOverlay() && row.mouseClicked(mx, my, button)) {
                  return true;
               }
            }

            if (isInside(mx, my, this.panelX, this.panelY, this.panelW, this.panelH)) {
               return true;
            } else {
               return super.method_25402(click, doubled);
            }
         } else if (button == 0 && this.scroll.isOverScrollbar(mx, my)) {
            this.draggingScrollbar = true;
            this.scrollbarDragStartMouseY = (int)my;
            this.scrollbarDragStartScroll = this.scroll.targetScroll;
            return true;
         } else {
            return this.scroll.mouseClicked(mx, my, button) ? true : super.method_25402(click, doubled);
         }
      }
   }

   public boolean method_25403(class_11909 click, double deltaX, double deltaY) {
      double mx = click.comp_4798();
      double my = click.comp_4799();
      int button = click.method_74245();
      if (this.draggingScrollbar) {
         int dy = (int)my - this.scrollbarDragStartMouseY;
         float track = Math.max(1.0F, (float)(this.scroll.viewportH - this.scroll.getThumbHeight()));
         float target = this.scrollbarDragStartScroll + (float)dy / track * Math.max(1.0F, this.scroll.maxScroll);
         this.scroll.targetScroll = clamp(target, 0.0F, this.scroll.maxScroll);
         return true;
      } else if (this.activePopup != null && this.activePopup.mouseDragged(mx, my, button)) {
         return true;
      } else if (this.hasAnyOpenOverlay()) {
         for(Row row : this.rows) {
            if (row.hasOpenOverlay() && row.mouseDragged(mx, my, button)) {
               return true;
            }
         }

         if (isInside(mx, my, this.panelX, this.panelY, this.panelW, this.panelH)) {
            return true;
         } else {
            return super.method_25403(click, deltaX, deltaY);
         }
      } else {
         return this.scroll.mouseDragged(mx, my, button);
      }
   }

   public boolean method_25406(class_11909 click) {
      this.draggingScrollbar = false;
      double mx = click.comp_4798();
      double my = click.comp_4799();
      int button = click.method_74245();
      if (this.activePopup != null && this.activePopup.mouseReleased(mx, my, button)) {
         return true;
      } else if (this.hasAnyOpenOverlay()) {
         for(Row row : this.rows) {
            if (row.hasOpenOverlay()) {
               row.mouseReleased(mx, my, button);
            }
         }

         return true;
      } else {
         this.scroll.mouseReleased(mx, my, button);
         return super.method_25406(click);
      }
   }

   public void method_25394(class_332 context, int mouseX, int mouseY, float delta) {
      this.mouseX = mouseX;
      this.mouseY = mouseY;
      this.layout();
      float anim = easeOutCubic(this.openAnim);
      float lift = (1.0F - anim) * 18.0F;
      context.method_51448().pushMatrix();
      context.method_51448().translate(0.0F, lift);
      this.renderBackdrop(context);
      this.renderPanel(context, mouseX, mouseY);
      this.scroll.render(context, mouseX, mouseY);
      if (this.activePopup != null) {
         this.activePopup.render(context, mouseX, mouseY, 0, 0, 0);
      }

      context.method_51448().popMatrix();
      super.method_25394(context, mouseX, mouseY, delta);
   }

   private void rebuildRows() {
      this.rows.clear();
      if (this.module == null) {
         this.rows.add(new InfoRow("No module selected"));
      } else {
         this.rows.add(new SectionRow("General"));
         List<Setting> settings = this.module.getSettings();
         if (settings != null) {
            for(Setting setting : settings) {
               if (setting != null) {
                  this.rows.add(this.createRow(setting));
               }
            }
         }

         if (this.rows.size() == 1) {
            this.rows.add(new InfoRow("No settings available"));
         }

      }
   }

   private Row createRow(Setting setting) {
      if (setting instanceof BooleanSetting bool) {
         return new ToggleRow(bool);
      } else if (setting instanceof NumberSetting number) {
         return new SliderRow(number);
      } else if (setting instanceof ModeSetting mode) {
         return new ModeRow(mode);
      } else {
         String name = safeName(setting.getName(), "").toLowerCase(Locale.ROOT);
         if (looksLikeColor(name, setting)) {
            return new ColorRow(setting);
         } else if (looksLikeKeybind(name, setting)) {
            return new KeybindRow(setting);
         } else {
            return (Row)(looksLikeSection(name) ? new SectionRow(safeName(setting.getName(), "Section")) : new FallbackRow(setting));
         }
      }
   }

   private static boolean looksLikeColor(String name, Setting setting) {
      if (!name.contains("color") && !name.contains("colour") && !name.contains("rgb") && !name.contains("rainbow") && !name.contains("background") && !name.contains("outline") && !name.contains("glow") && !name.contains("box") && !name.contains("border") && !name.contains("shadow") && !name.contains("fill") && !name.contains("plate")) {
         String simple = setting.getClass().getSimpleName().toLowerCase(Locale.ROOT);
         return simple.contains("color") || simple.contains("rgb") || simple.contains("tint");
      } else {
         return true;
      }
   }

   private static boolean looksLikeKeybind(String name, Setting setting) {
      return !name.contains("key") && !name.contains("bind") ? setting.getClass().getSimpleName().toLowerCase(Locale.ROOT).contains("key") : true;
   }

   private static boolean looksLikeSection(String name) {
      return name.contains("section") || name.contains("group") || name.contains("category") || name.contains("divider");
   }

   private void layout() {
      int targetW = 360;
      int targetH = 420;
      this.panelW = Math.min(targetW, Math.max(320, this.field_22789 - 40));
      this.panelH = Math.min(targetH, Math.max(340, this.field_22790 - 40));
      this.panelX = (this.field_22789 - this.panelW) / 2;
      this.panelY = (this.field_22790 - this.panelH) / 2;
      this.contentX = this.panelX + 16;
      this.contentY = this.panelY + 54;
      this.contentW = this.panelW - 32;
      this.contentH = this.panelH - 66;
      this.scroll.layout(this.contentX, this.contentY, this.contentW, this.contentH, this.rows);
   }

   private void renderBackdrop(class_332 context) {
   }

   private void renderPanel(class_332 context, int mouseX, int mouseY) {
      RoundedRectRenderer.outline(context, (float)this.panelX, (float)this.panelY, (float)this.panelW, (float)this.panelH, 20.0F, 1.0F, -12958118, -15986665);
      boolean backHover = isInside((double)mouseX, (double)mouseY, this.panelX + 12, this.panelY + 12, 86, 28);
      RoundedRectRenderer.outline(context, (float)(this.panelX + 12), (float)(this.panelY + 12), 86.0F, 28.0F, 14.0F, 1.0F, backHover ? -6325249 : -7643914, backHover ? -15656921 : -15986665);
      context.method_51439(this.field_22793, BACK_TEXT, this.panelX + 34, this.panelY + 21, backHover ? -1 : -1381137, false);
      int titleW = this.field_22793.method_27525(this.titleText);
      context.method_51439(this.field_22793, this.titleText, this.panelX + (this.panelW - titleW) / 2, this.panelY + 18, -1, false);
      context.method_25294(this.panelX + 18, this.panelY + 50, this.panelX + this.panelW - 18, this.panelY + 51, 574244442);
   }

   private boolean hasAnyOpenOverlay() {
      for(Row row : this.rows) {
         if (row.hasOpenOverlay()) {
            return true;
         }
      }

      return false;
   }

   private void closeAllOverlays() {
      for(Row row : this.rows) {
         row.closeOverlay();
      }

   }

   private int clampPopupX(int preferredX, int popupW) {
      int minX = this.panelX + 8;
      int maxX = this.panelX + this.panelW - popupW - 8;
      return Math.max(minX, Math.min(preferredX, maxX));
   }

   private int clampPopupY(int preferredY, int popupH) {
      int minY = this.panelY + 54;
      int maxY = this.panelY + this.panelH - popupH - 10;
      return Math.max(minY, Math.min(preferredY, maxY));
   }

   static boolean isInside(double mx, double my, int x, int y, int w, int h) {
      return mx >= (double)x && mx <= (double)(x + w) && my >= (double)y && my <= (double)(y + h);
   }

   private static float clamp(float value, float min, float max) {
      return Math.max(min, Math.min(max, value));
   }

   private static float lerp(float current, float target, float speed) {
      return current + (target - current) * speed;
   }

   private static float easeOutCubic(float t) {
      t = clamp(t, 0.0F, 1.0F);
      float inv = 1.0F - t;
      return 1.0F - inv * inv * inv;
   }

   private static int blendColor(int from, int to, float t) {
      t = Math.max(0.0F, Math.min(1.0F, t));
      int fa = from >>> 24 & 255;
      int fr = from >>> 16 & 255;
      int fg = from >>> 8 & 255;
      int fb = from & 255;
      int ta = to >>> 24 & 255;
      int tr = to >>> 16 & 255;
      int tg = to >>> 8 & 255;
      int tb = to & 255;
      int a = (int)((float)fa + (float)(ta - fa) * t);
      int r = (int)((float)fr + (float)(tr - fr) * t);
      int g = (int)((float)fg + (float)(tg - fg) * t);
      int b = (int)((float)fb + (float)(tb - fb) * t);
      return a << 24 | r << 16 | g << 8 | b;
   }

   private static String safeName(String value, String fallback) {
      return value != null && !value.isEmpty() ? value : fallback;
   }

   private static String formatNumber(double value, double step) {
      if (!(Math.abs(value - Math.rint(value)) < 1.0E-5) && !(step >= (double)1.0F)) {
         double abs = Math.abs(value);
         if (abs >= (double)1000.0F) {
            return String.format(Locale.ROOT, "%.0f", value);
         } else if (abs >= (double)100.0F) {
            return trimZeros(String.format(Locale.ROOT, "%.1f", value));
         } else {
            return abs >= (double)10.0F ? trimZeros(String.format(Locale.ROOT, "%.2f", value)) : trimZeros(String.format(Locale.ROOT, "%.3f", value));
         }
      } else {
         return Integer.toString((int)Math.round(value));
      }
   }

   private static String trimZeros(String value) {
      int end;
      for(end = value.length(); end > 0 && value.charAt(end - 1) == '0'; --end) {
      }

      if (end > 0 && value.charAt(end - 1) == '.') {
         --end;
      }

      return value.substring(0, end);
   }

   private static void writeColor(Setting setting, int argb) {
      if (setting != null) {
         if (!invokeIntSetter(setting, argb, "setColor", "setValue", "setArgb", "setARGB", "setRgb", "setRGB", "setHudColor", "setAccentColor", "setPrimaryColor", "setModuleColor")) {
            if (!writeIntField(setting, argb, "color", "value", "argb", "rgb", "packedColor", "packedColorValue", "hex", "tint", "hudColor", "accentColor", "primaryColor", "moduleColor")) {
               ;
            }
         }
      }
   }

   private static int readColor(Setting setting, int fallback) {
      if (setting == null) {
         return fallback;
      } else {
         for(String name : new String[]{"getColor", "getValue", "getArgb", "getARGB", "getRgb", "getRGB", "getHudColor", "getAccentColor", "getPrimaryColor", "getModuleColor"}) {
            try {
               Method m = setting.getClass().getMethod(name);
               Object v = m.invoke(setting);
               if (v instanceof Number) {
                  Number n = (Number)v;
                  return n.intValue();
               }
            } catch (Throwable var10) {
            }
         }

         try {
            Field f = findField(setting.getClass(), "color", "value", "argb", "rgb", "packedColor", "packedColorValue", "hex", "tint", "hudColor", "accentColor", "primaryColor", "moduleColor");
            if (f != null) {
               f.setAccessible(true);
               Object v = f.get(setting);
               if (v instanceof Number) {
                  Number n = (Number)v;
                  return n.intValue();
               }
            }
         } catch (Throwable var9) {
         }

         return fallback;
      }
   }

   private static String readKeyText(Setting setting) {
      try {
         Method m = setting.getClass().getMethod("getKeyCode");
         Object v = m.invoke(setting);
         if (v instanceof Number n) {
            return "Key " + n.intValue();
         }
      } catch (Throwable var6) {
      }

      try {
         Method m = setting.getClass().getMethod("getKey");
         Object v = m.invoke(setting);
         if (v instanceof Number n) {
            return "Key " + n.intValue();
         }
      } catch (Throwable var5) {
      }

      try {
         Method m = setting.getClass().getMethod("getValue");
         Object v = m.invoke(setting);
         if (v instanceof Number n) {
            return "Key " + n.intValue();
         }
      } catch (Throwable var4) {
      }

      return "Bind";
   }

   private static void writeKey(Setting setting, int keyCode) {
      try {
         Method m = setting.getClass().getMethod("setKeyCode", Integer.TYPE);
         m.invoke(setting, keyCode);
      } catch (Throwable var6) {
         try {
            Method m = setting.getClass().getMethod("setKey", Integer.TYPE);
            m.invoke(setting, keyCode);
         } catch (Throwable var5) {
            try {
               Method m = setting.getClass().getMethod("setValue", Integer.TYPE);
               m.invoke(setting, keyCode);
            } catch (Throwable var4) {
               try {
                  Field f = findField(setting.getClass(), "keyCode", "key", "bind");
                  if (f != null) {
                     f.setAccessible(true);
                     f.set(setting, keyCode);
                  }
               } catch (Throwable var3) {
               }

            }
         }
      }
   }

   private static double readNumberMin(NumberSetting setting, double fallback) {
      try {
         Method m = setting.getClass().getMethod("getMin");
         Object v = m.invoke(setting);
         if (v instanceof Number n) {
            return n.doubleValue();
         }
      } catch (Throwable var7) {
      }

      try {
         Field f = findField(setting.getClass(), "min", "minimum");
         if (f != null) {
            f.setAccessible(true);
            Object v = f.get(setting);
            if (v instanceof Number) {
               Number n = (Number)v;
               return n.doubleValue();
            }
         }
      } catch (Throwable var6) {
      }

      return fallback;
   }

   private static double readNumberMax(NumberSetting setting, double fallback) {
      try {
         Method m = setting.getClass().getMethod("getMax");
         Object v = m.invoke(setting);
         if (v instanceof Number n) {
            return n.doubleValue();
         }
      } catch (Throwable var7) {
      }

      try {
         Field f = findField(setting.getClass(), "max", "maximum");
         if (f != null) {
            f.setAccessible(true);
            Object v = f.get(setting);
            if (v instanceof Number) {
               Number n = (Number)v;
               return n.doubleValue();
            }
         }
      } catch (Throwable var6) {
      }

      return fallback;
   }

   private static double readNumberStep(NumberSetting setting, double fallback) {
      try {
         Method m = setting.getClass().getMethod("getStep");
         Object v = m.invoke(setting);
         if (v instanceof Number n) {
            return n.doubleValue();
         }
      } catch (Throwable var7) {
      }

      try {
         Field f = findField(setting.getClass(), "step", "increment", "inc");
         if (f != null) {
            f.setAccessible(true);
            Object v = f.get(setting);
            if (v instanceof Number) {
               Number n = (Number)v;
               return n.doubleValue();
            }
         }
      } catch (Throwable var6) {
      }

      return fallback;
   }

   private static String readModeValue(ModeSetting mode) {
      try {
         Method m = mode.getClass().getMethod("getMode");
         return String.valueOf(m.invoke(mode));
      } catch (Throwable var3) {
         try {
            Method m = mode.getClass().getMethod("getValue");
            return String.valueOf(m.invoke(mode));
         } catch (Throwable var2) {
            return "Mode";
         }
      }
   }

   private static String[] readModeOptions(ModeSetting mode) {
      try {
         Method m = mode.getClass().getMethod("getModes");
         Object v = m.invoke(mode);
         if (v instanceof List<?> list) {
            if (!list.isEmpty()) {
               String[] out = new String[list.size()];

               for(int i = 0; i < list.size(); ++i) {
                  out[i] = String.valueOf(list.get(i));
               }

               return out;
            }
         }

         if (v instanceof String[] arr) {
            if (arr.length > 0) {
               return arr;
            }
         }
      } catch (Throwable var7) {
      }

      try {
         Method m = mode.getClass().getMethod("getOptions");
         Object v = m.invoke(mode);
         if (v instanceof List<?> list) {
            if (!list.isEmpty()) {
               String[] out = new String[list.size()];

               for(int i = 0; i < list.size(); ++i) {
                  out[i] = String.valueOf(list.get(i));
               }

               return out;
            }
         }
      } catch (Throwable var6) {
      }

      return new String[]{readModeValue(mode)};
   }

   private static int getModeIndex(ModeSetting mode) {
      try {
         Method m = mode.getClass().getMethod("getIndex");
         Object v = m.invoke(mode);
         if (v instanceof Number n) {
            return n.intValue();
         }
      } catch (Throwable var5) {
      }

      try {
         Field f = findField(mode.getClass(), "index", "modeIndex", "selected");
         if (f != null) {
            f.setAccessible(true);
            Object v = f.get(mode);
            if (v instanceof Number) {
               Number n = (Number)v;
               return n.intValue();
            }
         }
      } catch (Throwable var4) {
      }

      return 0;
   }

   private static void setModeByIndex(ModeSetting mode, int index) {
      try {
         Method m = mode.getClass().getMethod("setMode", Integer.TYPE);
         m.invoke(mode, index);
      } catch (Throwable var5) {
         try {
            Method m = mode.getClass().getMethod("setIndex", Integer.TYPE);
            m.invoke(mode, index);
         } catch (Throwable var4) {
            try {
               Field f = findField(mode.getClass(), "index", "modeIndex", "selected");
               if (f != null) {
                  f.setAccessible(true);
                  f.set(mode, index);
               }
            } catch (Throwable var3) {
            }

         }
      }
   }

   private static void cycleMode(ModeSetting mode, int delta) {
      String[] options = readModeOptions(mode);
      if (options.length > 1) {
         int index = getModeIndex(mode);
         index = Math.floorMod(index + delta, options.length);
         setModeByIndex(mode, index);
      }
   }

   private static boolean invokeIntSetter(Object target, int value, String... methodNames) {
      if (target == null) {
         return false;
      } else {
         Class<?> type = target.getClass();

         for(String methodName : methodNames) {
            Class<?> cursor = type;

            while(true) {
               if (cursor != null && cursor != Object.class) {
                  for(Method method : cursor.getDeclaredMethods()) {
                     if (method.getName().equals(methodName) && method.getParameterCount() == 1) {
                        Class<?> param = method.getParameterTypes()[0];

                        try {
                           method.setAccessible(true);
                           if (param != Integer.TYPE && param != Integer.class) {
                              if (param != Long.TYPE && param != Long.class) {
                                 if (param != Float.TYPE && param != Float.class) {
                                    if (param != Double.TYPE && param != Double.class) {
                                       if (param != Short.TYPE && param != Short.class) {
                                          if (param != Byte.TYPE && param != Byte.class) {
                                             continue;
                                          }

                                          method.invoke(target, (byte)value);
                                          return true;
                                       }

                                       method.invoke(target, (short)value);
                                       return true;
                                    }

                                    method.invoke(target, (double)value);
                                    return true;
                                 }

                                 method.invoke(target, (float)value);
                                 return true;
                              }

                              method.invoke(target, (long)value);
                              return true;
                           }

                           method.invoke(target, value);
                           return true;
                        } catch (Throwable var15) {
                        }
                     }
                  }

                  cursor = cursor.getSuperclass();
                  continue;
               }
            }
         }

         return false;
      }
   }

   private static boolean writeIntField(Object target, int value, String... names) {
      if (target == null) {
         return false;
      } else {
         Field field = findField(target.getClass(), names);
         if (field == null) {
            return false;
         } else {
            try {
               field.setAccessible(true);
               Class<?> type = field.getType();
               if (type != Integer.TYPE && type != Integer.class) {
                  if (type != Long.TYPE && type != Long.class) {
                     if (type != Float.TYPE && type != Float.class) {
                        if (type != Double.TYPE && type != Double.class) {
                           if (type != Short.TYPE && type != Short.class) {
                              if (type != Byte.TYPE && type != Byte.class) {
                                 return false;
                              }

                              field.set(target, (byte)value);
                           } else {
                              field.set(target, (short)value);
                           }
                        } else {
                           field.set(target, (double)value);
                        }
                     } else {
                        field.set(target, (float)value);
                     }
                  } else {
                     field.set(target, (long)value);
                  }
               } else {
                  field.set(target, value);
               }

               return true;
            } catch (Throwable var5) {
               return false;
            }
         }
      }
   }

   private static Field findField(Class<?> type, String... names) {
      for(Class<?> cursor = type; cursor != null && cursor != Object.class; cursor = cursor.getSuperclass()) {
         for(String name : names) {
            try {
               return cursor.getDeclaredField(name);
            } catch (NoSuchFieldException var8) {
            }
         }
      }

      return null;
   }

   private int getModuleAccentColor() {
      if (!this.moduleAccentColorLoaded) {
         this.moduleAccentColor = readModuleColor(this.module, this.moduleAccentColor);
         this.moduleAccentColorLoaded = true;
      }

      return this.moduleAccentColor;
   }

   private void setModuleAccentColor(int argb) {
      this.moduleAccentColor = argb;
      writeModuleColor(this.module, argb);
      persistModuleColor(this.module);
   }

   private static int readModuleColor(Object target, int fallback) {
      if (target == null) {
         return fallback;
      } else {
         for(String name : new String[]{"getColor", "getHudColor", "getAccentColor", "getPrimaryColor", "getModuleColor", "getValue", "getArgb", "getARGB", "getRgb", "getRGB"}) {
            try {
               Method m = target.getClass().getMethod(name);
               Object v = m.invoke(target);
               if (v instanceof Number) {
                  Number n = (Number)v;
                  return n.intValue();
               }
            } catch (Throwable var10) {
            }
         }

         try {
            Field f = findField(target.getClass(), "color", "hudColor", "accentColor", "primaryColor", "moduleColor", "value", "argb", "rgb", "packedColor");
            if (f != null) {
               f.setAccessible(true);
               Object v = f.get(target);
               if (v instanceof Number) {
                  Number n = (Number)v;
                  return n.intValue();
               }
            }
         } catch (Throwable var9) {
         }

         return fallback;
      }
   }

   private static void writeModuleColor(Object target, int argb) {
      if (target != null) {
         if (!invokeIntSetter(target, argb, "setColor", "setHudColor", "setAccentColor", "setPrimaryColor", "setModuleColor", "setValue", "setArgb", "setARGB", "setRgb", "setRGB")) {
            try {
               Field f = findField(target.getClass(), "color", "hudColor", "accentColor", "primaryColor", "moduleColor", "value", "argb", "rgb", "packedColor");
               if (f != null) {
                  f.setAccessible(true);
                  f.set(target, argb);
               }
            } catch (Throwable var3) {
            }

         }
      }
   }

   private static void persistModuleColor(Object target) {
      if (target != null) {
         String[] saverNames = new String[]{"saveConfig", "save", "markDirty", "requestSave", "persist", "writeConfig", "sync"};

         for(String name : saverNames) {
            try {
               Method m = target.getClass().getMethod(name);
               m.invoke(target);
               return;
            } catch (Throwable var7) {
            }
         }

      }
   }

   private static void drawRow(class_332 context, int x, int drawY, int w, int h, boolean hover) {
      RoundedRectRenderer.outline(context, (float)x, (float)drawY, (float)w, (float)h, 14.0F, 1.0F, hover ? -12958118 : -14801615, hover ? -14998992 : -15328476);
   }

   private static void drawGlow(class_332 context, int x, int y, int w, int h, int radius, int argb, int layers) {
      int a = argb >>> 24 & 255;
      int rgb = argb & 16777215;

      for(int i = layers; i >= 1; --i) {
         float pad = 1.25F * (float)i;
         int alpha = Math.max(0, Math.round((float)a * (0.1F / (float)i)));
         RoundedRectRenderer.outline(context, (float)x - pad, (float)y - pad, (float)w + pad * 2.0F, (float)h + pad * 2.0F, (float)radius + pad, 1.0F, alpha << 24 | rgb, 0);
      }

   }

   private static float clamp01(float value) {
      return Math.max(0.0F, Math.min(1.0F, value));
   }

   private static double clampDouble(double value, double min, double max) {
      return Math.max(min, Math.min(max, value));
   }

   private static int packColor(float r, float g, float b) {
      return -16777216 | (int)(clamp01(r) * 255.0F) << 16 | (int)(clamp01(g) * 255.0F) << 8 | (int)(clamp01(b) * 255.0F);
   }

   private static void persistColorChange(Setting setting) {
      for(String methodName : new String[]{"save", "saveConfig", "writeConfig", "markDirty", "setDirty", "onChange", "onChanged", "apply", "refresh", "update"}) {
         try {
            Method m = setting.getClass().getMethod(methodName);
            m.invoke(setting);
         } catch (Throwable var6) {
         }
      }

   }

   private static void persistChange(Object target) {
      if (target != null) {
         for(String methodName : new String[]{"save", "saveConfig", "writeConfig", "markDirty", "setDirty", "onChange", "onChanged", "apply", "refresh", "update"}) {
            try {
               Method m = target.getClass().getMethod(methodName);
               m.invoke(target);
               return;
            } catch (Throwable var6) {
            }
         }

      }
   }

   private abstract static class Row {
      protected int x;
      protected int y;
      protected int w;
      protected int h;
      protected int visibleY;
      protected float hover = 0.0F;

      abstract int getHeight();

      void layout(int x, int y, int w) {
         this.x = x;
         this.y = y;
         this.w = w;
         this.h = this.getHeight();
      }

      void tick(float dt) {
      }

      abstract void render(class_332 var1, int var2, int var3, int var4);

      void renderOverlay(class_332 context, int mouseX, int mouseY) {
      }

      boolean mouseClicked(double mouseX, double mouseY, int button) {
         return false;
      }

      boolean mouseDragged(double mouseX, double mouseY, int button) {
         return false;
      }

      boolean mouseScrolled(double mouseX, double mouseY, double verticalAmount) {
         return false;
      }

      void mouseReleased(double mouseX, double mouseY, int button) {
      }

      boolean keyPressed(class_11908 input) {
         return false;
      }

      boolean charTyped(class_11905 input) {
         return false;
      }

      boolean hasOpenOverlay() {
         return false;
      }

      void closeOverlay() {
      }

      protected final boolean hit(double mouseX, double mouseY, int drawY) {
         return ModuleConfigScreen.isInside(mouseX, mouseY, this.x, drawY, this.w, this.h);
      }
   }

   private final class SectionRow extends Row {
      private final class_2561 labelText;

      SectionRow(String title) {
         this.labelText = class_2561.method_43470(ModuleConfigScreen.safeName(title, "Section"));
      }

      int getHeight() {
         return 24;
      }

      void render(class_332 context, int mouseX, int mouseY, int drawY) {
         context.method_25294(this.x + 16, drawY + 12, this.x + this.w - 16, drawY + 13, 592338563);
         context.method_51439(ModuleConfigScreen.this.field_22793, this.labelText, this.x + 16, drawY + 2, -2630401, false);
      }
   }

   private final class InfoRow extends Row {
      private final class_2561 labelText;
      private final class_2561 hintText = class_2561.method_43470("No editor available");

      InfoRow(String title) {
         this.labelText = class_2561.method_43470(ModuleConfigScreen.safeName(title, "Info"));
      }

      int getHeight() {
         return 40;
      }

      void render(class_332 context, int mouseX, int mouseY, int drawY) {
         boolean hovered = this.hit((double)mouseX, (double)mouseY, drawY);
         ModuleConfigScreen.drawRow(context, this.x, drawY, this.w, this.h, hovered);
         context.method_51439(ModuleConfigScreen.this.field_22793, this.labelText, this.x + 16, drawY + 10, -1, false);
         context.method_51439(ModuleConfigScreen.this.field_22793, this.hintText, this.x + 16, drawY + 23, -6511697, false);
      }
   }

   private final class ToggleRow extends Row {
      private final BooleanSetting setting;
      private final class_2561 labelText;
      private float onAnim = 0.0F;

      ToggleRow(BooleanSetting setting) {
         this.setting = setting;
         this.labelText = class_2561.method_43470(ModuleConfigScreen.safeName(setting.getName(), "Toggle"));
      }

      int getHeight() {
         return 44;
      }

      void tick(float dt) {
         this.onAnim = ModuleConfigScreen.lerp(this.onAnim, this.setting.getValue() ? 1.0F : 0.0F, 0.22F);
      }

      void render(class_332 context, int mouseX, int mouseY, int drawY) {
         boolean hovered = this.hit((double)mouseX, (double)mouseY, drawY);
         ModuleConfigScreen.drawRow(context, this.x, drawY, this.w, this.h, hovered);
         context.method_51439(ModuleConfigScreen.this.field_22793, this.labelText, this.x + 12, drawY + 14, -1, false);
         int toggleW = 42;
         int toggleH = 20;
         int tx = this.x + this.w - toggleW - 12;
         int ty = drawY + 10;
         int bg = ModuleConfigScreen.blendColor(-15394011, -7643914, this.onAnim);
         RoundedRectRenderer.outline(context, (float)tx, (float)ty, (float)toggleW, (float)toggleH, 10.0F, 1.0F, -14077630, bg);
         int knobX = (int)((float)(tx + 2) + (float)(toggleW - 16) * this.onAnim);
         RoundedRectRenderer.outline(context, (float)knobX, (float)(ty + 2), 16.0F, 16.0F, 8.0F, 1.0F, -1, -460036);
      }

      boolean mouseClicked(double mouseX, double mouseY, int button) {
         if (button == 0 && this.hit(mouseX, mouseY, this.visibleY)) {
            this.setting.toggle();
            ModuleConfigScreen.persistChange(this.setting);
            return true;
         } else {
            return false;
         }
      }
   }

   private final class SliderRow extends Row {
      private final NumberSetting setting;
      private final class_2561 labelText;
      private class_2561 valueText = class_2561.method_43470("");
      private String valueCache = "";
      private boolean dragging = false;
      private double min;
      private double max;
      private double step;
      private float dragAnim = 0.0F;

      SliderRow(NumberSetting setting) {
         this.setting = setting;
         this.labelText = class_2561.method_43470(ModuleConfigScreen.safeName(setting.getName(), "Slider"));
         this.refreshBounds();
         this.syncValueText();
      }

      int getHeight() {
         return 54;
      }

      void tick(float dt) {
         this.dragAnim = ModuleConfigScreen.lerp(this.dragAnim, this.dragging ? 1.0F : 0.0F, 0.24F);
         if (this.dragging) {
            this.setFromMouse((double)ModuleConfigScreen.this.mouseX, (double)ModuleConfigScreen.this.mouseY);
         }

         this.syncValueText();
      }

      void render(class_332 context, int mouseX, int mouseY, int drawY) {
         this.refreshBounds();
         boolean hovered = this.hit((double)mouseX, (double)mouseY, drawY);
         this.hover = ModuleConfigScreen.lerp(this.hover, !hovered && !this.dragging ? 0.0F : 1.0F, 0.16F);
         ModuleConfigScreen.drawRow(context, this.x, drawY, this.w, this.h, hovered || this.dragging);
         context.method_51439(ModuleConfigScreen.this.field_22793, this.labelText, this.x + 16, drawY + 11, -1, false);
         String valueString = this.valueText.getString();
         int valueWidth = ModuleConfigScreen.this.field_22793.method_1727(valueString);
         context.method_51439(ModuleConfigScreen.this.field_22793, this.valueText, this.x + this.w - valueWidth - 16, drawY + 11, -6511697, false);
         int barX = this.x + 16;
         int barY = drawY + 32;
         int barW = this.w - 32;
         int barH = 6;
         RoundedRectRenderer.outline(context, (float)barX, (float)barY, (float)barW, (float)barH, 3.0F, 1.0F, -15789027, -15789027);
         double value = this.setting.getValue();
         double t = (double)ModuleConfigScreen.clamp((float)((value - this.min) / Math.max(1.0E-4, this.max - this.min)), 0.0F, 1.0F);
         int fillW = (int)Math.round((double)barW * t);
         if (fillW > 0) {
            int fillColor = ModuleConfigScreen.blendColor(-13090471, -7643914, Math.max(this.hover, this.dragAnim));
            RoundedRectRenderer.outline(context, (float)barX, (float)barY, (float)fillW, (float)barH, 3.0F, 1.0F, fillColor, fillColor);
         }

         int thumbX = barX + Math.max(0, fillW - 6);
         RoundedRectRenderer.outline(context, (float)thumbX, (float)(barY - 4), 12.0F, 14.0F, 7.0F, 1.0F, -1, -460036);
      }

      boolean mouseClicked(double mouseX, double mouseY, int button) {
         if (button == 0 && this.hit(mouseX, mouseY, this.visibleY)) {
            this.dragging = true;
            this.setFromMouse(mouseX, mouseY);
            return true;
         } else {
            return false;
         }
      }

      boolean mouseDragged(double mouseX, double mouseY, int button) {
         if (this.dragging && button == 0) {
            this.setFromMouse(mouseX, mouseY);
            return true;
         } else {
            return false;
         }
      }

      void mouseReleased(double mouseX, double mouseY, int button) {
         if (button == 0) {
            this.dragging = false;
         }

      }

      private void refreshBounds() {
         this.min = ModuleConfigScreen.readNumberMin(this.setting, (double)0.0F);
         this.max = ModuleConfigScreen.readNumberMax(this.setting, (double)100.0F);
         if (this.max <= this.min) {
            this.max = this.min + (double)1.0F;
         }

         this.step = Math.max(1.0E-4, ModuleConfigScreen.readNumberStep(this.setting, (double)1.0F));
      }

      private void syncValueText() {
         String newValue = ModuleConfigScreen.formatNumber(this.setting.getValue(), this.step);
         if (!Objects.equals(this.valueCache, newValue)) {
            this.valueCache = newValue;
            this.valueText = class_2561.method_43470(newValue);
         }

      }

      private void setFromMouse(double mouseX, double mouseY) {
         int barX = this.x + 16;
         int barW = this.w - 32;
         float pct = ModuleConfigScreen.clamp((float)((mouseX - (double)barX) / Math.max((double)1.0F, (double)barW)), 0.0F, 1.0F);
         double value = this.min + (this.max - this.min) * (double)pct;
         value = (double)Math.round(value / this.step) * this.step;
         value = (double)ModuleConfigScreen.clamp((float)value, (float)this.min, (float)this.max);
         this.setting.setValue(value);
         this.syncValueText();
      }
   }

   private final class ModeRow extends Row {
      private final ModeSetting setting;
      private final class_2561 labelText;
      private String[] options;
      private class_2561[] optionTexts = new class_2561[0];
      private class_2561 modeText;
      private boolean open = false;
      private float openAnim = 0.0F;

      ModeRow(ModeSetting setting) {
         this.setting = setting;
         this.labelText = class_2561.method_43470(ModuleConfigScreen.safeName(setting.getName(), "Mode"));
         this.reloadOptions();
         this.syncCurrentModeText();
      }

      int getHeight() {
         return 44;
      }

      void tick(float dt) {
         this.openAnim = ModuleConfigScreen.lerp(this.openAnim, this.open ? 1.0F : 0.0F, 0.22F);
      }

      void render(class_332 context, int mouseX, int mouseY, int drawY) {
         boolean hovered = this.hit((double)mouseX, (double)mouseY, drawY);
         this.hover = ModuleConfigScreen.lerp(this.hover, !hovered && !this.open ? 0.0F : 1.0F, 0.18F);
         ModuleConfigScreen.drawRow(context, this.x, drawY, this.w, this.h, hovered || this.open);
         context.method_51439(ModuleConfigScreen.this.field_22793, this.labelText, this.x + 16, drawY + 14, -1, false);
         String current = this.modeText.getString();
         int pillW = Math.min(220, Math.max(120, ModuleConfigScreen.this.field_22793.method_1727(current) + 40));
         int px = this.x + this.w - pillW - 16;
         int py = drawY + 10;
         int pillBorder = ModuleConfigScreen.blendColor(-13945791, -7643914, this.openAnim);
         int pillBg = ModuleConfigScreen.blendColor(-15789027, -15525336, this.openAnim);
         RoundedRectRenderer.outline(context, (float)px, (float)py, (float)pillW, 24.0F, 12.0F, 1.0F, pillBorder, pillBg);
         context.method_51439(ModuleConfigScreen.this.field_22793, this.modeText, px + 12, py + 8, -1, false);
         context.method_51439(ModuleConfigScreen.this.field_22793, this.open ? class_2561.method_43470("▴") : class_2561.method_43470("▾"), px + pillW - 18, py + 8, -6511697, false);
      }

      void renderOverlay(class_332 context, int mouseX, int mouseY) {
         if (this.openAnim > 0.01F) {
            int pillW = Math.min(220, Math.max(120, ModuleConfigScreen.this.field_22793.method_1727(this.modeText.getString()) + 40));
            int px = this.x + this.w - pillW - 16;
            this.renderDropdown(context, mouseX, mouseY, px, this.visibleY + this.h + 6, pillW, this.openAnim);
         }

      }

      boolean mouseClicked(double mouseX, double mouseY, int button) {
         if (button != 0) {
            return false;
         } else if (this.open) {
            int pillW = Math.min(220, Math.max(120, ModuleConfigScreen.this.field_22793.method_1727(this.modeText.getString()) + 40));
            int listX = this.x + this.w - pillW - 16;
            int listH = this.options.length * 24 + 8;
            int listY = ModuleConfigScreen.this.clampPopupY(this.visibleY + this.h + 6, listH);
            if (ModuleConfigScreen.isInside(mouseX, mouseY, listX, listY, pillW, listH)) {
               int index = (int)((mouseY - (double)listY - (double)4.0F) / (double)24.0F);
               if (index >= 0 && index < this.options.length) {
                  this.setModeIndex(index);
                  this.open = false;
                  return true;
               }
            }

            this.open = false;
            return true;
         } else if (this.hit(mouseX, mouseY, this.visibleY)) {
            this.open = true;
            return true;
         } else {
            return false;
         }
      }

      boolean mouseScrolled(double mouseX, double mouseY, double verticalAmount) {
         if (!this.hit(mouseX, mouseY, this.visibleY)) {
            return false;
         } else {
            if (verticalAmount > (double)0.0F) {
               ModuleConfigScreen.cycleMode(this.setting, -1);
            } else if (verticalAmount < (double)0.0F) {
               ModuleConfigScreen.cycleMode(this.setting, 1);
            }

            this.syncCurrentModeText();
            return true;
         }
      }

      private void renderDropdown(class_332 context, int mouseX, int mouseY, int x, int y, int width, float anim) {
         int height = this.options.length * 24 + 8;
         int popupY = ModuleConfigScreen.this.clampPopupY(y, height);
         int alpha = (int)(anim * 255.0F);
         int frameColor = alpha << 24 | 3819098;
         int fillColor = alpha << 24 | 658964;
         RoundedRectRenderer.outline(context, (float)x, (float)popupY, (float)width, (float)height, 14.0F, 1.0F, frameColor, fillColor);
         int itemY = popupY + 4;

         for(int i = 0; i < this.options.length; ++i) {
            boolean over = ModuleConfigScreen.isInside((double)mouseX, (double)mouseY, x + 4, itemY, width - 8, 22);
            int rowBorder = over ? -7643914 : -14735822;
            int rowFill = over ? -15656921 : -15986665;
            RoundedRectRenderer.outline(context, (float)(x + 4), (float)itemY, (float)(width - 8), 22.0F, 11.0F, 1.0F, rowBorder, rowFill);
            context.method_51439(ModuleConfigScreen.this.field_22793, this.optionTexts[i], x + 12, itemY + 7, over ? -1 : -6511697, false);
            itemY += 24;
         }

      }

      private void reloadOptions() {
         this.options = ModuleConfigScreen.readModeOptions(this.setting);
         this.optionTexts = new class_2561[this.options.length];

         for(int i = 0; i < this.options.length; ++i) {
            this.optionTexts[i] = class_2561.method_43470(this.options[i]);
         }

      }

      private void syncCurrentModeText() {
         this.modeText = class_2561.method_43470(ModuleConfigScreen.readModeValue(this.setting));
      }

      private void setModeIndex(int index) {
         if (this.options != null && this.options.length != 0) {
            index = Math.floorMod(index, this.options.length);
            ModuleConfigScreen.setModeByIndex(this.setting, index);
            this.syncCurrentModeText();
         }
      }

      boolean hasOpenOverlay() {
         return this.open;
      }

      void closeOverlay() {
         this.open = false;
      }
   }

   private final class ColorRow extends Row {
      private final Setting setting;
      private final class_2561 labelText;
      private final ColorPopup popup;
      private float hoverAnim = 0.0F;

      ColorRow(Setting setting) {
         this.setting = setting;
         this.labelText = class_2561.method_43470(ModuleConfigScreen.safeName(setting.getName(), "Color"));
         this.popup = ModuleConfigScreen.this.new ColorPopup(setting);
      }

      int getHeight() {
         return 44;
      }

      void tick(float dt) {
         this.popup.tick(dt);
      }

      void render(class_332 context, int mouseX, int mouseY, int drawY) {
         boolean hovered = this.hit((double)mouseX, (double)mouseY, drawY);
         this.hoverAnim = ModuleConfigScreen.lerp(this.hoverAnim, hovered ? 1.0F : 0.0F, 0.18F);
         ModuleConfigScreen.drawRow(context, this.x, drawY, this.w, this.h, hovered || this.popup.isOpen());
         context.method_51439(ModuleConfigScreen.this.field_22793, this.labelText, this.x + 16, drawY + 14, -1, false);
         int chipW = 42;
         int chipH = 24;
         int chipX = this.x + this.w - 42 - 16;
         int chipY = drawY + 10;
         int argb = ModuleConfigScreen.readColor(this.setting, -7643914);
         RoundedRectRenderer.outline(context, (float)chipX, (float)chipY, 42.0F, 24.0F, 10.0F, 1.0F, hovered ? -7643914 : -14011584, argb);
         context.method_51439(ModuleConfigScreen.this.field_22793, class_2561.method_43470(">"), chipX + 42 + 8, chipY + 8, -6511697, false);
      }

      void renderOverlay(class_332 context, int mouseX, int mouseY) {
         if (this.popup.isOpen()) {
            int chipW = 42;
            int chipH = 24;
            int chipX = this.x + this.w - 42 - 16;
            this.popup.render(context, mouseX, mouseY, chipX, this.visibleY + 24 + 6, 42);
         }

      }

      boolean mouseClicked(double mouseX, double mouseY, int button) {
         if (this.popup.isOpen() && this.popup.mouseClicked(mouseX, mouseY, button)) {
            return true;
         } else if (button == 0 && this.hit(mouseX, mouseY, this.visibleY)) {
            this.popup.toggle(this.x + this.w - 224 - 16, this.visibleY + this.h + 6, 224);
            return true;
         } else {
            return false;
         }
      }

      boolean mouseDragged(double mouseX, double mouseY, int button) {
         return this.popup.mouseDragged(mouseX, mouseY, button);
      }

      void mouseReleased(double mouseX, double mouseY, int button) {
         this.popup.mouseReleased(mouseX, mouseY, button);
      }

      boolean keyPressed(class_11908 input) {
         return this.popup.keyPressed(input);
      }

      boolean charTyped(class_11905 input) {
         return this.popup.charTyped(input);
      }

      boolean hasOpenOverlay() {
         return this.popup.isOpen();
      }

      void closeOverlay() {
         this.popup.close();
      }
   }

   private final class KeybindRow extends Row {
      private final Setting setting;
      private final class_2561 labelText;
      private class_2561 keyText;
      private boolean listening = false;

      KeybindRow(Setting setting) {
         this.setting = setting;
         this.labelText = class_2561.method_43470(ModuleConfigScreen.safeName(setting.getName(), "Bind"));
         this.syncKeyText();
      }

      int getHeight() {
         return 44;
      }

      void tick(float dt) {
         this.syncKeyText();
      }

      void render(class_332 context, int mouseX, int mouseY, int drawY) {
         boolean hovered = this.hit((double)mouseX, (double)mouseY, drawY);
         ModuleConfigScreen.drawRow(context, this.x, drawY, this.w, this.h, hovered || this.listening);
         context.method_51439(ModuleConfigScreen.this.field_22793, this.labelText, this.x + 16, drawY + 14, -1, false);
         String text = this.keyText.getString();
         int pillW = Math.max(120, ModuleConfigScreen.this.field_22793.method_1727(text) + 24);
         int px = this.x + this.w - pillW - 16;
         int py = drawY + 10;
         RoundedRectRenderer.outline(context, (float)px, (float)py, (float)pillW, 24.0F, 12.0F, 1.0F, this.listening ? -4682753 : -9076576, this.listening ? -13361078 : -14669766);
         context.method_51439(ModuleConfigScreen.this.field_22793, this.keyText, px + 12, py + 8, -1, false);
      }

      boolean mouseClicked(double mouseX, double mouseY, int button) {
         if (button == 0 && this.hit(mouseX, mouseY, this.visibleY)) {
            this.listening = true;
            return true;
         } else {
            return false;
         }
      }

      boolean keyPressed(class_11908 input) {
         if (!this.listening) {
            return false;
         } else if (input.method_74231()) {
            this.listening = false;
            return true;
         } else {
            ModuleConfigScreen.writeKey(this.setting, input.comp_4795());
            this.syncKeyText();
            this.listening = false;
            return true;
         }
      }

      private void syncKeyText() {
         String newText = this.listening ? "Press a key..." : ModuleConfigScreen.readKeyText(this.setting);
         if (this.keyText == null || !Objects.equals(this.keyText.getString(), newText)) {
            this.keyText = class_2561.method_43470(newText);
         }

      }
   }

   private final class FallbackRow extends Row {
      private final class_2561 labelText;
      private final class_2561 hintText = class_2561.method_43470("Unsupported setting");

      FallbackRow(Setting setting) {
         this.labelText = class_2561.method_43470(ModuleConfigScreen.safeName(setting.getName(), setting.getClass().getSimpleName()));
      }

      int getHeight() {
         return 40;
      }

      void render(class_332 context, int mouseX, int mouseY, int drawY) {
         boolean hovered = this.hit((double)mouseX, (double)mouseY, drawY);
         ModuleConfigScreen.drawRow(context, this.x, drawY, this.w, this.h, hovered);
         context.method_51439(ModuleConfigScreen.this.field_22793, this.labelText, this.x + 16, drawY + 10, -1, false);
         context.method_51439(ModuleConfigScreen.this.field_22793, this.hintText, this.x + 16, drawY + 23, -6511697, false);
      }
   }

   private final class ModuleColorTarget implements ColorTarget {
      public int getArgb() {
         return ModuleConfigScreen.this.getModuleAccentColor();
      }

      public void setArgb(int argb) {
         ModuleConfigScreen.this.setModuleAccentColor(argb);
      }
   }

   private final class ModuleColorPopup extends OverlayPopup {
      private static final int[] PRESETS = new int[]{-41369, -14003, -11665525, -11687937, -45640, -4682753, -8696321, -11665409, -30131, -10954753, -6488243, -8585317};
      private static final int SWATCH_COLUMNS = 4;
      private final ColorTarget target;
      private int currentColor = -7643914;
      private int selectedPreset = -1;
      private float r;
      private float g;
      private float b;
      private boolean draggingR;
      private boolean draggingG;
      private boolean draggingB;

      ModuleColorPopup(ColorTarget target) {
         this.target = target;
         this.syncFromTarget();
      }

      int getCurrentColor() {
         this.currentColor = this.target.getArgb();
         return this.currentColor;
      }

      void openPopup(int x, int y, int w) {
         if (this.open) {
            this.close();
         } else {
            int popupW = 246;
            int popupH = 198;
            this.open(x, y, 246, 198);
            this.x = ModuleConfigScreen.this.clampPopupX(x, 246);
            this.y = ModuleConfigScreen.this.clampPopupY(y, 198);
            this.syncFromTarget();
         }
      }

      void close() {
         super.close();
         this.draggingR = false;
         this.draggingG = false;
         this.draggingB = false;
      }

      void tick(float dt) {
         if (this.open) {
            if (this.draggingR || this.draggingG || this.draggingB) {
               this.applyColorFromSliders();
            }

         }
      }

      void render(class_332 context, int mouseX, int mouseY, int anchorX, int anchorY, int anchorW) {
         if (this.open) {
            int drawW = this.w == 0 ? 246 : this.w;
            int drawH = this.h == 0 ? 198 : this.h;
            int drawX = ModuleConfigScreen.this.clampPopupX(this.x == 0 ? anchorX : this.x, drawW);
            int drawY = ModuleConfigScreen.this.clampPopupY(this.y == 0 ? anchorY : this.y, drawH);
            drawX = Math.max(ModuleConfigScreen.this.panelX + 8, Math.min(drawX, ModuleConfigScreen.this.panelX + ModuleConfigScreen.this.panelW - drawW - 8));
            drawY = Math.max(ModuleConfigScreen.this.panelY + 54, Math.min(drawY, ModuleConfigScreen.this.panelY + ModuleConfigScreen.this.panelH - drawH - 8));
            this.x = drawX;
            this.y = drawY;
            this.w = drawW;
            this.h = drawH;
            ModuleConfigScreen.drawGlow(context, drawX, drawY, drawW, drawH, 18, 1519082742, 4);
            RoundedRectRenderer.outline(context, (float)drawX, (float)drawY, (float)drawW, (float)drawH, 18.0F, 1.0F, -13023914, -200667628);
            context.method_51439(ModuleConfigScreen.this.field_22793, ModuleConfigScreen.COLOR_TITLE, drawX + 12, drawY + 9, -1, false);
            int previewX = drawX + drawW - 44;
            int previewY = drawY + 8;
            RoundedRectRenderer.outline(context, (float)previewX, (float)previewY, 30.0F, 18.0F, 8.0F, 1.0F, -4944641, this.currentColor);
            int swatchSize = 24;
            int swatchGap = 8;
            int startX = drawX + 12;
            int startY = drawY + 30;

            for(int i = 0; i < PRESETS.length; ++i) {
               int sx = startX + i % 4 * 32;
               int sy = startY + i / 4 * 32;
               boolean over = ModuleConfigScreen.isInside((double)mouseX, (double)mouseY, sx, sy, 24, 24);
               boolean selected = i == this.selectedPreset || PRESETS[i] == this.currentColor;
               RoundedRectRenderer.outline(context, (float)sx, (float)sy, 24.0F, 24.0F, 9.0F, 1.0F, selected ? -1 : (over ? -2432513 : -14011584), PRESETS[i]);
               if (selected) {
                  context.method_25294(sx + 2, sy + 2, sx + 24 - 2, sy + 6, 587202559);
               }
            }

            int sliderX = drawX + 12;
            int sliderW = drawW - 24;
            this.drawColorSlider(context, mouseX, mouseY, sliderX, drawY + 108, sliderW, 0, this.r, -4944641, ModuleConfigScreen.R_TEXT);
            this.drawColorSlider(context, mouseX, mouseY, sliderX, drawY + 126, sliderW, 1, this.g, -4944641, ModuleConfigScreen.G_TEXT);
            this.drawColorSlider(context, mouseX, mouseY, sliderX, drawY + 144, sliderW, 2, this.b, -4944641, ModuleConfigScreen.B_TEXT);
         }
      }

      private void drawColorSlider(class_332 context, int mouseX, int mouseY, int x, int y, int width, int channel, float value, int fillColor, class_2561 label) {
         context.method_51439(ModuleConfigScreen.this.field_22793, label, x, y + 2, -6511697, false);
         int trackX = x + 18;
         int trackY = y + 7;
         int trackW = width - 24;
         RoundedRectRenderer.outline(context, (float)trackX, (float)trackY, (float)trackW, 6.0F, 3.0F, 1.0F, -15789027, -15789027);
         int fillW = (int)((float)trackW * value);
         if (fillW > 0) {
            RoundedRectRenderer.outline(context, (float)trackX, (float)trackY, (float)fillW, 6.0F, 3.0F, 1.0F, fillColor, fillColor);
         }

         int thumbX = trackX + Math.max(0, fillW - 5);
         RoundedRectRenderer.outline(context, (float)thumbX, (float)(trackY - 3), 10.0F, 12.0F, 6.0F, 1.0F, -1, -460036);
      }

      boolean mouseClicked(double mouseX, double mouseY, int button) {
         if (this.open && button == 0) {
            if (!this.isInside(mouseX, mouseY)) {
               this.close();
               return true;
            } else {
               int swatchSize = 24;
               int swatchGap = 8;
               int startX = this.x + 12;
               int startY = this.y + 30;

               for(int i = 0; i < PRESETS.length; ++i) {
                  int sx = startX + i % 4 * 32;
                  int sy = startY + i / 4 * 32;
                  if (ModuleConfigScreen.isInside(mouseX, mouseY, sx, sy, 24, 24)) {
                     this.applyPreset(PRESETS[i]);
                     return true;
                  }
               }

               if (ModuleConfigScreen.isInside(mouseX, mouseY, this.x + 18, this.y + 108, this.w - 24, 12)) {
                  this.draggingR = true;
                  this.updateChannelFromMouse(mouseX, 0);
                  return true;
               } else if (ModuleConfigScreen.isInside(mouseX, mouseY, this.x + 18, this.y + 126, this.w - 24, 12)) {
                  this.draggingG = true;
                  this.updateChannelFromMouse(mouseX, 1);
                  return true;
               } else if (ModuleConfigScreen.isInside(mouseX, mouseY, this.x + 18, this.y + 144, this.w - 24, 12)) {
                  this.draggingB = true;
                  this.updateChannelFromMouse(mouseX, 2);
                  return true;
               } else {
                  return true;
               }
            }
         } else {
            return false;
         }
      }

      boolean mouseDragged(double mouseX, double mouseY, int button) {
         if (this.open && button == 0) {
            if (this.draggingR) {
               this.updateChannelFromMouse(mouseX, 0);
               return true;
            } else if (this.draggingG) {
               this.updateChannelFromMouse(mouseX, 1);
               return true;
            } else if (this.draggingB) {
               this.updateChannelFromMouse(mouseX, 2);
               return true;
            } else {
               return false;
            }
         } else {
            return false;
         }
      }

      boolean mouseReleased(double mouseX, double mouseY, int button) {
         if (button == 0) {
            this.draggingR = false;
            this.draggingG = false;
            this.draggingB = false;
         }

         return false;
      }

      boolean keyPressed(class_11908 input) {
         if (input.method_74231()) {
            this.close();
            return true;
         } else {
            return false;
         }
      }

      boolean charTyped(class_11905 input) {
         return false;
      }

      private void updateChannelFromMouse(double mouseX, int channel) {
         float pct = ModuleConfigScreen.clamp01((float)((mouseX - (double)(this.x + 18)) / Math.max((double)1.0F, (double)(this.w - 24))));
         this.setChannel(channel, pct);
         this.applyColorFromSliders();
      }

      private void setChannel(int channel, float pct) {
         switch (channel) {
            case 0 -> this.r = pct;
            case 1 -> this.g = pct;
            case 2 -> this.b = pct;
         }

      }

      private void applyPreset(int argb) {
         this.selectedPreset = this.indexOfPreset(argb);
         this.r = (float)(argb >>> 16 & 255) / 255.0F;
         this.g = (float)(argb >>> 8 & 255) / 255.0F;
         this.b = (float)(argb & 255) / 255.0F;
         this.applyColorFromSliders();
      }

      private int indexOfPreset(int argb) {
         for(int i = 0; i < PRESETS.length; ++i) {
            if (PRESETS[i] == argb) {
               return i;
            }
         }

         return -1;
      }

      private void syncFromTarget() {
         this.currentColor = this.target.getArgb();
         this.selectedPreset = this.indexOfPreset(this.currentColor);
         this.r = (float)(this.currentColor >>> 16 & 255) / 255.0F;
         this.g = (float)(this.currentColor >>> 8 & 255) / 255.0F;
         this.b = (float)(this.currentColor & 255) / 255.0F;
      }

      private void applyColorFromSliders() {
         this.currentColor = ModuleConfigScreen.packColor(this.r, this.g, this.b);
         this.selectedPreset = this.indexOfPreset(this.currentColor);
         this.target.setArgb(this.currentColor);
      }
   }

   private abstract class OverlayPopup {
      protected int x;
      protected int y;
      protected int w;
      protected int h;
      protected boolean open = false;

      boolean isOpen() {
         return this.open;
      }

      boolean isClosed() {
         return !this.open;
      }

      void open(int x, int y, int w, int h) {
         this.x = x;
         this.y = y;
         this.w = w;
         this.h = h;
         this.open = true;
      }

      void close() {
         this.open = false;
      }

      boolean isInside(double mouseX, double mouseY) {
         return ModuleConfigScreen.isInside(mouseX, mouseY, this.x, this.y, this.w, this.h);
      }

      void tick(float dt) {
      }

      abstract void render(class_332 var1, int var2, int var3, int var4, int var5, int var6);

      boolean mouseClicked(double mouseX, double mouseY, int button) {
         return false;
      }

      boolean mouseDragged(double mouseX, double mouseY, int button) {
         return false;
      }

      boolean mouseReleased(double mouseX, double mouseY, int button) {
         return false;
      }

      boolean mouseScrolled(double mouseX, double mouseY, double verticalAmount) {
         return false;
      }

      boolean keyPressed(class_11908 input) {
         return false;
      }

      boolean charTyped(class_11905 input) {
         return false;
      }
   }

   private static final class RectBox {
      final int x;
      final int y;
      final int w;
      final int h;

      RectBox(int x, int y, int w, int h) {
         this.x = x;
         this.y = y;
         this.w = w;
         this.h = h;
      }
   }

   private final class ColorPopup extends OverlayPopup {
      private static final int[] PRESETS = new int[]{-1, -854792, -3485477, -6182728, -9603966, -12893614, -14472650, -16118510, -15395563, -14408668, -13163237, -11915745, -10734302, -9753042, -7660002, -13754562, -7643914, -41369, -14003, -11665525, -11687937, -45640, -11665409, -6488243};
      private static final int SWATCH_COLUMNS = 4;
      private final Setting setting;
      private int selectedPreset = -1;

      ColorPopup(Setting setting) {
         this.setting = setting;
         this.syncFromSetting();
      }

      void toggle(int preferredX, int preferredY, int preferredW) {
         if (this.open) {
            this.close();
         } else {
            int popupW = 154;
            int popupH = 160;
            this.open(preferredX, preferredY, 154, 160);
            this.x = ModuleConfigScreen.this.clampPopupX(preferredX, 154);
            this.y = ModuleConfigScreen.this.clampPopupY(preferredY, 160);
            this.syncFromSetting();
         }
      }

      void close() {
         super.close();
      }

      void tick(float dt) {
         if (this.open) {
            this.syncFromSetting();
         }
      }

      void render(class_332 context, int mouseX, int mouseY, int anchorX, int anchorY, int anchorW) {
         if (this.open) {
            int drawW = this.w == 0 ? 154 : this.w;
            int drawH = this.h == 0 ? 160 : this.h;
            int drawX = ModuleConfigScreen.this.clampPopupX(this.x == 0 ? anchorX : this.x, drawW);
            int drawY = ModuleConfigScreen.this.clampPopupY(this.y == 0 ? anchorY : this.y, drawH);
            drawX = Math.max(ModuleConfigScreen.this.panelX + 6, Math.min(drawX, ModuleConfigScreen.this.panelX + ModuleConfigScreen.this.panelW - drawW - 6));
            drawY = Math.max(ModuleConfigScreen.this.panelY + 44, Math.min(drawY, ModuleConfigScreen.this.panelY + ModuleConfigScreen.this.panelH - drawH - 6));
            this.x = drawX;
            this.y = drawY;
            this.w = drawW;
            this.h = drawH;
            RoundedRectRenderer.outline(context, (float)drawX, (float)drawY, (float)drawW, (float)drawH, 14.0F, 1.0F, -14011323, -200667628);
            context.method_51439(ModuleConfigScreen.this.field_22793, ModuleConfigScreen.COLOR_TITLE, drawX + 10, drawY + 8, -1, false);
            int current = ModuleConfigScreen.readColor(this.setting, -7643914);
            int swatchSize = 22;
            int swatchGap = 6;
            int startX = drawX + 10;
            int startY = drawY + 28;

            for(int i = 0; i < PRESETS.length; ++i) {
               int sx = startX + i % 4 * 28;
               int sy = startY + i / 4 * 28;
               boolean over = ModuleConfigScreen.isInside((double)mouseX, (double)mouseY, sx, sy, 22, 22);
               boolean selected = this.selectedPreset == i || (PRESETS[i] & 16777215) == (current & 16777215);
               if (over) {
                  this.applyPreset(PRESETS[i]);
               }

               RoundedRectRenderer.outline(context, (float)sx, (float)sy, 22.0F, 22.0F, 7.0F, 1.0F, selected ? -7643914 : -14669768, PRESETS[i]);
            }

         }
      }

      boolean mouseClicked(double mouseX, double mouseY, int button) {
         if (this.open && button == 0) {
            if (!this.isInside(mouseX, mouseY)) {
               this.close();
               return true;
            } else {
               int swatchSize = 22;
               int swatchGap = 6;
               int startX = this.x + 10;
               int startY = this.y + 28;

               for(int i = 0; i < PRESETS.length; ++i) {
                  int sx = startX + i % 4 * 28;
                  int sy = startY + i / 4 * 28;
                  if (ModuleConfigScreen.isInside(mouseX, mouseY, sx, sy, 22, 22)) {
                     this.applyPreset(PRESETS[i]);
                     return true;
                  }
               }

               return true;
            }
         } else {
            return false;
         }
      }

      boolean mouseDragged(double mouseX, double mouseY, int button) {
         return false;
      }

      boolean mouseReleased(double mouseX, double mouseY, int button) {
         return false;
      }

      boolean keyPressed(class_11908 input) {
         if (input.method_74231()) {
            this.close();
            return true;
         } else {
            return false;
         }
      }

      boolean charTyped(class_11905 input) {
         return false;
      }

      private void applyPreset(int argb) {
         this.selectedPreset = this.indexOfPreset(argb);
         ModuleConfigScreen.writeColor(this.setting, argb);
         ModuleConfigScreen.persistChange(this.setting);
      }

      private int indexOfPreset(int argb) {
         for(int i = 0; i < PRESETS.length; ++i) {
            if ((PRESETS[i] & 16777215) == (argb & 16777215)) {
               return i;
            }
         }

         return -1;
      }

      private void syncFromSetting() {
         int argb = ModuleConfigScreen.readColor(this.setting, -7643914);
         this.selectedPreset = this.indexOfPreset(argb);
      }
   }

   private static final class ScrollState {
      private final int rowGap = 8;
      private int viewportX;
      private int viewportY;
      private int viewportW;
      private int viewportH;
      private float scroll = 0.0F;
      private float targetScroll = 0.0F;
      private float maxScroll = 0.0F;
      private List<Row> rows = List.of();

      void reset() {
         this.scroll = 0.0F;
         this.targetScroll = 0.0F;
         this.maxScroll = 0.0F;
      }

      void layout(int x, int y, int w, int h, List<Row> rows) {
         this.viewportX = x;
         this.viewportY = y;
         this.viewportW = w;
         this.viewportH = h;
         this.rows = rows;
         int cy = y + 6;

         for(Row row : rows) {
            row.layout(x, cy, w);
            cy += row.getHeight() + 8;
         }

         this.maxScroll = Math.max(0.0F, (float)(cy - y - h + 8));
         this.scroll = clamp(this.scroll, 0.0F, this.maxScroll);
         this.targetScroll = clamp(this.targetScroll, 0.0F, this.maxScroll);
      }

      void tick(float dt) {
         float speed = 1.0F - (float)Math.exp((double)(-dt) * (double)24.0F);
         this.scroll += (this.targetScroll - this.scroll) * speed;
         if (Math.abs(this.scroll - this.targetScroll) < 0.02F) {
            this.scroll = this.targetScroll;
         }

      }

      boolean mouseScrolled(double mouseX, double mouseY, double verticalAmount) {
         if (!ModuleConfigScreen.isInside(mouseX, mouseY, this.viewportX, this.viewportY, this.viewportW, this.viewportH)) {
            return false;
         } else {
            float step = Math.max(34.0F, (float)this.viewportH * 0.1F);
            this.targetScroll = clamp(this.targetScroll - (float)verticalAmount * step, 0.0F, this.maxScroll);
            return true;
         }
      }

      boolean isOverScrollbar(double mouseX, double mouseY) {
         int barX = this.viewportX + this.viewportW - 6;
         return ModuleConfigScreen.isInside(mouseX, mouseY, barX - 4, this.viewportY, 12, this.viewportH);
      }

      boolean mouseClicked(double mouseX, double mouseY, int button) {
         if (!ModuleConfigScreen.isInside(mouseX, mouseY, this.viewportX, this.viewportY, this.viewportW, this.viewportH)) {
            return false;
         } else {
            int drawY = this.viewportY - (int)this.scroll;
            int yCursor = drawY;

            for(Row row : this.rows) {
               int rowH = row.getHeight();
               row.visibleY = yCursor;
               if (ModuleConfigScreen.isInside(mouseX, mouseY, this.viewportX, yCursor, this.viewportW, rowH) && row.mouseClicked(mouseX, mouseY, button)) {
                  return true;
               }

               yCursor += rowH + 8;
            }

            return false;
         }
      }

      boolean mouseDragged(double mouseX, double mouseY, int button) {
         int drawY = this.viewportY - (int)this.scroll;
         int yCursor = drawY;

         for(Row row : this.rows) {
            int rowH = row.getHeight();
            row.visibleY = yCursor;
            if (ModuleConfigScreen.isInside(mouseX, mouseY, this.viewportX, yCursor, this.viewportW, rowH) && row.mouseDragged(mouseX, mouseY, button)) {
               return true;
            }

            yCursor += rowH + 8;
         }

         return false;
      }

      void mouseReleased(double mouseX, double mouseY, int button) {
         for(Row row : this.rows) {
            row.mouseReleased(mouseX, mouseY, button);
         }

      }

      boolean keyPressed(class_11908 input) {
         for(Row row : this.rows) {
            if (row.keyPressed(input)) {
               return true;
            }
         }

         return false;
      }

      boolean charTyped(class_11905 input) {
         for(Row row : this.rows) {
            if (row.charTyped(input)) {
               return true;
            }
         }

         return false;
      }

      void render(class_332 context, int mouseX, int mouseY) {
         int clipLeft = this.viewportX;
         int clipTop = this.viewportY;
         int clipRight = this.viewportX + this.viewportW;
         int clipBottom = this.viewportY + this.viewportH;
         context.method_44379(clipLeft, clipTop, clipRight, clipBottom);
         int drawY = this.viewportY - (int)this.scroll;
         int yCursor = drawY;

         for(Row row : this.rows) {
            int rowH = row.getHeight();
            row.visibleY = yCursor;
            if (yCursor + rowH >= this.viewportY - 24 && yCursor <= this.viewportY + this.viewportH + 24) {
               row.render(context, mouseX, mouseY, yCursor);
            }

            yCursor += rowH + 8;
         }

         context.method_44380();
         if (this.maxScroll > 0.5F) {
            int barX = this.viewportX + this.viewportW - 4;
            int thumbH = this.getThumbHeight();
            float thumbPos = this.scroll / Math.max(1.0F, this.maxScroll) * (float)Math.max(1, this.viewportH - thumbH);
            int thumbY = this.viewportY + Math.round(thumbPos);
            context.method_25294(barX, this.viewportY, barX + 2, this.viewportY + this.viewportH, 1317668);
            context.method_25294(barX, thumbY, barX + 2, thumbY + thumbH, -7643914);
         }

         for(Row row : this.rows) {
            row.renderOverlay(context, mouseX, mouseY);
         }

      }

      int getThumbHeight() {
         return this.maxScroll <= 0.5F ? this.viewportH : Math.max(24, (int)((float)this.viewportH * ((float)this.viewportH / ((float)this.viewportH + this.maxScroll))));
      }

      private static float clamp(float value, float min, float max) {
         return Math.max(min, Math.min(max, value));
      }
   }

   private interface ColorTarget {
      int getArgb();

      void setArgb(int var1);
   }
}
