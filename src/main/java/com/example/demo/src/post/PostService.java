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

    public void modifyPost(int userIdx, int postIdx, PatchPostsReq patchPostsReq) throws BaseException{

        // 먼저 존재하는 유저인지 확인해준다.
        if(postProvider.checkUserExist(userIdx)==0){
            throw new BaseException(USERS_EMPTY_USER_ID);
        }
        // 존재하는 게시글인지 확인해준다.
        if(postProvider.checkPostExist(postIdx)==0){
            throw new BaseException(POST_EMPTY_POST_ID);
        }
        try{
            // postDao에서 업데이트를 성공적으로 마치면 2를 받아줄 것이고, 실패하면 0을 받아줄 것이다.
            int result = postDao.updatePost(postIdx, patchPostsReq.getContent());
            if(result==0){
                throw new BaseException(MODIFY_FAIL_POST);
            }
        } catch(Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

}
