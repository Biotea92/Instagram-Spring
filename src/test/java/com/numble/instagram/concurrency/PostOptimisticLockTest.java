package com.numble.instagram.concurrency;

import com.numble.instagram.domain.post.entity.Post;
import com.numble.instagram.domain.post.repository.PostRepository;
import com.numble.instagram.domain.user.entity.User;
import com.numble.instagram.domain.user.repository.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.OptimisticLockingFailureException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Disabled
@SpringBootTest
public class PostOptimisticLockTest {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private PostRepository postRepository;
    @Autowired
    private UserRepository userRepository;

    @Test
    public void testOptimisticLocking() {
        User writer = new User("writer", "password", "profile");
        userRepository.save(writer);
        Post post = new Post("postImageUrl", "content", writer);
        postRepository.save(post);

        Post post1 = postRepository.findById(post.getId()).get();
        Post post2 = postRepository.findById(post.getId()).get();

        post1.incrementLikeCount();
        post2.incrementLikeCount();

        postRepository.save(post1);
        assertThatThrownBy(() -> postRepository.save(post2))
                .isInstanceOf(OptimisticLockingFailureException.class);

        Post updatedPost = entityManager.find(Post.class, post.getId());
        assertThat(updatedPost.getLikeCount()).isEqualTo(1L);
    }
}
