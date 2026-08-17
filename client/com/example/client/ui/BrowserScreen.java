package com.example.client.ui;

import com.example.client.util.ModrinthAPI;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import javax.imageio.ImageIO;
import net.minecraft.class_1011;
import net.minecraft.class_1043;
import net.minecraft.class_10799;
import net.minecraft.class_11905;
import net.minecraft.class_11908;
import net.minecraft.class_11909;
import net.minecraft.class_2561;
import net.minecraft.class_2960;
import net.minecraft.class_310;
import net.minecraft.class_327;
import net.minecraft.class_332;
import net.minecraft.class_342;
import net.minecraft.class_437;

public class BrowserScreen extends class_437 {
   private final class_310 mc = class_310.method_1551();
   private final Map<String, class_2960> iconTextures = new ConcurrentHashMap();
   private final String screenTitle;
   private final String searchType;
   private final String installFolder;
   private final String loaderFilter;
   private class_342 searchBar;
   private List<ModrinthAPI.Project> projects = new ArrayList();
   private String categoryFilter = "all";
   private boolean loading = true;
   private boolean searchFocused = false;
   private String statusText = "Search Modrinth";
   private float scroll = 0.0F;
   private ModrinthAPI.Project selectedProject = null;
   private String installingId = null;
   private float installProgress = 0.0F;
   private final Map<String, Float> installProgressMap = new HashMap();

   public BrowserScreen(String screenTitle, String searchType, String installFolder, String loaderFilter) {
      super(class_2561.method_43470(screenTitle));
      this.screenTitle = screenTitle;
      this.searchType = searchType;
      this.installFolder = installFolder;
      this.loaderFilter = loaderFilter;
   }

   protected void method_25426() {
      this.searchBar = new class_342(this.field_22793, this.field_22789 / 2 - 140, 20, 280, 20, class_2561.method_43470("Search"));
      this.searchBar.method_1852("");
      this.reloadProjects();
   }

