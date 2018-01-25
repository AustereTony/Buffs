package ru.buffs.events;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerWakeUpEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import ru.buffs.entity.BuffsProvider;
import ru.buffs.entity.IBuffs;
import ru.buffs.gui.BuffsOverlay;
import ru.buffs.main.ActiveBuff;
import ru.buffs.main.Buff;
import ru.buffs.network.BuffsSyncRequest;
import ru.buffs.network.NetworkHandler;

public class BuffsEvents {
	
	@SubscribeEvent
	public void onPlayerJoinWorld(EntityJoinWorldEvent event) {
			
	    if (event.getEntity() instanceof EntityPlayer) {
	    		    		    	
			EntityPlayer player = (EntityPlayer) event.getEntity();
			
	    	if (player.world.isRemote) {
	    		
	    		if (player != null) {
	    			
        			NetworkHandler.sendToServer(new BuffsSyncRequest());
	    		}
	    	}
	    }
	}
	
	@SubscribeEvent
	public void onLivingUpdate(LivingUpdateEvent event) {
		
		if (event.getEntityLiving() instanceof EntityLivingBase) {
			
			EntityLivingBase livingBase = event.getEntityLiving();
			
			livingBase.getCapability(BuffsProvider.BUFFS_CAP, null).updateBuffs(livingBase);
		}
	}
	
	@SubscribeEvent
	public void onPlayerDeath(LivingDeathEvent event) {
		
		if (event.getEntityLiving() instanceof EntityPlayer) {
						
			if (!event.getEntityLiving().world.isRemote) {
				
				EntityPlayer player = (EntityPlayer) event.getEntityLiving();
				
				IBuffs buffs = player.getCapability(BuffsProvider.BUFFS_CAP, null);

				buffs.clearBuffs(player, true);
			}
		}
	}
	
	@SubscribeEvent
	public void onPlayerClone(PlayerEvent.Clone event) {	
						
		EntityPlayer player = event.getEntityPlayer();

		IBuffs buffs = player.getCapability(BuffsProvider.BUFFS_CAP, null);
		
		IBuffs oldBuffs = event.getOriginal().getCapability(BuffsProvider.BUFFS_CAP, null);
		 
		buffs.copyActiveBuffs(oldBuffs.activeBuffsCollection());
	}
	
	@SubscribeEvent
    public void onRenderOverlay(RenderGameOverlayEvent.Post event) {	
        
		if (event.getType() == ElementType.CHAT) {

			BuffsOverlay.getInstance().renderBuffs();
		}
    }
	
	@SubscribeEvent
	public void onPlayerFall(LivingFallEvent event) {
		
		if (!event.getEntity().world.isRemote) {
			
			if (event.getEntityLiving() instanceof EntityPlayer) {
				
				EntityPlayer player = (EntityPlayer) event.getEntityLiving();
				
				if (!player.capabilities.isCreativeMode) {
				
					if (event.getDistance() > 5.0F) {
					
						IBuffs buffs = player.getCapability(BuffsProvider.BUFFS_CAP, null);
					
						if (!buffs.isBuffActive(Buff.sprain.id)) {
					
							buffs.addBuff(new ActiveBuff(Buff.sprain.id), player);
						}
					}
				}
			}
		}
	}
	
	@SubscribeEvent
	public void onWakeUp(PlayerWakeUpEvent event) {
		
		EntityPlayer player = event.getEntityPlayer();
												
		IBuffs buffs = player.getCapability(BuffsProvider.BUFFS_CAP, null);
				
		if (buffs.isBuffActive(Buff.sprain.id)) {
					
			buffs.removeBuff(player, Buff.sprain.id);
		}
	}
	
	@SubscribeEvent
	public void onPlayerAttack(AttackEntityEvent event) {
				
		if (!event.getEntity().world.isRemote) {
			
			if (event.getTarget() instanceof EntityLivingBase) {
				
				EntityPlayer player = event.getEntityPlayer();
				
				if (player.getHeldItemMainhand() != null && player.getHeldItemMainhand().getItem() == Items.ROTTEN_FLESH) {
				
					IBuffs buffs = event.getTarget().getCapability(BuffsProvider.BUFFS_CAP, null);
				
					buffs.addBuff(new ActiveBuff(Buff.intoxication.id, 100), (EntityLivingBase) event.getTarget());		
				}
			}
		}
	}
}

