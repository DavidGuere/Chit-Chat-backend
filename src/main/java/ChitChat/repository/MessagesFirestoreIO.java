package ChitChat.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import ChitChat.model.Message;

import java.util.concurrent.ExecutionException;

public class MessagesFirestoreIO {

    /**
     * Receives a data payload in a JSON format and saves it to Firestore. The JSON contains data of the chat room. If
     * the room exist, it will save the massage to that room with an update message ID. If the room does not exist, it will create a new collection
     * to save the messages of the room.
     * @param json JSON containing the information of the message such as the room Id, the nickname of the user and the message.
     * @return Returns the same message with and updated ID
     */
    public static Message saveToDatabaseAndUpdateID(String json){
        // Open Firestore connection
        Firestore db = FirestoreClient.getFirestore();
        Message messageWithUpdatedId = new Message();

        try {
            // convert JSON to object
            ObjectMapper jsonToMessage = new ObjectMapper();
            messageWithUpdatedId = jsonToMessage.readValue(json, Message.class);

            // get collection if exist
            String collectionID = messageWithUpdatedId.getRoomId();
            ApiFuture<QuerySnapshot> collection = db.collection("ChatRooms").document("rooms").collection(collectionID).get();
            QuerySnapshot getCollection = collection.get();

            if (!getCollection.isEmpty()){
                // get last messageId from document
                Query lastMessageId = db.collection("ChatRooms").document("rooms").collection(collectionID).orderBy("messageId", Query.Direction.valueOf("ASCENDING")).limitToLast(1);
                // convert query List to object List and get the first message
                Message tempMessage = lastMessageId.get().get().toObjects(Message.class).get(0);

                messageWithUpdatedId.setMessageId(tempMessage.getMessageId() + 1);
                String newDocName =String.valueOf(messageWithUpdatedId.getMessageId());

                // Save to Firestore with updated id
                ApiFuture<WriteResult> saveMessageData = db.collection("ChatRooms").document("rooms").collection(collectionID).document(newDocName).set(messageWithUpdatedId);

            } else {
                String newRoomID = messageWithUpdatedId.getRoomId();
                String newDocName = Integer.toString(messageWithUpdatedId.getMessageId());
                DocumentReference docRef = db.collection("ChatRooms").document("rooms").collection(newRoomID).document(newDocName);
                ApiFuture<WriteResult> saveMessageData = docRef.set(messageWithUpdatedId);
            }

        } catch (JsonProcessingException | InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return messageWithUpdatedId;
    }
}
