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
import java.sql.Statement;

public class About_car extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html;charset=UTF-8");
        String what_do = req.getParameter("what_do");
        try {
            String user = req.getParameter("user");
            if(user != "") {
                if (what_do.equals("insert")) {
                    String id = req.getParameter("id");
                    String number = req.getParameter("number");
                    insert(id, user, number);
                } else if (what_do.equals("delete")) {
                    delete();
                } else {
                    search(req, resp, user);
                }
            } else {
                System.out.println("出现了错误，传过来的who === " + user);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req,resp);
    }

    //加入购物车
    public void insert(String id, String user,String number) throws Exception {
        Connection conn = null;
        Statement st = null;
        ResultSet rs = null;

        //注册驱动
        conn = JDBCUtil.getConn();
        //获得对象与数据库进行交互
        st = conn.createStatement();
        String has_sql = "select number from shop_car where user = " + user + " and id = " + id;
        rs = st.executeQuery(has_sql);
        String has_number = null;
        if(rs.next()) {
            has_number = rs.getString("number");//获取数据库中已添加该商品的数量
            int before_number = Integer.parseInt(has_number);//由于数据库字段设置购买数量为varchar类型，因此首先转换为整型
            int insert_number = Integer.parseInt(number);//传过来要购买的数量
            int new_number = before_number + insert_number;//两者相加和成总数量
            String sql = "update shop_car set number = " + new_number + " where id = " + id + " and user = " + user;
            //update shop_car set number = 4 where id = 1 and user = 15733037053
            System.out.println("加入购物车时看看这个商品有没有 === " + sql);
            //用来接收查询结果
            st.executeUpdate(sql);
        } else {
            String sql = "insert into shop_car (id,number,user) values ( " + id + "," + number + "," + user + ")";
            //用来接收查询结果
            st.executeUpdate(sql);
            System.out.println("该用户之前购物车中没有该商品，所以加入 === " + sql);
        }
    }
    //显示购物车
    public void search(HttpServletRequest req, HttpServletResponse resp,String user) throws Exception {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html;charset=UTF-8");
        Connection conn = null;
        Statement st = null;
        ResultSet rs = null;

        //注册驱动
        conn = JDBCUtil.getConn();
        //获得对象与数据库进行交互
        st = conn.createStatement();
        /*
            select goods.id,goods.img,goods.describe,goods.price,shop_car.number
            from goods,shop_car where goods.id = shop_car.id and shop_car.user = 15733037053
        */
        String sql = "select goods.id,goods.img,goods.describe,goods.price,shop_car.number from goods,shop_car where goods.id = shop_car.id"
                        + " and shop_car.user = " + user;
        System.out.println("显示购物车信息 === " + sql);
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
    //删除购物车中加入订单的商品
    public void delete() throws Exception {
        Connection conn = null;
        Statement st = null;

        //注册驱动
        conn = JDBCUtil.getConn();
        //获得对象与数据库进行交互
        st = conn.createStatement();
        /*delete from shop_car where exists (select * from buy_order
                where shop_car.id = buy_order.id and shop_car.user = buy_order.user);*/
        String sql = "delete from shop_car where exists (select * from buy_order " +
                " where shop_car.id = buy_order.id and shop_car.user = buy_order.user)";
        st.executeUpdate(sql);
        System.out.println("删除购物车中加入订单的商品 === " + sql);

    }
}
