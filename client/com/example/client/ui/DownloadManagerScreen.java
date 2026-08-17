package com.example.client.ui;

import com.example.client.util.ModrinthAPI;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import javax.imageio.ImageIO;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.class_1011;
import net.minecraft.class_1043;
import net.minecraft.class_10799;
import net.minecraft.class_11905;
import net.minecraft.class_11908;
import net.minecraft.class_11909;
import net.minecraft.class_155;
import net.minecraft.class_2561;
import net.minecraft.class_2960;
import net.minecraft.class_310;
import net.minecraft.class_332;
import net.minecraft.class_342;
import net.minecraft.class_437;

public class DownloadManagerScreen extends class_437 {
   private static final Map<String, List<ModrinthAPI.Project>> SEARCH_CACHE = new ConcurrentHashMap();
   private static final Map<String, List<String>> GALLERY_CACHE = new ConcurrentHashMap();
   private final class_310 mc = class_310.method_1551();
   private final String type;
   private final String projectType;
   private final String installFolder;
   private final String loaderFilter;
   private class_342 searchBar;
   private List<ModrinthAPI.Project> projects = new ArrayList();
   private ModrinthAPI.Project selectedProject = null;
   private volatile boolean loading = true;
   private volatile boolean searchFocused = false;
   private volatile boolean loadingGallery = false;
   private volatile String statusText = "Search Modrinth";
   private float scroll = 0.0F;
   private volatile String installingId = null;
   private volatile float installProgress = 0.0F;
   private List<String> galleryUrls = new ArrayList();
   private int galleryIndex = 0;
   private final Map<String, TextureRef> textureCache = new ConcurrentHashMap();
   private final Set<String> loadingTextures = ConcurrentHashMap.newKeySet();
   private final Path cacheDir = FabricLoader.getInstance().getGameDir().resolve("optix-cache");

   public DownloadManagerScreen(String type) {
      super(class_2561.method_43470("Downloader"));
      this.type = type;
      if ("shaders".equalsIgnoreCase(type)) {
         this.projectType = "shader";
         this.installFolder = "shaderpacks";
         this.loaderFilter = "any";
      } else if ("resourcepacks".equalsIgnoreCase(type)) {
         this.projectType = "resourcepack";
         this.installFolder = "resourcepacks";
         this.loaderFilter = "any";
      } else {
         this.projectType = "mod";
         this.installFolder = "mods";
         this.loaderFilter = "fabric";
      }

   }

   protected void method_25426() {
      super.method_25426();
      this.searchBar = new class_342(this.field_22793, this.field_22789 / 2 - 140, 20, 280, 20, class_2561.method_43470("Search"));
      this.searchBar.method_1880(48);
      this.searchBar.method_47404(class_2561.method_43470("Search"));
      this.searchBar.method_71503(false);
      this.reloadProjects();
   }

   private void reloadProjects() {
      String querySnapshot = this.searchBar == null ? "" : this.searchBar.method_1882().trim();
      String query = querySnapshot.isEmpty() ? "" : querySnapshot;
      String var10000 = this.projectType;
      String cacheKey = var10000 + "|" + query.toLowerCase(Locale.ROOT);
      List<ModrinthAPI.Project> cached = (List)SEARCH_CACHE.get(cacheKey);
      if (cached != null) {
         this.mc.execute(() -> {
            this.projects = new ArrayList(cached);
            this.selectedProject = this.projects.isEmpty() ? null : (ModrinthAPI.Project)this.projects.get(0);
            this.statusText = this.projects.isEmpty() ? "No results" : this.projects.size() + " results";
            this.loading = false;
            this.scroll = 0.0F;
            this.preloadProjectIcons(this.projects);
            if (this.selectedProject != null) {
               this.loadProjectGallery(this.selectedProject.id);
            }

         });
      } else {
         this.loading = true;
         this.statusText = "Searching Modrinth...";
         (new Thread(() -> {
            try {
               List<ModrinthAPI.Project> fresh = ModrinthAPI.search(query, this.projectType);
               SEARCH_CACHE.put(cacheKey, new ArrayList(fresh));
               this.mc.execute(() -> {
                  this.projects = fresh;
                  this.selectedProject = fresh.isEmpty() ? null : (ModrinthAPI.Project)fresh.get(0);
                  this.statusText = fresh.isEmpty() ? "No results" : fresh.size() + " results";
                  this.scroll = 0.0F;
                  this.loading = false;
                  this.preloadProjectIcons(fresh);
                  if (this.selectedProject != null) {
                     this.loadProjectGallery(this.selectedProject.id);
                  }

               });
            } catch (Exception e) {
               e.printStackTrace();
               this.mc.execute(() -> {
                  this.projects = new ArrayList();
                  this.selectedProject = null;
                  this.statusText = "Search failed";
                  this.loading = false;
               });
            }

         }, "optix-modrinth-search")).start();
      }
   }

