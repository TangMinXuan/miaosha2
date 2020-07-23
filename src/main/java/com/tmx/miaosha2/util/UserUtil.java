package com.tmx.miaosha2.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.tmx.miaosha2.DAO.DO.UserDO;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserUtil {

    private static List<UserDO> createUser(int count) throws Exception{
        List<UserDO> users = new ArrayList<UserDO>(count);
        //生成用户
        String firstAddSalt = MD5Util.fixedAddSalt("123456");
        String passwordInDB = MD5Util.addSalt(firstAddSalt, 15);
        for(int i=0; i<count; i++) {
            UserDO user = new UserDO();
            user.setUserId(100L+i);
            String email = "test" + user.getUserId() + "@temp.com";
            user.setUserEmail(email);
            user.setUserSalt(15);
            user.setUserPassword(passwordInDB);
            users.add(user);
        }
        System.out.println("create user finish");

		return users;
    }

    private static void insertIntoDB(List<UserDO> users) throws Exception {
        //插入数据库
		Connection conn = DBUtil.getConn();
		String sql = "insert into user(user_id, user_Email, user_password, user_salt)values(?, ?, ?, ?)";
		PreparedStatement pstmt = conn.prepareStatement(sql);
		for(int i=0; i<users.size(); i++) {
			UserDO user = users.get(i);
			pstmt.setLong(1, user.getUserId());
			pstmt.setString(2, user.getUserEmail());
			pstmt.setString(3, user.getUserPassword());
			pstmt.setInt(4, user.getUserSalt());
			pstmt.addBatch();
		}
		pstmt.executeBatch();
		pstmt.close();
		conn.close();
		System.out.println("insert to db");
    }

    private static void createToken(List<UserDO> users) throws IOException {
        //登录，生成token
        String urlString = "http://localhost:8080/user/login";
        File file = new File("D:/tokens.txt");
        if(file.exists()) {
            file.delete();
        }
        RandomAccessFile raf = new RandomAccessFile(file, "rw");
        file.createNewFile();
        raf.seek(0);
        for(int i=0; i<users.size(); i++) {
            UserDO user = users.get(i);
            URL url = new URL(urlString);
            HttpURLConnection co = (HttpURLConnection)url.openConnection();
            co.setRequestMethod("POST");
            co.setDoOutput(true);
            OutputStream out = co.getOutputStream();
            String params = "email="+user.getUserEmail()+"&password="+MD5Util.fixedAddSalt("123456");
            out.write(params.getBytes());
            out.flush();
            InputStream inputStream = co.getInputStream();
            ByteArrayOutputStream bout = new ByteArrayOutputStream();
            byte buff[] = new byte[1024];
            int len = 0;
            while((len = inputStream.read(buff)) >= 0) {
                bout.write(buff, 0 ,len);
            }
            inputStream.close();
            bout.close();
            String response = new String(bout.toByteArray());
            JSONObject jo = JSON.parseObject(response);
            String token = jo.getString("data");
            System.out.println("create token : " + user.getUserId());

            String row = user.getUserId()+","+token;
            raf.seek(raf.length());
            raf.write(row.getBytes());
            raf.write("\r\n".getBytes());
            System.out.println("write to file : " + user.getUserId());
        }
        raf.close();

        System.out.println("over");
    }

    public static void main(String[] args)throws Exception {
        List<UserDO> users = createUser(500);
//        insertIntoDB(users);
        createToken(users);
    }
}
