CREATE DATABASE IF NOT EXISTS lujuria CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE lujuria;

-- ============================================================
-- ESQUEMA DE REFERENCIA DEL PROYECTO LUJURIA
-- Fuente operativa real:
--   src/main/resources/db/migration/V1__initial_schema.sql
--   src/main/resources/db/migration/V2__seed_reference_data.sql
-- Este archivo existe para lectura humana y revisión rápida.
-- ============================================================

SOURCE src/main/resources/db/migration/V1__initial_schema.sql;
SOURCE src/main/resources/db/migration/V2__seed_reference_data.sql;
