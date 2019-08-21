package ru.agrmv.twitter.model.user;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ru.agrmv.twitter.model.Message;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@Table(name = "user")
public class User implements UserDetails {
    /** Идентификатор пользователя */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    /** Имя пользователя */
    @NotBlank(message = "Username cannot be empty")
    private String username;

    /**Пароль пользователя */
    @NotBlank(message = "Password cannot be empty")
    private String password;

    /** Признак активности */
    private boolean active;

    public boolean isAdmin() {
        return roles.contains(Role.ROLE_ADMIN);
    }

    /**
     * Ролевая система (админ, модератор, обычный юзер)
     *
     * ElementCollection формирует дополнительную таблицу для хрения enum'ов
     * fetch = FetchType.EAGER - сразу вместе с загрузкой юзера подгружаются его роли
     *
     * CollectionTable данное поле будет храниться в отдельной таблице user_role,
     * которая будет соединяться с текущей табличкей через user_id
     *
     * Enumerated(EnumType.STRING) - храним enum в виде строки
     * */
    @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    private Set<Role> roles;

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Message> messages;

    @ManyToMany
    @JoinTable(
            name = "user_subscriptions",
            joinColumns = {@JoinColumn(name = "channel_id")},
            inverseJoinColumns = {@JoinColumn(name = "subscriber_id")}
    )
    private Set<User> subscribers = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "user_subscriptions",
            joinColumns = {@JoinColumn(name = "subscriber_id")},
            inverseJoinColumns = {@JoinColumn(name = "channel_id")}
    )
    private Set<User> subscriptions = new HashSet<>();


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
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
        return isActive();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return getRoles();
    }
}
