package application;

import tokenizer_http.HttpResponseMessage;
import tokenizer_http.HttpStatusCode;
import tokenizer_whatsapp.WhatsAppMessage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

/**
 * Created by gal on 1/13/2015.
 */
public class WhatsAppApplication {

    private ArrayList<String> _cookiesContainer;
    private HashMap<String,>


    public WhatsAppApplication(){
        _cookiesContainer = new ArrayList<String>();
    }


    public boolean checkCookie(String cookie) {
        return _cookiesContainer.contains(cookie);
    }

    public HttpResponseMessage executeURI(WhatsAppMessage msg) {
        HttpResponseMessage response = new HttpResponseMessage(HttpStatusCode.S200);
        switch (msg.getUri()){
            case "login.jsp":
                response.addMessageBody(login(msg));
                break;
            case "logout.jsp":
                response.addMessageBody(logout(msg));
                break;
            case "list.jsp":
                response.addMessageBody(list(msg));
                break;
            case "create_group.jsp":
                response.addMessageBody(create_group(msg));
                break;
            case "send.jsp":
                response.addMessageBody(send(msg));
                break;
            case "add_user.jsp":
                response.addMessageBody(add_user(msg));
                break;
            case "remove_user.jsp":
                response.addMessageBody(remove_user(msg));
                break;
            case "queue.jsp":
                response.addMessageBody(queue(msg));
                break;
        }
        return response;
    }

    private String queue(WhatsAppMessage msg) {
        return null;
    }

    private String remove_user(WhatsAppMessage msg) {
        return null;
    }

    private String add_user(WhatsAppMessage msg) {
        return null;
    }

    private String send(WhatsAppMessage msg) {
        return null;
    }

    private String list(WhatsAppMessage msg) {
        return null;
    }

    private String create_group(WhatsAppMessage msg) {
        return null;
    }

    private String logout(WhatsAppMessage msg) {
        return null;
    }

    private String login(WhatsAppMessage msg) {
        return null;
    }

}
