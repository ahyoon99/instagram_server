package com.example.demo.src.auth;

import com.example.demo.src.auth.model.PostLoginReq;
import com.example.demo.src.auth.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

@Repository
public class AuthDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public User getpwd(PostLoginReq postLoginReq){
        String getPwdQuery = "select userIdx, nickName, name, email, pwd from User where email = ?";
        String getPwdParams = postLoginReq.getEmail();

        System.out.println("getPwdParams : "+getPwdParams);

        return this.jdbcTemplate.queryForObject(getPwdQuery,
                (rs, rowNum)-> new User(
                        rs.getInt("userIdx"),
                        rs.getString("name"),
                        rs.getString("nickName"),
                        rs.getString("email"),
                        rs.getString("pwd")
                ),
                getPwdParams);
    }
}
