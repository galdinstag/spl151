package protocol_http;

import protocol.ServerProtocol;
import protocol.ServerProtocolFactory;
import tokenizer_http.HttpMessage;
import tokenizer_http.HttpTokenizerFactory;

/**
 * Created by airbag on 1/8/15.
 */
public class HttpProtocolFactory implements ServerProtocolFactory<HttpMessage> {

    public HttpProtocolFactory(){

    }
    @Override
    public ServerProtocol<HttpMessage> create() {
        return new HttpProtocol();
    }
}
