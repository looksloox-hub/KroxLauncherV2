package com.example.client.config;

import com.example.client.module.Module;
import com.example.client.setting.BooleanSetting;
import com.example.client.setting.NumberSetting;
import com.example.client.setting.Setting;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSyntaxException;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import net.fabricmc.loader.api.FabricLoader;

public final class ConfigManager {
   private static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().disableHtmlEscaping().create();
   private static final Path CONFIG_PATH = FabricLoader.getInstance().getConfigDir().resolve("optixclient.json");
   private static final Object SAVE_LOCK = new Object();
   private static final ScheduledExecutorService AUTOSAVE_EXECUTOR = Executors.newSingleThreadScheduledExecutor(new ThreadFactory() {
      public Thread newThread(Runnable r) {
         Thread t = new Thread(r, "OptixClient-Config-Autosave");
         t.setDaemon(true);
         return t;
      }
   });
   private static volatile List<Module> modulesRef = List.of();
   private static volatile boolean loading = false;
   private static volatile boolean dirty = false;
   private static volatile boolean autosaveStarted = false;
   private static volatile String lastSnapshot = "";
   private static final Object NO_VALUE;

   private ConfigManager() {
   }

   public static boolean isLoading() {
      return loading;
   }

   public static void init(List<Module> modules) {
      setModules(modules);
      load();
      startAutosave();
   }

   public static void setModules(List<Module> modules) {
      modulesRef = modules == null ? List.of() : modules;
   }

   public static void requestSave() {
      if (!loading) {
         dirty = true;
      }

   }

   public static void load(List<Module> modules) {
      setModules(modules);
      load();
   }

   public static void load() {
      loading = true;
      boolean existedBefore = Files.exists(CONFIG_PATH, new LinkOption[0]);

      try {
         Path parent = CONFIG_PATH.getParent();
         if (parent != null) {
            Files.createDirectories(parent);
         }

         if (Files.exists(CONFIG_PATH, new LinkOption[0])) {
            Reader reader = Files.newBufferedReader(CONFIG_PATH, StandardCharsets.UTF_8);

            try {
               JsonElement parsed = JsonParser.parseReader(reader);
               if (parsed != null && parsed.isJsonObject()) {
                  JsonObject root = parsed.getAsJsonObject();
                  JsonObject modulesObj = root.has("modules") && root.get("modules").isJsonObject() ? root.getAsJsonObject("modules") : new JsonObject();

                  for(Module module : modulesRef) {
                     if (module != null) {
                        JsonObject moduleObj = modulesObj.has(module.getName()) && modulesObj.get(module.getName()).isJsonObject() ? modulesObj.getAsJsonObject(module.getName()) : null;
                        if (moduleObj != null) {
                           applyModule(module, moduleObj);
                        }
                     }
                  }
               }
            } catch (Throwable var16) {
               if (reader != null) {
                  try {
                     reader.close();
                  } catch (Throwable var15) {
                     var16.addSuppressed(var15);
                  }
               }

               throw var16;
            }

            if (reader != null) {
               reader.close();
            }
         }
      } catch (JsonSyntaxException | IllegalStateException | IOException e) {
         ((Exception)e).printStackTrace();
      } catch (Throwable t) {
         t.printStackTrace();
      } finally {
         loading = false;
         lastSnapshot = snapshot();
         dirty = false;
         if (!autosaveStarted) {
            startAutosave();
         }

         if (!existedBefore) {
            save();
         }

      }

   }

   public static void save(List<Module> modules) {
      setModules(modules);
      save();
   }

