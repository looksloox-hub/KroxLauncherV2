package com.example.client.module.impl;

import com.example.client.module.Category;
import com.example.client.module.Module;
import java.lang.reflect.Method;
import net.minecraft.class_2960;
import net.minecraft.class_4063;
import net.minecraft.class_4066;
import net.minecraft.class_7172;

public class LowGraphicsModule extends Module {
   private int oldViewDistance;
   private int oldSimulationDistance;
   private int oldMipmapLevels;
   private int oldBiomeBlendRadius;
   private int oldCloudRenderDistance;
   private double oldEntityDistanceScaling;
   private boolean oldEntityShadows;
   private boolean oldBobView;
   private boolean oldVignette;
   private Boolean oldCutoutLeaves;
   private class_4066 oldParticles;
   private class_4063 oldCloudMode;

   public LowGraphicsModule() {
      super("LowGraphics", Category.RENDER, (class_2960)null);
   }

   public void onEnable() {
      if (mc != null && mc.field_1690 != null) {
         this.oldViewDistance = (Integer)mc.field_1690.method_42503().method_41753();
         this.oldSimulationDistance = (Integer)mc.field_1690.method_42510().method_41753();
         this.oldMipmapLevels = (Integer)mc.field_1690.method_42563().method_41753();
         this.oldBiomeBlendRadius = (Integer)mc.field_1690.method_41805().method_41753();
         this.oldCloudRenderDistance = (Integer)mc.field_1690.method_71270().method_41753();
         this.oldEntityDistanceScaling = (Double)mc.field_1690.method_42517().method_41753();
         this.oldEntityShadows = (Boolean)mc.field_1690.method_42435().method_41753();
         this.oldBobView = (Boolean)mc.field_1690.method_42448().method_41753();
         this.oldVignette = (Boolean)mc.field_1690.method_75335().method_41753();
         this.oldCutoutLeaves = this.readCutoutLeavesSafely();
         this.oldParticles = (class_4066)mc.field_1690.method_42475().method_41753();
         this.oldCloudMode = (class_4063)mc.field_1690.method_42528().method_41753();
         this.applyLowSettings();
      }
   }

   public void onDisable() {
      if (mc != null && mc.field_1690 != null) {
         mc.field_1690.method_42503().method_41748(this.oldViewDistance);
         mc.field_1690.method_42510().method_41748(this.oldSimulationDistance);
         mc.field_1690.method_42563().method_41748(this.oldMipmapLevels);
         mc.field_1690.method_41805().method_41748(this.oldBiomeBlendRadius);
         mc.field_1690.method_71270().method_41748(this.oldCloudRenderDistance);
         mc.field_1690.method_42517().method_41748(this.oldEntityDistanceScaling);
         mc.field_1690.method_42435().method_41748(this.oldEntityShadows);
         mc.field_1690.method_42448().method_41748(this.oldBobView);
         mc.field_1690.method_75335().method_41748(this.oldVignette);
         this.writeCutoutLeavesSafely(this.oldCutoutLeaves);
         mc.field_1690.method_42475().method_41748(this.oldParticles);
         mc.field_1690.method_42528().method_41748(this.oldCloudMode);
      }
   }

   public void onTick() {
      if (this.isEnabled() && mc != null && mc.field_1690 != null) {
         this.applyLowSettings();
      }
   }

   private void applyLowSettings() {
      mc.field_1690.method_42503().method_41748(6);
      mc.field_1690.method_42510().method_41748(4);
      mc.field_1690.method_42563().method_41748(0);
      mc.field_1690.method_41805().method_41748(0);
      mc.field_1690.method_71270().method_41748(0);
      mc.field_1690.method_42517().method_41748((double)0.5F);
      mc.field_1690.method_42435().method_41748(false);
      mc.field_1690.method_42448().method_41748(false);
      mc.field_1690.method_75335().method_41748(false);
      this.writeCutoutLeavesSafely(Boolean.FALSE);
      mc.field_1690.method_42475().method_41748(class_4066.field_18199);
      mc.field_1690.method_42528().method_41748(class_4063.field_18162);
   }

   private Boolean readCutoutLeavesSafely() {
      try {
         Method m = mc.field_1690.getClass().getMethod("getCutoutLeaves");
         Object option = m.invoke(mc.field_1690);
         if (option instanceof class_7172<?> simpleOption) {
            Object value = simpleOption.method_41753();
            if (value instanceof Boolean b) {
               return b;
            }
         }
      } catch (Throwable var6) {
      }

      return null;
   }

   private void writeCutoutLeavesSafely(Boolean value) {
      if (value != null && mc != null && mc.field_1690 != null) {
         try {
            Method m = mc.field_1690.getClass().getMethod("getCutoutLeaves");
            Object option = m.invoke(mc.field_1690);
            if (option instanceof class_7172) {
               class_7172<?> simpleOption = (class_7172)option;
               simpleOption.method_41748(value);
            }
         } catch (Throwable var5) {
         }

      }
   }
}
