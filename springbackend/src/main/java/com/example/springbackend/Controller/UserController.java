package com.example.springbackend.Controller;
import com.example.springbackend.Exception.User.UserErrorCode;
import com.example.springbackend.Exception.User.UserApiException;
import com.example.springbackend.Service.JoinService;
import com.example.springbackend.Entity.User;
import com.example.springbackend.DTO.JoinDto;
import com.example.springbackend.repo.UserRepo;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("http://localhost:3000")

public class UserController {

    private final JoinService joinService;
    private final UserRepo userRepository;

    public UserController(JoinService joinService, UserRepo userRepository) {
        this.joinService = joinService;
        this.userRepository = userRepository;
    }

    @PostMapping("/user")
    User newUser(@RequestBody JoinDto joinDto) throws Exception {
        return joinService.joinProcess(joinDto);
    }

    @GetMapping("/users")
    List<User> getAllUsers(){
        return userRepository.findAll();
    }

    @GetMapping("user/{id}")
    User getUserById(@PathVariable(name = "id") Long id){
        return userRepository.findById(id)
                .orElseThrow(()-> new UserApiException(UserErrorCode.NOT_FOUND_USER));
    }

    @PutMapping("updateUser/{id}")
    User updateUser(@RequestBody User newUser,@PathVariable(name = "id") Long id){
        return userRepository.findById(id)
                .map(user ->{
                    user.setUsername(newUser.getUsername());
                    user.setEmail(user.getEmail());
                    user.setName(newUser.getName());
                    user.setPassword(user.getPassword());
                    user.setRole("ROLE_USER");
                    return userRepository.save(user);
                }).orElseThrow(()->new UserApiException(UserErrorCode.NOT_FOUND_USER));
    }

    @DeleteMapping("deleteUser/{id}")
    String deleteuserById(@PathVariable(name = "id") Long id){
        if (!userRepository.existsById(id)){
            throw new UserApiException(UserErrorCode.NOT_FOUND_USER);
        };
        userRepository.deleteById(id);
        return "User id"+id+"delete success";

    }

}
