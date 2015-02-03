package ThreadPerClient.application;

import java.util.Iterator;
import java.util.LinkedList;

/**
 * Created by gal on 1/14/2015.
 */
public class User {

    private final String _name;
    private final String _phoneNumber;
    private LinkedList<InstantMessage> _messages;
    private Iterator _messageQueuePointer;
    private int _messageQueueIndex;


    public User(String name, String number) {
        _name = name;
        _phoneNumber = number;
        _messages = new LinkedList<>();
        _messageQueueIndex = 0;
        _messageQueuePointer = null;
    }

    public String getName(){ return _name; }

    public void addMessage(String message, String sender){
        synchronized (_messages) {
            _messages.addLast(new InstantMessage(message, sender));
        }
    }

    public String queue(){
        StringBuilder sb = new StringBuilder();
        synchronized (_messages) {
            _messageQueuePointer = _messages.listIterator(_messageQueueIndex);
            while (_messageQueuePointer.hasNext()) {
                InstantMessage currMessage = (InstantMessage) _messageQueuePointer.next();
                sb.append(currMessage.toString());
                sb.append("\n");
            }
            _messageQueueIndex = _messages.size();
        }
        //trim the last "/n" before sending
        if(sb.length() > 0){
            sb = new StringBuilder(sb.substring(0,sb.length()-2));
        }

        return new String(sb);
    }
    public String getPhoneNumber(){ return _phoneNumber; }
}