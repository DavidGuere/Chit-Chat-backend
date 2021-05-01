package ChitChat.model;

import java.util.List;

public class User {
    private int userId;
    private String nickname;
    private String password;
    private Boolean connected;
    private List<String> chatRooms;
    private String currentRoom;

    public User() {
    }

    public User(int userId, String nickname, String password, Boolean connected) {
        this.userId = userId;
        this.nickname = nickname;
        this.password = password;
        this.connected = connected;
    }

    public User(int userId, String nickname, String password, Boolean connected, List<String> chatRooms, String currentRoom) {
        this.userId = userId;
        this.nickname = nickname;
        this.password = password;
        this.connected = connected;
        this.chatRooms = chatRooms;
        this.currentRoom = currentRoom;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getConnected() {
        return connected;
    }

    public void setConnected(Boolean connected) {
        this.connected = connected;
    }

    public List<String> getChatRooms() {
        return chatRooms;
    }

    public void setChatRooms(List<String> chatRooms) {
        this.chatRooms = chatRooms;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", nickname='" + nickname + '\'' +
                ", password='" + password + '\'' +
                ", connected=" + connected +
                ", chatRooms=" + chatRooms +
                ", currentRoom='" + currentRoom + '\'' +
                '}';
    }

    public String getCurrentRoom() {
        return currentRoom;
    }

    public void setCurrentRoom(String currentRoom) {
        this.currentRoom = currentRoom;
    }
}
