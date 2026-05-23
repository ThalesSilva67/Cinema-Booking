CREATE TYPE type_ticket AS ENUM ('INTEIRA', 'MEIA-ENTRADA');

ALTER TABLE booking
ADD COLUMN ticket type_ticket NOT NULL