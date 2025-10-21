package com.example.web_lab4.dto.request;

import com.example.web_lab4.dto.groups.OnCreate;
import com.example.web_lab4.dto.groups.OnUpdate;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRequestDTO {
    @NotNull(groups = OnUpdate.class)
    @Null(groups = OnCreate.class)
    private Long id;

    @NotNull(groups = OnCreate.class)
    private String name;

    @NotNull(groups = OnCreate.class)
    private String password;
}
