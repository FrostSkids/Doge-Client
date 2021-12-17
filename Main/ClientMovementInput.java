// 
// Decompiled by Procyon v0.5.36
// 

package Main;

import net.minecraft.potion.Potion;
import java.text.DecimalFormat;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.util.MovementInput;

public class ClientMovementInput extends MovementInput
{
    public static boolean sprint;
    private GameSettings gameSettings;
    private int sneakWasPressed;
    private int sprintWasPressed;
    private EntityPlayerSP player;
    private float originalFlySpeed;
    private float boostedFlySpeed;
    private Minecraft mc;
    public boolean flyBoost;
    public float flyBoostFactor;
    public int keyHoldTicks;
    public boolean shiftToggled;
    private static final DecimalFormat df;
    
    static {
        ClientMovementInput.sprint = false;
        df = new DecimalFormat("#.0");
    }
    
    public ClientMovementInput(final GameSettings gameSettings) {
        this.sneakWasPressed = 0;
        this.sprintWasPressed = 0;
        this.originalFlySpeed = -1.0f;
        this.boostedFlySpeed = 1.0f;
        this.flyBoost = false;
        this.flyBoostFactor = 1.0f;
        this.keyHoldTicks = 7;
        this.shiftToggled = false;
        this.gameSettings = gameSettings;
        this.mc = Minecraft.getMinecraft();
    }
    
    @Override
    public void updatePlayerMoveState() {
        this.player = this.mc.thePlayer;
        this.moveStrafe = 0.0f;
        this.moveForward = 0.0f;
        if (this.gameSettings.keyBindForward.isKeyDown()) {
            ++this.moveForward;
        }
        if (this.gameSettings.keyBindBack.isKeyDown()) {
            --this.moveForward;
        }
        if (this.gameSettings.keyBindLeft.isKeyDown()) {
            ++this.moveStrafe;
        }
        if (this.gameSettings.keyBindRight.isKeyDown()) {
            --this.moveStrafe;
        }
        this.jump = this.gameSettings.keyBindJump.isKeyDown();
        this.sneak = this.gameSettings.keyBindSneak.isKeyDown();
        if (this.sneak) {
            this.moveStrafe *= 0.3f;
            this.moveForward *= 0.3f;
        }
        if (this.gameSettings.keyBindSprint.isKeyDown()) {
            if (this.sprintWasPressed == 0) {
                if (ClientMovementInput.sprint) {
                    this.sprintWasPressed = -1;
                }
                else if (this.player.capabilities.isFlying) {
                    this.sprintWasPressed = this.keyHoldTicks + 1;
                }
                else {
                    this.sprintWasPressed = 1;
                }
                ClientMovementInput.sprint = !ClientMovementInput.sprint;
            }
            else if (this.sprintWasPressed > 0) {
                ++this.sprintWasPressed;
            }
        }
        else {
            if (this.keyHoldTicks > 0 && this.sprintWasPressed > this.keyHoldTicks) {
                ClientMovementInput.sprint = false;
            }
            this.sprintWasPressed = 0;
        }
        if (ClientMovementInput.sprint && this.moveForward == 1.0f && this.player.onGround && !this.player.isUsingItem() && !this.player.isPotionActive(Potion.blindness)) {
            this.player.setSprinting(true);
        }
        if (this.flyBoost && this.player.capabilities.isCreativeMode && this.player.capabilities.isFlying && this.mc.getRenderViewEntity() == this.player && ClientMovementInput.sprint) {
            if (this.originalFlySpeed < 0.0f || this.player.capabilities.getFlySpeed() != this.boostedFlySpeed) {
                this.originalFlySpeed = this.player.capabilities.getFlySpeed();
            }
            this.boostedFlySpeed = this.originalFlySpeed * this.flyBoostFactor;
            this.player.capabilities.setFlySpeed(this.boostedFlySpeed);
            if (this.sneak) {
                final EntityPlayerSP player = this.player;
                player.motionY -= 0.15 * (this.flyBoostFactor - 1.0f);
            }
            if (this.jump) {
                final EntityPlayerSP player2 = this.player;
                player2.motionY += 0.15 * (this.flyBoostFactor - 1.0f);
            }
        }
        else {
            if (this.player.capabilities.getFlySpeed() == this.boostedFlySpeed) {
                this.player.capabilities.setFlySpeed(this.originalFlySpeed);
            }
            this.originalFlySpeed = -1.0f;
        }
    }
    
    public String getDisplayText() {
        String displayText = "";
        final boolean isFlying = this.mc.thePlayer.capabilities.isFlying;
        final boolean isRiding = this.mc.thePlayer.isRiding();
        final boolean isHoldingSneak = this.gameSettings.keyBindSneak.isKeyDown();
        final boolean isHoldingSprint = this.gameSettings.keyBindSprint.isKeyDown();
        if (isFlying) {
            if (this.originalFlySpeed > 0.0f) {
                displayText = String.valueOf(displayText) + "§f[" + ClientInfos.ClientColorMods + "Flying (" + ClientMovementInput.df.format(this.boostedFlySpeed / this.originalFlySpeed) + "x Boost)" + "§f]";
            }
            else {
                displayText = String.valueOf(displayText) + "§f[" + ClientInfos.ClientColorMods + "Flying" + "§f]";
            }
        }
        if (isRiding) {
            displayText = String.valueOf(displayText) + "§f[" + ClientInfos.ClientColorMods + "Riding" + "§f]";
        }
        else if (ClientMovementInput.sprint && !isFlying && !isRiding) {
            if (isHoldingSprint) {
                displayText = String.valueOf(displayText) + "§f[" + ClientInfos.ClientColorMods + "Sprinting Held" + "§f]";
            }
            else {
                displayText = String.valueOf(displayText) + "§f[" + ClientInfos.ClientColorMods + "Sprinting Toggled" + "§f]";
            }
        }
        return displayText.trim();
    }
}
