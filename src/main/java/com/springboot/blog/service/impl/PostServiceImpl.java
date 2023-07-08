package com.springboot.blog.service.impl;

import com.springboot.blog.entity.Post;
import com.springboot.blog.payload.PostDTO;
import com.springboot.blog.repository.PostRepository;
import com.springboot.blog.service.PostService;

import java.util.List;
import java.util.stream.Collectors;

public class PostServiceImpl implements PostService {

    private PostRepository postRepository;


    public PostServiceImpl(PostRepository postRepository) {
        this.postRepository = postRepository;
    }



    @Override
    public PostDTO createPost(PostDTO postDTO) {
        //covert DTO into Entity

        Post post = mapToEntity(postDTO);
        Post newPost = postRepository.save(post);

        //conver Entity into DTO

        PostDTO postResponse = mapToDto(newPost);
        return postResponse;
    }

    @Override
    public List<PostDTO> getAllDataPost() {
        List<Post> posts = postRepository.findAll();
       return  posts.stream().map(post -> mapToDto(post)).collect(Collectors.toList());

    }

    private Post mapToEntity(PostDTO postDTO){
        Post post = new Post();
        post.setTitle(postDTO.getTitle());
        post.setContent(postDTO.getContent());
        post.setDescription(postDTO.getDescription());
        return post;
    }


    private  PostDTO mapToDto(Post post){
        PostDTO postDTO = new PostDTO();
        postDTO.setId(post.getId());
        postDTO.setTitle(post.getTitle());
        postDTO.setDescription(post.getDescription());
        postDTO.setContent(post.getContent());
        return  postDTO;
    }
}
