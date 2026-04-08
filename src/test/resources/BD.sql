-- Copia de referencia para pruebas manuales locales.
-- La fuente oficial del esquema vive en:
--   src/main/resources/db/migration/V1__initial_schema.sql
--   src/main/resources/db/migration/V2__seed_reference_data.sql

CREATE DATABASE IF NOT EXISTS lujuria CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE lujuria;

SOURCE src/main/resources/db/migration/V1__initial_schema.sql;
SOURCE src/main/resources/db/migration/V2__seed_reference_data.sql;
