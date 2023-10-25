package com.example.vinay.blogapplication.blog.controller.api;

import com.example.vinay.blogapplication.blog.model.Comment;
import com.example.vinay.blogapplication.blog.model.Post;
import com.example.vinay.blogapplication.blog.model.Tag;
import com.example.vinay.blogapplication.blog.repository.PostRepository;
import com.example.vinay.blogapplication.blog.service.CommentService;
import com.example.vinay.blogapplication.blog.service.PostService;
import com.example.vinay.blogapplication.blog.service.TagService;
import com.example.vinay.blogapplication.blog.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;

@RestController
public class PostRestController {
    @Autowired
    PostRepository postRepository;
    @GetMapping("/api/posts")
    public List<Post> allPosts() {

        List<Post> posts = postRepository.findAll();
        return posts;
    }

}


