package com.example.demo.src.user.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetUserInfoRes {
    private int userIdx;
    private String nickName;
    private String name;
    private String profileImgUrl;
    private String website;
    private String introduce;
    private int followerCount;  // 팔로워의 수
    private int followingCount; // 팔로잉의 수
    private int postCount;  // 게시물의 개수
}
