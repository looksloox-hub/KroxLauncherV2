package com.example.client;

import com.example.client.module.ModuleManager;
import com.example.client.performance.PerformanceEngine;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;

public final class ClientHooks {
   private ClientHooks() {
   }

   public static void init() {
      ClientTickEvents.END_CLIENT_TICK.register((ClientTickEvents.EndTick)(client) -> {
         ModuleManager.onTick();
         PerformanceEngine.tick(client);
      });
   }
}
