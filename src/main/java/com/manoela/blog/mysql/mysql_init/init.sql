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

-- Inserção de usuários com UUIDs e senhas iguais ao nome (criptografadas com bcrypt)
INSERT INTO usuario (id, nome, email, senha, foto, idioma) VALUES
                                                               ('7106fe58-02e0-4541-a3fa-f33d2b36b0d3', 'ana',   'ana@ana',   '$2a$10$oMgOTS7H0oXRO2hY5XwG9ePi102bSAi6YyMRpnuEuyfEXrKQx6122', NULL, 'pt-BR'),
                                                               ('b801f4a3-1b7e-4e4d-a5a6-19d91e3ea562', 'bruno', 'bruno@bruno','$2a$10$850icgVgI/CheQcVjX8TWubvM4ArojAa5o/3czl4t/6NHInAIV7HC', NULL, 'pt-BR'),
                                                               ('f8d7cb61-eaa2-4171-b3f5-b1fa6be5154b', 'carla', 'carla@carla','$2a$10$ogh9rdIh/99mMEmJ4YsbwOXkAohFWIhECFEbbhr7aE.gxzl2RZzhG', NULL, 'pt-BR'),
                                                               ('b6a4313f-04a9-4d11-bdcc-6f8db8b364e3', 'diego', 'diego@diego','$2a$10$ek9j2fnYTcPM/VSre/1QyulJB0MrhMvb3h6AvLBkb5PJ7WXiwztvy', NULL, 'pt-BR'),
                                                               ('8a27aa57-c84e-469f-9dc9-c2f0b1d7eae5', 'edu',   'edu@edu',   '$2a$10$V7DqgcLkPWaqUqP.Adj2WeHSQC4.sxDjI3kfxseZ3zNxeHyWHbD9y', NULL, 'pt-BR'),
                                                               ('cfcbfd10-b1ec-4b5c-b899-09d4c3d3289f', 'root',  'root@root', '$2a$10$yI9Wb4wrIruw8gyirefIX.vI2kRLN0Cxu2JOCUzUIyCzfNDomw1VG', NULL, 'pt-BR');

-- Inserção de postagens
INSERT INTO postagem (id, imagem, categoria_id, usuario_id) VALUES
-- Ana
('3e6a1b27-6a4c-4a14-b97f-48f2cb60dc50', NULL, 1, '7106fe58-02e0-4541-a3fa-f33d2b36b0d3'),
('d7b71a59-598c-4a34-8448-982230f7b641', 'img-default.webp', 2, '7106fe58-02e0-4541-a3fa-f33d2b36b0d3'),
('c5fcfd84-eacd-4f55-9055-4cdab49a9947', NULL, 3, '7106fe58-02e0-4541-a3fa-f33d2b36b0d3'),

-- Bruno
('a3cfb49e-f191-4d3b-950b-d42a6e408b5f', 'img-default.webp', 1, 'b801f4a3-1b7e-4e4d-a5a6-19d91e3ea562'),
('b2c4f0a1-8d15-40c6-92c5-0f5c4e14db65', NULL, 2, 'b801f4a3-1b7e-4e4d-a5a6-19d91e3ea562'),
('7fcd205e-b52d-4c3f-8120-e8a319a46b6a', NULL, 3, 'b801f4a3-1b7e-4e4d-a5a6-19d91e3ea562'),

-- Carla
('ec733d52-e44d-44b8-b52c-b55b1d74640b', NULL, 1, 'f8d7cb61-eaa2-4171-b3f5-b1fa6be5154b'),
('9987c22f-c72e-4b3a-92df-97ad2f4136e4', NULL, 2, 'f8d7cb61-eaa2-4171-b3f5-b1fa6be5154b'),
('fbd1a3a4-3925-4d08-90cb-d28b05350693', 'img-default.webp', 3, 'f8d7cb61-eaa2-4171-b3f5-b1fa6be5154b'),

-- Diego
('f404e0a1-6b7c-4132-882e-1a0a78b4dc4a', NULL, 1, 'b6a4313f-04a9-4d11-bdcc-6f8db8b364e3'),
('2a64bcfd-2932-43ac-8137-7f9981ec9b7f', 'img-default.webp', 2, 'b6a4313f-04a9-4d11-bdcc-6f8db8b364e3'),
('d2cd0895-24bc-4705-b68d-0d6f116f803c', 'img-default.webp', 3, 'b6a4313f-04a9-4d11-bdcc-6f8db8b364e3'),

