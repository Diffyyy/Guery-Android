package com.mobdeve.s13.kok.james.gueryandroid.helper;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Transaction;
import com.mobdeve.s13.kok.james.gueryandroid.model.Comment;
import com.mobdeve.s13.kok.james.gueryandroid.model.Content;
import com.mobdeve.s13.kok.james.gueryandroid.model.Post;
import com.mobdeve.s13.kok.james.gueryandroid.model.Profile;
import com.mobdeve.s13.kok.james.gueryandroid.model.Vote;

import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
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
    public static final String PROFILE_EMAIL = "email";
    public static final String PROFILE_NUMPOSTS= "numposts";
    public static final String PROFILE_ABOUT = "about";

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

    public void deletePost(Post post, Consumer<Void> callback){
        db.collection(POSTS).document(post.getId())
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        callback.accept(unused);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("BURGER", "FAILED TO DELETE POST: "+e.getMessage());
                    }
                });
    }
    public void editPost(Post post, Consumer<Void> callback){
        Log.d("BURGER", "EDITING POST: "+post);
        DocumentReference previousPost = db.collection(POSTS).document(post.getId());
        String newAttachment = post.getType()==Post.PostType.TEXT.value?null:StorageHelper.POSTS_FOLDER+post.getId()    ;
        Map<String, Object> map = convertPost(post);
        map.put(POST_ATTACHED, newAttachment);
        previousPost.update(convertPost(post))
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        callback.accept(unused);
                    }
                });
    }

    public  void addPost(Post post, Consumer<String> callback){
        DocumentReference newPost = db.collection(POSTS).document();
        if(post.getType()!=Post.PostType.TEXT.value){
            post.setAttachment(StorageHelper.POSTS_FOLDER+newPost.getId());
        }
        post.setId(newPost.getId());
        Map<String, Object> map = convertPost(post);

        Log.d("BURGER", "ADDING POST");
        newPost.set(map).addOnSuccessListener(new OnSuccessListener<>() {
                    @Override
                    public void onSuccess(Void unused) {
                        db.collection(USERS).document(AuthHelper.getInstance().getProfile().getId())
                                .update(PROFILE_NUMPOSTS, FieldValue.increment(1));
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
                retrieveVote(AuthHelper.getInstance().getProfile(), post, new Consumer<Vote>() {
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
    private Query retrievePostsQuery(){
        return db.collection(POSTS).orderBy(POST_DATE, Query.Direction.DESCENDING);
    }


    public void retrieveProfilePosts(Profile profile, Consumer<Post> callback, Consumer<Void> doneCallback)  {
        retrievePostsQuery()
                .whereEqualTo(POST_PROFILE, profile.getId())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for(QueryDocumentSnapshot document: task.getResult()){
                                retrievePostDetails(document, callback);
                            }
                        }
                        doneCallback.accept(null);
                    }
                });
    }
    public void retrieveProfile(String id, Consumer<Profile> callback){
        db.collection(USERS).document(id).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        callback.accept(convertMapToProfile(documentSnapshot.getData()));
                    }
                });
    }
    public void  retrievePosts(Consumer<Post > callback, Consumer<Integer> doneCallback){
        retrievePostsQuery()
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            doneCallback.accept(task.getResult().size());
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                retrievePostDetails(document, callback);
                            }

                        } else {

                        }

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
                                retrieveVote(AuthHelper.getInstance().getProfile(), comment, new Consumer<Vote>() {
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
        if(profile==null){
            callback.accept(Vote.CANCEL);
            return;
        }
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
        Profile profile = new Profile(id, username);
        String pfp = (String) map.get(PROFILE_PFP);
        profile.setPfp(pfp);

        String email = (String) map.get(PROFILE_EMAIL   );
        int numPosts = ((Long) map.get(PROFILE_NUMPOSTS  )).intValue();
        String about = (String) map.get(PROFILE_ABOUT);

        profile.setEmail(email);
        profile.setNumPosts(numPosts);
        profile.setAbout(about);
        return profile;
    }

    public HashMap<String, Object> convertProfile(Profile profile){
        HashMap<String, Object> map = new HashMap<>();
        map.put(ID, profile.getId());
        map.put(PROFILE_NAME, profile.getUsername() );
        map.put(PROFILE_PFP, profile.getPfp());
        map.put(PROFILE_EMAIL, profile.getEmail()   );
        map.put(PROFILE_NUMPOSTS, profile.getNumPosts());
        map.put(PROFILE_ABOUT, profile.getAbout());
        return map;
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
        int type = ((Long) map.get(POST_TYPE)).intValue();
        post.setType(type, attachment   );


        return post;
    }

    public void vote(Profile profile, Content content,Vote currVote, Consumer<Void> callback, Consumer<Exception> error){
        callback.accept(null);
        String collection;
        String field;
        if(content instanceof Post){
            collection = POSTS;
            field = POST_UPVOTES;
        }else{
            collection = COMMENTS;
            field = COMMENT_UPVOTES;
        }
        String voteId = profile.getId() + content.getId();
        DocumentReference voteReference = db.collection(VOTES).document(voteId);
        DocumentReference contentReference = db.collection(collection).document(content.getId());
        db.runTransaction(new Transaction.Function<Vote>() {
            @Nullable
            @Override
            public Vote apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {
                Vote prevVote  = Vote.CANCEL;
                HashMap<String, Object> map = (HashMap<String, Object>) transaction.get(voteReference).getData();
                if(map==null){
                    map = new HashMap<>();
                    map.put(VOTE_PROFILE, profile.getId());
                    map.put(VOTE_CONTENT, content.getId()   );
                    map.put(VOTE_TYPE, currVote.name());
                }else{
                    prevVote =  Vote.valueOf((String) map.get(VOTE_TYPE));
                    map.put(VOTE_TYPE, currVote.name());
                }
                transaction.set(voteReference, map);


                return prevVote;
            }
        }).addOnSuccessListener(new OnSuccessListener<Vote>() {
            @Override
            public void onSuccess(Vote prevVote) {
                int diff = currVote.value - prevVote.value;
                db.runTransaction(new Transaction.Function<Void>() {
                    @Nullable
                    @Override
                    public Void apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {
                        transaction.update(contentReference, field, FieldValue.increment(diff));
                        return null;
                    }
                });

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("BURGER", "VOTE EXCEPTION: "+e.getMessage());
                error.accept(e);
            }
        });
    }
//    public void searchPosts(String keyword, Consumer<Post> callback, Consumer<Void> doneCallback ){
//        retrievePosts(new Consumer<Post>() {
//            @Override
//            public void accept(Post post) {
//                if (keyword==null || keyword.isEmpty() || post.getTitle().toLowerCase().contains(keyword.toLowerCase())
//                        || post.getBody().toLowerCase().contains(keyword.toLowerCase())
//                        || post.getGame().toLowerCase().contains(keyword.toLowerCase())) {
//                    callback.accept(post);
//                }
//            }
//        }, new Consumer<Void>() {
//            @Override
//            public void accept(Void unused) {
//                doneCallback.accept(unused);
//            }
//        });
//    }
    private void checkUser(String fieldName, String fieldValue, Consumer<Profile> callback){
        db.collection(USERS).whereEqualTo(fieldName, fieldValue)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if(queryDocumentSnapshots.isEmpty()){
                            callback.accept(null);
                        }else{
                            Map<String, Object> profileMap = queryDocumentSnapshots.getDocuments().get(0).getData();
                            Log.d("BURGER", "SUCCESSFULLY GOT USER LOGIN: "+profileMap);
                            Profile profile = convertMapToProfile(profileMap);
                            Log.d("BURGER", "SUCCESSFULLY GOT USER LOGIN: "+profile);
                            callback.accept(profile);

                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("BURGER", "FAILED TO GET USER: "+fieldValue, e);
                    }
                });
    }


    public void checkUsername(String username, Consumer<Profile > callback ){
        checkUser(PROFILE_NAME, username, callback);
    }
    public void checkEmail(String email, Consumer<Profile> callback){
        checkUser(PROFILE_EMAIL, email, callback);
    }

    public void addUser(String username, String email, Consumer<Profile> callback){
        DocumentReference newUser = db.collection(USERS).document();
        Profile profile = new Profile(newUser.getId(), email, username);
        newUser.set(convertProfile(profile))
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        callback.accept(profile);
                    }
                });
    }


    public void editUser(Profile profile, String newUsername, String newAbout, InputStream inputStream, Consumer<Void> callback){
        Boolean updateSuccessful = false;
        String filename = profile.getId();

        if (inputStream != null) {
            Log.i("EDITUSER", "INPUTSTREAM NOT NULL");
            StorageHelper.getInstance().upload(filename, StorageHelper.PFP_FOLDER, inputStream, new Consumer<String>() {
                @Override
                public void accept(String downloadUrl) {
                    updateProfile(profile, newUsername, newAbout, downloadUrl, callback);
                }
            });
        } else {
            Log.i("EDITUSER", "INPUTSTREAM NULL");
            // If inputStream is null, update without uploading picture
            updateProfile(profile, newUsername, newAbout, null, callback);
        }

    }

    //updates the profile of the user
    private void updateProfile(Profile profile, String newUsername, String newAbout, String downloadUrl, Consumer<Void> callback) {
        Map<String, Object> updates = convertProfile(profile);
        AtomicBoolean unique = new AtomicBoolean(false);

        db.collection(USERS).whereEqualTo(PROFILE_NAME, newUsername).get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (queryDocumentSnapshots.isEmpty()) {
                        unique.set(true);
                    }
                }
            );

        if(unique.get()){
            updates.put(FirestoreHelper.PROFILE_NAME, newUsername);
        }
        if (downloadUrl != null) {
            updates.put(FirestoreHelper.PROFILE_PFP, downloadUrl);
        }
        updates.put(FirestoreHelper.PROFILE_ABOUT, newAbout);
        db.collection(USERS).document(profile.getId()).update(updates)
                .addOnSuccessListener(aVoid -> {
                    Log.d("SUCCESS: ", "Profile Updated");
                    callback.accept(null);
                })
                .addOnFailureListener(e -> {
                    Log.e("FAIL: ", e.getMessage());
                });
    }



    //updates the post
    public void editPost(String postId, String newTitle, String newBody){
        getPost(postId, post -> {
            if (post != null) {
                Map<String, Object> updates = new HashMap<>();
                updates.put(POST_TITLE, newTitle);
                updates.put(POST_BODY, newBody);

                db.collection(POSTS).document(postId)
                        .update(updates)
                        .addOnSuccessListener(aVoid -> {
                            Log.e("SUCCESS: ", "Post Updated");
                        })
                        .addOnFailureListener(e -> {
                            Log.e("FAIL: ", e.getMessage());
                        });
            }
        });
    }

    //gets the post item from postId
    public void getPost(String postId, Consumer<Post> callback) {
        db.collection(POSTS).document(postId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    Post post = documentSnapshot.getData() == null ? null : convertMapToPost(documentSnapshot.getData());
                    if (post != null) {
                        getAndSaveProfile(post.getProfile().getId(), profile -> {
                            post.setProfile(profile);
                            retrieveVote(AuthHelper.getInstance().getProfile(), post, vote -> {
                                post.setUserVote(vote);
                                callback.accept(post);
                            });
                        });
                    } else {
                        callback.accept(null);
                    }
                });
    }

}



