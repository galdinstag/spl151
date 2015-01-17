package application;

import tokenizer_http.HttpResponseMessage;
import tokenizer_http.HttpStatusCode;
import tokenizer_whatsapp.WhatsAppMessage;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by gal on 1/13/2015.
 */
public class WhatsAppApplication {

    private HashMap<String,String> _cookiesContainer;
    private HashMap<String,User> _usersContainer;
    private HashMap<String,Group> _groupContainer;
    private AtomicInteger _cookieCounter;
    private Object loginDummy;


    public WhatsAppApplication(){
        _cookiesContainer = new HashMap<>();
        _usersContainer = new HashMap<>();
        _groupContainer = new HashMap<>();
        _cookieCounter = new AtomicInteger(1);
        loginDummy = new Object();
    }


    public boolean checkCookie(String cookie) {
        return _cookiesContainer.containsKey(cookie);
    }

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

    private String queue(WhatsAppMessage msg) {
        String userName = _cookiesContainer.get(msg.getCookie());
        User user = _usersContainer.get(userName);

        return user.queue();
    }

    private String remove_user(WhatsAppMessage msg) {
        StringBuilder response = new StringBuilder();
        String targetGroupName = msg.getAttribute("Target");
        String userPhone = msg.getAttribute("User");


        String userName = _cookiesContainer.get(msg.getCookie());
        User queryingUser = _usersContainer.get(userName);

        User userToRemove = getUserByPhoneNumber(userPhone);

        if(!_groupContainer.containsKey(targetGroupName)){
            response.append("ERROR 770: Target Does not Exist");
        }
        else if(!(_groupContainer.get(targetGroupName)).containUser(userToRemove.getName())){
            response.append("ERROR 336: Cannot remove, missing parameters");
        }
        else if(!(_groupContainer.get(targetGroupName)).containUser(queryingUser.getName())) {
            response.append("ERROR 668: Permission denied");
        }
        else {
            _groupContainer.get(targetGroupName).removeUser(userToRemove);
            response.append(userPhone);
            response.append(" removed from ");
            response.append(targetGroupName);
        }

        return new String(response);
    }

    private String add_user(WhatsAppMessage msg){
        StringBuilder response = new StringBuilder();
        String targetGroupName = msg.getAttribute("Target");
        String userPhone = msg.getAttribute("User");

        String userName = _cookiesContainer.get(msg.getCookie());
        User queryingUser = _usersContainer.get(userName);

        User userToAdd = getUserByPhoneNumber(userPhone);


        if(!_groupContainer.containsKey(targetGroupName)){
            response.append("ERROR 770: Target Does not Exist");
        }
        else if(userToAdd == null){
            response.append("ERROR 242: Cannot add user, missing parameters");
        }
        else if((_groupContainer.get(targetGroupName)).containUser(userToAdd.getName())){
            response.append("ERROR 142: Cannot add user, user already in group");
        }
        else if(!(_groupContainer.get(targetGroupName)).containUser(queryingUser.getName())){
            response.append("ERROR 669: Permission denied");
        }
        else{
            _groupContainer.get(targetGroupName).addUser(userToAdd);
            response.append(userPhone);
            response.append(" added to ");
            response.append(targetGroupName);
        }
        return new String(response);
    }

    private User getUserByPhoneNumber(String userPhone) {
        User user = null;
        for(Map.Entry<String,User> entry : _usersContainer.entrySet()){
            if((entry.getValue().getPhoneNumber().equals(userPhone))){
                user = entry.getValue();
            }
        }
        return user;
    }

