package btw.community.xptome;

import btw.BTWAddon;
import btw.AddonHandler;
import btw.crafting.recipe.RecipeManager;
import com.inf1nlty.xptome.HandshakeClient;
import com.inf1nlty.xptome.HandshakeServer;
import net.minecraft.src.EntityPlayerMP;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import com.inf1nlty.xptome.ExpCommand;
import com.inf1nlty.xptome.XPTomeItems;
import net.minecraft.src.NetServerHandler;

public class XPTomeAddon extends BTWAddon {

    public static String MOD_VERSION;

    @Override
    public void initialize() {
        AddonHandler.logMessage(getName() + " v" + getVersionString() + " Initializing...");
        XPTomeItems.registerItems();
        AddonHandler.registerCommand(new ExpCommand(), false);

        this.registerPacketHandler(HandshakeServer.VERSION_ACK_CHANNEL, (packet, player) -> {
            if (!(player instanceof EntityPlayerMP mp)) return;
            HandshakeServer.handleVersionAckPacket(mp.playerNetServerHandler, packet);
        });
        this.registerPacketHandler(HandshakeClient.VERSION_CHECK_CHANNEL, (packet, player) -> HandshakeClient.handleVersionCheckPacketClient(packet));

        RecipeManager.addShapelessRecipe(
                new ItemStack(Item.enchantedBook, 2),
                new Object[]{
                        Item.enchantedBook,
                        Item.writableBook
                }
        );
    }

    @Override
    public void serverPlayerConnectionInitialized(NetServerHandler serverHandler, EntityPlayerMP playerMP) {
        HandshakeServer.onPlayerJoin(serverHandler, playerMP);
    }
}