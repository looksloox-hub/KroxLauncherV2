package com.example.client.mixin;

import com.example.client.module.ModuleManager;
import com.example.client.module.impl.CrosshairIndicatorModule;
import com.example.client.module.impl.CrosshairStyleModule;
import net.minecraft.class_329;
import net.minecraft.class_332;
import net.minecraft.class_9779;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({class_329.class})
public class InGameHudMixin {
   @Inject(
      method = {"renderCrosshair"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void cancelCrosshair(class_332 context, class_9779 tickCounter, CallbackInfo ci) {
      CrosshairIndicatorModule indicator = (CrosshairIndicatorModule)ModuleManager.getModule(CrosshairIndicatorModule.class);
      CrosshairStyleModule style = (CrosshairStyleModule)ModuleManager.getModule(CrosshairStyleModule.class);
      boolean customCrosshairEnabled = indicator != null && indicator.isEnabled() || style != null && style.isEnabled();
      if (customCrosshairEnabled) {
         ci.cancel();
      }

   }
}
