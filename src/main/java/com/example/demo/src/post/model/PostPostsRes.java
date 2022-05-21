package com.example.demo.src.post.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class PostPostsRes { // 응답을 의미한다.
    // 우리가 생성한 게시물의 Idx값을 반환해줄 것이다.
    private int postIdx;
}
