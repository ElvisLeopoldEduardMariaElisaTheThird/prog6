import java.sql.*;

public class testing {

    public static void main(String[] args) {
        try {
            // Загрузка драйвера
            Class.forName("org.postgresql.Driver");

            // Создание соединения с базой данных
            String url = "jdbc:postgresql://localhost:5432/postgres";
            String user = "postgres";
            String password = "qwerty";
            Connection conn = DriverManager.getConnection(url, user, password);

            // Выполнение запроса
            //Statement stmt = conn.createStatement();

            //ResultSet rs = stmt.executeQuery("select * from datatable");


            String sql = "insert into workers(id, name, coordinates, creationdate, salary, startdate, enddate, position, organization) values (3,'jhon','3 0.0','2023-05-11',3,'2023-05-11T05:23:31.207831700','2023-05-11T05:23:31.207831700+03:00[Europe/Moscow]','MANAGER','3 COMMERCIAL')";
            Statement rs = conn.prepareStatement(sql);

            // Обработка результатов запроса
            //while (rs.next()) {
                //System.out.println(rs.getString("PersonID")+" "+rs.getString("name"));
            //}

            //System.out.println(rs);

            // Закрытие соединения
            rs.close();
            //stmt.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}