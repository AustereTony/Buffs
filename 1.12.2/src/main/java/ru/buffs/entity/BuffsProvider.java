package ru.buffs.entity;

import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

public class BuffsProvider implements ICapabilitySerializable<NBTBase> {
	
	@CapabilityInject(IBuffs.class)
	public static final Capability<IBuffs> BUFFS_CAP = null;
	 
	private IBuffs instance = BUFFS_CAP.getDefaultInstance();

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		
		return capability == BUFFS_CAP;
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		
		return capability == BUFFS_CAP ? BUFFS_CAP.<T> cast(this.instance) : null;
	}

	@Override
	public NBTBase serializeNBT() {
		
		return BUFFS_CAP.getStorage().writeNBT(BUFFS_CAP, this.instance, null);
    }

	@Override
	public void deserializeNBT(NBTBase nbt) {
		
		BUFFS_CAP.getStorage().readNBT(BUFFS_CAP, this.instance, null, nbt);
	}
}