   public static void save() {
      if (!loading) {
         synchronized(SAVE_LOCK) {
            try {
               Path parent = CONFIG_PATH.getParent();
               if (parent != null) {
                  Files.createDirectories(parent);
               }

               JsonObject root = buildRoot();
               String currentSnapshot = GSON.toJson(root);
               Path tmp = CONFIG_PATH.resolveSibling(CONFIG_PATH.getFileName().toString() + ".tmp");
               Writer writer = Files.newBufferedWriter(tmp, StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE);

               try {
                  GSON.toJson(root, writer);
               } catch (Throwable var11) {
                  if (writer != null) {
                     try {
                        writer.close();
                     } catch (Throwable var9) {
                        var11.addSuppressed(var9);
                     }
                  }

                  throw var11;
               }

               if (writer != null) {
                  writer.close();
               }

               try {
                  Files.move(tmp, CONFIG_PATH, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
               } catch (AtomicMoveNotSupportedException var10) {
                  Files.move(tmp, CONFIG_PATH, StandardCopyOption.REPLACE_EXISTING);
               }

               lastSnapshot = currentSnapshot;
               dirty = false;
            } catch (Throwable t) {
               t.printStackTrace();
            }

         }
      }
   }

   private static void startAutosave() {
      if (!autosaveStarted) {
         autosaveStarted = true;
         AUTOSAVE_EXECUTOR.scheduleAtFixedRate(() -> {
            try {
               if (loading) {
                  return;
               }

               if (modulesRef == null || modulesRef.isEmpty()) {
                  return;
               }

               String current = snapshot();
               if (dirty || !current.equals(lastSnapshot)) {
                  save();
               }
            } catch (Throwable t) {
               t.printStackTrace();
            }

         }, 2L, 2L, TimeUnit.SECONDS);
      }
   }

   private static JsonObject buildRoot() {
      JsonObject root = new JsonObject();
      JsonObject modulesObj = new JsonObject();

      for(Module module : modulesRef) {
         if (module != null) {
            modulesObj.add(module.getName(), writeModule(module));
         }
      }

      root.addProperty("version", 1);
      root.add("modules", modulesObj);
      return root;
   }

   private static String snapshot() {
      try {
         return GSON.toJson(buildRoot());
      } catch (Throwable var1) {
         return "";
      }
   }

   private static JsonObject writeModule(Module module) {
      JsonObject obj = new JsonObject();
      putBool(obj, "enabled", safeBool(() -> module.isEnabled()));
      putInt(obj, "key", safeInt(() -> module.getKey()));
      putInt(obj, "x", safeInt(() -> module.getX()));
      putInt(obj, "y", safeInt(() -> module.getY()));
      putInt(obj, "width", safeInt(() -> module.getBaseWidth()));
      putInt(obj, "height", safeInt(() -> module.getBaseHeight()));
      putFloat(obj, "scale", safeFloat(() -> module.getScale()));
      putInt(obj, "hudColor", safeInt(() -> module.getHudColor()));
      putBool(obj, "rainbow", safeBool(() -> module.isRainbow()));
      putBool(obj, "rgb", safeBool(() -> module.isRGB()));
      putBool(obj, "box", safeBool(() -> module.isBox()));
      putBool(obj, "outline", safeBool(() -> module.isOutline()));
      putBool(obj, "glow", safeBool(() -> module.isGlow()));
      putBool(obj, "vertical", safeBool(() -> module.isVertical()));
      putBool(obj, "draggable", safeBool(() -> module.isDraggable()));
      putInt(obj, "gridSize", safeInt(() -> module.getGridSize()));
      putBool(obj, "snapToGrid", safeBool(() -> module.isSnapToGrid()));
      JsonObject settingsObj = new JsonObject();

      try {
         List<Setting> settings = module.getSettings();
         if (settings != null) {
            for(Setting setting : settings) {
               if (setting != null) {
                  settingsObj.add(setting.getName(), serializeSetting(setting));
               }
            }
         }
      } catch (Throwable var6) {
      }

      obj.add("settings", settingsObj);
      return obj;
   }

   private static JsonElement serializeSetting(Setting setting) {
      try {
         if (setting instanceof BooleanSetting bs) {
            return new JsonPrimitive(bs.getValue());
         } else if (setting instanceof NumberSetting ns) {
            return new JsonPrimitive(ns.getValue());
         } else {
            Object value = readValue(setting);
            if (value == null) {
               return JsonNull.INSTANCE;
            } else if (value instanceof Boolean) {
               Boolean b = (Boolean)value;
               return new JsonPrimitive(b);
            } else if (value instanceof Number) {
               Number n = (Number)value;
               return new JsonPrimitive(n);
            } else if (value instanceof String) {
               String s = (String)value;
               return new JsonPrimitive(s);
            } else if (value instanceof Character) {
               Character c = (Character)value;
               return new JsonPrimitive(c);
            } else {
               return value.getClass().isEnum() ? new JsonPrimitive(((Enum)value).name()) : new JsonPrimitive(String.valueOf(value));
            }
         }
      } catch (Throwable var3) {
         return JsonNull.INSTANCE;
      }
   }

   private static void applyModule(Module module, JsonObject obj) {
      try {
         if (obj.has("enabled") && obj.get("enabled").isJsonPrimitive()) {
            module.setEnabled(obj.get("enabled").getAsBoolean());
         }

         if (obj.has("key") && obj.get("key").isJsonPrimitive()) {
            module.setKey(obj.get("key").getAsInt());
         }

         if (obj.has("x") && obj.has("y") && obj.get("x").isJsonPrimitive() && obj.get("y").isJsonPrimitive()) {
            module.setPosition(obj.get("x").getAsInt(), obj.get("y").getAsInt());
         }

         if (obj.has("width") && obj.has("height") && obj.get("width").isJsonPrimitive() && obj.get("height").isJsonPrimitive()) {
            module.setSize(obj.get("width").getAsInt(), obj.get("height").getAsInt());
         }

         if (obj.has("scale") && obj.get("scale").isJsonPrimitive()) {
            module.setScale(obj.get("scale").getAsFloat());
         }

         if (obj.has("hudColor") && obj.get("hudColor").isJsonPrimitive()) {
            module.setHudColor(obj.get("hudColor").getAsInt());
         }

         if (obj.has("rainbow") && obj.get("rainbow").isJsonPrimitive()) {
            module.setRainbow(obj.get("rainbow").getAsBoolean());
         }

         if (obj.has("rgb") && obj.get("rgb").isJsonPrimitive()) {
            module.setRGB(obj.get("rgb").getAsBoolean());
         }

         if (obj.has("box") && obj.get("box").isJsonPrimitive()) {
            module.setBox(obj.get("box").getAsBoolean());
         }

         if (obj.has("outline") && obj.get("outline").isJsonPrimitive()) {
            module.setOutline(obj.get("outline").getAsBoolean());
         }

         if (obj.has("glow") && obj.get("glow").isJsonPrimitive()) {
            module.setGlow(obj.get("glow").getAsBoolean());
         }

         if (obj.has("vertical") && obj.get("vertical").isJsonPrimitive()) {
            module.setVertical(obj.get("vertical").getAsBoolean());
         }

         if (obj.has("draggable") && obj.get("draggable").isJsonPrimitive()) {
            module.setDraggable(obj.get("draggable").getAsBoolean());
         }

         if (obj.has("gridSize") && obj.get("gridSize").isJsonPrimitive()) {
            module.setGridSize(obj.get("gridSize").getAsInt());
         }

         if (obj.has("snapToGrid") && obj.get("snapToGrid").isJsonPrimitive()) {
            module.setSnapToGrid(obj.get("snapToGrid").getAsBoolean());
         }
      } catch (Throwable t) {
         t.printStackTrace();
      }

      JsonObject settingsObj = obj.has("settings") && obj.get("settings").isJsonObject() ? obj.getAsJsonObject("settings") : new JsonObject();

      try {
         List<Setting> settings = module.getSettings();
         if (settings != null) {
            for(Setting setting : settings) {
               if (setting != null) {
                  JsonElement value = settingsObj.get(setting.getName());
                  if (value != null && !value.isJsonNull()) {
                     applySetting(setting, value);
                  }
               }
            }
         }
      } catch (Throwable t) {
         t.printStackTrace();
      }

   }

   private static void applySetting(Setting setting, JsonElement value) {
      try {
         if (setting instanceof BooleanSetting bs) {
            if (value.isJsonPrimitive()) {
               bs.setValue(value.getAsBoolean());
            }

            return;
         }

         if (setting instanceof NumberSetting ns) {
            if (value.isJsonPrimitive()) {
               ns.setValue(value.getAsDouble());
            }

            return;
         }

         Object current = readValue(setting);
         if (current instanceof Boolean) {
            invokeSetValue(setting, value.getAsBoolean());
            return;
         }

         if (current instanceof Byte) {
            invokeSetValue(setting, value.getAsByte());
            return;
         }

         if (current instanceof Short) {
            invokeSetValue(setting, value.getAsShort());
            return;
         }

         if (current instanceof Integer) {
            invokeSetValue(setting, value.getAsInt());
            return;
         }

         if (current instanceof Long) {
            invokeSetValue(setting, value.getAsLong());
            return;
         }

         if (current instanceof Float) {
            invokeSetValue(setting, value.getAsFloat());
            return;
         }

         if (current instanceof Double) {
            invokeSetValue(setting, value.getAsDouble());
            return;
         }

         if (current != null && current.getClass().isEnum()) {
            invokeSetValue(setting, (String)value.getAsString());
            return;
         }

         if (value.isJsonPrimitive()) {
            JsonPrimitive p = value.getAsJsonPrimitive();
            if (p.isBoolean()) {
               invokeSetValue(setting, p.getAsBoolean());
            } else if (p.isNumber()) {
               invokeSetValue(setting, p.getAsDouble());
            } else if (p.isString()) {
               invokeSetValue(setting, (String)p.getAsString());
            }
         }
      } catch (Throwable t) {
         t.printStackTrace();
      }

   }

   private static Object readValue(Object setting) {
      try {
         Method m = findNoArgMethod(setting.getClass(), "getValue");
         if (m != null) {
            m.setAccessible(true);
            return m.invoke(setting);
         }
      } catch (Throwable var3) {
      }

      try {
         Field f = findField(setting.getClass(), "value");
         if (f != null) {
            f.setAccessible(true);
            return f.get(setting);
         }
      } catch (Throwable var2) {
      }

      return null;
   }

   private static boolean invokeSetValue(Object target, Object value) {
      try {
         for(Method m : target.getClass().getMethods()) {
            if (m.getName().equals("setValue") && m.getParameterCount() == 1) {
               Class<?> param = m.getParameterTypes()[0];
               Object converted = convertValue(value, param);
               if (converted != NO_VALUE) {
                  m.setAccessible(true);
                  m.invoke(target, converted);
                  return true;
               }
            }
         }
      } catch (Throwable var8) {
      }

      return false;
   }

   private static Object convertValue(Object value, Class<?> targetType) {
      if (value == null) {
         return NO_VALUE;
      } else {
         Class<?> boxed = box(targetType);
         if (boxed.isInstance(value)) {
            return value;
         } else if (boxed == Boolean.class) {
            if (value instanceof Boolean) {
               Boolean b = (Boolean)value;
               return b;
            } else if (value instanceof Number) {
               Number n = (Number)value;
               return n.intValue() != 0;
            } else {
               return Boolean.parseBoolean(String.valueOf(value));
            }
         } else if (Number.class.isAssignableFrom(boxed)) {
            double d;
            try {
               if (value instanceof Number) {
                  Number n = (Number)value;
                  d = n.doubleValue();
               } else {
                  d = Double.parseDouble(String.valueOf(value));
               }
            } catch (Throwable var6) {
               return NO_VALUE;
            }

            if (boxed == Byte.class) {
               return (byte)((int)d);
            } else if (boxed == Short.class) {
               return (short)((int)d);
            } else if (boxed == Integer.class) {
               return (int)d;
            } else if (boxed == Long.class) {
               return (long)d;
            } else if (boxed == Float.class) {
               return (float)d;
            } else {
               return boxed == Double.class ? d : NO_VALUE;
            }
         } else if (boxed == String.class) {
            return String.valueOf(value);
         } else {
            if (boxed.isEnum() && value instanceof String) {
               String s = (String)value;

               try {
                  Object enumValue = Enum.valueOf(boxed, s);
                  return enumValue;
               } catch (Throwable var7) {
               }
            }

            return NO_VALUE;
         }
      }
   }

   private static void invokeSetValue(Object target, boolean value) {
      invokeSetValue(target, (Object)value);
   }

   private static void invokeSetValue(Object target, int value) {
      invokeSetValue(target, (Object)value);
   }

   private static void invokeSetValue(Object target, long value) {
      invokeSetValue(target, (Object)value);
   }

   private static void invokeSetValue(Object target, float value) {
      invokeSetValue(target, (Object)value);
   }

   private static void invokeSetValue(Object target, double value) {
      invokeSetValue(target, (Object)value);
   }

   private static void invokeSetValue(Object target, String value) {
      invokeSetValue(target, (Object)value);
   }

   private static Method findNoArgMethod(Class<?> type, String name) {
      for(Class<?> current = type; current != null && current != Object.class; current = current.getSuperclass()) {
         try {
            return current.getDeclaredMethod(name);
         }
      }

      try {
         return type.getMethod(name);
      } catch (Throwable var4) {
         return null;
      }
   }

   private static Field findField(Class<?> type, String name) {
      for(Class<?> current = type; current != null && current != Object.class; current = current.getSuperclass()) {
         try {
            return current.getDeclaredField(name);
         }
      }

      return null;
   }

   private static Class<?> box(Class<?> type) {
      if (!type.isPrimitive()) {
         return type;
      } else if (type == Boolean.TYPE) {
         return Boolean.class;
      } else if (type == Byte.TYPE) {
         return Byte.class;
      } else if (type == Short.TYPE) {
         return Short.class;
      } else if (type == Integer.TYPE) {
         return Integer.class;
      } else if (type == Long.TYPE) {
         return Long.class;
      } else if (type == Float.TYPE) {
         return Float.class;
      } else if (type == Double.TYPE) {
         return Double.class;
      } else {
         return type == Character.TYPE ? Character.class : type;
      }
   }

   private static void putBool(JsonObject obj, String key, boolean value) {
      obj.addProperty(key, value);
   }

   private static void putInt(JsonObject obj, String key, int value) {
      obj.addProperty(key, value);
   }

   private static void putFloat(JsonObject obj, String key, float value) {
      obj.addProperty(key, value);
   }

   private static boolean safeBool(BooleanSupplier supplier) {
      try {
         return supplier.getAsBoolean();
      } catch (Throwable var2) {
         return false;
      }
   }

   private static int safeInt(IntSupplier supplier) {
      try {
         return supplier.getAsInt();
      } catch (Throwable var2) {
         return 0;
      }
   }

   private static float safeFloat(FloatSupplier supplier) {
      try {
         return supplier.getAsFloat();
      } catch (Throwable var2) {
         return 0.0F;
      }
   }

   static {
      Runtime.getRuntime().addShutdownHook(new Thread(() -> {
         try {
            save();
         } catch (Throwable var1) {
         }

      }, "OptixClient-Config-Shutdown"));
      NO_VALUE = new Object();
   }

   @FunctionalInterface
   private interface BooleanSupplier {
      boolean getAsBoolean();
   }

   @FunctionalInterface
   private interface FloatSupplier {
      float getAsFloat();
   }

   @FunctionalInterface
   private interface IntSupplier {
      int getAsInt();
   }
}
