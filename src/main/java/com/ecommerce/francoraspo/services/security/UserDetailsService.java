package com.ecommerce.francoraspo.services.security;

import com.ecommerce.francoraspo.models.authJwtModels.UserDetailsImpl;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface UserDetailsService {
    UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;

    public static boolean IsAdmin() {
        return (SecurityContextHolder.getContext()
                .getAuthentication().getAuthorities().stream().filter(
                        x -> x.getAuthority().equals("ADMINISTRADOR")
                ).count() != 0);
    }

    public static String UsuarioContext() {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
        return userDetails.getUsername();
    }

}

