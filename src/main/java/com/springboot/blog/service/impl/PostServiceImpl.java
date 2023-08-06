package com.springboot.blog.service.impl;

import com.springboot.blog.entity.Post;
import com.springboot.blog.exception.ResourceNotFound;
import com.springboot.blog.payload.PostDTO;
import com.springboot.blog.payload.PostResponse;
import com.springboot.blog.repository.PostRepository;
import com.springboot.blog.service.PostService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService {

    private PostRepository postRepository;
    private ModelMapper mapper;
    public PostServiceImpl(PostRepository postRepository ,ModelMapper mapper) {
        this.postRepository = postRepository;
        this.mapper = mapper;
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
    public PostResponse getAllPosts(int pageNo , int pageSize, String sortBy, String sortDir) {

        // Apakah sortingan itu asc atau tidak
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        //create Pageable instace
        Pageable pageable  = PageRequest.of(pageNo, pageSize, sort);


        Page<Post> posts = postRepository.findAll(pageable);

        //get Content for page object
        List<Post> listOfPost =  posts.getContent();

        // convert into list array of object
       List<PostDTO> contentPost =  listOfPost.stream().map(post -> mapToDto(post)).collect(Collectors.toList());

        PostResponse postResponse = new PostResponse();
        postResponse.setContent(contentPost);
        postResponse.setPageNo(posts.getNumber());
        postResponse.setPageSize(posts.getSize());
        postResponse.setTotalElements(posts.getTotalElements());
        postResponse.setTotalPages(posts.getTotalPages());
        postResponse.setLast(posts.isLast());

        return  postResponse;
    }

    @Override
    public PostDTO getPostById(long id) {
        Post post  = postRepository.findById(id).orElseThrow(() -> new ResourceNotFound("Post" , "id" , id));

        return mapToDto(post);
    }

    @Override
    public PostDTO updatePost(PostDTO postDTO, long id) {
        //get post by id from database
        Post post  = postRepository.findById(id).orElseThrow(() -> new ResourceNotFound("Post" , "id" , id));

        post.setTitle(post.getTitle());
        post.setContent(post.getContent());
        post.setDescription(post.getDescription());

        Post updatePost = postRepository.save(post);

        return mapToDto(updatePost);
    }

    @Override
    public void deletePostById(long id) {
        Post post  = postRepository.findById(id).orElseThrow(() -> new ResourceNotFound("Post" , "id" , id));
        postRepository.delete(post);
    }

    private Post mapToEntity(PostDTO postDTO){
        Post post = mapper.map(postDTO, Post.class);
        return post;
    }


    private  PostDTO mapToDto(Post post){
        PostDTO postDTO = mapper.map(post, PostDTO.class);
    /*
        PostDTO postDTO = new PostDTO();
        postDTO.setId(post.getId());
        postDTO.setTitle(post.getTitle());
        postDTO.setDescription(post.getDescription());
        postDTO.setContent(post.getContent());
    */
        return  postDTO;
    }


}
