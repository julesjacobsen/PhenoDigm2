-- create mouse tables

-- MODEL - PHENOTYPE ASSOCIATIONS

-- -----------------------------------------------------
-- Table `mouse_model`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `mouse_model` ;

CREATE TABLE IF NOT EXISTS `mouse_model` (
  `model_id` INT(8) NOT NULL AUTO_INCREMENT,
  `source` VARCHAR(45) NULL,
  `allelic_composition` VARCHAR(255) NULL,
  `genetic_background` VARCHAR(255) NULL,
  `allelic_composition_link` TEXT NULL,
  `hom_het` VARCHAR(3) NULL,
  PRIMARY KEY (`model_id`));


-- -----------------------------------------------------
-- Table `mp`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `mp` ;

CREATE TABLE IF NOT EXISTS `mp` (
  `mp_id` VARCHAR(12) NOT NULL,
  `term` VARCHAR(200) NULL,
  PRIMARY KEY (`mp_id`));


-- -----------------------------------------------------
-- Table `mouse_model_mp`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `mouse_model_mp` ;

CREATE TABLE IF NOT EXISTS `mouse_model_mp` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `model_id` INT(8) UNSIGNED NOT NULL,
  `mp_id` VARCHAR(12) NOT NULL,
  PRIMARY KEY (`id`));

CREATE INDEX `model_id_mp_id` ON `mouse_model_mp` (`model_id` ASC, `mp_id` ASC);


-- -----------------------------------------------------
-- Table `hp_mp_mapping`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `hp_mp_mapping` ;

CREATE TABLE IF NOT EXISTS `hp_mp_mapping` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `hp_id` VARCHAR(12) NOT NULL,
  `hp_term` VARCHAR(200) NULL,
  `mp_id` VARCHAR(12) NOT NULL,
  `mp_term` VARCHAR(200) NULL,
  `simJ` DOUBLE NULL,
  `ic` DOUBLE NULL,
  `lcs` LONGTEXT NULL,
  `ic_ratio` DOUBLE NULL,
  PRIMARY KEY (`id`));

CREATE INDEX `hp_mp` ON `hp_mp_mapping` (`hp_id` ASC, `mp_id` ASC);

-- MODEL - DISEASE ASSOCIATIONS

-- -----------------------------------------------------
-- Table `mouse_disease_model_association_detail`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `mouse_disease_model_association_detail` ;

CREATE TABLE IF NOT EXISTS `mouse_disease_model_association_detail` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `disease_id` VARCHAR(20) NOT NULL,
  `model_id` INT(8) UNSIGNED NOT NULL,
  `hp_id` VARCHAR(12) NOT NULL,
  `mp_id` VARCHAR(12) NOT NULL,
  `simJ` DOUBLE NULL,
  `ic` DOUBLE NULL,
  `lcs` TEXT NULL,
  PRIMARY KEY (`id`),
  INDEX `mdmad_disease_model` (`disease_id` ASC, `model_id` ASC));


-- -----------------------------------------------------
-- Table `mouse_disease_model_association`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `mouse_disease_model_association` ;

CREATE TABLE IF NOT EXISTS `mouse_disease_model_association` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `disease_id` VARCHAR(20) NOT NULL,
  `model_id` INT(8) UNSIGNED NOT NULL,
  `lit_model` TINYINT(1) NOT NULL DEFAULT 0,
  `disease_to_model_perc_score` DOUBLE NOT NULL DEFAULT 0.0,
  `model_to_disease_perc_score` DOUBLE NOT NULL DEFAULT 0.0,
  `raw_score` DOUBLE NOT NULL DEFAULT 0.0,
  `hp_matched_terms` text,
  `mp_matched_terms` text,
  PRIMARY KEY (`id`))
COMMENT = 'Used to show the AJAX details view in IMPC portal.';

CREATE INDEX `m_disease_id` ON `mouse_disease_model_association` (`disease_id` ASC);

CREATE INDEX `m_model_id` ON `mouse_disease_model_association` (`model_id` ASC);

CREATE INDEX `m_disease_model` ON `mouse_disease_model_association` (`disease_id` ASC, `model_id` ASC);

-- GENE / ORTHOLOG TABLES

-- -----------------------------------------------------
-- Table `mouse_gene_ortholog`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `mouse_gene_ortholog` ;

