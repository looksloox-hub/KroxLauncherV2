package com.example.client.mixin;

import com.example.client.module.impl.AntiOverlayModule;
import net.minecraft.class_1058;
import net.minecraft.class_310;
import net.minecraft.class_4587;
import net.minecraft.class_4597;
import net.minecraft.class_4603;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({class_4603.class})
public class InGameOverlayRendererMixin {
   @Inject(
      method = {"renderFireOverlay"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private static void cancelFire(class_4587 matrices, class_4597 vertexConsumers, CallbackInfo ci) {
      if (AntiOverlayModule.enabled()) {
         ci.cancel();
      }

   }

   @Inject(
      method = {"renderUnderwaterOverlay"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private static void cancelWater(class_310 client, class_4587 matrices, class_4597 vertexConsumers, CallbackInfo ci) {
      if (AntiOverlayModule.enabled()) {
         ci.cancel();
      }

   }

   @Inject(
      method = {"renderInWallOverlay"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private static void cancelPumpkin(class_1058 sprite, class_4587 matrices, class_4597 vertexConsumers, CallbackInfo ci) {
      if (AntiOverlayModule.enabled()) {
         ci.cancel();
      }

   }
}
