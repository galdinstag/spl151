package protocol_whatsapp;

import protocol.ServerProtocol;
import protocol.ServerProtocolFactory;

/**
 * Created by airbag on 1/8/15.
 */
public class WhatsAppProtocolFactory implements ServerProtocolFactory{

    public WhatsAppProtocolFactory() {}
    @Override
    public ServerProtocol create() {
        return new WhatsAppProtocol();
    }
}
