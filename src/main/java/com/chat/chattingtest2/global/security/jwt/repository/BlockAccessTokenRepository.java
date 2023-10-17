package com.chat.chattingtest2.global.security.jwt.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.chat.chattingtest2.global.security.jwt.model.BlockAccessToken;

@Repository
public interface BlockAccessTokenRepository extends CrudRepository<BlockAccessToken, String> {
}
