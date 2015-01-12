package tokenizer_http;

import java.util.Map;

/**
 * this class represents an HTTP request object. Inherits from HttpMessage.
 */
public class HttpRequestMessage extends HttpMessage {

    private final HttpRequestType _httpRequestType;
    private final String _httpRequestURI;

    public HttpRequestMessage(HttpRequestType httpRequestType, String httpRequestUri) {
        _httpRequestType = httpRequestType;
        _httpRequestURI = httpRequestUri;
    }

    public String getHttpRequestURI() {
        return _httpRequestURI;
    }

    public HttpRequestType getHttpRequestType() {
        return _httpRequestType;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append(_httpRequestType.toString());
        sb.append(" ");
        sb.append(_httpRequestURI);
        sb.append(" ");
        sb.append(HTTP_VERSION);
        sb.append("\n");

        for (Map.Entry<String, String> header: _headers.entrySet()) {
            sb.append(header.getKey());
            sb.append(": ");
            sb.append(header.getValue());
            sb.append("\n");
        }

        sb.append("\n");
        sb.append(_body);
        sb.append("\n");
        sb.append(DELIMITER);

        return sb.toString();
    }
}
