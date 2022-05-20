package com.example.demo.src.user;


import com.example.demo.src.user.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class UserDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

//    public List<GetUserRes> getUsers(){
//        String getUsersQuery = "select userIdx,name,nickName,email from User";
//        return this.jdbcTemplate.query(getUsersQuery,
//                (rs,rowNum) -> new GetUserRes(
//                        rs.getInt("userIdx"),
//                        rs.getString("name"),
//                        rs.getString("nickName"),
//                        rs.getString("email")
//                ));
//    }

    // 유저의 정보를 출력해준다.
    public GetUserInfoRes selectUserInfo(int userIdx){
        String selectUsersInfoQuery = "SELECT u.userIdx as userIdx, u.nickName as nickName, u.name as name, u.profileImgUrl as profileImgUrl, u.website as website, u.introduce as introduce,If(postCount is null, 0, postCount) as postCount,If(followerCount is null, 0, followerCount) as followerCount, If(followeeCount is null, 0, followeeCount) as followingCount\n" +
                "FROM User as u\n" +
                "        left join (select userIdx, count(postIdx) as postCount from Post WHERE status = 'ACTIVE' GROUP BY userIdx) p on p.userIdx = u.userIdx\n" +
                "        left join (select followerIdx, count(followIdx) as followerCount from Follow WHERE status = 'ACTIVE' GROUP BY followerIdx) fc on fc.followerIdx = u.userIdx\n" +
                "        left join (select followeeIdx, count(followIdx) as followeeCount from Follow WHERE status = 'ACTIVE' GROUP BY followeeIdx) f on f.followeeIdx = u.userIdx\n" +
                "WHERE u.userIdx =? and u.status = 'ACTIVE';";
        int selectUserInfoParam = userIdx;
        return this.jdbcTemplate.queryForObject(selectUsersInfoQuery,
                (rs,rowNum) -> new GetUserInfoRes(
                        rs.getInt("userIdx"),
                        rs.getString("nickName"),
                        rs.getString("name"),
                        rs.getString("profileImgUrl"),
                        rs.getString("website"),
                        rs.getString("introduce"),
                        rs.getInt("followerCount"),
                        rs.getInt("followingCount"),
                        rs.getInt("postCount")
                ),selectUserInfoParam);
    }

    // 유저의 정보 아래에 있는 게시물 리스트를 리턴해준다.
    public List<GetUserPostsRes> selectUserPosts(int userIdx){
        String selectUserPostsQuery = "SELECT p.postIdx as postIdx, pi.imgUrl as postImgUrl\n" +
                "FROM Post as p\n" +
                "\tjoin PostImgUrl as pi on pi.postIdx = p.postIdx and pi.status = 'ACTIVE'\n" +
                "\tjoin User as u on u.userIdx = p.userIdx\n" +
                "\tWHERE p.status = 'ACTIVE' and u.userIdx = ?\n" +
                "GROUP BY p.postIdx\n" +
                "HAVING min(pi.postImgUrlIdx)\n" +
                "ORDER BY p.postIdx;";
        int selectUserPostsParam = userIdx;
        return this.jdbcTemplate.query(selectUserPostsQuery,
                (rs,rowNum) -> new GetUserPostsRes(
                        rs.getInt("postIdx"),
                        rs.getString("postImgUrl")
                ), selectUserPostsParam);
    }

    // userIdx가 유효한 Idx 값인지 확인해준다.
    public int checkUserExist(int userIdx){
        String checkUserExistQuery = "select exists(select userIdx from User where userIdx = ?)";
        int checkUserExistParams = userIdx;
        return this.jdbcTemplate.queryForObject(checkUserExistQuery,
                int.class,
                checkUserExistParams);

    }

//    public GetUserRes getUsersByEmail(String email){
//        String getUsersByEmailQuery = "select userIdx,name,nickName,email from User where email=?";
//        String getUsersByEmailParams = email;
//        return this.jdbcTemplate.queryForObject(getUsersByEmailQuery,
//                (rs, rowNum) -> new GetUserRes(
//                        rs.getInt("userIdx"),
//                        rs.getString("name"),
//                        rs.getString("nickName"),
//                        rs.getString("email")),
//                getUsersByEmailParams);
//    }


//    public GetUserRes getUsersByIdx(int userIdx){
//        String getUsersByIdxQuery = "select userIdx,name,nickName,email from User where userIdx=?";
//        int getUsersByIdxParams = userIdx;
//        return this.jdbcTemplate.queryForObject(getUsersByIdxQuery,
//                (rs, rowNum) -> new GetUserRes(
//                        rs.getInt("userIdx"),
//                        rs.getString("name"),
//                        rs.getString("nickName"),
//                        rs.getString("email")),
//                getUsersByIdxParams);
//    }

//    public int createUser(PostUserReq postUserReq){
//        String createUserQuery = "insert into User (name, nickName, phone, email, password) VALUES (?,?,?,?,?)";
//        Object[] createUserParams = new Object[]{postUserReq.getName(), postUserReq.getNickName(),postUserReq.getPhone(), postUserReq.getEmail(), postUserReq.getPassword()};
//        this.jdbcTemplate.update(createUserQuery, createUserParams);
//
//        String lastInserIdQuery = "select last_insert_id()";
//        return this.jdbcTemplate.queryForObject(lastInserIdQuery,int.class);
//    }

//    public int checkEmail(String email){
//        String checkEmailQuery = "select exists(select email from User where email = ?)";
//        String checkEmailParams = email;
//        return this.jdbcTemplate.queryForObject(checkEmailQuery,
//                int.class,
//                checkEmailParams);
//
//    }

//    public int modifyUserName(PatchUserReq patchUserReq){
//        String modifyUserNameQuery = "update User set nickName = ? where userIdx = ? ";
//        Object[] modifyUserNameParams = new Object[]{patchUserReq.getNickName(), patchUserReq.getUserIdx()};
//
//        return this.jdbcTemplate.update(modifyUserNameQuery,modifyUserNameParams);
//    }


    public int deleteUserByIdx(int userIdx) {
        String deleteUserByIdxQuery = "update User set status = 'DELETED' where userIdx = ? ";

        return this.jdbcTemplate.update(deleteUserByIdxQuery,userIdx);
    }
}
