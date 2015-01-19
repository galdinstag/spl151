package application;

import tokenizer_http.HttpResponseMessage;
import tokenizer_http.HttpStatusCode;
import tokenizer_whatsapp.WhatsAppMessage;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by gal on 1/13/2015.
 */
public class WhatsAppApplication {

    private ConcurrentHashMap<String,String> _cookiesContainer;
    private ConcurrentHashMap<String,User> _usersContainer;
    private ConcurrentHashMap<String,Group> _groupContainer;
    private AtomicInteger _cookieCounter;
    private Object loginDummy;


    public WhatsAppApplication(){
        _cookiesContainer = new ConcurrentHashMap<>();
        _usersContainer = new ConcurrentHashMap<>();
        _groupContainer = new ConcurrentHashMap<>();
        _cookieCounter = new AtomicInteger(1);
        loginDummy = new Object();
    }

    /**
     *
     * @param cookie - the cookie the user provided in the request
     * @return : weather the cookie exists in the Map which means the user's request should be authorized or not.
     */
    public boolean checkCookie(String cookie) {
        return _cookiesContainer.containsKey(cookie);
    }

    /**
     *
     * @param msg - a WhatsApp message containing the URI to execute and the parameters needed by the URI
     * @return - a WhatsApp message consists of the body of the HttpResponse that should be send to the user
     */
    public WhatsAppMessage executeURI(WhatsAppMessage msg) {
        WhatsAppMessage response = new WhatsAppMessage();
        String RequestedURI = msg.getUri();

            if(RequestedURI.equals("login.jsp")) {
            response.addBody(login(msg));
            }
            if(RequestedURI.equals("logout.jsp")) {
                response.addBody(logout(msg));
            }
            if(RequestedURI.equals("list.jsp")){
                response.addBody(list(msg));
            }
            if(RequestedURI.equals("create_group.jsp")) {
                response.addBody(create_group(msg));
            }
            if(RequestedURI.equals("send.jsp")) {
                response.addBody(send(msg));
            }
            if(RequestedURI.equals("add_user.jsp")) {
                response.addBody(add_user(msg));
            }
            if(RequestedURI.equals("remove_user.jsp")) {
                response.addBody(remove_user(msg));
            }
            if(RequestedURI.equals("queue.jsp")) {
                response.addBody(queue(msg));
            }
        return response;
    }

    /**
     * Get all the messages sent to the user by a group or a user since the last "queue" request
     * @param msg - a WhatsApp message containing the needed arguments for the function to fulfill
     * @return - a String consists of all messages since last "queue" request
     */
    private String queue(WhatsAppMessage msg) {
        User queryingUser = getUserByCookie(msg.getCookie());
        return queryingUser.queue();
    }

    /**
     * Removes a given user from a target group, if all parameters are correct
     * @param msg - a WhatsApp message containing the needed arguments for the function to fulfill
     * @return - a String consists of the success or failure (and if so then why) of the action
     */
    private String remove_user(WhatsAppMessage msg) {
        StringBuilder response = new StringBuilder();
        //get the needed arguments
        String targetGroupName = msg.getAttribute("Target");
        String userPhone = msg.getAttribute("User");


        User queryingUser = getUserByCookie(msg.getCookie());
        User userToRemove = getUserByPhoneNumber(userPhone);

        if(!_groupContainer.containsKey(targetGroupName)){
            response.append("ERROR 770: Target Does not Exist");
        }
        //check if userToRemove exists in the target group
        else if(!(_groupContainer.get(targetGroupName)).containUser(userToRemove.getName())){
            response.append("ERROR 336: Cannot remove, missing parameters");
        }
        //check if the querying user is authorized to delete someone
        else if(!(_groupContainer.get(targetGroupName)).containUser(queryingUser.getName())) {
            response.append("ERROR 668: Permission denied");
        }
        //all is good in the hood
        else {
            _groupContainer.get(targetGroupName).removeUser(userToRemove);
            response.append(userPhone);
            response.append(" removed from ");
            response.append(targetGroupName);
        }

        return new String(response);
    }

