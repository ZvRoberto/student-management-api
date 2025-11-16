package com.upana.studentmanagement.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentDTO {

    private Long id;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 255, message = "El nombre no puede exceder los 255 caracteres")
    private String nombre;

    @NotBlank(message = "El correo es obligatorio")
    @Email(message = "El correo debe tener un formato válido")
    private String correo;

    @NotBlank(message = "El número de teléfono es obligatorio")
    @Pattern(regexp = "^\\d{10}$", message = "El número de teléfono debe contener exactamente 10 dígitos")
    @JsonProperty("numero_telefono")
    private String numeroTelefono;

    @NotBlank(message = "El idioma es obligatorio")
    @Pattern(regexp = "^(inglés|español|francés)$",
            message = "El idioma debe ser: inglés, español o francés")
    private String idioma;
}