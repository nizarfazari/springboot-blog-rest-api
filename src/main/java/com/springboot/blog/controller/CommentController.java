package com.springboot.blog.controller;

import com.springboot.blog.payload.CommentDTO;
import com.springboot.blog.service.CommentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class CommentController {
    private CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }


    @PostMapping("/{postId}/comments")
    public ResponseEntity<CommentDTO> createComment(@PathVariable(value = "postId") long postId,@Valid @RequestBody CommentDTO commentDTO) {
    return new ResponseEntity<>(commentService.createComment(postId,commentDTO), HttpStatus.CREATED);
    }

    @GetMapping("/{postId}/comments")
    public List<CommentDTO> getCommentsByPostId(@PathVariable(value = "postId") long postId){
    return commentService.getCommentsByPostId(postId);
    }

    @GetMapping("/{postId}/comments/{commentId}")
    public ResponseEntity<CommentDTO> getCommentById(@PathVariable(value = "postId") Long postId, @PathVariable(value = "commentId") Long commentId){
    CommentDTO commentDTO = commentService.getCommentById(postId,commentId);

    return  new ResponseEntity<>(commentDTO,HttpStatus.OK);
    }

    @PutMapping("/{postId}/comments/{commentId}")
    public ResponseEntity<CommentDTO> updateComment(@PathVariable(value = "postId") Long postId,
                                                    @PathVariable(value = "commentId") Long commentId, @Valid @RequestBody CommentDTO commentDTO){
        CommentDTO updateComment = commentService.updateComment(postId,commentId,commentDTO);
        return  new ResponseEntity<>(updateComment, HttpStatus.OK);
    }


    @DeleteMapping("{postId}/comments/")
    public ResponseEntity<String> deleteComment(@PathVariable(value = "postId") Long postId,
                                                @PathVariable(value = "commentId") Long commentId)
    {
        commentService.deleteComment(postId,commentId);
        return new ResponseEntity<>("Comment delete succesfuly" , HttpStatus.OK);
    }
}
