package tokenizer_http;

import tokenizer.Message;

import java.util.HashMap;
import java.util.Map;

/**
 * HttpMessage abstract class. both HttpResponseMessage & HttpRequestMessage inherit from this class.
 */
public abstract class HttpMessage implements Message<HttpMessage> {

    public static final String DELIMITER = "$";
    protected static final String HTTP_VERSION = "HTTP/1.1";
    protected final Map<String, String> _headers;
    protected String _body; // object?

    public HttpMessage() {
        this._headers = new HashMap<String, String>();
        this._body = null;
    }

    /**
     * add a new header to HttpMessage
     * @param name of header
     * @param value of header
     */
    public void addMessageHeader(String name, String value) {
        _headers.put(name, value);
    }

    /**
     * adds body to HttpMessage.
     * @param body
     */
    public void addMessageBody(String body) {
        _body = body;
    }

    public String getMessageBody() {
        return _body;
    }
    public abstract String toString();
}
