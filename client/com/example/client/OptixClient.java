package com.example.client;

import com.example.client.performance.PerformanceManager;
import com.example.client.renderer.vulkan.VulkanRuntime;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.class_310;

public final class OptixClient implements ClientModInitializer {
   private static VulkanRuntime VULKAN;
   private static PerformanceManager PERFORMANCE;

   public void onInitializeClient() {
      System.out.println("[Optix] Initializing client...");

      try {
         VULKAN = new VulkanRuntime();
         long window = class_310.method_1551().method_22683().method_4490();
         int width = class_310.method_1551().method_22683().method_4489();
         int height = class_310.method_1551().method_22683().method_4506();
         VULKAN.init(window, width, height);
         PERFORMANCE = new PerformanceManager(VULKAN);
         PERFORMANCE.boot();
         System.out.println("[Optix] Vulkan initialized successfully.");
      } catch (Exception e) {
         System.err.println("[Optix] Failed to initialize Vulkan.");
         e.printStackTrace();
      }

   }

   public static VulkanRuntime vulkan() {
      return VULKAN;
   }

   public static PerformanceManager perf() {
      return PERFORMANCE;
   }
}
