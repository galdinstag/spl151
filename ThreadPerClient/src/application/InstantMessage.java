package application;

/**
 * Created by gal on 1/17/2015.
 */
public class InstantMessage {

    private String _message;
    private String _sender;
    public InstantMessage(String message, String sender){
        _message = message;
        _sender = sender;
    }

    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("From:");
        sb.append(_sender);
        sb.append("\n");
        sb.append("Msg:");
        sb.append(_message);
        sb.append("\n");

        return new String(sb);
    }
}
