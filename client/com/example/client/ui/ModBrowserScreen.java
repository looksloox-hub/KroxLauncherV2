package com.example.client.ui;

import com.example.client.util.ModrinthAPI;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.class_11909;
import net.minecraft.class_2561;
import net.minecraft.class_310;
import net.minecraft.class_327;
import net.minecraft.class_332;
import net.minecraft.class_342;
import net.minecraft.class_437;

public class ModBrowserScreen extends class_437 {
   private final class_310 mc = class_310.method_1551();
   private class_342 searchBar;
   private List<ModrinthAPI.Project> projects = new ArrayList();
   private List<ModrinthAPI.Project> filtered = new ArrayList();
   private float scroll = 0.0F;
   private boolean loading = true;
   private String status = "Loading...";
   private String category = "all";
   private String loaderFilter = "fabric";
   private ModrinthAPI.Project selectedProject = null;
   private final Map<String, Float> progressMap = new HashMap();
   private final Map<String, String> fileByProject = new HashMap();
   private final Set<String> installedKeys = new HashSet();
   private final Deque<InstallTask> queue = new ArrayDeque();
   private final Set<String> queued = new HashSet();
   private boolean processingQueue = false;
   private String currentInstallingId = null;
   private final Map<String, Integer> iconTint = new ConcurrentHashMap();
   private static final String[] CATEGORIES = new String[]{"all", "performance", "utility", "pvp", "client", "library"};

   public ModBrowserScreen() {
      super(class_2561.method_43470("Mod Browser"));
   }

   protected void method_25426() {
      this.searchBar = new class_342(this.field_22793, this.field_22789 / 2 - 140, 18, 280, 20, class_2561.method_43470(""));
      this.searchBar.method_1880(48);
      this.method_37063(this.searchBar);
      this.scanInstalled();
      this.reload();
   }

   private void reload() {
      this.loading = true;
      this.status = "Searching Modrinth...";
      this.scroll = 0.0F;
      String query = this.searchBar.method_1882() != null && !this.searchBar.method_1882().isBlank() ? this.searchBar.method_1882().trim() : "mod";
      (new Thread(() -> {
         try {
            this.projects = ModrinthAPI.search(query, "mod");
            this.applyFilters();
            if (!this.filtered.isEmpty()) {
               this.selectedProject = (ModrinthAPI.Project)this.filtered.get(0);
            } else {
               this.selectedProject = null;
            }

            this.status = this.filtered.size() + " results";
         } catch (Exception e) {
            e.printStackTrace();
            this.projects = new ArrayList();
            this.filtered = new ArrayList();
            this.selectedProject = null;
            this.status = "Search failed";
         } finally {
            this.loading = false;
         }

      }, "optix-modrinth-search")).start();
   }

   private void applyFilters() {
      this.filtered.clear();
      String q = this.searchBar == null ? "" : this.searchBar.method_1882().trim().toLowerCase(Locale.ROOT);
      String cat = this.category == null ? "all" : this.category.toLowerCase(Locale.ROOT);

      for(ModrinthAPI.Project p : this.projects) {
         if (p != null) {
            boolean queryOk = q.isEmpty() || safe(p.title).toLowerCase(Locale.ROOT).contains(q) || safe(p.description).toLowerCase(Locale.ROOT).contains(q) || safe(p.author).toLowerCase(Locale.ROOT).contains(q) || safe(p.slug).toLowerCase(Locale.ROOT).contains(q);
            boolean categoryOk = "all".equals(cat) || this.matchesCategory(p, cat);
            if (queryOk && categoryOk) {
               this.filtered.add(p);
            }
         }
      }

   }

   private boolean matchesCategory(ModrinthAPI.Project p, String cat) {
      for(String c : p.categories) {
         if (c != null && c.toLowerCase(Locale.ROOT).contains(cat)) {
            return true;
         }
      }

      String title = safe(p.title).toLowerCase(Locale.ROOT);
      String desc = safe(p.description).toLowerCase(Locale.ROOT);
      return title.contains(cat) || desc.contains(cat);
   }