    /**
     * Add a given user from a target group, if all parameters are correct.
     * @param msg - a WhatsApp message containing the needed arguments for the function to .
     * @return - a String consists of the success or failure (and if so then why) of the action.
     */
    private String add_user(WhatsAppMessage msg){
        StringBuilder response = new StringBuilder();
        //get the needed attributes
        String targetGroupName = msg.getAttribute("Target");
        String userPhone = msg.getAttribute("User");


        User queryingUser = getUserByCookie(msg.getCookie());
        User userToAdd = getUserByPhoneNumber(userPhone);


        if(!_groupContainer.containsKey(targetGroupName)){
            response.append("ERROR 770: Target Does not Exist");
        }
        //check if userToAdd exists in the program
        else if(userToAdd == null){
            response.append("ERROR 242: Cannot add user, missing parameters");
        }
        else if((_groupContainer.get(targetGroupName)).containUser(userToAdd.getName())){
            response.append("ERROR 142: Cannot add user, user already in group");
        }
        //check if the querying user is authorized to add someone
        else if(!(_groupContainer.get(targetGroupName)).containUser(queryingUser.getName())){
            response.append("ERROR 669: Permission denied");
        }
        //all is good in the hood
        else{
            _groupContainer.get(targetGroupName).addUser(userToAdd);
            response.append(userPhone);
            response.append(" added to ");
            response.append(targetGroupName);
        }
        return new String(response);
    }


    /**
     * Sends a message to a user or a group
     * @param msg - a WhatsApp message containing the needed arguments for the function to fulfill.
     * @return - a String consists of the success or failure (and if so then why) of the action.
     */
    private String send(WhatsAppMessage msg) {
        StringBuilder response = new StringBuilder();
        //get attributes
        String type = msg.getAttribute("Type");
        String target = null;
        if(type.equals("Direct")){
            target =  getUserByPhoneNumber(msg.getAttribute("Target")).getName();
        }
        else if(type.equals("Group")){
            target = msg.getAttribute("Target");
        }
        else {
            response.append("ERROR 836: Invalid Type");
        }

        String content = msg.getAttribute("Content");
        User sender = getUserByCookie(msg.getCookie());

        if(! (_usersContainer.containsKey(target) || _groupContainer.containsKey(target))){
            response.append("ERROR 771: Target Does not Exist");
        }
        //it's good, it's good, it's gooooooooood...
        else{
            if(type.equals("Direct")){
                _usersContainer.get(target).addMessage(content,sender.getPhoneNumber());
            }
            else{
                Group targetGroup = _groupContainer.get(target);
                targetGroup.addMessage(content);
            }
        }
        return new String(response);
    }

    /**
     * Returns a list of all users/groups/users in a specific group in the program (as requested)
     * @param msg - a WhatsApp message containing the needed arguments for the function to fulfill.
     * @return - if succeed- a list as requested, if failed- why?.
     */
    private String list(WhatsAppMessage msg) {
        String listType = msg.getAttribute("List");
        StringBuilder response = new StringBuilder();
        if(listType.equals("Users")){
            synchronized (_usersContainer) {
                for (Map.Entry<String, User> entry : _usersContainer.entrySet()) {
                    response.append(entry.getValue().getPhoneNumber());
                    response.append(",");
                }
                //trim the last ","
                if(response.length() > 0){
                    response.deleteCharAt(response.length()-1);
                }
            }
        }
        else if(listType.equals("Groups")){
            synchronized (_groupContainer) {
                for (Map.Entry<String, Group> entry : _groupContainer.entrySet()) {
                    response.append(entry.getKey());
                    response.append(":");
                    response.append(entry.getValue().getUsersPhone());
                    response.append("\n");
                }
            }
        }
        else if(!_groupContainer.containsKey(listType)){
            response.append("ERROR 273: Missing Parameters");
        }
        else{
            response.append(_groupContainer.get(listType).getUsersInformation());
        }
        return new String(response);
    }

    /**
     * Creates a new group
     * @param msg - a WhatsApp message containing the needed arguments for the function to fulfill.
     * @return - a String consists of the success or failure (and if so then why) of the action.
     */
    private String create_group(WhatsAppMessage msg) {
        StringBuilder response = new StringBuilder();
        //get attributes
        String groupName = msg.getAttribute("GroupName");
        String groupUsers = msg.getAttribute("Users");
        ArrayList<String> usersList = usersList(new StringBuilder(groupUsers));
        String unknownUser = usersCheck(usersList);

        //check if GroupName is empty
        if(groupName == ""){
            response.append("ERROR 675: Cannot create group, missing parameters\n");
        }
        else if(_groupContainer.containsKey(groupName)){
            response.append("ERROR 511: Group Name Already Taken");
        }
        //check if there is an unknown user in the Users given
        else if(unknownUser.length() > 0){
            response.append("ERROR 929: Unknown User ");
            response.append(unknownUser);
        }
        //it's all good in the hood
        else{
            _groupContainer.put(groupName,new Group(groupName));
            ArrayList<User> users = new ArrayList<>();
            for(String currUser : usersList){
                users.add(_usersContainer.get(currUser));
            }
            _groupContainer.get(groupName).addUsersList(users);
            response.append("Group ");
            response.append(groupName);
            response.append(" Created");
        }

        return new String(response);
    }

