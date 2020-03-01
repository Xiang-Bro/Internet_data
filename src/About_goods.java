
import Utils.JDBCUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class About_goods extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws UnsupportedEncodingException {

        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html;charset=UTF-8");
        String what_do = req.getParameter("what_do");
        try {
            if(what_do.equals("main")) {
                suggest_Goods(req,resp);
            } else if(what_do.equals("detail")){
                String id = req.getParameter("id");
                detail(req,resp,id);
            } else if(what_do.equals("search")) {
                String option = req.getParameter("option");
                search(option,req,resp);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }

    //显示主页的商品
    public void suggest_Goods(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html;charset=UTF-8");
        Connection conn = null;
        Statement st = null;
        ResultSet rs = null;

        //注册驱动
        conn = JDBCUtil.getConn();
        //获得对象与数据库进行交互
        st = conn.createStatement();
        String sql = "select * from goods order by number desc";
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
            jsonObj.put("address",rs.getString("address"));
            jsonObj.put("number",rs.getString("number"));
            jsonObj.put("buy",rs.getString("buy"));

            jsonArray.put(jsonObj);
        }
        PrintWriter out = resp.getWriter();
        out.println(jsonArray);

    }
    //显示商品的详情
    public void detail(HttpServletRequest req, HttpServletResponse resp,String id) throws Exception {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html;charset=UTF-8");
        Connection conn = null;
        Statement st = null;
        ResultSet rs = null;

        //注册驱动
        conn = JDBCUtil.getConn();
        //获得对象与数据库进行交互
        st = conn.createStatement();
        String sql = "select * from goods where id = " + id;
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
            jsonObj.put("address",rs.getString("address"));
            jsonObj.put("number",rs.getString("number"));
            jsonObj.put("buy",rs.getString("buy"));

            jsonArray.put(jsonObj);
        }
        PrintWriter out = resp.getWriter();
        out.println(jsonArray);
    }
    //显示查找的商品
    public void search(String option, HttpServletRequest req, HttpServletResponse resp) throws Exception {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html;charset=UTF-8");
        Connection conn = null;
        Statement st = null;
        ResultSet rs = null;

        //注册驱动
        conn = JDBCUtil.getConn();
        //获得对象与数据库进行交互
        st = conn.createStatement();
        //select * from goods where address = '北京' or type = '北京'
        //select * from goods where address like '%北京%' or type like '%北京%'
        String sql = "select * from goods where address like '%" + option + "%' or type like '%" + option +"%'";
        System.out.println("条件查询 === " + sql);
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
            jsonObj.put("address",rs.getString("address"));
            jsonObj.put("number",rs.getString("number"));
            jsonObj.put("buy",rs.getString("buy"));

            jsonArray.put(jsonObj);
        }
        PrintWriter out = resp.getWriter();
        out.println(jsonArray);
    }

}
