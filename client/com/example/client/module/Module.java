package com.example.client.module;

import com.example.client.config.ConfigManager;
import com.example.client.setting.BooleanSetting;
import com.example.client.setting.NumberSetting;
import com.example.client.setting.Setting;
import com.example.client.ui.HudScaleManager;
import com.example.client.ui.RenderUtils;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.class_2960;
import net.minecraft.class_304;
import net.minecraft.class_310;
import net.minecraft.class_332;

public abstract class Module {
   protected static final class_310 mc = class_310.method_1551();
   private final String name;
   private final Category category;
   private final class_2960 icon;
   private boolean enabled;
   private int key = -1;
   private class_304 keyBinding;
   private final List<Setting> settings = new ArrayList();
   protected int x = 20;
   protected int y = 20;
   protected int width = 100;
   protected int height = 30;
   protected float scale = 1.0F;
   protected float smoothX;
   protected float smoothY;
   protected boolean dragging;
   protected int dragOffsetX;
   protected int dragOffsetY;
   protected float velX;
   protected float velY;
   protected float dragEase;
   protected float friction;
   protected boolean resizing;
   protected ResizeDir resizeDir;
   protected static final int CORNER = 8;
   protected int minWidth;
   protected int minHeight;
   protected int gridSize;
   protected boolean snapToGrid;
   public static final List<Module> selectedModules = new ArrayList();
   public static boolean selectingBox = false;
   public static float selStartX;
   public static float selStartY;
   public static float selEndX;
   public static float selEndY;
   public static float groupStartX;
   public static float groupStartY;
   public static boolean hudEditing = false;
   private int hudColor;
   private boolean rainbow;
   private boolean rgb;
   private boolean box;
   private boolean outline;
   private boolean glow;
   private boolean vertical;
   private boolean draggable;
   private final BooleanSetting rainbowSetting;
   private final BooleanSetting rgbSetting;
   private final BooleanSetting boxSetting;
   private final BooleanSetting outlineSetting;
   private final BooleanSetting glowSetting;
   private final BooleanSetting verticalSetting;
   private final BooleanSetting draggableSetting;
   private final NumberSetting scaleSetting;
   private final NumberSetting radiusSetting;
   private final NumberSetting outlineThicknessSetting;
   private final NumberSetting glowSizeSetting;
   private final NumberSetting redSetting;
   private final NumberSetting greenSetting;
   private final NumberSetting blueSetting;

   protected Module(String name, Category category, class_2960 icon) {
      this.smoothX = (float)this.x;
      this.smoothY = (float)this.y;
      this.dragging = false;
      this.velX = 0.0F;
      this.velY = 0.0F;
      this.dragEase = 0.3F;
      this.friction = 0.85F;
      this.resizing = false;
      this.resizeDir = Module.ResizeDir.NONE;
      this.minWidth = 20;
      this.minHeight = 12;
      this.gridSize = 5;
      this.snapToGrid = true;
      this.hudColor = -7643914;
      this.rainbow = false;
      this.rgb = false;
      this.box = true;
      this.outline = false;
      this.glow = false;
      this.vertical = false;
      this.draggable = true;
      this.rainbowSetting = new BooleanSetting("Rainbow", false);
      this.rgbSetting = new BooleanSetting("RGB", false);
      this.boxSetting = new BooleanSetting("Box", true);
      this.outlineSetting = new BooleanSetting("Outline", false);
      this.glowSetting = new BooleanSetting("Glow", false);
      this.verticalSetting = new BooleanSetting("Vertical", false);
      this.draggableSetting = new BooleanSetting("Draggable", true);
      this.scaleSetting = new NumberSetting("Scale", (double)1.0F, (double)0.5F, (double)2.5F, 0.1);
      this.radiusSetting = new NumberSetting("Radius", (double)6.0F, (double)0.0F, (double)20.0F, (double)1.0F);
      this.outlineThicknessSetting = new NumberSetting("Outline Thickness", (double)1.0F, (double)1.0F, (double)6.0F, (double)1.0F);
      this.glowSizeSetting = new NumberSetting("Glow Size", (double)6.0F, (double)0.0F, (double)16.0F, (double)1.0F);
      this.redSetting = new NumberSetting("Red", (double)139.0F, (double)0.0F, (double)255.0F, (double)1.0F);
      this.greenSetting = new NumberSetting("Green", (double)92.0F, (double)0.0F, (double)255.0F, (double)1.0F);
      this.blueSetting = new NumberSetting("Blue", (double)246.0F, (double)0.0F, (double)255.0F, (double)1.0F);
      this.name = name;
      this.category = category;
      this.icon = icon;
      if (this.isHud()) {
         this.addSetting(this.rainbowSetting);
         this.addSetting(this.rgbSetting);
         this.addSetting(this.boxSetting);
         this.addSetting(this.outlineSetting);
         this.addSetting(this.glowSetting);
         this.addSetting(this.verticalSetting);
         this.addSetting(this.draggableSetting);
         this.addSetting(this.scaleSetting);
         this.addSetting(this.radiusSetting);
         this.addSetting(this.outlineThicknessSetting);
         this.addSetting(this.glowSizeSetting);
         this.addSetting(this.redSetting);
         this.addSetting(this.greenSetting);
         this.addSetting(this.blueSetting);
      }

   }

