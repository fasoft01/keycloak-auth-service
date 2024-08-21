package zw.co.fasoft;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

/**
 * @author Farai Matsika
 * 9/6/2024
 */

@Data
@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/password")
@Tag(name = "Password", description = "Password")
@CrossOrigin
@SecurityRequirement(name = "authorization")
public class PasswordController {
    private final AuthenticationService authService;

    @PostMapping("/resetPassword")
    @Operation(description = "Password reset")
    public ResponseEntity<String> passwordReset(
            @RequestBody PasswordResetRequest passwordResetRequest, Principal principal
            ) {
        authService.resetPassword(passwordResetRequest, principal.getName());
        return ResponseEntity.ok().body("Password reset successfully");
    }

    @PostMapping("/update-password")
    @Operation(description = "Password reset")
    public ResponseEntity<String> updatePassword(
            @RequestBody UpdatePasswordRequest request, String username
    ) {
        authService.updatePassword(request, username);
        return ResponseEntity.ok().body("Password reset successfully");
    }
}
