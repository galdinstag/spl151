package protocol_http;

import protocol.ServerProtocol;
import protocol_whatsapp.WhatsAppProtocol;
import protocol_whatsapp.WhatsAppProtocolFactory;
import tokenizer.Message;
import tokenizer_http.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by gal on 1/10/2015.
 */
public class HttpProtocol implements ServerProtocol<HttpMessage> {
    HttpMessage msg;

    public HttpProtocol(){
        msg = null;
    }
    @Override
    public HttpMessage processMessage(HttpMessage msg) {
        this.msg = msg;
        if (!massageValid()) {
            msg = new HttpResponseMessage(HttpStatusCode.S404);
        }
        return msg;
    }

    private boolean massageValid(){
        boolean isValid = true;
        AvailableURIs uri = AvailableURIs.getURI(((HttpRequestMessage) msg).getHttpRequestURI());
        if(uri.getURI().equals("NA")){
            isValid = false;
        }
        if(((HttpRequestMessage) msg).getHttpRequestType() == HttpRequestType.POST){
           if(uri.getType() == HttpRequestType.GET){
               isValid = false;
           }
        }
        else if(uri.getType() == HttpRequestType.POST){
            isValid = false;
        }
        return isValid;
    }

    @Override
    public boolean isEnd(HttpMessage msg) {
        return false;
    }
}