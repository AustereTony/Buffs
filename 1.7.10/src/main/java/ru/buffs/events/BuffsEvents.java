package ru.buffs.events;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.event.entity.EntityEvent.EntityConstructing;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import ru.buffs.entity.BuffsLivingBase;
import ru.buffs.gui.BuffsOverlay;
import ru.buffs.main.ActiveBuff;
import ru.buffs.main.Buff;
import ru.buffs.network.NetworkHandler;
import ru.buffs.network.SyncBuff;
import ru.buffs.network.SyncMaxHealth;
import ru.buffs.player.BuffsPlayer;

public class BuffsEvents {
		
	@SubscribeEvent
	public void onEntityConstructing(EntityConstructing event) {
		
		if (event.entity instanceof EntityLivingBase) {
			
			if (BuffsLivingBase.get((EntityLivingBase) event.entity) == null) { 
	    	
				BuffsLivingBase.register((EntityLivingBase) event.entity);
	    	}
		}
		
		if (event.entity instanceof EntityPlayer) {
			
			if (BuffsPlayer.get((EntityPlayer) event.entity) == null) { 
	    	
				BuffsPlayer.register((EntityPlayer) event.entity);
			}
		}
	}
	
	@SubscribeEvent
	public void onEntityJoinWorld(EntityJoinWorldEvent event) {
			
	    if (event.entity instanceof EntityPlayer) {
	    		    	
	    	if (!event.entity.worldObj.isRemote) {
	    		
				EntityPlayer player = (EntityPlayer) event.entity;
				
				BuffsLivingBase eLivingBase = BuffsLivingBase.get(player);
	    			    		
	        	if (eLivingBase.haveActiveBuffs()) {
		        	
					for (ActiveBuff buff : eLivingBase.activeBuffsCollection()) {
	            	    			
	        			NetworkHandler.sendTo(new SyncBuff(buff), (EntityPlayerMP) player);
	        		}
	        	}	 
	        	
				BuffsPlayer ePlayer = BuffsPlayer.get(player);//Дополнительные EEP игрока.
	        	
    			NetworkHandler.sendTo(new SyncMaxHealth(ePlayer.getMaxHealth()), (EntityPlayerMP) player);
	    	}
	    }
	}
	
	@SubscribeEvent
	public void onPlayerUpdate(LivingUpdateEvent event) {
		
		if (event.entityLiving instanceof EntityLivingBase) {
			
			BuffsLivingBase.get(event.entityLiving).updateBuffs();
		}
	}
	
	@SubscribeEvent
	public void onLivingDeath(LivingDeathEvent event) {
		
		if (event.entityLiving instanceof EntityPlayer) {
			
			if (!event.entityLiving.worldObj.isRemote) {
			
				BuffsLivingBase.get(event.entityLiving).clearBuffs(true);
			}
		}
	}
	
	@SubscribeEvent
	public void onPlayerClone(PlayerEvent.Clone event) {	
		
		BuffsLivingBase eLivingBase = BuffsLivingBase.get(event.entityPlayer);
		
		NBTTagCompound tagCompound = new NBTTagCompound();
		
		BuffsLivingBase.get(event.original).saveNBTData(tagCompound);
		eLivingBase.loadNBTData(tagCompound);
		
		BuffsPlayer ePlayer = BuffsPlayer.get(event.entityPlayer);
		
		ePlayer.setPlayerHealth(event.entityPlayer, ePlayer.getMaxHealth(), ePlayer.getMaxHealth());
	}
	
	@SubscribeEvent
    public void onRenderOverlay(RenderGameOverlayEvent.Post event) {	
        
		if (event.type == ElementType.CHAT) {

			BuffsOverlay.getInstance().renderBuffs();
		}
    }
	
	@SubscribeEvent
	public void onPlayerAttack(AttackEntityEvent event) {
				
		if (!event.entity.worldObj.isRemote) {
			
			if (event.target instanceof EntityLivingBase) {
				
				if (event.entityPlayer.getCurrentEquippedItem() != null && event.entityPlayer.getCurrentEquippedItem().getItem() == Items.rotten_flesh) {
				
					BuffsLivingBase eEntityLiving = BuffsLivingBase.get((EntityLivingBase) event.target);
					
					eEntityLiving.addBuff(new ActiveBuff(Buff.intoxicationBuff.id, 100));			
				}
			}
		}
	}
}
