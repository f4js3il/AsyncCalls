package com.example.asynchcalls.Service;

import com.example.asynchcalls.Model.UserDO;
import com.example.asynchcalls.Payload.User;
import com.example.asynchcalls.Repository.UserRepository;
import org.apache.juli.logging.LogFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
public class UserService {

    private UserRepository userRepository;

    Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Async
    public CompletableFuture<List<UserDO>> saveUser(MultipartFile file) throws Exception {
        long start = System.currentTimeMillis();
        List<User> users = parseCSVFile(file);
        logger.info("saving users of size "+users.size()+", "+Thread.currentThread().getName());
        List<UserDO> userDOs = users.stream().map(user->new UserDO(user)).collect(Collectors.toList());
        List<UserDO> usersSaved = this.userRepository.saveAll(userDOs);
        long end = System.currentTimeMillis();
        logger.info("Total time"+(end-start));
        return CompletableFuture.completedFuture(usersSaved);
    }

    @Async
    public CompletableFuture<List<User>> getAllUsers(){
        logger.info("get list of users"+ Thread.currentThread().getName());
       List<UserDO> userDOs = this.userRepository.findAll();
        List<User> users = userDOs.stream().map(userDO->new User(userDO)).collect(Collectors.toList());
        return CompletableFuture.completedFuture(users);
    }



    private List<User> parseCSVFile(final MultipartFile file) throws Exception {
        final List<User> users = new ArrayList<>();
        try {
            try (final BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
                String line;
                while ((line = br.readLine()) != null) {
                    final String[] data = line.split(",");
                    final User user = new User();
                    user.setName(data[0]);
                    user.setEmail(data[1]);
                    user.setGender(data[2]);
                    users.add(user);
                }
                return users;
            }
        } catch (final IOException e) {
            logger.error("Failed to parse CSV file {}", e);
            throw new Exception("Failed to parse CSV file {}", e);
        }
    }


}
