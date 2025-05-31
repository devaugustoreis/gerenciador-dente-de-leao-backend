INSERT INTO dente_de_leao_manager.materials_groups (id, label, excluded) VALUES
(gen_random_uuid(), 'Consulta para aparelho móvel', false),
(gen_random_uuid(), 'Restauração simples', false),
(gen_random_uuid(), 'Aplicação de selante', false),
(gen_random_uuid(), 'Tratamento de canal', false),
(gen_random_uuid(), 'Profilaxia', false),
(gen_random_uuid(), 'Extração dentária simples', false),
(gen_random_uuid(), 'Entrega de aparelho móvel', false),
(gen_random_uuid(), 'Primeira consulta', false);

INSERT INTO dente_de_leao_manager.materials_groups_items (material_group_id, material_id, quantity) VALUES
((SELECT id FROM dente_de_leao_manager.materials_groups WHERE label = 'Restauração simples'), (SELECT id FROM dente_de_leao_manager.materials WHERE name = 'Resina composta fotopolimerizável'), 1),
((SELECT id FROM dente_de_leao_manager.materials_groups WHERE label = 'Restauração simples'), (SELECT id FROM dente_de_leao_manager.materials WHERE name = 'Adesivo odontológico'), 1),
((SELECT id FROM dente_de_leao_manager.materials_groups WHERE label = 'Restauração simples'), (SELECT id FROM dente_de_leao_manager.materials WHERE name = 'Ácido fosfórico 37%'), 1),
((SELECT id FROM dente_de_leao_manager.materials_groups WHERE label = 'Restauração simples'), (SELECT id FROM dente_de_leao_manager.materials WHERE name = 'Brocas diamantadas'), 2),
((SELECT id FROM dente_de_leao_manager.materials_groups WHERE label = 'Restauração simples'), (SELECT id FROM dente_de_leao_manager.materials WHERE name = 'Brocas multilaminadas'), 1),
((SELECT id FROM dente_de_leao_manager.materials_groups WHERE label = 'Restauração simples'), (SELECT id FROM dente_de_leao_manager.materials WHERE name = 'Espátulas plásticas descartáveis'), 1),
((SELECT id FROM dente_de_leao_manager.materials_groups WHERE label = 'Restauração simples'), (SELECT id FROM dente_de_leao_manager.materials WHERE name = 'Luvas de procedimento (M)'), 1),

((SELECT id FROM dente_de_leao_manager.materials_groups WHERE label = 'Primeira consulta'), (SELECT id FROM dente_de_leao_manager.materials WHERE name = 'Pasta de dente'), 1),
((SELECT id FROM dente_de_leao_manager.materials_groups WHERE label = 'Primeira consulta'), (SELECT id FROM dente_de_leao_manager.materials WHERE name = 'Escova de dente'), 1),
((SELECT id FROM dente_de_leao_manager.materials_groups WHERE label = 'Primeira consulta'), (SELECT id FROM dente_de_leao_manager.materials WHERE name = 'Fio dental'), 1),
((SELECT id FROM dente_de_leao_manager.materials_groups WHERE label = 'Primeira consulta'), (SELECT id FROM dente_de_leao_manager.materials WHERE name = 'Flúor'), 1),
((SELECT id FROM dente_de_leao_manager.materials_groups WHERE label = 'Primeira consulta'), (SELECT id FROM dente_de_leao_manager.materials WHERE name = 'Balão'), 1),

((SELECT id FROM dente_de_leao_manager.materials_groups WHERE label = 'Extração dentária simples'), (SELECT id FROM dente_de_leao_manager.materials WHERE name = 'Cureta de extração'), 1),
((SELECT id FROM dente_de_leao_manager.materials_groups WHERE label = 'Extração dentária simples'), (SELECT id FROM dente_de_leao_manager.materials WHERE name = 'Elevador periósteo'), 1),
((SELECT id FROM dente_de_leao_manager.materials_groups WHERE label = 'Extração dentária simples'), (SELECT id FROM dente_de_leao_manager.materials WHERE name = 'Pinça clínica'), 1),
((SELECT id FROM dente_de_leao_manager.materials_groups WHERE label = 'Extração dentária simples'), (SELECT id FROM dente_de_leao_manager.materials WHERE name = 'Luvas de procedimento (M)'), 1),
((SELECT id FROM dente_de_leao_manager.materials_groups WHERE label = 'Extração dentária simples'), (SELECT id FROM dente_de_leao_manager.materials WHERE name = 'Máscara cirúrgica'), 1),
((SELECT id FROM dente_de_leao_manager.materials_groups WHERE label = 'Extração dentária simples'), (SELECT id FROM dente_de_leao_manager.materials WHERE name = 'Gaze estéril'), 3),
((SELECT id FROM dente_de_leao_manager.materials_groups WHERE label = 'Extração dentária simples'), (SELECT id FROM dente_de_leao_manager.materials WHERE name = 'Anestésico lidocaína 2%'), 1),
((SELECT id FROM dente_de_leao_manager.materials_groups WHERE label = 'Extração dentária simples'), (SELECT id FROM dente_de_leao_manager.materials WHERE name = 'Agulhas curtas 30G'), 1),
((SELECT id FROM dente_de_leao_manager.materials_groups WHERE label = 'Extração dentária simples'), (SELECT id FROM dente_de_leao_manager.materials WHERE name = 'Seringas carpule'), 1),
((SELECT id FROM dente_de_leao_manager.materials_groups WHERE label = 'Extração dentária simples'), (SELECT id FROM dente_de_leao_manager.materials WHERE name = 'Sugador descartável'), 1);
