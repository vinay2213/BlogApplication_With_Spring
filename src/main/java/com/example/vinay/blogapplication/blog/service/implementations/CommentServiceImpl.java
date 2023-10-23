package com.example.vinay.blogapplication.blog.service.implementations;

import com.example.vinay.blogapplication.blog.model.Comment;
import com.example.vinay.blogapplication.blog.repository.CommentRepository;
import com.example.vinay.blogapplication.blog.service.CommentService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {
    private CommentRepository commentRepository;

    @Autowired
    public CommentServiceImpl(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    @Override
    public Comment findById(Integer id) {
        return commentRepository.findById(id).get();
    }

    @Override
    public void save(Comment comment) {
        commentRepository.save(comment);
    }

    @Override
    public void deleteById(Integer id) {
        commentRepository.deleteById(id);
    }

    @Override
    public List<Comment> findAllBYPostId(Integer postId) {
        List<Comment> comments = commentRepository.findAllByPostId(postId);
        return comments;
    }
}
