package ru.buffs.entity;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;
import ru.buffs.main.ActiveBuff;

public class BuffsStorage  implements IStorage<IBuffs> {

	@Override
	public NBTBase writeNBT(Capability<IBuffs> capability, IBuffs instance, EnumFacing side) {
				
        NBTTagCompound buffsCompound = new NBTTagCompound();
        		
        if (instance.haveActiveBuffs()) {
        	
            NBTTagList tagList = new NBTTagList();
            
			for (ActiveBuff buff : instance.activeBuffsCollection()) {
                
                tagList.appendTag(buff.saveBuffToNBT(buff));
            }

            buffsCompound.setTag("buffs", tagList);         
        }      
		
		return buffsCompound;
	}

	@Override
	public void readNBT(Capability<IBuffs> capability, IBuffs instance, EnumFacing side, NBTBase nbt) {
		
        NBTTagCompound buffsCompound = (NBTTagCompound) nbt;
		
        if (buffsCompound.hasKey("buffs", 9)) {
        	
            NBTTagList tagList = buffsCompound.getTagList("buffs", 10);//10 для NBTTagCompound.

            for (int i = 0; i < tagList.tagCount(); ++i) {
            	
                NBTTagCompound tagCompound = tagList.getCompoundTagAt(i);
                
                ActiveBuff buff = ActiveBuff.readBuffFromNBT(tagCompound);

                if (buff != null) {      
                	
                	instance.putActiveBuffToMap(buff);
                }
            }
        }
	}	
}
