package tokenizer_whatsapp;

import tokenizer.Tokenizer;
import tokenizer.TokenizerFactory;
import tokenizer_http.HttpMessage;

/**
 * Created by airbag on 1/8/15.
 */
public class WhatsAppTokenizerFactory{

    public WhatsAppTokenizerFactory(){}

    public WhatsAppTokenizer create(){
        return new WhatsAppTokenizer();
    }

}
