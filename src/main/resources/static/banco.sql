/* =========================================================
   TABELAS
   ========================================================= */
CREATE DATABASE IF NOT EXISTS ifbank;
USE ifbank;

CREATE TABLE usuarios (
    id                          INT NOT NULL PRIMARY KEY auto_increment,
    nome                        VARCHAR(255) NOT NULL,
    email                       VARCHAR(255) UNIQUE NOT NULL,
    senha                       VARCHAR(255) NOT NULL,
    telefone                    VARCHAR(255),
    endereco                    VARCHAR(50),
    foto_perfil 				VARCHAR(500),
    status                      VARCHAR(20) DEFAULT 'BLOQUEADO' CHECK ( status IN ( 'ATIVO', 'INATIVO', 'BLOQUEADO', 'PENDENTE' ) ),
    role                        VARCHAR(20) DEFAULT 'CLIENTE' NOT NULL CHECK ( role IN ( 'CLIENTE', 'GERENTE' ) ),
    data_criacao                TIMESTAMP DEFAULT current_timestamp,
    data_ultima_atualizacao     TIMESTAMP DEFAULT current_timestamp
);

CREATE TABLE contas (
    id           INT NOT NULL PRIMARY KEY auto_increment,
    agencia      INT DEFAULT 5 NOT NULL,
    numero_conta INT NOT NULL,
    usuario_id   INT NOT NULL,
    saldo        DECIMAL(15, 2) DEFAULT 0.00,
    role         VARCHAR(20) DEFAULT 'CLIENTE' CHECK ( role IN ( 'CLIENTE', 'GERENTE' ) ),
    status       VARCHAR(20) DEFAULT 'PENDENTE' CHECK ( status IN ( 'PENDENTE', 'ATIVA', 'BLOQUEADA' ) ),
    data_criacao TIMESTAMP DEFAULT current_timestamp,
    CONSTRAINT fk_contas_usuarios FOREIGN KEY ( usuario_id ) REFERENCES usuarios ( id )
);

CREATE TABLE movimentacoes (
    id               INT NOT NULL PRIMARY KEY auto_increment,
    conta_origem_id  INT,
    conta_destino_id INT,
    valor            DECIMAL(15, 2) NOT NULL,
    saldo_anterior   DECIMAL(15, 2) NOT NULL,
    saldo_posterior  DECIMAL(15, 2) NOT NULL,
    tipo             VARCHAR(50) CHECK ( tipo IN ( 'DEPOSITO', 'SAQUE', 'INVESTIMENTO', 'EMPRESTIMO', 'TRANSFERENCIA_ENVIADA',
                                        'TRANSFERENCIA_RECEBIDA', 'PAGAMENTO' ) ),
    data_transacao   TIMESTAMP DEFAULT current_timestamp,
    descricao        VARCHAR(200),
    status           VARCHAR(20) DEFAULT 'PENDENTE' CHECK ( status IN ( 'PENDENTE', 'CONCLUIDA', 'FALHA' ) ),
    CONSTRAINT fk_mov_conta_origem FOREIGN KEY ( conta_origem_id )
        REFERENCES contas ( id ),
    CONSTRAINT fk_mov_conta_destino FOREIGN KEY ( conta_destino_id )
        REFERENCES contas ( id )
);

CREATE TABLE investimentos (
    id                INT NOT NULL PRIMARY KEY auto_increment,
    conta_id          INT NOT NULL,
    tipo_investimento VARCHAR(100) NOT NULL,
    valor_investido   DECIMAL(15, 2) NOT NULL,
    data_inicio       DATETIME(6) DEFAULT NULL,
    data_fim          DATETIME(6),
    status            VARCHAR(20) DEFAULT 'ATIVO' CHECK ( status IN ( 'ATIVO', 'ENCERRADO' ) ),
    CONSTRAINT fk_invest_conta FOREIGN KEY ( conta_id ) REFERENCES contas ( id )
);

CREATE TABLE config_emprestimo (
    id                INT PRIMARY KEY auto_increment,
    taxa_juros_padrao DECIMAL(5, 2) NOT NULL
);

CREATE TABLE emprestimos (
    id                    INT NOT NULL PRIMARY KEY auto_increment,
    conta_id              INT NOT NULL,
    valor_emprestimo      DECIMAL(10, 2) NOT NULL,
    taxa_juros_mensal     DECIMAL(5, 2) NOT NULL,
    parcelas              INT NOT NULL,
    status                VARCHAR(30) DEFAULT 'SIMULADO' CHECK ( status IN ( 'SIMULADO', 'SOLICITADO', 'APROVADO', 'REJEITADO', 'EM_ANDAMENTO', 'QUITADO' ) ),
    data_solicitacao      DATETIME(6) DEFAULT NULL,
    data_aprovacao        DATETIME(6),
    data_ultimo_pagamento DATETIME(6),
    CONSTRAINT fk_emprestimo_conta FOREIGN KEY ( conta_id ) REFERENCES contas ( id ),
    CONSTRAINT uk_emprestimo_duplicado UNIQUE ( conta_id, valor_emprestimo, parcelas, taxa_juros_mensal )
);

CREATE TABLE parcelas_emprestimo (
    id                INT NOT NULL PRIMARY KEY auto_increment,
    emprestimo_id     INT NOT NULL,
    numero_parcela    INT NOT NULL,
    valor_amortizacao DECIMAL(15, 2),
    valor_juros       DECIMAL(15, 2),
    valor_parcela     DECIMAL(15, 2) NOT NULL,
    data_vencimento   DATETIME(6) NOT NULL,
    data_pagamento    DATETIME(6),
    status            VARCHAR(20) DEFAULT 'PENDENTE' CHECK ( status IN ( 'PENDENTE', 'PAGO', 'ATRASADO' ) ),
    CONSTRAINT fk_parcelas_emprestimo FOREIGN KEY ( emprestimo_id ) REFERENCES emprestimos ( id )
);

CREATE TABLE tokens_recuperacao (
    id           INT PRIMARY KEY auto_increment,
    usuario_id   INT NOT NULL,
    email        VARCHAR(255) NOT NULL,
    token        VARCHAR(64) NOT NULL UNIQUE,
    data_expiracao TIMESTAMP NOT NULL,
	utilizado BIT NOT NULL DEFAULT 0,
    CONSTRAINT fk_token_user FOREIGN KEY (usuario_id) REFERENCES usuarios(id)
);

COMMIT;