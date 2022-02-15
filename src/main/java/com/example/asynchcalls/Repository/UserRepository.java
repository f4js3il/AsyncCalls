package com.example.asynchcalls.Repository;

import com.example.asynchcalls.Model.UserDO;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository  extends JpaRepository<UserDO, Integer> {
}
