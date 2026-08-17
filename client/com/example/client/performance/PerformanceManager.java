package com.example.client.performance;

import com.example.client.OptixClient;
import com.example.client.gpu.GpuFrameTimer;
import com.example.client.gpu.GpuUploadQueue;
import com.example.client.gpu.GpuUploadTask;
import com.example.client.memory.PersistentBufferAllocator;
import com.example.client.memory.StagingBufferArena;
import com.example.client.render.batch.RenderBatcher;
import com.example.client.renderer.renderpass.TerrainRenderPass;
import com.example.client.renderer.vulkan.VulkanCommandRecorder;
import com.example.client.renderer.vulkan.VulkanRuntime;
import com.example.client.renderer.vulkan.VulkanTerrainRenderer;
import com.example.client.renderer.vulkan.VulkanUploadBridge;
import com.example.client.renderer.vulkan.pipeline.VulkanTerrainPipeline;
import com.example.client.renderer.vulkan.shader.TerrainShaderSystem;
import java.io.InputStream;
import java.nio.ByteBuffer;
import net.minecraft.class_310;
import net.minecraft.class_4604;

public final class PerformanceManager {
   private final VulkanRuntime vk;
   private final FramePacer framePacer = new FramePacer(120);
   private final AdaptiveResolutionController scaling = new AdaptiveResolutionController(0.7F, 1.0F, 0.05F, 0.02F);
   private final ChunkVisibilityCache visibilityCache = new ChunkVisibilityCache();
   private final OcclusionCuller occlusionCuller = new OcclusionCuller();
   private final GpuUploadQueue uploadQueue = new GpuUploadQueue();
   private final StagingBufferArena stagingBuffers = new StagingBufferArena();
   private final PersistentBufferAllocator persistentBuffers = new PersistentBufferAllocator(67108864L);
   private final RenderBatcher renderBatcher = new RenderBatcher();
   private final GpuFrameTimer gpuFrameTimer;
   private VulkanUploadBridge uploadBridge;
   private TerrainRenderPass terrainRenderPass;
   private TerrainShaderSystem terrainShaderSystem;
   private VulkanTerrainPipeline terrainPipeline;
   private VulkanCommandRecorder commandRecorder;
   private VulkanTerrainRenderer terrainRenderer;

   public PerformanceManager(VulkanRuntime vk) {
      this.vk = vk;
      this.gpuFrameTimer = new GpuFrameTimer(vk);
   }

   public void boot() {
      class_310 mc = class_310.method_1551();
      if (mc != null && mc.method_22683() != null) {
         this.vk.init(mc.method_22683().method_4490(), mc.method_22683().method_4489(), mc.method_22683().method_4506());
      }

      this.uploadBridge = new VulkanUploadBridge(this.stagingBuffers, this.persistentBuffers);
      this.tryInitializeTerrainRenderer();
   }

   private void tryInitializeTerrainRenderer() {
      try {
         byte[] vertSpv = loadResource("/assets/optix/shaders/terrain.vert.spv");
         byte[] fragSpv = loadResource("/assets/optix/shaders/terrain.frag.spv");
         if (vertSpv == null || fragSpv == null) {
            return;
         }

         this.terrainRenderPass = new TerrainRenderPass(this.vk.device());
         this.terrainRenderPass.create(50);
         this.terrainShaderSystem = new TerrainShaderSystem(this.vk.device());
         long vert = this.terrainShaderSystem.createShaderModule(vertSpv);
         long frag = this.terrainShaderSystem.createShaderModule(fragSpv);
         this.terrainPipeline = new VulkanTerrainPipeline(this.vk.device());
         this.terrainPipeline.create(this.terrainRenderPass.handle(), vert, frag);
         this.commandRecorder = new VulkanCommandRecorder(this.vk.device(), this.vk.commandPool());
         this.terrainRenderer = new VulkanTerrainRenderer(OptixClient.vulkan());
         this.terrainRenderer.initialize(44);
      } catch (Throwable var7) {
         this.terrainRenderer = null;
      }

   }

   private static byte[] loadResource(String path) {
      try {
         InputStream in = PerformanceManager.class.getResourceAsStream(path);

         byte[] var7;
         label48: {
            try {
               if (in == null) {
                  var7 = null;
                  break label48;
               }

               var7 = in.readAllBytes();
            } catch (Throwable var5) {
               if (in != null) {
                  try {
                     in.close();
                  } catch (Throwable var4) {
                     var5.addSuppressed(var4);
                  }
               }

               throw var5;
            }

            if (in != null) {
               in.close();
            }

            return var7;
         }

         if (in != null) {
            in.close();
         }

         return var7;
      } catch (Exception var6) {
         return null;
      }
   }

   public void beginFrame() {
      this.framePacer.begin();
      this.vk.beginFrame();
      class_310 mc = class_310.method_1551();
      if (mc != null && mc.field_1769 != null) {
         class_4604 frustum = mc.field_1769.method_62222();
         if (frustum != null) {
            this.visibilityCache.clear();
         }
      }

   }

   public void beginWorldRender() {
   }

   public void endWorldRender() {
      this.flushUploads();
   }

   public void endFrame() {
      this.vk.endFrame();
      this.framePacer.end();
      long gpuNanos = this.gpuFrameTimer.pollGpuFrameNanos();
      double gpuMs = gpuNanos > 0L ? (double)gpuNanos / (double)1000000.0F : this.framePacer.averageMillis();
      this.scaling.update(gpuMs, 8.333333333333334);
   }

   public void flushUploads() {
      for(GpuUploadTask task : this.uploadQueue.drainAll()) {
         if (this.uploadBridge != null) {
            ByteBuffer staging = this.uploadBridge.packMesh(task.mesh());
            this.uploadBridge.release(staging);
         }
      }

   }

   public boolean shouldThrottle() {
      return this.framePacer.overBudget();
   }

   public float internalScale() {
      return this.scaling.scale();
   }

   public ChunkVisibilityCache visibilityCache() {
      return this.visibilityCache;
   }

   public OcclusionCuller occlusionCuller() {
      return this.occlusionCuller;
   }

   public RenderBatcher renderBatcher() {
      return this.renderBatcher;
   }

   public PersistentBufferAllocator persistentBuffers() {
      return this.persistentBuffers;
   }

   public GpuUploadQueue uploadQueue() {
      return this.uploadQueue;
   }

   public void shutdown() {
      if (this.terrainRenderer != null) {
         this.terrainRenderer.destroy();
      }

      this.vk.close();
   }
}
