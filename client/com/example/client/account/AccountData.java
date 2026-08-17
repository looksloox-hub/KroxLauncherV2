package com.example.client.account;

import java.util.Optional;
import java.util.UUID;
import net.minecraft.class_320;

public class AccountData {
   public String username;
   public String uuid;
   public String accessToken;
   public String xuid = "";
   public String clientId = "";
   public Type type;

   public AccountData() {
      this.type = AccountData.Type.OFFLINE;
   }

   public AccountData(String username, String uuid, String accessToken, Type type) {
      this.type = AccountData.Type.OFFLINE;
      this.username = username;
      this.uuid = uuid;
      this.accessToken = accessToken;
      this.type = type == null ? AccountData.Type.OFFLINE : type;
   }

   public AccountData(String username, String uuid, String accessToken, Type type, String xuid, String clientId) {
      this.type = AccountData.Type.OFFLINE;
      this.username = username;
      this.uuid = uuid;
      this.accessToken = accessToken;
      this.type = type == null ? AccountData.Type.OFFLINE : type;
      this.xuid = xuid == null ? "" : xuid;
      this.clientId = clientId == null ? "" : clientId;
   }

   public boolean isMicrosoft() {
      return this.type == AccountData.Type.MICROSOFT;
   }

   public String displayType() {
      return this.isMicrosoft() ? "Microsoft" : "Offline";
   }

   public class_320 toSession() {
      UUID parsed;
      try {
         parsed = UUID.fromString(this.uuid);
      } catch (Exception var4) {
         parsed = UUID.randomUUID();
      }

      Optional<String> xuidOpt = this.xuid != null && !this.xuid.isBlank() ? Optional.of(this.xuid) : Optional.empty();
      Optional<String> clientIdOpt = this.clientId != null && !this.clientId.isBlank() ? Optional.of(this.clientId) : Optional.empty();
      return new class_320(this.username, parsed, this.accessToken, xuidOpt, clientIdOpt);
   }

   public static enum Type {
      OFFLINE,
      MICROSOFT;

      // $FF: synthetic method
      private static Type[] $values() {
         return new Type[]{OFFLINE, MICROSOFT};
      }
   }
}
