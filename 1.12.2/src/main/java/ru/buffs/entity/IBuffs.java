package ru.buffs.entity;

import java.util.Collection;
import java.util.Set;

import net.minecraft.entity.EntityLivingBase;
import ru.buffs.main.ActiveBuff;

public interface IBuffs {
	
	boolean haveActiveBuffs();
	
    boolean isBuffActive(int buffId);
    
	Set<Integer> activeBuffsIdSet();
	
	Collection<ActiveBuff> activeBuffsCollection();
	
	void putActiveBuffToMap(ActiveBuff buff);
	
	void removeActiveBuffFromMap(int buffId);
	
	void copyActiveBuffs(Collection<ActiveBuff> collection);
	
	ActiveBuff getActiveBuff(int buffId);
	
    void addBuff(ActiveBuff buff, EntityLivingBase livingBase);
    
    void removeBuff(EntityLivingBase livingBase, int buffId);
    
    void clearBuffs(EntityLivingBase livingBase, boolean onDeath);
    
    void updateBuffs(EntityLivingBase livingBase);
}
