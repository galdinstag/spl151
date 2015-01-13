package protocol_http;

import tokenizer_http.HttpRequestType;

import java.util.Arrays;
import java.util.LinkedList;

/**
 * Created by airbag on 1/12/15.
 */
public enum AvailableURIs {
    LOGIN (1, "login.jsp", HttpRequestType.POST,new LinkedList<String>(Arrays.asList("UserName","Phone"))),
    LOGOUT (2, "logout.jsp", HttpRequestType.GET,new LinkedList<String>()),
    LIST (3, "list.jsp" , HttpRequestType.POST,new LinkedList<String>(Arrays.asList("List"))),
    CREATE_GROUP (4, "create_group.jsp", HttpRequestType.POST,new LinkedList<String>(Arrays.asList("GroupName","Users"))),
    SEND (5, "send.jsp", HttpRequestType.POST,new LinkedList<String>(Arrays.asList("Type","Target","Content"))),
    ADD_USER (6, "add_user.jsp", HttpRequestType.POST,new LinkedList<String>(Arrays.asList("Target","User"))),
    REMOVE_USER (7, "remove_user.jsp", HttpRequestType.POST,new LinkedList<String>(Arrays.asList("Target","User"))),
    QUEUE (8, "queue.jsp", HttpRequestType.GET,new LinkedList<String>()),
    NA (-1, "N/A", HttpRequestType.GET,new LinkedList<String>());

    private final int _code;
    private final String _URI;
    private final HttpRequestType _type;
    private final LinkedList<String> _bodyKeys;

    AvailableURIs(int code, String URI, HttpRequestType type, LinkedList<String> bodyKeys) {
        _code = code;
        _URI = URI;
        _type = type;
        _bodyKeys = bodyKeys;
    }

    public String getURI(){ return _URI; }

    public HttpRequestType getType(){ return _type; }

    public LinkedList getBodyKeys(){ return _bodyKeys; }

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