CREATE TABLE IF NOT EXISTS `mouse_gene_ortholog` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `model_gene_id` VARCHAR(45) NULL,
  `model_gene_symbol` VARCHAR(255) NULL,
  `hgnc_id` VARCHAR(45) NULL,
  `hgnc_gene_symbol` VARCHAR(255) NULL,
  `hgnc_gene_locus` VARCHAR(255) NULL,
  PRIMARY KEY (`id`),
  INDEX `mg_model_gene_id_symbol` (`model_gene_id` ASC, `model_gene_symbol` ASC),
  INDEX `mg_hgnc_gene_id_symbol` (`hgnc_id` ASC, `hgnc_gene_symbol` ASC),
  INDEX `mg_model_hgnc_id` (`model_gene_id` ASC, `hgnc_id` ASC),
  INDEX `mg_model_hgnc_symbol` (`model_gene_symbol` ASC, `hgnc_gene_symbol` ASC));

-- -----------------------------------------------------
-- Table `mouse_model_gene_ortholog`
-- -----------------------------------------------------

DROP TABLE IF EXISTS `mouse_model_gene_ortholog` ;

CREATE TABLE IF NOT EXISTS `mouse_model_gene_ortholog` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `model_id` INT(8) NOT NULL,
  `model_gene_id` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id`));

CREATE INDEX `mmgo_model_gene_id` ON `mouse_model_gene_ortholog` (`model_id` ASC, `model_gene_id` ASC);
CREATE INDEX `mmgo_model_id` ON `mouse_model_gene_ortholog` (`model_id` ASC);
CREATE INDEX `mmgo_gene_id` ON `mouse_model_gene_ortholog`  (`model_gene_id` ASC);


-- SUMMARY TABLES FOR SEARCH AND INITIAL DISPLAY

-- -----------------------------------------------------
-- Table `mouse_gene_summary`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `mouse_gene_summary` ;

CREATE TABLE IF NOT EXISTS `mouse_gene_summary` (
  `model_gene_id` VARCHAR(45) NOT NULL,
  `human_curated` TINYINT(1) NULL DEFAULT FALSE,
  `mod_curated` TINYINT(1) NULL DEFAULT FALSE,
  `mod_predicted` TINYINT(1) NULL DEFAULT FALSE,
  `htpc_predicted` TINYINT(1) NULL DEFAULT FALSE,
  `mod_predicted_in_locus` TINYINT(1) NULL DEFAULT FALSE,
  `htpc_predicted_in_locus` TINYINT(1) NULL DEFAULT FALSE,
  `mod_model` TINYINT(1) NULL DEFAULT FALSE,
  `htpc_model` TINYINT(1) NULL DEFAULT FALSE,
  `htpc_phenotype` TINYINT(1) NULL DEFAULT FALSE,
  PRIMARY KEY (`model_gene_id`))
COMMENT = 'Used for solr faceting';


-- -----------------------------------------------------
-- Table `mouse_disease_gene_summary`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `mouse_disease_gene_summary` ;

CREATE TABLE IF NOT EXISTS `mouse_disease_gene_summary` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `disease_id` VARCHAR(20) NOT NULL,
  `model_gene_id` VARCHAR(45) NOT NULL,
  `human_curated` TINYINT(1) NULL DEFAULT FALSE,
  `mod_curated` TINYINT(1) NULL DEFAULT FALSE,
  `in_locus` TINYINT(1) NULL DEFAULT FALSE,
  `max_mod_disease_to_model_perc_score` DOUBLE NULL DEFAULT NULL,
  `max_mod_model_to_disease_perc_score` DOUBLE NULL DEFAULT NULL,
  `max_htpc_disease_to_model_perc_score` DOUBLE NULL DEFAULT NULL,
  `max_htpc_model_to_disease_perc_score` DOUBLE NULL DEFAULT NULL,
  `mod_raw_score` DOUBLE NULL DEFAULT NULL,
  `htpc_raw_score` DOUBLE NULL DEFAULT NULL, 
  PRIMARY KEY (`id`))
COMMENT = 'Used for the Disease/Gene pages in their initial, collapsed form (i.e. without the details panes visible)';

CREATE INDEX `mdgs_disease_id` ON `mouse_disease_gene_summary` (`disease_id` ASC);

CREATE INDEX `mdgs_model_gene_id` ON `mouse_disease_gene_summary` (`model_gene_id` ASC);


-- -----------------------------------------------------
-- Table `mouse_disease_summary`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `mouse_disease_summary` ;

CREATE TABLE IF NOT EXISTS `mouse_disease_summary` (
  `disease_id` VARCHAR(20) NOT NULL,
  `human_curated` TINYINT(1) NULL DEFAULT FALSE,
  `mod_curated` TINYINT(1) NULL DEFAULT FALSE,
  `mod_predicted` TINYINT(1) NULL DEFAULT FALSE,
  `htpc_predicted` TINYINT(1) NULL DEFAULT FALSE,
  `mod_predicted_in_locus` TINYINT(1) NULL DEFAULT FALSE,
  `htpc_predicted_in_locus` TINYINT(1) NULL DEFAULT FALSE,
  PRIMARY KEY (`disease_id`))
COMMENT = 'Used for solr faceting';


