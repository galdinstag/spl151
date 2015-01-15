package protocol_whatsapp;

import protocol.ServerProtocol;
import protocol.ServerProtocolFactory;
import tokenizer_http.HttpMessage;

/**
 * Created by airbag on 1/8/15.
 */
public class WhatsAppProtocolFactory implements ServerProtocolFactory<HttpMessage>{

    public WhatsAppProtocolFactory() {}
    @Override
    public ServerProtocol<HttpMessage> create() {
        return new WhatsAppProtocol();
    }
}
