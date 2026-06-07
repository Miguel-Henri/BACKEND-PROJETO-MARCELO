USE ifbank;

-- =========================================================
-- DADOS DE TESTE — GERENTE
-- =========================================================

-- 1. Cria o usuário gerente
INSERT INTO usuarios (nome, email, senha, telefone, endereco, status)
VALUES ('Gerente IFBank', 'gerente@ifbank.com', 'gerente123', '(11) 91234-5678', 'Sede IFBank, Ag. 5', 'ATIVO');

-- 2. Cria a conta do gerente (usa o id gerado acima; ajuste se necessário)
--    numero_conta 99999 é reservado para o gerente.
INSERT INTO contas (agencia, numero_conta, usuario_id, tipo, status)
VALUES (5, 99999, LAST_INSERT_ID(), 'GERENTE', 'ATIVA');


-- =========================================================
-- DADOS DE TESTE — CLIENTES PENDENTES (para o gerente aprovar)
-- =========================================================

INSERT INTO usuarios (nome, email, senha, telefone, endereco, status)
VALUES ('Ana Silva', 'ana@email.com', 'senha123', '(11) 98888-1111', 'Rua das Flores, 10', 'PENDENTE');

INSERT INTO contas (agencia, numero_conta, usuario_id, tipo, status)
VALUES (5, 1001, LAST_INSERT_ID(), 'CLIENTE', 'PENDENTE');


INSERT INTO usuarios (nome, email, senha, telefone, endereco, status)
VALUES ('Bruno Costa', 'bruno@email.com', 'senha123', '(11) 97777-2222', 'Av. Central, 200', 'PENDENTE');

INSERT INTO contas (agencia, numero_conta, usuario_id, tipo, status)
VALUES (5, 1002, LAST_INSERT_ID(), 'CLIENTE', 'PENDENTE');


INSERT INTO usuarios (nome, email, senha, telefone, endereco, status)
VALUES ('Carla Mendes', 'carla@email.com', 'senha123', '(21) 96666-3333', 'Rua Verde, 55', 'PENDENTE');

INSERT INTO contas (agencia, numero_conta, usuario_id, tipo, status)
VALUES (5, 1003, LAST_INSERT_ID(), 'CLIENTE', 'PENDENTE');


-- =========================================================
-- DADOS DE TESTE — CLIENTE JÁ APROVADO
-- =========================================================

INSERT INTO usuarios (nome, email, senha, telefone, endereco, status)
VALUES ('Diego Alves', 'diego@email.com', 'senha123', '(31) 95555-4444', 'Av. Norte, 99', 'ATIVO');

INSERT INTO contas (agencia, numero_conta, usuario_id, tipo, status, saldo)
VALUES (5, 1004, LAST_INSERT_ID(), 'CLIENTE', 'ATIVA', 1500.00);

COMMIT;
