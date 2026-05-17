package com.edulearn.auth.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GoogleAuthRequest {
    private String googleId;
    private String email;
    private String name;
    private String pictureUrl;
}
