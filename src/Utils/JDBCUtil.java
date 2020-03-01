package Utils;
import java.sql.*;
//连接数据库的工具类
public class JDBCUtil {
    //创建连接对象
    public static Connection getConn() {
        Connection conn = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            //参数一：协议+访问的数据库  参数二：用户名  参数三：密码
            conn = DriverManager.getConnection("jdbc:mysql://localhost/andriod_data","root","130423");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return conn;
    }
    //释放资源
    public static void release(Connection conn, Statement st, ResultSet rs) {
        closeConn(conn);
        closeRs(rs);
        closeSt(st);
    }
    public static void closeRs(ResultSet rs) {
        try {
            if(rs != null) {
                rs.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            rs = null;
        }
    }
    public static void closeSt(Statement st) {
        try {
            if(st != null) {
                st.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            st = null;
        }
    }
    public static void closeConn(Connection conn) {
        try {
            if(conn != null) {
                conn.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            conn = null;
        }
    }

}
