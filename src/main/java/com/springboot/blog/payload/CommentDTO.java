package com.springboot.blog.payload;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CommentDTO {
    private long id;

    @NotEmpty(message = "Name should not to be null or empty")
    private String name;

    @NotEmpty(message = "Email should not to be null")
    @Email
    private String  email;

    @NotEmpty(message = "Email should not to be null")
    @Size(min = 10,message = "comment must be minimum 10 characters")
    private String body;
}
