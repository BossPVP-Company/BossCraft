package com.bosspvp.api.skills.violation;

import org.jetbrains.annotations.NotNull;

public record ConfigViolation(@NotNull String param, @NotNull String message){
}