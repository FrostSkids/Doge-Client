// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.entity;

import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.init.Items;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.IImageBuffer;
import java.io.File;
import net.minecraft.client.renderer.ImageBufferDownload;
import net.minecraft.util.StringUtils;
import net.minecraft.client.renderer.ThreadDownloadImageData;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.WorldSettings;
import net.minecraft.client.Minecraft;
import com.mojang.authlib.GameProfile;
import net.minecraft.world.World;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.entity.player.EntityPlayer;

public abstract class AbstractClientPlayer extends EntityPlayer
{
    private NetworkPlayerInfo playerInfo;
    
    public AbstractClientPlayer(final World worldIn, final GameProfile playerProfile) {
        super(worldIn, playerProfile);
    }
    
    @Override
    public boolean isSpectator() {
        final NetworkPlayerInfo networkplayerinfo = Minecraft.getMinecraft().getNetHandler().getPlayerInfo(this.getGameProfile().getId());
        return networkplayerinfo != null && networkplayerinfo.getGameType() == WorldSettings.GameType.SPECTATOR;
    }
    
    public boolean hasPlayerInfo() {
        return this.getPlayerInfo() != null;
    }
    
    protected NetworkPlayerInfo getPlayerInfo() {
        if (this.playerInfo == null) {
            this.playerInfo = Minecraft.getMinecraft().getNetHandler().getPlayerInfo(this.getUniqueID());
        }
        return this.playerInfo;
    }
    
    public boolean hasSkin() {
        final NetworkPlayerInfo networkplayerinfo = this.getPlayerInfo();
        return networkplayerinfo != null && networkplayerinfo.hasLocationSkin();
    }
    
    public ResourceLocation getLocationSkin() {
        final NetworkPlayerInfo networkplayerinfo = this.getPlayerInfo();
        return (networkplayerinfo == null) ? DefaultPlayerSkin.getDefaultSkin(this.getUniqueID()) : networkplayerinfo.getLocationSkin();
    }
    
    public ResourceLocation getLocationCape() {
        final NetworkPlayerInfo networkplayerinfo = this.getPlayerInfo();
        return (networkplayerinfo == null) ? null : networkplayerinfo.getLocationCape();
    }
    
    public static ThreadDownloadImageData getDownloadImageSkin(final ResourceLocation resourceLocationIn, final String username) {
        final TextureManager texturemanager = Minecraft.getMinecraft().getTextureManager();
        ITextureObject itextureobject = texturemanager.getTexture(resourceLocationIn);
        if (itextureobject == null) {
            itextureobject = new ThreadDownloadImageData(null, String.format("http://skins.minecraft.net/MinecraftSkins/%s.png", StringUtils.stripControlCodes(username)), DefaultPlayerSkin.getDefaultSkin(EntityPlayer.getOfflineUUID(username)), new ImageBufferDownload());
            texturemanager.loadTexture(resourceLocationIn, itextureobject);
        }
        return (ThreadDownloadImageData)itextureobject;
    }
    
    public static ResourceLocation getLocationSkin(final String username) {
        return new ResourceLocation("skins/" + StringUtils.stripControlCodes(username));
    }
    
    public String getSkinType() {
        final NetworkPlayerInfo networkplayerinfo = this.getPlayerInfo();
        return (networkplayerinfo == null) ? DefaultPlayerSkin.getSkinType(this.getUniqueID()) : networkplayerinfo.getSkinType();
    }
    
    public float getFovModifier() {
        float f = 1.0f;
        if (this.capabilities.isFlying) {
            f *= 1.1f;
        }
        final IAttributeInstance iattributeinstance = this.getEntityAttribute(SharedMonsterAttributes.movementSpeed);
        f *= (float)((iattributeinstance.getAttributeValue() / this.capabilities.getWalkSpeed() + 1.0) / 2.0);
        if (this.capabilities.getWalkSpeed() == 0.0f || Float.isNaN(f) || Float.isInfinite(f)) {
            f = 1.0f;
        }
        if (this.isUsingItem() && this.getItemInUse().getItem() == Items.bow) {
            final int i = this.getItemInUseDuration();
            float f2 = i / 20.0f;
            if (f2 > 1.0f) {
                f2 = 1.0f;
            }
            else {
                f2 *= f2;
            }
            f *= 1.0f - f2 * 0.15f;
        }
        return f;
    }
}
