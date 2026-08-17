package com.example.client.account;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import net.fabricmc.loader.api.FabricLoader;

public class AccountManager {
   public static final List<AccountData> ACCOUNTS = new ArrayList();
   private static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().create();
   private static final Path FILE = FabricLoader.getInstance().getGameDir().resolve("OptiXClient").resolve("accounts.json");

   public static void init() {
      load();
   }

   public static void save() {
      try {
         Files.createDirectories(FILE.getParent());
         Path tmp = FILE.resolveSibling(String.valueOf(FILE.getFileName()) + ".tmp");
         Writer writer = Files.newBufferedWriter(tmp);

         try {
            GSON.toJson(ACCOUNTS, writer);
         } catch (Throwable var6) {
            if (writer != null) {
               try {
                  writer.close();
               } catch (Throwable var4) {
                  var6.addSuppressed(var4);
               }
            }

            throw var6;
         }

         if (writer != null) {
            writer.close();
         }

         try {
            Files.move(tmp, FILE, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
         } catch (Exception var5) {
            Files.move(tmp, FILE, StandardCopyOption.REPLACE_EXISTING);
         }
      } catch (Exception e) {
         e.printStackTrace();
      }

   }

   public static void load() {
      try {
         if (!Files.exists(FILE, new LinkOption[0])) {
            save();
            return;
         }

         Reader reader = Files.newBufferedReader(FILE);

         try {
            Type type = (new TypeToken<List<AccountData>>() {
            }).getType();
            List<AccountData> loaded = (List)GSON.fromJson(reader, type);
            ACCOUNTS.clear();
            if (loaded != null) {
               for(AccountData acc : loaded) {
                  if (acc != null && acc.username != null && !acc.username.isBlank()) {
                     if (acc.uuid == null || acc.uuid.isBlank()) {
                        acc.uuid = UUID.randomUUID().toString();
                     }

                     if (acc.accessToken == null) {
                        acc.accessToken = "0";
                     }

                     if (acc.type == null) {
                        acc.type = AccountData.Type.OFFLINE;
                     }

                     if (acc.xuid == null) {
                        acc.xuid = "";
                     }

                     if (acc.clientId == null) {
                        acc.clientId = "";
                     }

                     ACCOUNTS.add(acc);
                  }
               }
            }
         } catch (Throwable var6) {
            if (reader != null) {
               try {
                  reader.close();
               } catch (Throwable var5) {
                  var6.addSuppressed(var5);
               }
            }

            throw var6;
         }

         if (reader != null) {
            reader.close();
         }
      } catch (Exception e) {
         e.printStackTrace();
         ACCOUNTS.clear();
         save();
      }

   }

   public static void add(AccountData acc) {
      if (acc != null) {
         ACCOUNTS.removeIf((a) -> a.uuid != null && acc.uuid != null && a.uuid.equalsIgnoreCase(acc.uuid));
         ACCOUNTS.add(acc);
         save();
      }
   }

   public static void remove(AccountData acc) {
      if (acc != null) {
         ACCOUNTS.remove(acc);
         save();
      }
   }

   public static void rename(AccountData acc, String newName) {
      if (acc != null && newName != null && !newName.isBlank()) {
         acc.username = newName.trim();
         save();
      }
   }

   public static void refresh() {
      load();
   }
}