    private String send(WhatsAppMessage msg) {
        StringBuilder response = new StringBuilder();
        String type = msg.getAttribute("Type");
        String target = msg.getAttribute("Target");
        String content = msg.getAttribute("Content");

        String userName = _cookiesContainer.get(msg.getCookie());
        User sender = _usersContainer.get(userName);

        if(! (type.equals("Group") || type.equals("Direct"))){
            response.append("ERROR 836: Invalid Type");
        }
        else if(! (_usersContainer.containsKey(target) || _groupContainer.containsKey(target))){
            response.append("ERROR 771: Target Does not Exist");
        }
        else{
            if(type.equals("Direct")){
                _usersContainer.get(target).addMessage(content,sender.getPhoneNumber());
            }
            else{
                Group targetGroup = _groupContainer.get(target);
                targetGroup.addMessage(content);
            }
        }


        return null;
    }

    private String list(WhatsAppMessage msg) {
        String listType = msg.getAttribute("List");
        LinkedList<String> participantsList = new LinkedList<>();
        if(listType.equals("Users")){
            for(Map.Entry<String,User> entry : _usersContainer.entrySet()){
                participantsList.add(entry.getKey());
            }
        }
        else if(listType.equals("Group") || listType.equals("Groups")){
            for(Map.Entry<String,Group> entry : _groupContainer.entrySet()){
                participantsList.add(entry.getKey());
            }
        }
        return participantsList.toString();
    }

    private String create_group(WhatsAppMessage msg) {
        StringBuilder response = new StringBuilder();

        String groupName = msg.getAttribute("GroupName");
        String groupUsers = msg.getAttribute("Users");
        String unknownUser = usersCheck(new StringBuilder(groupUsers));

        if(groupName == ""){
            response.append("ERROR 675: Cannot create group, missing parameters\n");
        }
        else if(_groupContainer.containsKey(groupName)){
            response.append("ERROR 511: Group Name Already Taken");
        }
        else if(unknownUser.length() > 0){
            response.append("ERROR 929: Unknown User ");
            response.append(unknownUser);
        }
        //it's all good in the hood
        else{
            _groupContainer.put(groupName,new Group(groupName));
            ArrayList<String> usersList = usersList(new StringBuilder(groupUsers));
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



    private ArrayList<String> usersList(StringBuilder groupUsers) {
        ArrayList<String> users = new ArrayList<>();
        while(groupUsers.length() > 0){
            if(groupUsers.indexOf("=") != -1){
                users.add(groupUsers.substring(0,groupUsers.indexOf(",")));
                groupUsers.delete(0,groupUsers.indexOf("=")+1);
            }
            else{
                users.add(groupUsers.substring(0, groupUsers.length()));
                groupUsers.delete(0,groupUsers.length());
            }
        }
        return users;
    }

    private String usersCheck(StringBuilder groupUsers) {
        System.out.println();
        StringBuilder unknownUser = new StringBuilder();
        while(groupUsers.length() > 0 && unknownUser.length() == 0){
            if(groupUsers.indexOf(",") != -1){
                if(!_usersContainer.containsKey(groupUsers.substring(0,groupUsers.indexOf(",")))){
                    unknownUser.append(groupUsers.substring(0,groupUsers.indexOf(",")));
                }
                groupUsers.delete(0,groupUsers.indexOf(",")+1);
            }
            else{
                if(!_usersContainer.containsKey(groupUsers.substring(0,groupUsers.length()))){
                    unknownUser.append(groupUsers.substring(0,groupUsers.length()));
                }
                groupUsers.delete(0,groupUsers.length());
            }
        }
        return new String(unknownUser);
    }






    private String logout(WhatsAppMessage msg) {
        _cookiesContainer.remove(msg.getCookie());
        return new String("GoodBye");
    }

    private String login(WhatsAppMessage msg) {
            StringBuilder responseMessage = new StringBuilder();
            HashMap<String,String> messageBody = new HashMap<String,String>(msg.getBody());
        if(!_usersContainer.containsKey(messageBody.get("UserName"))){
            _usersContainer.put(messageBody.get("UserName"), new User(messageBody.get("UserName"),messageBody.get("Phone")));
        }
        responseMessage.append("Welcome ");
        responseMessage.append(messageBody.get("UserName"));
        responseMessage.append("@");
        responseMessage.append(messageBody.get("Phone"));

        return new String(responseMessage);
    }

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
}
