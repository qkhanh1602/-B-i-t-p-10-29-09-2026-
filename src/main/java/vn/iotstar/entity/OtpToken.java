package vn.iotstar.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "otp_tokens", indexes = @Index(name = "idx_otp_email_type", columnList = "email,type"))
public class OtpToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String email;

    @Column(nullable = false, length = 100)
    private String otpHash;

    @Column(nullable = false, length = 30)
    private String type;

    @Column(nullable = false)
    private LocalDateTime expiresAt;

    @Column(nullable = false)
    private int attempts;

    @Column(nullable = false)
    private boolean used = false;

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    public OtpToken() {
    }

    public OtpToken(Long id, String email, String otpHash, String type, LocalDateTime expiresAt, int attempts, boolean used, LocalDateTime createdAt) {
        this.id = id;
        this.email = email;
        this.otpHash = otpHash;
        this.type = type;
        this.expiresAt = expiresAt;
        this.attempts = attempts;
        this.used = used;
        this.createdAt = createdAt != null ? createdAt : LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getOtpHash() {
        return otpHash;
    }

    public void setOtpHash(String otpHash) {
        this.otpHash = otpHash;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(LocalDateTime expiresAt) {
        this.expiresAt = expiresAt;
    }

    public int getAttempts() {
        return attempts;
    }

    public void setAttempts(int attempts) {
        this.attempts = attempts;
    }

    public boolean isUsed() {
        return used;
    }

    public void setUsed(boolean used) {
        this.used = used;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public static OtpTokenBuilder builder() {
        return new OtpTokenBuilder();
    }

    public static class OtpTokenBuilder {
        private Long id;
        private String email;
        private String otpHash;
        private String type;
        private LocalDateTime expiresAt;
        private int attempts;
        private boolean used = false;
        private LocalDateTime createdAt = LocalDateTime.now();

        public OtpTokenBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public OtpTokenBuilder email(String email) {
            this.email = email;
            return this;
        }

        public OtpTokenBuilder otpHash(String otpHash) {
            this.otpHash = otpHash;
            return this;
        }

        public OtpTokenBuilder type(String type) {
            this.type = type;
            return this;
        }

        public OtpTokenBuilder expiresAt(LocalDateTime expiresAt) {
            this.expiresAt = expiresAt;
            return this;
        }

        public OtpTokenBuilder attempts(int attempts) {
            this.attempts = attempts;
            return this;
        }

        public OtpTokenBuilder used(boolean used) {
            this.used = used;
            return this;
        }

        public OtpTokenBuilder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public OtpToken build() {
            return new OtpToken(id, email, otpHash, type, expiresAt, attempts, used, createdAt);
        }
    }
}
