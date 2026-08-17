package com.example.client.ui;

import com.google.gson.Gson;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import net.minecraft.class_11909;
import net.minecraft.class_156;
import net.minecraft.class_2561;
import net.minecraft.class_310;
import net.minecraft.class_327;
import net.minecraft.class_332;
import net.minecraft.class_342;
import net.minecraft.class_3532;
import net.minecraft.class_437;
import org.jetbrains.annotations.Nullable;

public class PartnershipScreen extends class_437 {
   private static final String DISCORD_INVITE = "https://discord.gg/vexorianetwork";
   private static final String API_BASE = "https://YOUR_DOMAIN_HERE/api";
   private final @Nullable class_437 parent;
   private Tab selectedTab;
   private float fade;
   private boolean loading;
   private String status;
   private PartnershipState state;
   private class_342 searchField;
   private final PartnershipApi api;

   public PartnershipScreen(@Nullable class_437 parent) {
      super(class_2561.method_43470("Ranked Bedwars Register"));
      this.selectedTab = PartnershipScreen.Tab.REGISTER;
      this.fade = 0.0F;
      this.loading = true;
      this.status = "Loading...";
      this.state = null;
      this.api = new HttpPartnershipApi("https://YOUR_DOMAIN_HERE/api");
      this.parent = parent;
   }

   protected void method_25426() {
      int fieldW = Math.min(180, this.field_22789 / 3);
      this.searchField = new class_342(this.field_22793, this.field_22789 - fieldW - 18, 14, fieldW, 20, class_2561.method_43470("Search player"));
      this.searchField.method_47404(class_2561.method_43470("Search player"));
      this.searchField.method_1880(32);
      this.searchField.method_1863(this::refreshLeaderboard);
      this.method_37063(this.searchField);
      String mcName = class_310.method_1551().method_1548() != null ? class_310.method_1551().method_1548().method_1676() : "Player";
      this.api.fetchState(mcName).whenComplete((s, t) -> class_310.method_1551().execute(() -> {
            this.loading = false;
            if (t == null && s != null) {
               this.state = s;
               this.status = s.registered() ? "Registered" : "Not registered";
            } else {
               this.status = "Failed to load register data";
            }
         }));
   }

   public void method_25419() {
      if (this.field_22787 != null) {
         this.field_22787.method_1507(this.parent);
      }

   }

   public void method_25394(class_332 ctx, int mouseX, int mouseY, float delta) {
      this.fade = class_3532.method_15363(this.fade + 0.05F, 0.0F, 1.0F);
      int sw = this.field_22789;
      int sh = this.field_22790;
      ctx.method_25296(0, 0, sw, sh, -16185079, -15658214);
      ctx.method_25294(0, 0, sw, sh, (int)((1.0F - this.fade) * 95.0F) << 24);
      int leftX = 18;
      int leftY = 34;
      int leftW = Math.min(250, Math.max(190, sw / 4));
      int leftH = sh - 52;
      int rightX = leftX + leftW + 12;
      int rightW = sw - rightX - 18;
      int rightH = sh - 52;
      this.drawCard(ctx, leftX, leftY, leftW, leftH);
      this.drawCard(ctx, rightX, leftY, rightW, rightH);
      ctx.method_27535(this.field_22793, class_2561.method_43470("Ranked Bedwars"), leftX + 16, 12, -1);
      ctx.method_27535(this.field_22793, class_2561.method_43470(this.status), leftX + 16, 24, -6310698);
      this.drawTab(ctx, leftX + 14, leftY + 14, leftW - 28, 42, "Register", "Join Discord + get code", this.selectedTab == PartnershipScreen.Tab.REGISTER);
      this.drawTab(ctx, leftX + 14, leftY + 66, leftW - 28, 42, "Leaderboard", "Top players", this.selectedTab == PartnershipScreen.Tab.LEADERBOARD);
      int panelX = rightX + 16;
      int panelY = leftY + 16;
      int panelW = rightW - 32;
      this.drawInsetCard(ctx, panelX, panelY, panelW, 170);
      if (this.selectedTab == PartnershipScreen.Tab.REGISTER) {
         this.drawRegisterPanel(ctx, mouseX, mouseY, panelX, panelY, panelW);
      } else {
         this.drawLeaderboardPanel(ctx, panelX, panelY, panelW, rightH);
      }

      super.method_25394(ctx, mouseX, mouseY, delta);
   }

