-- create fish tables

-- -----------------------------------------------------
-- Table `fish_model`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `fish_model` ;

CREATE TABLE IF NOT EXISTS `fish_model` (
  `model_id` INT(8) UNSIGNED NOT NULL,
  `source` VARCHAR(45) NULL,
  `allelic_composition` VARCHAR(255) NULL,
  `genetic_background` VARCHAR(255) NULL,
  `allelic_composition_link` TEXT NULL,
  `hom_het` VARCHAR(3) NULL,
  `model_phenotype_terms` TEXT NULL,
  PRIMARY KEY (`model_id`));


-- -----------------------------------------------------
-- Table `zp`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `zp` ;

CREATE TABLE IF NOT EXISTS `zp` (
  `zp_id` VARCHAR(12) NOT NULL,
  `term` VARCHAR(200) NULL,
  PRIMARY KEY (`zp_id`));

-- -----------------------------------------------------
-- Table `fish_model_zp`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `fish_model_zp` ;

CREATE TABLE IF NOT EXISTS `fish_model_zp` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `model_id` INT(8) UNSIGNED NOT NULL,
  `zp_id` VARCHAR(12) NOT NULL,
  PRIMARY KEY (`id`));

CREATE INDEX `model_id_zp_id` ON `fish_model_zp` (`model_id` ASC, `zp_id` ASC);


-- -----------------------------------------------------
-- Table `hp_zp_mapping`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `hp_zp_mapping` ;

CREATE TABLE IF NOT EXISTS `hp_zp_mapping` (
  `id` INT(11) NOT NULL,
  `hp_id` VARCHAR(12) NOT NULL,
  `hp_term` VARCHAR(200) NULL,
  `zp_id` VARCHAR(12) NOT NULL,
  `zp_term` VARCHAR(200) NULL,
  `simJ` DOUBLE NULL,
  `ic` DOUBLE NULL,
  `lcs` LONGTEXT NULL,
  `ic_ratio` DOUBLE NULL,
  PRIMARY KEY (`id`));

CREATE INDEX `hp_zp` ON `hp_zp_mapping` (`zp_id` ASC, `hp_id` ASC);


-- MODEL - DISEASE ASSOCIATIONS

-- -----------------------------------------------------
-- Table `fish_disease_model_association_detail`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `fish_disease_model_association_detail` ;

CREATE TABLE IF NOT EXISTS `fish_disease_model_association_detail` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `disease_id` VARCHAR(20) NOT NULL,
  `model_id` INT(8) UNSIGNED NOT NULL,
  `hp_id` VARCHAR(12) NOT NULL,
  `zp_id` VARCHAR(12) NOT NULL,
  `simJ` DOUBLE NULL,
  `ic` DOUBLE NULL,
  PRIMARY KEY (`id`),
  INDEX `fdmad_disease_model` (`disease_id` ASC, `model_id` ASC));


-- -----------------------------------------------------
-- Table `fish_disease_model_association`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `fish_disease_model_association` ;

CREATE TABLE IF NOT EXISTS `fish_disease_model_association` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `disease_id` VARCHAR(20) NOT NULL,
  `model_id` INT(8) UNSIGNED NOT NULL,
  `lit_model` TINYINT(1) NOT NULL DEFAULT 0,
  `disease_to_model_perc_score` DOUBLE NOT NULL DEFAULT 0.0,
  `model_to_disease_perc_score` DOUBLE NOT NULL DEFAULT 0.0,
  `raw_score` DOUBLE NOT NULL DEFAULT '0',
  `hp_matched_terms` text,
  `mp_matched_terms` text,
  PRIMARY KEY (`id`))
COMMENT = 'Used to show the AJAX details view in IMPC portal.';

CREATE INDEX `f_disease_id` ON `fish_disease_model_association` (`disease_id` ASC);

CREATE INDEX `f_model_id` ON `fish_disease_model_association` (`model_id` ASC);

CREATE INDEX `f_disease_model` ON `fish_disease_model_association` (`disease_id` ASC, `model_id` ASC);

-- GENE / ORTHOLOG TABLES

-- -----------------------------------------------------
-- Table `fish_gene_orthologs`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `fish_gene_ortholog` ;

CREATE TABLE IF NOT EXISTS `fish_gene_orthologs` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `model_gene_id` VARCHAR(45) NULL,
  `model_gene_symbol` VARCHAR(255) NULL,
  `hgnc_id` VARCHAR(45) NULL,
  `hgnc_gene_symbol` VARCHAR(255) NULL,
  `hgnc_gene_locus` VARCHAR(255) NULL,
  PRIMARY KEY (`id`),
  INDEX `fg_model_gene_id_symbol` (`model_gene_id` ASC, `model_gene_symbol` ASC),
  INDEX `fg_hgnc_gene_id_symbol` (`hgnc_id` ASC, `hgnc_gene_symbol` ASC),
  INDEX `fg_model_hgnc_id` (`model_gene_id` ASC, `hgnc_id` ASC),
  INDEX `fg_model_hgnc_symbol` (`model_gene_symbol` ASC, `hgnc_gene_symbol` ASC));

-- -----------------------------------------------------
-- Table `fish_model_gene_ortholog`
-- -----------------------------------------------------

DROP TABLE IF EXISTS `fish_model_gene_ortholog` ;

CREATE TABLE IF NOT EXISTS `fish_model_gene_ortholog` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `model_id` INT(8) NOT NULL,
  `model_gene_id` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id`));

CREATE INDEX `fmgo_model_gene_id` ON `fish_model_gene_ortholog` (`model_id` ASC, `model_gene_id` ASC);
CREATE INDEX `fmgo_model_id` ON `fish_model_gene_ortholog` (`model_id` ASC);
CREATE INDEX `fmgo_gene_id` ON `fish_model_gene_ortholog`  (`model_gene_id` ASC);


-- SUMMARY TABLES FOR SEARCH AND INITIAL DISPLAY

-- -----------------------------------------------------
-- Table `fish_gene_summary`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `fish_gene_summary` ;

CREATE TABLE IF NOT EXISTS `fish_gene_summary` (
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
-- Table `fish_disease_gene_summary`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `fish_disease_gene_summary` ;

CREATE TABLE IF NOT EXISTS `fish_disease_gene_summary` (
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

CREATE INDEX `fdgs_disease_id` ON `fish_disease_gene_summary` (`disease_id` ASC);

CREATE INDEX `fdgs_model_gene_id` ON `fish_disease_gene_summary` (`model_gene_id` ASC);


-- -----------------------------------------------------
-- Table `fish_disease_summary`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `fish_disease_summary` ;

CREATE TABLE IF NOT EXISTS `fish_disease_summary` (
  `disease_id` VARCHAR(20) NOT NULL,
  `human_curated` TINYINT(1) NULL DEFAULT FALSE,
  `mod_curated` TINYINT(1) NULL DEFAULT FALSE,
  `mod_predicted` TINYINT(1) NULL DEFAULT FALSE,
  `htpc_predicted` TINYINT(1) NULL DEFAULT FALSE,
  `mod_predicted_in_locus` TINYINT(1) NULL DEFAULT FALSE,
  `htpc_predicted_in_locus` TINYINT(1) NULL DEFAULT FALSE,
  PRIMARY KEY (`disease_id`))
COMMENT = 'Used for solr faceting';
