package com.example.client.mixin;

import com.example.client.cosmetics.SkinManager;
import com.mojang.authlib.GameProfile;
import net.minecraft.class_310;
import net.minecraft.class_640;
import net.minecraft.class_8685;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({class_640.class})
public abstract class PlayerListEntryMixin {
   @Shadow
   @Final
   private GameProfile field_3741;

   @Inject(
      method = {"getSkinTextures"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void optix$getCustomSkin(CallbackInfoReturnable<class_8685> cir) {
      class_310 mc = class_310.method_1551();
      if (mc.field_1724 != null) {
         if (this.field_3741.id().equals(mc.field_1724.method_5667())) {
            class_8685 textures = SkinManager.getCurrentSkinTextures();
            if (textures != null) {
               cir.setReturnValue(textures);
            }

         }
      }
   }
}
