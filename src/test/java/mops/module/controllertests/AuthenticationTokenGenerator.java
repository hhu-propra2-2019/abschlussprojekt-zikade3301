package mops.module.controllertests;

import org.keycloak.KeycloakPrincipal;
import org.keycloak.adapters.RefreshableKeycloakSecurityContext;
import org.keycloak.adapters.springsecurity.account.SimpleKeycloakAccount;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;

import java.util.HashSet;
import java.util.Set;

import static org.mockito.Mockito.*;

public class AuthenticationTokenGenerator {

    public static KeycloakAuthenticationToken generateAuthenticationToken(String role){

        Set<String> roles = new HashSet<>();
        roles.add(role);

        KeycloakPrincipal principal = mock(KeycloakPrincipal.class, RETURNS_DEEP_STUBS);

        when(principal.getName()).thenReturn(role);
        when(principal.getKeycloakSecurityContext().getIdToken().getEmail()).thenReturn("some@mail.de");

        SimpleKeycloakAccount account = new SimpleKeycloakAccount(principal, roles, mock(RefreshableKeycloakSecurityContext.class));

        return new KeycloakAuthenticationToken(account, true);
    }
}