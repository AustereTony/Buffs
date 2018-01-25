package ru.buffs.network;

import java.io.IOException;

import cpw.mods.fml.relauncher.Side;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.PacketBuffer;
import ru.buffs.network.AbstractMessage.AbstractClientMessage;
import ru.buffs.player.BuffsPlayer;

public class SyncMaxHealth extends AbstractClientMessage<SyncMaxHealth> {
	
	private float maxHealth;
	
	public SyncMaxHealth() {}
	
	public SyncMaxHealth(float maxHealth) {
		
		this.maxHealth = maxHealth;
	}
	
	@Override
	protected void writeData(PacketBuffer buffer) throws IOException {
		
		buffer.writeFloat(this.maxHealth);
	}

	@Override
	protected void readData(PacketBuffer buffer) throws IOException {
		
		this.maxHealth = buffer.readFloat();
	}

	@Override
	public void performProcess(EntityPlayer player, Side side) {
		
		BuffsPlayer ePlayer = BuffsPlayer.get(player);
		
		ePlayer.setMaxHealth(this.maxHealth);
		
		ePlayer.setPlayerHealth(player, ePlayer.getMaxHealth(), ePlayer.getMaxHealth());	
	}
}
