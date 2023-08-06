package com.springboot.blog.service.impl;

import com.springboot.blog.entity.Comment;
import com.springboot.blog.entity.Post;
import com.springboot.blog.exception.BlogAPIException;
import com.springboot.blog.exception.ResourceNotFound;
import com.springboot.blog.payload.CommentDTO;
import com.springboot.blog.payload.PostDTO;
import com.springboot.blog.repository.CommentRepository;
import com.springboot.blog.repository.PostRepository;
import com.springboot.blog.service.CommentService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService {

    private CommentRepository commentRepository;
    private PostRepository postRepository;
    private ModelMapper mapper;

    public CommentServiceImpl(CommentRepository commentRepository, PostRepository postRepository, ModelMapper mapper) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.mapper = mapper;

    }

    @Override
    public CommentDTO createComment(long postId, CommentDTO commentDTO) {
        Comment comment = mapToEntity(commentDTO);

        //mengambil post berdasarkan id dan set di dalam objek
        Post post = postRepository.findById(postId).orElseThrow(() -> new ResourceNotFound("Post", "id", postId));
        comment.setPost(post);

        Comment newComment = commentRepository.save(comment);
        return mapToDto(comment);
    }

    @Override
    public List<CommentDTO> getCommentsByPostId(long postId) {
        List<Comment> comments = commentRepository.findByPostId(postId);

        return comments.stream().map(comment -> mapToDto(comment)).collect(Collectors.toList());
    }

    @Override
    public CommentDTO getCommentById(Long postId, Long commentId) {
        //mengambil data post by id
        Post post = postRepository.findById(postId).orElseThrow(() -> new ResourceNotFound("Post" , "id", postId));

        //mengambil data comment by id
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new ResourceNotFound("Comments" , "id" , commentId));

        if(!comment.getPost().getId().equals(post.getId())){
            throw  new BlogAPIException(HttpStatus.BAD_REQUEST, "Comments dosnt not belongs to post");
        }


        return mapToDto(comment);
    }

    @Override
    public CommentDTO updateComment(Long postId, long commentId, CommentDTO commentRequest) {
        //mengambil data post by id
        Post post = postRepository.findById(postId).orElseThrow(() -> new ResourceNotFound("Post" , "id", postId));

        //mengambil data comment by id
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new ResourceNotFound("Comments" , "id" , commentId));

        if(!comment.getPost().getId().equals(post.getId())){
            throw  new BlogAPIException(HttpStatus.BAD_REQUEST, "Comments dosnt not belongs to post");
        }

        comment.setName(commentRequest.getName());
        comment.setEmail(commentRequest.getEmail());
        comment.setBody(commentRequest.getBody());

        Comment updateComment = commentRepository.save(comment);
        return mapToDto(updateComment);
    }

    @Override
    public void deleteComment(Long postId, Long commentId) {
        //mengambil data post by id
        Post post = postRepository.findById(postId).orElseThrow(() -> new ResourceNotFound("Post" , "id", postId));

        //mengambil data comment by id
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new ResourceNotFound("Comments" , "id" , commentId));

        if(!comment.getPost().getId().equals(post.getId())){
            throw  new BlogAPIException(HttpStatus.BAD_REQUEST, "Comments dosnt not belongs to post");
        }

        commentRepository.delete(comment);
    }

    private Comment mapToEntity(CommentDTO commentDTO){
        Comment comment = mapper.map(commentDTO, Comment.class);

        /*
        Comment comment = new Comment();
        comment.setId(commentDTO.getId());
        comment.setBody(commentDTO.getBody());
        comment.setName(commentDTO.getName());
        comment.setEmail(commentDTO.getEmail());
        */
        return comment;
    }


    private  CommentDTO mapToDto(Comment comment){

        CommentDTO commentDTO = mapper.map(comment, CommentDTO.class);
        /*
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setId(comment.getId());
        commentDTO.setBody(comment.getBody());
        commentDTO.setName(comment.getName());
        commentDTO.setEmail(comment.getEmail());
        */
        return  commentDTO;
    }

}
