package com.example.client;

import com.example.client.ui.ModernClickGUI;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import org.lwjgl.glfw.GLFW;

public class ClientInit implements ClientModInitializer {
   private boolean wasPressed = false;

   public void onInitializeClient() {
      ClientTickEvents.END_CLIENT_TICK.register((ClientTickEvents.EndTick)(client) -> {
         if (client.field_1724 != null) {
            boolean pressed = GLFW.glfwGetKey(client.method_22683().method_4490(), 344) == 1;
            if (pressed && !this.wasPressed) {
               client.method_1507(new ModernClickGUI());
            }

            this.wasPressed = pressed;
         }
      });
   }
}
