package com.icceey.jweb;

import com.icceey.jweb.beans.User;
import com.icceey.jweb.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.util.DigestUtils;
import org.springframework.util.ResourceUtils;
import sun.security.provider.MD5;

import javax.validation.constraints.Digits;
import java.io.File;
import java.io.FileNotFoundException;

@SpringBootApplication
public class JwebApplication {

    public static void main(String[] args) {
        SpringApplication.run(JwebApplication.class, args);
    }

}
