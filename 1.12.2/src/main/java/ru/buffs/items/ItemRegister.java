package ru.buffs.items;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import ru.buffs.main.BuffsMain;

public class ItemRegister {

    public static Item 
    healthRegenerationPotion = new PotionHealthRegeneration("health_regeneration_potion");

    public static void register() {
    	
        setRegister(healthRegenerationPotion);
    }

    @SideOnly(Side.CLIENT)
    public static void registerRender() {
    	        
    	int i;
    	
    	for (i = 0; i < PotionHealthRegeneration.potionNames.length; i++)
    	setRender(healthRegenerationPotion, i, healthRegenerationPotion.getUnlocalizedName().substring(5) + "_" + PotionHealthRegeneration.potionNames[i]);  	
    }

    private static void setRegister(Item item) {
    	
        ForgeRegistries.ITEMS.register(item); 
    }

    @SideOnly(Side.CLIENT)
    private static void setRender(Item item) {
    	
    	ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(new ResourceLocation(BuffsMain.MODID, item.getUnlocalizedName().substring(5)), "inventory"));
    }
    
    @SideOnly(Side.CLIENT)
    private static void setRender(Item item, int meta, String location) {
    	    	
    	ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(new ResourceLocation(BuffsMain.MODID, location), "inventory"));
    }
}
