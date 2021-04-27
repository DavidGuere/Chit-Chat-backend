package ChitChat.model;

public class Message {
    private String roomId;
    private int messageId;
    private String user;
    private String message;

    public Message() {
    }

    /**
     * Payload that contains the data to be transmitted and received though the websockets.
     * @param roomId Id of the chatroom.
     * @param messageId ID of the message.
     * @param user Nickname of the user.
     * @param message Massage sent by the user.
     */
    public Message(String roomId, int messageId, String user, String message) {
        this.roomId = roomId;
        this.messageId = messageId;
        this.user = user;
        this.message = message;
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

}
