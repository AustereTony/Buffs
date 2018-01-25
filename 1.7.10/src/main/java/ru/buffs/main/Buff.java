package ru.buffs.main;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import ru.buffs.network.NetworkHandler;
import ru.buffs.network.SetStepHeight;
import ru.buffs.player.BuffsPlayer;

public class Buff {

	public final int id;

	private String name;
	
	private int iconIndex;
	
	private boolean keepOnDeath, isPersistent;
	
	public static List<Buff> buffs = new ArrayList<Buff>();
	
	public static final Buff 
	potionCooldown = new Buff().setName("buff.potionCooldown").setIconIndex(1, 0),
	healthRegeneration = new Buff().setName("buff.healthRegeneration").setIconIndex(2, 0),
	healthBoost = new Buff().setName("buff.healthBoost").setIconIndex(3, 0).keepOnDeath(),
	advancedClimbing = new Buff().setName("buff.advancedClimbing").setIconIndex(4, 0),
	intoxicationBuff = new Buff().setName("buff.intoxicationBuff").setIconIndex(5, 0);	
	
	public Buff() {
		
		this.id = buffs.size();
		
		this.buffs.add(this);			
	}
	
	public static Buff of(int buffId) {
		
		return buffs.get(buffId);
	}
	
	protected void onActive(EntityLivingBase entityLivingBase, World world, ActiveBuff buff) {
		
		int 
		tier = buff.getTier(),
		duration = buff.getDuration();
		
		if (this.id == healthRegeneration.id) {
			
			if (tier == 0) {
				
				entityLivingBase.heal(1.0F);
			}
			
			else if (tier == 1) {
				
				entityLivingBase.heal(1.5F);
			}
			
			else if (tier == 2) {
				
				entityLivingBase.heal(1.5F);
			}
		}
		
		else if (this.id == advancedClimbing.id) {
			
			if (entityLivingBase instanceof EntityPlayer) {
			
				if (world.isRemote) {
				
					if (entityLivingBase.stepHeight != 1.0F) {
					
						entityLivingBase.stepHeight = 1.0F;
					}
				}
			}
		}
		
		else if (this.id == intoxicationBuff.id) {
			
	        if (entityLivingBase.getHealth() > 1.0F) {
	        	
	        	entityLivingBase.attackEntityFrom(DamageSource.magic, 1.0F);
	        }
		}
	}
	
	protected boolean isReady(ActiveBuff buff) {
		
		int 
		tier = buff.getTier(),
		duration = buff.getDuration();
		
		if (this.id == healthRegeneration.id) {
			
	        int k = 0;
	        
			if (tier == 0) {
				
				k = 40;
			}
			
			else if (tier == 1) {
				
				k = 30;
			}
			
			else if (tier == 2) {
				
				k = 20;
			}
	        
	        return duration % k == 0;	
		}
		
		else if (this.id == advancedClimbing.id) {
			
			return true;
		}
		
		else if (this.id == intoxicationBuff.id) {
			
			return duration % 20 == 0;
		}
		
		return false;
	}
		
    public void applyBuffEffect(EntityLivingBase entityLivingBase, World world, ActiveBuff buff) {
    	
    	if (!world.isRemote) {
    	
    		int 
    		id = buff.getId(),
    		tier = buff.getTier();
       		
    		if (id == healthBoost.id) {

    			if (entityLivingBase instanceof EntityPlayer) {
    				
    				EntityPlayer player = (EntityPlayer) entityLivingBase;

    				BuffsPlayer ePlayer = BuffsPlayer.get(player);

    				ePlayer.setPlayerHealth(player, ePlayer.getMaxHealth() + 2 * (tier + 1), player.getHealth());  
    			}
    		}
    		
    		else if (id == advancedClimbing.id) {
    			
    			if (entityLivingBase instanceof EntityPlayer) {
    			
    				NetworkHandler.sendTo(new SetStepHeight(true), (EntityPlayerMP) entityLivingBase);
    			}
    		}
    	}
    }
    
	public void removeBuffEffect(EntityLivingBase entityLivingBase, World world, ActiveBuff buff) {
    	
    	if (!world.isRemote) {

    		int 
    		id = buff.getId(),
    		tier = buff.getTier();  
    		
    		if (id == healthBoost.id) {

    			if (entityLivingBase instanceof EntityPlayer) {
    				
    				EntityPlayer player = (EntityPlayer) entityLivingBase;

    				BuffsPlayer ePlayer = BuffsPlayer.get(player);

    				ePlayer.setPlayerHealth(player, ePlayer.getMaxHealth() - 2 * (tier + 1), player.getHealth());  
    			}
    		}
    		
    		else if (id == advancedClimbing.id) {
    			
    			if (entityLivingBase instanceof EntityPlayer) {
    			
    				NetworkHandler.sendTo(new SetStepHeight(false), (EntityPlayerMP) entityLivingBase);
    			}
    		}
    	}
    }
	
	protected Buff setName(String name) {
		
		this.name = name;
		
		return this;
	}
	
	public String getName() {
		
		return this.name;
	}
	
	protected Buff setIconIndex(int x, int y) {
		
		this.iconIndex = x + y * 8;
		
		return this;
	}
	
	public int getIconIndex() {
		
		return this.iconIndex;
	}
	
	protected Buff keepOnDeath() {
		
		this.keepOnDeath = true;
		
		return this;
	}
	
	public boolean shouldKeepOnDeath() {
		
		return this.keepOnDeath;
	}
		
	protected Buff setPersistent() {
		
		this.isPersistent = true;
		
		return this;
	}
	
	public boolean isPersistent() {
		
		return this.isPersistent;
	}
	
    public String getDurationForDisplay(ActiveBuff buff) {
    	
    	int 
    	i = buff.getDuration(),
        j = i / 20,
        k = j / 60;
    	
        j %= 60;
        
        return Buff.of(buff.getId()).isPersistent ? "-:-" : j < 10 ? String.valueOf(k) + ":0" + String.valueOf(j) : String.valueOf(k) + ":" + String.valueOf(j);	
    }
}
