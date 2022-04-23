package com.controllers;

import java.util.List;
import javax.validation.Valid;
import com.entities.Post;
import com.service.PostDAOService;
import com.utils.CustomResponse;
import com.utils.IdAndDescription;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
public class PostController {
    @Autowired
    private PostDAOService postDAOService;

    @Operation(summary = "Creates a post (Do not pass an id, it is auto-generated)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created the post", content = {
                    @Content(mediaType = "application/json") }),
            @ApiResponse(responseCode = "400", description = "The user for whom you are creating the post does not exist. Or your post is not well validated", content = @Content),
    })
    @PostMapping("post/createPost")
    public ResponseEntity<?> createUser(@Valid @RequestBody IdAndDescription request) {
        CustomResponse customResponse = null;
        try {

            if (request.getId() == 0 || request.getDescription() == null)
                throw new Exception(
                        "Missing variables. The userId or description is not provided. Please provide them both");
            postDAOService.createPost(request.getId(), request.getDescription());
            customResponse = new CustomResponse(
                    HttpStatus.CREATED,
                    "New post created",
                    null);
            return customResponse.getResponse();
        } catch (Exception e) {
            customResponse = new CustomResponse(
                    HttpStatus.BAD_REQUEST,
                    e.getMessage(),
                    null);
            return customResponse.getResponse();
        }
    }

    @Operation(summary = "Gets one post")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the post", content = {
                    @Content(mediaType = "application/json") }),
            @ApiResponse(responseCode = "404", description = "The post id you used to search does not exist", content = @Content),
    })
    @GetMapping("post/getOnePost")
    public ResponseEntity<?> getOnePost(@RequestParam int id) {
        Post post = postDAOService.getOnePost(id);

        CustomResponse customResponse = new CustomResponse(
                post != null ? HttpStatus.OK : HttpStatus.NOT_FOUND,
                post != null ? null : "User not found",
                post);
        return customResponse.getResponse();
    }

    @Operation(summary = "Gets all posts associated with a user id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found posts associated with a user", content = {
                    @Content(mediaType = "application/json") }),
            @ApiResponse(responseCode = "400", description = "The user does not exist", content = @Content),
    })
    @GetMapping("post/getAllUserPost")
    public ResponseEntity<?> getAllUserPost(@RequestParam int id) {
        List<Post> posts = postDAOService.getAllUserPost(id);
        CustomResponse customResponse = new CustomResponse(
                HttpStatus.OK,
                null,
                posts);
        return customResponse.getResponse();
    }

    @Operation(summary = "Deletes a post")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Deleted the post if it existed", content = {
                    @Content(mediaType = "application/json") }),
    })
    @DeleteMapping("post/deletePost")
    public ResponseEntity<?> deletePost(@RequestParam int id) {
        postDAOService.deletePost(id);
        CustomResponse customResponse = new CustomResponse(
                HttpStatus.OK,
                "Post has been deleted",
                null);
        return customResponse.getResponse();
    }

    @Operation(summary = "Updates a post")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Updated the post", content = {
                    @Content(mediaType = "application/json") }),
            @ApiResponse(responseCode = "400", description = "The user does not exist, or the post was not well validated.", content = @Content),
    })
    @PostMapping("post/updatePost")
    public ResponseEntity<?> updatePost(@Valid @RequestBody IdAndDescription request) {
        CustomResponse customResponse = null;
        try {
            if (request.getId() == 0 || request.getDescription() == null)
                throw new Exception(
                        "Missing variables. The id or description is not provided. Please provide them both");
            postDAOService.updatePost(request.getId(), request.getDescription());
            customResponse = new CustomResponse(
                    HttpStatus.OK,
                    "Post has been updated",
                    null);
            return customResponse.getResponse();
        } catch (Exception e) {
            customResponse = new CustomResponse(
                    HttpStatus.BAD_REQUEST,
                    e.getMessage(),
                    null);
            return customResponse.getResponse();
        }
    }

}
