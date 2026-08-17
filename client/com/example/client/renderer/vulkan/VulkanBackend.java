package com.example.client.renderer.vulkan;

import com.example.client.render.RenderAbstractionLayer;
import java.nio.file.Paths;
import java.util.concurrent.atomic.AtomicBoolean;

public final class VulkanBackend implements RenderAbstractionLayer {
   private final AtomicBoolean initialized = new AtomicBoolean(false);
   private final VulkanPipelineCache pipelineCache = new VulkanPipelineCache(Paths.get("config", "optix", "pipeline-cache.bin"));

   public void initialize() {
      this.initialized.set(true);
      this.pipelineCache.load();
   }

   public void beginFrame() {
      if (this.initialized.get()) {
         ;
      }
   }

   public void endFrame() {
      if (this.initialized.get()) {
         ;
      }
   }

   public void shutdown() {
      this.initialized.set(false);
      this.pipelineCache.save(new byte[0]);
   }

   public boolean isInitialized() {
      return this.initialized.get();
   }

   public VulkanPipelineCache pipelineCache() {
      return this.pipelineCache;
   }
}
