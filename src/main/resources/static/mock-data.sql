use ifbank;

INSERT INTO usuarios (id, nome, email, senha, telefone, status) VALUES
  (1, 'Marcelo Silva', 'marcelo@ifbank.com', '123456', '11999990001', 'ATIVO'),
  (2, 'Ana Costa',     'ana@ifbank.com',     '123456', '11999990002', 'ATIVO');

INSERT INTO contas (id, agencia, numero_conta, usuario_id, saldo) VALUES
  (1, 5, 10001, 1, 4500.00),
  (2, 5, 10002, 2, 2500.00);

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

INSERT INTO investimentos (conta_id, tipo_investimento, valor_investido, status) VALUES
  (1, 'CDB', 1000.00, 'ATIVO');

