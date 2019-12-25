package com.totality.model.repositories;

import com.totality.model.entities.Post;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository <Post, Integer > {

  List<Post> findAllByPostSlug(String slug);

  Post findByPostId(Long postId);

}
