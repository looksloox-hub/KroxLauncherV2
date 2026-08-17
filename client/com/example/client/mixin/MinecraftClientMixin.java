package com.example.client.mixin;

import com.example.client.ui.ModernClickGUI;
import net.minecraft.class_310;
import net.minecraft.class_433;
import net.minecraft.class_437;
import net.minecraft.class_442;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({class_310.class})
public class MinecraftClientMixin {
   @Unique
   private Integer optix$savedGuiScale = null;
   @Unique
   private boolean optix$forced = false;

   @Unique
   private boolean optix$shouldForce(class_437 screen) {
      return screen instanceof class_442 || screen instanceof ModernClickGUI || screen instanceof class_433;
   }

   @Inject(
      method = {"setScreen"},
      at = {@At("HEAD")}
   )
   private void optix$onSetScreen(class_437 screen, CallbackInfo ci) {
      class_310 mc = (class_310)this;
      if (mc.field_1690 != null) {
         class_437 current = mc.field_1755;
         boolean currentForced = this.optix$shouldForce(current);
         boolean nextForced = this.optix$shouldForce(screen);
         if (nextForced) {
            if (!this.optix$forced) {
               try {
                  this.optix$savedGuiScale = (Integer)mc.field_1690.method_42474().method_41753();
               } catch (Throwable var9) {
                  this.optix$savedGuiScale = null;
               }

               this.optix$forced = true;
            }

            try {
               mc.field_1690.method_42474().method_41748(2);
            } catch (Throwable var8) {
            }

         } else {
            if (currentForced || this.optix$forced) {
               try {
                  if (this.optix$savedGuiScale != null) {
                     mc.field_1690.method_42474().method_41748(this.optix$savedGuiScale);
                  }
               } catch (Throwable var10) {
               }

               this.optix$savedGuiScale = null;
               this.optix$forced = false;
            }

         }
      }
   }

   @Inject(
      method = {"tick"},
      at = {@At("HEAD")}
   )
   private void optix$onTick(CallbackInfo ci) {
      class_310 mc = (class_310)this;
      if (mc.field_1690 != null) {
         class_437 current = mc.field_1755;
         if (this.optix$shouldForce(current)) {
            if (!this.optix$forced) {
               try {
                  this.optix$savedGuiScale = (Integer)mc.field_1690.method_42474().method_41753();
               } catch (Throwable var7) {
                  this.optix$savedGuiScale = null;
               }

               this.optix$forced = true;
            }

            try {
               mc.field_1690.method_42474().method_41748(2);
            } catch (Throwable var6) {
            }
         } else if (this.optix$forced) {
            try {
               if (this.optix$savedGuiScale != null) {
                  mc.field_1690.method_42474().method_41748(this.optix$savedGuiScale);
               }
            } catch (Throwable var5) {
            }

            this.optix$savedGuiScale = null;
            this.optix$forced = false;
         }

      }
   }
}