   public void method_25394(class_332 context, int mouseX, int mouseY, float delta) {
      context.method_25294(0, 0, this.field_22789, this.field_22790, -871822576);
      context.method_27535(this.field_22793, class_2561.method_43470("Modrinth " + this.type), this.field_22789 / 2 - 40, 6, 16777215);
      context.method_27535(this.field_22793, class_2561.method_43470(this.statusText), 18, 18, 11053224);
      if (this.searchBar != null) {
         this.searchBar.method_25394(context, mouseX, mouseY, delta);
      }

      int topY = 46;
      this.drawButton(context, 18, topY, 68, 20, "Search");
      this.drawButton(context, 92, topY, 68, 20, "Reload");
      int listX = 18;
      int listY = 102;
      int listW = this.field_22789 * 2 / 3 - 30;
      int listH = this.field_22790 - listY - 18;
      int detailsX = listX + listW + 12;
      int detailsW = this.field_22789 - detailsX - 18;
      context.method_25294(listX, listY, listX + listW, listY + listH, -1442050282);
      context.method_25294(detailsX, listY, detailsX + detailsW, listY + listH, -1442050282);
      if (this.loading) {
         context.method_27535(this.field_22793, class_2561.method_43470("Loading..."), listX + 16, listY + 16, -1);
         super.method_25394(context, mouseX, mouseY, delta);
      } else {
         float maxScroll = this.getMaxScroll(listH);
         if (this.scroll < 0.0F) {
            this.scroll = 0.0F;
         }

         if (this.scroll > maxScroll) {
            this.scroll = maxScroll;
         }

         int cardX = listX + 10;
         int cardY = listY + 10 - (int)this.scroll;
         int cardW = listW - 20;
         int cardH = 72;
         int gap = 10;
         context.method_44379(listX, listY, listX + listW - 4, listY + listH);

         for(ModrinthAPI.Project p : this.projects) {
            if (cardY + cardH >= listY && cardY <= listY + listH) {
               boolean hover = this.inside((double)mouseX, (double)mouseY, cardX, cardY, cardW, cardH);
               boolean selected = this.selectedProject != null && this.selectedProject.id != null && this.selectedProject.id.equals(p.id);
               int bg = selected ? -15262936 : -15657695;
               int border = selected ? -7638017 : (hover ? -11576716 : -14472645);
               context.method_25294(cardX, cardY, cardX + cardW, cardY + cardH, bg);
               this.drawBorder(context, cardX, cardY, cardW, cardH, border);
               this.drawIcon(context, p, cardX + 8, cardY + 8);
               String desc = safe(p.description);
               if (desc.length() > 68) {
                  desc = desc.substring(0, 68) + "...";
               }

               context.method_51439(this.field_22793, class_2561.method_43470(safe(p.title)), cardX + 40, cardY + 10, -1, false);
               context.method_51439(this.field_22793, class_2561.method_43470("by " + safe(p.author)), cardX + 40, cardY + 24, -1644826, false);
               context.method_51439(this.field_22793, class_2561.method_43470(desc), cardX + 40, cardY + 38, -4669228, false);
               int btnW = 84;
               int btnH = 22;
               int btnX = cardX + cardW - btnW - 10;
               int btnY = cardY + 25;
               String safeName = safeFileName(p.title);
               boolean installed = this.isInstalled(safeName);
               boolean installingNow = p.id != null && p.id.equals(this.installingId);
               int btnColor = installed ? -13710223 : (installingNow ? -12303292 : -7638017);
               String label = installed ? "Installed" : (installingNow ? "..." : "Install");
               context.method_25294(btnX, btnY, btnX + btnW, btnY + btnH, btnColor);
               context.method_27534(this.field_22793, class_2561.method_43470(label), btnX + btnW / 2, btnY + 7, -1);
               if (installingNow && this.installProgress > 0.0F) {
                  context.method_25294(cardX + 10, cardY + cardH - 6, cardX + 10 + (int)((float)(cardW - 20) * this.installProgress), cardY + cardH - 2, -16711936);
               }

               cardY += cardH + gap;
            } else {
               cardY += cardH + gap;
            }
         }

         context.method_44380();
         if (maxScroll > 0.0F) {
            int barX = listX + listW - 6;
            int handleH = Math.max(20, (int)((float)listH * ((float)listH / ((float)listH + maxScroll))));
            int handleY = (int)((float)listY + this.scroll / maxScroll * (float)(listH - handleH));
            context.method_25294(barX, listY, barX + 2, listY + listH, 570425344);
            context.method_25294(barX, handleY, barX + 2, handleY + handleH, -7638017);
         }

         context.method_27535(this.field_22793, class_2561.method_43470("Details"), detailsX + 12, listY + 12, 16777215);
         if (this.selectedProject != null) {
            context.method_27535(this.field_22793, class_2561.method_43470(safe(this.selectedProject.title)), detailsX + 12, listY + 30, 16777215);
            context.method_27535(this.field_22793, class_2561.method_43470("Author: " + safe(this.selectedProject.author)), detailsX + 12, listY + 46, -1);
            context.method_27535(this.field_22793, class_2561.method_43470("Type: " + this.projectType), detailsX + 12, listY + 62, -1);
            context.method_27535(this.field_22793, class_2561.method_43470("Install folder: " + this.installFolder), detailsX + 12, listY + 78, -1);
            int previewX = detailsX + 12;
            int previewY = listY + 104;
            int previewW = detailsW - 24;
            int previewH = 120;
            if (this.loadingGallery) {
               context.method_25294(previewX, previewY, previewX + previewW, previewY + previewH, -15657695);
               context.method_27535(this.field_22793, class_2561.method_43470("Loading preview..."), previewX + 10, previewY + 10, -1);
            } else if (!this.galleryUrls.isEmpty()) {
               this.drawGallery(context, previewX, previewY, previewW, previewH);
            } else {
               context.method_25294(previewX, previewY, previewX + previewW, previewY + previewH, -15657695);
               context.method_27535(this.field_22793, class_2561.method_43470("No gallery image"), previewX + 10, previewY + 10, -1);
            }

            String desc = safe(this.selectedProject.description);
            this.drawWrapped(context, desc, detailsX + 12, previewY + previewH + 10, detailsW - 24, 16777215);
            int btnY = listY + listH - 40;
            this.drawButton(context, detailsX + 12, btnY, detailsW - 24, 24, "Install Selected");
            String safeName = safeFileName(this.selectedProject.title);
            boolean installed = this.isInstalled(safeName);
            boolean installingNow = this.selectedProject.id != null && this.selectedProject.id.equals(this.installingId);
            if (installingNow && this.installProgress > 0.0F) {
               int px = detailsX + 12;
               int py = btnY - 14;
               int pw = detailsW - 24;
               context.method_25294(px, py, px + pw, py + 6, -14472645);
               context.method_25294(px, py, px + (int)((float)pw * this.installProgress), py + 6, -16711936);
            }

            context.method_27535(this.field_22793, class_2561.method_43470(installed ? "Installed" : (installingNow ? "Installing..." : "")), detailsX + 12, btnY - 24, installed ? -13710223 : 11053224);
         } else {
            context.method_27535(this.field_22793, class_2561.method_43470("Pick a project from the list."), detailsX + 12, listY + 32, -1);
         }

         super.method_25394(context, mouseX, mouseY, delta);
      }
   }

