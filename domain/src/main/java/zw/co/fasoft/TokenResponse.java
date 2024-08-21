package zw.co.fasoft;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
    public  class TokenResponse {
    @JsonProperty("access_token")
        private String accessToken;

    @JsonProperty("refresh_token")
        private String refreshToken;
}
