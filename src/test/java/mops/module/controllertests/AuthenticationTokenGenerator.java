package mops.module.controllertests;


import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.HashSet;
import java.util.Set;
import org.keycloak.KeycloakPrincipal;
import org.keycloak.adapters.RefreshableKeycloakSecurityContext;
import org.keycloak.adapters.springsecurity.account.SimpleKeycloakAccount;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;

public class AuthenticationTokenGenerator {

    /**Mocking an KeyclaokAuthenticationToken.
     *
     * @param role which should ne passed to Account for the Token
     * @return KeycloakAuthenticationTokenMock for Testing
     */
    public static KeycloakAuthenticationToken generateAuthenticationToken(String role) {
        Set<String> roles = new HashSet<>();
        roles.add(role);

        KeycloakPrincipal principal = mock(KeycloakPrincipal.class, RETURNS_DEEP_STUBS);

        when(principal.getName()).thenReturn(role);
        when(principal.getKeycloakSecurityContext().getIdToken().getEmail())
                .thenReturn("some@mail.de");

        SimpleKeycloakAccount account = new SimpleKeycloakAccount(
                principal,
                roles,
                mock(RefreshableKeycloakSecurityContext.class));

        return new KeycloakAuthenticationToken(account, true);
    }
}