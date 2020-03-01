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

public class About_order extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html;charset=UTF-8");
        String what_do = req.getParameter("what_do");
        String user = req.getParameter("user");
        String id = req.getParameter("good_id");
        try {
            if(what_do.equals("show")) {
                //展示待收货信息
                show_receive(user,req,resp);
            } else if(what_do.equals("show_buy")) {
                //展示待付款信息
                show_buy(user,req,resp);
            } else if(what_do.equals("toPingjia")) {
                delete_receive(id,user);
            } else if(what_do.equals("show_ping")) {
                show_ping(req,resp,user);
            } else if(what_do.equals("cancel")) {
                cancel(id,user);
            } else if(what_do.equals("now_buy")) {
                buyToRece(id,user);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req,resp);
    }

    //展示待收货订单
    public void show_receive(String user, HttpServletRequest req, HttpServletResponse resp) throws Exception {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html;charset=UTF-8");
        Connection conn = null;
        Statement st = null;
        ResultSet rs = null;

        //注册驱动
        conn = JDBCUtil.getConn();
        //获得对象与数据库进行交互
        st = conn.createStatement();
        /*select goods.id,goods.describe,goods.img,goods.price,wait_rece.number
        from goods,wait_rece where goods.id = wait_rece.id and wait_rece.user = 15733037053*/
        String sql = "select goods.id,goods.describe,goods.img,goods.price,wait_rece.number from goods,wait_rece " +
                " where goods.id = wait_rece.id and wait_rece.user = " + user;
        //用来接收查询结果
        rs = st.executeQuery(sql);
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
    //两个方法可以优化，传参数判断 展示待支付订单
    public void show_buy(String user, HttpServletRequest req, HttpServletResponse resp) throws Exception {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html;charset=UTF-8");
        Connection conn = null;
        Statement st = null;
        ResultSet rs = null;

        //注册驱动
        conn = JDBCUtil.getConn();
        //获得对象与数据库进行交互
        st = conn.createStatement();
        /*select goods.id,goods.describe,goods.img,goods.price,wait_rece.number
        from goods,wait_rece where goods.id = wait_rece.id and wait_rece.user = 15733037053*/
        String sql = "select goods.id,goods.describe,goods.img,goods.price,wait_buy.number from goods,wait_buy " +
                " where goods.id = wait_buy.id and wait_buy.user = " + user;
        System.out.println("显示待付款界面 === " + sql);
        //用来接收查询结果
        rs = st.executeQuery(sql);
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
    //确认收货之后将该订单从 待收货 表中删除，并将该条数据添加到待评价 表中
    public void delete_receive(String id, String user) throws Exception {
        Connection conn = null;
        Statement st = null;

        //注册驱动
        conn = JDBCUtil.getConn();
        //获得对象与数据库进行交互
        st = conn.createStatement();
        //insert into wait_pingjia select * from wait_rece where id = 8 and user = 15733037053
        String in_sql = "insert into wait_pingjia select * from wait_rece where id = " + id + " and user = " + user;
        st.executeUpdate(in_sql);
        String sql = "delete from wait_rece where id = " + id + " and user = " + user;
        System.out.println("确认收货，删除订单 === " + sql);
        st.executeUpdate(sql);

    }
    //展示待评价订单
    public void show_ping(HttpServletRequest req, HttpServletResponse resp,String user) throws Exception {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html;charset=UTF-8");

        Connection conn = null;
        Statement st = null;
        ResultSet rs = null;

        //注册驱动
        conn = JDBCUtil.getConn();
        //获得对象与数据库进行交互
        st = conn.createStatement();
        /*select goods.id,goods.describe,goods.img,goods.price,wait_pingjia.number
        from goods,wait_pingjia where goods.id = wait_pingjia.id and wait_pingjia.user = 15733037053*/
        String sql = "select goods.id,goods.describe,goods.img,goods.price,wait_pingjia.number from goods,wait_pingjia " +
                        " where goods.id = wait_pingjia.id and wait_pingjia.user = " + user;
        System.out.println("准备展示待评价的商品 === " + sql);
        //用来接收查询结果
        rs = st.executeQuery(sql);
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
    //取消订单
    public void cancel(String id, String user) throws Exception {
        Connection conn = null;
        Statement st = null;
        ResultSet rs = null;

        //注册驱动
        conn = JDBCUtil.getConn();
        //获得对象与数据库进行交互
        st = conn.createStatement();
        String sql = "delete from wait_buy where id = " + id + " and user = " + user;
        System.out.println(" 取消待支付的订单 === " + sql);
        st.executeUpdate(sql);
    }
    //支付订单，添加到待收货表中
    public void buyToRece(String id, String user) throws Exception {
        Connection conn = null;
        Statement st = null;
        ResultSet rs = null;

        //注册驱动
        conn = JDBCUtil.getConn();
        //获得对象与数据库进行交互
        st = conn.createStatement();
        String sql = "insert into wait_rece select * from wait_buy where id = " + id + " and user = " + user;
        System.out.println("支付待付款的订单加入待收货表中 === " + sql);
        st.executeUpdate(sql);
        //然后从 待付款表 中删除该订单
        cancel(id,user);
    }

}
