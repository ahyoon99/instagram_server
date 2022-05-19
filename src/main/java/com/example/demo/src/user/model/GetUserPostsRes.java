package com.example.demo.src.user.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetUserPostsRes {
    private int postIdx;    // 게시물의 idx 값
    private String postImgUrl;  // 게시물의 링크
}
