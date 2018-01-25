package ru.buffs.network;

import java.io.IOException;

import com.google.common.base.Throwables;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.PacketBuffer;
import ru.buffs.main.BuffsMain;

public abstract class AbstractMessage<T extends AbstractMessage<T>> implements IMessage, IMessageHandler <T, IMessage> {

	protected abstract void writeData(PacketBuffer buffer) throws IOException;
	
	protected abstract void readData(PacketBuffer buffer) throws IOException;

	public abstract void performProcess(EntityPlayer player, Side side);

	protected boolean isValidOnSide(Side side) {
		
		return true;
	}

	protected boolean requiresMainThread() {
		
		return true;
	}

	@Override
	public void fromBytes(ByteBuf buffer) {
		
		try {
			
			readData(new PacketBuffer(buffer));
		} 
		
		catch (IOException e) {
			
			throw Throwables.propagate(e);
		}
	}

	@Override
	public void toBytes(ByteBuf buffer) {
		
		try {
			
			writeData(new PacketBuffer(buffer));			
		} 
		
		catch (IOException e) {
			
			throw Throwables.propagate(e);
		}
	}
	
	@Override
	public final IMessage onMessage(T msg, MessageContext ctx) {
		
		if (!msg.isValidOnSide(ctx.side)) {
			
			throw new RuntimeException("Invalid side " + ctx.side.name() + " for " + msg.getClass().getSimpleName());
		}
        
        else {
        	
			msg.performProcess(BuffsMain.proxy.getPlayerEntity(ctx), ctx.side);
		}
		
		return null;
    }
	
	public static abstract class AbstractClientMessage<T extends AbstractMessage<T>> extends AbstractMessage<T> {
		
		@Override
		protected final boolean isValidOnSide(Side side) {
			
			return side.isClient();
		}
	}


	public static abstract class AbstractServerMessage<T extends AbstractMessage<T>> extends AbstractMessage<T> {
		
		@Override
		protected final boolean isValidOnSide(Side side) {
			
			return side.isServer();
		}
    }
}