   private void scanInstalled() {
      this.installedKeys.clear();

      try {
         Path modsDir = FabricLoader.getInstance().getGameDir().resolve("mods");
         if (!Files.exists(modsDir, new LinkOption[0])) {
            return;
         }

         Stream<Path> stream = Files.list(modsDir);

         try {
            stream.forEach((path) -> this.installedKeys.add(path.getFileName().toString().toLowerCase(Locale.ROOT)));
         } catch (Throwable var6) {
            if (stream != null) {
               try {
                  stream.close();
               } catch (Throwable var5) {
                  var6.addSuppressed(var5);
               }
            }

            throw var6;
         }

         if (stream != null) {
            stream.close();
         }
      } catch (Exception var7) {
      }

   }

   public void method_25394(class_332 ctx, int mouseX, int mouseY, float delta) {
      this.method_25420(ctx, mouseX, mouseY, delta);
      ctx.method_27535(this.field_22793, class_2561.method_43470("Modrinth Browser"), this.field_22789 / 2 - 52, 4, 16777215);
      ctx.method_27535(this.field_22793, class_2561.method_43470(this.status), 16, 8, 11053224);
      this.searchBar.method_25394(ctx, mouseX, mouseY, delta);
      if (this.searchBar.method_25370()) {
         ctx.method_25294(this.searchBar.method_46426() - 2, this.searchBar.method_46427() - 2, this.searchBar.method_46426() + this.searchBar.method_25368() + 2, this.searchBar.method_46427() + this.searchBar.method_25364() + 2, 1149989887);
      }

      int topY = 46;
      this.drawButton(ctx, 16, topY, 68, 20, "Search", -7638017);
      this.drawButton(ctx, 90, topY, 68, 20, "Reload", -14472645);
      this.drawChip(ctx, 170, topY, "Fabric", "fabric".equals(this.loaderFilter));
      this.drawChip(ctx, 236, topY, "Any", "any".equals(this.loaderFilter));
      int catY = 72;
      int cx = 16;

      for(String c : CATEGORIES) {
         this.drawChip(ctx, cx, catY, c.substring(0, 1).toUpperCase(Locale.ROOT) + c.substring(1), c.equals(this.category));
         cx += Math.max(42, this.field_22793.method_1727(c) + 18) + 8;
      }

      int leftX = 16;
      int leftY = 102;
      int leftW = this.field_22789 * 2 / 3 - 24;
      int leftH = this.field_22790 - leftY - 16;
      int rightX = leftX + leftW + 12;
      int rightW = this.field_22789 - rightX - 16;
      ctx.method_25294(leftX, leftY, leftX + leftW, leftY + leftH, -1442050282);
      ctx.method_25294(rightX, leftY, rightX + rightW, leftY + leftH, -1442050282);
      if (this.loading) {
         ctx.method_27535(this.field_22793, class_2561.method_43470("Loading..."), leftX + 16, leftY + 16, -1);
         super.method_25394(ctx, mouseX, mouseY, delta);
      } else {
         this.applyFilters();
         int cardX = leftX + 10;
         int cardY = leftY + 10 - (int)this.scroll;
         int cardW = leftW - 20;
         int cardH = 72;
         int gap = 10;
         ctx.method_44379(leftX, leftY, leftX + leftW, leftY + leftH);

         for(ModrinthAPI.Project p : this.filtered) {
            if (cardY + cardH >= leftY && cardY <= leftY + leftH) {
               boolean hover = this.inside((double)mouseX, (double)mouseY, cardX, cardY, cardW, cardH);
               boolean selected = this.selectedProject != null && this.selectedProject.id != null && this.selectedProject.id.equals(p.id);
               int bg = selected ? -15262936 : -15657695;
               int border = selected ? -7638017 : (hover ? -11576716 : -14472645);
               ctx.method_25294(cardX, cardY, cardX + cardW, cardY + cardH, bg | -16777216);
               this.drawBorder(ctx, cardX, cardY, cardW, cardH, border | -16777216);
               int badge = badgeColorFor(p.id);
               ctx.method_25294(cardX + 8, cardY + 8, cardX + 32, cardY + 32, badge);
               String initial = initialOf(p.title);
               int initX = cardX + 8 + (24 - this.field_22793.method_1727(initial)) / 2;
               int initY = cardY + 8 + 8;
               ctx.method_27535(this.field_22793, class_2561.method_43470(initial), initX, initY, 16777215);
               ctx.method_27535(this.field_22793, class_2561.method_43470(safe(p.title)), cardX + 40, cardY + 9, 16777215);
               ctx.method_27535(this.field_22793, class_2561.method_43470("by " + safe(p.author)), cardX + 40, cardY + 23, -1);
               String desc = safe(p.description);
               if (desc.length() > 68) {
                  desc = desc.substring(0, 68) + "...";
               }

               ctx.method_27535(this.field_22793, class_2561.method_43470(desc), cardX + 40, cardY + 38, -3092272);
               int btnW = 82;
               int btnH = 22;
               int btnX = cardX + cardW - btnW - 10;
               int btnY = cardY + 25;
               boolean queuedNow = this.queued.contains(p.id);
               boolean installingNow = p.id.equals(this.currentInstallingId);
               int fill = installingNow ? -11184811 : (queuedNow ? -13733986 : -7638017);
               String label = installingNow ? "Installing" : (queuedNow ? "Queued" : (this.isInstalled(p) ? "Installed" : "Install"));
               ctx.method_25294(btnX, btnY, btnX + btnW, btnY + btnH, fill);
               this.drawCentered(ctx, label, btnX + btnW / 2, btnY + 7, -1);
               Float prog = (Float)this.progressMap.get(p.id);
               if (prog != null) {
                  int pw = (int)((float)(cardW - 20) * prog);
                  ctx.method_25294(cardX + 10, cardY + cardH - 6, cardX + 10 + pw, cardY + cardH - 2, -16711936);
               }

               cardY += cardH + gap;
            } else {
               cardY += cardH + gap;
            }
         }

         ctx.method_27535(this.field_22793, class_2561.method_43470("Details"), rightX + 12, leftY + 12, 16777215);
         if (this.selectedProject != null) {
            ctx.method_27535(this.field_22793, class_2561.method_43470(this.selectedProject.title), rightX + 12, leftY + 30, 16777215);
            class_327 var10001 = this.field_22793;
            String var10002 = safe(this.selectedProject.author);
            ctx.method_27535(var10001, class_2561.method_43470("Author: " + var10002), rightX + 12, leftY + 46, -1);
            ctx.method_27535(this.field_22793, class_2561.method_43470("Loader: Fabric"), rightX + 12, leftY + 62, -1);
            var10001 = this.field_22793;
            var10002 = safe(this.selectedProject.slug);
            ctx.method_27535(var10001, class_2561.method_43470("Slug: " + var10002), rightX + 12, leftY + 78, -1);
            int var53 = this.selectedProject.downloads;
            ctx.method_27535(this.field_22793, class_2561.method_43470("Downloads: " + var53), rightX + 12, leftY + 94, -1);
            String desc = safe(this.selectedProject.description);
            this.drawWrapped(ctx, desc, rightX + 12, leftY + 118, rightW - 24, 16777215);
            int btnY = leftY + leftH - 42;
            this.drawButton(ctx, rightX + 12, btnY, rightW - 24, 24, this.isInstalled(this.selectedProject) ? "Reinstall" : "Install Selected", -7638017);
            if (this.currentInstallingId != null && this.currentInstallingId.equals(this.selectedProject.id)) {
               int px = rightX + 12;
               int py = btnY - 14;
               int pw = rightW - 24;
               ctx.method_25294(px, py, px + pw, py + 6, -14472645);
               Float prog = (Float)this.progressMap.get(this.selectedProject.id);
               if (prog != null) {
                  ctx.method_25294(px, py, px + (int)((float)pw * prog), py + 6, -16711936);
               }
            }
         } else {
            ctx.method_27535(this.field_22793, class_2561.method_43470("Pick a project from the list."), rightX + 12, leftY + 30, -1);
         }

         super.method_25394(ctx, mouseX, mouseY, delta);
      }
   }

