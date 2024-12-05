import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Array;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class BackEnd extends ServerResourceAccessible {
    List<User> usersList = new ArrayList<>();
    List<List<Post>> totalPost = new ArrayList<>();

    BackEnd() {
        File dir = new File(getServerStorageDir());
        File[] files = dir.listFiles();

        for (File file : files) {
            Scanner sc = new Scanner(file + "/password.txt");
            String userId = file.getName();
            String password = sc.nextLine();
            usersList.add(new User(userId, password));

            File postDir = new File(file + "/post");
            File[] postFiles = postDir.listFiles();
            List<Post> posts = new ArrayList<>();

            for (File post : postFiles) {
                try {
                    sc = new Scanner(post);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                int indexDot = post.getName().lastIndexOf(".");
                int id = Integer.parseInt(post.getName().substring(0, indexDot));
                LocalDateTime dateTime = Post.parseDateTimeString(sc.nextLine(), Post.formatter);
                String title = sc.nextLine();
                String advertising = sc.nextLine();
                String numLike = sc.nextLine();
                int indexSpace = numLike.lastIndexOf(" ");
                int likeNum = Integer.parseInt(numLike.substring(indexSpace + 1));
                String content = new String();
                while (sc.hasNext()) {
                    content += sc.nextLine() + "\n";
                }
                sc.close();

                posts.add(new Post(id, dateTime, advertising, likeNum, title, content));
            }
            totalPost.add(posts);
        }
    }

    public String getUserID(User user) {
        return user.id;
    }
}
