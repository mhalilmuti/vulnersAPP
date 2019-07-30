package com.muti.VulnersAPP.vulnerAPP.repository;

import com.muti.VulnersAPP.vulnerAPP.model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserRepository extends CrudRepository<User, Long> {
    User findByUsername(String username);

    User findByEmail(String email);

    List<User> getUsersByEnabledIsTrue();
}