   private void drawRegisterPanel(class_332 ctx, int mouseX, int mouseY, int panelX, int panelY, int panelW) {
      ctx.method_27535(this.field_22793, class_2561.method_43470("Vexoria Network Register"), panelX + 16, panelY + 14, -1);
      ctx.method_27535(this.field_22793, class_2561.method_43470("Join Discord, then register your Minecraft name."), panelX + 16, panelY + 32, -6310698);
      int emblem = panelX + panelW - 74;
      ctx.method_25294(emblem, panelY + 16, emblem + 48, panelY + 64, -15394268);
      ctx.method_25294(emblem, panelY + 16, emblem + 48, panelY + 17, -11688961);
      ctx.method_27534(this.field_22793, class_2561.method_43470("RB"), emblem + 24, panelY + 34, -1);
      int btnY = panelY + 78;
      int btnH = 34;
      int btnW = panelW - 32;
      boolean hasRegistered = this.state != null && this.state.registered();
      boolean joinHover = this.isInside((double)mouseX, (double)mouseY, panelX + 16, btnY, btnW, btnH);
      this.drawButton(ctx, panelX + 16, btnY, btnW, btnH, "Join Discord", joinHover, true);
      if (!hasRegistered) {
         boolean regHover = this.isInside((double)mouseX, (double)mouseY, panelX + 16, btnY + 42, btnW, btnH);
         this.drawButton(ctx, panelX + 16, btnY + 42, btnW, btnH, "Register", regHover, true);
      } else {
         this.drawButton(ctx, panelX + 16, btnY + 42, btnW, btnH, "Registered", false, false);
      }

      if (this.state != null) {
         int infoY = btnY + 92;
         class_327 var10001 = this.field_22793;
         String var10002 = this.safe(this.state.minecraftName());
         ctx.method_27535(var10001, class_2561.method_43470("Minecraft: " + var10002), panelX + 16, infoY, -1643790);
         var10001 = this.field_22793;
         var10002 = this.safe(this.state.discordName());
         ctx.method_27535(var10001, class_2561.method_43470("Discord: " + var10002), panelX + 16, infoY + 14, -1643790);
         String code = this.safe(this.state.linkCode());
         ctx.method_27535(this.field_22793, class_2561.method_43470("Code: " + (code.isEmpty() ? "Not generated yet" : code)), panelX + 16, infoY + 28, -6310698);
         if (!code.isEmpty()) {
            boolean copyHover = this.isInside((double)mouseX, (double)mouseY, panelX + 16, infoY + 46, 120, 28);
            this.drawButton(ctx, panelX + 16, infoY + 46, 120, 28, "Copy Code", copyHover, true);
         }
      } else {
         ctx.method_27535(this.field_22793, class_2561.method_43470(this.loading ? "Loading..." : "Press Register after joining Discord."), panelX + 16, btnY + 94, -6310698);
      }

   }

   private void drawLeaderboardPanel(class_332 ctx, int panelX, int panelY, int panelW, int rightH) {
      if (this.state != null && this.state.leaderboard() != null) {
         this.drawLeaderboard(ctx, panelX, panelY + 120, panelW, rightH - 136, this.state.leaderboard(), this.searchField == null ? "" : this.searchField.method_1882());
      } else {
         ctx.method_27535(this.field_22793, class_2561.method_43470(this.loading ? "Loading leaderboard..." : "No leaderboard data"), panelX + 16, panelY + 128, -6310698);
      }

   }

   public boolean method_25402(class_11909 click, boolean doubled) {
      double mouseX = click.comp_4798();
      double mouseY = click.comp_4799();
      int sw = this.field_22789;
      int sh = this.field_22790;
      int leftX = 18;
      int leftY = 34;
      int leftW = Math.min(250, Math.max(190, sw / 4));
      int rightX = leftX + leftW + 12;
      int rightW = sw - rightX - 18;
      if (this.isInside(mouseX, mouseY, leftX + 14, leftY + 14, leftW - 28, 42)) {
         this.selectedTab = PartnershipScreen.Tab.REGISTER;
         return true;
      } else if (this.isInside(mouseX, mouseY, leftX + 14, leftY + 66, leftW - 28, 42)) {
         this.selectedTab = PartnershipScreen.Tab.LEADERBOARD;
         return true;
      } else {
         int panelX = rightX + 16;
         int panelY = leftY + 16;
         int panelW = rightW - 32;
         int btnY = panelY + 78;
         int btnH = 34;
         int btnW = panelW - 32;
         if (this.selectedTab == PartnershipScreen.Tab.REGISTER) {
            if (this.isInside(mouseX, mouseY, panelX + 16, btnY, btnW, btnH)) {
               class_156.method_668().method_673(URI.create("https://discord.gg/vexorianetwork"));
               return true;
            }

            if ((this.state == null || !this.state.registered()) && this.isInside(mouseX, mouseY, panelX + 16, btnY + 42, btnW, btnH)) {
               String mcName = class_310.method_1551().method_1548() != null ? class_310.method_1551().method_1548().method_1676() : "Player";
               this.api.register(mcName).whenComplete((s, t) -> class_310.method_1551().execute(() -> {
                     if (t == null && s != null) {
                        this.state = s;
                        this.status = s.registered() ? "Registered" : "Not registered";
                     } else {
                        this.status = "Register failed";
                     }
                  }));
               return true;
            }

            if (this.state != null && !this.safe(this.state.linkCode()).isEmpty()) {
               int infoY = btnY + 92;
               if (this.isInside(mouseX, mouseY, panelX + 16, infoY + 46, 120, 28)) {
                  class_310.method_1551().field_1774.method_1455(this.state.linkCode());
                  this.status = "Code copied";
                  return true;
               }
            }
         }

         return super.method_25402(click, doubled);
      }
   }

