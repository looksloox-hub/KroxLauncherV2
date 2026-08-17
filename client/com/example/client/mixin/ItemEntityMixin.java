package com.example.client.mixin;

import net.minecraft.class_1542;
import net.minecraft.class_1657;
import net.minecraft.class_1799;
import net.minecraft.class_1937;
import net.minecraft.class_238;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({class_1542.class})
public class ItemEntityMixin {
   @Unique
   private int optix$tickSkip;
   @Unique
   private int optix$mergeTick;

   @Inject(
      method = {"tick"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void optix$fpsOptimize(CallbackInfo ci) {
      class_1542 item = (class_1542)this;
      class_1937 world = item.method_73183();
      if (!world.method_8608()) {
         if (!world.method_22340(item.method_24515())) {
            ci.cancel();
         } else {
            class_1657 player = world.method_18460(item, (double)64.0F);
            if (player != null) {
               double distSq = item.method_5858(player);
               if (distSq > (double)4096.0F && this.optix$tickSkip++ % 4 != 0) {
                  ci.cancel();
               } else {
                  if (this.optix$mergeTick++ % 20 == 0) {
                     class_1799 stack = item.method_6983();
                     if (stack.method_7960()) {
                        return;
                     }

                     class_238 box = item.method_5829().method_1014(0.35);

                     for(class_1542 other : world.method_8390(class_1542.class, box, (otherx) -> otherx != item && !otherx.method_31481())) {
                        class_1799 otherStack = other.method_6983();
                        if (class_1799.method_7984(stack, otherStack)) {
                           int space = otherStack.method_7914() - otherStack.method_7947();
                           if (space > 0) {
                              int move = Math.min(space, stack.method_7947());
                              otherStack.method_7933(move);
                              stack.method_7934(move);
                              if (stack.method_7960()) {
                                 item.method_31472();
                                 ci.cancel();
                                 return;
                              }
                           }
                        }
                     }
                  }

                  if (distSq > (double)2304.0F && this.optix$tickSkip++ % 2 == 0) {
                     ci.cancel();
                  }

               }
            }
         }
      }
   }
}
