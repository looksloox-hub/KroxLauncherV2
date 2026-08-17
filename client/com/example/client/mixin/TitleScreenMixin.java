package com.example.client.mixin;

import com.example.client.account.AccountSwitcherScreen;
import com.example.client.ui.ModernClickGUI;
import com.example.client.ui.render.GlassRenderer;
import com.example.client.ui.render.RoundedRectRenderer;
import com.example.client.ui.theme.GlassTheme;
import com.example.client.ui.theme.GlassThemeManager;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.class_1068;
import net.minecraft.class_1109;
import net.minecraft.class_11909;
import net.minecraft.class_2561;
import net.minecraft.class_310;
import net.minecraft.class_332;
import net.minecraft.class_3417;
import net.minecraft.class_429;
import net.minecraft.class_437;
import net.minecraft.class_442;
import net.minecraft.class_500;
import net.minecraft.class_526;
import net.minecraft.class_7532;
import net.minecraft.class_8685;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({class_442.class})
public class TitleScreenMixin {
   @Unique
   private float singleAnim = 0.0F;
   @Unique
   private float multiAnim = 0.0F;
   @Unique
   private float modsAnim = 0.0F;
   @Unique
   private float partnerAnim = 0.0F;
   @Unique
   private float quitAnim = 0.0F;
   @Unique
   private boolean open = false;
   @Unique
   private float openAnim = 0.0F;
   @Unique
   private float hoverAnim = 0.0F;
   @Unique
   private float pressAnim = 0.0F;
   @Unique
   private UUID cachedUuid = null;
   @Unique
   private class_8685 cachedTextures = null;
   @Unique
   private float guiHoverAnim = 0.0F;
   @Unique
   private float guiOpenAnim = 0.0F;
   @Unique
   private float settingsAnim = 0.0F;
   @Unique
   private float flashbackAnim = 0.0F;
   @Unique
   private float replayAnim = 0.0F;
   @Unique
   private float switchAnim = 0.0F;
   @Unique
   private float addAnim = 0.0F;
   @Unique
   private float managerAnim = 0.0F;
   @Unique
   private Integer savedGuiScale = null;
   @Unique
   private boolean titleScaleForced = false;

   @Unique
   private boolean inside(double mx, double my, float x, float y, float w, float h) {
      return mx >= (double)x && mx <= (double)(x + w) && my >= (double)y && my <= (double)(y + h);
   }

   @Unique
   private float clamp01(float v) {
      if (v <= 0.0F) {
         return 0.0F;
      } else {
         return v >= 1.0F ? 1.0F : v;
      }
   }

   @Unique
   private float easeOutCubic(float t) {
      t = this.clamp01(t);
      float p = 1.0F - t;
      return 1.0F - p * p * p;
   }

   @Unique
   private float easeOutBack(float t) {
      t = this.clamp01(t);
      float c1 = 1.70158F;
      float c3 = c1 + 1.0F;
      float p = t - 1.0F;
      return 1.0F + c3 * p * p * p + c1 * p * p;
   }

   @Unique
   private float animate(float current, boolean active, float inSpeed, float outSpeed) {
      if (active) {
         current += (1.0F - current) * inSpeed;
         if (current > 0.995F) {
            current = 1.0F;
         }
      } else {
         current -= current * outSpeed;
         if (current < 0.005F) {
            current = 0.0F;
         }
      }

      return current;
   }

   @Unique
   private int withAlpha(int color, int alpha) {
      alpha = Math.max(0, Math.min(255, alpha));
      return alpha << 24 | color & 16777215;
   }

   @Unique
   private void playClick(class_310 mc) {
      try {
         mc.method_1483().method_4873(class_1109.method_47978(class_3417.field_15015, 1.0F));
      } catch (Throwable var3) {
      }

   }

   @Unique
   private void forceTitleGuiScale(class_310 mc) {
      if (mc != null && mc.field_1690 != null) {
         if (!this.titleScaleForced) {
            try {
               this.savedGuiScale = (Integer)mc.field_1690.method_42474().method_41753();
            } catch (Throwable var4) {
               this.savedGuiScale = null;
            }

            this.titleScaleForced = true;
         }

         try {
            mc.field_1690.method_42474().method_41748(2);
         } catch (Throwable var3) {
         }

      }
   }

