import com.t2012e.entity.Article;
import com.t2012e.util.ConnectionHelper;
import javathread.ArticleThread;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;


import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MainThread {

    public static void main(String[] args) {
//        Article article = new Article("url", "title", "description", "content", "thumbnail", 0);
//        try {
//            insertArticle(article);
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }

//        try {
//            Document document = Jsoup.connect("https://vnexpress.net/the-thao").get(); //vào chuyên mục thể thao
//            Elements element = document.select(".title-news a"); // Lấy ra tất cả các tiêu đề thẻ a
//            for (int i = 0; i < element.size(); i++) {
//                int getI = i;
//                Thread t = new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        try {
//                            Article article = new Article();
//                            article.setUrl(element.get(getI).attr("href"));
//
//                            Document documentCurrent = Jsoup.connect(element.get(getI).attr("href")).get();
//                            String title = documentCurrent.select("h1.title-detail").first().text();
//                            String content = documentCurrent.select("article.fck_detail").first().text();
//
//                            article.setTitle(title);
//                            article.setContent(content);
//                            insertArticle(article);
//                            System.out.printf("%d - %s\n",getI+1, title);
//                        }
//                        catch (SQLException e) {
//                            e.printStackTrace();
//                        }
//                        catch (IOException e){
//                            e.printStackTrace();
//                        }
//                    }
//                });
//                t.start();
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        long startTime = System.currentTimeMillis();
        try {
            Document document = Jsoup.connect("https://vnexpress.net/the-thao").get(); //vào chuyên mục thể thao
            Elements element = document.select(".title-news a"); // Lấy ra tất cả các tiêu đề thẻ a

            for (int i = 0; i < element.size(); i++) {
                String url = element.get(i).attr("href");
                ArticleThread t = new ArticleThread(url);
                t.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
//        List<ArticleThread> testlist = findAll();
//        for (int i = 0; i < testlist.size(); i++) {
//            ArticleThread test = testlist.get(i);
//            System.out.printf("%d - %s\n",i+1, test.getTitle());
//        }
//        findAll();
        long endTime = System.currentTimeMillis();
        System.out.printf(endTime - startTime + "mls\n");
    }

     static void insertArticle(Article article) throws SQLException {
        Connection cnn = ConnectionHelper.getConnection();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("insert into articles");
        stringBuilder.append(" ");
        stringBuilder.append("(url, title, description, content, thumbnail, createAt, updateAt, status)");
        stringBuilder.append(" ");
        stringBuilder.append("values");
        stringBuilder.append(" ");
        stringBuilder.append("(?,?,?,?,?,?,?,?)");

        PreparedStatement preparedStatement = cnn.prepareStatement(stringBuilder.toString());
        preparedStatement.setString(1,article.getUrl());
        preparedStatement.setString(2,article.getTitle());
        preparedStatement.setString(3,article.getDescription());
        preparedStatement.setString(4,article.getContent());
        preparedStatement.setString(5,article.getThumbnail());
        preparedStatement.setString(6,article.getStringCreateAt());
        preparedStatement.setString(7,article.getStringUpdateAt());
        preparedStatement.setInt(8,article.getStatus());
        preparedStatement.execute();
    }

    public static void findAll() {
        int count =0;
        List<ArticleThread> listAll = new ArrayList<>();
        try {
            Connection cnn = ConnectionHelper.getConnection();
            if (cnn == null){
                throw new SQLException("Can't open connection!");
            }

            PreparedStatement preparedStatement = cnn.prepareStatement("select title from articles");
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()){
//                String url = rs.getString("url");
//                String title = rs.getString("title");
//                String description = rs.getString("description");
//                String content = rs.getString("content");
//                String thumbnail = rs.getString("thumbnail");
//                java.util.Date createdAt =  DateTimeUtil.parseDateString(rs.getString("createAt"));
//                Date updateAt = DateTimeUtil.parseDateString(rs.getString("updateAt"));
//                int status = rs.getInt("status");
//                ArticleThread obj = new ArticleThread(url,title,description,content,thumbnail,createdAt,updateAt,status);
//                listAll.add(obj);
                System.out.printf("%d - %s\n",count++, rs.getString("title") );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
//        return listAll;
    }
}
