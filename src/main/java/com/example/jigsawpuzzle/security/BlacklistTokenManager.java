package com.example.jigsawpuzzle.security;

import org.springframework.stereotype.Component;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class BlacklistTokenManager {
    private final Set<String> blacklistedTokens = ConcurrentHashMap.newKeySet();

    public boolean isRevoked(String token) {
        return blacklistedTokens.contains(token);
    }

    public void revokeToken(String token) {
        blacklistedTokens.add(token);
    }

    public void removeTokenFromBlacklist(String token) {
        blacklistedTokens.remove(token);
    }
}