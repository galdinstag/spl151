package tokenizer_whatsapp;

import tokenizer.Tokenizer;
import tokenizer_http.HttpMessage;
import tokenizer_http.HttpRequestMessage;
import tokenizer_http.HttpRequestType;
import tokenizer_http.HttpTokenizer;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;

/**
 * Created by gal on 1/12/2015.
 */
public class WhatsAppTokenizer extends HttpTokenizer{


    public WhatsAppTokenizer(){
        super();
    }

    public WhatsAppMessage nextMessage(HttpRequestMessage msg){
        StringBuilder body = new StringBuilder(msg.getMessageBody());
        String uri = msg.getHttpRequestURI();

        WhatsAppMessage message = new WhatsAppMessage(uri);

        String key = new String();
        String value = new String();

        // parse body
        while(body.length() > 0){
            key = body.substring(0,body.indexOf("="));
            try {
                if(body.indexOf("&") != -1){
                    value = URLDecoder.decode((body.substring(body.indexOf("=")+1,body.indexOf("&"))),"UTF-8");
                    body.delete(0,body.indexOf("&")+1);
                }
                else
                {
                    value = URLDecoder.decode((body.substring(body.indexOf("=")+1,body.length())),"UTF-8");
                    body.delete(0,body.length());
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            message.addToBody(key,value);
        }

        return message;
    }





}
