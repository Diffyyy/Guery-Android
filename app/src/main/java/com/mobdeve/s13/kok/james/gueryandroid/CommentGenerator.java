package com.mobdeve.s13.kok.james.gueryandroid;

import java.time.LocalDateTime;

public class CommentGenerator {
    public static final Comment POST_1_COMMENT_1 =  new Comment(new Profile("CuriousGamer22"), LocalDateTime.now(),
            "Hey there, MysterySeeker88! I absolutely love 'Eldoria' and have been exploring every nook and cranny. Recently, I discovered a hidden cave with rare artifacts. Exciting times in the world of Eldoria! 🏞️💎");

    // Create a reply to the first comment
    public static final Comment POST_1_COMMENT_1_REPLY_1 = new Comment(new Profile("AdventureEnthusiast"), LocalDateTime.now(),
            "CuriousGamer22, that sounds amazing! Could you share the location of the hidden cave? I'd love to check it out too! 🗺️🕵️‍♂️");


    public static final Comment POST_1_COMMENT_2 = new Comment(new Profile("EnigmaHunter"), LocalDateTime.now(),
            "I'm thrilled to join this quest! Eldoria is a truly mystical realm, and I've discovered a hidden cave filled with ancient artifacts. Let's team up to uncover more secrets together!");

    // Create a reply
    public static final Comment Post_1_Comment_2_REPLY_1 = new Comment(new Profile("EldoriaExplorer"), LocalDateTime.now(),
            "That's amazing, EnigmaHunter! I've also found a hidden pathway leading to a hidden village. Let's share our discoveries and explore further!");


    static{
        POST_1_COMMENT_1.replies.add(POST_1_COMMENT_1_REPLY_1);
        POST_1_COMMENT_2.replies.add(Post_1_Comment_2_REPLY_1);
    }

}
