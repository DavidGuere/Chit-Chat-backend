package ChitChat.repository;

//import com.coxautodev.graphql.tools.GraphQLQueryResolver;
//import com.coxautodev.graphql.tools.GraphQLQueryResolver;
//import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import ChitChat.model.User;
import graphql.kickstart.tools.GraphQLQueryResolver;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.concurrent.ExecutionException;

@Component
public class UserFirestoreIO implements GraphQLQueryResolver {

    /**
     * Saves a new user to the Firestore database.
     * @param json Data of the new user in a JSON format.
     */
//    public static void saveToFirestore(String json){
//        // Open Firestore connection
//        Firestore db = FirestoreClient.getFirestore();
//
//        try {
//            // convert JSON to object
//            ObjectMapper jsonToUser = new ObjectMapper();
//            User userPayload = jsonToUser.readValue(json, User.class);
//
//            // get collection if exist
//            ApiFuture<QuerySnapshot> collection = db.collection("Users").get();
//            QuerySnapshot getCollection = collection.get();
//
//            if (!getCollection.isEmpty()){
//                // get last messageId from document
//                Query lastUserId = db.collection("Users").orderBy("userId", Query.Direction.valueOf("ASCENDING")).limitToLast(1);
//                // convert query List to object List and get the first message
//                User tempUser = lastUserId.get().get().toObjects(User.class).get(0);
//
//                userPayload.setUserId(tempUser.getUserId() + 1);
//                String newDocName =String.valueOf(userPayload.getUserId());
//
//                // Save to Firestore with updated id
//                ApiFuture<WriteResult> saveMessageData = db.collection("Users").document(newDocName).set(userPayload);
//
//            } else {
//                String newDocName = Integer.toString(userPayload.getUserId());
//                DocumentReference docRef = db.collection("Users").document(newDocName);
//                ApiFuture<WriteResult> saveUserData = docRef.set(userPayload);
//            }
//
//        } catch (JsonProcessingException | InterruptedException | ExecutionException e) {
//            e.printStackTrace();
//        }
//    }

    /**
     * Adds a new chatroom Id to the selected user.
     * @param userId The user ID of the selected user.
     * @param roomId The new chatroom ID to add to the list.
     */
//    public static void saveUserChatRoomToFirestore(String userId, String roomId){
//        Firestore db = FirestoreClient.getFirestore();
//        ApiFuture<WriteResult> saveUserData = db.collection("Users").document(userId).update("ChatRoom", FieldValue.arrayUnion(roomId));
//    }

    /**
     * Removes a chatroom ID from the user's list.
     * @param userId The user ID of the selected user.
     * @param roomId The ID of the room to remove from the list.
     */
//    public static void removeUserChatRoomFromFirebase(String userId, String roomId){
//        Firestore db = FirestoreClient.getFirestore();
//        ApiFuture<WriteResult> saveUserData = db.collection("Users").document(userId).update("ChatRoom", FieldValue.arrayRemove(roomId));
//    }

    /**
     * Sets the status of a user to connected.
     * @param userId ID of the user to log in.
     */
//    public static void loginUser(String userId){
//        Firestore db = FirestoreClient.getFirestore();
//        HashMap<String,String> loginUser = new HashMap<>();
//        loginUser.put("connected", "true");
//        ApiFuture<WriteResult> login = db.collection("Users").document(userId).set(loginUser, SetOptions.merge());
//    }

    /**
     * Sets the status of a user to disconnected.
     * @param userId ID of the user to log out.
     */
//    public static void logoutUser(String userId){
//        Firestore db = FirestoreClient.getFirestore();
//        HashMap<String,String> loginUser = new HashMap<>();
//        loginUser.put("connected", "false");
//        ApiFuture<WriteResult> logout = db.collection("Users").document(userId).set(loginUser, SetOptions.merge());
//    }

    /**
     * Retrieves the connection status of the user.
     * @param userId The ID of the user
     * @return Returns a String containing true of false.
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
}
