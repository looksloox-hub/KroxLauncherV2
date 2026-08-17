package com.example.client.renderer.vulkan;

import com.example.client.renderer.renderpass.TerrainRenderPass;
import com.example.client.renderer.vulkan.pipeline.VulkanTerrainPipeline;

public final class VulkanTerrainRenderer {
   private final VulkanRuntime runtime;
   private TerrainRenderPass renderPass;
   private VulkanTerrainPipeline pipeline;

   public VulkanTerrainRenderer(VulkanRuntime runtime) {
      this.runtime = runtime;
   }

   public void initialize(int swapchainFormat) {
      this.renderPass = new TerrainRenderPass(this.runtime.device());
      this.renderPass.create(swapchainFormat);
      this.pipeline = new VulkanTerrainPipeline(this.runtime.device());
   }

   public void render() {
   }

   public void destroy() {
      if (this.pipeline != null) {
         this.pipeline.destroy();
      }

      if (this.renderPass != null) {
         this.renderPass.destroy();
      }

   }
}
