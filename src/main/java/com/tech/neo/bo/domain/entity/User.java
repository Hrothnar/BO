package com.tech.neo.bo.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity()
@Table(name = "users")
@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler", "account"})
public class User implements UserDetails {
    @Id()
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "login", unique = true, nullable = false, insertable = true, updatable = true, length = 64)
    private String login;

    @Column(name = "password", unique = false, nullable = false, insertable = true, updatable = true, length = 64)
    private String password;

    @ElementCollection(targetClass = String.class, fetch = FetchType.LAZY)
    @CollectionTable(name = "phone_numbers",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id", unique = false, nullable = false, insertable = true, updatable = true, table = "users",
                    foreignKey = @ForeignKey(value = ConstraintMode.PROVIDER_DEFAULT)),
            uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "phone_number"}))
    @Column(name = "phone_number", unique = true, nullable = false, insertable = true, updatable = true, length = 16)
    private Set<String> phoneNumbers = new HashSet<>();

    @ElementCollection(targetClass = String.class, fetch = FetchType.LAZY)
    @CollectionTable(name = "emails",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id", unique = false, nullable = false, insertable = true, updatable = true, table = "users",
                    foreignKey = @ForeignKey(value = ConstraintMode.PROVIDER_DEFAULT)),
            uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "email"}))
    @Column(name = "email", unique = true, nullable = false, insertable = true, updatable = true, length = 32)
    private Set<String> emails = new HashSet<>();

    @Column(name = "full_name", unique = false, nullable = false, insertable = true, updatable = false, length = 64)
    private String fullName;

    @Column(name = "birth_date", unique = false, nullable = true, insertable = true, updatable = false, length = 32)
    private LocalDate birthDate;

    @OneToOne(targetEntity = Account.class, cascade = CascadeType.ALL, fetch = FetchType.LAZY, optional = true, mappedBy = "user", orphanRemoval = true)
    @PrimaryKeyJoinColumn(name = "user_id", referencedColumnName = "id")
    private Account account;

    public User() {

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<String> getPhoneNumbers() {
        return phoneNumbers;
    }

    public void setPhoneNumbers(Set<String> phoneNumbers) {
        this.phoneNumbers = phoneNumbers;
    }

    public Set<String> getEmails() {
        return emails;
    }

    public void setEmails(Set<String> emails) {
        this.emails = emails;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getUsername() {
        return login;
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
        return true;
    }
}
