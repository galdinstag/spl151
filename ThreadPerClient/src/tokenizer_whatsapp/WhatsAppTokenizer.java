package tokenizer_whatsapp;

import tokenizer.Tokenizer;
import tokenizer_http.*;

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

    public WhatsAppMessage nextMessage(HttpMessage msg){
        WhatsAppMessage message;
        if(msg instanceof HttpPostRequest) {
            message =  new WhatsAppMessage(((HttpPostRequest)msg).getURI(),((HttpPostRequest)msg).getCookie());
        }
        else{
            message =  new WhatsAppMessage(((HttpGetRequest)msg).getURI(),((HttpGetRequest)msg).getCookie());
            //get body
            message.addBody(((HttpPostRequest)msg).getPostBody());
        }

        return message;
    }





}
