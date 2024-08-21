package zw.co.fasoft;

import com.fasterxml.jackson.core.JsonProcessingException;

public interface AuthenticationService {
   LoginResponse login(LoginRequest request);

    void resetPassword(PasswordResetRequest request, String username);
    void updatePassword(UpdatePasswordRequest request, String username);
    Responses forgotPassword(String username);

    TokenResponse refreshToken(RefreshTokenRequest refreshTokenRequest) throws JsonProcessingException;
}
