package com.example.vinay.blogapplication.blog.repository;

import com.example.vinay.blogapplication.blog.model.Tag;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepository extends JpaRepository<Tag, Integer> {
    Tag findByName(String Name);

}