-- Edu
('35c8b2b4-c108-41aa-b4d4-49b62a7159e6', 'img-default.webp', 1, '8a27aa57-c84e-469f-9dc9-c2f0b1d7eae5'),
('16dd0276-1c7d-49ff-8c53-bf82be6e6a9c', 'img-default.webp', 2, '8a27aa57-c84e-469f-9dc9-c2f0b1d7eae5'),
('738f4978-3db4-4b97-b221-07359f226377', NULL, 3, '8a27aa57-c84e-469f-9dc9-c2f0b1d7eae5'),

-- Root
('e72322a4-ef1a-4706-8f9a-0f06e3bcdbdc', NULL, 1, 'cfcbfd10-b1ec-4b5c-b899-09d4c3d3289f'),
('9c83e842-f6f1-4f7e-b327-fc5be0219447', NULL, 2, 'cfcbfd10-b1ec-4b5c-b899-09d4c3d3289f'),
('49ae6f56-7d29-4a07-a08e-5c315a1f303c', 'img-default.webp', 3, 'cfcbfd10-b1ec-4b5c-b899-09d4c3d3289f');

-- Traduções das postagens
INSERT INTO postagem_traducao (postagem_id, idioma, titulo, conteudo) VALUES
-- Ana
('3e6a1b27-6a4c-4a14-b97f-48f2cb60dc50', 'pt-BR', 'A evolução da inteligência artificial', 'Explorando os avanços em IA na última década.'),
('3e6a1b27-6a4c-4a14-b97f-48f2cb60dc50', 'en',    'The evolution of artificial intelligence', 'Exploring AI advancements in the last decade.'),
('3e6a1b27-6a4c-4a14-b97f-48f2cb60dc50', 'es',    'La evolución de la inteligencia artificial', 'Explorando los avances de la IA en la última década.'),

('d7b71a59-598c-4a34-8448-982230f7b641', 'pt-BR', 'Dicas de saúde para o inverno', 'Como manter sua saúde durante os dias frios.'),
('d7b71a59-598c-4a34-8448-982230f7b641', 'en',    'Health tips for winter', 'How to stay healthy during cold days.'),
('d7b71a59-598c-4a34-8448-982230f7b641', 'es',    'Consejos de salud para el invierno', 'Cómo mantener la salud durante los días fríos.'),

('c5fcfd84-eacd-4f55-9055-4cdab49a9947', 'pt-BR', 'Métodos modernos de ensino', 'O impacto da tecnologia na educação atual.'),
('c5fcfd84-eacd-4f55-9055-4cdab49a9947', 'en',    'Modern teaching methods', 'The impact of technology on today’s education.'),
('c5fcfd84-eacd-4f55-9055-4cdab49a9947', 'es',    'Métodos modernos de enseñanza', 'El impacto de la tecnología en la educación actual.'),

-- Bruno
('a3cfb49e-f191-4d3b-950b-d42a6e408b5f', 'pt-BR', 'Futuro dos dispositivos móveis', 'Novas tendências em smartphones e wearables.'),
('a3cfb49e-f191-4d3b-950b-d42a6e408b5f', 'en',    'Future of mobile devices', 'New trends in smartphones and wearables.'),
('a3cfb49e-f191-4d3b-950b-d42a6e408b5f', 'es',    'Futuro de los dispositivos móviles', 'Nuevas tendencias en smartphones y wearables.'),

('b2c4f0a1-8d15-40c6-92c5-0f5c4e14db65', 'pt-BR', 'Alimentação saudável', 'Como manter uma dieta equilibrada no dia a dia.'),
('b2c4f0a1-8d15-40c6-92c5-0f5c4e14db65', 'en',    'Healthy eating', 'How to maintain a balanced diet daily.'),
('b2c4f0a1-8d15-40c6-92c5-0f5c4e14db65', 'es',    'Alimentación saludable', 'Cómo mantener una dieta equilibrada a diario.'),

('7fcd205e-b52d-4c3f-8120-e8a319a46b6a', 'pt-BR', 'A importância da leitura', 'Ler amplia horizontes e desenvolve o pensamento crítico.'),
('7fcd205e-b52d-4c3f-8120-e8a319a46b6a', 'en',    'The importance of reading', 'Reading expands horizons and develops critical thinking.'),
('7fcd205e-b52d-4c3f-8120-e8a319a46b6a', 'es',    'La importancia de la lectura', 'Leer amplía horizontes y desarrolla el pensamiento crítico.'),

