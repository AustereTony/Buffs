package ru.buffs.proxy;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import ru.buffs.entity.Buffs;
import ru.buffs.entity.BuffsStorage;
import ru.buffs.entity.IBuffs;
import ru.buffs.events.BuffsCupRegistrationEvent;
import ru.buffs.events.BuffsEvents;
import ru.buffs.items.ItemRegister;
import ru.buffs.network.NetworkHandler;

public class CommonProxy {

    public void preInit(FMLPreInitializationEvent event) {
    	
    	NetworkHandler.registerPackets();   
    	
    	ItemRegister.register(); 	
    }

    public void init(FMLInitializationEvent event) {   	
    	
    	CapabilityManager.INSTANCE.register(IBuffs.class, new BuffsStorage(), Buffs.class);
    	
    	MinecraftForge.EVENT_BUS.register(new BuffsCupRegistrationEvent());
    	MinecraftForge.EVENT_BUS.register(new BuffsEvents());
    }
    
	public EntityPlayer getPlayerEntity(MessageContext ctx) {
		
		return ctx.getServerHandler().player;
	}
}
