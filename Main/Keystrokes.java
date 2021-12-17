// 
// Decompiled by Procyon v0.5.36
// 

package Main;

import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.gui.Gui;
import java.awt.Color;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;

public class Keystrokes
{
    private KeystrokesMode mode;
    
    public Keystrokes() {
        this.mode = KeystrokesMode.WASD_SNEAK_MOUSE;
    }
    
    public FontRenderer getFontRenderer() {
        return Minecraft.getMinecraft().fontRendererObj;
    }
    
    public void setMode(final KeystrokesMode mode) {
        this.mode = mode;
    }
    
    public void draw() {
        this.mode = KeystrokesMode.WASD_MOUSE;
        GL11.glPushMatrix();
        Key[] keys;
        for (int length = (keys = this.mode.getKeys()).length, i = 0; i < length; ++i) {
            final Key key = keys[i];
            final int textWidth = this.getFontRenderer().getStringWidth(key.getName());
            Gui.drawRect(pos.getAbsoluteX() + key.getX(), pos.getAbsoluteY() + key.getY(), pos.getAbsoluteX() + key.getX() + key.getWidth(), pos.getAbsoluteY() + key.getY() + key.getHeight(), key.isDown() ? new Color(255, 255, 255, 102).getRGB() : new Color(0, 0, 0, 150).getRGB());
            this.getFontRenderer().drawString(key.getName(), pos.getAbsoluteX() + key.getX() + key.getWidth() / 2 - textWidth / 2, pos.getAbsoluteY() + key.getY() + key.getHeight() / 2 - 4, key.isDown() ? ClientInfos.ClientColorInt : Color.WHITE.getRGB());
            this.getFontRenderer().drawString("§f", pos.getAbsoluteX() + key.getX() + key.getWidth() / 2 - textWidth / 2, pos.getAbsoluteY() + key.getY() + key.getHeight() / 2 + 4, -1);
        }
        GL11.glPopMatrix();
    }
    
    public enum KeystrokesMode
    {
        WASD("WASD", 0, new Key[] { Key.W, Key.A, Key.S, Key.D }), 
        WASD_MOUSE("WASD_MOUSE", 1, new Key[] { Key.W, Key.A, Key.S, Key.D, Key.LMB, Key.RMB }), 
        WASD_SNEAK("WASD_SNEAK", 2, new Key[] { Key.W, Key.A, Key.S, Key.D, new Key("Sneak", Minecraft.getMinecraft().gameSettings.keyBindSneak, 1, 41, 58, 18, false) }), 
        WASD_JUMP("WASD_JUMP", 3, new Key[] { Key.W, Key.A, Key.S, Key.D, Key.LMB, Key.RMB, new Key("Jump", Minecraft.getMinecraft().gameSettings.keyBindJump, 1, 61, 58, 18, false) }), 
        WASD_SNEAK_MOUSE("WASD_SNEAK_MOUSE", 4, new Key[] { Key.W, Key.A, Key.S, Key.LMB, Key.RMB, Key.D, new Key("Sneak", Minecraft.getMinecraft().gameSettings.keyBindSneak, 1, 61, 58, 18, false) });
        
        private final Key[] keys;
        private int width;
        private int height;
        
        private KeystrokesMode(final String name, final int ordinal, final Key... keysIn) {
            this.width = 0;
            this.height = 0;
            this.keys = keysIn;
            Key[] keys;
            for (int length = (keys = this.keys).length, i = 0; i < length; ++i) {
                final Key key = keys[i];
                this.width = Math.max(this.width, key.getX() + key.getWidth());
                this.height = Math.max(this.height, key.getY() + key.getHeight());
            }
        }
        
        public int getHeight() {
            return this.height;
        }
        
        public int getWidth() {
            return this.width;
        }
        
        public Key[] getKeys() {
            return this.keys;
        }
    }
    
    private static class Key
    {
        private static final Key W;
        private static final Key A;
        private static final Key S;
        private static final Key D;
        private static final Key LMB;
        private static final Key RMB;
        private final String name;
        private final KeyBinding keyBind;
        private final int x;
        private final int y;
        private final int width;
        private final int height;
        private final boolean cps;
        
        static {
            W = new Key("W", Minecraft.getMinecraft().gameSettings.keyBindForward, 21, 1, 18, 18, false);
            A = new Key("A", Minecraft.getMinecraft().gameSettings.keyBindLeft, 1, 21, 18, 18, false);
            S = new Key("S", Minecraft.getMinecraft().gameSettings.keyBindBack, 21, 21, 18, 18, false);
            D = new Key("D", Minecraft.getMinecraft().gameSettings.keyBindRight, 41, 21, 18, 18, false);
            LMB = new Key("LMB", Minecraft.getMinecraft().gameSettings.keyBindAttack, 1, 41, 28, 18, true);
            RMB = new Key("RMB", Minecraft.getMinecraft().gameSettings.keyBindUseItem, 31, 41, 28, 18, true);
        }
        
        public Key(final String name, final KeyBinding keyBind, final int x, final int y, final int width, final int height, final boolean cps) {
            this.name = name;
            this.keyBind = keyBind;
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.cps = cps;
        }
        
        public boolean isDown() {
            return this.keyBind.isKeyDown();
        }
        
        public int getHeight() {
            return this.height;
        }
        
        public String getName() {
            return this.name;
        }
        
        public int getWidth() {
            return this.width;
        }
        
        public int getX() {
            return this.x;
        }
        
        public int getY() {
            return this.y;
        }
    }
}