   private void refreshLeaderboard(String q) {
      if (this.selectedTab == PartnershipScreen.Tab.LEADERBOARD) {
         this.api.fetchLeaderboard(q == null ? "" : q).whenComplete((s, t) -> class_310.method_1551().execute(() -> {
               if (t == null && s != null) {
                  this.state = s;
               }
            }));
      }
   }

   private void drawLeaderboard(class_332 ctx, int x, int y, int w, int h, List<LeaderboardEntry> entries, String query) {
      this.drawInsetCard(ctx, x, y, w, h);
      ctx.method_27535(this.field_22793, class_2561.method_43470("Leaderboard"), x + 16, y + 12, -1);
      int rowY = y + 32;
      int shown = 0;
      String q = query == null ? "" : query.trim().toLowerCase();

      for(LeaderboardEntry e : entries) {
         if (shown >= 6) {
            break;
         }

         if (q.isEmpty() || e.playerName().toLowerCase().contains(q)) {
            String var10000 = e.playerName();
            String line = var10000 + "  •  " + e.rank() + "  •  " + e.points() + " pts";
            ctx.method_27535(this.field_22793, class_2561.method_43470(line), x + 16, rowY, -1643790);
            rowY += 14;
            ++shown;
         }
      }

      if (shown == 0) {
         ctx.method_27535(this.field_22793, class_2561.method_43470("No matching players"), x + 16, rowY, -6310698);
      }

   }

   private void drawTab(class_332 ctx, int x, int y, int w, int h, String title, String subtitle, boolean active) {
      int bg = active ? -15195853 : -15592165;
      int border = active ? -11688961 : -14012096;
      this.drawCard(ctx, x, y, w, h, bg, border);
      ctx.method_27535(this.field_22793, class_2561.method_43470(title), x + 12, y + 8, -1);
      ctx.method_27535(this.field_22793, class_2561.method_43470(subtitle), x + 12, y + 22, active ? -2694145 : -6642251);
   }

   private void drawButton(class_332 ctx, int x, int y, int w, int h, String label, boolean hover, boolean primary) {
      int bg = primary ? (hover ? -14471094 : -15261645) : -14670802;
      int border = primary ? -11688961 : -13090478;
      this.drawCard(ctx, x, y, w, h, bg, border);
      ctx.method_27534(this.field_22793, class_2561.method_43470(label), x + w / 2, y + 11, -1);
   }

   private void drawCard(class_332 ctx, int x, int y, int w, int h) {
      this.drawCard(ctx, x, y, w, h, -15724268, -14407885);
   }

   private void drawCard(class_332 ctx, int x, int y, int w, int h, int fill, int border) {
      ctx.method_25294(x, y, x + w, y + h, fill);
      ctx.method_25294(x, y, x + w, y + 1, border);
      ctx.method_25294(x, y + h - 1, x + w, y + h, -1442840576);
      ctx.method_25294(x, y, x + 1, y + h, 1426063360);
      ctx.method_25294(x + w - 1, y, x + w, y + h, 1426063360);
   }

   private void drawInsetCard(class_332 ctx, int x, int y, int w, int h) {
      this.drawCard(ctx, x, y, w, h);
      ctx.method_25294(x + 1, y + 1, x + w - 1, y + 2, 573786726);
   }

   private boolean isInside(double mx, double my, int x, int y, int w, int h) {
      return mx >= (double)x && mx <= (double)(x + w) && my >= (double)y && my <= (double)(y + h);
   }

   private String safe(String s) {
      return s == null ? "" : s;
   }

