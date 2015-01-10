package tokenizer_http;

import tokenizer.Tokenizer;
import tokenizer.TokenizerFactory;

/**
 * Created by airbag on 1/8/15.
 */
public class HttpTokenizerFactory implements TokenizerFactory {

    public HttpTokenizerFactory(){

    }
    @Override
    public Tokenizer create() {
        HttpTokenizer httpTokenizer = new HttpTokenizer();
        return httpTokenizer;
    }
}
