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
     * @return Returns a success or failure message
     */
    public static String saveToFirestore(String json){
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
                } else {
                    return "The nickname already exists";
                }

            } else {
                String newDocName = Integer.toString(userPayload.getUserId());
                DocumentReference docRef = db.collection("Users").document(newDocName);
                ApiFuture<WriteResult> saveUserData = docRef.set(userPayload);
            }

        } catch (JsonProcessingException | InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return "User created";
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
        ApiFuture<WriteResult> saveUserData = db.collection("Users").document(userId).update("ChatRoom", FieldValue.arrayRemove(roomId));
        return "Room removed";
    }

    /**
     * Sets the status of a user to connected.
     * @param userId ID of the user to log in.
     * @return Returns a welcome message
     */
    public static String loginUser(String userId){
        Firestore db = FirestoreClient.getFirestore();
        HashMap<String,String> loginUser = new HashMap<>();
        loginUser.put("connected", "true");
        ApiFuture<WriteResult> login = db.collection("Users").document(userId).set(loginUser, SetOptions.merge());
        return "Welcome!";
    }

    /**
     * Sets the status of a user to disconnected.
     * @param userId ID of the user to log out.
     * @return Returns a success message
     */
    public static String logoutUser(String userId){
        Firestore db = FirestoreClient.getFirestore();
        HashMap<String,String> loginUser = new HashMap<>();
        loginUser.put("connected", "false");
        ApiFuture<WriteResult> logout = db.collection("Users").document(userId).set(loginUser, SetOptions.merge());
        return "You have successfully logged out";
    }

    /**
     * Retrieves the data of a user.
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
        String message = "Welcome to the room: " + roomId;
        Message welcome = new Message(roomId, 1, "Admin", message);
        ApiFuture<WriteResult> saveUserData = db.collection("ChatRooms").document("rooms").collection(roomId).document("1").set(welcome);

        return "Room created";
    }
}
