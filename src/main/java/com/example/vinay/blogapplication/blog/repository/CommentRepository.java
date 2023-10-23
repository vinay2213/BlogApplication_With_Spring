package com.example.vinay.blogapplication.blog.repository;

import com.example.vinay.blogapplication.blog.model.Comment;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Integer> {
    List<Comment> findAllByPostId(Integer postId);
}
