package zw.co.fasoft;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Embeddable
public class LoginRequest {
    @NotNull
    private String username;
    @NotNull
    private String password;
}