   public boolean method_25402(class_11909 click, boolean doubled) {
      double mouseX = click.comp_4798();
      double mouseY = click.comp_4799();
      int button = click.method_74245();
      if (button == 0 && this.searchBar.method_25405(mouseX, mouseY)) {
         this.searchBar.method_25365(true);
         return true;
      } else {
         this.searchBar.method_25365(false);
         if (button != 0) {
            return super.method_25402(click, doubled);
         } else {
            int topY = 46;
            if (this.inside(mouseX, mouseY, 16, topY, 68, 20)) {
               this.reload();
               return true;
            } else if (this.inside(mouseX, mouseY, 90, topY, 68, 20)) {
               this.reload();
               return true;
            } else if (this.inside(mouseX, mouseY, 170, topY, 56, 20)) {
               this.loaderFilter = "fabric";
               this.reload();
               return true;
            } else if (this.inside(mouseX, mouseY, 236, topY, 44, 20)) {
               this.loaderFilter = "any";
               this.reload();
               return true;
            } else {
               int catY = 72;
               int cx = 16;

               for(String c : CATEGORIES) {
                  class_327 var10001 = this.field_22793;
                  String var10002 = c.substring(0, 1).toUpperCase(Locale.ROOT);
                  int w = Math.max(42, var10001.method_1727(var10002 + c.substring(1)) + 16);
                  if (this.inside(mouseX, mouseY, cx, catY, w, 20)) {
                     this.category = c;
                     this.applyFilters();
                     return true;
                  }

                  cx += w + 8;
               }

               if (this.projects == null) {
                  return true;
               } else {
                  int listX = 16;
                  int listY = 102;
                  int listW = this.field_22789 * 2 / 3 - 24;
                  int cardX = listX + 10;
                  int cardY = listY + 10 - (int)this.scroll;
                  int cardW = listW - 20;
                  int cardH = 72;
                  int gap = 10;

                  for(ModrinthAPI.Project p : this.filtered) {
                     int btnW = 82;
                     int btnH = 22;
                     int btnX = cardX + cardW - btnW - 10;
                     int btnY = cardY + 25;
                     if (this.inside(mouseX, mouseY, btnX, btnY, btnW, btnH)) {
                        this.selectedProject = p;
                        this.enqueueInstall(p);
                        return true;
                     }

                     if (this.inside(mouseX, mouseY, cardX, cardY, cardW, cardH)) {
                        this.selectedProject = p;
                        return true;
                     }

                     cardY += cardH + gap;
                  }

                  return true;
               }
            }
         }
      }
   }

