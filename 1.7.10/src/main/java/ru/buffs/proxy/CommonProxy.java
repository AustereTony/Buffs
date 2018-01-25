package ru.buffs.proxy;

import java.util.HashMap;
import java.util.Map;

import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

public class CommonProxy {
	
	public EntityPlayer getPlayerEntity(MessageContext ctx) {
		
		return ctx.getServerHandler().playerEntity;
	}
}
