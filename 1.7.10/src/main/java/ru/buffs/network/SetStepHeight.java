package ru.buffs.network;

import java.io.IOException;

import cpw.mods.fml.relauncher.Side;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.PacketBuffer;
import ru.buffs.network.AbstractMessage.AbstractClientMessage;

public class SetStepHeight extends AbstractClientMessage<SetStepHeight> {
	
	private boolean increasedStep;
	
	public SetStepHeight() {}
	
	public SetStepHeight(boolean increasedStep) {
		
		this.increasedStep = increasedStep;
	}
	
	@Override
	protected void writeData(PacketBuffer buffer) throws IOException {
		
		buffer.writeBoolean(this.increasedStep);
	}

	@Override
	protected void readData(PacketBuffer buffer) throws IOException {
		
		this.increasedStep = buffer.readBoolean();
	}

	@Override
	public void performProcess(EntityPlayer player, Side side) {
		
		player.stepHeight = this.increasedStep ? 1.0F : 0.5F;
	}
}