   private static enum Tab {
      REGISTER,
      LEADERBOARD;

      // $FF: synthetic method
      private static Tab[] $values() {
         return new Tab[]{REGISTER, LEADERBOARD};
      }
   }

   public static record PartnershipState(boolean registered, String discordName, String minecraftName, String linkCode, List<LeaderboardEntry> leaderboard) {
   }

   public static record LeaderboardEntry(String playerName, int points, int wins, int losses, String rank) {
   }

   public static final class HttpPartnershipApi implements PartnershipApi {
      private static final Gson GSON = new Gson();
      private final String baseUrl;

      public HttpPartnershipApi(String baseUrl) {
         this.baseUrl = baseUrl.endsWith("/") ? baseUrl.substring(0, baseUrl.length() - 1) : baseUrl;
      }

      public CompletableFuture<PartnershipState> fetchState(String minecraftName) {
         return CompletableFuture.supplyAsync(() -> (PartnershipState)this.getJson("/partnership/state?mc=" + url(minecraftName), PartnershipState.class));
      }

      public CompletableFuture<PartnershipState> register(String minecraftName) {
         return CompletableFuture.supplyAsync(() -> (PartnershipState)this.postJson("/partnership/register", "{\"mcName\":\"" + escape(minecraftName) + "\"}", PartnershipState.class));
      }

      public CompletableFuture<PartnershipState> fetchLeaderboard(String query) {
         return CompletableFuture.supplyAsync(() -> (PartnershipState)this.getJson("/partnership/leaderboard?q=" + url(query), PartnershipState.class));
      }

      private <T> T getJson(String path, Class<T> type) {
         HttpURLConnection con = null;

         Object var7;
         try {
            URL url = URI.create(this.baseUrl + path).toURL();
            con = (HttpURLConnection)url.openConnection();
            con.setRequestMethod("GET");
            con.setConnectTimeout(6000);
            con.setReadTimeout(6000);
            con.setRequestProperty("Accept", "application/json");
            int code = con.getResponseCode();
            String body = readBody(code >= 200 && code < 300 ? con.getInputStream() : con.getErrorStream());
            if (code < 200 || code >= 300) {
               throw new IOException("HTTP " + code + ": " + body);
            }

            var7 = GSON.fromJson(body, type);
         } catch (Exception e) {
            throw new RuntimeException("Failed request: " + path, e);
         } finally {
            if (con != null) {
               con.disconnect();
            }

         }

         return (T)var7;
      }

      private <T> T postJson(String path, String json, Class<T> type) {
         HttpURLConnection con = null;

         Object var8;
         try {
            URL url = URI.create(this.baseUrl + path).toURL();
            con = (HttpURLConnection)url.openConnection();
            con.setRequestMethod("POST");
            con.setDoOutput(true);
            con.setConnectTimeout(6000);
            con.setReadTimeout(6000);
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("Accept", "application/json");
            OutputStream os = con.getOutputStream();

            try {
               os.write(json.getBytes(StandardCharsets.UTF_8));
            } catch (Throwable var15) {
               if (os != null) {
                  try {
                     os.close();
                  } catch (Throwable var14) {
                     var15.addSuppressed(var14);
                  }
               }

               throw var15;
            }

            if (os != null) {
               os.close();
            }

            int code = con.getResponseCode();
            String body = readBody(code >= 200 && code < 300 ? con.getInputStream() : con.getErrorStream());
            if (code < 200 || code >= 300) {
               throw new IOException("HTTP " + code + ": " + body);
            }

            var8 = GSON.fromJson(body, type);
         } catch (Exception e) {
            throw new RuntimeException("Failed request: " + path, e);
         } finally {
            if (con != null) {
               con.disconnect();
            }

         }

         return (T)var8;
      }

      private static String readBody(InputStream in) throws IOException {
         if (in == null) {
            return "";
         } else {
            byte[] bytes = in.readAllBytes();
            return new String(bytes, StandardCharsets.UTF_8);
         }
      }

      private static String url(String s) {
         try {
            return URLEncoder.encode(s == null ? "" : s, StandardCharsets.UTF_8);
         } catch (Exception var2) {
            return "";
         }
      }

      private static String escape(String s) {
         return s == null ? "" : s.replace("\\", "\\\\").replace("\"", "\\\"");
      }
   }

   public interface PartnershipApi {
      CompletableFuture<PartnershipState> fetchState(String var1);

      CompletableFuture<PartnershipState> register(String var1);

      CompletableFuture<PartnershipState> fetchLeaderboard(String var1);
   }
}
