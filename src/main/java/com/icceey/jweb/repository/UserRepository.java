package com.icceey.jweb.repository;

import com.icceey.jweb.beans.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {

    User findUserByUsername(String username);

    Page<User> findAll(Pageable pageable);

}