   @Unique
   private void restoreTitleGuiScale(class_310 mc) {
      if (mc != null && mc.field_1690 != null && this.titleScaleForced) {
         try {
            if (this.savedGuiScale != null) {
               mc.field_1690.method_42474().method_41748(this.savedGuiScale);
            }
         } catch (Throwable var3) {
         }

         this.titleScaleForced = false;
         this.savedGuiScale = null;
      }
   }

   @Unique
   private void openScreenAndRestore(class_310 mc, class_437 next) {
      this.restoreTitleGuiScale(mc);
      mc.method_1507(next);
   }

   @Unique
   private boolean hasMod(String modId) {
      try {
         return FabricLoader.getInstance().isModLoaded(modId);
      } catch (Throwable var3) {
         return false;
      }
   }

   @Unique
   private void openScreenFromCandidates(class_310 mc, String... classNames) {
      this.restoreTitleGuiScale(mc);

      for(String className : classNames) {
         try {
            Class<?> raw = Class.forName(className);

            try {
               Object instance = raw.getConstructor(class_437.class).newInstance((class_437)this);
               if (instance instanceof class_437 screen) {
                  mc.method_1507(screen);
                  return;
               }
            } catch (NoSuchMethodException var11) {
            }

            try {
               Object instance = raw.getDeclaredConstructor().newInstance();
               if (instance instanceof class_437 screen) {
                  mc.method_1507(screen);
                  return;
               }
            } catch (Throwable var10) {
            }
         } catch (Throwable var12) {
         }
      }

   }

   @Unique
   private void openFlashbackScreen(class_310 mc) {
      this.openScreenFromCandidates(mc, "com.moulberry.flashback.screen.select_replay.SelectReplayScreen", "com.moulberry.flashback.screen.SelectReplayScreen", "com.moulberry.flashback.screen.select_replay.ReplaySelectionScreen", "com.moulberry.flashback.gui.FlashbackScreen", "com.moulberry.flashback.client.gui.FlashbackScreen");
   }

   @Unique
   private void openReplayScreen(class_310 mc) {
      this.openScreenFromCandidates(mc, "com.replaymod.replay.gui.screen.GuiReplayViewer", "com.replaymod.replay.gui.screen.SelectReplayScreen", "com.replaymod.replay.gui.screen.ReplayViewerScreen", "replaymod.gui.replayviewer", "com.replaymod.replay.gui.screen.GuiReplayBrowser", "com.replaymod.replay.gui.screen.GuiReplayViewerScreen");
   }

   @Unique
   private void drawMainButton(class_332 ctx, class_310 mc, int x, int y, int w, int h, String text, boolean hover, float anim) {
      float expand = anim * 6.0F;
      float lift = anim * 3.0F;
      float drawX = (float)x - expand * 0.5F;
      float drawY = (float)y - lift;
      float drawW = (float)w + expand;
      float drawH = (float)h;
      GlassRenderer.drawButton(ctx, drawX, drawY, drawW, drawH, text, hover, false);
   }

   @Unique
   private void drawSquareIconButton(class_332 ctx, class_310 mc, float x, float y, float size, String label, float anim) {
      GlassTheme theme = GlassThemeManager.active();
      float expand = anim * 2.0F;
      float lift = anim * 1.0F;
      float drawX = x - expand * 0.5F;
      float drawY = y - lift;
      float drawS = size + expand;
      int outer = anim > 0.01F ? theme.borderStrong() : theme.border();
      int inner = theme.button(anim > 0.01F, false);
      RoundedRectRenderer.outline(ctx, drawX, drawY, drawS, drawS, 8.0F, 1.0F, outer, inner);
      if (label != null && !label.isEmpty()) {
         int textW = mc.field_1772.method_1727(label);
         int textX = Math.round(drawX + drawS * 0.5F - (float)textW * 0.5F);
         int textY = Math.round(drawY + drawS * 0.5F - 4.0F);
         ctx.method_51439(mc.field_1772, class_2561.method_43470(label), textX, textY, theme.text(), false);
      }

   }

