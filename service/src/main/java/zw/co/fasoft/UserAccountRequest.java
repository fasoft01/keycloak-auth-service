package zw.co.fasoft;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserAccountRequest {
    private String fullName;
    private String email;
    private String username;
    private String phoneNumber;
    @Enumerated(EnumType.STRING)
    private UserGroup role;
}
