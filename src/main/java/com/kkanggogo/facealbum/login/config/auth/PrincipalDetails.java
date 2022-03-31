package com.kkanggogo.facealbum.login.config.auth;

import com.kkanggogo.facealbum.login.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

public class PrincipalDetails implements UserDetails {

    private User user;

    public PrincipalDetails(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
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

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collectors = new ArrayList<GrantedAuthority>();
        collectors.add(() -> {
            //GrantedAuthority의 getAuthority함수를 사용한 것.
            return "ROLE_" + user.getRole(); //spring에서 역할에 대해 리턴을 받을 때 'ROLE_'를 꼭 넣어야함(규칙)
        });
        return collectors;
    }
}
