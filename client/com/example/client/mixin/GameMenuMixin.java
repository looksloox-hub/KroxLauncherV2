package com.example.client.mixin;

import com.example.client.ui.ModernClickGUI;
import com.example.client.ui.render.GlassRenderer;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.class_1109;
import net.minecraft.class_2561;
import net.minecraft.class_310;
import net.minecraft.class_332;
import net.minecraft.class_3417;
import net.minecraft.class_3469;
import net.minecraft.class_4185;
import net.minecraft.class_429;
import net.minecraft.class_433;
import net.minecraft.class_436;
import net.minecraft.class_437;
import net.minecraft.class_442;
import net.minecraft.class_447;
import net.minecraft.class_457;
import net.minecraft.class_5522;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({class_433.class})
public abstract class GameMenuMixin {
   @Unique
   private int optix$panelX;
   @Unique
   private int optix$panelY;
   @Unique
   private final int optix$panelW = 220;
   @Unique
   private final int optix$panelH = 24;
   @Unique
   private final int optix$gap = 4;
   @Unique
   private int optix$halfW;
   @Unique
   private boolean optix$showMods;
   @Unique
   private boolean optix$singleplayer;
   @Unique
   private boolean optix$multiplayer;
   @Unique
   private boolean optix$recordingInstalled;
   @Unique
   private boolean optix$pauseSupported;
   @Unique
   private int optix$recordX;
   @Unique
   private int optix$recordY;
   @Unique
   private final int optix$recordW = 20;
   @Unique
   private final int optix$recordH = 20;
   @Unique
   private final int optix$recordGap = 4;
   @Unique
   private float optix$backAnim = 0.0F;
   @Unique
   private float optix$advAnim = 0.0F;
   @Unique
   private float optix$statAnim = 0.0F;
   @Unique
   private float optix$modsAnim = 0.0F;
   @Unique
   private float optix$optixAnim = 0.0F;
   @Unique
   private float optix$optionsAnim = 0.0F;
   @Unique
   private float optix$extraAnim = 0.0F;
   @Unique
   private float optix$quitAnim = 0.0F;
   @Unique
   private float optix$recordStartAnim = 0.0F;
   @Unique
   private float optix$recordPauseAnim = 0.0F;
   @Unique
   private float optix$recordStopAnim = 0.0F;

   @Unique
   private void optix$playClick(class_310 mc) {
      try {
         mc.method_1483().method_4873(class_1109.method_47978(class_3417.field_15015, 1.0F));
      } catch (Throwable var3) {
      }

   }

   @Unique
   private boolean optix$inside(double mx, double my, int x, int y, int w, int h) {
      return mx >= (double)x && mx <= (double)(x + w) && my >= (double)y && my <= (double)(y + h);
   }

   @Unique
   private void optix$drawButton(class_332 ctx, class_310 mc, int x, int y, int w, int h, String text, boolean hover, float anim) {
      float expand = anim * 6.0F;
      float lift = anim * 3.0F;
      float drawX = (float)x - expand * 0.5F;
      float drawY = (float)y - lift;
      float drawW = (float)w + expand;
      float drawH = (float)h;
      GlassRenderer.drawButton(ctx, drawX, drawY, drawW, drawH, text, hover, false);
   }

   @Unique
   private void optix$drawRecordButton(class_332 ctx, class_310 mc, int x, int y, int w, int h, String text, boolean hover, float anim) {
      float expand = anim * 4.0F;
      float lift = anim * 2.0F;
      float drawX = (float)x - expand * 0.5F;
      float drawY = (float)y - lift;
      float drawW = (float)w + expand;
      float drawH = (float)h;
      GlassRenderer.drawButton(ctx, drawX, drawY, drawW, drawH, text, hover, false);
   }

   @Unique
   private boolean optix$hasFlashback() {
      return FabricLoader.getInstance().isModLoaded("flashback");
   }

   @Unique
   private boolean optix$hasReplayMod() {
      return FabricLoader.getInstance().isModLoaded("replaymod");
   }

   @Unique
   private boolean optix$hasRecordingMod() {
      return this.optix$hasFlashback() || this.optix$hasReplayMod();
   }

