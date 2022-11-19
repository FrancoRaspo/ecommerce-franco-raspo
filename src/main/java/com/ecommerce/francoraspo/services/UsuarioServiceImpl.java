package com.ecommerce.francoraspo.services;

import com.ecommerce.francoraspo.models.authJwtModels.UserDetailsImpl;
import com.ecommerce.francoraspo.models.entities.Usuario;
import com.ecommerce.francoraspo.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UsuarioServiceImpl implements org.springframework.security.core.userdetails.UserDetailsService {
    @Autowired
    UsuarioRepository userNewRepository;
    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario user = userNewRepository.findByUsuarioNombre(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + username));
        return UserDetailsImpl.build(user);
    }
}

