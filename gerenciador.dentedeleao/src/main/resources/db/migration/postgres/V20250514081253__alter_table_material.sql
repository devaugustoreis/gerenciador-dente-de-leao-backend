ALTER TABLE IF EXISTS dente_de_leao_manager.materials
ADD COLUMN IF NOT EXISTS stock_quantity INT,
ADD COLUMN IF NOT EXISTS scheduled_quantity INT,
ADD COLUMN IF NOT EXISTS alert_quantity INT;

UPDATE dente_de_leao_manager.materials m
SET stock_quantity = ms.stock_quantity,
    scheduled_quantity = ms.scheduled_quantity,
    alert_quantity = ms.alert_quantity
FROM dente_de_leao_manager.materials_stock ms
WHERE ms.material_id = m.id;

ALTER TABLE dente_de_leao_manager.materials_historic
ADD COLUMN material_id UUID;

UPDATE dente_de_leao_manager.materials_historic mh
SET material_id = ms.material_id
FROM dente_de_leao_manager.materials_stock ms
WHERE mh.material_stock_id = ms.id;

ALTER TABLE dente_de_leao_manager.materials_historic
ALTER COLUMN material_id SET NOT NULL;

ALTER TABLE dente_de_leao_manager.materials_historic
DROP CONSTRAINT fk_material_historic_material_stock;

ALTER TABLE dente_de_leao_manager.materials_historic
ADD CONSTRAINT fk_material_historic_material FOREIGN KEY (material_id) REFERENCES dente_de_leao_manager.materials (id);

ALTER TABLE dente_de_leao_manager.materials_historic
DROP COLUMN material_stock_id;

DROP TABLE IF EXISTS dente_de_leao_manager.materials_stock;