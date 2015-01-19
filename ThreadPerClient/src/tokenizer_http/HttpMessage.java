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
    protected String _httpRequestURI;


    public HttpMessage() {
        this._headers = new HashMap<String, String>();
        _httpRequestURI = null;
    }

    /**
     * add a new header to HttpMessage
     * @param name of header
     * @param value of header
     */
    public void addMessageHeader(String name, String value) {
        _headers.put(name, value);
    }

    public String getURI() { return _httpRequestURI; }

    public abstract String toString();
}
