-- OperationClaim Seed Data
-- Product Operations
INSERT INTO operation_claims (name, description) VALUES ('Product.Create', 'Ürün oluşturma izni') ON CONFLICT (name) DO NOTHING;
INSERT INTO operation_claims (name, description) VALUES ('Product.Read', 'Ürün okuma izni') ON CONFLICT (name) DO NOTHING;
INSERT INTO operation_claims (name, description) VALUES ('Product.Update', 'Ürün güncelleme izni') ON CONFLICT (name) DO NOTHING;
INSERT INTO operation_claims (name, description) VALUES ('Product.Delete', 'Ürün silme izni') ON CONFLICT (name) DO NOTHING;

-- Order Operations
INSERT INTO operation_claims (name, description) VALUES ('Order.Create', 'Sipariş oluşturma izni') ON CONFLICT (name) DO NOTHING;
INSERT INTO operation_claims (name, description) VALUES ('Order.Read', 'Sipariş okuma izni') ON CONFLICT (name) DO NOTHING;
INSERT INTO operation_claims (name, description) VALUES ('Order.ReadAll', 'Tüm siparişleri okuma izni') ON CONFLICT (name) DO NOTHING;
INSERT INTO operation_claims (name, description) VALUES ('Order.Update', 'Sipariş güncelleme izni') ON CONFLICT (name) DO NOTHING;
INSERT INTO operation_claims (name, description) VALUES ('Order.Delete', 'Sipariş silme izni') ON CONFLICT (name) DO NOTHING;
