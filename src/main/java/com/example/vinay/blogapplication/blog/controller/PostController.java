package com.example.vinay.blogapplication.blog.controller;

import com.example.vinay.blogapplication.blog.model.Comment;
import com.example.vinay.blogapplication.blog.model.Post;
import com.example.vinay.blogapplication.blog.model.Tag;
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

@Controller
public class PostController {
    private PostService postService;
    private CommentService commentService;
    private TagService tagService;
    private UserService userService;

    @Autowired
    public PostController(PostService postService, CommentService commentService, TagService tagService,
                          UserService userService) {
        this.postService = postService;
        this.commentService = commentService;
        this.tagService = tagService;
        this.userService = userService;
    }

    @GetMapping("/new-post")
    public String addPost(Model model) {
        Post post = new Post();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isAuthor = authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_AUTHOR"));

        if (isAuthor) {
            String username = authentication.getName();
            post.setAuthor(username);
        }

        model.addAttribute("post", post);

        return "post-form";
    }

    @PostMapping("/save-post")
    public String savePost(@ModelAttribute("post") Post modelPost, @RequestParam("tagSet") String requestTags,
                           @RequestParam(value = "action") String action, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isAdmin = authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"));

        if (isAdmin) {
            modelPost.setAdminName(authentication.getName());
            if (userService.findByName(modelPost.getAuthor()) == null) {
                model.addAttribute("error", "User doesn't exists...");

                return "post-form";
            }
        }

        if (modelPost.getCreatedAt() == null) {
            modelPost.setCreatedAt(LocalDateTime.now());
        }

        if (modelPost.getContent().contains(".")) {
            int fullStopIndex = modelPost.getContent().indexOf('.');
            modelPost.setExcerpt(modelPost.getContent().substring(0, fullStopIndex + 1));

        } else {
            modelPost.setExcerpt(modelPost.getContent());

        }

        Set<Tag> tagSet = new HashSet<>();
        if (requestTags != null && !requestTags.isEmpty()) {
            String[] tagArray = requestTags.trim().split(",");

            for (String tagName : tagArray) {
                tagName = tagName.trim();

                Tag existingTag = tagService.findByName(tagName);
                if (existingTag != null) {
                    tagSet.add(existingTag);

                } else {
                    Tag newTag = new Tag();
                    newTag.setName(tagName);
                    newTag.setCreatedAt(LocalDateTime.now());
                    tagService.save(newTag);

                    tagSet.add(newTag);
                }
            }
        }

        if (action.equals("Draft")) {
            modelPost.setPublished(false);
        }

        if (action.equals("Publish")) {
            modelPost.setPublished(true);
            modelPost.setPublishedAt(LocalDateTime.now());
        }

        if (action.equals("Update")) {
            modelPost.setUpdatedAt(LocalDateTime.now());
            modelPost.setPublished(true);
        }

        modelPost.setTags(tagSet);

        if (action.equals("Update Draft")) {
            modelPost.setUpdatedAt(LocalDateTime.now());
        }

        postService.save(modelPost);

        if (action.equals("Update Draft") || action.equals("Draft")) {
            return "redirect:/drafts";
        }

        return "redirect:/";
    }

    @GetMapping("/post/{id}")
    public String getPost(@PathVariable Integer id, Model model) {
        List<Comment> comments = commentService.findAllBYPostId((id));
        Post post = postService.findById(id);

        if (post.getAdminName() != null) {
            model.addAttribute("adminCreated", "Admin created post...");
        }

        Comment comment = new Comment();
        comment.setCreatedAt(LocalDateTime.now());
        model.addAttribute("comment", comment);

        model.addAttribute("post", post);
        model.addAttribute("comments", comments);

        return "post-page";
    }

    @PostMapping("/update-post")
    public String updatePost(@RequestParam("postId") Integer postId, Model model) {
        Post post = postService.findById(postId);
        Set<Tag> tags = post.getTags();

        StringJoiner tagsJoiner = new StringJoiner(",");
        for (Tag tag : tags) {
            tagsJoiner.add(tag.getName());
        }

        String postTags = tagsJoiner.toString();

        model.addAttribute("post", post);
        model.addAttribute("postTags", postTags);

        return "post-form";
    }

    @PostMapping("/delete-post")
    public String deletePost(@RequestParam("postId") Integer postId) {
        boolean isPublished = postService.findById(postId).isPublished();
        postService.deleteById(postId);

        if (!isPublished) {
            return "redirect:/drafts";
        }

        return "redirect:/";
    }

    @GetMapping("/my-drafts")
    public String drafts(Model model) {
        List<Post> posts = new ArrayList<>();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        boolean isAdmin = authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"));
        boolean isAuthor = authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_AUTHOR"));

        if (isAdmin) {
            posts = postService.findAllByIsPublishedFalseAndAdminNameOrderByPublishedAtDesc(username);

        } else if (isAuthor) {
            posts = postService.findAllByIsPublishedFalseAndAuthorOrderByPublishedAtDesc(username);

        }

        model.addAttribute("posts", posts);

        return "drafts";
    }

    @PostMapping("/publish")
    public String updatePublish(@RequestParam("postId") Integer postId) {
        Post post = postService.findById(postId);
        post.setPublishedAt(LocalDateTime.now());
        post.setPublished(true);

        postService.save(post);

        return "redirect:/";
    }

    @GetMapping("/my-posts")
    public String myPosts(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        boolean isAdmin = authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"));
        boolean isAuthor = authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_AUTHOR"));

        List<Post> posts = new ArrayList<>();
        if (isAdmin) {
            posts = postService.findAllByIsPublishedTrueAndAdminNameOrderByPublishedAtDesc(username);

        } else if (isAuthor) {
            posts = postService.findAllByIsPublishedTrueAndAuthorAndAdminNameIsNullOrderByPublishedAtDesc(username);

        }

        model.addAttribute("posts", posts);

        return "my-posts";
    }

}


