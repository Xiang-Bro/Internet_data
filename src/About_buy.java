import Utils.JDBCUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class About_buy extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("utf-8");
        resp.setContentType("text/html;charset=UTF-8");
        String what_do = req.getParameter("what_do");
        String id = req.getParameter("id");
        String number = req.getParameter("number");
        String user = req.getParameter("user");
        try {
            if(what_do.equals("plan_buy")) {
                plan_insert(id,number,user);
            } else if(what_do.equals("plan_delete")) {
                plan_delete(id,user);
            } else if(what_do.equals("copy")) {
                clear_copy();
            } else if(what_do.equals("clear")) {
                clear_plan(user);
            } else if(what_do.equals("wait_buy")) {
                copy_buy();
            } else if(what_do.equals("now_buy")){
                insert(id,number,user);
            } else {
                order_show(req,resp,user);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req,resp);
    }

    //加入准备购买订单
    public void plan_insert(String id, String number,String user) throws Exception {
        Connection conn = null;
        Statement st = null;

        //注册驱动
        conn = JDBCUtil.getConn();
        //获得对象与数据库进行交互
        st = conn.createStatement();
        String sql = "insert into buy_order(id,number,user) values(" + id + "," + number + "," + user + ")";
        st.executeUpdate(sql);
        System.out.println("加入到待购买订单 === " + sql);
    }
    //从准备购买订单中删除
    public void plan_delete(String id, String user) throws Exception {
        Connection conn = null;
        Statement st = null;

        //注册驱动
        conn = JDBCUtil.getConn();
        //获得对象与数据库进行交互
        st = conn.createStatement();
        String sql = "delete from buy_order where id = " + id + " and user = " + user;
        st.executeUpdate(sql);
        System.out.println("从待购买订单中删除 === " + sql);
    }
    //展示购买订单,等待支付
    public void order_show(HttpServletRequest req, HttpServletResponse resp,String user) throws Exception {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html;charset=UTF-8");

        Connection conn = null;
        Statement st = null;
        ResultSet rs = null;

        //注册驱动
        conn = JDBCUtil.getConn();
        //获得对象与数据库进行交互
        st = conn.createStatement();
        /*select goods.id,goods.img,goods.describe,goods.price,buy_order.number
        from goods,buy_order where buy_order.id = goods.id and buy_order.user = 15733037053*/
        String sql = "select goods.id,goods.img,goods.describe,goods.price,buy_order.number from goods,buy_order " +
                "where buy_order.id = goods.id and buy_order.user = " + user;
        System.out.println("准备展示要购买的商品 === " + sql);
        //用来接收查询结果
        rs = st.executeQuery(sql);
        System.out.println("展示要购买的商品 === " + sql);
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObj;
        while(rs.next()){
            jsonObj = new JSONObject();
            jsonObj.put("id",rs.getString("id"));
            jsonObj.put("img",rs.getString("img"));
            jsonObj.put("describe",rs.getString("describe"));
            jsonObj.put("price",rs.getString("price"));
            jsonObj.put("number",rs.getString("number"));

            jsonArray.put(jsonObj);
        }
        PrintWriter out = resp.getWriter();
        out.println(jsonArray);

    }
    //确认支付,将订单表中的数据复制到待收货表，然后清空
    public void clear_copy() throws Exception {
        Connection conn = null;
        Statement st = null;

        //注册驱动
        conn = JDBCUtil.getConn();
        //获得对象与数据库进行交互
        st = conn.createStatement();
        //首先复制信息
        String sql = "insert into wait_rece select * from buy_order";
        st.executeUpdate(sql);
        System.out.println("购买成功,复制数据 ===" + sql);
        //然后清空表
        String delete_sql = "delete from buy_order";
        st.executeUpdate(delete_sql);
        System.out.println("购买成功,清空数据 ===" + delete_sql);
    }
    //取消支付,将订单表中的数据复制到待付款表，然后清空 ----------和支付成功方法可以合并，优化传参数判断
    public void copy_buy() throws Exception {
        Connection conn = null;
        Statement st = null;

        //注册驱动
        conn = JDBCUtil.getConn();
        //获得对象与数据库进行交互
        st = conn.createStatement();
        //首先复制信息
        String sql = "insert into wait_buy select * from buy_order";
        st.executeUpdate(sql);
        System.out.println("购买失败,复制数据 ===" + sql);
        //然后清空表
        String delete_sql = "delete from buy_order";
        st.executeUpdate(delete_sql);
        System.out.println("购买失败,清空数据 ===" + delete_sql);
    }
    //注销登陆时清空准备购买订单
    public void clear_plan(String user) throws Exception {
        Connection conn = null;
        Statement st = null;

        //注册驱动
        conn = JDBCUtil.getConn();
        //获得对象与数据库进行交互
        st = conn.createStatement();

        String sql = "delete from buy_order where user = " + user ;
        st.executeUpdate(sql);
    }
    //立即购买，加入待购买订单
    public void insert(String id, String number, String user) throws Exception {
        Connection conn = null;
        Statement st = null;

        //注册驱动
        conn = JDBCUtil.getConn();
        //获得对象与数据库进行交互
        st = conn.createStatement();

        String sql = "insert into buy_order(id,number,user) values( " + id + "," + number + "," + user + ")";
        System.out.println("立即购买 === " + sql);
        st.executeUpdate(sql);
    }

}