-- Carla
('ec733d52-e44d-44b8-b52c-b55b1d74640b', 'pt-BR', 'Inovações em realidade aumentada', 'Como a RA está mudando a forma como interagimos com o mundo.'),
('ec733d52-e44d-44b8-b52c-b55b1d74640b', 'en',    'Innovations in augmented reality', 'How AR is changing how we interact with the world.'),
('ec733d52-e44d-44b8-b52c-b55b1d74640b', 'es',    'Innovaciones en realidad aumentada', 'Cómo la RA está cambiando la forma en que interactuamos con el mundo.'),

('9987c22f-c72e-4b3a-92df-97ad2f4136e4', 'pt-BR', 'A importância do sono', 'Dormir bem melhora a saúde física e mental.'),
('9987c22f-c72e-4b3a-92df-97ad2f4136e4', 'en',    'The importance of sleep', 'Sleeping well improves physical and mental health.'),
('9987c22f-c72e-4b3a-92df-97ad2f4136e4', 'es',    'La importancia del sueño', 'Dormir bien mejora la salud física y mental.'),

('fbd1a3a4-3925-4d08-90cb-d28b05350693', 'pt-BR', 'Educação inclusiva', 'A importância de garantir acesso à educação para todos.'),
('fbd1a3a4-3925-4d08-90cb-d28b05350693', 'en',    'Inclusive education', 'The importance of ensuring access to education for all.'),
('fbd1a3a4-3925-4d08-90cb-d28b05350693', 'es',    'Educación inclusiva', 'La importancia de garantizar el acceso a la educación para todos.'),

-- Diego
('f404e0a1-6b7c-4132-882e-1a0a78b4dc4a', 'pt-BR', 'Computação quântica', 'O que é e como pode revolucionar o futuro.'),
('f404e0a1-6b7c-4132-882e-1a0a78b4dc4a', 'en',    'Quantum computing', 'What it is and how it could revolutionize the future.'),
('f404e0a1-6b7c-4132-882e-1a0a78b4dc4a', 'es',    'Computación cuántica', 'Qué es y cómo puede revolucionar el futuro.'),

('2a64bcfd-2932-43ac-8137-7f9981ec9b7f', 'pt-BR', 'Exercícios para iniciantes', 'Como começar uma rotina de atividades físicas.'),
('2a64bcfd-2932-43ac-8137-7f9981ec9b7f', 'en',    'Exercises for beginners', 'How to start a physical activity routine.'),
('2a64bcfd-2932-43ac-8137-7f9981ec9b7f', 'es',    'Ejercicios para principiantes', 'Cómo comenzar una rutina de actividad física.'),

('d2cd0895-24bc-4705-b68d-0d6f116f803c', 'pt-BR', 'Ensino híbrido', 'A união entre presencial e online na educação.'),
('d2cd0895-24bc-4705-b68d-0d6f116f803c', 'en',    'Hybrid teaching', 'Combining in-person and online education.'),
('d2cd0895-24bc-4705-b68d-0d6f116f803c', 'es',    'Enseñanza híbrida', 'La unión entre lo presencial y lo online en la educación.'),

-- Edu
('35c8b2b4-c108-41aa-b4d4-49b62a7159e6', 'pt-BR', 'Robótica no cotidiano', 'Aplicações práticas da robótica em casa e no trabalho.'),
('35c8b2b4-c108-41aa-b4d4-49b62a7159e6', 'en',    'Robotics in everyday life', 'Practical applications of robotics at home and work.'),
('35c8b2b4-c108-41aa-b4d4-49b62a7159e6', 'es',    'Robótica en la vida cotidiana', 'Aplicaciones prácticas de la robótica en el hogar y el trabajo.'),

('16dd0276-1c7d-49ff-8c53-bf82be6e6a9c', 'pt-BR', 'Meditação e bem-estar', 'Como a meditação pode ajudar na saúde mental.'),
('16dd0276-1c7d-49ff-8c53-bf82be6e6a9c', 'en',    'Meditation and wellness', 'How meditation can help with mental health.'),
('16dd0276-1c7d-49ff-8c53-bf82be6e6a9c', 'es',    'Meditación y bienestar', 'Cómo la meditación puede ayudar con la salud mental.'),

