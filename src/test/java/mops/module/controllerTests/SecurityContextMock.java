package mops.module.controllerTests;

import org.keycloak.KeycloakPrincipal;
import org.keycloak.adapters.RefreshableKeycloakSecurityContext;
import org.keycloak.adapters.springsecurity.account.SimpleKeycloakAccount;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;

import java.util.HashSet;
import java.util.Set;

import static org.mockito.Mockito.*;

public class SecurityContextMock {

    public static void setupSecurityContextMock(SecurityContext context , String name){

        Set<String> roles = new HashSet<>();
        roles.add(name);

        KeycloakPrincipal principal = mock(KeycloakPrincipal.class, RETURNS_DEEP_STUBS);

        when(principal.getName()).thenReturn(name);
        when(principal.getKeycloakSecurityContext().getIdToken().getEmail()).thenReturn("some@mail.de");

        SimpleKeycloakAccount account = new SimpleKeycloakAccount(principal, roles, mock(RefreshableKeycloakSecurityContext.class));

        KeycloakAuthenticationToken authenticationToken = new KeycloakAuthenticationToken(account, true);

        context.setAuthentication(authenticationToken);

    }
}
