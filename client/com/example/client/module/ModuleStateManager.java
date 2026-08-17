package com.example.client.module;

import com.example.client.setting.BooleanSetting;
import com.example.client.setting.NumberSetting;
import com.example.client.setting.Setting;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.Properties;
import net.fabricmc.loader.api.FabricLoader;

public final class ModuleStateManager {
   private static final Path FILE = FabricLoader.getInstance().getConfigDir().resolve("optix-module-state.properties");
   private static boolean loading = false;

   private ModuleStateManager() {
   }

   public static boolean isLoading() {
      return loading;
   }

   public static synchronized void loadAll(List<Module> modules) {
      Properties props = new Properties();
      loading = true;

      try {
         if (Files.exists(FILE, new LinkOption[0])) {
            InputStream in = Files.newInputStream(FILE);

            try {
               props.load(in);
            } catch (Throwable var11) {
               if (in != null) {
                  try {
                     in.close();
                  } catch (Throwable var10) {
                     var11.addSuppressed(var10);
                  }
               }

               throw var11;
            }

            if (in != null) {
               in.close();
            }
         }

         for(Module module : modules) {
            loadModule(props, module);
         }
      } catch (Throwable t) {
         t.printStackTrace();
      } finally {
         loading = false;
      }

   }

   public static synchronized void saveAll(List<Module> modules) {
      if (!loading) {
         Properties props = new Properties();

         for(Module module : modules) {
            saveModule(props, module);
         }

         try {
            Path parent = FILE.getParent();
            if (parent != null) {
               Files.createDirectories(parent);
            }

            OutputStream out = Files.newOutputStream(FILE, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE);

            try {
               props.store(out, "Optix Module State");
            } catch (Throwable var7) {
               if (out != null) {
                  try {
                     out.close();
                  } catch (Throwable var6) {
                     var7.addSuppressed(var6);
                  }
               }

               throw var7;
            }

            if (out != null) {
               out.close();
            }
         } catch (Throwable t) {
            t.printStackTrace();
         }

      }
   }

   private static void loadModule(Properties props, Module module) {
      String p = prefix(module);

      try {
         module.setEnabled(Boolean.parseBoolean(props.getProperty(p + "enabled", String.valueOf(module.isEnabled()))));
         module.setKey(Integer.parseInt(props.getProperty(p + "key", String.valueOf(module.getKey()))));
         int x = Integer.parseInt(props.getProperty(p + "x", String.valueOf(module.getX())));
         int y = Integer.parseInt(props.getProperty(p + "y", String.valueOf(module.getY())));
         module.setPosition(x, y);
         int w = Integer.parseInt(props.getProperty(p + "width", String.valueOf(module.getWidth())));
         int h = Integer.parseInt(props.getProperty(p + "height", String.valueOf(module.getHeight())));
         module.setSize(w, h);
         module.setScale(Float.parseFloat(props.getProperty(p + "scale", String.valueOf(module.getScale()))));
         module.setHudColor((int)Long.parseLong(props.getProperty(p + "hudColor", String.valueOf(module.getHudColor()))));
         module.setRainbow(Boolean.parseBoolean(props.getProperty(p + "rainbow", String.valueOf(module.isRainbow()))));
         module.setRGB(Boolean.parseBoolean(props.getProperty(p + "rgb", String.valueOf(module.isRGB()))));
         module.setBox(Boolean.parseBoolean(props.getProperty(p + "box", String.valueOf(module.isBox()))));
         module.setOutline(Boolean.parseBoolean(props.getProperty(p + "outline", String.valueOf(module.isOutline()))));
         module.setGlow(Boolean.parseBoolean(props.getProperty(p + "glow", String.valueOf(module.isGlow()))));
         module.setVertical(Boolean.parseBoolean(props.getProperty(p + "vertical", String.valueOf(module.isVertical()))));
         module.setDraggable(Boolean.parseBoolean(props.getProperty(p + "draggable", String.valueOf(module.isDraggable()))));
         module.setGridSize(Integer.parseInt(props.getProperty(p + "gridSize", String.valueOf(module.getGridSize()))));
         module.setSnapToGrid(Boolean.parseBoolean(props.getProperty(p + "snapToGrid", String.valueOf(module.isSnapToGrid()))));
      } catch (Throwable var10) {
      }

      for(Setting setting : module.getSettings()) {
         String key = p + "setting." + setting.getName();
         String value = props.getProperty(key);
         if (value != null) {
            try {
               if (setting instanceof BooleanSetting) {
                  BooleanSetting bs = (BooleanSetting)setting;
                  bs.setValue(Boolean.parseBoolean(value));
               } else if (setting instanceof NumberSetting) {
                  NumberSetting ns = (NumberSetting)setting;
                  ns.setValue(Double.parseDouble(value));
               }
            } catch (Throwable var9) {
            }
         }
      }

   }

   private static void saveModule(Properties props, Module module) {
      String p = prefix(module);
      props.setProperty(p + "enabled", Boolean.toString(module.isEnabled()));
      props.setProperty(p + "key", Integer.toString(module.getKey()));
      props.setProperty(p + "x", Integer.toString(module.getX()));
      props.setProperty(p + "y", Integer.toString(module.getY()));
      props.setProperty(p + "width", Integer.toString(module.getWidth()));
      props.setProperty(p + "height", Integer.toString(module.getHeight()));
      props.setProperty(p + "scale", Float.toString(module.getScale()));
      props.setProperty(p + "hudColor", Integer.toString(module.getHudColor()));
      props.setProperty(p + "rainbow", Boolean.toString(module.isRainbow()));
      props.setProperty(p + "rgb", Boolean.toString(module.isRGB()));
      props.setProperty(p + "box", Boolean.toString(module.isBox()));
      props.setProperty(p + "outline", Boolean.toString(module.isOutline()));
      props.setProperty(p + "glow", Boolean.toString(module.isGlow()));
      props.setProperty(p + "vertical", Boolean.toString(module.isVertical()));
      props.setProperty(p + "draggable", Boolean.toString(module.isDraggable()));
      props.setProperty(p + "gridSize", Integer.toString(module.getGridSize()));
      props.setProperty(p + "snapToGrid", Boolean.toString(module.isSnapToGrid()));

      for(Setting setting : module.getSettings()) {
         String key = p + "setting." + setting.getName();

         try {
            if (setting instanceof BooleanSetting bs) {
               props.setProperty(key, Boolean.toString(bs.getValue()));
            } else if (setting instanceof NumberSetting ns) {
               props.setProperty(key, Double.toString(ns.getValue()));
            }
         } catch (Throwable var8) {
         }
      }

   }

   private static String prefix(Module module) {
      return "module." + module.getName() + ".";
   }
}
