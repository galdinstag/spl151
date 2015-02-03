package tokenizer_http;

import java.util.Map;

/**
 * Created by airbag on 1/15/15.
 */
public class HttpGetRequest extends HttpMessage {


    public HttpGetRequest(String requestURI) {
        _httpRequestURI = requestURI;
    }

    public String getURI() {
        return _httpRequestURI;
    }

    public String getCookie() {
        String cookie;
        if(_headers.containsKey("Cookie")){
            cookie = _headers.get("Cookie");
        }
        else{
            cookie = new String("null");
        }
        return cookie;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("GET");
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
        sb.append("\n");

        return sb.toString();
    }
}
