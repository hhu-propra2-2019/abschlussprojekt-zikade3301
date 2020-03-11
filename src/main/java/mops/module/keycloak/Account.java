package mops.module.keycloak;

import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
class Account {
    private final String name;
    private final String email;
    private final String image;
    private final Set<String> roles;
}