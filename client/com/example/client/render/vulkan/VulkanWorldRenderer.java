package com.example.client.render.vulkan;

import com.example.client.renderer.vulkan.VulkanRuntime;

public final class VulkanWorldRenderer {
   public static final VulkanWorldRenderer INSTANCE = new VulkanWorldRenderer();
   private VulkanRuntime runtime;

   private VulkanWorldRenderer() {
   }

   public void initialize(VulkanRuntime runtime) {
      this.runtime = runtime;
   }

   public void beginFrame() {
      if (this.runtime != null) {
         this.runtime.beginFrame();
      }
   }

   public void renderTerrain() {
      if (this.runtime != null) {
         ;
      }
   }

   public void endFrame() {
      if (this.runtime != null) {
         this.runtime.endFrame();
      }
   }
}
