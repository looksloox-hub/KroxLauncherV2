package com.example.client.mixin;

import com.example.client.performance.PerformanceEngine;
import net.minecraft.class_1297;
import net.minecraft.class_4604;
import net.minecraft.class_897;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({class_897.class})
public abstract class EntityCullingMixin<T extends class_1297> {
   @Inject(
      method = {"shouldRender"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void optix$cull(T entity, class_4604 frustum, double x, double y, double z, CallbackInfoReturnable<Boolean> cir) {
      if (entity != null) {
         double distSq = entity.method_5649(x, y, z);
         if (distSq > (double)PerformanceEngine.getEntityCullDistanceSq()) {
            cir.setReturnValue(false);
         }

      }
   }
}
