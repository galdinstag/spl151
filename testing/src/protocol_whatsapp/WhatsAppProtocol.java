package protocol_whatsapp;

import protocol.ServerProtocol;
import protocol_http.HttpProtocol;
import tokenizer_http.HttpResponseMessage;
import tokenizer_http.HttpStatusCode;
import tokenizer_whatsapp.WhatsAppMessage;

/**
 * Created by gal on 1/10/2015.
 */
public class WhatsAppProtocol extends HttpProtocol {

    private WhatsAppMessage _msg;

    public WhatsAppProtocol(){
        super();
    }

    public HttpResponseMessage processMessage(WhatsAppMessage msg){
        _msg = msg;
        HttpResponseMessage response;
        //checking that the message is whatsApp valid
        if(!msg.checkBody()){
            response = new HttpResponseMessage(HttpStatusCode.S200);
            response.addMessageBody(new String("ERROR: INCORRECT BODY SENT"));
        }
        else if(!_msg.checkCookie()){
            response = new HttpResponseMessage(HttpStatusCode.S403);
        }
        else{
            response = new HttpResponseMessage(HttpStatusCode.S200);
        }

        return response;
    }
}
