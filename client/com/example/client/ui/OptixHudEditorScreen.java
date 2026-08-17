package com.example.client.ui;

import com.example.client.config.HudConfig;
import com.example.client.module.Category;
import com.example.client.module.Module;
import com.example.client.module.ModuleManager;
import com.example.client.ui.render.GlassRenderer;
import com.example.client.ui.render.RoundedRectRenderer;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_11905;
import net.minecraft.class_11908;
import net.minecraft.class_11909;
import net.minecraft.class_2561;
import net.minecraft.class_310;
import net.minecraft.class_332;
import net.minecraft.class_437;
import org.lwjgl.glfw.GLFW;

@Environment(EnvType.CLIENT)
public final class OptixHudEditorScreen extends class_437 {
   private static final class_310 MC = class_310.method_1551();
   private static final int BACKDROP = -653916147;
   private static final int SOFT_OVERLAY = 570425344;
   private static final int TEXT_MAIN = -1;
   private static final int TEXT_DIM = -1275068417;
   private static final int TEXT_FAINT = 1895825407;
   private static final int ACCENT = -8614657;
   private static final int ACCENT_SOFT = 863800575;
   private static final int OUTLINE = 1283230975;
   private static final class_2561 OPTIX_TEXT = class_2561.method_43470("OptiX");
   private static final class_2561 CLEAN_TEXT = class_2561.method_43470("clean");
   private static final class_2561 HUD_EDITOR_TEXT = class_2561.method_43470("HUD Editor");
   private static final class_2561 HUD_SUBTITLE_TEXT = class_2561.method_43470("Keep the center empty. Drag cards to the corners.");
   private static final class_2561 DRAG_TEXT = class_2561.method_43470("drag");
   private static final class_2561 OPTIX_OPTIONS_TEXT = class_2561.method_43470("OptiX Options");
   private static final class_2561 MAIN_MENU_HINT_TEXT = class_2561.method_43470("Open your main client menu");
   private static final class_2561 KEYBIND_HINT_TEXT = class_2561.method_43470("Change the open keybind in Minecraft Controls");
   private static final class_2561 ESC_SAVE_TEXT = class_2561.method_43470("ESC to save and close");
   private final List<Module> hudModules = new ArrayList();
   private final Map<String, AnimState> animMap = new HashMap();
   private Module draggingModule;
   private int dragOffsetX;
   private int dragOffsetY;
   private float openAnim = 0.0F;
   private float brandHoverAnim = 0.0F;
   private float centerHoverAnim = 0.0F;
   private long lastNs = 0L;
   private int lastMouseX;
   private int lastMouseY;

   public OptixHudEditorScreen() {
      super(class_2561.method_43470("OptiX HUD Editor"));
   }

   protected void method_25426() {
      super.method_25426();
      this.rebuildHudList();
      this.animMap.clear();
      this.openAnim = 0.0F;
      this.brandHoverAnim = 0.0F;
      this.centerHoverAnim = 0.0F;
      this.lastNs = System.nanoTime();
   }

   public boolean method_25421() {
      return false;
   }

   private void rebuildHudList() {
      this.hudModules.clear();

      for(Module module : ModuleManager.getModules()) {
         if (module != null && module.getCategory() == Category.HUD) {
            this.hudModules.add(module);
         }
      }

      this.hudModules.sort(Comparator.comparing(Module::getName, String.CASE_INSENSITIVE_ORDER));
   }

   private static float clamp01(float v) {
      return Math.max(0.0F, Math.min(1.0F, v));
   }

   private static float clampFloat(float value, float min, float max) {
      return Math.max(min, Math.min(max, value));
   }

   private static int clampInt(int value, int min, int max) {
      return Math.max(min, Math.min(max, value));
   }

   private static float stabilizeLerp(float current, float target, float speed, float dt) {
      float diff = target - current;
      if (Math.abs(diff) < 1.0E-4F) {
         return target;
      } else {
         float factor = speed * dt;
         if (factor > 1.0F) {
            factor = 1.0F;
         }

         return current + diff * factor;
      }
   }

   private static float easeOutCubic(float t) {
      t = clamp01(t);
      float u = 1.0F - t;
      return 1.0F - u * u * u;
   }

