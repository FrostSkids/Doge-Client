// 
// Decompiled by Procyon v0.5.36
// 

package Main;

import net.minecraft.util.ResourceLocation;
import net.minecraft.client.multiplayer.ServerData;

public class ServerDataFeatured extends ServerData
{
    public static final ResourceLocation ICON;
    
    static {
        ICON = new ResourceLocation("Client/logo1.png");
    }
    
    public ServerDataFeatured(final String serverName, final String serverIp) {
        super(serverName, serverIp, false);
    }
}
