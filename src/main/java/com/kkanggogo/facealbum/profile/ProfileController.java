package com.kkanggogo.facealbum.profile;

import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class ProfileController {
    private final Environment env;

    @GetMapping("/profile")
    public String profile() {
        List<String> profiles = Arrays.asList(env.getActiveProfiles());
        List<String> prodProfiles = Arrays.asList("prod", "prod1", "prod2");
        String defaultProfile = prodProfiles.isEmpty() ? "default" : prodProfiles.get(0);

        return profiles.stream()
                .filter(prodProfiles::contains)
                .findAny()
                .orElse(defaultProfile);
    }
}