   public String getName() {
      return this.name;
   }

   public Category getCategory() {
      return this.category;
   }

   public class_2960 getIcon() {
      return this.icon;
   }

   public boolean isEnabled() {
      return this.enabled;
   }

   public void setEnabled(boolean enabled) {
      if (this.enabled != enabled) {
         this.enabled = enabled;
         if (enabled) {
            this.onEnable();
         } else {
            this.onDisable();
         }

         this.saveState();
      }
   }

   public void toggle() {
      this.setEnabled(!this.enabled);
   }

   public void onEnable() {
   }

   public void onDisable() {
   }

   public void onTick() {
   }

   public void render(class_332 context) {
   }

   public void addSetting(Setting setting) {
      this.settings.add(setting);
   }

   public List<Setting> getSettings() {
      return this.settings;
   }

   public int getKey() {
      return this.key;
   }

   public void setKey(int key) {
      this.key = key;
      this.saveState();
   }

   public class_304 getKeyBinding() {
      return this.keyBinding;
   }

   public void setKeyBinding(class_304 keyBinding) {
      this.keyBinding = keyBinding;
   }

   public int getX() {
      return this.x;
   }

   public int getY() {
      return this.y;
   }

   public int getRenderX() {
      return Math.round(this.smoothX);
   }

   public int getRenderY() {
      return Math.round(this.smoothY);
   }

   public void setPosition(int x, int y) {
      this.x = x;
      this.y = y;
      this.smoothX = (float)x;
      this.smoothY = (float)y;
      this.saveState();
   }

   public int getWidth() {
      return (int)((float)this.width * this.getScale());
   }

   public int getHeight() {
      return (int)((float)this.height * this.getScale());
   }

   public int getBaseWidth() {
      return this.width;
   }

   public int getBaseHeight() {
      return this.height;
   }

   public void setSize(int width, int height) {
      this.width = Math.max(this.minWidth, width);
      this.height = Math.max(this.minHeight, height);
      this.saveState();
   }

   public void setWidth(int width) {
      this.width = Math.max(this.minWidth, width);
      this.saveState();
   }

   public void setHeight(int height) {
      this.height = Math.max(this.minHeight, height);
      this.saveState();
   }

   public float getScale() {
      return (float)this.scaleSetting.getValue();
   }

   public void setScale(float scale) {
      float clamped = Math.max(0.5F, Math.min(2.5F, scale));
      this.scale = clamped;
      this.scaleSetting.setValue((double)clamped);
      this.saveState();
   }