('738f4978-3db4-4b97-b221-07359f226377', 'pt-BR', 'Gamificação na aprendizagem', 'Jogos como ferramentas educacionais eficazes.'),
('738f4978-3db4-4b97-b221-07359f226377', 'en',    'Gamification in learning', 'Games as effective educational tools.'),
('738f4978-3db4-4b97-b221-07359f226377', 'es',    'Gamificación en el aprendizaje', 'Los juegos como herramientas educativas eficaces.'),

-- Root
('e72322a4-ef1a-4706-8f9a-0f06e3bcdbdc', 'pt-BR', 'Cibersegurança', 'Como proteger seus dados na internet.'),
('e72322a4-ef1a-4706-8f9a-0f06e3bcdbdc', 'en',    'Cybersecurity', 'How to protect your data online.'),
('e72322a4-ef1a-4706-8f9a-0f06e3bcdbdc', 'es',    'Ciberseguridad', 'Cómo proteger tus datos en internet.'),

('9c83e842-f6f1-4f7e-b327-fc5be0219447', 'pt-BR', 'Importância da hidratação', 'Beber água regularmente é essencial para a saúde.'),
('9c83e842-f6f1-4f7e-b327-fc5be0219447', 'en',    'Importance of hydration', 'Drinking water regularly is essential for health.'),
('9c83e842-f6f1-4f7e-b327-fc5be0219447', 'es',    'Importancia de la hidratación', 'Beber agua regularmente es esencial para la salud.'),

('49ae6f56-7d29-4a07-a08e-5c315a1f303c', 'pt-BR', 'A sala de aula do futuro', 'Como a tecnologia está moldando o ensino.'),
('49ae6f56-7d29-4a07-a08e-5c315a1f303c', 'en',    'The classroom of the future', 'How technology is shaping education.'),
('49ae6f56-7d29-4a07-a08e-5c315a1f303c', 'es',    'El aula del futuro', 'Cómo la tecnología está transformando la enseñanza.');

INSERT INTO curtida (usuario_id, postagem_id) VALUES
-- Ana curte postagens de Bruno e Carla
('7106fe58-02e0-4541-a3fa-f33d2b36b0d3', 'a3cfb49e-f191-4d3b-950b-d42a6e408b5f'),
('7106fe58-02e0-4541-a3fa-f33d2b36b0d3', '9987c22f-c72e-4b3a-92df-97ad2f4136e4'),

-- Bruno curte postagens de Ana e Diego
('b801f4a3-1b7e-4e4d-a5a6-19d91e3ea562', '3e6a1b27-6a4c-4a14-b97f-48f2cb60dc50'),
('b801f4a3-1b7e-4e4d-a5a6-19d91e3ea562', '2a64bcfd-2932-43ac-8137-7f9981ec9b7f'),

-- Carla curte postagens de Bruno e Edu
('f8d7cb61-eaa2-4171-b3f5-b1fa6be5154b', 'b2c4f0a1-8d15-40c6-92c5-0f5c4e14db65'),
('f8d7cb61-eaa2-4171-b3f5-b1fa6be5154b', '738f4978-3db4-4b97-b221-07359f226377'),

-- Diego curte postagens de Ana e Root
('b6a4313f-04a9-4d11-bdcc-6f8db8b364e3', 'd7b71a59-598c-4a34-8448-982230f7b641'),
('b6a4313f-04a9-4d11-bdcc-6f8db8b364e3', 'e72322a4-ef1a-4706-8f9a-0f06e3bcdbdc'),

-- Edu curte postagens de Carla e Diego
('8a27aa57-c84e-469f-9dc9-c2f0b1d7eae5', 'ec733d52-e44d-44b8-b52c-b55b1d74640b'),
('8a27aa57-c84e-469f-9dc9-c2f0b1d7eae5', 'd2cd0895-24bc-4705-b68d-0d6f116f803c'),

-- Root curte postagens de Ana, Bruno e Edu
('cfcbfd10-b1ec-4b5c-b899-09d4c3d3289f', 'c5fcfd84-eacd-4f55-9055-4cdab49a9947'),
('cfcbfd10-b1ec-4b5c-b899-09d4c3d3289f', '7fcd205e-b52d-4c3f-8120-e8a319a46b6a'),
('cfcbfd10-b1ec-4b5c-b899-09d4c3d3289f', '16dd0276-1c7d-49ff-8c53-bf82be6e6a9c');

