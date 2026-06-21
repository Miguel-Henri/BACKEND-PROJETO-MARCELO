use ifbank;

INSERT INTO usuarios (id, nome, email, senha, telefone, status, role) VALUES
  (1, 'Marcelo Silva', 'marcelo@ifbank.com', '123456', '11999990001', 'ATIVO', 'CLIENTE'),
  (2, 'Ana Costa',     'ana@ifbank.com',     '123456', '11999990002', 'ATIVO', 'CLIENTE');

INSERT INTO contas (id, agencia, numero_conta, usuario_id, saldo, status) VALUES
  (1, 5, 10001, 1, 4500.00, 'ATIVA'),
  (2, 5, 10002, 2, 2500.00, 'ATIVA');

INSERT INTO movimentacoes (conta_origem_id, conta_destino_id, valor, saldo_anterior, saldo_posterior, tipo, descricao, status) VALUES
  (NULL, 1, 5000.00,    0.00, 5000.00, 'DEPOSITO',               'Depósito inicial', 'CONCLUIDA'),
  (NULL, 2, 2000.00,    0.00, 2000.00, 'DEPOSITO',               'Depósito inicial', 'CONCLUIDA'),
  (1,    2,  500.00, 5000.00, 4500.00, 'TRANSFERENCIA_ENVIADA',  'Pagamento Ana',    'CONCLUIDA'),
  (1,    2,  500.00, 2000.00, 2500.00, 'TRANSFERENCIA_RECEBIDA', 'Pagamento Ana',    'CONCLUIDA');

INSERT INTO config_emprestimo (taxa_juros_padrao) VALUES (2.50);

INSERT INTO emprestimos (id, conta_id, valor_emprestimo, taxa_juros_mensal, parcelas, status) VALUES
  (1, 1, 1000.00, 2.50, 3, 'EM_ANDAMENTO');

INSERT INTO parcelas_emprestimo (emprestimo_id, numero_parcela, valor_amortizacao, valor_juros, valor_parcela, data_vencimento, status) VALUES
  (1, 1, 333.33, 25.00, 358.33, DATE_ADD(CURRENT_DATE, INTERVAL 30 DAY), 'PENDENTE'),
  (1, 2, 333.33, 16.67, 350.00, DATE_ADD(CURRENT_DATE, INTERVAL 60 DAY), 'PENDENTE'),
  (1, 3, 333.34,  8.33, 341.67, DATE_ADD(CURRENT_DATE, INTERVAL 90 DAY), 'PENDENTE');

INSERT INTO investimentos (conta_id, tipo_investimento, valor_investido, data_inicio, status) VALUES
  (1, 'CDB', 1000.00, DATE_SUB(NOW(), INTERVAL 60 DAY), 'ATIVO');

-- =========================================================
-- DADOS DE TESTE — GERENTE
-- =========================================================

-- 1. Cria o usuário gerente
INSERT INTO usuarios (nome, email, senha, telefone, endereco, status, role)
VALUES ('Gerente IFBank', 'gerente@ifbank.com', 'gerente123', '(11) 91234-5678', 'Sede IFBank, Ag. 5', 'ATIVO', 'GERENTE');

-- 2. Cria a conta do gerente (numero_conta 99999 é reservado para o gerente)
INSERT INTO contas (agencia, numero_conta, usuario_id, role, status)
VALUES (5, 99999, LAST_INSERT_ID(), 'GERENTE', 'ATIVA');


-- =========================================================
-- DADOS DE TESTE — CLIENTES PENDENTES (para o gerente aprovar)
-- =========================================================

INSERT INTO usuarios (nome, email, senha, telefone, endereco, status, role)
VALUES ('Ana Silva', 'ana@email.com', 'senha123', '(11) 98888-1111', 'Rua das Flores, 10', 'PENDENTE', 'CLIENTE');

INSERT INTO contas (agencia, numero_conta, usuario_id, role, status)
VALUES (5, 1001, LAST_INSERT_ID(), 'CLIENTE', 'PENDENTE');


INSERT INTO usuarios (nome, email, senha, telefone, endereco, status, role)
VALUES ('Bruno Costa', 'bruno@email.com', 'senha123', '(11) 97777-2222', 'Av. Central, 200', 'PENDENTE', 'CLIENTE');

INSERT INTO contas (agencia, numero_conta, usuario_id, role, status)
VALUES (5, 1002, LAST_INSERT_ID(), 'CLIENTE', 'PENDENTE');


INSERT INTO usuarios (nome, email, senha, telefone, endereco, status, role)
VALUES ('Carla Mendes', 'carla@email.com', 'senha123', '(21) 96666-3333', 'Rua Verde, 55', 'PENDENTE', 'CLIENTE');

INSERT INTO contas (agencia, numero_conta, usuario_id, role, status)
VALUES (5, 1003, LAST_INSERT_ID(), 'CLIENTE', 'PENDENTE');


-- =========================================================
-- DADOS DE TESTE — CLIENTE JÁ APROVADO
-- =========================================================

INSERT INTO usuarios (nome, email, senha, telefone, endereco, status, role)
VALUES ('Diego Alves', 'diego@email.com', 'senha123', '(31) 95555-4444', 'Av. Norte, 99', 'ATIVO', 'CLIENTE');

INSERT INTO contas (agencia, numero_conta, usuario_id, role, status, saldo)
VALUES (5, 1004, LAST_INSERT_ID(), 'CLIENTE', 'ATIVA', 1500.00);

COMMIT;

