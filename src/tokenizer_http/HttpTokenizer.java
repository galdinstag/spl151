package tokenizer_http;

import tokenizer.Tokenizer;

import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by gal on 1/10/2015.
 */
public class HttpTokenizer implements Tokenizer {

    private InputStreamReader in;
    private char DELIMITER = '$';
    private boolean _closed;

    private HttpTokenizer(){
        in = null;
        _closed = false;
    }
    @Override
    public Object nextMessage() {
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
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(sb);
        return sb;
    }

    @Override
    public boolean isAlive() {
        return true;
    }

    @Override
    public void addInputStream(InputStreamReader inputStreamReader) {
        in = inputStreamReader;
    }
}