   @Unique
   private boolean optix$hasPauseSupport() {
      if (this.optix$hasFlashback()) {
         try {
            Class<?> fb = Class.forName("com.moulberry.flashback.Flashback");

            for(Method method : fb.getDeclaredMethods()) {
               if ("pauseRecordingReplay".equals(method.getName())) {
                  return true;
               }
            }
         } catch (Throwable var13) {
         }
      }

      if (this.optix$hasReplayMod()) {
         try {
            for(String className : new String[]{"com.replaymod.recording.ReplayModRecording", "com.replaymod.core.ReplayMod"}) {
               try {
                  Class<?> raw = Class.forName(className);

                  for(Method method : raw.getDeclaredMethods()) {
                     String name = method.getName().toLowerCase();
                     if (name.contains("pause")) {
                        return true;
                     }
                  }
               } catch (Throwable var11) {
               }
            }
         } catch (Throwable var12) {
         }
      }

      return false;
   }

   @Unique
   private boolean optix$invokeFlashback(String action) {
      if (!this.optix$hasFlashback()) {
         return false;
      } else {
         try {
            Class<?> fb = Class.forName("com.moulberry.flashback.Flashback");
            switch (action) {
               case "start":
                  try {
                     Method m = fb.getMethod("startRecordingReplay");
                     m.setAccessible(true);
                     m.invoke((Object)null);
                     return true;
                  } catch (NoSuchMethodException var10) {
                     Method m = fb.getMethod("startRecording");
                     m.setAccessible(true);
                     m.invoke((Object)null);
                     return true;
                  }
               case "pause":
                  try {
                     Method m = fb.getMethod("pauseRecordingReplay", Boolean.TYPE);
                     m.setAccessible(true);
                     m.invoke((Object)null, true);
                     return true;
                  } catch (NoSuchMethodException var9) {
                     try {
                        Method m = fb.getMethod("pauseRecordingReplay");
                        m.setAccessible(true);
                        m.invoke((Object)null);
                        return true;
                     } catch (NoSuchMethodException var8) {
                        return false;
                     }
                  }
               case "stop":
                  try {
                     Method m = fb.getMethod("finishRecordingReplay");
                     m.setAccessible(true);
                     m.invoke((Object)null);
                     return true;
                  } catch (NoSuchMethodException var7) {
                     Method m = fb.getMethod("stopRecordingReplay");
                     m.setAccessible(true);
                     m.invoke((Object)null);
                     return true;
                  }
            }
         } catch (Throwable var11) {
         }

         return false;
      }
   }

   @Unique
   private Object optix$findReplayModRecordingInstance() {
      String[] classCandidates = new String[]{"com.replaymod.recording.ReplayModRecording", "com.replaymod.core.ReplayMod"};

      for(String className : classCandidates) {
         try {
            Class<?> raw = Class.forName(className);

            for(String fieldName : new String[]{"instance", "INSTANCE", "replayModRecording", "current"}) {
               try {
                  Field field = raw.getDeclaredField(fieldName);
                  field.setAccessible(true);
                  Object value = field.get((Object)null);
                  if (value != null) {
                     return value;
                  }
               } catch (Throwable var15) {
               }
            }

            for(String methodName : new String[]{"getInstance", "instance", "get"}) {
               try {
                  Method m = raw.getDeclaredMethod(methodName);
                  m.setAccessible(true);
                  Object value = m.invoke((Object)null);
                  if (value != null) {
                     return value;
                  }
               } catch (Throwable var14) {
               }
            }

            try {
               Constructor<?> ctor = raw.getDeclaredConstructor();
               ctor.setAccessible(true);
               Object value = ctor.newInstance();
               if (value != null) {
                  return value;
               }
            } catch (Throwable var13) {
            }
         } catch (Throwable var16) {
         }
      }

      return null;
   }

