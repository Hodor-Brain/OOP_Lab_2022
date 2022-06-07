package ua.university.utils;

import org.keycloak.KeycloakPrincipal;
import org.keycloak.TokenVerifier;
import org.keycloak.common.VerificationException;
import org.keycloak.representations.AccessToken;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.Set;

public class KeycloakTokenUtil {
    public static final String RESOURCE = "BackendClient";


    public static AccessToken getToken(HttpServletRequest httpServletRequest){
        Principal principal = httpServletRequest.getUserPrincipal();
        KeycloakPrincipal<?> keycloakPrincipal = (KeycloakPrincipal<?>) principal;
        return keycloakPrincipal.getKeycloakSecurityContext().getToken();
    }

    public static AccessToken getToken(HttpServletRequest req, String token_str) throws Exception {
        String method = req.getMethod();
        if (method.equals("GET") || method.equals("POST") ||
                method.equals("DELETE") ||  method.equals("PATCH") ||  method.equals("PUT")) {
            try
            {
                // Special notation
                token_str = token_str.replace("Bearer ", "");
                return TokenVerifier.create(token_str, AccessToken.class).getToken();
            }
            catch (VerificationException e)
            {
                throw new Exception();
            }
        }
        return null;
    }

    public static String getPreferredUsername(AccessToken token){
        return token.getPreferredUsername();
    }

    public static String getName(AccessToken token){
        return token.getName();
    }

    public static Set<String> getRoles(AccessToken token){
        return token.getResourceAccess(RESOURCE).getRoles();
    }
}

