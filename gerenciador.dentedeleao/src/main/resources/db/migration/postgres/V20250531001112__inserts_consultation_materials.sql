-- Consulta: restauração
INSERT INTO dente_de_leao_manager.consultation_materials (consultation_id, material_id, quantity) VALUES
('306008c4-f308-5876-a747-1b71d48cd1f4', (SELECT id FROM dente_de_leao_manager.materials WHERE name = 'Resina composta fotopolimerizável'), 1),
('306008c4-f308-5876-a747-1b71d48cd1f4', (SELECT id FROM dente_de_leao_manager.materials WHERE name = 'Ácido fosfórico 37%'), 1),
('306008c4-f308-5876-a747-1b71d48cd1f4', (SELECT id FROM dente_de_leao_manager.materials WHERE name = 'Adesivo odontológico'), 1),
('306008c4-f308-5876-a747-1b71d48cd1f4', (SELECT id FROM dente_de_leao_manager.materials WHERE name = 'Fita matriz'), 1),
('306008c4-f308-5876-a747-1b71d48cd1f4', (SELECT id FROM dente_de_leao_manager.materials WHERE name = 'Disco de lixa para acabamento'), 1),
('306008c4-f308-5876-a747-1b71d48cd1f4', (SELECT id FROM dente_de_leao_manager.materials WHERE name = 'Luvas de procedimento (M)'), 1),
('306008c4-f308-5876-a747-1b71d48cd1f4', (SELECT id FROM dente_de_leao_manager.materials WHERE name = 'Máscara cirúrgica'), 1);

-- Consulta: restauração lateral
INSERT INTO dente_de_leao_manager.consultation_materials (consultation_id, material_id, quantity) VALUES
('208aa79c-931b-5957-9614-54a2fc1cd597', (SELECT id FROM dente_de_leao_manager.materials WHERE name = 'Resina composta fotopolimerizável'), 1),
('208aa79c-931b-5957-9614-54a2fc1cd597', (SELECT id FROM dente_de_leao_manager.materials WHERE name = 'Ácido fosfórico 37%'), 1),
('208aa79c-931b-5957-9614-54a2fc1cd597', (SELECT id FROM dente_de_leao_manager.materials WHERE name = 'Adesivo odontológico'), 1),
('208aa79c-931b-5957-9614-54a2fc1cd597', (SELECT id FROM dente_de_leao_manager.materials WHERE name = 'Fita matriz'), 1),
('208aa79c-931b-5957-9614-54a2fc1cd597', (SELECT id FROM dente_de_leao_manager.materials WHERE name = 'Disco de lixa para acabamento'), 1),
('208aa79c-931b-5957-9614-54a2fc1cd597', (SELECT id FROM dente_de_leao_manager.materials WHERE name = 'Luvas de procedimento (M)'), 1),
('208aa79c-931b-5957-9614-54a2fc1cd597', (SELECT id FROM dente_de_leao_manager.materials WHERE name = 'Máscara cirúrgica'), 1);

-- Consulta: verniz
INSERT INTO dente_de_leao_manager.consultation_materials (consultation_id, material_id, quantity) VALUES
('29581295-4e4b-58df-b598-9e4400ee5302', (SELECT id FROM dente_de_leao_manager.materials WHERE name = 'Flúor gel neutro'), 1),
('29581295-4e4b-58df-b598-9e4400ee5302', (SELECT id FROM dente_de_leao_manager.materials WHERE name = 'Escova de Robinson'), 1),
('29581295-4e4b-58df-b598-9e4400ee5302', (SELECT id FROM dente_de_leao_manager.materials WHERE name = 'Luvas de procedimento (M)'), 1),
('29581295-4e4b-58df-b598-9e4400ee5302', (SELECT id FROM dente_de_leao_manager.materials WHERE name = 'Máscara cirúrgica'), 1);

-- Consulta: selante
INSERT INTO dente_de_leao_manager.consultation_materials (consultation_id, material_id, quantity) VALUES
('d13c8b35-9624-5703-b817-a44ebf3a9541', (SELECT id FROM dente_de_leao_manager.materials WHERE name = 'Selante dentário'), 1),
('d13c8b35-9624-5703-b817-a44ebf3a9541', (SELECT id FROM dente_de_leao_manager.materials WHERE name = 'Luvas de procedimento (M)'), 1),
('d13c8b35-9624-5703-b817-a44ebf3a9541', (SELECT id FROM dente_de_leao_manager.materials WHERE name = 'Máscara cirúrgica'), 1);
