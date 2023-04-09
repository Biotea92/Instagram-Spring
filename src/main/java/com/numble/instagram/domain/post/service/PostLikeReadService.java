package com.numble.instagram.domain.post.service;

import com.numble.instagram.domain.post.entity.Post;
import com.numble.instagram.domain.post.repository.PostLikeRepository;
import com.numble.instagram.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostLikeReadService {

    private final PostLikeRepository postLikeRepository;

    public List<Boolean> getPostLikes(User user, List<Post> posts) {
        List<Boolean> isPostLikes = postLikeRepository.findAllByUserAndPosts(user, posts);
        Collections.reverse(isPostLikes);
        return isPostLikes;
    }
}
