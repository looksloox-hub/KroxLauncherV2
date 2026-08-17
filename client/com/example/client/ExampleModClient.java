package com.example.client;

import com.example.client.account.AccountData;
import com.example.client.account.AccountManager;
import com.example.client.config.ConfigManager;
import com.example.client.config.HudConfig;
import com.example.client.module.Module;
import com.example.client.module.ModuleManager;
import com.example.client.ui.OptixHudEditorScreen;
import com.example.client.update.UpdateManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.util.Locale;
import java.util.UUID;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.class_2960;
import net.minecraft.class_304;
import net.minecraft.class_304.class_11900;
import net.minecraft.class_3675.class_307;
import org.lwjgl.glfw.GLFW;

public class ExampleModClient implements ClientModInitializer {
   private static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().disableHtmlEscaping().create();
   private static final Path UI_CONFIG_PATH = FabricLoader.getInstance().getConfigDir().resolve("optixclient-ui.json");
   private static final String KEY_OPEN_MENU = "key.optixclient.open_menu";
   private static final class_304.class_11900 KEY_CATEGORY = class_11900.method_74698(class_2960.method_60655("optixclient", "controls"));
   private static class_304 OPEN_MENU_KEYBIND;
   private boolean wasPressed;

   public void onInitializeClient() {
      this.registerKeybind();
      ModuleManager.init();
      ConfigManager.init(ModuleManager.getModules());
      HudConfig.load();
      AccountManager.load();
      if (AccountManager.ACCOUNTS.isEmpty()) {
         AccountManager.ACCOUNTS.add(new AccountData("ShadowPvP", UUID.randomUUID().toString(), "0", AccountData.Type.OFFLINE));
         AccountManager.save();
      }

      UpdateManager.startSilentStartupCheck();
      ClientEvents.register();
      ClientLifecycleEvents.CLIENT_STOPPING.register((ClientLifecycleEvents.ClientStopping)(client) -> {
         try {
            ConfigManager.save();
         } catch (Throwable var4) {
         }

         try {
            HudConfig.save();
         } catch (Throwable var3) {
         }

         try {
            AccountManager.save();
         } catch (Throwable var2) {
         }

      });
      ClientTickEvents.END_CLIENT_TICK.register((ClientTickEvents.EndTick)(client) -> {
         ModuleManager.onTick();
         if (client != null && client.method_22683() != null) {
            while(OPEN_MENU_KEYBIND != null && OPEN_MENU_KEYBIND.method_1436()) {
               client.method_1507(new OptixHudEditorScreen());
            }

            for(Module module : ModuleManager.getModules()) {
               if (module.getKeyBinding() != null) {
                  while(module.getKeyBinding().method_1436()) {
                     module.toggle();
                     ConfigManager.requestSave();
                  }
               }
            }

            boolean pressed = GLFW.glfwGetKey(client.method_22683().method_4490(), 344) == 1;
            this.wasPressed = pressed;
         }
      });
   }

   private void registerKeybind() {
      OPEN_MENU_KEYBIND = KeyBindingHelper.registerKeyBinding(new class_304("key.optixclient.open_menu", class_307.field_1668, 344, KEY_CATEGORY));
   }

   public static String getOpenKeyName() {
      if (OPEN_MENU_KEYBIND == null) {
         return "RSHIFT";
      } else {
         String raw = OPEN_MENU_KEYBIND.method_16007().getString();
         return raw != null && !raw.isBlank() ? raw.toUpperCase(Locale.ROOT) : "RSHIFT";
      }
   }

   public static void syncOpenKeybindFromControls() {
      if (OPEN_MENU_KEYBIND != null) {
         saveUiConfig();
      }

   }

   private static void loadUiConfig() {
      try {
         if (!Files.exists(UI_CONFIG_PATH, new LinkOption[0])) {
            saveUiConfig();
            return;
         }

         Reader reader = Files.newBufferedReader(UI_CONFIG_PATH, StandardCharsets.UTF_8);

         try {
            JsonObject root = (JsonObject)GSON.fromJson(reader, JsonObject.class);
            if (root != null) {
            }
         } catch (Throwable var4) {
            if (reader != null) {
               try {
                  reader.close();
               } catch (Throwable var3) {
                  var4.addSuppressed(var3);
               }
            }

            throw var4;
         }

         if (reader != null) {
            reader.close();
         }
      } catch (Throwable t) {
         t.printStackTrace();
      }

   }

   private static void saveUiConfig() {
      try {
         Path parent = UI_CONFIG_PATH.getParent();
         if (parent != null) {
            Files.createDirectories(parent);
         }

         JsonObject root = new JsonObject();
         root.addProperty("version", 1);
         Writer writer = Files.newBufferedWriter(UI_CONFIG_PATH, StandardCharsets.UTF_8);

         try {
            GSON.toJson(root, writer);
         } catch (Throwable var6) {
            if (writer != null) {
               try {
                  writer.close();
               } catch (Throwable var5) {
                  var6.addSuppressed(var5);
               }
            }

            throw var6;
         }

         if (writer != null) {
            writer.close();
         }
      } catch (Throwable t) {
         t.printStackTrace();
      }

   }
}
