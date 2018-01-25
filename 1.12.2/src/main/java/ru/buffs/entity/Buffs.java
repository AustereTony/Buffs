package ru.buffs.entity;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import ru.buffs.main.ActiveBuff;
import ru.buffs.main.Buff;
import ru.buffs.network.NetworkHandler;
import ru.buffs.network.RemoveBuff;
import ru.buffs.network.SyncBuff;

public class Buffs implements IBuffs {
	
    private final Map<Integer, ActiveBuff> activeBuffs = new HashMap<Integer, ActiveBuff>();

	@Override
	public boolean haveActiveBuffs() {
		
		return !this.activeBuffs.isEmpty();
	}

	@Override
	public boolean isBuffActive(int buffId) {

    		return this.activeBuffs.containsKey(buffId);
	}

	@Override
	public Set<Integer> activeBuffsIdSet() {

		return this.activeBuffs.keySet();
	}

	@Override
	public Collection<ActiveBuff> activeBuffsCollection() {
		
		return this.activeBuffs.values();
	}

	@Override
	public void putActiveBuffToMap(ActiveBuff buff) {

		this.activeBuffs.put(buff.getId(), buff);
	}

	@Override
	public void removeActiveBuffFromMap(int buffId) {

		this.activeBuffs.remove(buffId);
	}
	
	@Override
	public void copyActiveBuffs(Collection<ActiveBuff> collection) {
		
		for (ActiveBuff buff : collection) {
			
			this.activeBuffs.put(buff.getId(), buff);
		}
	}
	
	@Override
	public ActiveBuff getActiveBuff(int buffId) {

		return this.activeBuffs.get(buffId);
	}

	@Override
	public void addBuff(ActiveBuff buff, EntityLivingBase livingBase) {

    	if (this.isBuffActive(buff.getId())) {
    		
    		ActiveBuff activeBuff = this.getActiveBuff(buff.getId());
    		
    		if (activeBuff.getTier() == buff.getTier()) {
    			
    			activeBuff.combineBuffs(buff);  
    			
    			if (livingBase instanceof EntityPlayer) {
    			
    				if (!livingBase.world.isRemote) {
    					
    					NetworkHandler.sendTo(new SyncBuff(activeBuff), (EntityPlayerMP) livingBase);
    				}
    			}
    		}
    		
    		else {
    			
    			this.removeBuff(livingBase, activeBuff.getId());
    			
    			this.activeBuffs.put(buff.getId(), buff);
    			
				Buff.of(buff.getId()).applyBuffEffect(livingBase, livingBase.world, buff);
				
    			if (livingBase instanceof EntityPlayer) {
        			
    				if (!livingBase.world.isRemote) {
    					
    					NetworkHandler.sendTo(new SyncBuff(buff), (EntityPlayerMP) livingBase);
    				}
    			}
    		}
    	}
    	
    	else {
    		
    		this.activeBuffs.put(buff.getId(), buff);
    		
			Buff.of(buff.getId()).applyBuffEffect(livingBase, livingBase.world, buff);
			
			if (livingBase instanceof EntityPlayer) {
    			
				if (!livingBase.world.isRemote) {
					
					NetworkHandler.sendTo(new SyncBuff(buff), (EntityPlayerMP) livingBase);
				}
			}
    	}
	}

	@Override
	public void removeBuff(EntityLivingBase livingBase, int buffId) {

    	if (this.isBuffActive(buffId)) {
    		
    		ActiveBuff activeBuff = this.getActiveBuff(buffId);
    		
			Buff.of(buffId).removeBuffEffect(livingBase, livingBase.world, activeBuff);
    		
    		this.activeBuffs.remove(buffId);
    		
			if (livingBase instanceof EntityPlayer) {
    			
				if (!livingBase.world.isRemote) {
					
					NetworkHandler.sendTo(new RemoveBuff(buffId), (EntityPlayerMP) livingBase);
				}
			}
    	}
	}

	@Override
	public void clearBuffs(EntityLivingBase livingBase, boolean onDeath) {

    	if (this.haveActiveBuffs()) {
        	
    		Iterator buffsIterator = this.activeBuffsIdSet().iterator();

        	while (buffsIterator.hasNext()) {
        	
            	int buffId = (Integer) buffsIterator.next();
            
    			ActiveBuff buff = this.getActiveBuff(buffId);

            	if (!livingBase.world.isRemote) {                			           			
                    
            		if (livingBase instanceof EntityPlayer) {
            			
            			if (onDeath) {
            			          			
            				if (!Buff.of(buffId).shouldKeepOnDeath() && !Buff.of(buffId).isPersistent()) {
            				            
            					Buff.of(buffId).removeBuffEffect(livingBase, livingBase.world, buff);
            				
            					buffsIterator.remove();
            				}
            			}
            		
            			else {            			           			
                    	
        					Buff.of(buffId).removeBuffEffect(livingBase, livingBase.world, buff);
            		
            				NetworkHandler.sendTo(new RemoveBuff(buffId), (EntityPlayerMP) livingBase);
						
            				buffsIterator.remove();
            			}
            		}
            		
            		else {
            			
    					Buff.of(buffId).removeBuffEffect(livingBase, livingBase.world, buff);
                							
        				buffsIterator.remove();
            		}
            	}
        	}
    	}
	}

	@Override
	public void updateBuffs(EntityLivingBase livingBase) {

    	if (this.haveActiveBuffs()) {
    	
    		Iterator buffsIterator = this.activeBuffsIdSet().iterator();
    	
    		while (buffsIterator.hasNext()) {
    		
    			int buffId = (Integer) buffsIterator.next();
    		
    			ActiveBuff buff = this.getActiveBuff(buffId);
    		
    			if (!buff.updateBuff(livingBase, livingBase.world)) {
    			
    				if (!livingBase.world.isRemote) {

    					Buff.of(buffId).removeBuffEffect(livingBase, livingBase.world, buff);
    					
    					if (livingBase instanceof EntityPlayer) {
    						
    						NetworkHandler.sendTo(new RemoveBuff(buffId), (EntityPlayerMP) livingBase);
    					}
    					
    					buffsIterator.remove();
    				}
    			}
    		}
    	}
	}	
}
