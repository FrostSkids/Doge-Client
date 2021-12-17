// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer;

import org.apache.commons.io.FileUtils;
import net.minecraft.client.Minecraft;
import java.net.URL;
import java.net.HttpURLConnection;
import java.io.IOException;
import javax.imageio.ImageIO;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.concurrent.atomic.AtomicInteger;
import org.apache.logging.log4j.Logger;
import net.minecraft.client.renderer.texture.SimpleTexture;

public class ThreadDownloadImageData extends SimpleTexture
{
    private static final Logger logger;
    private static final AtomicInteger threadDownloadCounter;
    private final File cacheFile;
    private final String imageUrl;
    private final IImageBuffer imageBuffer;
    private BufferedImage bufferedImage;
    private Thread imageThread;
    private boolean textureUploaded;
    
    static {
        logger = LogManager.getLogger();
        threadDownloadCounter = new AtomicInteger(0);
    }
    
    public ThreadDownloadImageData(final File cacheFileIn, final String imageUrlIn, final ResourceLocation textureResourceLocation, final IImageBuffer imageBufferIn) {
        super(textureResourceLocation);
        this.cacheFile = cacheFileIn;
        this.imageUrl = imageUrlIn;
        this.imageBuffer = imageBufferIn;
    }
    
    private void checkTextureUploaded() {
        if (!this.textureUploaded && this.bufferedImage != null) {
            if (this.textureLocation != null) {
                this.deleteGlTexture();
            }
            TextureUtil.uploadTextureImage(super.getGlTextureId(), this.bufferedImage);
            this.textureUploaded = true;
        }
    }
    
    @Override
    public int getGlTextureId() {
        this.checkTextureUploaded();
        return super.getGlTextureId();
    }
    
    public void setBufferedImage(final BufferedImage bufferedImageIn) {
        this.bufferedImage = bufferedImageIn;
        if (this.imageBuffer != null) {
            this.imageBuffer.skinAvailable();
        }
    }
    
    @Override
    public void loadTexture(final IResourceManager resourceManager) throws IOException {
        if (this.bufferedImage == null && this.textureLocation != null) {
            super.loadTexture(resourceManager);
        }
        if (this.imageThread == null) {
            if (this.cacheFile != null && this.cacheFile.isFile()) {
                ThreadDownloadImageData.logger.debug("Loading http texture from local cache ({})", new Object[] { this.cacheFile });
                try {
                    this.bufferedImage = ImageIO.read(this.cacheFile);
                    if (this.imageBuffer != null) {
                        this.setBufferedImage(this.imageBuffer.parseUserSkin(this.bufferedImage));
                    }
                }
                catch (IOException ioexception) {
                    ThreadDownloadImageData.logger.error("Couldn't load skin " + this.cacheFile, (Throwable)ioexception);
                    this.loadTextureFromServer();
                }
            }
            else {
                this.loadTextureFromServer();
            }
        }
    }
    
    protected void loadTextureFromServer() {
        (this.imageThread = new Thread("Texture Downloader #" + ThreadDownloadImageData.threadDownloadCounter.incrementAndGet()) {
            @Override
            public void run() {
                HttpURLConnection httpurlconnection = null;
                ThreadDownloadImageData.logger.debug("Downloading http texture from {} to {}", new Object[] { ThreadDownloadImageData.this.imageUrl, ThreadDownloadImageData.this.cacheFile });
                try {
                    httpurlconnection = (HttpURLConnection)new URL(ThreadDownloadImageData.this.imageUrl).openConnection(Minecraft.getMinecraft().getProxy());
                    httpurlconnection.setDoInput(true);
                    httpurlconnection.setDoOutput(false);
                    httpurlconnection.connect();
                    if (httpurlconnection.getResponseCode() / 100 == 2) {
                        BufferedImage bufferedimage;
                        if (ThreadDownloadImageData.this.cacheFile != null) {
                            FileUtils.copyInputStreamToFile(httpurlconnection.getInputStream(), ThreadDownloadImageData.this.cacheFile);
                            bufferedimage = ImageIO.read(ThreadDownloadImageData.this.cacheFile);
                        }
                        else {
                            bufferedimage = TextureUtil.readBufferedImage(httpurlconnection.getInputStream());
                        }
                        if (ThreadDownloadImageData.this.imageBuffer != null) {
                            bufferedimage = ThreadDownloadImageData.this.imageBuffer.parseUserSkin(bufferedimage);
                        }
                        ThreadDownloadImageData.this.setBufferedImage(bufferedimage);
                        return;
                    }
                }
                catch (Exception exception) {
                    ThreadDownloadImageData.logger.error("Couldn't download http texture", (Throwable)exception);
                    return;
                }
                finally {
                    if (httpurlconnection != null) {
                        httpurlconnection.disconnect();
                    }
                }
                if (httpurlconnection != null) {
                    httpurlconnection.disconnect();
                }
            }
        }).setDaemon(true);
        this.imageThread.start();
    }
}