   private void updateAnim() {
      long now = System.nanoTime();
      if (this.lastNs == 0L) {
         this.lastNs = now;
      }

      float dt = Math.min((float)(now - this.lastNs) / 1.0E9F, 0.033F);
      this.lastNs = now;
      this.openAnim = stabilizeLerp(this.openAnim, 1.0F, 8.0F, dt);
      boolean brandHovered = this.isInside((double)this.lastMouseX, (double)this.lastMouseY, 16, 16, 126, 36);
      this.brandHoverAnim = stabilizeLerp(this.brandHoverAnim, brandHovered ? 1.0F : 0.0F, 12.0F, dt);
      boolean centerHovered = this.isInside((double)this.lastMouseX, (double)this.lastMouseY, this.field_22789 / 2 - 150, this.field_22790 / 2 - 19, 300, 38);
      this.centerHoverAnim = stabilizeLerp(this.centerHoverAnim, centerHovered ? 1.0F : 0.0F, 12.0F, dt);

      for(int i = 0; i < this.hudModules.size(); ++i) {
         Module module = (Module)this.hudModules.get(i);
         if (module != null) {
            String key = module.getName();
            AnimState state = (AnimState)this.animMap.get(key);
            if (state == null) {
               state = new AnimState(key, module.getCategory().name(), module.isEnabled());
               this.animMap.put(key, state);
            }

            boolean hovered = this.isModuleHovered(module, this.lastMouseX, this.lastMouseY);
            state.hover = stabilizeLerp(state.hover, hovered ? 1.0F : 0.0F, 12.0F, dt);
            state.enable = stabilizeLerp(state.enable, module.isEnabled() ? 1.0F : 0.0F, 8.0F, dt);
         }
      }

   }

   public void method_25394(class_332 context, int mouseX, int mouseY, float delta) {
      this.lastMouseX = mouseX;
      this.lastMouseY = mouseY;
      this.updateAnim();
      float ui = easeOutCubic(this.openAnim);
      float popupLift = (1.0F - ui) * 12.0F;
      float popupScale = 0.975F + ui * 0.025F;
      context.method_25294(0, 0, this.field_22789, this.field_22790, -653916147);
      context.method_25294(0, 0, this.field_22789, this.field_22790, 570425344);
      this.drawBackdropDetails(context);
      this.drawBrandCard(context);
      this.drawTitle(context);
      this.drawModules(context, mouseX, mouseY, popupLift, popupScale);
      this.drawCenterButton(context, mouseX, mouseY, popupLift, popupScale);
      this.drawFooter(context);
      super.method_25394(context, mouseX, mouseY, delta);
   }

   private void drawBackdropDetails(class_332 context) {
      int alpha = 18;
      context.method_25294(0, this.field_22790 / 2 - 1, this.field_22789, this.field_22790 / 2 + 1, alpha << 24 | 16777215);
      context.method_25294(this.field_22789 / 2 - 1, 0, this.field_22789 / 2 + 1, this.field_22790, alpha << 24 | 16777215);
   }

   private void drawBrandCard(class_332 context) {
      float scale = 1.0F + this.brandHoverAnim * 0.045F;
      float lift = this.brandHoverAnim * 2.0F;
      float w = 126.0F * scale;
      float h = 36.0F * scale;
      float x = 16.0F - (w - 126.0F) * 0.5F;
      float y = 16.0F - (h - 36.0F) * 0.5F - lift;
      GlassRenderer.drawCard(context, x, y, w, h);
      RoundedRectRenderer.outline(context, x, y, w, h, 14.0F, 1.0F, 1283230975, 1052692);
      context.method_51439(this.field_22793, OPTIX_TEXT, Math.round(x + 14.0F), Math.round(y + 10.0F), -1, false);
      context.method_51439(this.field_22793, CLEAN_TEXT, Math.round(x + 72.0F), Math.round(y + 10.0F), 1895825407, false);
   }

   private void drawTitle(class_332 context) {
      context.method_51439(this.field_22793, HUD_EDITOR_TEXT, this.field_22789 / 2 - this.field_22793.method_27525(HUD_EDITOR_TEXT) / 2, 14, -1, false);
      context.method_51439(this.field_22793, HUD_SUBTITLE_TEXT, this.field_22789 / 2 - this.field_22793.method_27525(HUD_SUBTITLE_TEXT) / 2, 30, -1275068417, false);
   }

