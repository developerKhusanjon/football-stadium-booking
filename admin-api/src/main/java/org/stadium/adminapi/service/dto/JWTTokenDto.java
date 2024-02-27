package org.stadium.adminapi.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class JWTTokenDto {
    private String token;
    private boolean success;

    public JWTTokenDto(String token) {
        this.token = token;
    }

    @JsonProperty("id_token")
    public String getToken() {
        return token;
    }

    public void setIdToken(String token) {
        this.token = token;
    }

}
