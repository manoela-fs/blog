/*!40101 SET NAMES utf8mb4 */;
-- Apaga e recria o banco
DROP DATABASE IF EXISTS blog_db;

CREATE DATABASE blog_db
    DEFAULT CHARACTER SET utf8mb4
    DEFAULT COLLATE utf8mb4_unicode_ci;

USE blog_db;

-- Tabela: categoria
CREATE TABLE categoria (
                           id INT AUTO_INCREMENT PRIMARY KEY
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Traduções da categoria (PK composta)
CREATE TABLE categoria_traducao (
                                    categoria_id INT NOT NULL,
                                    idioma VARCHAR(5) NOT NULL,
                                    nome VARCHAR(100) NOT NULL,
                                    PRIMARY KEY (categoria_id, idioma),
                                    CONSTRAINT fk_categoria_traducao_categoria
                                        FOREIGN KEY (categoria_id) REFERENCES categoria(id)
                                            ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Tabela: usuario
CREATE TABLE usuario (
                         id CHAR(36) PRIMARY KEY,
                         nome VARCHAR(100) NOT NULL,
                         email VARCHAR(100) NOT NULL UNIQUE,
                         senha VARCHAR(255) NOT NULL,
                         foto VARCHAR(255),
                         idioma VARCHAR(5),
                         data_criacao DATETIME DEFAULT CURRENT_TIMESTAMP,
                         data_atualizacao DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Tabela: postagem
CREATE TABLE postagem (
                          id CHAR(36) PRIMARY KEY,
                          imagem VARCHAR(255),
                          categoria_id INT,
                          usuario_id CHAR(36),
                          data_criacao DATETIME DEFAULT CURRENT_TIMESTAMP,
                          data_atualizacao DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                          CONSTRAINT fk_postagem_categoria FOREIGN KEY (categoria_id) REFERENCES categoria(id)
                              ON DELETE SET NULL ON UPDATE CASCADE,
                          CONSTRAINT fk_postagem_usuario FOREIGN KEY (usuario_id) REFERENCES usuario(id)
                              ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Traduções da postagem
CREATE TABLE postagem_traducao (
                                   postagem_id CHAR(36) NOT NULL,
                                   idioma VARCHAR(5) NOT NULL,
                                   titulo VARCHAR(255) NOT NULL,
                                   conteudo TEXT NOT NULL,
                                   PRIMARY KEY (postagem_id, idioma),
                                   CONSTRAINT fk_postagem_traducao_postagem FOREIGN KEY (postagem_id) REFERENCES postagem(id)
                                       ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Tabela: comentario
CREATE TABLE comentario (
                            id CHAR(36) PRIMARY KEY,
                            usuario_id CHAR(36),
                            postagem_id CHAR(36),
                            data_criacao DATETIME DEFAULT CURRENT_TIMESTAMP,
                            CONSTRAINT fk_comentario_usuario FOREIGN KEY (usuario_id) REFERENCES usuario(id)
                                ON DELETE SET NULL ON UPDATE CASCADE,
                            CONSTRAINT fk_comentario_postagem FOREIGN KEY (postagem_id) REFERENCES postagem(id)
                                ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Traduções do comentário
CREATE TABLE comentario_traducao (
                                     comentario_id CHAR(36) NOT NULL,
                                     idioma VARCHAR(5) NOT NULL,
                                     comentario TEXT NOT NULL,
                                     PRIMARY KEY (comentario_id, idioma),
                                     CONSTRAINT fk_comentario_traducao_comentario FOREIGN KEY (comentario_id) REFERENCES comentario(id)
                                         ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Tabela: curtida
CREATE TABLE curtida (
                         usuario_id CHAR(36),
                         postagem_id CHAR(36),
                         data_curtida DATETIME DEFAULT CURRENT_TIMESTAMP,
                         PRIMARY KEY (usuario_id, postagem_id),
                         CONSTRAINT fk_curtida_usuario FOREIGN KEY (usuario_id) REFERENCES usuario(id)
                             ON DELETE CASCADE ON UPDATE CASCADE,
                         CONSTRAINT fk_curtida_postagem FOREIGN KEY (postagem_id) REFERENCES postagem(id)
                             ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Inserção de categorias
INSERT INTO categoria () VALUES (), (), (), (), (), (), (), (), (), ();

-- Traduções de categorias
INSERT INTO categoria_traducao (categoria_id, idioma, nome) VALUES
                                                                (1, 'pt-BR', 'Tecnologia'), (1, 'en', 'Technology'), (1, 'es', 'Tecnología'),
                                                                (2, 'pt-BR', 'Saúde'), (2, 'en', 'Health'), (2, 'es', 'Salud'),
                                                                (3, 'pt-BR', 'Educação'), (3, 'en', 'Education'), (3, 'es', 'Educación'),
                                                                (4, 'pt-BR', 'Esportes'), (4, 'en', 'Sports'), (4, 'es', 'Deportes'),
                                                                (5, 'pt-BR', 'Entretenimento'), (5, 'en', 'Entertainment'), (5, 'es', 'Entretenimiento'),
                                                                (6, 'pt-BR', 'Negócios'), (6, 'en', 'Business'), (6, 'es', 'Negocios'),
                                                                (7, 'pt-BR', 'Viagens'), (7, 'en', 'Travel'), (7, 'es', 'Viajes'),
                                                                (8, 'pt-BR', 'Culinária'), (8, 'en', 'Cooking'), (8, 'es', 'Cocina'),
                                                                (9, 'pt-BR', 'Moda'), (9, 'en', 'Fashion'), (9, 'es', 'Moda'),
                                                                (10, 'pt-BR', 'Ciência'), (10, 'en', 'Science'), (10, 'es', 'Ciencia');
