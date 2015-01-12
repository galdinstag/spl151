package protocol_http;

import tokenizer_http.HttpRequestType;

/**
 * Created by airbag on 1/12/15.
 */
public enum AvailableURIs {
    LOGIN (1, "login.jsp", HttpRequestType.POST),
    LOGOUT (2, "logout.jsp", HttpRequestType.GET),
    LIST (3, "list.jsp" , HttpRequestType.POST),
    CREATE_GROUP (4, "create_group.jsp", HttpRequestType.POST),
    SEND (5, "send.jsp", HttpRequestType.POST),
    ADD_USER (6, "add_user.jsp", HttpRequestType.POST),
    REMOVE_USER (7, "remove_user.jsp", HttpRequestType.POST),
    QUEUE (8, "queue.jsp", HttpRequestType.GET),
    NA (-1, "N/A", HttpRequestType.GET);

    private final int _code;
    private final String _URI;
    private final HttpRequestType _type;

    AvailableURIs(int code, String URI, HttpRequestType type) {
        _code = code;
        _URI = URI;
        _type = type;
    }

    public String getURI(){ return _URI; }

    public HttpRequestType getType(){ return _type; }

    /**
     *
     * @param URI String representing request URI
     * @return requested URI if URI is available, "N/A" otherwise.
     */
    public static AvailableURIs getURI (String URI) {
        for(AvailableURIs u : AvailableURIs.values())
            if (u._URI.equals(URI))
                return u;
        return NA;
    }
}
