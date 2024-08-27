package zw.co.fasoft;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import zw.co.fasoft.exceptions.*;

import java.util.*;

import static zw.co.fasoft.Unique_Credentials_Generation.generateRandomPassword;

/**
 * @author Fasoft
 * @date 30/May/2024
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final RestTemplate restTemplate;
    private final NotificationService notificationService;
//    private final CommonsService commonsService;

    LoginResponse loginResponse;
    String userId;
    String userGroupID;
    String roleId;
    String password;
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
    @Value("${keycloak.realm-base-url}")
    private String realmBaseUrl;

    @Override
    public UserAccountRequest createUser(UserAccountRequest request) {
         buildAndPostUserAccount(request);
         String content = Message.USER_ACCOUNT_CREATION_MESSAGE
                 .replace("{username}", request.getEmail())
                 .replace("{password}", password)
                 .replace("{role}", request.getRole().name())
                 .replace("{login-message}",Message.ADMIN_LOGIN_LINK_MESSAGE.
                         replace("{epay-admin-login-link}","")
            );
         String subject = "User Account Creation";
         Boolean isSms = false;
         Boolean isEmail = true;
         Boolean isPush = false;

         notificationService.sendNotification(content,subject,request.getFullName(),request,isEmail,isSms,isPush);
         return request;
    }
    private UserAccountRequest buildAndPostUserAccount(UserAccountRequest userAccountRequest) {
        String token;
        password = generateRandomPassword(7);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        assert loginResponse != null;
        token = loginResponse.getAccess_token();

        headers.setBearerAuth(token);
        String username = userAccountRequest.getEmail();
        String requestBody = "{\n" +
                "    \"username\":\"" + username + "\",\n" +
                "    \"email\":\"" + userAccountRequest.getEmail() + "\",\n" +
                "    \"lastName\":\"" + "" + "\",\n" +
                "    \"firstName\":\"" + userAccountRequest.getFullName() + "\",\n" +
                "    \"enabled\":\"true\",\n" +
                "    \"credentials\":[{\"value\":\"" + password + "\",\"type\":\"password\",\"temporary\":true}]\n" +
                "}";

        HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);
        log.info("request : {}", requestEntity);
        try {
            ResponseEntity<String> responses = restTemplate.postForEntity(clientUserUrl, requestEntity, String.class);

            userId = responses.getHeaders().getLocation().getPath().replaceAll(".*/([^/]+)$", "$1");
        } catch (HttpClientErrorException.Conflict e) {
            throw new EmailAlreadyExistsException("Email already exists");
        } catch (HttpClientErrorException.BadRequest e) {
            throw new FailedToProcessRequestException("Please provide a valid email or input");
        } catch (HttpClientErrorException httpClientErrorException) {
            throw new IncorrectUsernameOrPasswordException("Error occured");
        } catch (Exception e) {
            throw new FailedToProcessRequestException("unable to process request");
        }
        return userAccountRequest;
    }

        @Override
    public LoginResponse login(LoginRequest request) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        LoginResponse response = null;
        LinkedMultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("grant_type", grantType);
        map.add("client_id", clientId);
        map.add("username", request.getUsername());
        map.add("password", request.getPassword());

        HttpEntity<LinkedMultiValueMap<String, String>> httpEntity = new HttpEntity<>(map, headers);
//        log.info("request : {}", httpEntity);
        try {
            response = restTemplate.postForObject(tokenUrl, httpEntity, LoginResponse.class);
        }
        catch (HttpClientErrorException httpClientErrorException) {
            try {
                ErrorResponse errorResponse = new ObjectMapper()
                   .readValue(httpClientErrorException.getResponseBodyAsString(), ErrorResponse.class);
                if(errorResponse.getError_description().contains("Account not fully setup"))
                  throw new AccountNotFullySetupException(errorResponse.getError_description());

                throw new IncorrectUsernameOrPasswordException(errorResponse.getError_description());
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }

        } catch (Exception e) {
            throw new FailedToProcessRequestException("unable to process request "+e.getMessage());
        }
        return response;
    }
    @Override
    public void updatePassword(UpdatePasswordRequest request, String userName) {
        getAdminToken();
        if(!Objects.equals(request.getPassword(), request.getConfirmPassword()))
            throw new IncorrectUsernameOrPasswordException("Passwords do not match");

        String userId = findUserIdByUsername(userName);

        String url = realmBaseUrl+"/users/" + userId + "/reset-password";
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(loginResponse.getAccess_token());
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("type", "password");
        requestBody.put("value", request.getPassword());
        requestBody.put("temporary", false);
        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);

        try {

            ResponseEntity<Void> response = restTemplate.exchange(url, HttpMethod.PUT, requestEntity, Void.class);
            sendForgotPasswordNotification(userName);
        } catch (HttpClientErrorException httpClientErrorException) {
            throw new FailedToProcessRequestException("Error resetting password");
        } catch (Exception e) {
            throw new FailedToProcessRequestException("unable to process request");
        }

    }


    @Override
    public void resetPassword(PasswordResetRequest passwordResetRequest, String userName) {
        getAdminToken();
        LoginRequest verifyLogin = LoginRequest.builder()
                .password(passwordResetRequest.getOldPassword())
                .username(userName)
                .build();
        login(verifyLogin);
        assert loginResponse != null;
        userId = findUserIdByUsername(userName);

        String url = realmBaseUrl+"/users/" + userId + "/reset-password";
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(loginResponse.getAccess_token());
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("type", "password");
        requestBody.put("value", passwordResetRequest.getNewPassword());
        requestBody.put("temporary", false);
        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);

        try {

            ResponseEntity<Void> response = restTemplate.exchange(url, HttpMethod.PUT, requestEntity, Void.class);
            sendForgotPasswordNotification(userName);
        } catch (HttpClientErrorException httpClientErrorException) {
            throw new IncorrectUsernameOrPasswordException("Error resetting password");
        } catch (Exception e) {
            throw new FailedToProcessRequestException("unable to process request");
        }

    }
