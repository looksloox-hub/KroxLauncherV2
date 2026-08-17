package com.example.client;

import com.example.client.module.ModuleManager;
import com.example.client.performance.ChunkLightingOptimizer;
import com.example.client.performance.ChunkMeshOptimizer;
import com.example.client.performance.PerformanceEngine;
import com.example.client.render.DynamicResolutionController;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;

public class ClientMain implements ClientModInitializer {
   public void onInitializeClient() {
      ModuleManager.init();
      ClientTickEvents.END_CLIENT_TICK.register((ClientTickEvents.EndTick)(client) -> {
         ModuleManager.onTick();
         DynamicResolutionController.apply();
      });
      HudRenderCallback.EVENT.register((HudRenderCallback)(context, tickDelta) -> ModuleManager.onRender(context));
      ClientTickEvents.END_CLIENT_TICK.register((ClientTickEvents.EndTick)(client) -> {
         ModuleManager.onTick();
         PerformanceEngine.tick(client);
      });
      ClientTickEvents.END_CLIENT_TICK.register((ClientTickEvents.EndTick)(client) -> {
         ModuleManager.onTick();
         PerformanceEngine.tick(client);
         ChunkLightingOptimizer.tick(client);
      });
      ClientTickEvents.END_CLIENT_TICK.register((ClientTickEvents.EndTick)(client) -> {
         ModuleManager.onTick();
         PerformanceEngine.tick(client);
         ChunkMeshOptimizer.tick(client);
      });
      ClientTickEvents.END_CLIENT_TICK.register((ClientTickEvents.EndTick)(client) -> {
         ModuleManager.onTick();
         PerformanceEngine.tick(client);
         ChunkLightingOptimizer.tick(client);
         ChunkMeshOptimizer.tick(client);
      });
   }
}
