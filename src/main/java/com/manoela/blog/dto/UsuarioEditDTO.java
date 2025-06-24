package com.manoela.blog.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class UsuarioEditDTO {

    @NotBlank(message = "O nome é obrigatório")
    private String nome;

    @NotBlank(message = "O email é obrigatório")
    @Email(message = "Email inválido")
    private String email;

    @NotBlank(message = "O idioma é obrigatório")
    private String idioma;

    private String fotoAtual;
    private MultipartFile foto;

    @NotBlank(message = "A senha atual é obrigatória")
    private String senhaAtual;

    private String novaSenha;
    private String confirmarSenha;
}
