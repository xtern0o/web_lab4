package com.example.web_lab4.dto.request;

import com.example.web_lab4.dto.groups.OnCreate;
import com.example.web_lab4.dto.groups.OnUpdate;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.groups.Default;
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

    @NotNull(groups = {Default.class, OnCreate.class}, message = "REQUIRED fields are: {x, y, r}")
    @DecimalMin(value = "-5.0", groups = {Default.class, OnCreate.class}, message = "X должен быть от -5 до 3 не включительно")
    @DecimalMax(value = "3.0", groups = {Default.class, OnCreate.class}, message = "X должен быть от -5 до 3 не включительно")
    private Float x;

    @NotNull(groups = {Default.class, OnCreate.class}, message = "REQUIRED fields are: {x, y, r}")
    @DecimalMin(value = "-5.0", groups = {Default.class, OnCreate.class}, message = "Y должен быть от -5 до 3 не включительно")
    @DecimalMax(value = "3.0", groups = {Default.class, OnCreate.class}, message = "Y должен быть от -5 до 3 не включительно")
    private Float y;

    @NotNull(groups = {Default.class, OnCreate.class}, message = "REQUIRED fields are: {x, y, r}")
    @DecimalMin(value = "0.0000001", groups = {Default.class, OnCreate.class}, message = "R должен быть от 0 до 3 не включительно")
    @DecimalMax(value = "2.9999999", groups = {Default.class, OnCreate.class}, message = "R должен быть от 0 до 3 не включительно")
    private Float r;

}
