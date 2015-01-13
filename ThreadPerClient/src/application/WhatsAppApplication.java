package application;

import java.util.Vector;

/**
 * Created by gal on 1/13/2015.
 */
public class WhatsAppApplication {

    private Vector<String> _cookiesContainer;


    public WhatsAppApplication(){
        _cookiesContainer = new Vector<String>();
    }


    public boolean checkCookie(String cookie) {
        return _cookiesContainer.contains(cookie);
    }
}
