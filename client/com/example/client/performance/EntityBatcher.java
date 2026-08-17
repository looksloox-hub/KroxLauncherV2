package com.example.client.performance;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.class_1297;

public final class EntityBatcher {
   private static final List<class_1297> cachedEntities = new ArrayList();
   private static int tickCounter = 0;

   public static void tick(List<class_1297> worldEntities) {
      ++tickCounter;
      if (tickCounter % 5 == 0) {
         cachedEntities.clear();

         for(class_1297 e : worldEntities) {
            if (e != null && !e.method_7325() && !e.method_5767()) {
               cachedEntities.add(e);
            }
         }

      }
   }

   public static List<class_1297> getEntities() {
      return cachedEntities;
   }
}
