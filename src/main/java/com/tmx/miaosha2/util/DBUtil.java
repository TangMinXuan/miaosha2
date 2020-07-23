package com.tmx.miaosha2.util;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.Properties;

public class DBUtil {
    private static Properties props;

    static {
        try {
            InputStream in = DBUtil.class.getClassLoader().getResourceAsStream("application.properties");
            props = new Properties();
            props.load(in);
            in.close();
        }catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static Connection getConn() throws Exception{
        String url = props.getProperty("spring.datasource.url");
        String username = props.getProperty("spring.datasource.username");
        String password = props.getProperty("spring.datasource.password");
        String driver = props.getProperty("spring.datasource.driver-class-name");
        Class.forName(driver);
        return DriverManager.getConnection(url,username, password);
    }

    public static void main(String[] args) throws Exception {
        Connection conn = DBUtil.getConn();
        String sql = "delete from miaosha_order;\n" +
                "ALTER TABLE miaosha_order AUTO_INCREMENT= 1;\n" +
                "update goods set goods_stock = 10;";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.execute();
		pstmt.close();
		conn.close();
    }
}
