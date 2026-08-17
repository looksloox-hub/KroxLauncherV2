package com.example.client.ui;

import com.example.client.account.AccountData;
import com.example.client.account.AccountManager;
import net.minecraft.class_2561;
import net.minecraft.class_310;
import net.minecraft.class_332;
import net.minecraft.class_437;

public class AccountSwitcherScreen extends class_437 {
   private final class_310 mc = class_310.method_1551();
   private String search = "";

   public AccountSwitcherScreen() {
      super(class_2561.method_43470("Account Switcher"));
   }

   public void method_25394(class_332 context, int mouseX, int mouseY, float delta) {
      context.method_25296(0, 0, this.field_22789, this.field_22790, -871690476, -871362536);
      int x = this.field_22789 / 2 - 170;
      int y = 40;
      int w = 340;
      int h = this.field_22790 - 80;
      context.method_25294(x, y, x + w, y + h, -586149864);
      context.method_27535(this.field_22793, class_2561.method_43470("Account Switcher"), x + 14, y + 12, -1);
      int searchY = y + 36;
      context.method_25294(x + 12, searchY, x + w - 12, searchY + 24, -15066586);
      context.method_27535(this.field_22793, class_2561.method_43470(this.search.isEmpty() ? "Search..." : this.search), x + 18, searchY + 8, -4473925);
      int cardY = searchY + 40;

      for(AccountData acc : AccountManager.ACCOUNTS) {
         if (this.search.isEmpty() || acc.username.toLowerCase().contains(this.search.toLowerCase())) {
            int cardX = x + 12;
            int cardW = w - 24;
            int cardH = 42;
            boolean hover = mouseX >= cardX && mouseX <= cardX + cardW && mouseY >= cardY && mouseY <= cardY + cardH;
            context.method_25294(cardX, cardY, cardX + cardW, cardY + cardH, hover ? -14013888 : -15000789);
            context.method_27535(this.field_22793, class_2561.method_43470(acc.username), cardX + 12, cardY + 8, -1);
            context.method_27535(this.field_22793, class_2561.method_43470(acc.type == AccountData.Type.MICROSOFT ? "Microsoft Account" : "Offline Account"), cardX + 12, cardY + 22, -5592406);
            cardY += 50;
         }
      }

   }
}
