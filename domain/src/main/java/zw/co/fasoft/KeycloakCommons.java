package zw.co.fasoft;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class KeycloakCommons {
    @Value("${keycloak.token-uri}")
    private String tokenUrl;
    @Value("${keycloak.client-id}")
    private String clientId;
    @Value("${keycloak.grant-type}")
    private String grantType;
    @Value("${keycloak.admin-client-id}")
    private String adminClientId;
    @Value("${keycloak.admin-username}")
    private String adminUsername;
    @Value("${keycloak.admin-password}")
    private String adminPassword;
    @Value("${keycloak.masterUrl}")
    private String masterUrl;
    @Value("${keycloak.client-user-uri}")
    private String clientUserUrl;
    @Value("${keycloak.client-uuid}")
    private String clientUUID;
    @Value("${notifications.service-url}")
    private String notificationsUrl;
    @Value("${api-urls.frontend-login-link}")
    private String loginLink;
    @Value("${keycloak.realm-base-url}")
    private String realmBaseUrl;
}
