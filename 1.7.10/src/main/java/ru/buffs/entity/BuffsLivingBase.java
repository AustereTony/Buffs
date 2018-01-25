package ru.buffs.entity;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;
import net.minecraftforge.common.IExtendedEntityProperties;
import ru.buffs.main.ActiveBuff;
import ru.buffs.main.Buff;
import ru.buffs.network.NetworkHandler;
import ru.buffs.network.RemoveBuff;
import ru.buffs.network.SyncBuff;

public class BuffsLivingBase implements IExtendedEntityProperties {
	
	public final static String EEP_NAME = "BuffsLivingEEP";
	
    private EntityLivingBase livingBase;
    
    private World world;
    
    private final Map<Integer, ActiveBuff> activeBuffs = new HashMap<Integer, ActiveBuff>();
    
    public BuffsLivingBase() {}  
    
    public static final void register(EntityLivingBase entity) {
    	
    	entity.registerExtendedProperties(BuffsLivingBase.EEP_NAME, new BuffsLivingBase());
    }
	
    public static final BuffsLivingBase get(EntityLivingBase entityLiving) {
    	
        return (BuffsLivingBase) entityLiving.getExtendedProperties(EEP_NAME);
    }

	@Override
	public void saveNBTData(NBTTagCompound mainCompound) {
		
        NBTTagCompound buffsCompound = new NBTTagCompound();
        
        if (this.haveActiveBuffs()) {
        	
            NBTTagList tagList = new NBTTagList();
            
			for (ActiveBuff buff : this.activeBuffsCollection()) {
                
                tagList.appendTag(buff.saveBuffToNBT(buff));
            }

            buffsCompound.setTag("buffs", tagList);         
        }
        
        mainCompound.setTag(EEP_NAME, buffsCompound);
	}

	@Override
	public void loadNBTData(NBTTagCompound mainCompound) {
		
        NBTTagCompound buffsCompound = (NBTTagCompound) mainCompound.getTag(EEP_NAME);
		
        if (buffsCompound.hasKey("buffs", 9)) {
        	
            NBTTagList tagList = buffsCompound.getTagList("buffs", 10);//10 для NBTTagCompound.

            for (int i = 0; i < tagList.tagCount(); ++i) {
            	
                NBTTagCompound tagCompound = tagList.getCompoundTagAt(i);
                
                ActiveBuff buff = ActiveBuff.readBuffFromNBT(tagCompound);

                if (buff != null) {      
                	
                	this.activeBuffs.put(buff.getId(), buff);
                }
            }
        }
	}

	@Override
	public void init(Entity entity, World world) {
		
		this.livingBase = (EntityLivingBase) entity;
		
		this.world = world;
	}
	
	public EntityLivingBase getEntityLiving() {
		
		return this.livingBase;
	}
	
	public World getWorld() {
		
		return this.world;
	}
	
	public boolean haveActiveBuffs() {
		
		return !this.activeBuffs.isEmpty();
	}
	
    public boolean isBuffActive(int buffId) {
    	
    	return this.activeBuffs.containsKey(buffId);
    }    
	
	public Set<Integer> activeBuffsIdSet() {
		
		return this.activeBuffs.keySet();
	}
	
	public Collection<ActiveBuff> activeBuffsCollection() {
		
		return this.activeBuffs.values();
	}
	
	@SideOnly(Side.CLIENT)
	public void putActiveBuffToMap(ActiveBuff buff) {
		
		this.activeBuffs.put(buff.getId(), buff);
	}
	
	@SideOnly(Side.CLIENT)
	public void removeActiveBuffFromMap(int buffId) {
		
		this.activeBuffs.remove(buffId);
	}
	
	public ActiveBuff getActiveBuff(int buffId) {
		
		return this.activeBuffs.get(buffId);
	}
	
