package com.example.client.render.vulkan;

import com.example.client.renderer.vulkan.VulkanRuntime;
import java.nio.ByteBuffer;

public final class VulkanUploadBridge {
   private final VulkanRuntime runtime;

   public VulkanUploadBridge(VulkanRuntime runtime) {
      this.runtime = runtime;
   }

   public void uploadMesh(long meshKey, ByteBuffer staging) {
   }
}
