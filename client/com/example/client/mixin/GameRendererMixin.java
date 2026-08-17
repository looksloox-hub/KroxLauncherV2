package com.example.client.mixin;

import com.example.client.OptixClient;
import com.example.client.module.ModuleManager;
import com.example.client.module.impl.MotionBlurModule;
import com.example.client.module.impl.NoHurtCamModule;
import com.example.client.performance.PerformanceManager;
import com.example.client.render.MotionBlurRenderer;
import com.example.client.render.vulkan.VulkanWorldRenderer;
import net.minecraft.class_757;
import net.minecraft.class_9779;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({class_757.class})
public class GameRendererMixin {
   @Inject(
      method = {"render"},
      at = {@At("HEAD")}
   )
   private void optix$beginFrame(class_9779 tickCounter, boolean tick, CallbackInfo ci) {
      PerformanceManager perf = OptixClient.perf();
      if (perf != null) {
         perf.beginFrame();
      }

      VulkanWorldRenderer.INSTANCE.beginFrame();
   }

   @Inject(
      method = {"render"},
      at = {@At("TAIL")}
   )
   private void optix$endFrame(class_9779 tickCounter, boolean tick, CallbackInfo ci) {
      MotionBlurModule module = (MotionBlurModule)ModuleManager.getModule(MotionBlurModule.class);
      if (module != null && module.isEnabled()) {
         MotionBlurRenderer.setStrength((float)module.getBlurLevel() / 5.0F);
         MotionBlurRenderer.updateFrameMotion();
         MotionBlurRenderer.render();
      }

      VulkanWorldRenderer.INSTANCE.renderTerrain();
      VulkanWorldRenderer.INSTANCE.endFrame();
      PerformanceManager perf = OptixClient.perf();
      if (perf != null) {
         perf.endFrame();
      }

   }

   @Inject(
      method = {"tiltViewWhenHurt"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void optix$noHurtCam(CallbackInfo ci) {
      NoHurtCamModule module = (NoHurtCamModule)ModuleManager.getModule(NoHurtCamModule.class);
      if (module != null && module.isEnabled()) {
         ci.cancel();
      }

   }
}
