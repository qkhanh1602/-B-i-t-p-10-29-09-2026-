package vn.iotstar.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import vn.iotstar.entity.User;
import java.util.Collection;
import java.util.List;

public class CustomUserDetails implements UserDetails {
    private static final long serialVersionUID = 1L;

    private final Long id;
    private final String username;
    private final String email;
    private final String password;
    private final String fullName;
    private final String images;
    private final boolean enabled;
    private final Collection<? extends GrantedAuthority> authorities;

    public CustomUserDetails(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.password = user.getPassword();
        this.fullName = user.getFullName();
        this.images = user.getImages();
        this.enabled = user.isEnabled();
        String roleName = user.getRole() != null ? user.getRole().getName() : "ROLE_USER";
        if (roleName.startsWith("ROLE_")) {
            this.authorities = List.of(new SimpleGrantedAuthority(roleName));
        } else {
            this.authorities = List.of(new SimpleGrantedAuthority(roleName), new SimpleGrantedAuthority("ROLE_" + roleName));
        }
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getFullName() {
        return fullName;
    }

    public String getImages() {
        return images;
    }

    public String getRole() {
        return authorities.stream().findFirst().map(GrantedAuthority::getAuthority).orElse("ROLE_USER");
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}