   public boolean isHovering(double mouseX, double mouseY) {
      return mouseX >= (double)this.getX() && mouseX <= (double)(this.getX() + this.getWidth()) && mouseY >= (double)this.getY() && mouseY <= (double)(this.getY() + this.getHeight());
   }

   public void startDrag(int mouseX, int mouseY) {
      this.startDrag(mouseX, mouseY, false);
   }

   public void startDrag(int mouseX, int mouseY, boolean shiftHeld) {
      if (this.isDraggable()) {
         if (!shiftHeld) {
            selectedModules.clear();
         }

         if (!selectedModules.contains(this)) {
            selectedModules.add(this);
         }

         this.dragging = true;
         this.resizing = false;
         this.resizeDir = Module.ResizeDir.NONE;
         hudEditing = true;
         groupStartX = (float)mouseX;
         groupStartY = (float)mouseY;
         this.dragOffsetX = mouseX - this.getX();
         this.dragOffsetY = mouseY - this.getY();
         this.velX = 0.0F;
         this.velY = 0.0F;
         this.smoothX = (float)this.x;
         this.smoothY = (float)this.y;
      }
   }

   public void stopDrag() {
      this.dragging = false;
      hudEditing = false;
      this.velX = 0.0F;
      this.velY = 0.0F;
      this.smoothX = (float)this.x;
      this.smoothY = (float)this.y;
      this.saveState();
   }

   public void dragTo(int mouseX, int mouseY) {
      if (this.dragging && !this.resizing) {
         if (selectedModules.size() > 1) {
            float dx = (float)mouseX - groupStartX;
            float dy = (float)mouseY - groupStartY;

            for(Module module : selectedModules) {
               if (module != null) {
                  module.x += (int)dx;
                  module.y += (int)dy;
                  module.applySnap();
                  module.smartSnap();
                  module.clampToScreen();
                  module.smoothX = (float)module.x;
                  module.smoothY = (float)module.y;
               }
            }

            groupStartX = (float)mouseX;
            groupStartY = (float)mouseY;
            this.saveState();
         } else {
            int targetX = mouseX - this.dragOffsetX;
            int targetY = mouseY - this.dragOffsetY;
            this.velX = (float)(targetX - this.x) * this.dragEase;
            this.velY = (float)(targetY - this.y) * this.dragEase;
            this.x += Math.round(this.velX);
            this.y += Math.round(this.velY);
            this.applySnap();
            this.smartSnap();
            this.clampToScreen();
            this.smoothX = (float)this.x;
            this.smoothY = (float)this.y;
            this.saveState();
         }
      }
   }

   public void updateDragMotion() {
      if (!this.dragging && !this.resizing) {
         if (Math.abs(this.velX) > 0.001F || Math.abs(this.velY) > 0.001F) {
            this.x += Math.round(this.velX);
            this.y += Math.round(this.velY);
            this.velX *= this.friction;
            this.velY *= this.friction;
            if (Math.abs(this.velX) < 0.02F) {
               this.velX = 0.0F;
            }

            if (Math.abs(this.velY) < 0.02F) {
               this.velY = 0.0F;
            }

            this.applySnap();
            this.smartSnap();
            this.clampToScreen();
         }

         this.smoothX += ((float)this.x - this.smoothX) * 0.25F;
         this.smoothY += ((float)this.y - this.smoothY) * 0.25F;
      } else {
         this.smoothX = (float)this.x;
         this.smoothY = (float)this.y;
      }
   }

   public void applyInertia() {
      this.updateDragMotion();
   }

   public boolean isHoverCorner(double mouseX, double mouseY) {
      return this.getCorner(mouseX, mouseY) != Module.ResizeDir.NONE;
   }

