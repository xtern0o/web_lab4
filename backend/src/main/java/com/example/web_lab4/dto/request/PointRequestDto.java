package com.example.web_lab4.dto.request;

import com.example.web_lab4.dto.groups.OnCreate;
import com.example.web_lab4.dto.groups.OnUpdate;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PointRequestDto {
    @NotNull(groups = OnUpdate.class)
    @Null(groups = OnCreate.class)
    private Long id;

    @NotNull
    @DecimalMin(value = "-5.0")
    @DecimalMax(value = "3.0")
    private Float x;

    @NotNull
    @DecimalMin(value = "-5.0")
    @DecimalMax(value = "3.0")
    private Float y;

    @NotNull
    @DecimalMin(value = "-5.0")
    @DecimalMax(value = "3.0")
    private Float r;

}
