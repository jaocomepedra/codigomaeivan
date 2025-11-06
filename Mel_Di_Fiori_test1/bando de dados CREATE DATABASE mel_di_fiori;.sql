-- Cria o banco de dados
CREATE DATABASE IF NOT EXISTS mel_di_fiori;

-- Usa o banco
USE mel_di_fiori;

-- Tabela de usuários
CREATE TABLE IF NOT EXISTS usuarios (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    senha VARCHAR(255) NOT NULL,
    perfil VARCHAR(50) NOT NULL,
    data_cadastro DATE NOT NULL,
    status VARCHAR(50) NOT NULL
);

-- Tabela de clientes
CREATE TABLE IF NOT EXISTS clientes (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    telefone VARCHAR(50) NOT NULL,
    endereco TEXT,
    cidade VARCHAR(100),
    estado VARCHAR(50),
    data_cadastro DATE NOT NULL,
    status VARCHAR(50) NOT NULL
);

-- Tabela de colmeias
CREATE TABLE IF NOT EXISTS colmeias (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    identificacao VARCHAR(255) NOT NULL,
    localizacao VARCHAR(255) NOT NULL,
    tipo VARCHAR(100) NOT NULL,
    status VARCHAR(50) NOT NULL,
    data_instalacao DATE NOT NULL,
    numero_quadros INT,
    observacoes TEXT,
    data_ultima_rega DATE,
    entrou_lista_rega DATE,
    status_rega VARCHAR(50)
);

-- Insere usuário admin (senha: 123456)
INSERT INTO usuarios (nome, email, senha, perfil, data_cadastro, status) 
VALUES ('Administrador', 'admin@floricultura.com', 'e10adc3949ba59abbe56e057f20f883e', 'ADMIN', CURDATE(), 'ATIVO');

-- Insere clientes de exemplo
INSERT INTO clientes (nome, email, telefone, endereco, cidade, estado, data_cadastro, status) 
VALUES ('João Silva', 'joao@email.com', '(11) 99999-9999', 'Rua A, 123', 'São Paulo', 'SP', CURDATE(), 'ATIVO');

INSERT INTO clientes (nome, email, telefone, endereco, cidade, estado, data_cadastro, status) 
VALUES ('Maria Santos', 'maria@email.com', '(11) 88888-8888', 'Rua B, 456', 'Rio de Janeiro', 'RJ', CURDATE(), 'ATIVO');

-- Insere colmeias de exemplo
INSERT INTO colmeias (identificacao, localizacao, tipo, status, data_instalacao, numero_quadros, observacoes, data_ultima_rega, status_rega) 
VALUES ('Planta A1', 'Estufa Principal', 'Terra', 'Ativa', CURDATE(), 10, 'Planta saudável', CURDATE(), 'NORMAL');

INSERT INTO colmeias (identificacao, localizacao, tipo, status, data_instalacao, numero_quadros, observacoes, data_ultima_rega, status_rega) 
VALUES ('Planta B2', 'Jardim Externo', 'Agua', 'Ativa', CURDATE(), 8, 'Necessita mais sol', CURDATE(), 'NORMAL');