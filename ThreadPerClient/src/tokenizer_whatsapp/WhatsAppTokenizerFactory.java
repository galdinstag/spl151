package tokenizer_whatsapp;

import protocol.ServerProtocolFactory;
import tokenizer.Tokenizer;
import tokenizer.TokenizerFactory;
import tokenizer_http.HttpMessage;

/**
 * Created by airbag on 1/8/15.
 */
public class WhatsAppTokenizerFactory implements TokenizerFactory<HttpMessage>{

    public WhatsAppTokenizerFactory(){}

    public Tokenizer<HttpMessage> create(){
        return new WhatsAppTokenizer();
    }

}
