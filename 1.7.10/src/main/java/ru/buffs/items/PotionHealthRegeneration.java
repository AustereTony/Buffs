package ru.buffs.items;

import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import ru.buffs.entity.BuffsLivingBase;
import ru.buffs.main.ActiveBuff;
import ru.buffs.main.Buff;
import ru.buffs.main.BuffsMain;

public class PotionHealthRegeneration extends Item {
	
	public static final String[] potionNames = new String[] {"first", "second", "third"};
	
	@SideOnly(Side.CLIENT)
	private IIcon[] icons;
	
	public PotionHealthRegeneration() {
		
		super();
		
		this.setHasSubtypes(true);
		this.setCreativeTab(CreativeTabs.tabBrewing);
	}
	
	@Override
	public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player) {	
		
    	BuffsLivingBase eLivingBase = BuffsLivingBase.get(player); 
		
		if (!eLivingBase.isBuffActive(Buff.potionCooldown.id)) {
						
			eLivingBase.addBuff(new ActiveBuff(Buff.healthRegeneration.id, itemStack.getItemDamage(), 700));
			eLivingBase.addBuff(new ActiveBuff(Buff.potionCooldown.id, 300));
			
			itemStack.stackSize--;
		}
		
		return itemStack;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIconFromDamage(int damage) {
		
	    return icons[damage];
	}
	
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister iconregister) {
		
		icons = new IIcon[potionNames.length];
		
		for (int i = 0; i < icons.length; i++) {
			
			icons[i] = iconregister.registerIcon(BuffsMain.MODID + ":" + (this.getUnlocalizedName().substring(5)) + i);
		}
	}
	
	@SideOnly(Side.CLIENT)
	public void getSubItems(Item item, CreativeTabs tabs, List list) {
		
		for (int i = 0; i < potionNames.length; i++) {
			
			list.add(new ItemStack(item, 1, i));
		}
	}
}
