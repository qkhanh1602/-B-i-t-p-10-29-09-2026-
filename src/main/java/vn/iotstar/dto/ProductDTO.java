package vn.iotstar.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;
import java.math.BigDecimal;

public class ProductDTO {
    private Long id;

    @NotBlank(message = "Tên sản phẩm không được để trống")
    private String name;

    private String description;

    @NotNull(message = "Giá không được để trống")
    @DecimalMin(value = "0.0", message = "Giá phải >= 0")
    private BigDecimal price;

    private String imageUrl;
    private Long userId;
    private String username;
    private MultipartFile image;

    public ProductDTO() {
    }

    public ProductDTO(Long id, String name, String description, BigDecimal price, String imageUrl, Long userId, String username, MultipartFile image) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.imageUrl = imageUrl;
        this.userId = userId;
        this.username = username;
        this.image = image;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public MultipartFile getImage() {
        return image;
    }

    public void setImage(MultipartFile image) {
        this.image = image;
    }

    public static ProductDTOBuilder builder() {
        return new ProductDTOBuilder();
    }

    public static class ProductDTOBuilder {
        private Long id;
        private String name;
        private String description;
        private BigDecimal price;
        private String imageUrl;
        private Long userId;
        private String username;
        private MultipartFile image;

        public ProductDTOBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public ProductDTOBuilder name(String name) {
            this.name = name;
            return this;
        }

        public ProductDTOBuilder description(String description) {
            this.description = description;
            return this;
        }

        public ProductDTOBuilder price(BigDecimal price) {
            this.price = price;
            return this;
        }

        public ProductDTOBuilder imageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
            return this;
        }

        public ProductDTOBuilder userId(Long userId) {
            this.userId = userId;
            return this;
        }

        public ProductDTOBuilder username(String username) {
            this.username = username;
            return this;
        }

        public ProductDTOBuilder image(MultipartFile image) {
            this.image = image;
            return this;
        }

        public ProductDTO build() {
            return new ProductDTO(id, name, description, price, imageUrl, userId, username, image);
        }
    }
}