   public ResizeDir getCorner(double mouseX, double mouseY) {
      int x = this.getX();
      int y = this.getY();
      int w = this.getWidth();
      int h = this.getHeight();
      boolean left = mouseX >= (double)x && mouseX <= (double)(x + 8);
      boolean right = mouseX >= (double)(x + w - 8) && mouseX <= (double)(x + w);
      boolean top = mouseY >= (double)y && mouseY <= (double)(y + 8);
      boolean bottom = mouseY >= (double)(y + h - 8) && mouseY <= (double)(y + h);
      if (left && top) {
         return Module.ResizeDir.TL;
      } else if (right && top) {
         return Module.ResizeDir.TR;
      } else if (left && bottom) {
         return Module.ResizeDir.BL;
      } else {
         return right && bottom ? Module.ResizeDir.BR : Module.ResizeDir.NONE;
      }
   }

   public void startResize(ResizeDir dir) {
      if (this.isDraggable()) {
         this.resizing = true;
         this.dragging = false;
         this.resizeDir = dir;
         hudEditing = true;
      }
   }

   public void stopResize() {
      this.resizing = false;
      this.resizeDir = Module.ResizeDir.NONE;
      hudEditing = false;
      this.saveState();
   }

   public void resizeTo(int mouseX, int mouseY) {
      if (this.resizing) {
         int oldX = this.x;
         int oldY = this.y;
         int oldW = this.width;
         int oldH = this.height;
         switch (this.resizeDir.ordinal()) {
            case 1:
               this.setSize(mouseX - oldX, mouseY - oldY);
               break;
            case 2:
               int newW = oldX + oldW - mouseX;
               this.setSize(newW, mouseY - oldY);
               this.x = mouseX;
               break;
            case 3:
               int newH = oldY + oldH - mouseY;
               this.setSize(mouseX - oldX, newH);
               this.y = mouseY;
               break;
            case 4:
               int newW = oldX + oldW - mouseX;
               int newH = oldY + oldH - mouseY;
               this.x = mouseX;
               this.y = mouseY;
               this.setSize(newW, newH);
         }

         this.applySnap();
         this.smartSnap();
         this.clampToScreen();
         this.smoothX = (float)this.x;
         this.smoothY = (float)this.y;
         this.saveState();
      }
   }

   protected void applySnap() {
      if (this.snapToGrid) {
         this.x = Math.round((float)this.x / (float)this.gridSize) * this.gridSize;
         this.y = Math.round((float)this.y / (float)this.gridSize) * this.gridSize;
      }
   }

   public void setGridSize(int gridSize) {
      this.gridSize = Math.max(1, gridSize);
      this.saveState();
   }

   public int getGridSize() {
      return this.gridSize;
   }

   public boolean isSnapToGrid() {
      return this.snapToGrid;
   }

   public void setSnapToGrid(boolean snapToGrid) {
      this.snapToGrid = snapToGrid;
      this.saveState();
   }

   public static void beginSelectionBox(float mouseX, float mouseY) {
      selectingBox = true;
      selStartX = mouseX;
      selStartY = mouseY;
      selEndX = mouseX;
      selEndY = mouseY;
      clearSelection();
   }

   public static void updateSelectionBox(float mouseX, float mouseY) {
      if (selectingBox) {
         selEndX = mouseX;
         selEndY = mouseY;
      }
   }

   public static void finishSelectionBox(List<Module> modules) {
      if (selectingBox) {
         selectedModules.clear();

         for(Module module : modules) {
            if (module != null && module.isInsideSelectionBox()) {
               selectedModules.add(module);
            }
         }

         selectingBox = false;
      }
   }

   public boolean isInsideSelectionBox() {
      float minX = Math.min(selStartX, selEndX);
      float minY = Math.min(selStartY, selEndY);
      float maxX = Math.max(selStartX, selEndX);
      float maxY = Math.max(selStartY, selEndY);
      return (float)this.getX() < maxX && (float)(this.getX() + this.getWidth()) > minX && (float)this.getY() < maxY && (float)(this.getY() + this.getHeight()) > minY;
   }

   public boolean isSelected() {
      return selectedModules.contains(this);
   }

