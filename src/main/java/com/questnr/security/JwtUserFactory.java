package com.questnr.security;

import com.questnr.model.entities.Authority;
import com.questnr.model.entities.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public final class JwtUserFactory {

    private JwtUserFactory() {
    }

    public static JwtUser create(User user) {
        return new JwtUser(user.getUserId(), user.getUsername(), user.getFirstName(),
                user.getLastName(), user.getDisplayName(), user.getEmailId(), user.getPassword(),
                mapToGrantedAuthorities(user.getAuthorities()),
                user.getLastPasswordResetDate(), user.getSlug());
    }

    private static List<GrantedAuthority> mapToGrantedAuthorities(Set<Authority> authorities) {
        return authorities.stream()
                .map(authority -> new SimpleGrantedAuthority(authority.getName().name()))
                .collect(Collectors.toList());
    }
}
