package com.numble.instagram.domain.post.service;

import com.numble.instagram.domain.post.entity.Post;
import com.numble.instagram.domain.post.entity.PostLike;
import com.numble.instagram.domain.post.repository.PostLikeRepository;
import com.numble.instagram.domain.user.entity.User;
import com.numble.instagram.exception.badrequest.AlreadyLikedPostException;
import com.numble.instagram.exception.badrequest.NotLikedPostException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PostLikeWriteService {

    private final PostLikeRepository postLikeRepository;

    public void like(User user, Post post) {
        checkLiked(user, post);
        PostLike newPostLike = PostLike.builder().user(user).post(post).build();
        postLikeRepository.save(newPostLike);
    }

    public void dislike(User user, Post post) {
        PostLike postLike = postLikeRepository.findByUserAndPost(user, post)
                .orElseThrow(NotLikedPostException::new);
        postLikeRepository.delete(postLike);
    }

    public void deletePostLike(Post post) {
        List<PostLike> postLikes = postLikeRepository.findAllByPost(post);
        postLikeRepository.deleteAll(postLikes);
    }

    private void checkLiked(User user, Post post) {
        if (postLikeRepository.existsByUserAndPost(user, post)) {
            throw new AlreadyLikedPostException();
        }
    }
}
