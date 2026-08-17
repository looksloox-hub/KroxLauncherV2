package com.example.client.mixin;

import com.example.client.module.ModuleManager;
import com.example.client.ui.HudEditorScreen;
import com.example.client.ui.HudScaleManager;
import net.minecraft.class_310;
import net.minecraft.class_329;
import net.minecraft.class_332;
import net.minecraft.class_9779;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({class_329.class})
public abstract class HudMixin {
   @Inject(
      method = {"render"},
      at = {@At("TAIL")}
   )
   private void renderAllModules(class_332 context, class_9779 tickCounter, CallbackInfo ci) {
      class_310 mc = class_310.method_1551();
      if (mc == null || !(mc.field_1755 instanceof HudEditorScreen)) {
         HudScaleManager.begin(context);

         try {
            ModuleManager.onRender(context);
         } finally {
            HudScaleManager.end(context);
         }

      }
   }

   @Inject(
      method = {"renderScoreboardSidebar"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void hideVanillaScoreboard(class_332 context, class_9779 tickCounter, CallbackInfo ci) {
      if (ModuleManager.isEnabled("Scoreboard")) {
         ci.cancel();
      }

   }
}
