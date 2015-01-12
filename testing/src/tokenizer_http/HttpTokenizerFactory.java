package tokenizer_http;

import tokenizer.Tokenizer;
import tokenizer.TokenizerFactory;

/**
 * Created by airbag on 1/8/15.
 */
public class HttpTokenizerFactory implements TokenizerFactory<HttpMessage> {

    @Override
    public Tokenizer<HttpMessage> create() {
        return new HttpTokenizer();
    }
}
