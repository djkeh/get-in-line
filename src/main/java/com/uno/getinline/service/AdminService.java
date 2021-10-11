package com.uno.getinline.service;

import com.uno.getinline.domain.Admin;
import com.uno.getinline.repository.AdminRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.config.core.GrantedAuthorityDefaults;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class AdminService implements UserDetailsService {

    private final AdminRepository adminRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Admin admin = adminRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Admin not found."));

        return User.builder()
                .username(admin.getEmail())
                .password(admin.getPassword())
                .authorities(List.of())
                .build();
    }

}
