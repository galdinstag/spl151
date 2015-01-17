package tokenizer_whatsapp;

import protocol_http.AvailableURIs;
import tokenizer_http.HttpMessage;
import tokenizer_http.HttpRequestType;

import java.util.*;

/**
 * Created by gal on 1/12/2015.
 */
public class WhatsAppMessage{

    private String _uriType;
    private HashMap<String,String> _body;
    private String _cookie;

    public WhatsAppMessage(String uriType, String cookie){
        _uriType = uriType;
        _body = new HashMap<String, String>();
        _cookie = cookie;
    }

    public void addBody(HashMap<String,String> body){
    }


    public String toString() {
        StringBuilder Message = new StringBuilder();
        Message.append(_uriType + "\n");
        for(Map.Entry<String,String> entry : _body.entrySet()){
            Message.append(entry.getKey() + " = ");
            Message.append(entry.getValue());
            Message.append("\n");
        }
        return new String(Message);
    }

    public boolean checkBody() {
        LinkedList<String> bodyMapAsList = new LinkedList<String>(_body.keySet());
        AvailableURIs requestedURI = AvailableURIs.getURI(_uriType);
        LinkedList<String> correctBody = requestedURI.getBodyKeys();

        Collections.sort(bodyMapAsList);
        Collections.sort(correctBody);
        return bodyMapAsList.equals(correctBody);
    }

    public boolean isLogin() { return _uriType.equals("login.jsp"); }

    public String getCookie() { return _cookie; }

    public String getUri(){ return _uriType; }

    public HashMap<String,String> getBody() { return _body; }
}
