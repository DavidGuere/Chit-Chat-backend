package ChitChat.repository;

import ChitChat.model.Message;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import ChitChat.model.User;
import graphql.kickstart.tools.GraphQLQueryResolver;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Component
public class UserFirestoreIO implements GraphQLQueryResolver {

    /**
     * Saves a new user to the Firestore database.
     * @param json Data of the new user in a JSON format.
     * @return Returns the ID of the new user if the creation was successful, otherwise it returns 0
     */
    public static Integer saveToFirestore(String json){
        // Open Firestore connection
        Firestore db = FirestoreClient.getFirestore();

        try {
            // convert JSON to object
            ObjectMapper jsonToUser = new ObjectMapper();
            User userPayload = jsonToUser.readValue(json, User.class);

            // get collection if exist
            ApiFuture<QuerySnapshot> collection = db.collection("Users").get();
            QuerySnapshot getCollection = collection.get();

            if (!getCollection.isEmpty()){
                // get all users
                Query allUsersQuery = db.collection("Users").orderBy("userId");
                // convert query to List of Users
                List<User> allUsers = allUsersQuery.get().get().toObjects(User.class);

                List<User> foundNickname = allUsers.stream().filter(user -> user.getNickname().equals(userPayload.getNickname())).collect(Collectors.toList());
                boolean isNicknameAvailable = foundNickname.size() == 0;

                if (isNicknameAvailable){
                    // get last userId from document
                    Query lastUserId = db.collection("Users").orderBy("userId", Query.Direction.valueOf("ASCENDING")).limitToLast(1);
                    // convert query List to object List and get the first user
                    User tempUser = lastUserId.get().get().toObjects(User.class).get(0);

                    userPayload.setUserId(tempUser.getUserId() + 1);
                    String newDocName = String.valueOf(userPayload.getUserId());

                    // Save to Firestore with updated id
                    ApiFuture<WriteResult> saveMessageData = db.collection("Users").document(newDocName).set(userPayload);
                    return userPayload.getUserId();
                } else {
                    return 0;
                }

            } else {
                String newDocName = Integer.toString(userPayload.getUserId());
                DocumentReference docRef = db.collection("Users").document(newDocName);
                ApiFuture<WriteResult> saveUserData = docRef.set(userPayload);
            }

        } catch (JsonProcessingException | InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return 1;
    }

    /**
     * Adds a new chatroom Id to the selected user.
     * @param userId The user ID of the selected user.
     * @param roomId The new chatroom ID to add to the list.
     * @return Returns a success message
     */
    public static String saveUserChatRoomToFirestore(String userId, String roomId){
        Firestore db = FirestoreClient.getFirestore();
        ApiFuture<WriteResult> saveUserData = db.collection("Users").document(userId).update("chatRooms", FieldValue.arrayUnion(roomId));
        return "Room added";
    }

    /**
     * Removes a chatroom ID from the user's list.
     * @param userId The user ID of the selected user.
     * @param roomId The ID of the room to remove from the list.
     * @return Returns a success message
     */
    public static String removeUserChatRoomFromFirebase(String userId, String roomId){
        Firestore db = FirestoreClient.getFirestore();
        ApiFuture<WriteResult> saveUserData = db.collection("Users").document(userId).update("chatRooms", FieldValue.arrayRemove(roomId));
        return "Room removed";
    }

    /**
     * Sets the status of a user to connected if the Nickname exists and if the given password and the password in the
     * database match
     * @param nickname Nickname of the user to log in.
     * @param password Password if the user to log in
     * @return Returns true if the login was successful, otherwise returns false.
     */
    public static Boolean loginUser(String nickname, String password){

        try {
            Firestore db = FirestoreClient.getFirestore();
            Query userQuery = db.collection("Users").whereEqualTo("nickname", nickname);
            User asd = userQuery.get().get().toObjects(User.class).get(0);
            if (!userQuery.get().get().isEmpty()) {
                User user = userQuery.get().get().toObjects(User.class).get(0);
                if (user.getPassword().equals(password)){
                    HashMap<String,Boolean> loginUser = new HashMap<>();
                    loginUser.put("connected", true);
                    ApiFuture<WriteResult> login = db.collection("Users").document(Integer.toString(user.getUserId())).set(loginUser, SetOptions.merge());
                    return true;
                }
            }
        } catch (Exception e){
            return false;
        }
        return false;
    }

    /**
     * Sets the status of a user to disconnected.
     * @param userId ID of the user to log out.
     * @return Returns a success message
     */
    public static Boolean logoutUser(String userId){
        Firestore db = FirestoreClient.getFirestore();
        HashMap<String,Boolean> loginUser = new HashMap<>();
        loginUser.put("connected", false);
        ApiFuture<WriteResult> logout = db.collection("Users").document(userId).set(loginUser, SetOptions.merge());
        return false;
    }

    /**
     * Retrieves the data of a user by its Id
     * @param userId The ID of the user
     * @return Returns the data of the user in a form of a User object.
     * @throws ExecutionException Exception thrown when attempting to retrieve the result of a task that aborted by throwing an exception. This exception can be inspected using the getCause() method.
     * @throws InterruptedException Thrown when a thread is waiting, sleeping, or otherwise occupied, and the thread is interrupted, either before or during the activity. Occasionally a method may wish to test whether the current thread has been interrupted, and if so, to immediately throw this exception. The following code can be used to achieve this effect:
     *     if (Thread.interrupted())  // Clears interrupted status!
     *         throw new InterruptedException();
     */
    public static User getUser(String userId) throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();
        DocumentReference users = db.collection("Users").document(userId);
        User user = users.get().get().toObject(User.class);
        assert user != null;
        return user;
    }

