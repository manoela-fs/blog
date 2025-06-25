# 🌍 DailyLog

> Uma aplicação web para compartilhar experiências, vivências e reflexões do dia a dia — com suporte multilíngue automático.


## 📌 Sobre o Projeto

O **DailyLog** é uma plataforma web desenvolvida para permitir que usuários registrem e compartilhem suas experiências cotidianas. Cada postagem pode ser organizada por categorias, curtida por outros usuários e traduzida automaticamente para diversos idiomas.

A aplicação foi desenvolvida em Java com **Spring Boot**, utilizando **MySQL** como banco de dados e a API [**LibreTranslate**](https://libretranslate.com/) para tradução automática, totalmente integrada e gerenciada via Docker.


## ✅ Funcionalidades

- [x] Cadastro e autenticação de usuários
- [x] Criação, edição e exclusão de postagens
- [x] Organização de postagens por categorias
- [x] Sistema de curtidas
- [x] Tradução automática das postagens com LibreTranslate
- [x] Upload e gerenciamento de arquivos (imagens, documentos, etc.)
- [x] Suporte à internacionalização (interface multilíngue)


## 🛠️ Funcionalidades planejadas (ToDo)

- [ ] Sistema de comentários


## 🚀 Tecnologias Utilizadas

- **Java 24 (JDK 24)**
- **Spring Boot**
- **Spring Security**
- **Spring Data JPA**
- **MySQL** (via Docker)
- **LibreTranslate API** (via Docker)
- **Thymeleaf** (Template Engine)
- **Docker & Docker Compose**


## ⚙️ Instalação e Configuração

### 🔧 Pré-requisitos

- Docker e Docker Compose instalados
- JDK 24 configurado na sua IDE (IntelliJ, Eclipse, etc.)

### 📥 Clone o repositório

```bash
git clone https://github.com/manoela-fs/blog.git
cd blog
```

### 🐳 Suba os serviços do Docker (MySQL + LibreTranslate)

```bash
cd src/main/java/com/manoela/blog
docker-compose up -d
```
### ▶️ Execute a aplicação

Abra o projeto em sua IDE e execute a classe `BlogApplication` como uma aplicação Spring Boot.


### 🌐 Acesse no navegador

[http://localhost:8080](http://localhost:8080)


## Devlog do Projeto

Durante o desenvolvimento do DailyLog, foram registradas as principais etapas, desafios e aprendizados.

Para acessar o devlog completo em formato PDF, clique no link abaixo:

[Devlog Completo - PDF](https://drive.google.com/file/d/1fhOa1uAxDIM9tChgp748oKHrweJeJAwc/view?usp=sharing)

## 👩‍💻 Autoria

Projeto desenvolvido por **Manoela Fernandes Simão** como parte da disciplina **Laboratório de Desenvolvimento de Sistemas** no curso de **Ciência da Computação** do **Instituto Federal de Santa Catarina (IFSC)** — Campus Lages.
