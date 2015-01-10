package tokenizer;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by gal on 1/9/2015.
 */
public class TokenizerImpl implements Tokenizer {

    private InputStreamReader in;
    private char DELIMITER = '$';

    public TokenizerImpl(){
        in = null;
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
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(sb);
        return sb;
    }
        
    public boolean isAlive() {
        return true;
    }

    public void addInputStream(InputStreamReader inputStreamReader) {
        in = inputStreamReader;
    }
}
