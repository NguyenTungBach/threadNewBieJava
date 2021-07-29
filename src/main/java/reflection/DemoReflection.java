package reflection;

import com.t2012e.util.ConnectionHelper;
import reflection.myannotaion.Id;
import reflection.myannotaion.Table;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Parameter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class DemoReflection {
    public static void main(String[] args) {
        Employee employee = new Employee();
        employee.setName("Test Employee");
        employee.setId(1);
        employee.setAddress("asdc");
        employee.setPhone("0123456");

        Customer customer = new Customer();
        customer.setName("Test Customer");
        customer.setIdentityNumber("A001");
        customer.setBalance(200);
        customer.setEmail("khach@gmail.com");
        try {
//            save(employee);
            System.out.println("\n");
//            save(customer);
            migrateData(Employee.class);
            migrateData(Customer.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Tạo bảng theo class tương ứng
     * thực hiện câu lệnh: CREATE TABLE MyGuets(
     * id INT(6) UNSIGNED AUTO_INCREMENT PRIMARY KEY,
     * firstname VARCHAR(200) NOT NULL,
     * lastname VARCHAR(200) NOT NULL
     * )
      */

    private static void migrateData(Class clazz) throws SQLException {
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

        Connection cnn = ConnectionHelper.getConnection();
        Statement stt = cnn.createStatement();
        stt.execute(sqlQueryBuilder.toString());
    }


    private static void save(Object obj) throws SQLException, IllegalAccessException {
            }

}
