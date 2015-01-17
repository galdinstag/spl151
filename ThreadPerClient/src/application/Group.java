package application;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by gal on 1/14/2015.
 */
public class Group {

    private String _groupName;
    private ArrayList<User> _usersList;

    public Group(String groupName){
        _groupName = groupName;
        _usersList = null;
    }

    public String getGroupName(){ return _groupName; }

    public void addUsersList(ArrayList<User> users) {
        _usersList = users;
    }

    public boolean containUser(String userName) {
        return _usersList.contains(userName);
    }

    public void addUser(User user) {
        _usersList.add(user);
    }

    public void removeUser(User userToRemove) { //TODO: that shit works?
        _usersList.remove(userToRemove);
    }

    public void addMessage(String content) {
        Iterator<User> it = _usersList.iterator();
        while(it.hasNext()){
            User currUser = it.next();
            currUser.addMessage(content,_groupName);
        }
    }
}
