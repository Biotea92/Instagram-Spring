package com.numble.instagram.concurrency;

import com.numble.instagram.application.usecase.post.CreatePostLikeUsecase;
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

import java.util.concurrent.*;

@Disabled
@SpringBootTest
class PostLikeConcurrencyTest {

    @Autowired
    PostRepository postRepository;
    @Autowired
    UserRepository userRepository;
    @PersistenceContext
    private EntityManager em;
    @Autowired
    CreatePostLikeUsecase createPostLikeUsecase;

    @Test
    void test() throws InterruptedException {
        User writer = new User("writer", "password", "profile");
        userRepository.saveAndFlush(writer);
        Post post = new Post("postImageUrl", "content", writer);
        postRepository.saveAndFlush(post);
        System.out.println("처음 좋아요 수 = " + post.getLikeCount());

        em.clear();

        Long id = post.getId();
        System.out.println("id = " + id);

        int threadAmount = 10;
        CyclicBarrier barrier = new CyclicBarrier(threadAmount);

        ExecutorService executorService = Executors.newFixedThreadPool(threadAmount);
        for (int i = 0; i < threadAmount; i++) {
            executorService.execute(() -> {
                try {
                    barrier.await();
                } catch (InterruptedException | BrokenBarrierException e) {
                    System.out.println("======");
                }
                User user = new User("user", "password", "profile");
                userRepository.save(user);
                createPostLikeUsecase.execute(user.getId(), id);
                em.clear();
            });
        }

        executorService.shutdown();

        boolean allThreadsFinished = executorService.awaitTermination(1, TimeUnit.MINUTES);
        if (!allThreadsFinished) {
            executorService.shutdownNow();
        }

        em.clear();

        Long likeCount = postRepository.findById(post.getId()).orElseThrow().getLikeCount();
        System.out.println("likeCount = " + likeCount);
    }
}