   public boolean method_25402(class_11909 click, boolean doubled) {
      double mouseX = click.comp_4798();
      double mouseY = click.comp_4799();
      int button = click.method_74245();
      if (this.searchBar != null && this.searchBar.method_25402(click, doubled)) {
         this.searchFocused = true;
         this.searchBar.method_25365(true);
         return true;
      } else {
         if (this.searchBar != null) {
            this.searchFocused = false;
            this.searchBar.method_25365(false);
         }

         if (button != 0) {
            return super.method_25402(click, doubled);
         } else {
            int topY = 46;
            if (this.inside(mouseX, mouseY, 18, topY, 68, 20)) {
               this.reloadProjects();
               return true;
            } else if (this.inside(mouseX, mouseY, 92, topY, 68, 20)) {
               this.reloadProjects();
               return true;
            } else {
               int listX = 18;
               int listY = 102;
               int listW = this.field_22789 * 2 / 3 - 30;
               int cardX = listX + 10;
               int cardY = listY + 10 - (int)this.scroll;
               int cardW = listW - 20;
               int cardH = 72;
               int gap = 10;

               for(ModrinthAPI.Project p : this.projects) {
                  int btnW = 84;
                  int btnH = 22;
                  int btnX = cardX + cardW - btnW - 10;
                  int btnY = cardY + 25;
                  String safeName = safeFileName(p.title);
                  if (this.inside(mouseX, mouseY, btnX, btnY, btnW, btnH)) {
                     this.selectedProject = p;
                     this.loadProjectGallery(p.id);
                     if (!this.isInstalled(safeName)) {
                        this.installProject(p);
                     }

                     return true;
                  }

                  if (this.inside(mouseX, mouseY, cardX, cardY, cardW, cardH)) {
                     this.selectedProject = p;
                     this.loadProjectGallery(p.id);
                     return true;
                  }

                  cardY += cardH + gap;
               }

               if (this.selectedProject != null && !this.galleryUrls.isEmpty()) {
                  int previewX = listX + listW + 12 + 12;
                  int previewY = listY + 104;
                  int previewW = this.field_22789 - (listX + listW + 12) - 18 - 24;
                  int previewH = 120;
                  if (this.inside(mouseX, mouseY, previewX, previewY, 24, previewH)) {
                     --this.galleryIndex;
                     if (this.galleryIndex < 0) {
                        this.galleryIndex = this.galleryUrls.size() - 1;
                     }

                     return true;
                  }

                  if (this.inside(mouseX, mouseY, previewX + previewW - 24, previewY, 24, previewH)) {
                     ++this.galleryIndex;
                     if (this.galleryIndex >= this.galleryUrls.size()) {
                        this.galleryIndex = 0;
                     }

                     return true;
                  }
               }

               return super.method_25402(click, doubled);
            }
         }
      }
   }