   @Unique
   private boolean optix$invokeReplayMod(class_310 mc, String action) {
      if (!this.optix$hasReplayMod()) {
         return false;
      } else {
         Object target = this.optix$findReplayModRecordingInstance();
         if (target == null) {
            return false;
         } else {
            try {
               if ("start".equals(action)) {
                  Object connection = null;

                  try {
                     if (mc.method_1562() != null) {
                        connection = mc.method_1562().method_48296();
                     }
                  } catch (Throwable var17) {
                  }

                  for(Method m : target.getClass().getDeclaredMethods()) {
                     String n = m.getName().toLowerCase();
                     if (n.contains("start") || n.contains("initiate") || n.contains("begin") || n.contains("record")) {
                        Class<?>[] params = m.getParameterTypes();
                        Object[] args = new Object[params.length];
                        boolean ok = true;

                        for(int i = 0; i < params.length; ++i) {
                           Class<?> p = params[i];
                           Object val = null;
                           if (connection != null && p.isAssignableFrom(connection.getClass())) {
                              val = connection;
                           } else if (p.isAssignableFrom(class_310.class)) {
                              val = mc;
                           } else if (mc.method_1562() != null && p.isAssignableFrom(mc.method_1562().getClass())) {
                              val = mc.method_1562();
                           } else if (p != Boolean.TYPE && p != Boolean.class) {
                              if (p == Integer.TYPE) {
                                 val = 0;
                              } else if (p == Long.TYPE) {
                                 val = 0L;
                              } else if (p == Float.TYPE) {
                                 val = 0.0F;
                              } else if (p == Double.TYPE) {
                                 val = (double)0.0F;
                              }
                           } else {
                              val = Boolean.TRUE;
                           }

                           if (val == null && !p.isPrimitive()) {
                              val = null;
                           }

                           if (val == null && p.isPrimitive()) {
                              ok = false;
                              break;
                           }

                           args[i] = val;
                        }

                        if (ok) {
                           m.setAccessible(true);
                           m.invoke(target, args);
                           return true;
                        }
                     }
                  }
               } else if ("pause".equals(action) || "stop".equals(action)) {
                  String[] names = "pause".equals(action) ? new String[]{"pause", "togglePause", "suspend", "resume"} : new String[]{"stop", "finish", "end", "stopRecording", "finishRecording"};

                  for(Method m : target.getClass().getDeclaredMethods()) {
                     String n = m.getName().toLowerCase();
                     boolean matches = false;

                     for(String cand : names) {
                        if (n.contains(cand.toLowerCase())) {
                           matches = true;
                           break;
                        }
                     }

                     if (matches) {
                        Class<?>[] params = m.getParameterTypes();
                        Object[] args = new Object[params.length];
                        boolean ok = true;

                        for(int i = 0; i < params.length; ++i) {
                           Class<?> p = params[i];
                           Object val = null;
                           if (p != Boolean.TYPE && p != Boolean.class) {
                              if (p == Integer.TYPE) {
                                 val = 0;
                              } else if (p == Long.TYPE) {
                                 val = 0L;
                              } else if (p == Float.TYPE) {
                                 val = 0.0F;
                              } else if (p == Double.TYPE) {
                                 val = (double)0.0F;
                              } else if (p.isAssignableFrom(class_310.class)) {
                                 val = mc;
                              } else if (mc.method_1562() != null && p.isAssignableFrom(mc.method_1562().getClass())) {
                                 val = mc.method_1562();
                              }
                           } else {
                              val = Boolean.TRUE;
                           }

                           if (val == null && p.isPrimitive()) {
                              ok = false;
                              break;
                           }

                           args[i] = val;
                        }

                        if (ok) {
                           m.setAccessible(true);
                           m.invoke(target, args);
                           return true;
                        }
                     }
                  }
               }
            } catch (Throwable var18) {
            }

            return false;
         }
      }
   }

   @Unique
   private void optix$invokeRecordingAction(class_310 mc, String action) {
      if (!this.optix$invokeFlashback(action)) {
         if (!this.optix$invokeReplayMod(mc, action)) {
            ;
         }
      }
   }

   @Unique
   private class_4185 optix$makeButton(int x, int y, int w, int h, String label, Runnable action) {
      return this.optix$makeButton(x, y, w, h, label, true, action);
   }

