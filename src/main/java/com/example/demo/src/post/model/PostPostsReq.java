package com.example.demo.src.post.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class PostPostsReq {
    private int userIdx;    // 게시글을 작성한 유저, userIdx는 Jwt를 통해 받아도 되고 path variable로 받아도 된다. 어떤 방식이 더 restful한지 고민하고 개발하면 된다.
    private String content; // 게시글 내용
    private List<PostImgsUrlsReq> postImgUrls;  // 게시글의 사진 리스트
}
