package com.optiroute.backend.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ChangePasswordRequest(@NotBlank String currentPassword, @NotBlank @Size(min = 5) String newPassword) {
}