   @Unique
   private void drawAnimatedAccountButton(class_332 ctx, class_310 mc, float x, float y, float w, float h, boolean hovered) {
      GlassTheme theme = GlassThemeManager.active();
      float bgHover = this.hoverAnim;
      float hoverLift = bgHover * 2.5F;
      float hoverExpand = bgHover * 3.0F;
      float drawX = x - hoverExpand * 0.5F - bgHover * 1.0F;
      float drawY = y - hoverLift;
      float drawW = w + hoverExpand;
      float drawH = h + bgHover * 1.5F;
      int outer = bgHover > 0.01F ? theme.borderStrong() : theme.border();
      int inner = theme.button(hovered, false);
      RoundedRectRenderer.outline(ctx, drawX, drawY, drawW, drawH, 10.0F, 1.0F, outer, inner);
      float contentShiftX = bgHover * 2.0F;
      float contentShiftY = 0.0F;
      int avatarSize = 18;
      int avatarX = Math.round(drawX + 12.0F + contentShiftX);
      int avatarY = Math.round(drawY + (drawH - (float)avatarSize) * 0.5F + contentShiftY);
      String name = mc.method_1548().method_1676();
      int nameX = Math.round(drawX + 38.0F + contentShiftX);
      int nameY = Math.round(drawY + (drawH - 8.0F) * 0.5F + 1.0F + contentShiftY);
      int arrowX = Math.round(drawX + drawW - 16.0F);

      try {
         class_8685 textures = this.getCachedTextures(mc);
         class_7532.method_52722(ctx, textures, avatarX, avatarY, avatarSize);
      } catch (Throwable var29) {
         String letter = name.isBlank() ? "?" : name.substring(0, 1).toUpperCase();
         ctx.method_51439(mc.field_1772, class_2561.method_43470(letter), avatarX + 6, avatarY + 4, theme.text(), false);
      }

      ctx.method_51439(mc.field_1772, class_2561.method_43470(name), nameX, nameY, theme.text(), false);
      ctx.method_51439(mc.field_1772, class_2561.method_43470(this.open ? "v" : ">"), arrowX, nameY, -4603700, false);
   }

   @Unique
   private void drawDropdownRowAnimated(class_332 ctx, class_310 mc, float x, float y, float w, float h, String text, boolean hovered, float openProgress, float hoverProgress) {
      GlassTheme theme = GlassThemeManager.active();
      float appear = this.easeOutBack(openProgress);
      float slideUp = (1.0F - appear) * 8.0F;
      float hoverLift = hoverProgress * 1.2F;
      float hoverExpand = hoverProgress * 2.5F;
      float drawX = x - hoverExpand * 0.5F;
      float drawY = y - slideUp - hoverLift * 0.5F;
      float drawW = w + hoverExpand;
      float drawH = h + hoverProgress * 1.0F;
      int outer = hovered ? theme.borderStrong() : theme.border();
      int inner = theme.button(hovered, false);
      RoundedRectRenderer.outline(ctx, drawX, drawY, drawW, drawH, 8.0F, 1.0F, outer, inner);
      int textAlpha = (int)(255.0F * appear);
      int textColor = this.withAlpha(theme.text(), textAlpha);
      int textX = Math.round(drawX + 10.0F + hoverProgress * 1.0F);
      int textY = Math.round(drawY + (drawH - 8.0F) * 0.5F + 1.0F);
      ctx.method_51439(mc.field_1772, class_2561.method_43470(text), textX, textY, textColor, false);
   }

