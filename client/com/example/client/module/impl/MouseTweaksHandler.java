package com.example.client.module.impl;

import java.util.HashSet;
import java.util.Set;
import net.minecraft.class_1713;
import net.minecraft.class_1735;
import net.minecraft.class_310;
import net.minecraft.class_465;

public class MouseTweaksHandler {
   public static boolean dragging = false;
   public static int dragButton = -1;
   private static final Set<class_1735> visitedSlots = new HashSet();

   public static void startDrag(int button) {
      dragging = true;
      dragButton = button;
      visitedSlots.clear();
   }

   public static void stopDrag() {
      dragging = false;
      dragButton = -1;
      visitedSlots.clear();
   }

   public static void handleDrag(class_465<?> screen, class_1735 slot) {
      if (slot != null) {
         if (!visitedSlots.contains(slot)) {
            visitedSlots.add(slot);
            class_310 mc = class_310.method_1551();
            if (mc.field_1724 != null && mc.field_1761 != null) {
               if (dragButton == 1) {
                  mc.field_1761.method_2906(screen.method_17577().field_7763, slot.field_7874, 1, class_1713.field_7790, mc.field_1724);
               }

               if (dragButton == 0 && slot.method_7681()) {
                  mc.field_1761.method_2906(screen.method_17577().field_7763, slot.field_7874, 0, class_1713.field_7794, mc.field_1724);
               }

            }
         }
      }
   }

   public static void handleScroll(class_465<?> screen, class_1735 slot, boolean shift) {
      if (slot != null && slot.method_7681()) {
         class_310 mc = class_310.method_1551();
         class_1713 var10004 = shift ? class_1713.field_7794 : class_1713.field_7790;
         mc.field_1761.method_2906(screen.method_17577().field_7763, slot.field_7874, 0, var10004, mc.field_1724);
      }
   }
}
