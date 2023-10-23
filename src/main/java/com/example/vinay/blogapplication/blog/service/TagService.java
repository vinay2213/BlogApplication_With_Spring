package com.example.vinay.blogapplication.blog.service;

import com.example.vinay.blogapplication.blog.model.Tag;

public interface TagService {
    void save(Tag tag);
    Tag findByName(String tagName);


}
