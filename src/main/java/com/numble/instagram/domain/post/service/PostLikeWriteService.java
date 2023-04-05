package com.numble.instagram.domain.post.service;

import com.numble.instagram.domain.post.entity.Post;
import com.numble.instagram.domain.post.entity.PostLike;
import com.numble.instagram.domain.post.repository.PostLikeRepository;
import com.numble.instagram.domain.user.entity.User;
import com.numble.instagram.exception.badrequest.AlreadyLikedPostException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class PostLikeWriteService {

    private final PostLikeRepository postLikeRepository;

    public PostLike like(User user, Post post) {
        checkLiked(user, post);
        PostLike newPostLike = PostLike.builder().user(user).post(post).build();
        return postLikeRepository.save(newPostLike);
    }

    private void checkLiked(User user, Post post) {
        if (postLikeRepository.existsByUserAndPost(user, post)) {
            throw new AlreadyLikedPostException();
        }
    }
}
