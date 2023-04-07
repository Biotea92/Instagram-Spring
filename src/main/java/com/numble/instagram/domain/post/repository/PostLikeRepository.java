package com.numble.instagram.domain.post.repository;

import com.numble.instagram.domain.post.entity.Post;
import com.numble.instagram.domain.post.entity.PostLike;
import com.numble.instagram.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {

    boolean existsByUserAndPost(User user, Post post);

    Optional<PostLike> findByUserAndPost(User user, Post post);

    @Query("""
            select
                case
                    when (select count(pl.id)
                          from PostLike pl
                          where pl.post = p and pl.user = :user) > 0
                    then true
                    else false
                end
            from Post p
            where p in :posts
    """)
    List<Boolean> findAllByUserAndPosts(@Param("user") User user, @Param("posts") List<Post> posts);
}
