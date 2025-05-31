truncate table dente_de_leao_manager.categories cascade;

INSERT INTO dente_de_leao_manager.categories (id, label, excluded) VALUES
(gen_random_uuid(), 'Materiais Restauradores', false),
(gen_random_uuid(), 'Materiais para Endodontia', false),
(gen_random_uuid(), 'Materiais para Profilaxia', false),
(gen_random_uuid(), 'Materiais para Ortodontia Móvel', false),
(gen_random_uuid(), 'Materiais de Moldagem', false),
(gen_random_uuid(), 'Instrumentais Clínicos', false),
(gen_random_uuid(), 'Descartáveis e EPIs', false),
(gen_random_uuid(), 'Materiais para Isolamento', false),
(gen_random_uuid(), 'Materiais para Radiografia', false),
(gen_random_uuid(), 'Medicamentos e Substâncias Ativas', false),
(gen_random_uuid(), 'Brindes', false),
(gen_random_uuid(), 'Materiais da lojinha', false);
