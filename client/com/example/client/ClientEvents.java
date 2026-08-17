package com.example.client;

import com.example.client.module.ModuleManager;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;

public class ClientEvents {
   public static void register() {
      HudRenderCallback.EVENT.register((HudRenderCallback)(context, tickCounter) -> ModuleManager.onRender(context));
   }
}
