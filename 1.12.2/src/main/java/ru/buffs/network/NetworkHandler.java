package ru.buffs.network;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import ru.buffs.main.BuffsMain;

public class NetworkHandler {

    private static byte packetId = 0;
	 
	private static final SimpleNetworkWrapper dispatcher = NetworkRegistry.INSTANCE.newSimpleChannel(BuffsMain.MODID);
	
	public static final void registerPackets() {
		
		registerMessage(RemoveBuff.class);
		registerMessage(BuffsSyncRequest.class);
		registerMessage(SyncBuff.class);
	}
	
	private static final <T extends AbstractMessage<T> & IMessageHandler<T, IMessage>> void registerMessage(Class<T> clazz) {

		if (AbstractMessage.AbstractClientMessage.class.isAssignableFrom(clazz)) {
			
			NetworkHandler.dispatcher.registerMessage(clazz, clazz, packetId++, Side.CLIENT);
		}
		
		else if (AbstractMessage.AbstractServerMessage.class.isAssignableFrom(clazz)) {
			
			NetworkHandler.dispatcher.registerMessage(clazz, clazz, packetId++, Side.SERVER);
		} 
		
		else {

			NetworkHandler.dispatcher.registerMessage(clazz, clazz, packetId, Side.CLIENT);
			NetworkHandler.dispatcher.registerMessage(clazz, clazz, packetId++, Side.SERVER);
		}
    }
    
    public static final void sendTo(IMessage message, EntityPlayerMP player) {
    	
    	 NetworkHandler.dispatcher.sendTo(message, player);
    }
    
    public static final void sendToServer(IMessage message) {
     	  
        NetworkHandler.dispatcher.sendToServer(message);
    }
}
