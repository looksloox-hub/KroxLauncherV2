package com.example.client.mixin;

import com.example.client.performance.PerformanceEngine;
import net.minecraft.class_243;
import net.minecraft.class_2586;
import net.minecraft.class_827;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({class_827.class})
public interface BlockEntityCullingMixin<T extends class_2586, S> {
   @Inject(
      method = {"isInRenderDistance"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void optix$cullBlockEntity(T blockEntity, class_243 cameraPos, CallbackInfoReturnable<Boolean> cir) {
      if (blockEntity != null) {
         double dx = (double)blockEntity.method_11016().method_10263() + (double)0.5F - cameraPos.field_1352;
         double dy = (double)blockEntity.method_11016().method_10264() + (double)0.5F - cameraPos.field_1351;
         double dz = (double)blockEntity.method_11016().method_10260() + (double)0.5F - cameraPos.field_1350;
         double distSq = dx * dx + dy * dy + dz * dz;
         if (distSq > (double)PerformanceEngine.getBlockEntityCullDistanceSq()) {
            cir.setReturnValue(false);
         }

      }
   }
}
