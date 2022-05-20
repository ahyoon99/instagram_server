package com.example.demo.src.post.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class GetPostsRes {  // 게시글 객체이다.
    private int postIdx;
    private int userIdx;
    private String nickName;
    private String profileImgUrl;
    private String content;
    private int postLikeCount;
    private int commentCount;
    private String updatedAt;
    private String likeOrNot;  // 해당 게시물에 좋아요를 눌렀는지 안눌렀는지를 표시하는데 필요한 변수이다.
    private List<GetPostImgRes> imgs;  // 사진들이 리스트로 있어야하니깐 리스트로 만들어주었다.
}
