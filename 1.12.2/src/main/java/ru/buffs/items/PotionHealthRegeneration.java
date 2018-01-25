package ru.buffs.items;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import ru.buffs.entity.BuffsProvider;
import ru.buffs.entity.IBuffs;
import ru.buffs.main.ActiveBuff;
import ru.buffs.main.Buff;
import ru.buffs.main.BuffsMain;

public class PotionHealthRegeneration extends Item {
	
	public static final String[] potionNames = new String[] {"first", "second", "third"};

    public PotionHealthRegeneration(String name) {
		      		
	    this.setUnlocalizedName(name);
	    this.setRegistryName(new ResourceLocation(BuffsMain.MODID, name));
	    	    
	    this.setCreativeTab(CreativeTabs.BREWING);
	    
        this.setHasSubtypes(true);
	}
    
	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
		
        ItemStack itemStack = player.getHeldItem(hand);
        		
		IBuffs buffs = player.getCapability(BuffsProvider.BUFFS_CAP, null);
		
		if (!buffs.isBuffActive(Buff.potionCooldown.id)) {
						
			buffs.addBuff(new ActiveBuff(Buff.healthRegeneration.id, itemStack.getItemDamage(), itemStack.getItemDamage() == 0 ? 600 : (itemStack.getItemDamage() == 1 ? 900 : 1200)), player);
			buffs.addBuff(new ActiveBuff(Buff.potionCooldown.id, 300), player);
			
			itemStack.splitStack(1);
			
	        return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemStack);
		}
		
		return new ActionResult<ItemStack>(EnumActionResult.PASS, itemStack);		
	}
    
    @Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
    	
    	if (tab == CreativeTabs.BREWING) {
    	
    		for (int i = 0; i < potionNames.length; i++) {
    		
    			items.add(new ItemStack(this, 1, i));
    		}
    	}
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
    	
    	for (int i = 0; i < potionNames.length; i++) {
    		
    		if(stack.getItemDamage() == i) {
    			
    			return this.getUnlocalizedName() + "." + potionNames[i];
    		}
    		
    		else {
    			
    			continue;
    		}
    	}
    	
    	return this.getUnlocalizedName();
    }
}
