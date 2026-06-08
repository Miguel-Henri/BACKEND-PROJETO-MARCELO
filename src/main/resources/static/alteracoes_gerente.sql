-- =========================================================
-- ALTERAÇÕES PARA A FEATURE DE GERENTE
-- Execute este script no banco ifbank já existente.
-- Se for rodar do zero, use banco.sql completo abaixo.
-- =========================================================

USE ifbank;

-- 1. Adiciona a coluna status na tabela contas
--    (pula se já existir)
ALTER TABLE contas
    ADD COLUMN IF NOT EXISTS status VARCHAR(20) DEFAULT 'PENDENTE'
    CHECK (status IN ('PENDENTE', 'ATIVA', 'BLOQUEADA'));

-- 2. Adiciona PENDENTE ao check de status dos usuários
--    (MySQL não tem ALTER CHECK simples; recria a constraint)
ALTER TABLE usuarios
    DROP CHECK IF EXISTS usuarios_chk_1;

ALTER TABLE usuarios
    ADD CONSTRAINT usuarios_status_check
    CHECK (status IN ('ATIVO', 'INATIVO', 'BLOQUEADO', 'PENDENTE'));

COMMIT;


-- =========================================================
-- BANCO COMPLETO (do zero) — banco.sql atualizado
-- =========================================================

-- CREATE DATABASE IF NOT EXISTS ifbank;
-- USE ifbank;
--
-- CREATE TABLE usuarios (
--     id                          INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
--     nome                        VARCHAR(255) NOT NULL,
--     email                       VARCHAR(255) UNIQUE NOT NULL,
--     senha                       VARCHAR(255) NOT NULL,
--     telefone                    VARCHAR(20),
--     endereco                    VARCHAR(50),
--     foto_perfil                 VARCHAR(500),
--     status                      VARCHAR(20) DEFAULT 'PENDENTE'
--                                 CHECK (status IN ('ATIVO', 'INATIVO', 'BLOQUEADO', 'PENDENTE')),
--     data_criacao                TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
--     data_ultima_atualizacao     TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
-- );
--
-- CREATE TABLE contas (
--     id           INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
--     agencia      INT DEFAULT 5 NOT NULL,
--     numero_conta INT NOT NULL,
--     usuario_id   INT NOT NULL,
--     saldo        DECIMAL(15, 2) DEFAULT 0.00,
--     tipo         VARCHAR(20) DEFAULT 'CLIENTE' CHECK (tipo IN ('CLIENTE', 'GERENTE')),
--     status       VARCHAR(20) DEFAULT 'PENDENTE' CHECK (status IN ('PENDENTE', 'ATIVA', 'BLOQUEADA')),
--     data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
--     CONSTRAINT fk_contas_usuarios FOREIGN KEY (usuario_id) REFERENCES usuarios (id)
-- );
-- (demais tabelas sem alteração — ver banco.sql original)
