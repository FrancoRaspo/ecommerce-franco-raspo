package com.ecommerce.francoraspo.services;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface UsuarioService {
    UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;
}

