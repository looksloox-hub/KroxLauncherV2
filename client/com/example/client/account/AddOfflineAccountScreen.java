package com.example.client.account;

import java.util.UUID;
import net.minecraft.class_2561;
import net.minecraft.class_310;
import net.minecraft.class_332;
import net.minecraft.class_342;
import net.minecraft.class_4185;
import net.minecraft.class_437;

public class AddOfflineAccountScreen extends class_437 {
   private final class_437 parent;
   private class_342 usernameField;

   public AddOfflineAccountScreen(class_437 parent) {
      super(class_2561.method_43470("Add Offline Account"));
      this.parent = parent;
   }

   protected void method_25426() {
      int cx = this.field_22789 / 2;
      this.usernameField = new class_342(this.field_22793, cx - 100, this.field_22790 / 2 - 18, 200, 20, class_2561.method_43470("Username"));
      this.usernameField.method_1880(16);
      this.method_37063(this.usernameField);
      this.method_37063(class_4185.method_46430(class_2561.method_43470("Add"), (b) -> this.addAccount()).method_46434(cx - 100, this.field_22790 / 2 + 10, 96, 20).method_46431());
      this.method_37063(class_4185.method_46430(class_2561.method_43470("Back"), (b) -> class_310.method_1551().method_1507(this.parent)).method_46434(cx + 4, this.field_22790 / 2 + 10, 96, 20).method_46431());
   }

   private void addAccount() {
      String username = this.usernameField.method_1882().trim();
      if (!username.isBlank()) {
         AccountManager.add(new AccountData(username, UUID.randomUUID().toString(), "0", AccountData.Type.OFFLINE));
         class_310.method_1551().method_1507(this.parent);
      }
   }

   public void method_25394(class_332 context, int mouseX, int mouseY, float delta) {
      context.method_25296(0, 0, this.field_22789, this.field_22790, -16052717, -15394270);
      context.method_27534(this.field_22793, class_2561.method_43470("Create offline account"), this.field_22789 / 2, this.field_22790 / 2 - 42, -1);
      super.method_25394(context, mouseX, mouseY, delta);
   }

   public boolean method_25421() {
      return false;
   }
}
