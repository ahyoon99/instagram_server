package com.example.demo.src.user.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class GetUserFeedRes {
    private boolean _isMyFeed;  // 내 피드인지, 다른 사람의 피드인지 구별하는데 사용한다.
    private GetUserInfoRes getUserInfo;  // 위에 있는 유저의 정보들을 나타낸다.
    private List<GetUserPostsRes> getUserPosts; // 아래에 있는 유저의 게시물 리스트를 나타낸다.

}
