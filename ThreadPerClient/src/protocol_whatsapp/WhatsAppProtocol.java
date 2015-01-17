package protocol_whatsapp;

import application.WhatsAppApplication;
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
    private WhatsAppApplication _app;

    public WhatsAppProtocol(){
        super();
        _msg = null;

    }

    public HttpResponseMessage processMessage(WhatsAppMessage msg){
        _msg = msg;
        HttpResponseMessage response;
        //checking that the message is whatsApp valid
        if(!msg.checkBody()){
            response = new HttpResponseMessage(HttpStatusCode.S200);
            response.addMessageBody(new String("ERROR: INCORRECT BODY SENT"));
        }
        else if(!checkCookie()){
            response = new HttpResponseMessage(HttpStatusCode.S403);
        }
        else{
            WhatsAppMessage whatsAppResponse =  _app.executeURI(_msg);
            response = new HttpResponseMessage(HttpStatusCode.S200);
            // if it's a login, add a cookie to the response
            if(_msg.getUri().equals("login.jsp")){
                response.addMessageHeader("Set-Cookie",new String("user_auth=" + _app.getACookie(_msg.getUserName())));
            }
            response.addMessageBody(whatsAppResponse.getResponseBody());
        }

        return response;
    }

    public void setWhatsAppApplication(WhatsAppApplication app){ _app = app; }

    private boolean checkCookie() {
        boolean isValid = false;
        String cookie = _msg.getCookie();
        if(_msg.isLogin()){
            isValid = true;
        }
        else if(_app.checkCookie(cookie.substring(cookie.indexOf("=")+1,cookie.length()))){
            isValid = true;
        }
            return isValid;
    }

    public void addApp(WhatsAppApplication app) {
        _app = app;
    }
}
