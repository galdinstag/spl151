package tokenizer_http;
import tokenizer.Tokenizer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by gal on 1/9/2015.
 */
public class HttpTokenizer implements Tokenizer {

    private InputStreamReader in;
    private char DELIMITER = '$';
    private boolean _closed;

    public HttpTokenizer(){
        in = null;
        _closed = false;
    }



    public StringBuilder nextMessage() {
        int c;
        StringBuilder sb = new StringBuilder();
        try {
                in.read();
                while((c = in.read()) != -1){
                    if((char) c == DELIMITER){
                        break;
                    }
                    else{
                        sb.append((char) c);
                    }
                }
        }
        catch (IOException e) {
            _closed = true;
        }
        System.out.println(sb);
        return sb;
    }
        
    public boolean isAlive() {
        return !_closed;
    }

    public void addInputStream(InputStreamReader inputStreamReader) {
        in = inputStreamReader;
    }
}