    public void addBuff(ActiveBuff buff) {
    	
    	if (this.isBuffActive(buff.getId())) {
    		
    		ActiveBuff activeBuff = this.getActiveBuff(buff.getId());
    		
    		if (activeBuff.getTier() == buff.getTier()) {
    			
    			activeBuff.combineBuffs(buff);  
    			
    			if (this.livingBase instanceof EntityPlayer) {
        			
    				if (!this.world.isRemote) {
    					
    					NetworkHandler.sendTo(new SyncBuff(activeBuff), (EntityPlayerMP) livingBase);
    				}
    			}
    		}
    		
    		else {
    			
    			this.removeBuff(activeBuff.getId());
    			
    			this.activeBuffs.put(buff.getId(), buff);
    			
				Buff.of(buff.getId()).applyBuffEffect(this.livingBase, this.world, buff);
				
    			if (this.livingBase instanceof EntityPlayer) {
        			
    				if (!this.world.isRemote) {
    					
    					NetworkHandler.sendTo(new SyncBuff(buff), (EntityPlayerMP) livingBase);
    				}
    			}
    		}
    	}
    	
    	else {
    		
    		this.activeBuffs.put(buff.getId(), buff);
    		
			Buff.of(buff.getId()).applyBuffEffect(this.livingBase, this.world, buff);
			
			if (this.livingBase instanceof EntityPlayer) {
    			
				if (!this.world.isRemote) {
					
					NetworkHandler.sendTo(new SyncBuff(buff), (EntityPlayerMP) livingBase);
				}
			}
    	}
    }
    
    public void removeBuff(int buffId) {
    	    	
    	if (this.isBuffActive(buffId)) {
    		
    		ActiveBuff activeBuff = this.getActiveBuff(buffId);
    		
			Buff.of(buffId).removeBuffEffect(this.livingBase, this.world, activeBuff);
    		
    		this.activeBuffs.remove(buffId);
    		
			if (this.livingBase instanceof EntityPlayer) {
    			
				if (!this.world.isRemote) {
					
					NetworkHandler.sendTo(new RemoveBuff(buffId), (EntityPlayerMP) livingBase);
				}
			}
    	}
    }
	
    public void clearBuffs(boolean onDeath) {
    	
    	if (this.haveActiveBuffs()) {
    	
    		Iterator buffsIterator = this.activeBuffsIdSet().iterator();

        	while (buffsIterator.hasNext()) {
        	
            	int buffId = (Integer) buffsIterator.next();
            
    			ActiveBuff buff = this.getActiveBuff(buffId);

            	if (!this.world.isRemote) {                			           			
                    
            		if (this.livingBase instanceof EntityPlayer) {
            			
            			if (onDeath) {
            			          			
            				if (!Buff.of(buffId).shouldKeepOnDeath() && !Buff.of(buffId).isPersistent()) {
            				            
            					Buff.of(buffId).removeBuffEffect(this.livingBase, this.world, buff);
            				
            					buffsIterator.remove();
            				}
            			}
            		
            			else {            			           			
                    	            				
        					Buff.of(buffId).removeBuffEffect(this.livingBase, this.world, buff);
            		
            				NetworkHandler.sendTo(new RemoveBuff(buffId), (EntityPlayerMP) this.livingBase);
						
            				buffsIterator.remove();
            			}
            		}
            		
            		else {
            			            			
    					Buff.of(buffId).removeBuffEffect(this.livingBase, this.world, buff);
                							
        				buffsIterator.remove();
            		}
            	}
        	}
    	}
    }  
	
    public void updateBuffs() {
    	
    	if (this.haveActiveBuffs()) {
    	
    		Iterator buffsIterator = this.activeBuffsIdSet().iterator();
    	
    		while (buffsIterator.hasNext()) {
    		
    			int buffId = (Integer) buffsIterator.next();
    		
    			ActiveBuff buff = this.getActiveBuff(buffId);
    		
    			if (!buff.updateBuff(this.livingBase, this.world)) {
    			
    				if (!this.world.isRemote) {

    					Buff.of(buffId).removeBuffEffect(this.livingBase, this.world, buff);
    					
    					if (this.livingBase instanceof EntityPlayer) {
    						
    						NetworkHandler.sendTo(new RemoveBuff(buffId), (EntityPlayerMP) this.livingBase);
    					}
    					
    					buffsIterator.remove();
    				}
    			}
    		}
    	}
    }
}
