package com.example.client.update;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpClient.Redirect;
import java.net.http.HttpResponse.BodyHandlers;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.class_310;

public final class UpdateManager {
   private static final HttpClient HTTP;
   private static final String PROJECT_SLUG = "optixclient";
   private static final Path UPDATE_DIR;
   private static final Path STAGED_JAR;
   public static volatile String status;
   public static volatile float progress;
   private static volatile boolean checking;
   private static volatile boolean scanned;
   private static volatile boolean updateAvailable;
   private static volatile boolean downloaded;
   public static volatile boolean installed;
   private static volatile String currentVersion;
   private static volatile String latestVersion;
   private static volatile RemoteVersion latestRemote;
   private static volatile String popupMessage;
   private static volatile long popupUntilMs;

   private UpdateManager() {
   }

   public static void startSilentStartupCheck() {
      runCheck(false, true);
   }

   public static void checkForUpdates() {
      runCheck(true, false);
   }

   public static void restartAndInstall() {
      try {
         if (Files.exists(STAGED_JAR, new LinkOption[0])) {
            startInstallerAgent();
            status = "Restarting to install update...";
            showPopup("Restart now to finish installing OptiX.");
         } else {
            status = "No staged update to install.";
            showPopup("No downloaded update found.");
         }
      } catch (Exception e) {
         status = "Restart failed: " + e.getMessage();
         showPopup("Restart failed.");
      }

      class_310.method_1551().method_1490();
   }

   private static boolean versionsDifferent(String a, String b) {
      return !a.replace("v", "").trim().equalsIgnoreCase(b.replace("v", "").trim());
   }

   public static void restart() {
      restartAndInstall();
   }

   public static String getStatus() {
      return status;
   }

   public static float getProgress() {
      return progress;
   }

   public static String getButtonLabel() {
      if (downloaded) {
         return "Installed";
      } else {
         return scanned && !updateAvailable ? "Updated" : "Check";
      }
   }

   public static boolean hasPopup() {
      return !popupMessage.isEmpty() && System.currentTimeMillis() < popupUntilMs;
   }

   public static String getPopupMessage() {
      return popupMessage;
   }

   private static void runCheck(boolean downloadAfterScan, boolean silent) {
      if (!checking) {
         checking = true;
         status = silent ? "Scanning..." : "Checking...";
         progress = 0.05F;
         CompletableFuture.runAsync(() -> {
            try {
               scanAvailability();
               scanned = true;
               progress = 0.35F;
               if (updateAvailable && latestRemote != null) {
                  status = "Update available: " + latestVersion;
                  progress = 0.45F;
                  if (downloadAfterScan) {
                     downloadLatest();
                     downloaded = true;
                     installed = true;
                     status = "Downloaded. You can restart now.";
                     progress = 1.0F;
                     showPopup("Download complete. You can restart now.");
                     return;
                  }

                  return;
               }

               downloaded = false;
               installed = false;
               status = "Updated";
               progress = 1.0F;
               if (!silent) {
                  showPopup("OptiX Client is up to date.");
               }
            } catch (Exception e) {
               status = "Update failed: " + e.getMessage();
               progress = 1.0F;
               showPopup("Update failed.");
               return;
            } finally {
               checking = false;
            }

         });
      }
   }

   private static void scanAvailability() throws Exception {
      currentVersion = getLocalVersion();
      Optional<RemoteVersion> remote = fetchLatestCompatible(getMinecraftVersion());
      if (remote.isEmpty()) {
         latestRemote = null;
         latestVersion = "";
         updateAvailable = false;
         downloaded = false;
         installed = false;
         status = "Updated";
      } else {
         latestRemote = (RemoteVersion)remote.get();
         latestVersion = latestRemote.versionNumber;
         updateAvailable = versionsDifferent(latestVersion, currentVersion);
         downloaded = false;
         installed = false;
         if (!updateAvailable) {
            status = "Updated";
         }

      }
   }

   private static void downloadLatest() throws Exception {
      if (latestRemote != null && latestRemote.fileUrl != null) {
         Files.createDirectories(UPDATE_DIR);
         HttpURLConnection connection = (HttpURLConnection)latestRemote.fileUrl.toURL().openConnection();
         connection.setRequestProperty("User-Agent", "OptiXClient-Updater");
         connection.setConnectTimeout(15000);
         connection.setReadTimeout(30000);
         int total = connection.getContentLength();
         InputStream in = connection.getInputStream();

         try {
            OutputStream out = Files.newOutputStream(STAGED_JAR, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);

            try {
               byte[] buffer = new byte[8192];
               long done = 0L;

               int read;
               while((read = in.read(buffer)) != -1) {
                  out.write(buffer, 0, read);
                  done += (long)read;
                  if (total > 0) {
                     progress = 0.45F + 0.55F * ((float)done / (float)total);
                  }
               }
            } catch (Throwable var10) {
               if (out != null) {
                  try {
                     out.close();
                  } catch (Throwable var9) {
                     var10.addSuppressed(var9);
                  }
               }

               throw var10;
            }

            if (out != null) {
               out.close();
            }
         } catch (Throwable var11) {
            if (in != null) {
               try {
                  in.close();
               } catch (Throwable var8) {
                  var11.addSuppressed(var8);
               }
            }

            throw var11;
         }

         if (in != null) {
            in.close();
         }

         if (!downloaded) {
            progress = 1.0F;
         }
      } else {
         throw new IllegalStateException("No update file available.");
      }
   }

