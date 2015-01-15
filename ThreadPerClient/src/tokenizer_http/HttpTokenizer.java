package tokenizer_http;

import com.sun.deploy.net.HttpRequest;
import tokenizer.Message;
import tokenizer.Tokenizer;
import tokenizer_http.HttpMessage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;

/**
 * Created by gal on 1/9/2015.
 */
public class HttpTokenizer implements Tokenizer<HttpMessage> {

    private InputStreamReader in;
    private char DELIMITER = '$';
    private boolean _closed;

    public HttpTokenizer(){
        in = null;
        _closed = false;
    }



    public HttpMessage nextMessage() {
        HttpMessage message = null;
        int c;
        StringBuilder rawMessage = new StringBuilder();
        //tokenizing stage
        try {
                while((c = in.read()) != -1){
                    if((char) c == DELIMITER){
                        break;
                    }
                    else{
                        rawMessage.append((char) c);
                    }
                }
        }
        catch (IOException e) {
            _closed = true;
        }
        // we have the string of the test, let's build an http message

        //find type and URI
        String requestedType = rawMessage.substring(0, rawMessage.indexOf(" "));
        rawMessage.delete(0,rawMessage.indexOf(" ")+1);
        String RequestedURI = rawMessage.substring(0,rawMessage.indexOf(" "));
        rawMessage.delete(0, rawMessage.indexOf("\\n") + 2);
        HttpRequestType type;
        if(requestedType.equals("GET")){
            type = HttpRequestType.GET;
        }
        else{
            type = HttpRequestType.POST;
        }
        message = new HttpRequestMessage(type,RequestedURI);
        //set message headers
        HashMap<String,String> messageHeaders = new HashMap<String, String>();
        String key;
        String value;
        while(!rawMessage.substring(0,2).equals("\\n")){
            key = rawMessage.substring(0,rawMessage.indexOf(":"));
            value = rawMessage.substring(rawMessage.indexOf(":")+1,rawMessage.indexOf("\\n"));
            message.addMessageHeader(key, value);
            rawMessage.delete(0,rawMessage.indexOf("\\n") + 2);
        }
        // deleting the last /n
        rawMessage.delete(0,2);
        //set message body
        message.addMessageBody(new String(rawMessage.substring(0,rawMessage.indexOf("\\n"))));

        return message;
    }

    public boolean isAlive() {
        return !_closed;
    }

    public void addInputStream(InputStreamReader inputStreamReader) {
        in = inputStreamReader;
    }
}
