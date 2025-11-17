ALTER TABLE tournament ADD COLUMN customer_id BIGINT NOT NULL;
ALTER TABLE tournament ADD CONSTRAINT fk_tournament_customer FOREIGN KEY (customer_id) REFERENCES customer(id);