package com.t2012e.worker;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


import java.io.IOException;
import java.util.ArrayList;

public class DemoWorker {
    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();
        ArticleModel articleModel = new ArticleModel();
        ArrayList<String> listUrl = getListUrl(); // Lấy ra các danh sách url
        ArrayList<ArticleThread> listThread = new ArrayList<>();
        // Đưa danh sách url vào luồng, chạy 1 vòng lặp start tất cá các Thread lên
        for (int i = 0; i < listUrl.size(); i++) {
            ArticleThread articleThread = new ArticleThread(listUrl.get(i));
            listThread.add(articleThread);
            articleThread.start();
        }
        // Sau đó yêu cầu main Thread phải chờ tất cả Thread dừng lại
        // Trong quá trình chạy luồng, đảm bảo thread đã hoàn thành ArticleThread.add() lấy ra hết tất cả danh sách ArticleThread
        for (int i = 0; i < listThread.size(); i++) {
            try {
                listThread.get(i).join(); // = articleThread.join, join sẽ yêu cầu luồng chính là chạy xong cái này rồi mới đc chạy tiếp
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        // Đưa danh sách ArticleThread lên database
//        for (int i = 0; i < listThread.size(); i++) {
//            articleModel.insert(listThread.get(i).getArticle());
//        }
        long endTime = System.currentTimeMillis();
        System.out.printf(endTime - startTime + "mls\n");
    }

    private static ArrayList<String> getListUrl() {
        ArrayList<String> list = new ArrayList<>();
        try {
            Document document = Jsoup.connect("https://vnexpress.net/the-thao").get();
            Elements elements = document.select(".title-news a");// Lấy ra tất cả các tiêu đề thẻ a
            if (elements.size() > 0 ){
                for (int i = 0; i < elements.size(); i++) {
                    list.add(elements.get(i).attr("href"));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.printf("Got %d links\n", list.size());
        return list;
    }
}
