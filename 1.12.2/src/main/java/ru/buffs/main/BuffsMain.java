package ru.buffs.main;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import ru.buffs.proxy.CommonProxy;

@Mod(modid = BuffsMain.MODID, name = BuffsMain.VERSION, version = BuffsMain.VERSION)
public class BuffsMain {
	
    public static final String MODID = "buffs";
    public static final String NAME = "Buffs";
    public static final String VERSION = "1.0";
    
    @SidedProxy(clientSide = "ru.buffs.proxy.ClientProxy", serverSide = "ru.buffs.proxy.CommonProxy")
    public static CommonProxy proxy;
    
    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
    	
    	proxy.preInit(event);    	  	
    }
    
    @EventHandler
    public void init(FMLInitializationEvent event) {
    	
    	proxy.init(event); 	    
    }
}
