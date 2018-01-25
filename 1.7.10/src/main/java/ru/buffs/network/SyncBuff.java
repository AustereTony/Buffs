package ru.buffs.network;

import java.io.IOException;

import cpw.mods.fml.relauncher.Side;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.PacketBuffer;
import ru.buffs.entity.BuffsLivingBase;
import ru.buffs.main.ActiveBuff;
import ru.buffs.network.AbstractMessage.AbstractClientMessage;

public class SyncBuff extends AbstractClientMessage<SyncBuff> {

	private byte buffId, buffTier;
	
	private int buffDuration;
		
	public SyncBuff() {}
	
	public SyncBuff(ActiveBuff buff) {
						    	
    	this.buffId = (byte) buff.getId();
    	this.buffTier = (byte) buff.getTier();
    	
    	this.buffDuration = buff.getDuration();
	}

	@Override
	protected void writeData(PacketBuffer buffer) throws IOException {

		buffer.writeByte(this.buffId);
		buffer.writeByte(this.buffTier);
		
		buffer.writeInt(this.buffDuration);
	}

	@Override
	protected void readData(PacketBuffer buffer) throws IOException {
		
		this.buffId = buffer.readByte();
		this.buffTier = buffer.readByte();
		
		this.buffDuration = buffer.readInt();
	}

	@Override
	public void performProcess(EntityPlayer player, Side side) {
				
    	BuffsLivingBase eLivingBase = BuffsLivingBase.get(player); 

    	eLivingBase.putActiveBuffToMap(new ActiveBuff(this.buffId, this.buffTier, this.buffDuration));;		
	}
}
