package com.example.demo.src.post.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class PatchPostsReq {
    // 게시글 수정시, 게시글의 이미지는 수정할 수 없고 내용만 수정 가능하다.
    private int userIdx;    // 게시글을 작성한 유저, userIdx는 Jwt를 통해 받아도 되고 path variable로 받아도 된다. 어떤 방식이 더 restful한지 고민하고 개발하면 된다.
    private String content; // 게시글 내용
}
