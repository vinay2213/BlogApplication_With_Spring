package com.example.vinay.blogapplication.blog.service.implementations;

import com.example.vinay.blogapplication.blog.model.Post;
import com.example.vinay.blogapplication.blog.repository.PostRepository;
import com.example.vinay.blogapplication.blog.repository.TagRepository;
import com.example.vinay.blogapplication.blog.service.PostService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Service
public class PostServiceImpl implements PostService {
    private PostRepository postRepository;
    private TagRepository tagRepository;

    @Autowired
    public PostServiceImpl(PostRepository postRepository, TagRepository tagRepository) {
        this.postRepository = postRepository;
        this.tagRepository = tagRepository;
    }

    @Override
    public Page<Post> findAll(Pageable pageable) {
        return postRepository.findAllByIsPublishedTrue(pageable);
    }

    @Override
    public List<Post> findAll() {
        return postRepository.findAll();
    }

    @Override
    public Post findById(Integer id) {
        return postRepository.findById(id).get();
    }

    @Override
    public void save(Post post) {
        postRepository.save(post);
    }

    @Override
    public void deleteById(Integer id) {
        postRepository.deleteById(id);
    }

    @Override
    public List<Post> findAllByIsPublishedFalseAndAdminNameOrderByPublishedAtDesc(String adminName) {
        return postRepository.findAllByIsPublishedFalseAndAdminNameOrderByPublishedAtDesc(adminName);
    }

    @Override
    public List<Post> findAllByIsPublishedFalseAndAuthorOrderByPublishedAtDesc(String author) {
        return postRepository.findAllByIsPublishedFalseAndAuthorAndAdminNameIsNullOrderByPublishedAtDesc(author);
    }


    @Override
    public Page<Post> findAllPostsBySearchRequest(String search, Pageable pageable) {
        return postRepository.findAllPostsBySearchRequest(search, pageable);
    }

    @Override
    public Set<String> findAllAuthors() {
        return postRepository.findAllDistinctAuthorsFromPublishedPosts();
    }

    @Override
    public Set<String> findTagNamesWithPublishedPosts() {
        return postRepository.findTagsWithPublishedPosts();
    }

    @Override
    public Page<Post> filterPosts(Set<String> authorNames,
                                  Set<String> tagNames, LocalDateTime startDate, LocalDateTime endDate, Pageable pageable) {
        int authorList = authorNames == null || authorNames.isEmpty() ? 1 : 0;
        int tagList = tagNames == null || tagNames.isEmpty() ? 1 : 0;
        int dates = (startDate == null || endDate == null) ? 1 : 0;

        return postRepository.filterPosts(authorList, tagList, dates,
                authorNames, tagNames, startDate, endDate, pageable);
    }


    @Override
    public Page<Post> filterAndSearchPosts(String searchRequest, Set<String> authorNames, Set<String> tagNames, LocalDateTime startDate,
                                           LocalDateTime endDate, Pageable pageable) {
        int authorList = authorNames == null || authorNames.isEmpty() ? 1 : 0;
        int tagList = tagNames == null || tagNames.isEmpty() ? 1 : 0;
        int dates = (startDate == null || endDate == null) ? 1 : 0;

        return postRepository.filterAndSearchPosts(searchRequest, authorList, tagList, dates,
                authorNames, tagNames, startDate, endDate, pageable);

    }

    @Override
    public Set<String> findDistinctAuthorsBySearchRequest(String searchRequest) {
        return postRepository.findDistinctAuthorsBySearchRequest(searchRequest);
    }

    @Override
    public Set<String> findDistinctTagsBySearchRequest(String searchRequest) {
        return postRepository.findDistinctTagsBySearchRequest(searchRequest);
    }

    @Override
    public List<Post> findAllByIsPublishedTrueAndAuthorAndAdminNameIsNullOrderByPublishedAtDesc(String author) {
        return postRepository.findAllByIsPublishedTrueAndAuthorAndAdminNameIsNullOrderByPublishedAtDesc(author);
    }

    @Override
    public List<Post> findAllByIsPublishedTrueAndAdminNameOrderByPublishedAtDesc(String adminName) {
        return postRepository.findAllByIsPublishedTrueAndAdminNameOrderByPublishedAtDesc(adminName);
    }


}