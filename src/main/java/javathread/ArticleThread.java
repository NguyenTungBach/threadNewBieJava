package javathread;

import com.t2012e.util.ConnectionHelper;
import com.t2012e.util.DateTimeUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ArticleThread extends Thread{
    private String url;
    private String title;
    private String description;
    private String content;
    private String thumbnail;
    private Date createAt;
    private Date updateAt;
    private int status;

    public ArticleThread() {
        this.title= " ";
        this.content= " ";
        this.description= " ";
        this.thumbnail= " ";
        this.createAt = Calendar.getInstance().getTime();
        this.updateAt = Calendar.getInstance().getTime();
        this.status = 0;
    }

    public ArticleThread(String url, String title, String description, String content, String thumbnail, Date createAt, Date updateAt, int status) {
        this.url = url;
        this.title = title;
        this.description = description;
        this.content = content;
        this.thumbnail = thumbnail;
        this.createAt = createAt;
        this.updateAt = updateAt;
        this.status = status;
    }

    public ArticleThread(String url) {
        this.url = url;
        this.title= " ";
        this.content= " ";
        this.description= " ";
        this.thumbnail= " ";
        this.createAt = Calendar.getInstance().getTime();
        this.updateAt = Calendar.getInstance().getTime();
        this.status = 0;
    }

//    private static List<ArticleThread> articleThreads = new ArrayList<>();
    private static int count = 0;
    @Override
    public void run() {
        // parse thông tin vào các trường
        testRun();
    }

    public synchronized void testRun(){
        try {
            String url = getUrl();
            ArticleThread article = new ArticleThread(url);
            Document documentCurrent = Jsoup.connect(url).get();
            String title = documentCurrent.select("h1.title-detail,div.section-inner").first().text();
            String content = documentCurrent.select("article.fck_detail,div.section-inner").first().text();
            article.setTitle(title);
            article.setContent(content);
            insertArticle(article);
//            articleThreads.add(article);
            System.out.printf("%d - %s\n",count++, article.getTitle());
        } catch (IOException e) {
            e.printStackTrace();
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }

    public Date getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(Date updateAt) {
        this.updateAt = updateAt;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getStringCreateAt() {
        return DateTimeUtil.formatDateToString(createAt);
    }

    public String getStringUpdateAt() {
        return DateTimeUtil.formatDateToString(updateAt);
    }

    static void insertArticle(ArticleThread article) throws SQLException {
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

    static synchronized void findAllTitle() {
        List<ArticleThread> listAll = new ArrayList<>();
        int count = 0;
        try {
            Connection cnn = ConnectionHelper.getConnection();
            if (cnn == null){
                throw new SQLException("Can't open connection!");
            }

            PreparedStatement preparedStatement = cnn.prepareStatement("select title from articles");
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()){
//                String url1 = rs.getString("url");
                String title1 = rs.getString("title");
//                String description1 = rs.getString("description");
//                String content1 = rs.getString("content");
//                String thumbnail1 = rs.getString("thumbnail");
//                Date createdAt1 =  DateTimeUtil.parseDateString(rs.getString("createAt"));
//                Date updateAt1 = DateTimeUtil.parseDateString(rs.getString("updateAt"));
//                int status1 = rs.getInt("status");
//                ArticleThread obj = new ArticleThread(url1,title1,description1,content1,thumbnail1,createdAt1,updateAt1,status1);
                System.out.printf("%d - %s\n",count++, rs.getString("title") );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