   private void drawModules(class_332 context, int mouseX, int mouseY, float popupLift, float popupScale) {
      for(int i = 0; i < this.hudModules.size(); ++i) {
         Module module = (Module)this.hudModules.get(i);
         if (module != null) {
            String name = module.getName();
            AnimState state = (AnimState)this.animMap.get(name);
            float hover = state != null ? state.hover : 0.0F;
            float enabled = state != null ? state.enable : (module.isEnabled() ? 1.0F : 0.0F);
            class_2561 nameText = (class_2561)(state != null ? state.nameText : class_2561.method_43470(name));
            class_2561 categoryText = (class_2561)(state != null ? state.categoryText : class_2561.method_43470(module.getCategory().name().toLowerCase()));
            int x = module.getX();
            int y = module.getY();
            int w = this.displayWidth(module);
            int h = this.displayHeight(module);
            int drawW = Math.round((float)w * popupScale);
            int drawH = Math.round((float)h * popupScale);
            int drawX = Math.round((float)x - (float)(drawW - w) * 0.5F);
            int drawY = Math.round((float)y + popupLift - (float)(drawH - h) * 0.5F - hover * 2.0F);
            boolean hovered = this.isInside((double)mouseX, (double)mouseY, drawX, drawY, drawW, drawH);
            boolean selected = module == this.draggingModule;
            int fill = selected ? -535092944 : (hovered ? -719708373 : -921298396);
            if (enabled > 0.01F) {
               fill = this.lerpColor(fill, -719313096, enabled * 0.1F);
            }

            GlassRenderer.drawCard(context, (float)drawX, (float)drawY, (float)drawW, (float)drawH);
            RoundedRectRenderer.outline(context, (float)drawX, (float)drawY, (float)drawW, (float)drawH, 16.0F, 1.0F, selected ? -8614657 : (hovered ? 863800575 : 989855743), fill);
            int titleColor = selected ? -1 : (module.isEnabled() ? -1 : -1275068417);
            int subColor = hovered ? -1275068417 : 1895825407;
            context.method_51439(this.field_22793, nameText, drawX + 10, drawY + 8, titleColor, false);
            context.method_51439(this.field_22793, categoryText, drawX + 10, drawY + drawH - 12, subColor, false);
            if (hover > 0.02F) {
               int badgeW = 34;
               int badgeH = 16;
               int badgeX = drawX + drawW - badgeW - 8;
               int badgeY = drawY + 8;
               GlassRenderer.drawCard(context, (float)badgeX, (float)badgeY, (float)badgeW, (float)badgeH);
               RoundedRectRenderer.outline(context, (float)badgeX, (float)badgeY, (float)badgeW, (float)badgeH, 8.0F, 1.0F, hovered ? -8614657 : 1258291199, 1052692);
               context.method_51439(this.field_22793, DRAG_TEXT, badgeX + 7, badgeY + 4, -1275068417, false);
            }
         }
      }

   }

   private void drawCenterButton(class_332 context, int mouseX, int mouseY, float popupLift, float popupScale) {
      int baseW = 300;
      int baseH = 38;
      int x = this.field_22789 / 2 - baseW / 2;
      int y = Math.round((float)this.field_22790 / 2.0F - (float)baseH / 2.0F + popupLift);
      int drawW = Math.round((float)baseW * popupScale);
      int drawH = Math.round((float)baseH * popupScale);
      int drawX = x - (drawW - baseW) / 2;
      int drawY = y - (drawH - baseH) / 2 - Math.round(this.centerHoverAnim * 1.5F);
      boolean hovered = this.isInside((double)mouseX, (double)mouseY, drawX, drawY, drawW, drawH);
      boolean pressed = hovered && GLFW.glfwGetMouseButton(MC.method_22683().method_4490(), 0) == 1;
      GlassRenderer.drawButton(context, (float)drawX, (float)drawY, (float)drawW, (float)drawH, "OptiX Options", hovered, pressed);
      context.method_51439(this.field_22793, MAIN_MENU_HINT_TEXT, this.field_22789 / 2 - this.field_22793.method_27525(MAIN_MENU_HINT_TEXT) / 2, drawY + drawH + 12, -1275068417, false);
      context.method_51439(this.field_22793, KEYBIND_HINT_TEXT, this.field_22789 / 2 - this.field_22793.method_27525(KEYBIND_HINT_TEXT) / 2, drawY + drawH + 24, 1895825407, false);
   }

   private void drawFooter(class_332 context) {
      int x = 16;
      int y = this.field_22790 - 34;
      GlassRenderer.drawCard(context, (float)x, (float)y, 206.0F, 22.0F);
      context.method_51439(this.field_22793, ESC_SAVE_TEXT, x + 12, y + 7, -1, false);
   }

