import Utils.JDBCUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class About_user extends HttpServlet {

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws UnsupportedEncodingException {

        req.setCharacterEncoding("UTF-8");
        String choose = req.getParameter("choose");
        String phone = req.getParameter("phone");
        String password = req.getParameter("password");
        System.out.println("密码是 === " + password);
        System.out.println(phone + " === " + password + " --- " + choose);
        String back;
        try {
            if(choose.equals("登录")){

                if(isExist(phone,password)) {
                    back = "true";
                }
                else {
                    back = "false";
                }
                System.out.println(phone + " === " + password + " --- " + back);
                resp.getOutputStream().print(back);

            } else if(choose.equals("确认修改")) {
                if(canUpdate(phone,password)){
                    back = "true";
                }else {
                    back = "false";
                }
                System.out.println(phone + " === " + password + " --- " + back);
                resp.getOutputStream().print(back);
            } else {
                if(canInsert(phone,password)){
                    back = "true";
                }else {
                    back = "false";
                }
                System.out.println(phone + " === " + password + " --- " + back);
                resp.getOutputStream().print(back);
            }

        }catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req,resp);
    }

    public boolean isExist(String phone,String password) throws Exception {
        Connection conn = null;
        Statement st = null;
        ResultSet rs = null;

        //注册驱动
        conn = JDBCUtil.getConn();
        //获得对象与数据库进行交互
        st = conn.createStatement();
        String sql = "select * from user where phone = " + phone + " and password = '" + password + "'";
        //用来接收查询结果
        rs = st.executeQuery(sql);

        if (rs.next()){
            return true;
        }else {
            System.out.println(sql);
            return false;
        }
    }
    public boolean canUpdate(String phone,String password) throws Exception {
        Connection conn = null;
        Statement st = null;

        int result = 0;
        //注册驱动
        conn = JDBCUtil.getConn();
        //获得对象与数据库进行交互
        st = conn.createStatement();

        String sql = "update user set password = '" + password + "' where phone = " + phone;
        System.out.println("准备修改密码 === " + sql);
        //用来接收查询结果
        result = st.executeUpdate(sql);

        if (result > 0) {
            return true;
        } else {
            return false;
        }
    }
    
    public boolean canInsert(String phone,String password) throws Exception {
        Connection conn = null;
        Statement st = null;
        int result = 0;
        //注册驱动
        conn = JDBCUtil.getConn();
        //获得对象与数据库进行交互
        st = conn.createStatement();

        String sql = "insert into user values( " + phone + ",'" + password + "')";
        //用来接收查询结果
        result = st.executeUpdate(sql);
        System.out.println("添加新用户 === " + sql);

        if (result > 0) {
            return true;
        } else {
            return false;
        }
    }
}
