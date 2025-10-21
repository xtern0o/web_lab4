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
public class PointRequestDTO {
    @NotNull(groups = OnUpdate.class)
    @Null(groups = OnCreate.class)
    private Long id;

    @NotNull(groups = {OnCreate.class, OnUpdate.class})
    @DecimalMin(value = "-5.0", groups = {OnCreate.class, OnUpdate.class})
    @DecimalMax(value = "3.0", groups = {OnCreate.class, OnUpdate.class})
    private Float x;

    @NotNull(groups = {OnCreate.class, OnUpdate.class})
    @DecimalMin(value = "-5.0", groups = {OnCreate.class, OnUpdate.class})
    @DecimalMax(value = "3.0", groups = {OnCreate.class, OnUpdate.class})
    private Float y;

    @NotNull(groups = {OnCreate.class, OnUpdate.class})
    @DecimalMin(value = "-5.0", groups = {OnCreate.class, OnUpdate.class})
    @DecimalMax(value = "3.0", groups = {OnCreate.class, OnUpdate.class})
    private Float r;

}
