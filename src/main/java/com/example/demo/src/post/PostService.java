package com.example.demo.src.post;

import com.example.demo.config.BaseException;
import com.example.demo.config.secret.Secret;
import com.example.demo.src.post.model.*;
import com.example.demo.src.user.model.PatchUserReq;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;

@Service
public class PostService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final PostDao postDao;
    private final PostProvider postProvider;
    private final JwtService jwtService;


    @Autowired
    public PostService(PostDao postDao, PostProvider postProvider, JwtService jwtService) {
        this.postDao = postDao;
        this.postProvider = postProvider;
        this.jwtService = jwtService;

    }

    public PostPostsRes createPosts(int userIdx, PostPostsReq postPostsReq) throws BaseException{

        try{
            int postIdx = postDao.insertPosts(userIdx, postPostsReq.getContent());
            // 게시물의 이미지는 리스트로 넣어줘야한다. 그렇기 때문에 반복문을 사용을 해서 이미지를 하나씩 DB에 저장해주겠다.
            for(int i=0;i<postPostsReq.getPostImgUrls().size();i++){
                postDao.insertPostImgs(postIdx, postPostsReq.getPostImgUrls().get(i));
            }
            return new PostPostsRes(postIdx);
        } catch(Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

}
