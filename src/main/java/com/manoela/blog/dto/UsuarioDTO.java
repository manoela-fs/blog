package com.manoela.blog.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

/**
 * DTO para criação de um novo usuário.
 * Contém os dados necessários para cadastro, com validações básicas.
 * Utilizado para transferência de dados da camada de apresentação para a de serviço/controlador.
 */
@Data
public class UsuarioDTO {

    /**
     * Nome completo do usuário.
     * Campo obrigatório.
     */
    @NotBlank(message = "O nome é obrigatório")
    private String nome;

    /**
     * Email do usuário.
     * Campo obrigatório e deve estar no formato válido de email.
     */
    @NotBlank(message = "O email é obrigatório")
    @Email(message = "Email inválido")
    private String email;

    /**
     * Senha do usuário.
     * Campo obrigatório.
     */
    @NotBlank(message = "A senha é obrigatória")
    private String senha;

    /**
     * Idioma preferencial do usuário.
     * Campo obrigatório.
     */
    @NotBlank(message = "O idioma é obrigatório")
    private String idioma;

    /**
     * Arquivo da foto de perfil do usuário.
     * Campo opcional, pode ser nulo se o usuário não enviar foto.
     */
    private MultipartFile foto;
}
