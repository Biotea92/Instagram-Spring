package com.numble.instagram.domain.post.service;

import com.numble.instagram.domain.post.dto.PostDto;
import com.numble.instagram.domain.post.entity.Post;
import com.numble.instagram.domain.post.repository.PostRepository;
import com.numble.instagram.domain.user.entity.User;
import com.numble.instagram.exception.badrequest.NotPostWriterException;
import com.numble.instagram.exception.notfound.PostNotFoundException;
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

    public PostDto edit(User user, Long postId, String content, String postImageUrl) {
        Post post = postRepository.findById(postId)
                .orElseThrow(PostNotFoundException::new);
        checkWriter(user, post);
        post.updateContent(content);
        if (postImageUrl != null) {
            String willDeleteImageUrl = post.updatePostImageUrl(postImageUrl);
            return PostDto.from(post, willDeleteImageUrl);
        }
        return PostDto.from(post);
    }

    private static void checkWriter(User user, Post post) {
        if (!post.isWriter(user)) {
            throw new NotPostWriterException();
        }
    }
}
