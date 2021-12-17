// 
// Decompiled by Procyon v0.5.36
// 

package Main;

import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.ResourceLocation;
import net.minecraft.client.Minecraft;

public class GuiHelper
{
    private static Minecraft mc;
    
    static {
        GuiHelper.mc = Minecraft.getMinecraft();
    }
    
    public static void drawPicture(final int x, final int y, final int width, final int height, final String location) {
        final ResourceLocation loc = new ResourceLocation(location);
        GuiHelper.mc.getTextureManager().bindTexture(loc);
        Gui.drawModalRectWithCustomSizedTexture(x, y, 0.0f, 0.0f, width, height, (float)width, (float)height);
    }
    
    public static void drawBackgroundPicture(final int width, final int height, final String location) {
        final ScaledResolution scaledResolution = new ScaledResolution(GuiHelper.mc);
        final ResourceLocation loc = new ResourceLocation(location);
        GuiHelper.mc.getTextureManager().bindTexture(loc);
        Gui.drawModalRectWithCustomSizedTexture(0, 0, 0.0f, 0.0f, scaledResolution.getScaledWidth(), scaledResolution.getScaledHeight(), (float)scaledResolution.getScaledWidth(), (float)scaledResolution.getScaledHeight());
        Gui.drawRect(0, 0, width, height, 1073741824);
    }
}
