package application;

import java.util.Iterator;
import java.util.LinkedList;

/**
 * Created by gal on 1/14/2015.
 */
public class User {

    private String _name;
    private String _phoneNumber;
    private LinkedList<InstantMessage> _messages;
    private Iterator _messageQueuePointer;
    private Iterator _messagePointer;

    public User(String name, String number) {
        _name = name;
        _phoneNumber = number;
        _messages = new LinkedList<>();
        _messageQueuePointer = _messages.listIterator();
        _messageQueuePointer = _messages.listIterator();

    }

    public String getName(){ return _name; }

    public void addMessage(String message, String sender){
        _messages.addLast(new InstantMessage(message,sender));
    }

    public String queue(){
        StringBuilder sb = new StringBuilder();
        while(_messageQueuePointer.hasNext()){
            sb.append((_messageQueuePointer.next()).toString());
            sb.append("\n");
        }
        return new String(sb);
    }

    public String getLatestMessages(){
        StringBuilder sb = new StringBuilder();
        while(_messagePointer.hasNext()){
            sb.append((_messagePointer.next()).toString());
            sb.append("\n");
        }
        return new String(sb);
    }

    public String getPhoneNumber(){ return _phoneNumber; }
}