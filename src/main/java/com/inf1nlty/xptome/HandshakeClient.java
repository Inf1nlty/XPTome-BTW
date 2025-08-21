package com.inf1nlty.xptome;

import btw.community.xptome.XPTomeAddon;
import net.minecraft.src.Minecraft;
import net.minecraft.src.Packet250CustomPayload;
import net.minecraft.src.GuiDisconnected;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;

/**
 * Handles XPTome MOD presence and version handshake on the client.
 * Ensures matching XPTome MOD installation and version before allowing gameplay.
 */
public class HandshakeClient {
    // Client-side: handshake status
    private static int ticksSinceLogin = 0;
    private static boolean receivedVC = false;
    public static final String VERSION_CHECK_CHANNEL = "XPTome|VC";
    public static final String VERSION_ACK_CHANNEL = "XPTome|VC_Ack";

    /** Returns current XPTome mod version. */
    public static String getXPTomeVersion() {
        return XPTomeAddon.MOD_VERSION;
    }

    /** Called every client tick; checks server handshake. */
    public static void onClientTick() {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.thePlayer == null || mc.isIntegratedServerRunning()) {
            // Not connected
            ticksSinceLogin = 0;
            receivedVC = false;
            return;
        }
        ticksSinceLogin++;
        // Disconnect if handshake failed
        if (ticksSinceLogin > 1 && !receivedVC) {
            mc.thePlayer.sendQueue.getNetManager().closeConnections();
            mc.displayGuiScreen(new GuiDisconnected(null, "disconnect.genericReason", "XPTome mod not installed on server!"));
            mc.theWorld = null;
        }
    }

    /** Handles version check packet on client; responds with ACK and checks version. */
    public static void handleVersionCheckPacketClient(Packet250CustomPayload packet) {
        Minecraft mc = Minecraft.getMinecraft();
        receivedVC = true;
        ticksSinceLogin = 0;
        try {
            DataInputStream dis = new DataInputStream(new ByteArrayInputStream(packet.data));
            String serverVersion = dis.readUTF();
            // Disconnect if version mismatch
            if (!serverVersion.equals(getXPTomeVersion())) {
                mc.displayGuiScreen(new GuiDisconnected(
                        null, "disconnect.genericReason",
                        "XPTome mod version mismatch!\nServer: " + serverVersion + "\nClient: " + getXPTomeVersion()
                ));
                mc.theWorld = null;
                return;
            }
            // Send ACK packet
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            DataOutputStream dos = new DataOutputStream(out);
            dos.writeUTF(getXPTomeVersion());
            dos.close();
            byte[] data = out.toByteArray();

            Packet250CustomPayload ack = new Packet250CustomPayload(VERSION_ACK_CHANNEL, data);
            mc.thePlayer.sendQueue.addToSendQueue(ack);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}