   @Unique
   private class_4185 optix$makeButton(int x, int y, int w, int h, String label, boolean active, Runnable action) {
      class_4185 button = class_4185.method_46430(class_2561.method_43470(label), (b) -> {
         this.optix$playClick(class_310.method_1551());
         action.run();
      }).method_46434(x, y, w, h).method_46431();
      button.field_22763 = active;
      button.method_25350(0.0F);
      return button;
   }

   @Unique
   private boolean optix$openByClassName(class_310 mc, class_437 parent, String className, Object... extras) {
      try {
         Class<?> raw = Class.forName(className);
         Class<? extends class_437> clazz = raw.asSubclass(class_437.class);

         for(Constructor<?> ctor : clazz.getConstructors()) {
            Class<?>[] params = ctor.getParameterTypes();
            Object[] args = new Object[params.length];
            boolean ok = true;
            int i = 0;

            while(true) {
               if (i < params.length) {
                  Class<?> p = params[i];
                  Object matched = null;
                  if (class_437.class.isAssignableFrom(p)) {
                     matched = parent;
                  } else if (p.isAssignableFrom(class_310.class)) {
                     matched = mc;
                  } else if (mc.method_1562() != null && p.isAssignableFrom(mc.method_1562().getClass())) {
                     matched = mc.method_1562();
                  } else if (mc.field_1724 != null && p.isAssignableFrom(mc.field_1724.getClass())) {
                     matched = mc.field_1724;
                  } else if (extras != null) {
                     for(Object extra : extras) {
                        if (extra != null && p.isAssignableFrom(extra.getClass())) {
                           matched = extra;
                           break;
                        }
                     }
                  }

                  if (matched != null) {
                     args[i] = matched;
                     ++i;
                     continue;
                  }

                  ok = false;
               }

               if (ok) {
                  ctor.setAccessible(true);
                  Object screen = ctor.newInstance(args);
                  if (screen instanceof class_437) {
                     class_437 s = (class_437)screen;
                     mc.method_1507(s);
                     return true;
                  }
               }
               break;
            }
         }
      } catch (Throwable var21) {
      }

      return false;
   }

   @Unique
   private void optix$quitToTitle(class_310 mc) {
      mc.execute(() -> {
         try {
            mc.method_73360(class_2561.method_43470("Quit Game"));
         } catch (Throwable var2) {
         }

         mc.method_1507(new class_442());
      });
   }

