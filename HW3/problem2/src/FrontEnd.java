import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Array;
import java.time.LocalDateTime;
import java.util.*;

public class FrontEnd {
    private UserInterface ui;
    private BackEnd backend;
    private User user;
    String[] idAndPw;

    public FrontEnd(UserInterface ui, BackEnd backend) {
        this.ui = ui;
        this.backend = backend;
    }
    
    public boolean auth(String authInfo){
        idAndPw = authInfo.split("\n");
        if(idAndPw.length == 2){
            if (!idAndPw[0].isBlank() && !idAndPw[1].isBlank()) {
                File file =
                        new File(backend.getServerStorageDir()
                                + idAndPw[0] + "/password.txt");

                Scanner sc = null;
                try {
                    sc = new Scanner(file);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    return false;
                }
                String realPw = sc.nextLine();

                if (realPw.equals(idAndPw[1])) {
                    user = new User(idAndPw[0], idAndPw[1]);
                    return true;
                }
            }
        }
        return false;
    }

    public void post(List<String> titleContentList) {
        String title = titleContentList.get(0);
        String advertising = titleContentList.get(1);
        String contents = titleContentList.get(2);

        Post post = new Post(title, contents);
        post.setId(setFileId());

        List<String> fileContents = new ArrayList<>();
        fileContents.add(post.getDate());
        fileContents.add(title);
        fileContents.add(advertising);
        fileContents.add("like-number " + post.getLikeNum() + "\n");
        fileContents.add(contents);

        String fileName = backend.getServerStorageDir()
                + idAndPw[0] + "/post/" + post.getId() + ".txt";
        try {
            FileWriter fileWriter = new FileWriter(fileName);
            for(String string: fileContents){
                fileWriter.write(string + "\n");
            }
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int setFileId(){
        File dir = new File(backend.getServerStorageDir());
        File[] files = dir.listFiles();

        int maxId = 0;
        for(File file: files){
            File txtDir = new File(file + "/post");
            File[] txtFiles = txtDir.listFiles();

            for(File txtFile: txtFiles){
                int index = txtFile.getName().lastIndexOf(".");
                String txtName = txtFile.getName().substring(0, index);
                if(maxId < Integer.parseInt(txtName)){
                    maxId = Integer.parseInt(txtName);
                }

            }
        }
        return maxId + 1;
    }

    public void recommend(int N){
        List<User> friendsList = makeFriendsList();
        List<List<Post>> classified = classifyByAd(friendsList);
        List<Post> advertised = classified.get(0);
        List<Post> nonAdvertised = classified.get(1);

        advertised = sortPostByDate(advertised);
        nonAdvertised = sortPostByDate(nonAdvertised);
        Collections.reverse(advertised);
        Collections.reverse(nonAdvertised);

        printRecommend(N, advertised, nonAdvertised);
    }

    public List<User> makeFriendsList() {
        List<User> friendsList = new ArrayList<>();
        File friendsFile = new File(backend.getServerStorageDir()
                + idAndPw[0] + "/friend.txt");
        try {
            Scanner sc = new Scanner(friendsFile);
            while(sc.hasNext()){
                String friendName = sc.nextLine();
                for(User user: backend.usersList){
                    if(friendName.equals(backend.getUserID(user)))
                        friendsList.add(user);
                }
            } sc.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return friendsList;
    }

    public List<List<Post>> classifyByAd(List<User> friendsList) {
        List<List<Post>> nonClassified = new ArrayList<>();
        for(User friend: friendsList){
            int index = backend.usersList.indexOf(friend);
            nonClassified.add(backend.totalPost.get(index));
        }

        List<List<Post>> classified = new ArrayList<>();
        List<Post> advertised = new ArrayList<>();
        List<Post> nonAdvertised = new ArrayList<>();

        for(List<Post> posts: nonClassified){
            for(Post post: posts){
                if(post.getAdvertising().equals("yes")){
                    advertised.add(post);
                } else {
                    nonAdvertised.add(post);
                }
            }
        }
        classified.add(advertised);
        classified.add(nonAdvertised);
        return classified;
    }

    public void printRecommend(int N, List<Post> advertised, List<Post> nonAdvertised){
        if(advertised.size() >= N){
            for(int i=0; i<N; i++){
               ui.println(advertised.get(i).toString());
            }
        } else {
            for(Post post: advertised){
                ui.println(post.toString());
            }
            for(int i=0; i<N-advertised.size(); i++){
                ui.println(nonAdvertised.get(i).toString());
            }
        }
    }

    public void search(String command) {
        String[] commandSlices = command.split(" ");
        if(commandSlices.length != 1){
            if(commandSlices[1].matches("-?\\d+")){
                System.out.println("Wrong Command.");
            } else if(!commandSlices[commandSlices.length-1].matches("-?\\d+")){
                System.out.println("Wrong Command.");
            } else {
                String[] keywords = Arrays.copyOfRange(commandSlices, 1, commandSlices.length-1);
                LinkedHashSet<String> linkedHashSet =
                        new LinkedHashSet<>(Arrays.asList(keywords));
                keywords = linkedHashSet.toArray(new String[] {});

                List<String> keywordsList = new ArrayList<>();
                for(String keyword: keywords){
                    keywordsList.add(keyword);
                }

                int fq = Integer.parseInt(commandSlices[commandSlices.length-1]);
                List<Post> filtered = new ArrayList<>();

                for(List<Post> posts: backend.totalPost){
                    filtered.addAll(filterPost(posts, keywordsList, fq));
                }

                List<List<Post>> classified = classifyByLike(filtered);
                List<Post> userLiked = sortPostByLikeNum(classified.get(0));
                List<Post> nonUserLiked = sortPostByLikeNum(classified.get(1));

                printSearch(userLiked, nonUserLiked);
            }
        }
    }
    public void printSearch(List<Post> userLiked, List<Post> nonUserLiked){
        if(userLiked.size() != 0 || nonUserLiked.size() != 0){
            if(userLiked.size() >= 10){
                for(int i=0; i<10; i++){
                    ui.println(userLiked.get(i).getSummary());
                }
            } else {
                for(Post post: userLiked){
                    ui.println(post.getSummary());
                }
                for(int i=0; i<10-userLiked.size(); i++){
                    if(i == nonUserLiked.size()){
                        break;
                    }
                    ui.println(nonUserLiked.get(i).getSummary());
                }
            }
        }
    }

    public List<Post> filterPost(List<Post> searched, List<String> keywordsList, int fq){
        List<Post> filtered = new ArrayList<>();
        for(Post post: searched){
            filtered.add(post);
            String[] postTitle = post.getTitle().split(" ");
            String[] postContent = post.getContent().split(" ");
            List<String> postContentList = new ArrayList<>();
            postContentList.addAll(List.of(postContent));

            List<String> temp = new ArrayList<>();
            List<String> removed = new ArrayList<>();

            for(String word: postContentList){
                if(word.contains("\n")){
                    String[] split = word.split("\n");
                    temp.add(split[0]);
                    temp.add(split[1]);
                    removed.add(word);
                }
            }
            postContentList.addAll(temp);
            postContentList.removeAll(removed);

            int postFq = 0;
            for(String keyword: keywordsList){
                for(String titleWord: postTitle){
                    if(titleWord.equals(keyword)){
                        postFq += 1;
                    }
                } for(String contentWord: postContentList){
                    if(contentWord.equals(keyword)){
                        postFq += 1;
                    }
                }
            }
            if(postFq < fq){
                filtered.remove(post);
            }
        }
        return filtered;
    }
    public List<List<Post>> classifyByLike(List<Post> filtered){
        List<List<Post>> classified = new ArrayList<>();
        List<Post> userLiked = new ArrayList<>();
        List<Post> nonUserLiked = new ArrayList<>();

        File file = new File(backend.getServerStorageDir() +
                idAndPw[0] + "/likedposts.txt");
        Scanner sc = null;
        try {
            sc = new Scanner(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        List<Integer> LikedPostId = new ArrayList<>();
        while(sc.hasNext()){
            String likePosts = sc.nextLine();
            String[] likePostsId = likePosts.split(" ");
            for(int i=1; i<likePostsId.length; i++){
                LikedPostId.add(Integer.parseInt(likePostsId[i]));
            }
        } sc. close();

        for(Post post: filtered){
            if(LikedPostId.contains(post.getId())){
                userLiked.add(post);
            } else {
                nonUserLiked.add(post);
            }
        }

        classified.add(userLiked);
        classified.add(nonUserLiked);
        return classified;
    }

    public List<Post> sortPostByDate(List<Post> posts){
        Collections.sort(posts, new Comparator<Post>() {
            @Override
            public int compare(Post p1, Post p2) {
                String s1 = "";
                String s2 = "";

                s1 = p1.getDate() + "";
                s2 = p2.getDate() + "";

                return s1.compareTo(s2);
            }
        });
        return posts;
    }
    public List<Post> sortPostByLikeNum(List<Post> posts){
        Collections.sort(posts, new Comparator<Post>() {
            @Override
            public int compare(Post p1, Post p2) {
                if(p1.getLikeNum() > p2.getLikeNum()){
                    return -1;
                } else if(p1.getLikeNum() < p2.getLikeNum()){
                    return 1;
                } else {
                    String s1 = "";
                    String s2 = "";

                    s1 = p1.getDate() + "";
                    s2 = p2.getDate() + "";

                    return s2.compareTo(s1);
                }
            }
        });
        return posts;
    }
    User getUser(){
        return user;
    }
}

