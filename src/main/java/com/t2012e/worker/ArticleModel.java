package com.t2012e.worker;

import com.t2012e.util.ConnectionHelper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ArticleModel {
    public boolean insert(Article article){
        try {
            Connection cnn = ConnectionHelper.getConnection();
            if (cnn == null){
                System.err.println("Can not open conection to database");
                return false;
            }
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
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println(e.getMessage());
        }
        return false;
    }
}