   @Inject(
      method = {"init"},
      at = {@At("TAIL")}
   )
   private void optix$buildCustomMenu(CallbackInfo ci) {
      class_310 mc = class_310.method_1551();
      int sw = mc.method_22683().method_4486();
      int sh = mc.method_22683().method_4502();
      this.optix$panelX = sw / 2 - 110;
      this.optix$panelY = sh / 2 - 84;
      this.optix$halfW = 108;
      this.optix$showMods = FabricLoader.getInstance().isModLoaded("modmenu");
      this.optix$singleplayer = mc.method_1542();
      this.optix$multiplayer = mc.method_1562() != null && !this.optix$singleplayer;
      this.optix$recordingInstalled = this.optix$hasRecordingMod();
      this.optix$pauseSupported = this.optix$hasPauseSupport();
      ScreenAccessor accessor = (ScreenAccessor)this;
      accessor.optix$clearChildren();
      int currentY = this.optix$panelY;
      class_437 parent = (class_437)this;
      accessor.optix$addDrawableChild(this.optix$makeButton(this.optix$panelX, currentY, 220, 24, "Back to Game", () -> mc.method_1507((class_437)null)));
      currentY += 28;
      accessor.optix$addDrawableChild(this.optix$makeButton(this.optix$panelX, currentY, this.optix$halfW, 24, "Advancements", () -> {
         if (mc.method_1562() != null) {
            mc.method_1507(new class_457(mc.method_1562().method_2869(), parent));
         }

      }));
      accessor.optix$addDrawableChild(this.optix$makeButton(this.optix$panelX + this.optix$halfW + 4, currentY, this.optix$halfW, 24, "Statistics", () -> {
         if (mc.field_1724 != null) {
            class_3469 stats = mc.field_1724.method_3143();
            mc.method_1507(new class_447(parent, stats));
         }

      }));
      currentY += 28;
      if (this.optix$showMods) {
         accessor.optix$addDrawableChild(this.optix$makeButton(this.optix$panelX, currentY, 220, 24, "Mods", () -> this.optix$openByClassName(mc, parent, "com.terraformersmc.modmenu.gui.ModsScreen")));
         currentY += 28;
      }

      accessor.optix$addDrawableChild(this.optix$makeButton(this.optix$panelX, currentY, 220, 24, "Optix Options", () -> mc.method_1507(new ModernClickGUI())));
      currentY += 28;
      accessor.optix$addDrawableChild(this.optix$makeButton(this.optix$panelX, currentY, 220, 24, "Options", () -> mc.method_1507(new class_429(parent, mc.field_1690))));
      currentY += 28;
      if (this.optix$singleplayer) {
         accessor.optix$addDrawableChild(this.optix$makeButton(this.optix$panelX, currentY, 220, 24, "Open to LAN", () -> mc.method_1507(new class_436(parent))));
         currentY += 28;
      } else if (this.optix$multiplayer) {
         accessor.optix$addDrawableChild(this.optix$makeButton(this.optix$panelX, currentY, 220, 24, "Player Reporting", () -> mc.method_1507(new class_5522(parent))));
         currentY += 28;
      }

      accessor.optix$addDrawableChild(this.optix$makeButton(this.optix$panelX, currentY, 220, 24, "Quit Game", () -> this.optix$quitToTitle(mc)));
      if (this.optix$recordingInstalled) {
         int totalW = 68;
         this.optix$recordX = sw / 2 - totalW / 2;
         this.optix$recordY = this.optix$panelY - 20 - 8;
         accessor.optix$addDrawableChild(this.optix$makeButton(this.optix$recordX, this.optix$recordY, 20, 20, "⏺", true, () -> this.optix$invokeRecordingAction(mc, "start")));
         accessor.optix$addDrawableChild(this.optix$makeButton(this.optix$recordX + 20 + 4, this.optix$recordY, 20, 20, "⏸", this.optix$pauseSupported, () -> this.optix$invokeRecordingAction(mc, "pause")));
         accessor.optix$addDrawableChild(this.optix$makeButton(this.optix$recordX + 48, this.optix$recordY, 20, 20, "⏹", true, () -> this.optix$invokeRecordingAction(mc, "stop")));
      }

   }

