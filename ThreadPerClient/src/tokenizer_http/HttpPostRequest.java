package tokenizer_http;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * HttpPostRequest
 */
public class HttpPostRequest extends HttpMessage {

    private HashMap<String, String> _postBody;
    private String _rawBody;
    private final String _httpRequestURI;

    public HttpPostRequest(String requestURI) {
        _httpRequestURI = requestURI;

    }

    public void addBody (String body) {
        _rawBody = body;
        parseBody(body);

    }
    public HashMap<String, String> getPostBody() {
        return _postBody;
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

    public String getURI() {
        return _httpRequestURI;
    }

    private void parseBody(String postBody) {
        _postBody = new HashMap<String, String>();

        String[] bodyParts = postBody.split("&");

        for (String b : bodyParts) {
            int delimiterIndex = b.indexOf("=");

            String name = b.substring(0, delimiterIndex);
            String value = b.substring(delimiterIndex + 1);

            try {
                value = URLEncoder.encode(value, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            _postBody.put(name, value);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("POST");
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
        sb.append(_rawBody);
        sb.append("\n");

        return sb.toString();
    }
}