   private static Optional<RemoteVersion> fetchLatestCompatible(String mcVersion) throws Exception {
      String var10000 = URLEncoder.encode("[\"fabric\"]", StandardCharsets.UTF_8);
      String api = "https://api.modrinth.com/v2/project/optixclient/version?loaders=" + var10000 + "&game_versions=" + URLEncoder.encode("[\"" + mcVersion + "\"]", StandardCharsets.UTF_8) + "&include_changelog=false";
      HttpRequest request = HttpRequest.newBuilder(URI.create(api)).header("Accept", "application/json").GET().build();
      HttpResponse<String> response = HTTP.send(request, BodyHandlers.ofString());
      if (response.statusCode() != 200) {
         throw new IllegalStateException("Modrinth HTTP " + response.statusCode());
      } else {
         JsonArray array = JsonParser.parseString((String)response.body()).getAsJsonArray();
         if (array.isEmpty()) {
            return Optional.empty();
         } else {
            List<RemoteVersion> versions = new ArrayList();

            for(JsonElement el : array) {
               JsonObject obj = el.getAsJsonObject();
               String versionNumber = getString(obj, "version_number");
               String versionType = getString(obj, "version_type");
               String datePublished = getString(obj, "date_published");
               JsonArray files = obj.getAsJsonArray("files");
               if (files != null && !files.isEmpty()) {
                  JsonObject chosen = null;

                  for(JsonElement fileEl : files) {
                     JsonObject fileObj = fileEl.getAsJsonObject();
                     if (fileObj.has("primary") && fileObj.get("primary").getAsBoolean()) {
                        chosen = fileObj;
                        break;
                     }
                  }

                  if (chosen == null) {
                     for(JsonElement fileEl : files) {
                        JsonObject fileObj = fileEl.getAsJsonObject();
                        String filename = getString(fileObj, "filename");
                        if (filename.endsWith(".jar")) {
                           chosen = fileObj;
                           break;
                        }
                     }
                  }

                  versions.add(new RemoteVersion(versionNumber, versionType, datePublished, URI.create(getString(chosen, "url")), getString(chosen, "filename")));
               }
            }

            if (versions.isEmpty()) {
               return Optional.empty();
            } else {
               versions.sort(Comparator.comparing((v) -> OffsetDateTime.parse(v.datePublished), Comparator.reverseOrder()));
               return Optional.of((RemoteVersion)versions.get(0));
            }
         }
      }
   }

   private static void startInstallerAgent() throws Exception {
      Path currentJar = getCurrentJarPath();
      String javaExe = Path.of(System.getProperty("java.home"), "bin", isWindows() ? "java.exe" : "java").toString();
      List<String> command = List.of(javaExe, "-cp", currentJar.toAbsolutePath().toString(), "com.example.client.update.OptixUpdateAgent", Long.toString(ProcessHandle.current().pid()), STAGED_JAR.toAbsolutePath().toString(), currentJar.toAbsolutePath().toString());
      (new ProcessBuilder(command)).directory(FabricLoader.getInstance().getGameDir().toFile()).redirectErrorStream(true).start();
   }

   private static Path getCurrentJarPath() throws Exception {
      return Path.of(UpdateManager.class.getProtectionDomain().getCodeSource().getLocation().toURI());
   }

   private static String getMinecraftVersion() {
      return (String)FabricLoader.getInstance().getModContainer("minecraft").map((c) -> c.getMetadata().getVersion().getFriendlyString()).orElse("unknown");
   }

   private static String getLocalVersion() {
      return (String)FabricLoader.getInstance().getModContainer("optixclient").map((c) -> c.getMetadata().getVersion().getFriendlyString()).orElse("unknown");
   }

   private static String getString(JsonObject obj, String key) {
      return obj.has(key) && !obj.get(key).isJsonNull() ? obj.get(key).getAsString() : "";
   }

   private static boolean isWindows() {
      return System.getProperty("os.name").toLowerCase().contains("win");
   }

   private static void showPopup(String message) {
      popupMessage = message;
      popupUntilMs = System.currentTimeMillis() + 2500L;
   }

   static {
      HTTP = HttpClient.newBuilder().followRedirects(Redirect.NORMAL).build();
      UPDATE_DIR = FabricLoader.getInstance().getGameDir().resolve("optix-updater");
      STAGED_JAR = UPDATE_DIR.resolve("OptiXClient-update.jar");
      status = "Idle";
      progress = 0.0F;
      checking = false;
      scanned = false;
      updateAvailable = false;
      downloaded = false;
      installed = false;
      currentVersion = "unknown";
      latestVersion = "";
      latestRemote = null;
      popupMessage = "";
      popupUntilMs = 0L;
   }

   private static record RemoteVersion(String versionNumber, String versionType, String datePublished, URI fileUrl, String fileName) {
   }
}
