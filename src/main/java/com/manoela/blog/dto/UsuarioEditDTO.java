package com.manoela.blog.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

/**
 * DTO para edição dos dados do usuário.
 * Contém os campos que podem ser atualizados, incluindo validações para campos obrigatórios.
 * Usado para transferir dados da camada de apresentação para a de serviço/controlador.
 */
@Data
public class UsuarioEditDTO {

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
     * Idioma preferencial do usuário.
     * Campo obrigatório.
     */
    @NotBlank(message = "O idioma é obrigatório")
    private String idioma;

    /**
     * Nome do arquivo da foto atual do usuário (se existir).
     * Usado para exibir ou manter a foto atual caso não seja alterada.
     */
    private String fotoAtual;

    /**
     * Arquivo da nova foto enviada para atualização do perfil.
     * Pode ser nulo caso o usuário não deseje alterar a foto.
     */
    private MultipartFile foto;

    /**
     * Senha atual do usuário.
     * Campo obrigatório para validação antes de permitir alterações sensíveis.
     */
    @NotBlank(message = "A senha atual é obrigatória")
    private String senhaAtual;

    /**
     * Nova senha do usuário.
     * Pode ser nula ou vazia caso o usuário não deseje alterar a senha.
     */
    private String novaSenha;

    /**
     * Confirmação da nova senha.
     * Deve ser igual à nova senha para validação correta.
     */
    private String confirmarSenha;
}
