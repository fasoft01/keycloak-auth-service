package zw.co.fasoft;


import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

/**
 * @author Fasoft
 * @date 31/May/2024
 */
@Data
@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/auth")
@Tag(name = "Authentication", description = "authentication")
@CrossOrigin
//@SecurityRequirement(name = "authorization")
public class AuthController {
    private final AuthenticationService authService;

    @PostMapping("/login")
    @Operation(description = "Login")
    public ResponseEntity<LoginResponse> login(
            @RequestBody LoginRequest loginRequest
    ) {
        return ResponseEntity.ok(authService.login(loginRequest));
    }

    @PostMapping()
    @Operation(description = "Create A user")
    public ResponseEntity<UserAccountRequest> createUser(@RequestBody UserAccountRequest userAccountRequest){
        return ResponseEntity.ok(authService.createUser(userAccountRequest));
    }
    @PostMapping("/forgot-password")
    @Operation(description = "forgot password ")
    public ResponseEntity<Responses> forgotPassword(@RequestParam String username) {
        return ResponseEntity.ok(authService.forgotPassword(username));
    }

    @PostMapping("/refresh")
    @Operation(description = "Refresh token")
    public ResponseEntity<TokenResponse> refreshToken(
            @RequestBody RefreshTokenRequest refreshTokenRequest
    ) throws JsonProcessingException {
        return ResponseEntity.ok(authService.refreshToken(refreshTokenRequest));
    }
}