   @Inject(
      method = {"init"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void forceScaleOnInit(CallbackInfo ci) {
      class_310 mc = class_310.method_1551();
      this.forceTitleGuiScale(mc);
      ci.cancel();
   }

   @Inject(
      method = {"removed"},
      at = {@At("HEAD")}
   )
   private void onRemove(CallbackInfo ci) {
      this.restoreTitleGuiScale(class_310.method_1551());
   }

   @Inject(
      method = {"render"},
      at = {@At("TAIL")}
   )
   private void renderMainMenuButtons(class_332 ctx, int mouseX, int mouseY, float delta, CallbackInfo ci) {
      class_310 mc = class_310.method_1551();
      int realSw = mc.method_22683().method_4486();
      int realSh = mc.method_22683().method_4502();
      int buttonW = 220;
      int buttonH = 24;
      int gap = 4;
      int x = realSw / 2 - buttonW / 2;
      int y = realSh / 2 - 95;
      boolean singleHover = this.inside((double)mouseX, (double)mouseY, (float)x, (float)y, (float)buttonW, (float)buttonH);
      this.singleAnim += ((singleHover ? 1.0F : 0.0F) - this.singleAnim) * 0.15F;
      this.drawMainButton(ctx, mc, x, y, buttonW, buttonH, "Singleplayer", singleHover, this.singleAnim);
      boolean multiHover = this.inside((double)mouseX, (double)mouseY, (float)x, (float)(y + buttonH + gap), (float)buttonW, (float)buttonH);
      this.multiAnim += ((multiHover ? 1.0F : 0.0F) - this.multiAnim) * 0.15F;
      this.drawMainButton(ctx, mc, x, y + buttonH + gap, buttonW, buttonH, "Multiplayer", multiHover, this.multiAnim);
      int currentY = y + (buttonH + gap) * 2;
      if (FabricLoader.getInstance().isModLoaded("modmenu")) {
         boolean modsHover = this.inside((double)mouseX, (double)mouseY, (float)x, (float)currentY, (float)buttonW, (float)buttonH);
         this.modsAnim += ((modsHover ? 1.0F : 0.0F) - this.modsAnim) * 0.15F;
         this.drawMainButton(ctx, mc, x, currentY, buttonW, buttonH, "Mods", modsHover, this.modsAnim);
         currentY += buttonH + gap;
      }

      int halfW = (buttonW - 4) / 2;
      boolean partnerHover = this.inside((double)mouseX, (double)mouseY, (float)x, (float)currentY, (float)halfW, (float)buttonH);
      this.partnerAnim += ((partnerHover ? 1.0F : 0.0F) - this.partnerAnim) * 0.15F;
      this.drawMainButton(ctx, mc, x, currentY, halfW, buttonH, "Partnerships", partnerHover, this.partnerAnim);
      boolean quitHover = this.inside((double)mouseX, (double)mouseY, (float)(x + halfW + 4), (float)currentY, (float)halfW, (float)buttonH);
      this.quitAnim += ((quitHover ? 1.0F : 0.0F) - this.quitAnim) * 0.15F;
      this.drawMainButton(ctx, mc, x + halfW + 4, currentY, halfW, buttonH, "Quit", quitHover, this.quitAnim);
   }

   @Inject(
      method = {"render"},
      at = {@At("TAIL")}
   )
   private void renderAccountWidget(class_332 ctx, int mouseX, int mouseY, float delta, CallbackInfo ci) {
      class_310 mc = class_310.method_1551();
      if (mc.method_1548() != null) {
         int sw = mc.method_22683().method_4486();
         int w = 220;
         int h = 30;
         int utilityCount = 2;
         if (this.hasMod("flashback")) {
            ++utilityCount;
         }

         if (this.hasMod("replaymod")) {
            ++utilityCount;
         }

         int reservedButtonSpace = utilityCount * 30 + Math.max(0, utilityCount - 1) * 4;
         int x = sw - w - reservedButtonSpace - 8;
         int y = 10;
         int guiButtonSize = 30;
         int utilityX = x + w + 4;
         boolean flashbackShown = this.hasMod("flashback");
         boolean replayShown = this.hasMod("replaymod");
         if (flashbackShown) {
            boolean flashbackHovered = this.inside((double)mouseX, (double)mouseY, (float)utilityX, (float)y, (float)guiButtonSize, (float)guiButtonSize);
            this.flashbackAnim = this.animate(this.flashbackAnim, flashbackHovered, 0.2F, 0.4F);
            this.drawSquareIconButton(ctx, mc, (float)utilityX, (float)y, (float)guiButtonSize, "◀", this.flashbackAnim);
            utilityX += guiButtonSize + 4;
         }

         if (replayShown) {
            boolean replayHovered = this.inside((double)mouseX, (double)mouseY, (float)utilityX, (float)y, (float)guiButtonSize, (float)guiButtonSize);
            this.replayAnim = this.animate(this.replayAnim, replayHovered, 0.2F, 0.4F);
            this.drawSquareIconButton(ctx, mc, (float)utilityX, (float)y, (float)guiButtonSize, "◀", this.replayAnim);
            utilityX += guiButtonSize + 4;
         }

         boolean settingsHovered = this.inside((double)mouseX, (double)mouseY, (float)utilityX, (float)y, (float)guiButtonSize, (float)guiButtonSize);
         this.settingsAnim = this.animate(this.settingsAnim, settingsHovered, 0.2F, 0.4F);
         this.drawSquareIconButton(ctx, mc, (float)utilityX, (float)y, (float)guiButtonSize, "⌘", this.settingsAnim);
         utilityX += guiButtonSize + 4;
         boolean guiHovered = this.inside((double)mouseX, (double)mouseY, (float)utilityX, (float)y, (float)guiButtonSize, (float)guiButtonSize);
         this.guiHoverAnim = this.animate(this.guiHoverAnim, guiHovered, 0.2F, 0.4F);
         this.drawSquareIconButton(ctx, mc, (float)utilityX, (float)y, (float)guiButtonSize, "≡", this.guiHoverAnim);
         String name = mc.method_1548().method_1676();
         if (name != null) {
            float hoverExpand = this.hoverAnim * 3.0F;
            float hoverLift = this.hoverAnim * 2.5F;
            boolean hovered = this.inside((double)mouseX, (double)mouseY, (float)x - hoverExpand * 0.5F, (float)y - hoverLift, (float)w + hoverExpand, (float)h + this.hoverAnim * 1.5F);
            if (hovered) {
               this.hoverAnim += (1.0F - this.hoverAnim) * 0.22F;
               if (this.hoverAnim > 0.999F) {
                  this.hoverAnim = 1.0F;
               }
            } else {
               this.hoverAnim -= this.hoverAnim * 0.4F;
               if (this.hoverAnim < 0.01F) {
                  this.hoverAnim = 0.0F;
               }
            }

            this.openAnim = this.animate(this.openAnim, this.open, 0.16F, 0.16F);
            this.pressAnim *= 0.88F;
            int inset = (int)(this.hoverAnim * 1.2F);
            this.drawAnimatedAccountButton(ctx, mc, (float)(x - inset), (float)(y - inset), (float)w + (float)inset * 2.0F, (float)h + (float)inset * 2.0F, hovered);
            int dropFullH = 98;
            float panelProgress = this.easeOutCubic(this.openAnim);
            int dropH = (int)(panelProgress * (float)dropFullH);
            if (dropH > 2) {
               int dropY = y + h + 4;
               float panelAlpha = this.clamp01(panelProgress);
               RoundedRectRenderer.fill(ctx, (float)x, (float)dropY, (float)w, (float)dropH, 10.0F, this.withAlpha(GlassThemeManager.active().panel(), (int)(220.0F * panelAlpha)));
               RoundedRectRenderer.outline(ctx, (float)x, (float)dropY, (float)w, (float)dropH, 10.0F, 1.0F, this.withAlpha(GlassThemeManager.active().border(), (int)(255.0F * panelAlpha)), this.withAlpha(GlassThemeManager.active().panel(), (int)(220.0F * panelAlpha)));
               int rowX = x + 6;
               int rowW = w - 12;
               float r1p = this.clamp01((panelProgress - 0.0F) / 0.45F);
               float r2p = this.clamp01((panelProgress - 0.08F) / 0.45F);
               float r3p = this.clamp01((panelProgress - 0.16F) / 0.45F);
               boolean r1 = this.inside((double)mouseX, (double)mouseY, (float)rowX, (float)(dropY + 6), (float)rowW, 22.0F);
               boolean r2 = this.inside((double)mouseX, (double)mouseY, (float)rowX, (float)(dropY + 38), (float)rowW, 22.0F);
               boolean r3 = this.inside((double)mouseX, (double)mouseY, (float)rowX, (float)(dropY + 70), (float)rowW, 22.0F);
               this.switchAnim = this.animate(this.switchAnim, r1, 0.22F, 0.45F);
               this.addAnim = this.animate(this.addAnim, r2, 0.22F, 0.45F);
               this.managerAnim = this.animate(this.managerAnim, r3, 0.22F, 0.45F);
               this.drawDropdownRowAnimated(ctx, mc, (float)rowX, (float)(dropY + 6), (float)rowW, 22.0F, "Switch Account", r1, r1p, this.switchAnim);
               this.drawDropdownRowAnimated(ctx, mc, (float)rowX, (float)(dropY + 38), (float)rowW, 22.0F, "Add Account", r2, r2p, this.addAnim);
               this.drawDropdownRowAnimated(ctx, mc, (float)rowX, (float)(dropY + 70), (float)rowW, 22.0F, "Open Account Manager", r3, r3p, this.managerAnim);
            }

         }
      }
   }

   @Inject(
      method = {"mouseClicked"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void onClick(class_11909 click, boolean doubled, CallbackInfoReturnable<Boolean> cir) {
      class_310 mc = class_310.method_1551();
      if (mc.method_1548() != null) {
         double mouseX = click.comp_4798();
         double mouseY = click.comp_4799();
         int sw = mc.method_22683().method_4486();
         int sh = mc.method_22683().method_4502();
         int buttonW = 220;
         int buttonH = 24;
         int gap = 4;
         int x = sw / 2 - buttonW / 2;
         int y = sh / 2 - 95;
         if (this.inside(mouseX, mouseY, (float)x, (float)y, (float)buttonW, (float)buttonH)) {
            this.playClick(mc);
            this.openScreenAndRestore(mc, new class_526((class_437)this));
            cir.setReturnValue(true);
         } else if (this.inside(mouseX, mouseY, (float)x, (float)(y + buttonH + gap), (float)buttonW, (float)buttonH)) {
            this.playClick(mc);
            this.openScreenAndRestore(mc, new class_500((class_437)this));
            cir.setReturnValue(true);
         } else {
            int currentY = y + (buttonH + gap) * 2;
            if (FabricLoader.getInstance().isModLoaded("modmenu")) {
               if (this.inside(mouseX, mouseY, (float)x, (float)currentY, (float)buttonW, (float)buttonH)) {
                  this.playClick(mc);

                  try {
                     Class<?> clazz = Class.forName("com.terraformersmc.modmenu.gui.ModsScreen");
                     Object screen = clazz.getConstructor(class_437.class).newInstance((class_437)this);
                     if (screen instanceof class_437) {
                        class_437 s = (class_437)screen;
                        this.openScreenAndRestore(mc, s);
                     }
                  } catch (Throwable var40) {
                  }

                  cir.setReturnValue(true);
                  return;
               }

               currentY += buttonH + gap;
            }

            int halfW = (buttonW - 4) / 2;
            if (this.inside(mouseX, mouseY, (float)x, (float)currentY, (float)halfW, (float)buttonH)) {
               this.playClick(mc);
               cir.setReturnValue(true);
            } else if (this.inside(mouseX, mouseY, (float)(x + halfW + 4), (float)currentY, (float)halfW, (float)buttonH)) {
               this.playClick(mc);
               this.restoreTitleGuiScale(mc);
               mc.method_1592();
               cir.setReturnValue(true);
            } else {
               int utilityCount = 2;
               if (FabricLoader.getInstance().isModLoaded("flashback")) {
                  ++utilityCount;
               }

               if (FabricLoader.getInstance().isModLoaded("replaymod")) {
                  ++utilityCount;
               }

               int reservedButtonSpace = utilityCount * 30 + Math.max(0, utilityCount - 1) * 4;
               int accountX = sw - buttonW - reservedButtonSpace - 8;
               int accountY = 10;
               int guiButtonSize = 30;
               int utilityX = accountX + buttonW + 4;
               boolean flashbackShown = FabricLoader.getInstance().isModLoaded("flashback");
               boolean replayShown = FabricLoader.getInstance().isModLoaded("replaymod");
               if (flashbackShown) {
                  if (this.inside(mouseX, mouseY, (float)utilityX, (float)accountY, (float)guiButtonSize, (float)guiButtonSize)) {
                     this.playClick(mc);
                     this.openFlashbackScreen(mc);
                     cir.setReturnValue(true);
                     return;
                  }

                  utilityX += guiButtonSize + 4;
               }

               if (replayShown) {
                  if (this.inside(mouseX, mouseY, (float)utilityX, (float)accountY, (float)guiButtonSize, (float)guiButtonSize)) {
                     this.playClick(mc);
                     this.openReplayScreen(mc);
                     cir.setReturnValue(true);
                     return;
                  }

                  utilityX += guiButtonSize + 4;
               }

               if (this.inside(mouseX, mouseY, (float)utilityX, (float)accountY, (float)guiButtonSize, (float)guiButtonSize)) {
                  this.playClick(mc);
                  this.openScreenAndRestore(mc, new class_429((class_437)this, mc.field_1690));
                  cir.setReturnValue(true);
               } else {
                  utilityX += guiButtonSize + 4;
                  if (this.inside(mouseX, mouseY, (float)utilityX, (float)accountY, (float)guiButtonSize, (float)guiButtonSize)) {
                     this.playClick(mc);
                     this.openScreenAndRestore(mc, new ModernClickGUI());
                     cir.setReturnValue(true);
                  } else {
                     int w = 220;
                     int h = 30;
                     boolean insideMain = this.inside(mouseX, mouseY, (float)accountX, (float)accountY, (float)w, (float)h);
                     if (insideMain) {
                        this.open = !this.open;
                        this.pressAnim = 1.0F;
                        this.playClick(mc);
                        cir.setReturnValue(true);
                     } else if (this.open) {
                        int dropFullH = 98;
                        int dropH = (int)(this.openAnim * (float)dropFullH);
                        if (dropH > 2) {
                           int dropY = accountY + h + 4;
                           int rowX = accountX + 6;
                           int rowW = w - 12;
                           boolean switchAccount = this.inside(mouseX, mouseY, (float)rowX, (float)(dropY + 6), (float)rowW, 22.0F);
                           boolean addAccount = this.inside(mouseX, mouseY, (float)rowX, (float)(dropY + 38), (float)rowW, 22.0F);
                           boolean openManager = this.inside(mouseX, mouseY, (float)rowX, (float)(dropY + 70), (float)rowW, 22.0F);
                           if (switchAccount) {
                              this.open = false;
                              this.playClick(mc);
                              this.restoreTitleGuiScale(mc);
                              class_437 parent = (class_437)this;
                              mc.method_1507(new AccountSwitcherScreen(parent));
                              cir.setReturnValue(true);
                           } else if (addAccount) {
                              this.open = false;
                              this.playClick(mc);
                              cir.setReturnValue(true);
                           } else {
                              if (openManager) {
                                 this.open = false;
                                 this.playClick(mc);
                                 this.restoreTitleGuiScale(mc);
                                 class_437 parent = (class_437)this;
                                 mc.method_1507(new AccountSwitcherScreen(parent));
                                 cir.setReturnValue(true);
                              }

                           }
                        }
                     }
                  }
               }
            }
         }
      }
   }

   @Unique
   private class_8685 getCachedTextures(class_310 mc) {
      String name = mc.method_1548().method_1676();
      UUID uuid = mc.method_1548().method_44717();
      if (uuid == null) {
         uuid = UUID.nameUUIDFromBytes(("OfflinePlayer:" + name).getBytes(StandardCharsets.UTF_8));
      }

      if (this.cachedUuid == null || !this.cachedUuid.equals(uuid)) {
         this.cachedUuid = uuid;
         this.cachedTextures = class_1068.method_4648(uuid);
      }

      if (this.cachedTextures == null) {
         this.cachedTextures = class_1068.method_4648(uuid);
      }

      return this.cachedTextures;
   }
}
