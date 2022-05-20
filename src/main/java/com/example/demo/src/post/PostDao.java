package com.example.demo.src.post;

import com.example.demo.src.post.model.*;
import com.example.demo.src.user.model.PatchUserReq;
import com.example.demo.src.user.model.PostUserReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class PostDao {

    private JdbcTemplate jdbcTemplate;
    private List<GetPostImgRes> getPostImgRes;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public List<GetPostsRes> selectPosts(int userIdx){
        String selectPostsQuery =
                "SELECT p.postIdx as postIdx,\n" +
                        "\tu.userIdx as userIdx,\n" +
                        "\tu.nickName as nickName,\n" +
                        "\tu.profileImgUrl as profileImgUrl,\n" +
                        "\tp.content as content,\n" +
                        "\tIF(postLikeCount is null, 0, postLikeCount) as postLikeCount,\n" +
                        "\tIF(commentCount is null, 0, commentCount) as commentCount,\n" +
                        "\tcase\n" +
                        "\t\twhen timestampdiff(second, p.updatedAt, current_timestamp) < 60\n" +
                        "\t\tthen concat(timestampdiff(second, p.updatedAt, current_timestamp), '초 전')\n" +
                        "\t\twhen timestampdiff(minute , p.updatedAt, current_timestamp) < 60\n" +
                        "\t\tthen concat(timestampdiff(minute, p.updatedAt, current_timestamp), '분 전')\n" +
                        "\t\twhen timestampdiff(hour , p.updatedAt, current_timestamp) < 24\n" +
                        "\t\tthen concat(timestampdiff(hour, p.updatedAt, current_timestamp), '시간 전')\n" +
                        "\t\twhen timestampdiff(day , p.updatedAt, current_timestamp) < 365\n" +
                        "\t\tthen concat(timestampdiff(day, p.updatedAt, current_timestamp), '일 전')\n" +
                        "\t\telse timestampdiff(year , p.updatedAt, current_timestamp)\n" +
                        "\tend as updatedAt,\n" +
                        "\tIF(pl.status = 'ACTIVE', 'Y', 'N') as likeOrNot\n" +
                        "FROM Post as p\n" +
                        "\tjoin User as u on u.userIdx = p.userIdx\n" +
                        "\tleft join (select receivedPostIdx, givingUserIdx, count(heartIdx) as postLikeCount from Heart WHERE status = 'ACTIVE' group by receivedPostIdx) plc on plc.receivedPostIdx = p.postIdx\n" +
                        "\tleft join (select postIdx, count(commentIdx) as commentCount from Comment WHERE status = 'ACTIVE' group by postIdx) c on c.postIdx = p.postIdx\n" +
                        "\tleft join Follow as f on f.followeeIdx = p.userIdx and f.status = 'ACTIVE'\n" +
                        "\tleft join Heart as pl on pl.givingUserIdx = f.followerIdx and pl.receivedPostIdx = p.postIdx\n" +
                        "WHERE f.followerIdx = ? and p.status = 'ACTIVE'\n" +
                        "group by p.postIdx;";

        int selectPostsParam = userIdx;
        return this.jdbcTemplate.query(selectPostsQuery,
                (rs, rowNum) -> new GetPostsRes(
                        rs.getInt("postIdx"),
                        rs.getInt("userIdx"),
                        rs.getString("nickName"),
                        rs.getString("profileImgUrl"),
                        rs.getString("content"),
                        rs.getInt("postLikeCount"),
                        rs.getInt("commentCount"),
                        rs.getString("updatedAt"),
                        rs.getString("likeOrNot"),
                        getPostImgRes = this.jdbcTemplate.query(
                                " SELECT pi.postImgUrlIdx, pi.imgUrl \n" +
                                        " FROM PostImgUrl as pi\n" +
                                        " join Post as p on p.postIdx=pi.postIdx\n" +
                                        " WHERE pi.status = 'ACTIVE' and p.postIdx = ?",
                                (rk, rownum) -> new GetPostImgRes(
                                        rk.getInt("postImgUrlIdx"),
                                        rk.getString("imgUrl")
                                ), rs.getInt("postIdx")
                        )
                ), selectPostsParam);
    }

    public int checkUserExist(int userIdx){
        String checkUserExistQuery = "select exists(select userIdx from User where userIdx = ?)";
        int checkUserExistParams = userIdx;
        return this.jdbcTemplate.queryForObject(checkUserExistQuery,
                int.class,
                checkUserExistParams);

    }

}