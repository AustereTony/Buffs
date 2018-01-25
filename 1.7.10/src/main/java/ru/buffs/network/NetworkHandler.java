package ru.buffs.network;

import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.entity.player.EntityPlayerMP;
import ru.buffs.main.BuffsMain;

public class NetworkHandler {

    private static byte packetId = 0;
	 
	private static final SimpleNetworkWrapper dispatcher = NetworkRegistry.INSTANCE.newSimpleChannel(BuffsMain.MODID);
	
	public static final void registerPackets() {
		
		registerMessage(RemoveBuff.class);	
		registerMessage(SyncBuff.class);		
		registerMessage(SyncMaxHealth.class);	
		registerMessage(SetStepHeight.class);
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
}
