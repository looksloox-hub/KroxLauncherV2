package com.example.client.update;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpClient.Redirect;
import java.net.http.HttpResponse.BodyHandlers;
import java.nio.charset.StandardCharsets;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.class_155;
import net.minecraft.class_310;

public final class OptixUpdateService {
   private static final HttpClient HTTP;
   private static final String PROJECT_SLUG = "optixclient";
   private static final Path UPDATE_DIR;
   private static final Path STAGED_JAR;
   private static volatile String status;
   private static volatile boolean checking;
   private static volatile boolean updateReady;
   private static volatile String latestRemoteVersion;
   private static volatile String currentLocalVersion;

   private OptixUpdateService() {
   }

   public static String getStatus() {
      return status;
   }

   public static boolean isUpdateReady() {
      return updateReady;
   }

   public static String getLatestRemoteVersion() {
      return latestRemoteVersion;
   }

   public static String getCurrentLocalVersion() {
      return currentLocalVersion;
   }

   public static void checkForUpdate() {
      if (!checking) {
         checking = true;
         status = "Checking Modrinth...";
         updateReady = false;
         CompletableFuture.runAsync(() -> {
            try {
               String mcVersion = class_155.method_16673().comp_4024();
               currentLocalVersion = (String)FabricLoader.getInstance().getModContainer("optixclient").map((c) -> c.getMetadata().getVersion().getFriendlyString()).orElse("unknown");
               Optional<RemoteVersion> latest = fetchLatestCompatible(mcVersion);
               if (!latest.isEmpty()) {
                  RemoteVersion remote = (RemoteVersion)latest.get();
                  latestRemoteVersion = remote.versionNumber;
                  if (!"unknown".equalsIgnoreCase(currentLocalVersion) && remote.versionNumber.equalsIgnoreCase(currentLocalVersion)) {
                     status = "Optix is already up to date (" + remote.versionNumber + ")";
                     return;
                  }

                  Files.createDirectories(UPDATE_DIR);
                  status = "Downloading Optix " + remote.versionNumber + "...";
                  download(remote.downloadUrl, STAGED_JAR);
                  updateReady = true;
                  status = "Downloaded Optix " + remote.versionNumber + ". Restart to install.";
                  return;
               }

               status = "No Optix version found for Minecraft " + mcVersion;
            } catch (Exception e) {
               status = "Update failed: " + e.getMessage();
               updateReady = false;
               return;
            } finally {
               checking = false;
            }

         });
      }
   }

   public static void restartAndInstall() {
      if (updateReady && Files.exists(STAGED_JAR, new LinkOption[0])) {
         try {
            Path currentJar = getCurrentJarPath();
            if (!currentJar.toString().toLowerCase(Locale.ROOT).endsWith(".jar")) {
               status = "Updater only works from the built jar.";
               return;
            }

            launchInstallerAgent(currentJar, STAGED_JAR);
            status = "Restarting to install Optix update...";
            updateReady = false;
            class_310 client = class_310.method_1551();
            client.method_1592();
         } catch (Exception e) {
            status = "Restart failed: " + e.getMessage();
         }

      } else {
         status = "Download an update first.";
      }
   }

