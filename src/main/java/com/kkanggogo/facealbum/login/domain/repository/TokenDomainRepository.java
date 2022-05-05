package com.kkanggogo.facealbum.login.domain.repository;

import com.kkanggogo.facealbum.login.domain.TokenDomain;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface TokenDomainRepository extends CrudRepository<TokenDomain, Long> {
    Optional<TokenDomainRepository> findByUsername(String username);
}
