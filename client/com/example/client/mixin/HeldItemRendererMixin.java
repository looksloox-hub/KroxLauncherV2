package com.example.client.mixin;

import com.example.client.module.ModuleManager;
import com.example.client.module.impl.ViewModelModule;
import net.minecraft.class_1268;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_310;
import net.minecraft.class_4587;
import net.minecraft.class_759;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({class_759.class})
public class HeldItemRendererMixin {
   @Inject(
      method = {"renderFirstPersonItem"},
      at = {@At("HEAD")}
   )
   private void viewModel(class_4587 matrices, float tickDelta, float pitch, class_1268 hand, float swingProgress, class_1799 stack, float equipProgress, CallbackInfo ci) {
      class_310 mc = class_310.method_1551();
      ViewModelModule vm = (ViewModelModule)ModuleManager.getModule(ViewModelModule.class);
      if (vm != null && vm.isEnabled() && mc.field_1724 != null) {
         boolean holdingShield = hand == class_1268.field_5810 && stack.method_31574(class_1802.field_8255) && mc.field_1724.method_6115() && mc.field_1724.method_6079().method_31574(class_1802.field_8255);
         if (holdingShield) {
            float x = (float)vm.offsetX.getValue();
            float y = (float)vm.offsetY.getValue();
            float z = (float)vm.offsetZ.getValue();
            float s = (float)vm.scale.getValue();
            matrices.method_46416(x, y, z);
            matrices.method_22905(s, s, s);
         }
      }
   }
}