   public boolean method_25401(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
      int leftY = 102;
      int leftH = this.field_22790 - leftY - 16;
      int leftX = 16;
      int leftW = this.field_22789 * 2 / 3 - 24;
      if (!this.inside(mouseX, mouseY, leftX, leftY, leftW, leftH)) {
         return super.method_25401(mouseX, mouseY, horizontalAmount, verticalAmount);
      } else {
         float contentHeight = (float)this.filtered.size() * 82.0F;
         float maxScroll = Math.max(0.0F, contentHeight - (float)(leftH - 20));
         this.scroll -= (float)verticalAmount * 20.0F;
         if (this.scroll < 0.0F) {
            this.scroll = 0.0F;
         }

         if (this.scroll > maxScroll) {
            this.scroll = maxScroll;
         }

         return true;
      }
   }

   private void enqueueInstall(ModrinthAPI.Project p) {
      if (p != null && p.id != null && !p.id.isBlank()) {
         if (!this.queued.contains(p.id) && !p.id.equals(this.currentInstallingId)) {
            this.queued.add(p.id);
            this.queue.addLast(new InstallTask(p.id, p.title, "mods", this.loaderFilter));
            this.status = "Queued: " + p.title;

            for(String depId : ModrinthAPI.getDependencies(p.id)) {
               if (!this.queued.contains(depId)) {
                  this.queued.add(depId);
                  this.queue.addLast(new InstallTask(depId, depId, "mods", this.loaderFilter));
               }
            }

            this.processQueue();
         }
      }
   }