    /**
     * Retrieves the data of a user by its nickname
     * @param nickname The nickname of the user
     * @return the data of the user in a form of a User object.
     * @throws ExecutionException Exception thrown when attempting to retrieve the result of a task that aborted by throwing an exception. This exception can be inspected using the getCause() method.
     * @throws InterruptedException Thrown when a thread is waiting, sleeping, or otherwise occupied, and the thread is interrupted, either before or during the activity. Occasionally a method may wish to test whether the current thread has been interrupted, and if so, to immediately throw this exception. The following code can be used to achieve this effect:
     *     if (Thread.interrupted())  // Clears interrupted status!
     *         throw new InterruptedException();
     */
    public static User getUserByName(String nickname) throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();
        Query userQuery = db.collection("Users").whereEqualTo("nickname", nickname);
        User user = userQuery.get().get().toObjects(User.class).get(0);
        assert user != null;
        return user;
    }

    /**
     * Checks if the provided room ID has already been used
     * @param roomId Room to check
     * @return Returns true if the room ID is available otherwise returns false
     */
    public static Boolean isRoomIDAvailable(String roomId){
        Firestore db = FirestoreClient.getFirestore();
        Iterable<CollectionReference> collections = db.collection("ChatRooms").document("rooms").listCollections();
        List<String> rooms = new ArrayList<>();

        for (CollectionReference collRef : collections) {
            rooms.add(collRef.getId());
        }

        List<String> foundRoomId = rooms.stream().filter(ID -> ID.equals(roomId)).collect(Collectors.toList());

        return foundRoomId.size() == 0;
    }

    /**
     * Creates a new chatroom with the provided ID
     * @param roomId ID of the new room
     * @return Returns a success message
     */
    public static String createNewRoom(String roomId){
        Firestore db = FirestoreClient.getFirestore();
        String message = "Welcome to the room: " + roomId+"! This is a Beta version. If you find any bug contact the developer.";
        Message welcome = new Message(roomId, 1, "Chit-Chat", message, "external");
        ApiFuture<WriteResult> saveUserData = db.collection("ChatRooms").document("rooms").collection(roomId).document("1").set(welcome);

        return "Room created";
    }

    /**
     * Set the current room of the user to the given value
     * @param userId ID of the user
     * @param roomId ID of the room
     * @return
     */
    public static String setUserCurrentRoom(String userId, String roomId){
        Firestore db = FirestoreClient.getFirestore();
        HashMap<String,String> usersCurrentRoom = new HashMap<>();
        usersCurrentRoom.put("currentRoom", roomId);
        ApiFuture<WriteResult> currentRoom = db.collection("Users").document(userId).set(usersCurrentRoom, SetOptions.merge());
        return "Room added";
    }

    /**
     * Signs up a User to a room if the room exists.
     * @param userId ID of the user to sign
     * @param roomId ID of the room to add to the user's chatroom list
     * @return Returns true if the sign up was successful or returns false if the room does not exist.
     * @throws ExecutionException Exception thrown when attempting to retrieve the result of a task that aborted by throwing an exception. This exception can be inspected using the getCause() method.
     * @throws InterruptedException Thrown when a thread is waiting, sleeping, or otherwise occupied, and the thread is interrupted, either before or during the activity. Occasionally a method may wish to test whether the current thread has been interrupted, and if so, to immediately throw this exception. The following code can be used to achieve this effect:
     *     if (Thread.interrupted())  // Clears interrupted status!
     *         throw new InterruptedException();
     */
    public static Boolean joinUserToRoom(String userId, String roomId) throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();
        QuerySnapshot roomExists = db.collection("ChatRooms").document("rooms").collection(roomId).get().get();

        if (roomExists.isEmpty()) return false;

        User getUser = db.collection("Users").document(userId).get().get().toObject(User.class);

        List<String> userRooms = getUser.getChatRooms();
        boolean roomIsNotInUser = true;

        for (String room : userRooms) {
            if (room.equals(roomId)) {
                roomIsNotInUser = false;
                break;
            }
        }

        if (roomIsNotInUser){
            ApiFuture<WriteResult> saveUserData = db.collection("Users").document(userId).update("chatRooms", FieldValue.arrayUnion(roomId));
        }

        HashMap<String,String> usersCurrentRoom = new HashMap<>();
        usersCurrentRoom.put("currentRoom", roomId);
        ApiFuture<WriteResult> currentRoom = db.collection("Users").document(userId).set(usersCurrentRoom, SetOptions.merge());

        return true;
    }

    /**
     * Disconnects the user from the current room.
     * @param userId ID of the user
     * @return A succes message
     */
    public static String leaveCurrentRoom(String userId){
        Firestore db = FirestoreClient.getFirestore();
        HashMap<String,String> usersCurrentRoom = new HashMap<>();
        usersCurrentRoom.put("currentRoom", null);
        ApiFuture<WriteResult> currentRoom = db.collection("Users").document(userId).set(usersCurrentRoom, SetOptions.merge());
        return "Room left";
    }

    /**
     * Gets the last 5 messages from the selected chatroom
     * @param roomId ID of the chatroom
     * @return Returns a list of messages
     * @throws ExecutionException Exception thrown when attempting to retrieve the result of a task that aborted by throwing an exception. This exception can be inspected using the getCause() method.
     * @throws InterruptedException Thrown when a thread is waiting, sleeping, or otherwise occupied, and the thread is interrupted, either before or during the activity. Occasionally a method may wish to test whether the current thread has been interrupted, and if so, to immediately throw this exception. The following code can be used to achieve this effect:
     *     if (Thread.interrupted())  // Clears interrupted status!
     *         throw new InterruptedException();
     */
    public static List<Message> getLastMessages(String roomId) throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();
        Query lastMessagesQuery = db.collection("ChatRooms").document("rooms").collection(roomId).orderBy("messageId", Query.Direction.valueOf("ASCENDING")).limitToLast(5);
        return lastMessagesQuery.get().get().toObjects(Message.class);
    }
}
