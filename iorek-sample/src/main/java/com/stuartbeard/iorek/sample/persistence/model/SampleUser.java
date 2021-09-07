package com.stuartbeard.iorek.sample.persistence.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@Data
public class SampleUser implements UserDetails {

    private static final long serialVersionUID = 7041603734291863330L;

    private String username;
    private String password;
    private boolean isEnabled = true;
    private boolean isAccountNonExpired = true;
    private boolean isAccountNonLocked = true;
    private boolean isCredentialsNonExpired = true;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority("USER"));
    }

    public static SampleUser fromUserDetails(UserDetails userDetails) {
        SampleUser sampleUser = new SampleUser();
        sampleUser.setUsername(userDetails.getUsername());
        sampleUser.setPassword(userDetails.getPassword());
        sampleUser.setEnabled(userDetails.isEnabled());
        sampleUser.setAccountNonExpired(userDetails.isAccountNonExpired());
        sampleUser.setAccountNonLocked(userDetails.isAccountNonLocked());
        sampleUser.setCredentialsNonExpired(userDetails.isCredentialsNonExpired());
        return sampleUser;
    }
}
