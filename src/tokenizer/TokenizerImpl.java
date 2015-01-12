package tokenizer;
import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Created by gal on 1/9/2015.
 */
public class TokenizerImpl implements Tokenizer {

    private BufferedReader in;

    public TokenizerImpl(){
        in = null;
    }



    public StringBuilder nextMessage() {
        return null;
    }
        
    public boolean isAlive() {
        return false;
    }

    public void addInputStream(InputStreamReader inputStreamReader) {
        in = new BufferedReader(inputStreamReader);
    }
}
