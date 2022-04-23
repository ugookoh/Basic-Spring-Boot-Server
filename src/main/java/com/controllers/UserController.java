package com.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.validation.Valid;
import com.entities.User;
import com.service.UserDAOService;
import com.utils.CustomResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;

@RestController
public class UserController {
    @Autowired
    private UserDAOService userDAOService;

    @Operation(summary = "Creates a user (Do not pass an id, it is auto-generated)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created the user", content = {
                    @Content(mediaType = "application/json") }),
            @ApiResponse(responseCode = "400", description = "User already exists, all mandatory fields not provided", content = @Content),
    })
    @PostMapping("user/createUser")
    public ResponseEntity<?> createUser(@Valid @RequestBody User user) {
        CustomResponse customResponse = null;
        try {
            userDAOService.insert(user);
            customResponse = new CustomResponse(
                    HttpStatus.CREATED,
                    "New user created\n" +
                            "Name: " + user.getName() + "\n" +
                            "Role: " + user.getRole(),
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

    @Operation(summary = "Creates a list of users (Do not pass id. They are auto-generated)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created the list of users", content = {
                    @Content(mediaType = "application/json") }),
            @ApiResponse(responseCode = "400", description = "Your users are not formatted correctly. Please format them well", content = @Content),
    })
    @PostMapping("user/createBulkUsers")
    public ResponseEntity<?> createBulkUsers(@RequestBody ArrayList<User> users) {
        boolean created = userDAOService.insertBulk(users);
        Map<String, Object> message = new HashMap<>();
        message.put("message", created ? "Users successfully added" : "Users not added, an error occured");
        message.put("status", created ? HttpStatus.CREATED : HttpStatus.INTERNAL_SERVER_ERROR);
        message.put("status_code", created ? HttpStatus.CREATED.value() : HttpStatus.INTERNAL_SERVER_ERROR.value());
        return ResponseEntity.status((HttpStatus) message.get("status"))
                .body(message);

    }

    @Operation(summary = "Finds a user by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the user", content = {
                    @Content(mediaType = "application/json") }),
            @ApiResponse(responseCode = "404", description = "User does not exist", content = @Content),
    })
    @GetMapping("user/getUserById")
    public ResponseEntity<?> getUserById(@RequestParam int id) {
        User newUser = (User) userDAOService.findById(id);

        CustomResponse customResponse = new CustomResponse(
                newUser != null ? HttpStatus.OK : HttpStatus.NOT_FOUND,
                newUser != null ? null : "User not found",
                newUser);
        return customResponse.getResponse();
    }

    @Operation(summary = "Gets all the users in the database")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Created the user", content = {
                    @Content(mediaType = "application/json") }),
    })
    @GetMapping("user/getAllUsers")
    public ResponseEntity<?> getAllUsers() {
        List<User> users = userDAOService.getAllUsers();

        CustomResponse customResponse = new CustomResponse(
                HttpStatus.OK,
                null,
                users);
        return customResponse.getResponse();
    }

    @Operation(summary = "Deletes a user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Deleted the user if they exist", content = {
                    @Content(mediaType = "application/json") }),
    })
    @DeleteMapping("user/deleteUser")
    public ResponseEntity<?> deleteUser(@RequestParam int id) {
        boolean created = false;
        try {
            userDAOService.deleteUser(id);
            created = true;
        } catch (Exception e) {
            System.out.println(e);
        }

        return ResponseEntity.status(created ? HttpStatus.CREATED : HttpStatus.INTERNAL_SERVER_ERROR)
                .body(created ? "User successfully deleted" : "User not deleted, an error occured");
    }
}
