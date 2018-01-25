package ru.buffs.main;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public class Buff {

	public final int id;

	private String name;
	
	private int iconIndex;
	
	private boolean keepOnDeath, isPersistent;
	
	public static List<Buff> buffs = new ArrayList<Buff>();
	
	public static final Buff 
	potionCooldown = new Buff().setName("buff.potion_cooldown").setIconIndex(1, 0),
	healthRegeneration = new Buff().setName("buff.health_regeneration").setIconIndex(2, 0),
	sprain = new Buff().setName("buff.sprain").setIconIndex(3, 0).setPersistent(),
	intoxication = new Buff().setName("buff.intoxication").setIconIndex(4, 0);	
	
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
				
				entityLivingBase.heal(2.0F);
			}
		}
		
		else if (this.id == sprain.id) {
			
			entityLivingBase.motionX *= 0.4F;
			entityLivingBase.motionZ *= 0.4F;
		}
		
		else if (this.id == intoxication.id) {
			
	        if (entityLivingBase.getHealth() > 1.0F) {
	        	
	        	entityLivingBase.attackEntityFrom(DamageSource.MAGIC, 1.0F);
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
		
		else if (this.id == sprain.id) {
			
			return true;
		}
		
		else if (this.id == intoxication.id) {
			
			return duration % 20 == 0;
		}
		
		return false;
	}
		
    public void applyBuffEffect(EntityLivingBase entityLivingBase, World world, ActiveBuff buff) {
    	
    	if (!world.isRemote) {
    	
    		int 
    		id = buff.getId(),
    		tier = buff.getTier();
    	}
    }
    
    public void removeBuffEffect(EntityLivingBase entityLivingBase, World world, ActiveBuff buff) {
    	
    	if (!world.isRemote) {

    		int 
    		id = buff.getId(),
    		tier = buff.getTier();  
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

