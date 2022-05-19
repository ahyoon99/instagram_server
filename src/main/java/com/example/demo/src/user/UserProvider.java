package com.example.demo.src.user;


import com.example.demo.config.BaseException;
import com.example.demo.src.user.model.GetUserFeedRes;
import com.example.demo.src.user.model.GetUserInfoRes;
import com.example.demo.src.user.model.GetUserPostsRes;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;
import static com.example.demo.config.BaseResponseStatus.USERS_EMPTY_USER_ID;

//Provider : Read의 비즈니스 로직 처리
@Service
public class UserProvider {

    private final UserDao userDao;
    private final JwtService jwtService;


    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public UserProvider(UserDao userDao, JwtService jwtService) {
        this.userDao = userDao;
        this.jwtService = jwtService;
    }


//    public GetUserRes getUsersByEmail(String email) throws BaseException{
//        try{
//            GetUserRes getUsersRes = userDao.getUsersByEmail(email);
//            return getUsersRes;
//        }
//        catch (Exception exception) {
//            throw new BaseException(DATABASE_ERROR);
//        }
//                    }

    // 회원 피드 조회
    public GetUserFeedRes retrieveUserFeed(int userIdx, int userIdxByJwt) throws BaseException{
        if(checkUserExist(userIdx) ==0){
            throw new BaseException(USERS_EMPTY_USER_ID);
        }

        Boolean isMyFeed = true;
        try{
            if (userIdxByJwt != userIdx) {
                isMyFeed = false;
            }
            GetUserInfoRes getUserInfo = userDao.selectUserInfo(userIdx);
            List<GetUserPostsRes> getUserPosts = userDao.selectUserPosts(userIdx);
            GetUserFeedRes getUserFeed =new GetUserFeedRes(isMyFeed,getUserInfo,getUserPosts);
            return getUserFeed;
        } catch(Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

//    public GetUserRes getUsersByIdx(int userIdx) throws BaseException{
//        try{
//            GetUserRes getUsersRes = userDao.getUsersByIdx(userIdx);
//            return getUsersRes;
//        }
//        catch (Exception exception) {
//            throw new BaseException(DATABASE_ERROR);
//        }
//    }


//    public int checkEmail(String email) throws BaseException{
//        try{
//            return userDao.checkEmail(email);
//        } catch (Exception exception){
//            throw new BaseException(DATABASE_ERROR);
//        }
//    }

    // userIdx가 유효한 Idx 값인지 확인해준다.
    public int checkUserExist(int userIdx) throws BaseException{
        try{
            return userDao.checkUserExist(userIdx);
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }



}
