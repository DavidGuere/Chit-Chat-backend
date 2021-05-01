package ChitChat.model;

public class Message {
    private String roomId;
    private int messageId;
    private String user;
    private String message;
    private String source;

    public Message() {
    }

    /**
     * Payload that contains the data to be transmitted and received though the websockets.
     * @param roomId Id of the chatroom.
     * @param messageId ID of the message.
     * @param user Nickname of the user.
     * @param message Massage sent by the user.
     */
    public Message(String roomId, int messageId, String user, String message, String source) {
        this.roomId = roomId;
        this.messageId = messageId;
        this.user = user;
        this.message = message;
        this.source = source;
    }

    public int getMessageId() {
        return messageId;
    }

    public void setMessageId(int messageId) {
        this.messageId = messageId;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    @Override
    public String toString() {
        return "Message{" +
                "roomId='" + roomId + '\'' +
                ", messageId=" + messageId +
                ", user='" + user + '\'' +
                ", message='" + message + '\'' +
                ", source='" + source + '\'' +
                '}';
    }
}
