package com.example.client.module;

import com.example.client.module.impl.AdaptiveResolutionModule;
import com.example.client.module.impl.AntiOverlayModule;
import com.example.client.module.impl.ArmorStatusModule;
import com.example.client.module.impl.BetterDebugModule;
import com.example.client.module.impl.BiomeModule;
import com.example.client.module.impl.BlockHighlightModule;
import com.example.client.module.impl.CPSModule;
import com.example.client.module.impl.ChunkLimiterModule;
import com.example.client.module.impl.CoordinatesModule;
import com.example.client.module.impl.CrosshairIndicatorModule;
import com.example.client.module.impl.CrosshairStyleModule;
import com.example.client.module.impl.DirectionModule;
import com.example.client.module.impl.EffectTimersModule;
import com.example.client.module.impl.EntityLimiterModule;
import com.example.client.module.impl.FPSModule;
import com.example.client.module.impl.FastHUDModule;
import com.example.client.module.impl.FullbrightModule;
import com.example.client.module.impl.HealthModule;
import com.example.client.module.impl.ItemPhysicsModule;
import com.example.client.module.impl.KeystrokesModule;
import com.example.client.module.impl.LowGraphicsModule;
import com.example.client.module.impl.LowShieldModule;
import com.example.client.module.impl.MemoryModule;
import com.example.client.module.impl.MotionBlurModule;
import com.example.client.module.impl.MouseTweaksModule;
import com.example.client.module.impl.NoEntityRenderModule;
import com.example.client.module.impl.NoFogModule;
import com.example.client.module.impl.NoHurtCamModule;
import com.example.client.module.impl.NoParticlesModule;
import com.example.client.module.impl.NoRenderModule;
import com.example.client.module.impl.NoWeatherModule;
import com.example.client.module.impl.PingModule;
import com.example.client.module.impl.ScoreboardModule;
import com.example.client.module.impl.ShieldStatusModule;
import com.example.client.module.impl.TimeModule;
import com.example.client.module.impl.ViewModelModule;
import com.example.client.module.impl.WaypointModule;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.class_332;
import org.joml.Matrix3x2fStack;

public final class ModuleManager {
   private static final List<Module> modules = new ArrayList();

   private ModuleManager() {
   }

   public static void init() {
      if (modules.isEmpty()) {
         modules.add(new FPSModule());
         modules.add(new AdaptiveResolutionModule());
         modules.add(new CPSModule());
         modules.add(new CoordinatesModule());
         modules.add(new ArmorStatusModule());
         modules.add(new EffectTimersModule());
         modules.add(new ShieldStatusModule());
         modules.add(new LowShieldModule());
         modules.add(new WaypointModule());
         modules.add(new BetterDebugModule());
         modules.add(new PingModule());
         modules.add(new TimeModule());
         modules.add(new KeystrokesModule());
         modules.add(new MemoryModule());
         modules.add(new BiomeModule());
         modules.add(new DirectionModule());
         modules.add(new MotionBlurModule());
         modules.add(new CrosshairIndicatorModule());
         modules.add(new CrosshairStyleModule());
         modules.add(new ScoreboardModule());
         modules.add(new HealthModule());
         modules.add(new FullbrightModule());
         modules.add(new ViewModelModule());
         modules.add(new NoFogModule());
         modules.add(new NoParticlesModule());
         modules.add(new NoWeatherModule());
         modules.add(new NoHurtCamModule());
         modules.add(new NoRenderModule());
         modules.add(new NoEntityRenderModule());
         modules.add(new AntiOverlayModule());
         modules.add(new ItemPhysicsModule());
         modules.add(new LowGraphicsModule());
         modules.add(new ChunkLimiterModule());
         modules.add(new EntityLimiterModule());
         modules.add(new FastHUDModule());
         modules.add(new MouseTweaksModule());
         modules.add(new BlockHighlightModule());
      }
   }

   public static List<Module> getModules() {
      return modules;
   }

   public static List<Module> getEnabledModules() {
      List<Module> enabled = new ArrayList();

      for(Module module : modules) {
         if (module.isEnabled()) {
            enabled.add(module);
         }
      }

      return enabled;
   }

   public static List<Module> getHudModules() {
      List<Module> hud = new ArrayList();

      for(Module module : modules) {
         if (module.isHud()) {
            hud.add(module);
         }
      }

      return hud;
   }

   public static Module getModuleByName(String name) {
      for(Module module : modules) {
         if (module.getName().equalsIgnoreCase(name)) {
            return module;
         }
      }

      return null;
   }

   public static <T extends Module> T getModule(Class<T> clazz) {
      for(Module module : modules) {
         if (clazz.isInstance(module)) {
            return (T)(clazz.cast(module));
         }
      }

      return null;
   }

   public static boolean isEnabled(String name) {
      Module module = getModuleByName(name);
      return module != null && module.isEnabled();
   }

   public static void onTick() {
      for(Module module : modules) {
         if (module.isEnabled()) {
            module.onTick();
         }
      }

   }

   public static void onRender(class_332 context) {
      for(Module module : modules) {
         if (module.isEnabled() && module.isHud()) {
            Matrix3x2fStack matrices = context.method_51448();
            matrices.pushMatrix();
            matrices.translate((float)module.getX(), (float)module.getY());
            matrices.scale(module.getScale(), module.getScale());
            module.render(context);
            matrices.popMatrix();
         }
      }

   }
}