   private static Optional<RemoteVersion> fetchLatestCompatible(String mcVersion) throws Exception {
      String api = "https://api.modrinth.com/v2/project/optixclient/version?loaders=fabric&game_versions=" + URLEncoder.encode(mcVersion, StandardCharsets.UTF_8) + "&include_changelog=false";
      HttpRequest request = HttpRequest.newBuilder(URI.create(api)).header("Accept", "application/json").GET().build();
      HttpResponse<String> response = HTTP.send(request, BodyHandlers.ofString());
      if (response.statusCode() != 200) {
         throw new IOException("Modrinth HTTP " + response.statusCode());
      } else {
         JsonArray array = JsonParser.parseString((String)response.body()).getAsJsonArray();
         List<RemoteVersion> versions = new ArrayList();

         for(JsonElement el : array) {
            JsonObject obj = el.getAsJsonObject();
            String versionNumber = getString(obj, "version_number");
            String versionType = getString(obj, "version_type");
            String datePublished = getString(obj, "date_published");
            JsonArray files = obj.getAsJsonArray("files");
            if (files != null && !files.isEmpty()) {
               JsonObject chosenFile = null;

               for(JsonElement fileEl : files) {
                  JsonObject fileObj = fileEl.getAsJsonObject();
                  if (fileObj.has("primary") && fileObj.get("primary").getAsBoolean()) {
                     chosenFile = fileObj;
                     break;
                  }
               }

               if (chosenFile == null) {
                  chosenFile = files.get(0).getAsJsonObject();
               }

               String url = getString(chosenFile, "url");
               String filename = getString(chosenFile, "filename");
               versions.add(new RemoteVersion(versionNumber, versionType, datePublished, URI.create(url), filename));
            }
         }

         if (versions.isEmpty()) {
            return Optional.empty();
         } else {
            versions.sort(Comparator.comparing((v) -> OffsetDateTime.parse(v.datePublished), Comparator.reverseOrder()));

            for(RemoteVersion version : versions) {
               if ("release".equalsIgnoreCase(version.versionType)) {
                  return Optional.of(version);
               }
            }

            return Optional.of((RemoteVersion)versions.get(0));
         }
      }
   }

   private static void download(URI url, Path out) throws Exception {
      HttpRequest request = HttpRequest.newBuilder(url).header("Accept", "application/octet-stream").GET().build();
      HttpResponse<InputStream> response = HTTP.send(request, BodyHandlers.ofInputStream());
      if (response.statusCode() != 200) {
         throw new IOException("Download failed: HTTP " + response.statusCode());
      } else {
         Files.createDirectories(out.getParent());
         InputStream in = (InputStream)response.body();

         try {
            Files.copy(in, out, new CopyOption[]{StandardCopyOption.REPLACE_EXISTING});
         } catch (Throwable var8) {
            if (in != null) {
               try {
                  in.close();
               } catch (Throwable var7) {
                  var8.addSuppressed(var7);
               }
            }

            throw var8;
         }

         if (in != null) {
            in.close();
         }

      }
   }

   private static void launchInstallerAgent(Path currentJar, Path stagedJar) throws Exception {
      String javaExe = Paths.get(System.getProperty("java.home"), "bin", isWindows() ? "java.exe" : "java").toString();
      long pid = ProcessHandle.current().pid();
      List<String> command = List.of(javaExe, "-cp", currentJar.toAbsolutePath().toString(), "com.example.client.update.OptixUpdateAgent", Long.toString(pid), stagedJar.toAbsolutePath().toString(), currentJar.toAbsolutePath().toString());
      (new ProcessBuilder(command)).directory(FabricLoader.getInstance().getGameDir().toFile()).redirectErrorStream(true).start();
   }

   private static Path getCurrentJarPath() throws Exception {
      return Paths.get(OptixUpdateService.class.getProtectionDomain().getCodeSource().getLocation().toURI());
   }

   private static boolean isWindows() {
      return System.getProperty("os.name").toLowerCase(Locale.ROOT).contains("win");
   }

   private static String getString(JsonObject obj, String key) {
      return obj.has(key) && !obj.get(key).isJsonNull() ? obj.get(key).getAsString() : "";
   }

   static {
      HTTP = HttpClient.newBuilder().followRedirects(Redirect.NORMAL).build();
      UPDATE_DIR = FabricLoader.getInstance().getGameDir().resolve("optix-updater");
      STAGED_JAR = UPDATE_DIR.resolve("OptixClient-update.jar");
      status = "Idle";
      checking = false;
      updateReady = false;
      latestRemoteVersion = "";
      currentLocalVersion = "";
   }

   private static record RemoteVersion(String versionNumber, String versionType, String datePublished, URI downloadUrl, String filename) {
   }
}
