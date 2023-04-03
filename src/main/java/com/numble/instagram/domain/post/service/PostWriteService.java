package com.numble.instagram.domain.post.service;

import com.numble.instagram.domain.post.entity.Post;
import com.numble.instagram.domain.post.repository.PostRepository;
import com.numble.instagram.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class PostWriteService {

    private final PostRepository postRepository;

    public Post register(User writerUser, String content, String postImageUrl) {
        Post newPost = Post.builder()
                .writerUser(writerUser)
                .content(content)
                .postImageUrl(postImageUrl)
                .build();
        return postRepository.save(newPost);
    }
}
