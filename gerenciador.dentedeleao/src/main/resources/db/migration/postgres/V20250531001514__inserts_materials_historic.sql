-- Remoções por consumo em consultas
INSERT INTO dente_de_leao_manager.materials_historic (id, movement_type, quantity, movement_date, material_id) VALUES
(gen_random_uuid(), 'REMOVAL', 1, '2025-05-30 09:00:00', (SELECT id FROM dente_de_leao_manager.materials WHERE name = 'Resina composta fotopolimerizável')),
(gen_random_uuid(), 'REMOVAL', 1, '2025-05-30 09:00:00', (SELECT id FROM dente_de_leao_manager.materials WHERE name = 'Ácido fosfórico 37%')),
(gen_random_uuid(), 'REMOVAL', 1, '2025-05-30 09:00:00', (SELECT id FROM dente_de_leao_manager.materials WHERE name = 'Adesivo odontológico')),
(gen_random_uuid(), 'REMOVAL', 1, '2025-05-30 09:00:00', (SELECT id FROM dente_de_leao_manager.materials WHERE name = 'Fita matriz')),
(gen_random_uuid(), 'REMOVAL', 1, '2025-05-30 09:00:00', (SELECT id FROM dente_de_leao_manager.materials WHERE name = 'Disco de lixa para acabamento')),
(gen_random_uuid(), 'REMOVAL', 1, '2025-05-30 09:00:00', (SELECT id FROM dente_de_leao_manager.materials WHERE name = 'Luvas de procedimento (M)')),
(gen_random_uuid(), 'REMOVAL', 1, '2025-05-30 09:00:00', (SELECT id FROM dente_de_leao_manager.materials WHERE name = 'Máscara cirúrgica')),

-- Exemplo de REPOSIÇÕES
(gen_random_uuid(), 'ADDITION', 20, '2025-05-31 08:00:00', (SELECT id FROM dente_de_leao_manager.materials WHERE name = 'Luvas de procedimento (M)')),
(gen_random_uuid(), 'ADDITION', 10, '2025-05-31 08:00:00', (SELECT id FROM dente_de_leao_manager.materials WHERE name = 'Máscara cirúrgica'));
