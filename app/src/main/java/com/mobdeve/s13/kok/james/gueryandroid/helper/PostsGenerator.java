package com.mobdeve.s13.kok.james.gueryandroid.helper;

import com.mobdeve.s13.kok.james.gueryandroid.helper.CommentGenerator;
import com.mobdeve.s13.kok.james.gueryandroid.model.Post;
import com.mobdeve.s13.kok.james.gueryandroid.model.Profile;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class PostsGenerator {
//      public static Post POST_1 = new Post("Eldoria", new Profile("SDFSDFDF"), LocalDateTime.now(), "Fuck you", "Bitch");
    public static Post POST_1 = new Post("Eldoria",
            new Profile("MysterySeeker88"),
            LocalDateTime.of(2022, 2, 22, 2, 22),
            "Unveiling Hidden Secrets in the Realm of 'Eldoria",

            "Greetings fellow adventurers!\n" +
            "\n" +
            "I've been diving deep into the mystical world of 'Eldoria,' and I'm on a quest to uncover its hidden mysteries. Have you stumbled upon any elusive secrets, easter eggs, or hidden pathways within the game? Share your findings and let's collaborate to unveil the enigmas that this captivating realm holds. Happy exploring! \uD83D\uDDFA️\uD83D\uDD0D #EldoriaMysteries #GamerQuests");

    public static Post POST_2 = new Post("Eternal Realms",
            new Profile("DungeonDelver87"),
            LocalDateTime.of(2023, 3, 23, 2, 23),
            "Need Help with Boss Fight in \"Eternal Realms\"",
            "Hey, fellow adventurers!\n" +
                    "\n" +
                    "I've hit a major roadblock in \"Eternal Realms\" – the boss fight in the Shadowed Cavern. No matter what I try, I keep getting wiped out. I've leveled up my character and tried different strategies, but nothing seems to work. I'd appreciate any advice or tips from those who have conquered this boss. What's the best gear setup, skills, or tactics to beat it? Your insights could save me from going insane! Thanks in advance for your help. Happy gaming, everyone!"
            );
    public static Post POST_3 = new Post("Realm Warriors",
            new Profile("CoopSeeker88"),
            LocalDateTime.of(2023, 3, 23, 2, 23),
            "In Search of a Team for Co-op Adventures in \"Realm Warriors\"",
            "Hey fellow warriors,\n" +
                    "\n" +
                    "I'm on the lookout for fellow adventurers to join me in the epic battles of \"Realm Warriors.\" Let's team up, strategize, and conquer the challenging quests together! Whether you're a seasoned warrior or a newcomer looking to make your mark, let's unite our strengths and achieve victory as a team. Drop your gamertags below or shoot me a message. Let's forge our legacy in the realm!\n" +
                    "\n" +
                    "See you on the battlefield!");

    static{
        POST_1.reply(CommentGenerator.POST_1_COMMENT_1);
        POST_1.reply(CommentGenerator.POST_1_COMMENT_2);
    }
    public static ArrayList<Post> generatePosts(){
        ArrayList<Post> ret = new ArrayList<>();
        ret.add(POST_1);
        ret.add(POST_2);
        ret.add(POST_3  );
        return ret;
    }
}
