package com.example.client.account;

import java.util.UUID;
import net.minecraft.class_2561;
import net.minecraft.class_310;
import net.minecraft.class_332;
import net.minecraft.class_342;
import net.minecraft.class_4185;
import net.minecraft.class_437;

public class AddAccountScreen extends class_437 {
   private final class_437 parent;
   private final boolean microsoftMode;
   private class_342 usernameField;
   private class_342 uuidField;
   private class_342 tokenField;
   private class_342 xuidField;
   private class_342 clientIdField;
   private String error = "";

   public AddAccountScreen(class_437 parent, boolean microsoftMode) {
      super(class_2561.method_43470(microsoftMode ? "Import Microsoft Session" : "Add Offline Account"));
      this.parent = parent;
      this.microsoftMode = microsoftMode;
   }

   protected void method_25426() {
      int centerX = this.field_22789 / 2;
      int boxW = 240;
      int fieldX = centerX - boxW / 2;
      int y = 46;
      this.usernameField = new class_342(this.field_22793, fieldX, y, boxW, 20, class_2561.method_43470("Username"));
      this.usernameField.method_1880(32);
      this.usernameField.method_1852("");
      this.method_37063(this.usernameField);
      y += 28;
      this.uuidField = new class_342(this.field_22793, fieldX, y, boxW, 20, class_2561.method_43470("UUID"));
      this.uuidField.method_1880(64);
      this.uuidField.method_1852("");
      this.method_37063(this.uuidField);
      y += 28;
      this.tokenField = new class_342(this.field_22793, fieldX, y, boxW, 20, class_2561.method_43470("Access Token"));
      this.tokenField.method_1880(512);
      this.tokenField.method_1852("");
      this.method_37063(this.tokenField);
      y += 28;
      this.xuidField = new class_342(this.field_22793, fieldX, y, boxW, 20, class_2561.method_43470("XUID (optional)"));
      this.xuidField.method_1880(128);
      this.xuidField.method_1852("");
      this.method_37063(this.xuidField);
      y += 28;
      this.clientIdField = new class_342(this.field_22793, fieldX, y, boxW, 20, class_2561.method_43470("Client ID (optional)"));
      this.clientIdField.method_1880(128);
      this.clientIdField.method_1852("");
      this.method_37063(this.clientIdField);
      y += 36;
      this.method_37063(class_4185.method_46430(class_2561.method_43470("Save"), (b) -> this.saveAccount()).method_46434(fieldX, y, 74, 20).method_46431());
      this.method_37063(class_4185.method_46430(class_2561.method_43470("Fill Offline"), (b) -> this.fillOfflineDefaults()).method_46434(fieldX + 80, y, 80, 20).method_46431());
      this.method_37063(class_4185.method_46430(class_2561.method_43470("Back"), (b) -> class_310.method_1551().method_1507(this.parent)).method_46434(fieldX + 166, y, 74, 20).method_46431());
      if (!this.microsoftMode) {
         this.fillOfflineDefaults();
      }

   }

   private void fillOfflineDefaults() {
      if (this.usernameField.method_1882().isBlank()) {
         class_342 var10000 = this.usernameField;
         int var10001 = AccountManager.ACCOUNTS.size();
         var10000.method_1852("Player" + (var10001 + 1));
      }

      if (this.uuidField.method_1882().isBlank()) {
         this.uuidField.method_1852(UUID.randomUUID().toString());
      }

      if (this.tokenField.method_1882().isBlank()) {
         this.tokenField.method_1852("0");
      }

      if (this.xuidField.method_1882().isBlank()) {
         this.xuidField.method_1852("");
      }

      if (this.clientIdField.method_1882().isBlank()) {
         this.clientIdField.method_1852("");
      }

   }

   private void saveAccount() {
      this.error = "";
      String username = this.usernameField.method_1882().trim();
      String uuid = this.uuidField.method_1882().trim();
      String token = this.tokenField.method_1882().trim();
      String xuid = this.xuidField.method_1882().trim();
      String clientId = this.clientIdField.method_1882().trim();
      if (username.isBlank()) {
         this.error = "Username is required.";
      } else {
         if (this.microsoftMode) {
            if (uuid.isBlank() || token.isBlank()) {
               this.error = "UUID and access token are required for Microsoft import.";
               return;
            }

            try {
               UUID.fromString(uuid);
            } catch (Exception var8) {
               this.error = "UUID is invalid.";
               return;
            }

            AccountManager.add(new AccountData(username, uuid, token, AccountData.Type.MICROSOFT, xuid, clientId));
         } else {
            if (uuid.isBlank()) {
               uuid = UUID.randomUUID().toString();
            } else {
               try {
                  UUID.fromString(uuid);
               } catch (Exception var7) {
                  this.error = "UUID is invalid.";
                  return;
               }
            }

            if (token.isBlank()) {
               token = "0";
            }

            AccountManager.add(new AccountData(username, uuid, token, AccountData.Type.OFFLINE, "", ""));
         }

         class_310.method_1551().method_1507(this.parent);
      }
   }

   public void method_25394(class_332 context, int mouseX, int mouseY, float delta) {
      context.method_25296(0, 0, this.field_22789, this.field_22790, -16052717, -15525853);
      int panelX = this.field_22789 / 2 - 148;
      int panelY = 22;
      int panelW = 296;
      int panelH = 220;
      context.method_25294(panelX, panelY, panelX + panelW, panelY + panelH, -871295456);
      context.method_25294(panelX, panelY, panelX + panelW, panelY + 1, -10777345);
      context.method_27534(this.field_22793, this.field_22785, this.field_22789 / 2, 10, 16777215);
      context.method_27534(this.field_22793, class_2561.method_43470(this.microsoftMode ? "Paste a valid Microsoft session you already signed in with" : "Create a local offline account"), this.field_22789 / 2, 24, -1711276033);
      context.method_25303(this.field_22793, "Username", panelX, 40, 11056071);
      context.method_25303(this.field_22793, "UUID", panelX, 68, 11056071);
      context.method_25303(this.field_22793, "Access Token", panelX, 96, 11056071);
      context.method_25303(this.field_22793, "XUID", panelX, 124, 11056071);
      context.method_25303(this.field_22793, "Client ID", panelX, 152, 11056071);
      if (!this.error.isBlank()) {
         context.method_25300(this.field_22793, this.error, this.field_22789 / 2, 206, -39322);
      }

      super.method_25394(context, mouseX, mouseY, delta);
   }

   public boolean method_25421() {
      return false;
   }
}
