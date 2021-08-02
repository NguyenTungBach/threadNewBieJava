package reflection.generic;

import com.t2012e.util.ConnectionHelper;
import org.jsoup.select.Evaluator;
import reflection.Customer;
import reflection.myannotaion.Id;
import reflection.myannotaion.Table;

import javax.swing.text.html.parser.Entity;
import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MasterModel <T>{
    private Class <T> test;

    // kiểm tra xem nó có phải là 1 Entity
    public MasterModel(Class<T> clazz) {
            if (!clazz.isAnnotationPresent(Table.class)) {
                System.err.printf("Class %s không được đăng ký làm việc với database.", clazz.getSimpleName());
                return;
            }
            this.test = clazz;
        }

    public void migrateData (Class clazz){
        String tableName = clazz.getSimpleName(); // default = class name
        if (!clazz.isAnnotationPresent(Table.class)){
            System.out.println("Class không được đánh dấu để mapping với database");
            return;
        }
        // nếu có annotation @Table thì lấy tên ra
        Table table = (Table) clazz.getAnnotation(Table.class);
        if (!table.name().isEmpty()){
            tableName = table.name();
        }

        StringBuilder sqlQueryBuilder = new StringBuilder();
        sqlQueryBuilder.append("CREATE TABLE ");
        sqlQueryBuilder.append(tableName);
        sqlQueryBuilder.append("(");
        Field[] fields = clazz.getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            sqlQueryBuilder.append(fields[i].getName());
            sqlQueryBuilder.append(" ");
            boolean isPrimarykey = fields[i].isAnnotationPresent(Id.class);
            if (fields[i].getType().getSimpleName().equals("int")){
                sqlQueryBuilder.append("INT");
            } else if (fields[i].getType().getSimpleName().equals("String")){
                sqlQueryBuilder.append("VARCHAR(200)");
            } else if (fields[i].getType().getSimpleName().equals("double")){
                sqlQueryBuilder.append("DOUBLE");
            }
            if (isPrimarykey){
                Id id = fields[i].getAnnotation(Id.class);
                if (id.autoIncrement()){
                    sqlQueryBuilder.append(" AUTO_INCREMENT");
                }
                sqlQueryBuilder.append(" PRIMARY KEY");
            }
            sqlQueryBuilder.append(", ");
        }
        sqlQueryBuilder.setLength(sqlQueryBuilder.length() - 2);
        sqlQueryBuilder.append(")");
        System.out.println(sqlQueryBuilder.toString());

        try {
            Connection cnn = ConnectionHelper.getConnection();
            Statement stt = cnn.createStatement();
            stt.execute(sqlQueryBuilder.toString());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void save(T obj){
        try {
            Class clazz = obj.getClass();
        /*
        System.out.println(clazz.getSimpleName());
        System.out.println(clazz.getName());
        Field[] fields = clazz.getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            Field currentField = fields[i];
            currentField.setAccessible(true); // vì vấn đề bảo mật, lấy giá trị của trường
            try {
                System.out.println(currentField.getType() + " "+ currentField.getName() + " : "+currentField.get(obj));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        Constructor[] constructors = clazz.getDeclaredConstructors();

        for (int i = 0; i < constructors.length; i++) {
            Constructor currentConstructor = constructors[i];
            System.out.println("Tên Constructor thứ "+ i +": " + currentConstructor.getName());
            Parameter[] parameters = currentConstructor.getParameters();
            for (int j = 0; j < parameters.length; j++) {
                Parameter currentParameter = parameters[j];
                System.out.println("Tham số " + currentParameter.getName() + ", kiểu dữ liệu " +currentParameter.getType());
            }
        }
        */

            //1. Kết nối đến database
            Connection cnn = ConnectionHelper.getConnection();
//            StringBuilder stringBuilder = new StringBuilder();
//        stringBuilder.append("insert into articles");
//        stringBuilder.append(" ");
//        stringBuilder.append("(url, title, description, content, thumbnail, createAt, updateAt, status)");
//        stringBuilder.append(" ");
//        stringBuilder.append("values");
//        stringBuilder.append(" ");
//        stringBuilder.append("(?,?,?,?,?,?,?,?)");
//        PreparedStatement preparedStatement = cnn.prepareStatement(stringBuilder.toString());
            String tableName = clazz.getSimpleName();
            // nếu có annotation @Table thì lấy tên ra
            Table table = (Table) clazz.getAnnotation(Table.class);
            if (!table.name().isEmpty()){
                tableName = table.name();
            }

            Field[] fields = clazz.getDeclaredFields();
            StringBuilder fieldNames = new StringBuilder();
            fieldNames.append("(");
            for (int i = 0; i < fields.length; i++) {
                fieldNames.append(fields[i].getName());
                fieldNames.append(", ");
            }
            fieldNames.setLength(fieldNames.length() - 2);
            fieldNames.append(")");
//        fieldNames = fieldNames.substring(0, fieldNames.length() -2);
//        fieldNames += ")";
            System.out.println(fieldNames);

            StringBuilder fieldValues = new StringBuilder();
            fieldValues.append("(");
            for (int i = 0; i < fields.length; i++) {
                Field currentField = fields[i];
                currentField.setAccessible(true);
                if (currentField.getType().getSimpleName().equals("String")){
                    fieldValues.append("'");
                    fieldValues.append(currentField.get(obj));
                    fieldValues.append("', ");
//                fieldValues += "'" + fields[i].get(obj) +"', ";
                } else {
//                fieldValues += fields[i].get(obj) +",";
                    fieldValues.append(currentField.get(obj));
                    fieldValues.append(",");
                }
            }
            fieldValues.setLength(fieldValues.length() - 2);
            fieldValues.append(")");
//        fieldValues = fieldValues.substring(0, fieldValues.length() -2);
//        fieldValues += ")";
            System.out.println(fieldValues);

            //2. Tạo câu lệnh truy vấn
//            String sqlQuery = String.format("insert into %s"+
//                    "%s "+
//                    "values %s", tableName, fieldNames, fieldValues);
//        StringBuilder sqlQuery = new StringBuilder();
//        sqlQuery.append("insert into ");
//        sqlQuery.append(tableName);
//        sqlQuery.append(" ");
//        sqlQuery.append(fieldNames);
//        sqlQuery.append(" ");
//        sqlQuery.append("values");
//        sqlQuery.append(" ");
//        sqlQuery.append("(");
//        for (int i = 0; i < fields.length; i++) {
//            sqlQuery.append("?,");
//        }
//        sqlQuery.setLength(sqlQuery.length() - 1);
//        sqlQuery.append(")");
//            System.out.println(sqlQuery);
            StringBuilder sqlQuery = new StringBuilder();
            sqlQuery.append("insert into ");
            sqlQuery.append(tableName);
            sqlQuery.append(" ");
            sqlQuery.append(fieldNames);
            sqlQuery.append(" ");
            sqlQuery.append("values");
            sqlQuery.append(" ");
            sqlQuery.append("(");
            for (int i = 0; i < fields.length; i++) {
                sqlQuery.append("?,");
            }
            sqlQuery.setLength(sqlQuery.length() - 1);
            sqlQuery.append(")");
            System.out.println(sqlQuery);
            //3. Gửi câu lệnh vào database
//            stt.execute(sqlQuery);
            PreparedStatement preparedStatement = cnn.prepareStatement(sqlQuery.toString());
            for (int i = 0; i < fields.length; i++) {
                Field currentField = fields[i];
                currentField.setAccessible(true);
//            System.out.println(".........");
//            System.out.println(currentField.getType().getSimpleName());
//            System.out.println(currentField.get(obj));
//            System.out.println(".........");
                if (currentField.getType().getSimpleName().equals("String")){
//                String checkSetString = String.format("%s",currentField.get(obj));
                    StringBuilder checkSetString = new StringBuilder();
                    checkSetString.append(currentField.get(obj));
//                System.out.println(i+1 + " "+ checkSetString);
                    preparedStatement.setString(i+1, checkSetString.toString());
                }
                else if (currentField.getType().getSimpleName().equals("double")){
                    double checkSetDouble = (double) (currentField.get(obj));
//                System.out.println(i+1 + " "+ checkSetDouble);
                    preparedStatement.setDouble(i+1, checkSetDouble);
                }
                else if (currentField.getType().getSimpleName().equals("int")){
                    int checkSetInt = (int) currentField.get(obj);
                    preparedStatement.setInt(i+1, checkSetInt);
                }
            }
            preparedStatement.execute();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public List<T> findAll(Class clazz){
        List<T> listAll = new ArrayList<>();
        Connection cnn = null;
        try {
            cnn = ConnectionHelper.getConnection();
            if (cnn == null){
                throw new SQLException("Can't open connection!");
            }
            String tableName = clazz.getSimpleName();
            // nếu có annotation @Table thì lấy tên ra
            Table table = (Table) clazz.getAnnotation(Table.class);
            if (!table.name().isEmpty()){
                tableName = table.name();
            }
            System.out.println(tableName);
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("select * from ");
            stringBuilder.append(tableName);

            System.out.println(stringBuilder);
            Field[] fields = clazz.getDeclaredFields();
            PreparedStatement preparedStatement = cnn.prepareStatement(stringBuilder.toString());
                ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()){
                T tObj = test.newInstance();
                int numColumns = rs.getMetaData().getColumnCount();
                for (int i = 0; i < numColumns; i++) {
                    Field currentFieldAdd = fields[i];
                    currentFieldAdd.setAccessible(true);
                    System.out.println(fields[i].getName());

                    System.out.println(i + " " + rs.getMetaData().getColumnTypeName(i+1) + " " + rs.getMetaData().getColumnName(i+1) + " = " + rs.getObject(i+1) );
                    // Cần đưa vào 1 đối tượng cụ thể vào đó ví dụ như Customer customers = new Customer();
                    // cho listAll.add();// Cụ thể đối tượng cần đưa ở đây là T
                    if(rs.getMetaData().getColumnName(i+1).equalsIgnoreCase(fields[i].getName())){
                        currentFieldAdd.set(tObj,rs.getObject(i+1));
                    }

                }
                System.out.println("\n");
                listAll.add(tObj);
            }
        } catch (SQLException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        System.out.println("list All");
        for (int i = 0; i < listAll.size(); i++) {
            System.out.println(listAll.get(i).toString());
        }
        return listAll;
    }

//    public T findById(Id id){
//        Connection cnn = null;
//        try {
//            cnn = ConnectionHelper.getConnection();
//            if (cnn == null){
//                throw new SQLException("Can't open connection!");
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        String tableName = clazz.getSimpleName();
//        Field[] fields = clazz.getDeclaredFields();
//
//        for (int i = 0; i < fields.length; i++) {
//            boolean isPrimarykey = fields[i].isAnnotationPresent(Id.class);
//            if (isPrimarykey){
//
//            }
//        }
//        // nếu có annotation @Table thì lấy tên ra
//        Table table = (Table) clazz.getAnnotation(Table.class);
//        if (!table.name().isEmpty()){
//            tableName = table.name();
//        }
//        StringBuilder stringBuilder = new StringBuilder();
//        stringBuilder.append("select * from ");
//        stringBuilder.append(tableName);
//        stringBuilder.append(" where id = ");
//        stringBuilder.append(" where id = ");
//        PreparedStatement preparedStatement = cnn.prepareStatement(String.format("select * from orders where id = '%s'",id));
//    }

    public void update (T obj){
        Class clazz = obj.getClass();
        try {
            Connection cnn = ConnectionHelper.getConnection();
            if (cnn == null){
                throw new SQLException("Can't open connection!");
            }
            String tableName = clazz.getSimpleName();
            // nếu có annotation @Table thì lấy tên ra
            Table table = (Table) clazz.getAnnotation(Table.class);
            if (!table.name().isEmpty()){
                tableName = table.name();
            }
            
            String idName = null;
            Table id = (Table) clazz.getAnnotation(Table.class);
            if (!table.name().isEmpty()){
                idName = id.nameId();
            }

            String idValue = null;
            Field[] fields = clazz.getDeclaredFields();
//            for (int i = 0; i < fields.length; i++) {
//                Field currentField = fields[i];
//                currentField.setAccessible(true);
//                if (currentField.getName().equals(idName)) {
//                    try {
//                        idValue = (String) currentField.get(obj);
//                        System.out.println(currentField.get(obj));
//                    } catch (IllegalAccessException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }

            System.out.println("............");
            System.out.println("Tên các trường");
            for (int i = 0; i < fields.length; i++) {
                Field currentField = fields[i];
                System.out.println(currentField.getName());
            }
            System.out.println("............");

            System.out.println("............");
            System.out.println("Giá trị các trường");
            for (int i = 0; i < fields.length; i++) {
                Field currentField = fields[i];
                currentField.setAccessible(true);

                    System.out.println(currentField.get(obj));

            }
            System.out.println("............");

            System.out.println("tableName = " +tableName);
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("update ");
            stringBuilder.append(tableName);
            stringBuilder.append(" set ");
            for (int i = 0; i < fields.length; i++) {
                Field currentField = fields[i];
                stringBuilder.append(currentField.getName());
                stringBuilder.append(" = ");
                if (currentField.getType().getSimpleName().equals("String")){
                    stringBuilder.append("'");
                    stringBuilder.append(currentField.get(obj));
                    stringBuilder.append("', ");
                } else {
                    stringBuilder.append(currentField.get(obj));
                    stringBuilder.append(", ");
                }
            }
            stringBuilder.setLength(stringBuilder.length() - 2);
            stringBuilder.append(" ");
            stringBuilder.append("where ");
            stringBuilder.append(idName);
            stringBuilder.append(" = ");
            for (int i = 0; i < fields.length; i++) {
                Field currentField = fields[i];
                if (currentField.getName().equals(idName)){
                    if (currentField.getType().getSimpleName().equals("String")){
                        stringBuilder.append("'");
                        stringBuilder.append(currentField.get(obj));
                        stringBuilder.append("'");
                    } else {
                        stringBuilder.append(currentField.get(obj));
                    }
                }
            }
            System.out.println(stringBuilder);
            PreparedStatement preparedStatement = cnn.prepareStatement(stringBuilder.toString());
            preparedStatement.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public void delete (T obj){
        Class clazz = obj.getClass();
        try {
            Connection cnn = ConnectionHelper.getConnection();
            if (cnn == null){
                throw new SQLException("Can't open connection!");
            }
            String tableName = clazz.getSimpleName();
            // nếu có annotation @Table thì lấy tên ra
            Table table = (Table) clazz.getAnnotation(Table.class);
            if (!table.name().isEmpty()){
                tableName = table.name();
            }
            System.out.println(tableName);

            Field[] fields = clazz.getDeclaredFields();
            System.out.println("............");
            System.out.println("Tên các trường");
            for (int i = 0; i < fields.length; i++) {
                Field currentField = fields[i];
                System.out.println(currentField.getName());
            }
            System.out.println("............");

            System.out.println("Giá trị các trường");
            for (int i = 0; i < fields.length; i++) {
                Field currentField = fields[i];
                currentField.setAccessible(true);
                System.out.println(currentField.get(obj));
            }
            System.out.println("............");

            String idName = null;
            Table id = (Table) clazz.getAnnotation(Table.class);
            if (!table.name().isEmpty()){
                idName = id.nameId();
            }

            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("delete from ");
            stringBuilder.append(tableName);
            stringBuilder.append(" where ");
            stringBuilder.append(idName);
            stringBuilder.append(" = ");
            for (int i = 0; i < fields.length; i++) {
                Field currentField = fields[i];
                if (currentField.getName().equals(idName)){
                    if (currentField.getType().getSimpleName().equals("String")){
                        stringBuilder.append("'");
                        stringBuilder.append(currentField.get(obj));
                        stringBuilder.append("'");
                    } else {
                        stringBuilder.append(currentField.get(obj));
                    }
                }
            }
            System.out.println(stringBuilder);
            PreparedStatement preparedStatement = cnn.prepareStatement(stringBuilder.toString());
            preparedStatement.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
