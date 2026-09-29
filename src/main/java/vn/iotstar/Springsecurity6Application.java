package vn.iotstar;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import vn.iotstar.entity.User;
import vn.iotstar.repository.UserRepository;

@SpringBootApplication
public class Springsecurity6Application {

    public static void main(String[] args) {
        SpringApplication.run(Springsecurity6Application.class, args);
    }

    @Bean
    CommandLineRunner init(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            if (userRepository.findByEmail("trungnh@hcmute.edu.vn").isEmpty()) {
                User user = new User();
                user.setFullName("Nguyễn Hữu Trung");
                user.setEmail("trungnh@hcmute.edu.vn");
                user.setPassword(passwordEncoder.encode("123456"));
                user.setImages("https://images.unsplash.com/photo-1535713875002-d1d0cf377fde");
                userRepository.save(user);
            }
        };
    }
}
