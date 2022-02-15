package com.example.asynchcalls.Controller;

import com.example.asynchcalls.Payload.User;
import com.example.asynchcalls.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
public class UserController {

    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(value = "/save", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE}, produces = "application/json")
    public ResponseEntity  saveUsers( @RequestParam(value="files") MultipartFile[] files){
        Arrays.stream(files).forEach(file-> {
            try {
                userService.saveUser(file);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        return ResponseEntity.ok().build();
    }

    @GetMapping(value="/users", produces = "application/json")
    public CompletableFuture<ResponseEntity> findAllUsers(){
        return userService.getAllUsers().thenApply(ResponseEntity::ok);
    }

    @GetMapping(value="/users/all", produces = "application/json")
    public ResponseEntity getAllUsers() throws ExecutionException, InterruptedException {
        CompletableFuture<List<User>> users1 = userService.getAllUsers();
        CompletableFuture<List<User>> users2 = userService.getAllUsers();
        CompletableFuture<List<User>> users3 = userService.getAllUsers();

        CompletableFuture.allOf(users1,users2,users3).exceptionally(ex->null).join();

        List<List<User>> users = Arrays.asList(users1.get(),users2.get(),users3.get());



        return ResponseEntity.ok().body(users);

    }
}
