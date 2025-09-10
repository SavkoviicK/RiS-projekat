package com.veterinarska.stanica.security;

import java.util.List;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.veterinarska.stanica.model.Korisnik;
import com.veterinarska.stanica.repository.KorisnikRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final KorisnikRepository repo;

    public CustomUserDetailsService(KorisnikRepository repo) {
        this.repo = repo;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Korisnik k = repo.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Nepostojeći email: " + email));

        var authority = new SimpleGrantedAuthority("ROLE_" + k.getUloga().name());

        boolean enabled = Boolean.TRUE.equals(k.getAktivan());

        return new User(
                k.getEmail(),
                k.getLozinka(),      
                enabled,            
                true,               
                true,                
                true,                
                List.of(authority)
        );
    }
}