   private void processQueue() {
      if (!this.processingQueue) {
         this.processingQueue = true;
         (new Thread(() -> {
            try {
               while(true) {
                  if (!this.queue.isEmpty()) {
                     InstallTask task = (InstallTask)this.queue.removeFirst();
                     this.currentInstallingId = task.projectId;
                     this.progressMap.put(task.projectId, 0.0F);
                     this.status = "Installing: " + task.displayName;
                     String mcVersion = class_310.method_1551().method_1515();
                     String url = ModrinthAPI.getDownloadUrl(task.projectId, mcVersion, task.loader);
                     if (url != null && !url.isBlank()) {
                        Path folder = this.mc.field_1697.toPath().resolve(task.installFolder);
                        Files.createDirectories(folder);
                        String fileName = safe(task.displayName).replaceAll("[^a-zA-Z0-9._-]", "_");
                        String ext = this.guessExtension(url, task.installFolder);
                        Path target = folder.resolve(fileName + ext);
                        URLConnection connection = (new URL(url)).openConnection();
                        long size = connection.getContentLengthLong();
                        InputStream in = connection.getInputStream();

                        try {
                           OutputStream out = Files.newOutputStream(target, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);

                           try {
                              byte[] buffer = new byte[8192];
                              long total = 0L;

                              int read;
                              while((read = in.read(buffer)) != -1) {
                                 out.write(buffer, 0, read);
                                 total += (long)read;
                                 if (size > 0L) {
                                    this.progressMap.put(task.projectId, Math.min(1.0F, (float)total / (float)size));
                                 }
                              }
                           } catch (Throwable var26) {
                              if (out != null) {
                                 try {
                                    out.close();
                                 } catch (Throwable var25) {
                                    var26.addSuppressed(var25);
                                 }
                              }

                              throw var26;
                           }

                           if (out != null) {
                              out.close();
                           }
                        } catch (Throwable var27) {
                           if (in != null) {
                              try {
                                 in.close();
                              } catch (Throwable var24) {
                                 var27.addSuppressed(var24);
                              }
                           }

                           throw var27;
                        }

                        if (in != null) {
                           in.close();
                        }

                        this.progressMap.put(task.projectId, 1.0F);
                        this.fileByProject.put(task.projectId, target.getFileName().toString().toLowerCase(Locale.ROOT));
                        this.installedKeys.add(target.getFileName().toString().toLowerCase(Locale.ROOT));
                     } else {
                        this.progressMap.remove(task.projectId);
                     }
                  } else {
                     this.status = "Done";
                     break;
                  }
               }
            } catch (Exception e) {
               e.printStackTrace();
               this.status = "Install failed";
            } finally {
               this.currentInstallingId = null;
               this.processingQueue = false;
            }

         }, "optix-install-queue")).start();
      }
   }

   private boolean isInstalled(ModrinthAPI.Project p) {
      String titleKey = safe(p.title).toLowerCase(Locale.ROOT).replaceAll("[^a-z0-9]+", "_");
      String slugKey = safe(p.slug).toLowerCase(Locale.ROOT).replaceAll("[^a-z0-9]+", "_");

      for(String f : this.installedKeys) {
         if (f.contains(titleKey) || f.contains(slugKey)) {
            return true;
         }
      }

      return false;
   }

