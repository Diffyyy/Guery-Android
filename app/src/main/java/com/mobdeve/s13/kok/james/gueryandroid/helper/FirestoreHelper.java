package com.mobdeve.s13.kok.james.gueryandroid.helper;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Transaction;
import com.mobdeve.s13.kok.james.gueryandroid.activity.LoginActivity;
import com.mobdeve.s13.kok.james.gueryandroid.model.Comment;
import com.mobdeve.s13.kok.james.gueryandroid.model.Content;
import com.mobdeve.s13.kok.james.gueryandroid.model.Post;
import com.mobdeve.s13.kok.james.gueryandroid.model.Profile;
import com.mobdeve.s13.kok.james.gueryandroid.model.Vote;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class FirestoreHelper {
    public static final String POST_GAME = "game";
    public static final String POST_BODY = "body";
    public static final String  POST_TITLE = "title";
    public static final String  ID = "id";
    public static final String POST_PROFILE = "profile";
    public static final String POST_UPVOTES = "upvotes";
    public static final String  POST_DATE = "date";
    public static final String POST_COMMENTS = "comments";
    public static final String POST_ATTACHED = "attachment";
    public static final String POST_TYPE = "postType";


    public static final String COMMENTS = "comments";
    public static final String COMMENT_CONTENT = "content";
    public static final String COMMENT_REPLIES = "replies";
    public static final String COMMENT_PROFILE = "profile";
    public static final String COMMENT_DATE = "date";
    public static final String COMMENT_ISPOST = "ispost";
    public static final String COMMENT_UPVOTES = "upvotes";


    public static final String PROFILE_NAME = "username";
    public static final String PROFILE_PFP = "pfp";

    public static final String POSTS = "posts";
    public static final String USERS = "users";
    public static final FirebaseFirestore db = FirebaseFirestore.getInstance();
    public static FirestoreHelper instance = null  ;
    private HashMap<String, Profile> profiles;

    public static final String VOTES = "votes";
    public static final String VOTE_PROFILE = "profile";
    public static final String VOTE_CONTENT = "content";
    public static final String VOTE_TYPE = "voteType";




    public static FirestoreHelper getInstance(){
        if (instance == null){
            instance = new FirestoreHelper()    ;
        }
        return instance;
    }

    public FirestoreHelper() {
        this.profiles = new HashMap<>();
    }


    public void updatePostComments(Post post, Consumer<Void> callback){

        db.collection(POSTS)
                .document(post.getId()+"."+POST_COMMENTS)
                .update(POST_COMMENTS, post.getComments())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d("BURGER", "COMMENT ADDED SUCCESSFULLY");
                        callback.accept(unused);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });

    }
    public  void addPost(Post post, Consumer<String> callback){
        Map<String, Object> map = convertPost(post);
        DocumentReference newPost = db.collection(POSTS).document();
        map.put(ID, newPost.getId());
        Log.d("BURGER", "ADDING POST");
        newPost.set(map).addOnSuccessListener(new OnSuccessListener<>() {
                    @Override
                    public void onSuccess(Void unused) {
                        callback.accept(newPost.getId());
                    }

                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("BURGER", "Error adding post", e);
                    }
                });
    }
    private void retrievePostDetails(DocumentSnapshot document, Consumer<Post> callback){
        Post post = document.getData()==null?null:convertMapToPost(document.getData());
        if(post!=null)getAndSaveProfile(post.getProfile().getId(), new Consumer<Profile>() {
            @Override
            public void accept(Profile profile) {
                post.setProfile(profile);
                retrieveVote(LoginActivity.p, post, new Consumer<Vote>() {
                    @Override
                    public void accept(Vote vote) {
                        post.setUserVote(vote);
                        callback.accept(post);
                    }
                });

            }
        });
        else callback.accept(post);
    }
    public void  retrievePosts(Consumer<Post > callback, Consumer<Void> doneCallback){
        db.collection(POSTS)
                .orderBy(POST_DATE, Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                retrievePostDetails(document, callback);
                            }
                        } else {

                        }
                        doneCallback.accept(null);
                    }

                });
    }
    public void updateCommentList(String parentId, Comment comment){
        String collection;
        String field;
        if(comment.isToPost()){
            collection = POSTS;
            field = POST_COMMENTS;
        }else{
            collection = COMMENTS;
            field = COMMENT_REPLIES;
        }
        
        DocumentReference contentReference = db.collection(collection).document(parentId    );
        db.runTransaction(new Transaction.Function<Void>() {
            @Nullable
            @Override
            public Void apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {
                transaction.update(contentReference, field, FieldValue.arrayUnion(comment.getId()));

                return null;
            }
        })
        .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d("BURGER", "COMMENT SUCCESSFULLY ADDEDD TO ID: "+parentId);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("BURGER", "COMMENT FAILED TO ADD", e);
                    }
                });

    }
    public void addComment(String parentId, Comment comment, Consumer<String> callback){
        Map<String, Object> map = convertComment(comment);
        DocumentReference newComment = db.collection(COMMENTS).document();
        map.put(ID, newComment.getId());
        newComment.set(map).addOnSuccessListener(new OnSuccessListener<>() {
                    @Override
                    public void onSuccess(Void unused) {
                        comment.setId(newComment.getId());
                        updateCommentList(parentId, comment);
                        callback.accept(newComment.getId());
                    }

                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("BURGER", "Error adding comment", e);
                    }
                });
    }
    public void retrieveComment(String commentId, Consumer<Comment> callback){
        db.collection(COMMENTS)
                .document(commentId)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Comment comment = documentSnapshot.getData()==null?null:convertMapToComment(documentSnapshot.getData());
                        Log.d("BURGER", "RETRIEVING COMMENT: "+comment);
                        if(comment!=null)getAndSaveProfile(comment.getProfile().getId(), new Consumer<Profile>() {
                            @Override
                            public void accept(Profile profile) {

                                comment.setProfile(profile);
                                retrieveVote(LoginActivity.p, comment, new Consumer<Vote>() {
                                    @Override
                                    public void accept(Vote vote) {
                                        comment.setUserVote(vote);
                                        Log.d("BURGER", "SUCCESSFULLY RETRIEVED COMMENT");
                                        callback.accept(comment);
                                    }
                                });
                            }
                        });else callback.accept(comment);
                    }
                });
    }

    public void retrieveVote(Profile profile, Content content, Consumer<Vote> callback){

        db.collection(VOTES).whereEqualTo(VOTE_PROFILE, profile.getId())
                .whereEqualTo(VOTE_CONTENT, content.getId())
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if(!queryDocumentSnapshots.isEmpty()){
                            Log.d("BURGER","RETRIEVED VOTE FOR PROFILE ID: "+profile.getId() + " AND CONTENT: "+content.getId());
                            HashMap<String, Object> data = (HashMap<String, Object>) queryDocumentSnapshots.getDocuments().get(0).getData();
                            Vote vote = Vote.valueOf((String) data.get(VOTE_TYPE));
                            Log.d("BURGER", "RETRIEVING VOTE: "+vote.name());
                            callback.accept(vote);
                        }else callback.accept(Vote.CANCEL);

                    }
                });
    }

    public void retrievePost(String id, Consumer<Post> callback){
            db.collection(POSTS).document(id).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        retrievePostDetails(documentSnapshot, callback);
                    }
                });
    }
    public void getAndSaveProfile(String id, Consumer<Profile> callback)  {
        Log.d("BURGER", "GETTING PROFILE ID: "+id);
        db.collection(USERS).document(id).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Profile converted = convertMapToProfile(documentSnapshot.getData());
                        Log.d("BURGER", "GOT PROFILE: "+converted);
                        if(!profiles.containsKey(id))profiles.put(id, converted);
                        callback.accept(converted);
                    }
                });

    }
    public  Map<String, Object> convertPost(Post post){
        Log.d("BURGER", "Converting Post: "+post.getProfile().getId()   );
        Map<String, Object> map = new HashMap<>();

        map.put(ID, post.getId());
        map.put(POST_GAME, post.getGame());
        map.put(POST_BODY, post.getBody());
        map.put(POST_PROFILE, post.getProfile().getId());
        map.put(POST_TITLE, post.getTitle());
        map.put(POST_UPVOTES, post.getUpvotes());
        map.put(POST_DATE, FieldValue.serverTimestamp());
        map.put(POST_COMMENTS, post.getComments().stream().map(comment -> comment.getId()).collect(Collectors.toList()));
        map.put(POST_ATTACHED, post.getAttached());
        map.put(POST_TYPE, post.getType());
        return map;
    }

    public Map<String, Object> convertComment(Comment comment)  {
        Map<String, Object> map = new HashMap<>();

        map.put(ID, comment.getId());
        map.put(COMMENT_CONTENT, comment.getBody()  );
        map.put(COMMENT_DATE, FieldValue.serverTimestamp());
        map.put(COMMENT_PROFILE, comment.getProfile().getId());
        map.put(COMMENT_REPLIES, comment.getReplies().stream().map(reply -> reply.getId()).collect(Collectors.toList()));
        map.put(COMMENT_ISPOST, comment.getToPost());
        map.put(COMMENT_UPVOTES, comment.getUpvotes());
        return map;
    }


    public Comment convertMapToComment(Map<String, Object> map){
        String id  = (String) map.get(ID);
        String profileId = (String) map.get(COMMENT_PROFILE);
        String content = (String) map.get(COMMENT_CONTENT);
        LocalDateTime date = DateHelper.convertTimestamptoDate((Timestamp)map.get(COMMENT_DATE));

        ArrayList<Comment> replies =  new ArrayList<Comment>(((ArrayList<String>) map.get(COMMENT_REPLIES)).stream().map(commentId -> new Comment(commentId)).collect(Collectors.toList()));
        int toPost = ((Long) map.get(COMMENT_ISPOST)).intValue();
        int upvotes = ((Long) map.get(COMMENT_UPVOTES)).intValue();

        Profile profile = new Profile(profileId, "Loading..."  );
        Comment comment = new Comment(profile, date, content, replies, id);
        comment.setUpvotes(upvotes);
        comment.setToPost(toPost);
        return comment;
    }

    public Profile convertMapToProfile(Map<String, Object> map){
        String id = (String) map.get(ID);
        String username = (String) map.get(PROFILE_NAME);
        return new Profile(id, username);
    }




    public Post convertMapToPost(Map<String, Object> map)  {

        String id = (String) map.get(ID);
        String game = (String) map.get(POST_GAME);
        String body = (String) map.get(POST_BODY);

        String title = (String) map.get(POST_TITLE);
        int upvotes = ((Long) map.get(POST_UPVOTES)).intValue();
        LocalDateTime createdAt = DateHelper.convertTimestamptoDate((Timestamp) map.get(POST_DATE));
        ArrayList<Comment> comments =  new ArrayList<Comment>(((ArrayList<String>) map.get(POST_COMMENTS)).stream().map(commentId -> new Comment(commentId)).collect(Collectors.toList()));

        String profileId = (String) map.get(POST_PROFILE);
        Profile profile =  new Profile(profileId, "Loading...");
        Post post = new Post(game, profile, createdAt, title, body, upvotes ,comments  );
        post.setId(id);

        String attachment = (String) map.get(POST_ATTACHED);
        int type = (int) map.get(POST_TYPE);
        post.setType(type, attachment   );


        return post;
    }

    public void vote(Profile profile, Content content,Vote prevVote, Vote currVote, Consumer<Void> callback){
        String collection;
        String field;
        if(content instanceof Post){
            collection = POSTS;
            field = POST_UPVOTES;
        }else{
            collection = COMMENTS;
            field = COMMENT_UPVOTES;
        }

        DocumentReference contentReference = db.collection(collection).document(content.getId() );
        db.runTransaction(new Transaction.Function<Void>() {
            @Nullable
            @Override
            public Void apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {
                DocumentSnapshot snapshot = transaction.get(contentReference);
                transaction.update(contentReference, field, snapshot.getLong(field) + (currVote.value-prevVote.value) );

                return null;
            }
        }).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                if(prevVote.equals(Vote.CANCEL)){
                    Log.d("BURGER", "ADDING VOTE");
                    HashMap<String, Object> voteMap = new HashMap<>();
                    voteMap.put(VOTE_PROFILE, profile.getId());
                    voteMap.put(VOTE_CONTENT, content.getId());
                    voteMap.put(VOTE_TYPE, currVote.name());
                    db.collection(VOTES).add(voteMap).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        //add if haven't voted yet
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            callback.accept(null);
                        }
                    });
                }else {
                    db.collection(VOTES).whereEqualTo(VOTE_PROFILE, profile.getId())
                            .whereEqualTo(VOTE_CONTENT, content.getId())
                            .get()
                            .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                @Override
                                public void onSuccess(QuerySnapshot snapshot) {
                                    if(snapshot.isEmpty()) return;
                                    DocumentReference voteRef = snapshot.getDocuments().get(0).getReference();
                                    //delete if cancel vote
                                    if(currVote.equals(Vote.CANCEL)) voteRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            callback.accept(null);
                                        }
                                    });
                                    else {                          //edit otherwise
                                        voteRef.update(VOTE_TYPE, currVote.name()  ).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                callback.accept(null);
                                            }
                                        });
                                    }
                                }
                            });
                }
            }
        });
    };
    public void searchPosts(String keyword, Consumer<Post> callback, Consumer<Void> doneCallback ){
        CollectionReference postsRef = db.collection(POSTS);
        retrievePosts(new Consumer<Post>() {
            @Override
            public void accept(Post post) {
                if (keyword==null || keyword.isEmpty() || post.getTitle().toLowerCase().contains(keyword.toLowerCase())
                        || post.getBody().toLowerCase().contains(keyword.toLowerCase())
                        || post.getGame().toLowerCase().contains(keyword.toLowerCase())) {
                    callback.accept(post);
                }
            }
        }, new Consumer<Void>() {
            @Override
            public void accept(Void unused) {
                doneCallback.accept(unused);
            }
        });
    }



}



