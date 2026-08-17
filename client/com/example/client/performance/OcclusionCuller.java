package com.example.client.performance;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.class_238;

public final class OcclusionCuller {
   private final List<class_238> occluders = new ArrayList();

   public void clear() {
      this.occluders.clear();
   }

   public void addOccluder(class_238 box) {
      if (box != null) {
         this.occluders.add(box);
      }

   }

   public boolean isVisible(class_238 candidate) {
      if (candidate == null) {
         return true;
      } else {
         for(class_238 o : this.occluders) {
            boolean hidden = o.method_1008(candidate.field_1323, candidate.field_1322, candidate.field_1321) && o.method_1008(candidate.field_1320, candidate.field_1325, candidate.field_1324);
            if (hidden) {
               return false;
            }
         }

         return true;
      }
   }
}