   public static void clearSelection() {
      selectedModules.clear();
   }

   public static void renderSelectionBox(class_332 context) {
      if (selectingBox) {
         int x1 = (int)Math.min(selStartX, selEndX);
         int y1 = (int)Math.min(selStartY, selEndY);
         int x2 = (int)Math.max(selStartX, selEndX);
         int y2 = (int)Math.max(selStartY, selEndY);
         context.method_25294(x1, y1, x2, y1 + 1, 1728053247);
         context.method_25294(x1, y2 - 1, x2, y2, 1728053247);
         context.method_25294(x1, y1, x1 + 1, y2, 1728053247);
         context.method_25294(x2 - 1, y1, x2, y2, 1728053247);
      }
   }

   protected void smartSnap() {
      float snapRange = 6.0F;

      for(Module other : ModuleManager.getModules()) {
         if (other != null && other != this && !selectedModules.contains(other)) {
            float ax = (float)this.x;
            float ay = (float)this.y;
            float aw = (float)this.getWidth();
            float ah = (float)this.getHeight();
            float bx = (float)other.x;
            float by = (float)other.y;
            float bw = (float)other.getWidth();
            float bh = (float)other.getHeight();
            float aCenterX = ax + aw / 2.0F;
            float aCenterY = ay + ah / 2.0F;
            float bCenterX = bx + bw / 2.0F;
            float bCenterY = by + bh / 2.0F;
            if (Math.abs(ax + aw - bx) < snapRange) {
               this.x = Math.round(bx - aw);
            }

            if (Math.abs(ax - (bx + bw)) < snapRange) {
               this.x = Math.round(bx + bw);
            }

            if (Math.abs(ay + ah - by) < snapRange) {
               this.y = Math.round(by - ah);
            }

            if (Math.abs(ay - (by + bh)) < snapRange) {
               this.y = Math.round(by + bh);
            }

            if (Math.abs(aCenterX - bCenterX) < snapRange) {
               this.x = Math.round(bCenterX - aw / 2.0F);
            }

            if (Math.abs(aCenterY - bCenterY) < snapRange) {
               this.y = Math.round(bCenterY - ah / 2.0F);
            }
         }
      }

   }

   protected void clampToScreen() {
      int screenW = HudScaleManager.getVirtualWidth();
      int screenH = HudScaleManager.getVirtualHeight();
      if (screenW > 0 && screenH > 0) {
         int maxX = Math.max(0, screenW - this.getWidth());
         int maxY = Math.max(0, screenH - this.getHeight());
         this.x = Math.max(0, Math.min(this.x, maxX));
         this.y = Math.max(0, Math.min(this.y, maxY));
      }
   }

   public int getHudColor() {
      return this.hudColor;
   }

   public void setHudColor(int color) {
      this.hudColor = color;
      this.redSetting.setValue((double)(color >> 16 & 255));
      this.greenSetting.setValue((double)(color >> 8 & 255));
      this.blueSetting.setValue((double)(color & 255));
      this.rgbSetting.setValue(false);
      this.rainbowSetting.setValue(false);
      this.saveState();
   }

   public boolean isRainbow() {
      return this.rainbowSetting.getValue();
   }

   public void setRainbow(boolean rainbow) {
      this.rainbowSetting.setValue(rainbow);
      if (rainbow) {
         this.rgbSetting.setValue(false);
      }

      this.rainbow = rainbow;
      this.saveState();
   }

   public boolean isRGB() {
      return this.rgbSetting.getValue();
   }

   public boolean isRgb() {
      return this.isRGB();
   }

   public void setRGB(boolean rgb) {
      this.rgbSetting.setValue(rgb);
      if (rgb) {
         this.rainbowSetting.setValue(false);
      }

      this.rgb = rgb;
      this.saveState();
   }

   public boolean isBox() {
      return this.boxSetting.getValue();
   }

   public void setBox(boolean box) {
      this.boxSetting.setValue(box);
      this.box = box;
      this.saveState();
   }

