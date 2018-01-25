package ru.buffs.network;

import java.io.IOException;

import cpw.mods.fml.relauncher.Side;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.PacketBuffer;
import ru.buffs.entity.BuffsLivingBase;
import ru.buffs.network.AbstractMessage.AbstractClientMessage;

public class RemoveBuff extends AbstractClientMessage<RemoveBuff> {

	private byte buffId;
	
	public RemoveBuff() {}
	
	public RemoveBuff(int buffId) {
		
		this.buffId = (byte) buffId;
	}
	
	@Override
	protected void writeData(PacketBuffer buffer) throws IOException {
		
		buffer.writeByte(this.buffId);
	}

	@Override
	protected void readData(PacketBuffer buffer) throws IOException {
		
		this.buffId = buffer.readByte();
	}

	@Override
	public void performProcess(EntityPlayer player, Side side) {
		
    	BuffsLivingBase eLivingBase = BuffsLivingBase.get(player); 

		if (eLivingBase.isBuffActive(this.buffId)) {
			
			eLivingBase.removeActiveBuffFromMap(this.buffId);;
		}	
	}
}
