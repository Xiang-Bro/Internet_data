package Use;

import Utils.JDBCUtil;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class User {
    public static void main(String[] args) throws Exception {
        Connection conn = null;
        Statement st = null;
        ResultSet rs = null;

        //注册驱动
        conn = JDBCUtil.getConn();
        //获得对象与数据库进行交互
        st = conn.createStatement();
        String sql = "select * from user";
        //用来接收查询结果
        rs = st.executeQuery(sql);

        while (rs.next()){
            String phone = rs.getString("phone");
            String password = rs.getString("password");
            System.out.println(phone + " === " + password);
        }

        JDBCUtil.release(conn,st,rs);
    }
}
