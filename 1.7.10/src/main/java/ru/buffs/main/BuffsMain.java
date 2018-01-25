package ru.buffs.main;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.item.Item;
import net.minecraftforge.common.MinecraftForge;
import ru.buffs.events.BuffsEvents;
import ru.buffs.items.PotionAdvancedClimbing;
import ru.buffs.items.PotionHealthBoost;
import ru.buffs.items.PotionHealthRegeneration;
import ru.buffs.network.NetworkHandler;
import ru.buffs.proxy.CommonProxy;

@Mod(modid = BuffsMain.MODID, name = BuffsMain.NAME, version = BuffsMain.VERSION)
public class BuffsMain {
	
    public static final String MODID = "buffs";
    public static final String NAME = "Buffs";
    public static final String VERSION = "1.0";
	
	@SidedProxy(clientSide = "ru.buffs.proxy.ClientProxy", serverSide = "ru.buffs.proxy.CommonProxy")
	public static CommonProxy proxy;
	
	public static Item 
	potionHealthRegeneration,
	potionHealthBoost,
	potionAdvancedClimbing;
	    
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		
		NetworkHandler.registerPackets();
		
		potionHealthRegeneration = new PotionHealthRegeneration().setUnlocalizedName("potion.healthRegeneration");
		potionHealthBoost = new PotionHealthBoost().setUnlocalizedName("potion.healthBoost");
		potionAdvancedClimbing = new PotionAdvancedClimbing().setUnlocalizedName("potion.advancedClimbing");
		
		GameRegistry.registerItem(potionHealthRegeneration, "healthRegeneratioPotion");
		GameRegistry.registerItem(potionHealthBoost, "healthBoostPotion");
		GameRegistry.registerItem(potionAdvancedClimbing, "advancedClimbingPotion");
	}
	
    @EventHandler
    public void init(FMLInitializationEvent event) {
    	    	
		MinecraftForge.EVENT_BUS.register(new BuffsEvents());
    }
}
