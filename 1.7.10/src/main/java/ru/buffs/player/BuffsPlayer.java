package ru.buffs.player;

import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.IExtendedEntityProperties;

public class BuffsPlayer implements IExtendedEntityProperties {
	
	public final static String EEP_NAME = "BuffsPlayerEEP";
		
    private EntityPlayer player;
    
    private World world;
    
    private float maxHealth;
        
    public BuffsPlayer() {
        
        this.maxHealth = 20.0F;
    }
    
    public static final void register(EntityPlayer player) {
    	
        player.registerExtendedProperties(BuffsPlayer.EEP_NAME, new BuffsPlayer());
    }
	
    public static final BuffsPlayer get(EntityPlayer player) {
    	
        return (BuffsPlayer) player.getExtendedProperties(EEP_NAME);
    }

	@Override
	public void saveNBTData(NBTTagCompound mainCompound) {
		
        NBTTagCompound buffsCompound = new NBTTagCompound();
        
        mainCompound.setTag(EEP_NAME, buffsCompound);
        
        buffsCompound.setFloat("maxHealth", this.maxHealth);
	}

	@Override
	public void loadNBTData(NBTTagCompound mainCompound) {
		
        NBTTagCompound buffsCompound = (NBTTagCompound) mainCompound.getTag(EEP_NAME);
        
        this.maxHealth = buffsCompound.getFloat("maxHealth");
	}

	@Override
	public void init(Entity entity, World world) {
		
		this.player = (EntityPlayer) entity;
		
		this.world = world;
	}
	
	public EntityPlayer getPlayer() {
		
		return this.player;
	}
	
	public World getWorld() {
		
		return this.world;
	}
    
	public void setPlayerHealth(EntityPlayer player, float maxHealth, float startHealth) {
		
		player.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(maxHealth);
		player.setHealth(startHealth);
		
		this.maxHealth = maxHealth;
    }
	
	public void setMaxHealth(float maxHealth) {
		
		this.maxHealth = maxHealth;
	}
	
	public float getMaxHealth() {
		
		return this.maxHealth;
	}
}