   public boolean isOutline() {
      return this.outlineSetting.getValue();
   }

   public void setOutline(boolean outline) {
      this.outlineSetting.setValue(outline);
      this.outline = outline;
      this.saveState();
   }

   public boolean isGlow() {
      return this.glowSetting.getValue();
   }

   public void setGlow(boolean glow) {
      this.glowSetting.setValue(glow);
      this.glow = glow;
      this.saveState();
   }

   public boolean isVertical() {
      return this.verticalSetting.getValue();
   }

   public void setVertical(boolean vertical) {
      this.verticalSetting.setValue(vertical);
      this.vertical = vertical;
      this.saveState();
   }

   public boolean isDraggable() {
      return this.draggableSetting.getValue();
   }

   public void setDraggable(boolean draggable) {
      this.draggableSetting.setValue(draggable);
      this.draggable = draggable;
      this.saveState();
   }

   public int getRadius() {
      return (int)this.radiusSetting.getValue();
   }

   public int getOutlineThickness() {
      return (int)this.outlineThicknessSetting.getValue();
   }

   public int getGlowSize() {
      return (int)this.glowSizeSetting.getValue();
   }

   public int getRed() {
      return (int)this.redSetting.getValue();
   }

   public int getGreen() {
      return (int)this.greenSetting.getValue();
   }

   public int getBlue() {
      return (int)this.blueSetting.getValue();
   }

   public int getRenderColor() {
      if (this.isRainbow()) {
         float hue = (float)(System.currentTimeMillis() % 2000L) / 2000.0F;
         return Color.HSBtoRGB(hue, 0.85F, 1.0F) | -16777216;
      } else {
         return this.isRGB() ? -16777216 | this.getRed() << 16 | this.getGreen() << 8 | this.getBlue() : this.hudColor;
      }
   }

   protected void drawHUDBase(class_332 context) {
      int color = this.getRenderColor();
      int w = this.getWidth();
      int h = this.getHeight();
      if (this.boxSetting.getValue()) {
         RenderUtils.drawSmoothRect(context, 0, 0, w, h, Math.max(0, this.getRadius()), -1441787107);
      }

      if (this.glowSetting.getValue()) {
         RenderUtils.drawNeonGlow(context, 0, 0, w, h, Math.max(0, this.getGlowSize()), color);
      }

      if (this.outlineSetting.getValue()) {
         RenderUtils.drawOutline(context, 0, 0, w, h, Math.max(0, this.getRadius()), Math.max(1, this.getOutlineThickness()), color);
      }

      if (this.isVertical()) {
         context.method_25294(0, 0, 2, h, color);
      } else {
         context.method_25294(0, 0, w, 2, color);
      }

   }

   private void saveState() {
      ConfigManager.requestSave();
   }

   public boolean shouldBlockClickThrough() {
      return hudEditing;
   }

   public boolean isHud() {
      return this.category == Category.HUD;
   }

   public class_310 getMc() {
      return mc;
   }

   public void setHudPosition(int x, int y) {
      this.x = x;
      this.y = y;
      this.smoothX = (float)x;
      this.smoothY = (float)y;
      this.saveState();
   }

   public void setHudSize(int width, int height) {
      this.width = Math.max(this.minWidth, width);
      this.height = Math.max(this.minHeight, height);
      this.saveState();
   }

   public int getHudX() {
      return this.x;
   }

   public int getHudY() {
      return this.y;
   }

   public void renderAt(class_332 context, int x, int y) {
      int oldX = this.x;
      int oldY = this.y;
      this.x = x;
      this.y = y;
      this.render(context);
      this.x = oldX;
      this.y = oldY;
   }

   protected static enum ResizeDir {
      NONE,
      BR,
      BL,
      TR,
      TL;

      // $FF: synthetic method
      private static ResizeDir[] $values() {
         return new ResizeDir[]{NONE, BR, BL, TR, TL};
      }
   }
}