   public boolean method_25402(class_11909 click, boolean doubled) {
      double mouseX = click.comp_4798();
      double mouseY = click.comp_4799();
      int button = click.method_74245();
      int buttonW = 300;
      int buttonH = 38;
      int x = this.field_22789 / 2 - buttonW / 2;
      int y = this.field_22790 / 2 - buttonH / 2;
      if (button == 0 && this.isInside(mouseX, mouseY, x, y, buttonW, buttonH)) {
         MC.method_1507(new ModernClickGUI());
         return true;
      } else {
         for(int i = this.hudModules.size() - 1; i >= 0; --i) {
            Module module = (Module)this.hudModules.get(i);
            if (module != null) {
               int mx = module.getX();
               int my = module.getY();
               int mw = this.displayWidth(module);
               int mh = this.displayHeight(module);
               if (this.isInside(mouseX, mouseY, mx, my, mw, mh)) {
                  this.draggingModule = module;
                  this.dragOffsetX = (int)mouseX - mx;
                  this.dragOffsetY = (int)mouseY - my;
                  return true;
               }
            }
         }

         return super.method_25402(click, doubled);
      }
   }

   public boolean method_25403(class_11909 click, double deltaX, double deltaY) {
      if (click.method_74245() == 0 && this.draggingModule != null) {
         int screenW = MC.method_22683().method_4486();
         int screenH = MC.method_22683().method_4502();
         int mw = this.displayWidth(this.draggingModule);
         int mh = this.displayHeight(this.draggingModule);
         int newX = (int)click.comp_4798() - this.dragOffsetX;
         int newY = (int)click.comp_4799() - this.dragOffsetY;
         boolean snap = false;

         try {
            snap = this.draggingModule.isSnapToGrid();
         } catch (Throwable var14) {
         }

         if (snap) {
            int grid = 4;
            newX = Math.round((float)newX / (float)grid) * grid;
            newY = Math.round((float)newY / (float)grid) * grid;
         }

         newX = clampInt(newX, 8, screenW - mw - 8);
         newY = clampInt(newY, 8, screenH - mh - 8);
         this.draggingModule.setPosition(newX, newY);
         return true;
      } else {
         return super.method_25403(click, deltaX, deltaY);
      }
   }

   public boolean method_25406(class_11909 click) {
      if (click.method_74245() == 0 && this.draggingModule != null) {
         this.draggingModule = null;
         HudConfig.save();
         return true;
      } else {
         return super.method_25406(click);
      }
   }

   public boolean method_25401(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
      for(int i = 0; i < this.hudModules.size(); ++i) {
         Module module = (Module)this.hudModules.get(i);
         if (module != null) {
            int mx = module.getX();
            int my = module.getY();
            int mw = this.displayWidth(module);
            int mh = this.displayHeight(module);
            if (this.isInside(mouseX, mouseY, mx, my, mw, mh)) {
               try {
                  float next = module.getScale() + (float)(verticalAmount * (double)0.05F);
                  module.setScale(clampFloat(next, 0.5F, 2.0F));
                  HudConfig.save();
                  return true;
               } catch (Throwable var16) {
                  return true;
               }
            }
         }
      }

      return super.method_25401(mouseX, mouseY, horizontalAmount, verticalAmount);
   }

   public boolean method_25404(class_11908 input) {
      if (input.comp_4795() == 256) {
         HudConfig.save();
         this.method_25419();
         return true;
      } else {
         return super.method_25404(input);
      }
   }

   public boolean method_25400(class_11905 input) {
      return super.method_25400(input);
   }

   public void method_25419() {
      HudConfig.save();
      MC.method_1507((class_437)null);
   }

   private int displayWidth(Module module) {
      int w = 110;

      try {
         w = module.getWidth();
      } catch (Throwable var4) {
      }

      return clampInt(w, 86, 220);
   }

   private int displayHeight(Module module) {
      int h = 34;

      try {
         h = module.getHeight();
      } catch (Throwable var4) {
      }

      return clampInt(h, 28, 92);
   }

   private boolean isModuleHovered(Module module, int mouseX, int mouseY) {
      int x = module.getX();
      int y = module.getY();
      int w = this.displayWidth(module);
      int h = this.displayHeight(module);
      return this.isInside((double)mouseX, (double)mouseY, x, y, w, h);
   }

   private boolean isInside(double mx, double my, int x, int y, int w, int h) {
      return mx >= (double)x && mx <= (double)(x + w) && my >= (double)y && my <= (double)(y + h);
   }

   private int lerpColor(int from, int to, float t) {
      t = clamp01(t);
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

   private static final class AnimState {
      float hover = 0.0F;
      float enable;
      final class_2561 nameText;
      final class_2561 categoryText;

      AnimState(String name, String category, boolean enabled) {
         this.enable = enabled ? 1.0F : 0.0F;
         this.nameText = class_2561.method_43470(name);
         this.categoryText = class_2561.method_43470(category.toLowerCase());
      }
   }
}
