package com.example.servlet;

import com.example.entity.User;
import com.example.mapper.UserMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

@WebServlet(value = "/login", loadOnStartup = 1)
public class LoginServlet extends HttpServlet {
    SqlSessionFactory factory;
    @SneakyThrows
    @Override
    public void init() throws ServletException {
        factory = new SqlSessionFactoryBuilder().build(Resources.getResourceAsReader("mybatis-config.xml"));
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // <String, String[]>
//        req.getParameterMap().forEach((k, v) -> {
//            System.out.println(k + ": " + Arrays.toString(v));
//        });
        // set response type
        resp.setContentType("text/html;charset=UTF-8");
        //check form
        Map<String, String[]> map = req.getParameterMap();
        if (map.containsKey("username") && map.containsKey("password")) {
            String username = req.getParameter("username");
            String password = req.getParameter("password");

            try (SqlSession sqlSession = factory.openSession(true)) {
                UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
                User user = userMapper.getUser(username, password);

                if (user != null) {
                    resp.getWriter().write("User: " + username + " login successfully!");
                } else {
                    resp.getWriter().write("User non-exist or error password:(");
                }
            }
        } else {
            resp.getWriter().write("Error, the data of form is incomplete!");
        }
    }
}
