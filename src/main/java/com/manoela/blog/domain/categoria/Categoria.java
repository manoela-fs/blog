package com.manoela.blog.domain.categoria;

import jakarta.persistence.*;
import lombok.*;

/**
 * Representa uma categoria no sistema.
 * Cada categoria possui um identificador único.
 */
@Entity
@Table(name = "categoria")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Categoria {

    /**
     * Identificador único da categoria.
     * Gerado automaticamente pelo banco de dados.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
}
