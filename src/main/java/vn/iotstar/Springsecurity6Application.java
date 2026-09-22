package vn.iotstar;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import vn.iotstar.entity.Product;
import vn.iotstar.entity.Role;
import vn.iotstar.entity.User;
import vn.iotstar.repository.ProductRepository;
import vn.iotstar.repository.RoleRepository;
import vn.iotstar.repository.UserRepository;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@SpringBootApplication
public class Springsecurity6Application {

    public static void main(String[] args) {
        SpringApplication.run(Springsecurity6Application.class, args);
    }

    @Bean
    CommandLineRunner init(
        RoleRepository roleRepository,
        UserRepository userRepository,
        ProductRepository productRepository,
        PasswordEncoder passwordEncoder
    ) {
        return args -> {
            Role userRole = roleRepository
                .findByName("ROLE_USER")
                .orElseGet(() -> roleRepository.save(new Role("ROLE_USER")));

            Role adminRole = roleRepository
                .findByName("ROLE_ADMIN")
                .orElseGet(() -> roleRepository.save(new Role("ROLE_ADMIN")));

            User user = userRepository.findByUsername("user01").orElse(null);
            if (user == null) {
                user = User.builder()
                    .username("user01")
                    .email("user01@gmail.com")
                    .password(passwordEncoder.encode("123456"))
                    .fullName("Nguyễn Hữu Trung")
                    .images("/images/user.png")
                    .role(userRole)
                    .enabled(true)
                    .build();
                user = userRepository.save(user);
            }

            if (userRepository.findByUsername("admin").isEmpty()) {
                User admin = User.builder()
                    .username("admin")
                    .email("admin@gmail.com")
                    .password(passwordEncoder.encode("123456"))
                    .fullName("Quản trị viên")
                    .images("/images/user.png")
                    .role(adminRole)
                    .enabled(true)
                    .build();
                userRepository.save(admin);
            }

            if (productRepository.count() == 0) {
                Product p1 = Product.builder()
                    .name("Điện thoại Oppo A95")
                    .description("Điện thoại Oppo A95 chính hãng")
                    .price(new BigDecimal("6565656.00"))
                    .imageUrl("/images/user.png|sample_1")
                    .user(user)
                    .createdAt(LocalDateTime.now())
                    .build();

                Product p2 = Product.builder()
                    .name("Điện thoại Oppo A6")
                    .description("Điện thoại Oppo A6 pin khủng")
                    .price(new BigDecimal("689999.00"))
                    .imageUrl("/images/user.png|sample_2")
                    .user(user)
                    .createdAt(LocalDateTime.now())
                    .build();

                productRepository.save(p1);
                productRepository.save(p2);
            }
        };
    }
}