@Async
    public void sendForgotPasswordNotification(String userName) {
        // sending notification
        String content = Message.PASSWORD_RESET_NOTIFICATION;
        String subject = "ACCOUNT PASSWORD RESET";

        notificationService.sendNotification(content, subject, userName, null, true, true, false);
    }

    public String findUserIdByUsername(String username) {
        getAdminToken();

        String url = realmBaseUrl+"/users?username=" + username;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(loginResponse.getAccess_token());
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON)); // Set Accept header to JSON
        assert loginResponse != null;

        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        try {
            UserDetails[] userDetails = restTemplate.exchange(url, HttpMethod.GET, requestEntity, UserDetails[].class).getBody();

            assert userDetails != null;
            return userDetails[0].getId();

        } catch (HttpClientErrorException httpClientErrorException) {
            throw new IncorrectUsernameOrPasswordException("Error fetching user ID " + httpClientErrorException.getStatusCode() + " - " + httpClientErrorException.getResponseBodyAsString());
        } catch (Exception e) {
            throw new FailedToProcessRequestException("unable to process request");
        }
    }

    @Override
    public Responses forgotPassword(String username) {
        getAdminToken();
        sendForgotPassword(username);
        return Responses.builder()
                .code(200L)
                .message("Link sent to your email. Check Spam folder if you cant find it")
                .build();
    }


    public TokenResponse refreshToken(RefreshTokenRequest refreshTokenRequest) throws JsonProcessingException {
        getAdminToken();
        String tokenResponse = null;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setBearerAuth(loginResponse.getAccess_token());

        // Set request body parameters
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("client_id", clientId);
        params.add("grant_type", "refresh_token");
        params.add("refresh_token", refreshTokenRequest.getRefresh_token());

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(params, headers);

        try {
            tokenResponse = restTemplate.exchange(
                    tokenUrl,
                    HttpMethod.POST,
                    requestEntity,
                    String.class
            ).getBody();
        } catch (HttpClientErrorException httpClientErrorException) {
            throw new IncorrectUsernameOrPasswordException("Invalid Username or Password");
        } catch (Exception e) {
            throw new FailedToProcessRequestException("unable to process request");
        }
        ObjectMapper objectMapper = new ObjectMapper();
        TokenResponse token = objectMapper.readValue(tokenResponse, TokenResponse.class);
        return token;
    }


    public void sendForgotPassword(String username) {
        String token;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        assert loginResponse != null;
        token = loginResponse.getAccess_token();
        headers.setBearerAuth(token);

        String requestBody = "[\"UPDATE_PASSWORD\"]";

        HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);
//        log.info("request : {}", requestEntity);

        HttpEntity<String> requestEntity1 = new HttpEntity<>(null, headers);
//        log.info("request : {}", requestEntity1);
        try {
            ArrayList<LinkedHashMap<String, String>> passwordRequests = restTemplate.exchange(realmBaseUrl+"/users?username=" + username + "&exact=true"
                    , HttpMethod.GET, requestEntity1
                    , ArrayList.class).getBody();

            LinkedHashMap<String, String> link = passwordRequests.get(0);
log.info("link : {}", link);
            restTemplate.put(clientUserUrl + "/" + link.get("id") + "/reset-password-email",
                    requestEntity, String.class);

        } catch (HttpClientErrorException httpClientErrorException) {
            throw new IncorrectUsernameOrPasswordException("password or username is incorrect");
        } catch (Exception e) {
            throw new RecordNotFoundException("User not found");
        }
    }

    private void getAdminToken() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        LinkedMultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("grant_type", grantType);
        map.add("client_id", "admin-cli");
        map.add("username", "admin");
        map.add("password", "admin");

        HttpEntity<LinkedMultiValueMap<String, String>> httpEntity = new HttpEntity<>(map, headers);
//        log.info("request : {}", httpEntity);
        try {
            loginResponse = restTemplate.postForObject(masterUrl, httpEntity, LoginResponse.class);
        } catch (HttpClientErrorException httpClientErrorException) {
            throw new IncorrectUsernameOrPasswordException("password or username is incorrect");
        } catch (Exception e) {
            throw new FailedToProcessRequestException("unable to process request");
        }
    }
}