   private void reloadProjects() {
      this.loading = true;
      this.statusText = "Searching Modrinth...";
      String querySnapshot;
      if (this.searchBar != null && !this.searchBar.method_1882().trim().isBlank()) {
         querySnapshot = this.searchBar.method_1882().trim();
      } else {
         querySnapshot = this.searchType;
      }

      (new Thread(() -> {
         try {
            List<ModrinthAPI.Project> fetched = ModrinthAPI.search(querySnapshot, this.searchType);
            this.mc.execute(() -> {
               this.projects = fetched;

               for(ModrinthAPI.Project p : fetched) {
                  System.out.println("TITLE = " + p.title);
                  System.out.println("AUTHOR = " + p.author);
                  System.out.println("DESC = " + p.description);
                  System.out.println("ICON = " + p.iconUrl);
               }

               this.selectedProject = this.projects.isEmpty() ? null : (ModrinthAPI.Project)this.projects.get(0);
               this.statusText = this.projects.isEmpty() ? "No results" : this.projects.size() + " results";
               this.scroll = 0.0F;
               this.loading = false;
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

   public void method_25394(class_332 ctx, int mouseX, int mouseY, float delta) {
      ctx.method_25294(0, 0, this.field_22789, this.field_22790, -16184560);
      this.drawCentered(ctx, this.screenTitle, this.field_22789 / 2, 6, 16777215);
      ctx.method_27535(this.field_22793, class_2561.method_43470(this.statusText), 18, 18, 11053224);
      this.searchBar.method_25394(ctx, mouseX, mouseY, delta);
      int topY = 46;
      this.drawButton(ctx, 18, topY, 68, 20, "Search", -7638017);
      this.drawButton(ctx, 92, topY, 68, 20, "Reload", -14472645);
      this.drawChip(ctx, 170, topY, "All", "all".equals(this.categoryFilter));
      this.drawChip(ctx, 214, topY, "PvP", "pvp".equals(this.categoryFilter));
      this.drawChip(ctx, 260, topY, "Clean", "clean".equals(this.categoryFilter));
      this.drawChip(ctx, 316, topY, "FPS", "fps".equals(this.categoryFilter));
      this.drawChip(ctx, 366, topY, "Fancy", "fancy".equals(this.categoryFilter));
      this.drawChip(ctx, 430, topY, "Utility", "utility".equals(this.categoryFilter));
      int listX = 18;
      int listY = 102;
      int listW = this.field_22789 * 2 / 3 - 30;
      int listH = this.field_22790 - listY - 18;
      int detailsX = listX + listW + 12;
      int detailsW = this.field_22789 - detailsX - 18;
      ctx.method_25294(listX, listY, listX + listW, listY + listH, -1442050282);
      ctx.method_25294(detailsX, listY, detailsX + detailsW, listY + listH, -1442050282);
      if (this.loading) {
         ctx.method_27535(this.field_22793, class_2561.method_43470("Loading..."), listX + 16, listY + 16, -1);
         super.method_25394(ctx, mouseX, mouseY, delta);
      } else {
         List<ModrinthAPI.Project> visible = this.filterProjects(this.projects);
         int cardX = listX + 10;
         int cardY = listY + 10 - (int)this.scroll;
         int cardW = listW - 20;
         int cardH = 90;
         int gap = 18;

         for(ModrinthAPI.Project p : visible) {
            if (cardY + cardH >= listY && cardY <= listY + listH) {
               boolean hover = this.inside((double)mouseX, (double)mouseY, cardX, cardY, cardW, cardH);
               boolean selected = this.selectedProject != null && this.selectedProject.id != null && this.selectedProject.id.equals(p.id);
               int bg = selected ? -15262936 : -15657695;
               int border = selected ? -7638017 : (hover ? -11576716 : -14472645);
               ctx.method_25294(cardX, cardY, cardX + cardW, cardY + cardH, bg | -16777216);
               this.drawBorder(ctx, cardX, cardY, cardW, cardH, border | -16777216);
               ctx.method_25294(cardX, cardY, cardX + cardW, cardY + cardH, selected ? -15065298 : -15657695);
               this.drawBorder(ctx, cardX, cardY, cardW, cardH, selected ? -7638017 : -13946555);
               int iconX = cardX + 10;
               int iconY = cardY + 10;
               int iconSize = 32;
               ctx.method_25294(iconX, iconY, iconX + iconSize, iconY + iconSize, -14472645);
               this.drawBorder(ctx, iconX, iconY, iconSize, iconSize, -7638017);
               this.loadIcon(p);
               class_2960 icon = (class_2960)this.iconTextures.get(p.id);
               if (icon != null) {
                  try {
                     ctx.method_25290(class_10799.field_56883, icon, iconX, iconY, 0.0F, 0.0F, iconSize, iconSize, iconSize, iconSize);
                  } catch (Exception e) {
                     e.printStackTrace();
                     String initial = initialOf(p.title);
                     ctx.method_27535(this.field_22793, class_2561.method_43470(initial), iconX + 11, iconY + 12, -1);
                  }
               } else {
                  String initial = initialOf(p.title);
                  ctx.method_27535(this.field_22793, class_2561.method_43470(initial), iconX + 11, iconY + 12, -1);
               }

               String title = safe(p.title);
               if (title.isBlank()) {
                  title = "Unknown Project";
               }

               ctx.method_51433(this.field_22793, title, cardX + 52, cardY + 10, -1, true);
               String author = safe(p.author);
               if (author.isBlank()) {
                  author = "Unknown";
               }

               ctx.method_51433(this.field_22793, "by " + author, cardX + 52, cardY + 24, -3092272, false);
               String desc = safe(p.description);
               if (desc.isBlank()) {
                  desc = "No description available.";
               }

               if (desc.length() > 72) {
                  desc = desc.substring(0, 72) + "...";
               }

               ctx.method_51433(this.field_22793, desc, cardX + 52, cardY + 40, -4671304, false);
               int btnW = 76;
               int btnH = 22;
               int btnX = cardX + cardW - btnW - 12;
               int btnY = cardY + 30;
               ctx.method_25294(btnX, btnY, btnX + btnW, btnY + btnH, -7638017);
               this.drawBorder(ctx, btnX, btnY, btnW, btnH, -9808436);
               ctx.method_51433(this.field_22793, "Install", btnX + 15, btnY + 7, -1, true);
               Float prog = (Float)this.installProgressMap.get(p.id);
               if (prog != null) {
                  int pw = (int)((float)(cardW - 20) * prog);
                  ctx.method_25294(cardX + 10, cardY + cardH - 6, cardX + 10 + pw, cardY + cardH - 2, -16711834);
               }

               cardY += cardH + gap;
            } else {
               cardY += cardH + gap;
            }
         }

         ctx.method_27535(this.field_22793, class_2561.method_43470("Details"), detailsX + 12, listY + 12, 16777215);
         if (this.selectedProject != null) {
            ctx.method_27535(this.field_22793, class_2561.method_43470(safe(this.selectedProject.title)), detailsX + 12, listY + 30, 16777215);
            ctx.method_27535(this.field_22793, class_2561.method_43470("Author: " + safe(this.selectedProject.author)), detailsX + 12, listY + 46, -1);
            ctx.method_27535(this.field_22793, class_2561.method_43470("Type: " + this.searchType), detailsX + 12, listY + 62, -1);
            class_327 var10001 = this.field_22793;
            String var10002 = "any".equals(this.loaderFilter) ? "Any" : this.loaderFilter;
            ctx.method_27535(var10001, class_2561.method_43470("Loader: " + var10002), detailsX + 12, listY + 78, -1);
            String desc = safe(this.selectedProject.description);
            this.drawWrapped(ctx, desc, detailsX + 12, listY + 104, detailsW - 24, -1);
            int btnY = listY + listH - 40;
            this.drawButton(ctx, detailsX + 12, btnY, detailsW - 24, 24, "Install Selected", -7638017);
            if (this.installingId != null && this.installingId.equals(this.selectedProject.id)) {
               int px = detailsX + 12;
               int py = btnY - 14;
               int pw = detailsW - 24;
               ctx.method_25294(px, py, px + pw, py + 6, -14472645);
               ctx.method_25294(px, py, px + (int)((float)pw * this.installProgress), py + 6, -16711936);
            }
         } else {
            ctx.method_27535(this.field_22793, class_2561.method_43470("Pick a project from the list."), detailsX + 12, listY + 32, -1);
         }

         super.method_25394(ctx, mouseX, mouseY, delta);
      }
   }

   public boolean method_25402(class_11909 click, boolean doubled) {
      double mouseX = click.comp_4798();
      double mouseY = click.comp_4799();
      int button = click.method_74245();
      if (this.searchBar.method_25402(click, doubled)) {
         this.searchFocused = true;
         this.searchBar.method_25365(true);
         return true;
      } else {
         this.searchFocused = false;
         this.searchBar.method_25365(false);
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
            } else if (this.inside(mouseX, mouseY, 170, topY, 34, 20)) {
               this.categoryFilter = "all";
               return true;
            } else if (this.inside(mouseX, mouseY, 214, topY, 38, 20)) {
               this.categoryFilter = "pvp";
               return true;
            } else if (this.inside(mouseX, mouseY, 260, topY, 46, 20)) {
               this.categoryFilter = "clean";
               return true;
            } else if (this.inside(mouseX, mouseY, 316, topY, 38, 20)) {
               this.categoryFilter = "fps";
               return true;
            } else if (this.inside(mouseX, mouseY, 366, topY, 46, 20)) {
               this.categoryFilter = "fancy";
               return true;
            } else if (this.inside(mouseX, mouseY, 430, topY, 60, 20)) {
               this.categoryFilter = "utility";
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

               for(ModrinthAPI.Project p : this.filterProjects(this.projects)) {
                  int btnW = 76;
                  int btnH = 22;
                  int btnX = cardX + cardW - btnW - 10;
                  int btnY = cardY + 25;
                  if (this.inside(mouseX, mouseY, btnX, btnY, btnW, btnH)) {
                     this.selectedProject = p;
                     this.installProject(p);
                     return true;
                  }

                  if (this.inside(mouseX, mouseY, cardX, cardY, cardW, cardH)) {
                     this.selectedProject = p;
                     return true;
                  }

                  cardY += cardH + gap;
               }

               return super.method_25402(click, doubled);
            }
         }
      }
   }

   public boolean method_25401(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
      int listY = 102;
      int listH = this.field_22790 - listY - 18;
      List<ModrinthAPI.Project> visible = this.filterProjects(this.projects);
      float contentHeight = (float)visible.size() * 82.0F;
      float maxScroll = Math.max(0.0F, contentHeight - (float)(listH - 20));
      this.scroll = clamp(this.scroll - (float)verticalAmount * 20.0F, 0.0F, maxScroll);
      return true;
   }

   public boolean method_25404(class_11908 input) {
      if (this.searchFocused) {
         if (input.method_74231()) {
            this.searchFocused = false;
            this.searchBar.method_25365(false);
            return true;
         }

         if (this.searchBar.method_25404(input)) {
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
         this.installProgressMap.put(p.id, 0.0F);
         this.statusText = "Installing " + p.title + "...";
         (new Thread(() -> {
            try {
               String mcVersion = class_310.method_1551().method_1515();
               String url = ModrinthAPI.getDownloadUrl(p.id, mcVersion, this.loaderFilter);
               if (url != null && !url.isBlank()) {
                  Path folder = this.mc.field_1697.toPath().resolve(this.installFolder);
                  Files.createDirectories(folder);
                  String fileName = p.title.replaceAll("[^a-zA-Z0-9._-]", "_");
                  String ext = ".zip";
                  int dot = url.lastIndexOf(46);
                  if (dot >= 0) {
                     String raw = url.substring(dot);
                     int q = raw.indexOf(63);
                     if (q >= 0) {
                        raw = raw.substring(0, q);
                     }

                     if (raw.length() <= 5) {
                        ext = raw;
                     }
                  }

                  Path target = folder.resolve(fileName + ext);
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
                              float progress = Math.min(1.0F, (float)readTotal / (float)size);
                              this.installProgress = progress;
                              this.installProgressMap.put(p.id, progress);
                           }
                        }
                     } catch (Throwable var28) {
                        if (out != null) {
                           try {
                              out.close();
                           } catch (Throwable var27) {
                              var28.addSuppressed(var27);
                           }
                        }

                        throw var28;
                     }

                     if (out != null) {
                        out.close();
                     }
                  } catch (Throwable var29) {
                     if (in != null) {
                        try {
                           in.close();
                        } catch (Throwable var26) {
                           var29.addSuppressed(var26);
                        }
                     }

                     throw var29;
                  }

                  if (in != null) {
                     in.close();
                  }

                  this.installProgress = 1.0F;
                  this.installProgressMap.put(p.id, 1.0F);
                  this.statusText = "Installed: " + p.title + " (restart may be required)";
                  return;
               }

               this.statusText = "No compatible file found";
               this.installProgressMap.remove(p.id);
               this.installingId = null;
            } catch (Exception e) {
               e.printStackTrace();
               this.statusText = "Install failed";
               this.installProgressMap.remove(p.id);
               return;
            } finally {
               this.installingId = null;
            }

         }, "optix-modrinth-install")).start();
      }
   }

   private List<ModrinthAPI.Project> filterProjects(List<ModrinthAPI.Project> src) {
      List<ModrinthAPI.Project> out = new ArrayList();
      if (src == null) {
         return out;
      } else {
         String q = this.searchBar != null ? this.searchBar.method_1882().trim().toLowerCase(Locale.ROOT) : "";
         String cat = this.categoryFilter == null ? "all" : this.categoryFilter.toLowerCase(Locale.ROOT);

         for(ModrinthAPI.Project p : src) {
            if (p != null) {
               boolean queryOk = q.isEmpty() || safe(p.title).toLowerCase(Locale.ROOT).contains(q) || safe(p.description).toLowerCase(Locale.ROOT).contains(q) || safe(p.author).toLowerCase(Locale.ROOT).contains(q);
               boolean categoryOk = "all".equals(cat) || this.matchesCategory(p, cat);
               if (queryOk && categoryOk) {
                  out.add(p);
               }
            }
         }

         return out;
      }
   }

   private boolean matchesCategory(ModrinthAPI.Project p, String cat) {
      if (p.categories != null) {
         for(String c : p.categories) {
            if (c != null && c.toLowerCase(Locale.ROOT).contains(cat)) {
               return true;
            }
         }
      }

      String t = safe(p.title).toLowerCase(Locale.ROOT);
      String d = safe(p.description).toLowerCase(Locale.ROOT);
      return t.contains(cat) || d.contains(cat);
   }

   private void drawButton(class_332 ctx, int x, int y, int w, int h, String label, int fill) {
      ctx.method_25294(x, y, x + w, y + h, fill);
      this.drawBorder(ctx, x, y, w, h, -14472645);
      ctx.method_27535(this.field_22793, class_2561.method_43470(label), x + w / 2 - this.field_22793.method_1727(label) / 2, y + 7, -1);
   }

   private void drawChip(class_332 ctx, int x, int y, String label, boolean selected) {
      int w = Math.max(36, this.field_22793.method_1727(label) + 16);
      int fill = selected ? -7638017 : -14472645;
      ctx.method_25294(x, y, x + w, y + 20, fill);
      ctx.method_27535(this.field_22793, class_2561.method_43470(label), x + 8, y + 6, -1);
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

   private void loadIcon(ModrinthAPI.Project p) {
      if (p != null) {
         if (p.id != null) {
            if (p.iconUrl != null) {
               if (!p.iconUrl.isBlank()) {
                  if (!this.iconTextures.containsKey(p.id)) {
                     this.iconTextures.put(p.id, class_2960.method_60655("minecraft", "textures/item/barrier.png"));
                     (new Thread(() -> {
                        try {
                           BufferedImage image = ImageIO.read(new URL(p.iconUrl));
                           if (image == null) {
                              return;
                           }

                           class_1011 nativeImage = new class_1011(image.getWidth(), image.getHeight(), true);

                           for(int x = 0; x < image.getWidth(); ++x) {
                              for(int y = 0; y < image.getHeight(); ++y) {
                                 nativeImage.method_4305(x, y, image.getRGB(x, y));
                              }
                           }

                           class_2960 textureId = class_2960.method_60655("optix", "modrinth_" + p.id.toLowerCase(Locale.ROOT));
                           this.mc.execute(() -> {
                              this.mc.method_1531().method_4616(textureId, new class_1043(() -> p.title, nativeImage));
                              this.iconTextures.put(p.id, textureId);
                           });
                        } catch (Exception e) {
                           e.printStackTrace();
                        }

                     }, "optix-icon-loader")).start();
                  }
               }
            }
         }
      }
   }
}