    /**
     * Logout the user (removes his cookie from the container)
     * @param msg - a WhatsApp message containing the needed arguments for the function to fulfill.
     * @return - "GoodBye.
     */
    private String logout(WhatsAppMessage msg) {
        _cookiesContainer.remove(msg.getCookie());
        return new String("GoodBye");
    }

    /**
     * Login the user (add the user if he doesn't exists)
     * @param msg - a WhatsApp message containing the needed arguments for the function to fulfill.
     * @return - if succeed - a welcome message, if failed - why?
     */
    private String login(WhatsAppMessage msg) {
        StringBuilder responseMessage = new StringBuilder();
        //get attributes
        String userName = msg.getAttribute("UserName");
        String phone = msg.getAttribute("Phone");

        if(userName.equals("") || phone.equals("")){
            responseMessage.append("ERROR 273: Missing Parameters");
        }
        //it's good, it's good, it's gooooooood
        else {
            if (!_usersContainer.containsKey(userName)) {
                _usersContainer.put(userName, new User(userName, phone));
            }
            responseMessage.append("Welcome ");
            responseMessage.append(userName);
            responseMessage.append("@");
            responseMessage.append(phone);
        }
        return new String(responseMessage);
    }

    /**
     * Returns a new Cookie for the user's authentication
     * @param userName - the user that logins
     * @return - a new unique Cookie
     */
    public String getACookie(String userName){
        String Cookie;
        // synchronize so we won't give the same cookie twice (and someone will be left hungry...)
        synchronized (loginDummy) {
            Cookie = new String("Cookie" + _cookieCounter.get());
            _cookiesContainer.put(Cookie, userName);
            _cookieCounter.incrementAndGet();
        }
        return Cookie;
    }

    /**
     * Returns the correct user by his cookie.
     * @param cookie - user's cookie.
     * @return - the correct user.
     */
    private User getUserByCookie(String cookie){
        String userName = _cookiesContainer.get(cookie);
        return _usersContainer.get(userName);
    }

    /**
     * Returns the correct user by his phone number.
     * @param userPhone - dudes phone
     * @return - the correct user
     */
    private User getUserByPhoneNumber(String userPhone) {
        User user = null;
        synchronized (_usersContainer){
        for(Map.Entry<String,User> entry : _usersContainer.entrySet()) {
            if ((entry.getValue().getPhoneNumber().equals(userPhone))) {
                user = entry.getValue();
            }
        }
        }
        return user;
    }
    /**
     * Parse users for "create_group.jsp"
     * @param groupUsers - a string consists of all users intended to be in the group.
     * @return - a parsed list of the users.
     */
    private ArrayList<String> usersList(StringBuilder groupUsers) {
        ArrayList<String> users = new ArrayList<>();
        while(groupUsers.length() > 0){
            if(groupUsers.indexOf(",") != -1){
                users.add(groupUsers.substring(0,groupUsers.indexOf(",")));
                groupUsers.delete(0,groupUsers.indexOf(",")+1);
            }
            else{
                users.add(groupUsers.substring(0, groupUsers.length()));
                groupUsers.delete(0,groupUsers.length());
            }
        }
        return users;
    }

    /**
     * Check if all users for "create_group" exists in the program.
     * @param groupUsers - a list of desired users.
     * @return - a String of unknown users.
     */
    private String usersCheck(ArrayList<String> groupUsers) {
        StringBuilder unknownUser = new StringBuilder();
        for(String currUser : groupUsers){
            if(unknownUser.length() > 0){
                break;
            }
            else if(!_usersContainer.containsKey(currUser)){
                unknownUser.append(currUser);
            }
        }
        return new String(unknownUser);
    }
}
