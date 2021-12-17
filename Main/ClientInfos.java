// 
// Decompiled by Procyon v0.5.36
// 

package Main;

import net.minecraft.util.ResourceLocation;
import java.awt.Color;

public class ClientInfos
{
    public static String ClientName;
    public static String ClientDev;
    public static String ClientColorMods;
    public static Color ClientColor;
    public static int ClientColorInt;
    public static ResourceLocation CapeLocation;
    public static ResourceLocation Wallpaper;
    public static int ModType;
    public static boolean Coords;
    public static boolean FPS;
    public static boolean CPS;
    public static boolean Biom;
    public static boolean Time;
    public static boolean ToggleSprint;
    public static boolean Featured;
    public static String ServerName;
    public static String ServerIp;
    public static int frames;
    static int k;
    
    static {
        ClientInfos.ClientName = "Doge Client";
        ClientInfos.ClientDev = "§f";
        ClientInfos.ClientColorMods = "§e";
        ClientInfos.ClientColor = new Color(252, 186, 3);
        ClientInfos.ClientColorInt = ClientInfos.ClientColor.getRGB();
        ClientInfos.CapeLocation = new ResourceLocation("Client/cape.png");
        ClientInfos.Wallpaper = new ResourceLocation("Client/wallpaper.png");
        ClientInfos.ModType = 1;
        ClientInfos.Coords = true;
        ClientInfos.FPS = true;
        ClientInfos.CPS = true;
        ClientInfos.Biom = false;
        ClientInfos.Time = false;
        ClientInfos.ToggleSprint = true;
        ClientInfos.Featured = true;
        ClientInfos.ServerName = String.valueOf(ClientInfos.ClientColorMods) + ClientInfos.ClientName;
        ClientInfos.ServerIp = "eu.hivemc.net";
        ClientInfos.frames = 1;
        ClientInfos.k = 1;
    }
    
    public static void onTick() {
        if (ClientInfos.k <= 2) {
            ++ClientInfos.k;
        }
        if (ClientInfos.k == 2) {
            ++ClientInfos.frames;
            if (ClientInfos.frames == 48) {
                ClientInfos.frames = 1;
            }
            ClientInfos.k = 1;
        }
    }
}
