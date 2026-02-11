package org.example.bank_rest.persistence.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.example.bank_rest.persistence.model.entity.enums.UserRole;
import org.example.bank_rest.persistence.model.listener.TimestampsListener;
import org.jspecify.annotations.NonNull;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.OffsetDateTime;
import java.util.*;


@Entity
@EntityListeners(TimestampsListener.class)
@Table(name = "users")
@Setter
@Getter
@NoArgsConstructor
@EqualsAndHashCode(exclude = {"id"})
@ToString
public class User implements UserDetails, HasTimestamps {

    @Id
    @SequenceGenerator(name = "users_id_seq",
        sequenceName = "users_id_seq",
        allocationSize = 10)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "users_id_seq")
    private Long id;

    @Column(columnDefinition = "UUID UNIQUE NOT NULL")
    private UUID uuid = UUID.randomUUID();

    @Column(nullable = false, unique = true, length = 320)
    @Email
    @NotNull
    private String email;

    @Column(nullable = false)
    @NotNull
    private String password;

    @Column(nullable = false, name = "first_name", length = 128)
    @NotNull
    private String firstName;

    @Column(nullable = false, name = "last_name", length = 128)
    @NotNull
    private String lastName;

    @Column(name = "role")
    private UserRole role;

    @OneToMany(mappedBy = "owner", fetch = FetchType.LAZY, orphanRemoval = true)
    private Set<Card> cards = new LinkedHashSet<>();

    @Column(name = "created_at")
    private OffsetDateTime createdAt;

    @Column(name = "updated_at")
    private OffsetDateTime updatedAt;

    @Override
    @NonNull
    public String getUsername() {
        return uuid.toString();
    }

    @Override
    @NonNull
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.toString()));
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
