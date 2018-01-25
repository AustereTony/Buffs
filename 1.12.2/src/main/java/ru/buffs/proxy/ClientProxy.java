package ru.buffs.proxy;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import ru.buffs.items.ItemRegister;
import ru.buffs.main.BuffsMain;

public class ClientProxy extends CommonProxy {

    public void preInit(FMLPreInitializationEvent event) {

    	super.preInit(event);
    	
    	ItemRegister.registerRender();
    }

    public void init(FMLInitializationEvent event) {

    	super.init(event);   	
    	
		ModelBakery.registerItemVariants(ItemRegister.healthRegenerationPotion, 
				new ResourceLocation(BuffsMain.MODID, "health_regeneration_potion_first"), 
				new ResourceLocation(BuffsMain.MODID, "health_regeneration_potion_second"), 
				new ResourceLocation(BuffsMain.MODID, "health_regeneration_potion_third"));
    }
    
	@Override
	public EntityPlayer getPlayerEntity(MessageContext ctx) {
		
	    return (ctx.side.isClient() ? Minecraft.getMinecraft().player : super.getPlayerEntity(ctx));
	}
}
