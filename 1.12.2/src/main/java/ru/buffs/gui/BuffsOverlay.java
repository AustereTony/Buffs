package ru.buffs.gui;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import ru.buffs.entity.BuffsProvider;
import ru.buffs.entity.IBuffs;
import ru.buffs.main.ActiveBuff;
import ru.buffs.main.Buff;
import ru.buffs.main.BuffsMain;

public class BuffsOverlay {
	
	private static BuffsOverlay instance = new BuffsOverlay();

	private static Minecraft mc = Minecraft.getMinecraft();
	
    private static final ResourceLocation buffIcons = new ResourceLocation(BuffsMain.MODID + ":textures/gui/bufficons.png");
	
    private BuffsOverlay() {}
    
	public static BuffsOverlay getInstance() {
		
		return instance;
	}
	
	public void renderBuffs() {
    	
		if (this.mc.inGameHasFocus) {

			EntityPlayer player = this.mc.player;
    	
			IBuffs buffs = player.getCapability(BuffsProvider.BUFFS_CAP, null);
    	
			ScaledResolution scaledResolution = new ScaledResolution(this.mc);
        
			int
			i = scaledResolution.getScaledWidth() / 2 + 240,
			j = scaledResolution.getScaledHeight() - 30,
			counter = 0,
			index = 0;
    	
			if (buffs.haveActiveBuffs()) {
				
				for (ActiveBuff buff : buffs.activeBuffsCollection()) {
    			
					index = Buff.of(buff.getId()).getIconIndex();
            	
					counter++;    			
            	
			        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);					
					GL11.glEnable(GL11.GL_BLEND);
    			            	            	
					GL11.glPushMatrix();
            	
					GL11.glTranslatef(i + 5, j + 25 - 24 * counter, 0.0F);
					GL11.glScalef(0.5F, 0.5F, 0.5F);
            	
					this.mc.getTextureManager().bindTexture(buffIcons);           	
            		this.drawModalRectWithCustomSizedTexture(0, 0, index % 8 * 32, index / 8 * 32, 32, 32, 160, 32);
            	
            		GL11.glPopMatrix();      
            	
            		GL11.glPushMatrix();
            	
            		GL11.glTranslatef(i + 15, j + 42 - 24 * counter, 0.0F);          	            	
            		GL11.glScalef(0.7F, 0.7F, 0.7F);
            		
            		int durLength = Buff.of(buff.getId()).getDurationForDisplay(buff).length();
            	            	
            		this.mc.fontRenderer.drawStringWithShadow(Buff.of(buff.getId()).getDurationForDisplay(buff), - durLength * 3, 0, 8421504);
            	
            		String tier = "";
            	
            		if (buff.getTier() == 0) {
            		
            			tier = "I";
            		}
            	
            		else if (buff.getTier() == 1) {
            		
            			tier = "II";
            		} 
            	
            		else if (buff.getTier() == 2) {
            		
            			tier = "III";
            		}          	
            	
            		this.mc.fontRenderer.drawStringWithShadow(tier, 7 - tier.length() * 3, - 23, 8421504);
            		
            		GL11.glPopMatrix();
            	
            		GL11.glDisable(GL11.GL_BLEND);           	            	            	
				}
			}    
		}
    }
	
    public static void drawModalRectWithCustomSizedTexture(int x, int y, float u, float v, int width, int height, float textureWidth, float textureHeight) {
    	
        float f = 1.0F / textureWidth;
        float f1 = 1.0F / textureHeight;
        
        Tessellator tessellator = Tessellator.getInstance();
        
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
        
        bufferbuilder.pos((double)x, (double)(y + height), 0.0D).tex((double)(u * f), (double)((v + (float)height) * f1)).endVertex();
        bufferbuilder.pos((double)(x + width), (double)(y + height), 0.0D).tex((double)((u + (float)width) * f), (double)((v + (float)height) * f1)).endVertex();
        bufferbuilder.pos((double)(x + width), (double)y, 0.0D).tex((double)((u + (float)width) * f), (double)(v * f1)).endVertex();
        bufferbuilder.pos((double)x, (double)y, 0.0D).tex((double)(u * f), (double)(v * f1)).endVertex();
        
        tessellator.draw();
    }
}

