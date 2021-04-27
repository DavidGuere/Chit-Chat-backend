package ChitChat.model;

import java.util.List;

public class User {
    private int userId;
    private String nickname;
    private String password;
    private String connected;
    private List<String> chatRooms;

    public User() {
    }

    public User(int userId, String nickname, String password, String connected) {
        this.userId = userId;
        this.nickname = nickname;
        this.password = password;
        this.connected = connected;
    }

    public User(int userId, String nickname, String password, String connected, List<String> chatRooms) {
        this.userId = userId;
        this.nickname = nickname;
        this.password = password;
        this.connected = connected;
        this.chatRooms = chatRooms;
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

    public String getConnected() {
        return connected;
    }

    public void setConnected(String connected) {
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
                ", connected='" + connected + '\'' +
                ", chatRooms=" + chatRooms +
                '}';
    }
}
