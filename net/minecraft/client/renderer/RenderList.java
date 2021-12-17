// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer;

import java.util.Iterator;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.renderer.chunk.ListedRenderChunk;
import net.minecraft.client.renderer.chunk.RenderChunk;
import net.minecraft.util.EnumWorldBlockLayer;

public class RenderList extends ChunkRenderContainer
{
    @Override
    public void renderChunkLayer(final EnumWorldBlockLayer layer) {
        if (this.initialized) {
            for (final RenderChunk renderchunk : this.renderChunks) {
                final ListedRenderChunk listedrenderchunk = (ListedRenderChunk)renderchunk;
                GlStateManager.pushMatrix();
                this.preRenderChunk(renderchunk);
                GL11.glCallList(listedrenderchunk.getDisplayList(layer, listedrenderchunk.getCompiledChunk()));
                GlStateManager.popMatrix();
            }
            GlStateManager.resetColor();
            this.renderChunks.clear();
        }
    }
}