   @Inject(
      method = {"render"},
      at = {@At("TAIL")}
   )
   private void optix$drawCustomUi(class_332 ctx, int mouseX, int mouseY, float delta, CallbackInfo ci) {
      class_310 mc = class_310.method_1551();
      int x = this.optix$panelX;
      int y = this.optix$panelY;
      boolean backHover = this.optix$inside((double)mouseX, (double)mouseY, x, y, 220, 24);
      this.optix$backAnim += ((backHover ? 1.0F : 0.0F) - this.optix$backAnim) * 0.15F;
      this.optix$drawButton(ctx, mc, x, y, 220, 24, "Back to Game", backHover, this.optix$backAnim);
      int currentY = y + 28;
      boolean advHover = this.optix$inside((double)mouseX, (double)mouseY, x, currentY, this.optix$halfW, 24);
      this.optix$advAnim += ((advHover ? 1.0F : 0.0F) - this.optix$advAnim) * 0.15F;
      this.optix$drawButton(ctx, mc, x, currentY, this.optix$halfW, 24, "Advancements", advHover, this.optix$advAnim);
      boolean statHover = this.optix$inside((double)mouseX, (double)mouseY, x + this.optix$halfW + 4, currentY, this.optix$halfW, 24);
      this.optix$statAnim += ((statHover ? 1.0F : 0.0F) - this.optix$statAnim) * 0.15F;
      this.optix$drawButton(ctx, mc, x + this.optix$halfW + 4, currentY, this.optix$halfW, 24, "Statistics", statHover, this.optix$statAnim);
      currentY += 28;
      if (this.optix$showMods) {
         boolean modsHover = this.optix$inside((double)mouseX, (double)mouseY, x, currentY, 220, 24);
         this.optix$modsAnim += ((modsHover ? 1.0F : 0.0F) - this.optix$modsAnim) * 0.15F;
         this.optix$drawButton(ctx, mc, x, currentY, 220, 24, "Mods", modsHover, this.optix$modsAnim);
         currentY += 28;
      }

      boolean optixHover = this.optix$inside((double)mouseX, (double)mouseY, x, currentY, 220, 24);
      this.optix$optixAnim += ((optixHover ? 1.0F : 0.0F) - this.optix$optixAnim) * 0.15F;
      this.optix$drawButton(ctx, mc, x, currentY, 220, 24, "Optix Options", optixHover, this.optix$optixAnim);
      currentY += 28;
      boolean optionsHover = this.optix$inside((double)mouseX, (double)mouseY, x, currentY, 220, 24);
      this.optix$optionsAnim += ((optionsHover ? 1.0F : 0.0F) - this.optix$optionsAnim) * 0.15F;
      this.optix$drawButton(ctx, mc, x, currentY, 220, 24, "Options", optionsHover, this.optix$optionsAnim);
      currentY += 28;
      if (this.optix$singleplayer) {
         boolean extraHover = this.optix$inside((double)mouseX, (double)mouseY, x, currentY, 220, 24);
         this.optix$extraAnim += ((extraHover ? 1.0F : 0.0F) - this.optix$extraAnim) * 0.15F;
         this.optix$drawButton(ctx, mc, x, currentY, 220, 24, "Open to LAN", extraHover, this.optix$extraAnim);
         currentY += 28;
      } else if (this.optix$multiplayer) {
         boolean extraHover = this.optix$inside((double)mouseX, (double)mouseY, x, currentY, 220, 24);
         this.optix$extraAnim += ((extraHover ? 1.0F : 0.0F) - this.optix$extraAnim) * 0.15F;
         this.optix$drawButton(ctx, mc, x, currentY, 220, 24, "Player Reporting", extraHover, this.optix$extraAnim);
         currentY += 28;
      }

      boolean quitHover = this.optix$inside((double)mouseX, (double)mouseY, x, currentY, 220, 24);
      this.optix$quitAnim += ((quitHover ? 1.0F : 0.0F) - this.optix$quitAnim) * 0.15F;
      this.optix$drawButton(ctx, mc, x, currentY, 220, 24, "Quit Game", quitHover, this.optix$quitAnim);
      if (this.optix$recordingInstalled) {
         boolean startHover = this.optix$inside((double)mouseX, (double)mouseY, this.optix$recordX, this.optix$recordY, 20, 20);
         this.optix$recordStartAnim += ((startHover ? 1.0F : 0.0F) - this.optix$recordStartAnim) * 0.18F;
         this.optix$drawRecordButton(ctx, mc, this.optix$recordX, this.optix$recordY, 20, 20, "⏺", startHover, this.optix$recordStartAnim);
         boolean pauseHover = this.optix$inside((double)mouseX, (double)mouseY, this.optix$recordX + 20 + 4, this.optix$recordY, 20, 20);
         this.optix$recordPauseAnim += ((pauseHover ? 1.0F : 0.0F) - this.optix$recordPauseAnim) * 0.18F;
         this.optix$drawRecordButton(ctx, mc, this.optix$recordX + 20 + 4, this.optix$recordY, 20, 20, "⏸", pauseHover, this.optix$recordPauseAnim);
         boolean stopHover = this.optix$inside((double)mouseX, (double)mouseY, this.optix$recordX + 48, this.optix$recordY, 20, 20);
         this.optix$recordStopAnim += ((stopHover ? 1.0F : 0.0F) - this.optix$recordStopAnim) * 0.18F;
         this.optix$drawRecordButton(ctx, mc, this.optix$recordX + 48, this.optix$recordY, 20, 20, "⏹", stopHover, this.optix$recordStopAnim);
      }

   }
}
