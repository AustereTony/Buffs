package ru.buffs.main;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class ActiveBuff {

	private int id, duration, tier;
	
	public ActiveBuff(int buffId, int buffTier, int buffDuration) {
		
		this.id = buffId;
		this.tier = buffTier;
		this.duration = buffDuration;
	}
	
	public ActiveBuff(int buffId, int buffDuration) {
		
		this.id = buffId;
		this.tier = 0;
		this.duration = buffDuration;
	}
	
	public ActiveBuff(int buffId) {
		
		this.id = buffId;
		this.tier = 0;
		this.duration = 0;
	}
	
	public boolean updateBuff(EntityLivingBase entityLivingBase, World world) {
		
		if (Buff.of(this.id).isPersistent() || this.duration > 0) {
			
			if (Buff.of(this.id).isReady(this)) {
				
				Buff.of(this.id).onActive(entityLivingBase, world, this);
			}
			
			if (!Buff.of(this.id).isPersistent()) {
				
				this.duration--;
			}
		}
		
		return Buff.of(this.id).isPersistent() || this.duration > 0;
	}
	
	public void combineBuffs(ActiveBuff buff) {
		
		if (buff.duration > this.duration) {
				
			this.duration = buff.duration;		
		}
	}
	
	public int getId() {
		
		return this.id;		
	}
	
	public int getDuration() {
		
		return this.duration;
	}
	
	public int getTier() {
		
		return this.tier;
	}
	
	public String getBuffName() {
		
		return Buff.of(this.id).getName();
	}

	public NBTTagCompound saveBuffToNBT(ActiveBuff buff) {
		
        NBTTagCompound tagCompound = new NBTTagCompound();

        tagCompound.setByte("id", (byte) buff.getId());
        tagCompound.setByte("tier", (byte) buff.getTier());
        tagCompound.setInteger("duration", buff.getDuration());
        
        return tagCompound;
	}

	public static ActiveBuff readBuffFromNBT(NBTTagCompound tagCompound) {
		
		return new ActiveBuff(tagCompound.getByte("id"), tagCompound.getByte("tier"), tagCompound.getInteger("duration"));
	}
}
