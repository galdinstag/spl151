package protocol_whatsapp;

import protocol.ServerProtocol;

/**
 * Created by gal on 1/10/2015.
 */
public class WhatsAppProtocol implements ServerProtocol {

    private StringBuilder _msg;

    public WhatsAppProtocol(){

    }

    @Override
    public Object processMessage(Object msg) {
        _msg = (StringBuilder) msg;
        String RequestedURI

        return null;
    }

    @Override
    public boolean isEnd(Object msg) {
        return false;
    }
}
