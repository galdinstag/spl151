package tokenizer_http;

//import com.sun.deploy.net.HttpRequest;
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
    private String HEADER_DELIMITER = ":";
    private String LINE_DELIMITER = "\\n";
    private String SPACE = " ";
    private int START_OF_STRING = 0;
    private int LENGTH_OF_LINE_DELIMITER = 2;
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
        String requestedType = rawMessage.substring(START_OF_STRING, rawMessage.indexOf(SPACE));
        rawMessage.delete(START_OF_STRING,rawMessage.indexOf(SPACE)+1);
        String RequestedURI = rawMessage.substring(START_OF_STRING,rawMessage.indexOf(SPACE));
        rawMessage.delete(START_OF_STRING, rawMessage.indexOf(LINE_DELIMITER) + LENGTH_OF_LINE_DELIMITER);
        if(requestedType.equals("GET")){
            message = new HttpGetRequest(RequestedURI);
        }
        else{
            message = new HttpPostRequest(RequestedURI);
        }
        //set message headers
        String key;
        String value;
        while(!rawMessage.substring(START_OF_STRING,LENGTH_OF_LINE_DELIMITER).equals(LINE_DELIMITER)){
            key = rawMessage.substring(START_OF_STRING,rawMessage.indexOf(HEADER_DELIMITER));
            value = rawMessage.substring(rawMessage.indexOf(HEADER_DELIMITER)+1,rawMessage.indexOf(LINE_DELIMITER));
            message.addMessageHeader(key, value);
            rawMessage.delete(START_OF_STRING,rawMessage.indexOf(LINE_DELIMITER) + LENGTH_OF_LINE_DELIMITER);
        }
        // deleting the last /n
        rawMessage.delete(START_OF_STRING,LENGTH_OF_LINE_DELIMITER);
        //set message body
        if(message instanceof HttpPostRequest){
            ((HttpPostRequest) message).addBody(new String(rawMessage.substring(START_OF_STRING,rawMessage.indexOf(LINE_DELIMITER))));
        }

        return message;
    }

    public boolean isAlive() {
        return !_closed;
    }

    public void addInputStream(InputStreamReader inputStreamReader) {
        in = inputStreamReader;
    }
}
