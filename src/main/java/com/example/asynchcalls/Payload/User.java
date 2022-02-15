package com.example.asynchcalls.Payload;

import com.example.asynchcalls.Model.UserDO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    public User(UserDO userDO) {
        BeanUtils.copyProperties(userDO, this);
    }

    private Integer Id;
    private String name;
    private String email;
    private String gender;
}