   public boolean method_25401(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
      int listX = 18;
      int listY = 102;
      int listW = this.field_22789 * 2 / 3 - 30;
      int listH = this.field_22790 - listY - 18;
      int detailsX = listX + listW + 12;
      int detailsW = this.field_22789 - detailsX - 18;
      int previewX = detailsX + 12;
      int previewY = listY + 104;
      int previewW = detailsW - 24;
      int previewH = 120;
      if (this.selectedProject != null && !this.galleryUrls.isEmpty() && this.inside(mouseX, mouseY, previewX, previewY, previewW, previewH) && this.galleryUrls.size() > 1) {
         if (verticalAmount > (double)0.0F) {
            --this.galleryIndex;
            if (this.galleryIndex < 0) {
               this.galleryIndex = this.galleryUrls.size() - 1;
            }
         } else if (verticalAmount < (double)0.0F) {
            ++this.galleryIndex;
            if (this.galleryIndex >= this.galleryUrls.size()) {
               this.galleryIndex = 0;
            }
         }

         return true;
      } else if (!this.inside(mouseX, mouseY, listX, listY, listW, listH)) {
         return super.method_25401(mouseX, mouseY, horizontalAmount, verticalAmount);
      } else {
         float maxScroll = this.getMaxScroll(listH);
         if (maxScroll <= 0.0F) {
            return true;
         } else {
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
   }

   public boolean method_25404(class_11908 input) {
      if (this.searchFocused) {
         if (input.method_74231()) {
            this.searchFocused = false;
            if (this.searchBar != null) {
               this.searchBar.method_25365(false);
            }

            return true;
         }

         if (this.searchBar != null && this.searchBar.method_25404(input)) {
            if (input.method_74230()) {
               this.reloadProjects();
            }

            return true;
         }

         if (input.method_74230()) {
            this.reloadProjects();
            return true;
         }
      }

      return super.method_25404(input);
   }

   public boolean method_25400(class_11905 input) {
      return this.searchFocused && this.searchBar != null && this.searchBar.method_25400(input) ? true : super.method_25400(input);
   }

   private void installProject(ModrinthAPI.Project p) {
      if (p != null && p.id != null && !p.id.isBlank()) {
         this.installingId = p.id;
         this.installProgress = 0.0F;
         this.statusText = "Installing " + p.title + "...";
         (new Thread(() -> {
            try {
               String mcVersion = class_155.method_16673().comp_4025();
               String url = ModrinthAPI.getDownloadUrl(p.id, mcVersion, this.loaderFilter);
               if (url != null && !url.isBlank()) {
                  Path folder = this.mc.field_1697.toPath().resolve(this.installFolder);
                  Files.createDirectories(folder);
                  String safeName = safeFileName(p.title);
                  String extension = "mods".equalsIgnoreCase(this.installFolder) ? ".jar" : ".zip";
                  Path target = folder.resolve(safeName + extension);
                  URLConnection connection = (new URL(url)).openConnection();
                  long size = connection.getContentLengthLong();
                  InputStream in = connection.getInputStream();

                  try {
                     OutputStream out = Files.newOutputStream(target, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);

                     try {
                        byte[] buffer = new byte[8192];
                        long readTotal = 0L;

                        int read;
                        while((read = in.read(buffer)) != -1) {
                           out.write(buffer, 0, read);
                           readTotal += (long)read;
                           if (size > 0L) {
                              this.installProgress = Math.min(1.0F, (float)readTotal / (float)size);
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

                  this.installProgress = 1.0F;
                  this.statusText = "Installed: " + p.title + " (restart may be required)";
                  return;
               }

               this.statusText = "No compatible file found";
               this.installingId = null;
            } catch (Exception e) {
               e.printStackTrace();
               this.statusText = "Install failed";
               return;
            } finally {
               this.installingId = null;
            }

         }, "optix-modrinth-install")).start();
      }
   }

   private void loadProjectGallery(String projectId) {
      if (projectId != null && !projectId.isBlank()) {
         List<String> cached = (List)GALLERY_CACHE.get(projectId);
         if (cached != null) {
            this.galleryUrls = new ArrayList(cached);
            this.galleryIndex = 0;
            this.loadingGallery = false;
            this.preloadImages(this.galleryUrls);
         } else {
            this.loadingGallery = true;
            this.galleryUrls = new ArrayList();
            this.galleryIndex = 0;
            (new Thread(() -> {
               try {
                  HttpURLConnection conn = (HttpURLConnection)(new URL("https://api.modrinth.com/v2/project/" + projectId)).openConnection();
                  conn.setRequestProperty("User-Agent", "OptiXClient");
                  conn.setConnectTimeout(10000);
                  conn.setReadTimeout(10000);
                  JsonObject root = JsonParser.parseReader(new InputStreamReader(conn.getInputStream())).getAsJsonObject();
                  List<String> fetched = new ArrayList();
                  if (root.has("gallery") && root.get("gallery").isJsonArray()) {
                     for(JsonElement el : root.getAsJsonArray("gallery")) {
                        if (el.isJsonObject()) {
                           JsonObject obj = el.getAsJsonObject();
                           if (obj.has("url") && !obj.get("url").isJsonNull()) {
                              fetched.add(obj.get("url").getAsString());
                           }
                        }
                     }
                  }

                  GALLERY_CACHE.put(projectId, new ArrayList(fetched));
                  this.mc.execute(() -> {
                     if (this.selectedProject != null && projectId.equals(this.selectedProject.id)) {
                        this.galleryUrls = fetched;
                        this.galleryIndex = 0;
                        this.loadingGallery = false;
                        this.preloadImages(this.galleryUrls);
                     }

                  });
               } catch (Exception e) {
                  e.printStackTrace();
                  this.mc.execute(() -> {
                     if (this.selectedProject != null && projectId.equals(this.selectedProject.id)) {
                        this.galleryUrls = new ArrayList();
                        this.loadingGallery = false;
                     }

                  });
               }

            }, "optix-gallery-load")).start();
         }
      }
   }

   private void preloadProjectIcons(List<ModrinthAPI.Project> list) {
      if (list != null) {
         int count = Math.min(12, list.size());

         for(int i = 0; i < count; ++i) {
            ModrinthAPI.Project p = (ModrinthAPI.Project)list.get(i);
            if (p != null && p.iconUrl != null && !p.iconUrl.isBlank()) {
               this.queueTexture(p.iconUrl);
            }
         }

      }
   }

   private void preloadImages(List<String> urls) {
      if (urls != null) {
         int count = Math.min(6, urls.size());

         for(int i = 0; i < count; ++i) {
            String url = (String)urls.get(i);
            if (url != null && !url.isBlank()) {
               this.queueTexture(url);
            }
         }

      }
   }

   private void drawGallery(class_332 context, int x, int y, int w, int h) {
      context.method_25294(x, y, x + w, y + h, -15657695);
      if (this.galleryUrls.isEmpty()) {
         context.method_27535(this.field_22793, class_2561.method_43470("No gallery image"), x + 10, y + 10, -1);
      } else {
         String current = (String)this.galleryUrls.get(Math.max(0, Math.min(this.galleryIndex, this.galleryUrls.size() - 1)));
         this.drawRemoteImage(context, current, x, y, w, h);
         if (this.galleryUrls.size() > 1) {
            context.method_25294(x, y + h / 2 - 12, x + 20, y + h / 2 + 12, 1711276032);
            context.method_25294(x + w - 20, y + h / 2 - 12, x + w, y + h / 2 + 12, 1711276032);
            context.method_27535(this.field_22793, class_2561.method_43470("<"), x + 6, y + h / 2 - 4, 16777215);
            context.method_27535(this.field_22793, class_2561.method_43470(">"), x + w - 11, y + h / 2 - 4, 16777215);
            int dotsW = this.galleryUrls.size() * 8;
            int dotsX = x + w / 2 - dotsW / 2;
            int dotsY = y + h - 12;

            for(int i = 0; i < this.galleryUrls.size(); ++i) {
               int c = i == this.galleryIndex ? -1 : 1728053247;
               context.method_25294(dotsX + i * 8, dotsY, dotsX + i * 8 + 4, dotsY + 4, c);
            }
         }

      }
   }

   private void drawRemoteImage(class_332 context, String url, int x, int y, int w, int h) {
      context.method_25294(x, y, x + w, y + h, -15066598);
      if (url != null && !url.isBlank()) {
         TextureRef ref = (TextureRef)this.textureCache.get(url);
         if (ref != null) {
            try {
               context.method_25290(class_10799.field_56883, ref.id, x, y, 0.0F, 0.0F, w, h, w, h);
            } catch (Exception var9) {
            }

         } else {
            this.queueTexture(url);
         }
      }
   }

   private void queueTexture(String url) {
      if (url != null && !url.isBlank()) {
         if (!this.textureCache.containsKey(url)) {
            if (this.loadingTextures.add(url)) {
               Thread thread = new Thread(() -> {
                  try {
                     Files.createDirectories(this.cacheDir);
                     Path file = this.cacheDir.resolve("img_" + Integer.toHexString(url.hashCode()) + ".png");
                     BufferedImage[] imageBox = new BufferedImage[1];
                     HttpURLConnection conn = (HttpURLConnection)(new URL(url)).openConnection();
                     conn.setRequestProperty("User-Agent", "Mozilla/5.0");
                     conn.setRequestProperty("Accept", "image/png,image/webp,image/*,*/*");
                     conn.setInstanceFollowRedirects(true);
                     conn.setConnectTimeout(10000);
                     conn.setReadTimeout(10000);
                     InputStream in = conn.getInputStream();

                     try {
                        byte[] bytes = in.readAllBytes();
                        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
                        imageBox[0] = ImageIO.read(bais);
                        if (imageBox[0] == null) {
                           class_1011 ni = class_1011.method_4309(new ByteArrayInputStream(bytes));
                           imageBox[0] = new BufferedImage(ni.method_4307(), ni.method_4323(), 2);

                           for(int xx = 0; xx < ni.method_4307(); ++xx) {
                              for(int yy = 0; yy < ni.method_4323(); ++yy) {
                                 imageBox[0].setRGB(xx, yy, ni.method_61940(xx, yy));
                              }
                           }

                           ni.close();
                        }
                     } catch (Throwable var16) {
                        if (in != null) {
                           try {
                              in.close();
                           } catch (Throwable var14) {
                              var16.addSuppressed(var14);
                           }
                        }

                        throw var16;
                     }

                     if (in != null) {
                        in.close();
                     }

                     if (imageBox[0] != null) {
                        try {
                           ImageIO.write(imageBox[0], "png", file.toFile());
                        } catch (Exception var15) {
                        }
                     }

                     if (imageBox[0] == null) {
                        this.loadingTextures.remove(url);
                        return;
                     }

                     class_1011 nativeImage = new class_1011(imageBox[0].getWidth(), imageBox[0].getHeight(), true);

                     for(int x = 0; x < imageBox[0].getWidth(); ++x) {
                        for(int y = 0; y < imageBox[0].getHeight(); ++y) {
                           int argb = imageBox[0].getRGB(x, y);
                           int a = argb >> 24 & 255;
                           int r = argb >> 16 & 255;
                           int g = argb >> 8 & 255;
                           int b = argb & 255;
                           int abgr = a << 24 | b << 16 | g << 8 | r;
                           nativeImage.method_4305(x, y, abgr);
                        }
                     }

                     class_2960 textureId = class_2960.method_60655("optix", "remote_" + Integer.toHexString(url.hashCode()));
                     this.mc.execute(() -> {
                        try {
                           class_1043 texture = new class_1043(() -> "optix_remote", nativeImage);
                           this.mc.method_1531().method_4616(textureId, texture);
                           this.textureCache.put(url, new TextureRef(textureId, imageBox[0].getWidth(), imageBox[0].getHeight()));
                        } catch (Exception e) {
                           e.printStackTrace();
                        }

                        this.loadingTextures.remove(url);
                     });
                  } catch (Exception e) {
                     e.printStackTrace();
                     this.loadingTextures.remove(url);
                  }

               }, "optix-img-loader");
               thread.setDaemon(true);
               thread.start();
            }
         }
      }
   }

   private void drawIcon(class_332 context, ModrinthAPI.Project p, int x, int y) {
      String url = p.iconUrl;
      if (url != null && !url.isBlank()) {
         TextureRef ref = (TextureRef)this.textureCache.get(url);
         if (ref != null) {
            try {
               context.method_25290(class_10799.field_56883, ref.id, x, y, 0.0F, 0.0F, 24, 24, 24, 24);
            } catch (Exception var8) {
            }

         } else {
            this.queueTexture(url);
            this.drawFallbackIcon(context, p, x, y);
         }
      } else {
         this.drawFallbackIcon(context, p, x, y);
      }
   }

   private void drawFallbackIcon(class_332 context, ModrinthAPI.Project p, int x, int y) {
      int hash = Math.abs(safe(p.title).hashCode() * 31 ^ safe(p.author).hashCode());
      int base = -14472645 | (hash & 63) << 16 | (hash >> 6 & 63) << 8 | hash >> 12 & 63;
      context.method_25294(x, y, x + 24, y + 24, base);
      String initial = safe(p.title).isEmpty() ? "?" : String.valueOf(Character.toUpperCase(safe(p.title).charAt(0)));
      context.method_27534(this.field_22793, class_2561.method_43470(initial), x + 12, y + 8, 16777215);
   }

   private float getMaxScroll(int listH) {
      int contentHeight = this.projects.size() * 82;
      return Math.max(0.0F, (float)(contentHeight - (listH - 20)));
   }

   private boolean isInstalled(String safeName) {
      try {
         Path folder = this.mc.field_1697.toPath().resolve(this.installFolder);
         if (!Files.exists(folder, new LinkOption[0])) {
            return false;
         } else {
            String ext = "mods".equalsIgnoreCase(this.installFolder) ? ".jar" : ".zip";
            Path target = folder.resolve(safeName + ext);
            return Files.exists(target, new LinkOption[0]);
         }
      } catch (Exception var5) {
         return false;
      }
   }

   private boolean inside(double mx, double my, int x, int y, int w, int h) {
      return mx >= (double)x && mx <= (double)(x + w) && my >= (double)y && my <= (double)(y + h);
   }

   private void drawButton(class_332 ctx, int x, int y, int w, int h, String label) {
      ctx.method_25294(x, y, x + w, y + h, -7638017);
      this.drawBorder(ctx, x, y, w, h, -14472645);
      ctx.method_27534(this.field_22793, class_2561.method_43470(label), x + w / 2, y + 7, 16777215);
   }

   private void drawBorder(class_332 ctx, int x, int y, int w, int h, int color) {
      ctx.method_25294(x, y, x + w, y + 1, color);
      ctx.method_25294(x, y + h - 1, x + w, y + h, color);
      ctx.method_25294(x, y, x + 1, y + h, color);
      ctx.method_25294(x + w - 1, y, x + w, y + h, color);
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

   private static String safe(String s) {
      return s == null ? "" : s;
   }

   private static String safeFileName(String s) {
      return s == null ? "" : s.replaceAll("[^a-zA-Z0-9._-]", "_");
   }

   private static final class TextureRef {
      final class_2960 id;
      final int width;
      final int height;

      TextureRef(class_2960 id, int width, int height) {
         this.id = id;
         this.width = width;
         this.height = height;
      }
   }
}
