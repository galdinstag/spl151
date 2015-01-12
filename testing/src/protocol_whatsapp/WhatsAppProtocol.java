package protocol_whatsapp;

import protocol.ServerProtocol;

/**
 * Created by gal on 1/10/2015.
 */
public class WhatsAppProtocol implements ServerProtocol {
    @Override
    public Object processMessage(Object msg) {
        return null;
    }

    @Override
    public boolean isEnd(Object msg) {
        return false;
    }
}
