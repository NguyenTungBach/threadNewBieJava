package com.t2012e.worker;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class ArticleThread extends Thread{
    private String url;
    private Article article;

    public ArticleThread(String url) {
        this.url = url;
    }

    @Override
    public void run() {
        //parse thông tin vào các trường
        crawlData();
//        if (article.isValid()){
//            insertData();
//        } else {
//            System.err.println("Can not insert article. Reason: invalid article object");
//        }
    }

    private void crawlData() {
        System.out.printf("Crawl data form url %s \n", this.url);
        try {
            article = new Article();
            article.setUrl(url);
            Document document = Jsoup.connect(url).get();
            // Xử lý lấy tiêu đề tin theo selector từ trang vnexpress
            Element titleNode = document.selectFirst("h1.title-detail");
            // Kiểm tra tiêu đề node để lấy text bên trong set vào tiêu đề đối tượng article
            if (titleNode != null){
                String title = titleNode.text();
                article.setTitle(title);
            }
            // Xử lý lấy mô tả tin theo selector từ trang vnexpress
            Element descriptionElement = document.selectFirst("p.description");
            // Kiểm tra tiêu đề description để lấy text bên trong set vào tiêu đề đối tượng article
            if (descriptionElement != null){
                String description = descriptionElement.text();
                article.setDescription(description);
            }
            // Xử lý lấy nội dung tin theo selector từ trang vnexpress
            Element contentElement = document.selectFirst("article.fck_detail");
            // Kiểm tra tiêu đề content để lấy text bên trong set vào tiêu đề đối tượng article
            if (contentElement != null){
                String content = contentElement.text();
                article.setContent(content);
            }
            // Xử lý lấy ảnh selector từ trang vnexpress
            Element thumbnailElement = document.selectFirst("div.fig-picture picture img");
            // Kiểm tra tiêu đề thumbnail để lấy text bên trong set vào tiêu đề đối tượng article
            if (thumbnailElement != null){
                String thumbnail = thumbnailElement.attr("data-src");
                article.setThumbnail(thumbnail);
//                System.out.println(thumbnail);
            }else {
                article.setThumbnail("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRnKDnOnZUDol8y74_nu0cWooFGfMGCqJY-d7XW8IX_MMttUU0JfwhPbZhplrumFR_oGCU&usqp=CAU");
            }
            article.setStatus(1);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.printf("Error %s",e.getMessage());
        }
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Article getArticle() {
        return article;
    }

    public void setArticle(Article article) {
        this.article = article;
    }
}