   private String guessExtension(String url, String folder) {
      String lower = url.toLowerCase(Locale.ROOT);
      if (lower.contains(".jar")) {
         return ".jar";
      } else if (lower.contains(".zip")) {
         return ".zip";
      } else {
         return "mods".equalsIgnoreCase(folder) ? ".jar" : ".zip";
      }
   }

   private void drawButton(class_332 ctx, int x, int y, int w, int h, String label, int fill) {
      ctx.method_25294(x, y, x + w, y + h, fill);
      this.drawBorder(ctx, x, y, w, h, -14472645);
      this.drawCentered(ctx, label, x + w / 2, y + 7, -1);
   }

   private void drawChip(class_332 ctx, int x, int y, String label, boolean selected) {
      int w = Math.max(36, this.field_22793.method_1727(label) + 16);
      int fill = selected ? -7638017 : -14472645;
      ctx.method_25294(x, y, x + w, y + 20, fill);
      this.drawCentered(ctx, label, x + w / 2, y + 6, -1);
   }

   private void drawBorder(class_332 ctx, int x, int y, int w, int h, int color) {
      ctx.method_25294(x, y, x + w, y + 1, color);
      ctx.method_25294(x, y + h - 1, x + w, y + h, color);
      ctx.method_25294(x, y, x + 1, y + h, color);
      ctx.method_25294(x + w - 1, y, x + w, y + h, color);
   }

   private void drawCentered(class_332 ctx, String text, int centerX, int y, int color) {
      ctx.method_27535(this.field_22793, class_2561.method_43470(text), centerX - this.field_22793.method_1727(text) / 2, y, color);
   }

   private void drawWrapped(class_332 ctx, String text, int x, int y, int maxWidth, int color) {
      if (text != null && !text.isBlank()) {
         String[] words = text.split(" ");
         StringBuilder line = new StringBuilder();
         int yy = y;

         for(String word : words) {
            String test = line.isEmpty() ? word : String.valueOf(line) + " " + word;
            if (this.field_22793.method_1727(test) > maxWidth) {
               ctx.method_27535(this.field_22793, class_2561.method_43470(line.toString()), x, yy, color);
               Objects.requireNonNull(this.field_22793);
               yy += 9 + 2;
               line = new StringBuilder(word);
            } else {
               if (!line.isEmpty()) {
                  line.append(' ');
               }

               line.append(word);
            }
         }

         if (!line.isEmpty()) {
            ctx.method_27535(this.field_22793, class_2561.method_43470(line.toString()), x, yy, color);
         }

      }
   }

   private boolean inside(double mx, double my, int x, int y, int w, int h) {
      return mx >= (double)x && mx <= (double)(x + w) && my >= (double)y && my <= (double)(y + h);
   }

   private static float clamp(float value, float min, float max) {
      return Math.max(min, Math.min(max, value));
   }

   private static String safe(String s) {
      return s == null ? "" : s;
   }

   private static String initialOf(String s) {
      return s != null && !s.isBlank() ? String.valueOf(Character.toUpperCase(s.trim().charAt(0))) : "?";
   }

   private static int badgeColorFor(String id) {
      int h = id == null ? 0 : id.hashCode();
      int r = 96 + Math.abs(h) % 128;
      int g = 96 + Math.abs(h / 3) % 128;
      int b = 96 + Math.abs(h / 7) % 128;
      return -16777216 | r << 16 | g << 8 | b;
   }

   private static final class InstallTask {
      final String projectId;
      final String displayName;
      final String installFolder;
      final String loader;

      InstallTask(String projectId, String displayName, String installFolder, String loader) {
         this.projectId = projectId;
         this.displayName = displayName;
         this.installFolder = installFolder;
         this.loader = loader;
      }
   }
}
