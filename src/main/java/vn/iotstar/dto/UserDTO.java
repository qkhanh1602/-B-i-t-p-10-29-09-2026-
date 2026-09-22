package vn.iotstar.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class UserDTO {
    private Long id;

    @NotBlank(message = "Username không được để trống")
    private String username;

    @NotBlank(message = "Email không được để trống")
    @Email(message = "Email không hợp lệ")
    private String email;

    @NotBlank(message = "Họ tên không được để trống")
    private String fullName;

    private boolean enabled;
    private String roleName;
    private long productCount;

    public UserDTO() {
    }

    public UserDTO(Long id, String username, String email, String fullName, boolean enabled, String roleName, long productCount) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.fullName = fullName;
        this.enabled = enabled;
        this.roleName = roleName;
        this.productCount = productCount;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public long getProductCount() {
        return productCount;
    }

    public void setProductCount(long productCount) {
        this.productCount = productCount;
    }

    public static UserDTOBuilder builder() {
        return new UserDTOBuilder();
    }

    public static class UserDTOBuilder {
        private Long id;
        private String username;
        private String email;
        private String fullName;
        private boolean enabled;
        private String roleName;
        private long productCount;

        public UserDTOBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public UserDTOBuilder username(String username) {
            this.username = username;
            return this;
        }

        public UserDTOBuilder email(String email) {
            this.email = email;
            return this;
        }

        public UserDTOBuilder fullName(String fullName) {
            this.fullName = fullName;
            return this;
        }

        public UserDTOBuilder enabled(boolean enabled) {
            this.enabled = enabled;
            return this;
        }

        public UserDTOBuilder roleName(String roleName) {
            this.roleName = roleName;
            return this;
        }

        public UserDTOBuilder productCount(long productCount) {
            this.productCount = productCount;
            return this;
        }

        public UserDTO build() {
            return new UserDTO(id, username, email, fullName, enabled, roleName, productCount);
        }
    }
}
