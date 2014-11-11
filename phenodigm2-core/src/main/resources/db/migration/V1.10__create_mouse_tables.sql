-- create mouse tables

-- MODEL - PHENOTYPE ASSOCIATIONS

-- -----------------------------------------------------
-- Table `mp_mp_mapping`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `mp_mp_mapping` ;

CREATE TABLE IF NOT EXISTS `mp_mp_mapping` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `mp_id` varchar(12) NOT NULL,
  `mp_term` varchar(200) DEFAULT NULL,
  `mp_id_hit` varchar(12) NOT NULL,
  `mp_term_hit` varchar(200) DEFAULT NULL,
  `simJ` float DEFAULT NULL,
  `ic` float DEFAULT NULL,
  `lcs` varchar(200),
  `ic_ratio` float DEFAULT NULL,
  PRIMARY KEY (`id`));

 CREATE INDEX `mp_mp` ON `mp_mp_mapping` (`mp_id` ASC,`mp_id_hit` ASC);

-- -----------------------------------------------------
-- Table `mouse_mouse_association`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `mouse_mouse_association`;

CREATE TABLE `mouse_mouse_association` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `model_id` varchar(20) NOT NULL,
  `model_match` varchar(20) NOT NULL,
  `model_to_model_perc_score` double NOT NULL DEFAULT '0',
  `raw_score` double NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`));

CREATE INDEX `mma_model_id` ON `mouse_mouse_association` (`model_id` ASC);
CREATE INDEX `mma_model_match` ON `mouse_mouse_association` (`model_match` ASC);
CREATE INDEX `mma_model_model` ON `mouse_mouse_association` (`model_id` ASC,`model_match` ASC);


-- -----------------------------------------------------
-- Table `mouse_mouse_association_detail`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `mouse_mouse_association_detail`;

CREATE TABLE `mouse_mouse_association_detail` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `model_id` varchar(20) NOT NULL,
  `model_match` varchar(20) NOT NULL,
  `mp_id` varchar(12) NOT NULL,
  `mp_match` varchar(12) NOT NULL,
  `simJ` double DEFAULT NULL,
  `ic` double DEFAULT NULL,
  `lcs` varchar(200),
  PRIMARY KEY (`id`));

CREATE INDEX `mdmad_model_model` ON `mouse_mouse_association_detail` (`model_id` ASC,`model_match` ASC);

-- -----------------------------------------------------
-- Table `mouse_model`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `mouse_model` ;

CREATE TABLE IF NOT EXISTS `mouse_model` (
  `model_id` INT(8) NOT NULL AUTO_INCREMENT,
  `source` VARCHAR(45) NULL,
  `allelic_composition` VARCHAR(255) NULL,
  `genetic_background` VARCHAR(255) NULL,
  `allele_ids` VARCHAR(255) NULL,
  `hom_het` VARCHAR(3) NULL,
  PRIMARY KEY (`model_id`));


-- -----------------------------------------------------
-- Table `mp`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `mp` ;

CREATE TABLE IF NOT EXISTS `mp` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `mp_id` VARCHAR(12) NOT NULL,
  `term` VARCHAR(200) NULL,
  PRIMARY KEY (`id`));

CREATE INDEX `mp_id` ON `mp` (`mp_id` ASC);

-- -----------------------------------------------------
-- Table `mp_impc_slim`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `mp_impc_slim` ;

CREATE TABLE IF NOT EXISTS `mp_impc_slim` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `mp_id` VARCHAR(12) NOT NULL,
  `term` VARCHAR(200) NULL,
  PRIMARY KEY (`id`));

CREATE INDEX `mpimpcsl_mp_id` ON `mp_impc_slim` (`mp_id` ASC);


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
  `lcs` VARCHAR(200) NULL,
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
  `lcs` VARCHAR(200) NULL,
  PRIMARY KEY (`id`));

CREATE INDEX `mdmad_disease_model` ON `mouse_disease_model_association_detail` (`disease_id` ASC, `model_id` ASC);
-- -----------------------------------------------------
-- Table `mouse_disease_model_association`
--
-- Used to show the AJAX details view in IMPC portal.
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
  PRIMARY KEY (`id`));

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
  `entrezgene` INT(8) DEFAULT NULL,
  PRIMARY KEY (`id`));


CREATE INDEX `mg_model_gene_id_symbol` ON `mouse_gene_ortholog` (`model_gene_id` ASC, `model_gene_symbol` ASC);
CREATE INDEX `mg_hgnc_gene_id_symbol` ON `mouse_gene_ortholog` (`hgnc_id` ASC, `hgnc_gene_symbol` ASC);
CREATE INDEX `mg_model_hgnc_id` ON `mouse_gene_ortholog` (`model_gene_id` ASC, `hgnc_id` ASC);
CREATE INDEX `mg_model_hgnc_symbol` ON `mouse_gene_ortholog` (`model_gene_symbol` ASC, `hgnc_gene_symbol` ASC);
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
--
-- Used for solr faceting
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
  PRIMARY KEY (`model_gene_id`));


-- -----------------------------------------------------
-- Table `mouse_disease_gene_summary`
--
--Used for the Disease/Gene pages in their initial, collapsed form (i.e. without the details panes visible)
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
  PRIMARY KEY (`id`));

CREATE INDEX `mdgs_disease_id` ON `mouse_disease_gene_summary` (`disease_id` ASC);

CREATE INDEX `mdgs_model_gene_id` ON `mouse_disease_gene_summary` (`model_gene_id` ASC);

-- -----------------------------------------------------
-- Table mouse_disease_gene_summary_high_quality
--
-- High-quality data used for the IMPC Disease/Gene pages in their initial, collapsed form (i.e. without the details panes visible)
-- -----------------------------------------------------
DROP TABLE IF EXISTS `mouse_disease_gene_summary_high_quality` ;


CREATE TABLE IF NOT EXISTS `mouse_disease_gene_summary_high_quality` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `disease_id` VARCHAR(20) NOT NULL,
  `model_gene_id` VARCHAR(45) NOT NULL,
  `human_curated` BOOLEAN NULL DEFAULT FALSE,
  `mod_curated` BOOLEAN NULL DEFAULT FALSE,
  `in_locus` BOOLEAN NULL DEFAULT FALSE,
  `max_mod_disease_to_model_perc_score` DOUBLE NULL DEFAULT NULL,
  `max_mod_model_to_disease_perc_score` DOUBLE NULL DEFAULT NULL,
  `max_htpc_disease_to_model_perc_score` DOUBLE NULL DEFAULT NULL,
  `max_htpc_model_to_disease_perc_score` DOUBLE NULL DEFAULT NULL,
  `mod_raw_score` DOUBLE NULL DEFAULT NULL,
  `htpc_raw_score` DOUBLE NULL DEFAULT NULL,
  `mod_predicted` BOOLEAN DEFAULT FALSE,
  `mod_predicted_known_gene` BOOLEAN DEFAULT FALSE,
  `novel_mod_predicted_in_locus` BOOLEAN DEFAULT FALSE,
  `htpc_predicted` BOOLEAN DEFAULT FALSE,
  `htpc_predicted_known_gene` BOOLEAN DEFAULT FALSE,
  `novel_htpc_predicted_in_locus` BOOLEAN DEFAULT FALSE,
  PRIMARY KEY (`id`));
                        
CREATE INDEX `mdgshq_disease_id` ON `mouse_disease_gene_summary_high_quality` (`disease_id` ASC);

CREATE INDEX `mdgshq_model_gene_id` ON `mouse_disease_gene_summary_high_quality` (`model_gene_id` ASC);

-- -----------------------------------------------------
-- Table `mouse_disease_summary`
--
-- Used for solr faceting
-- -----------------------------------------------------
DROP TABLE IF EXISTS `mouse_disease_summary` ;

CREATE TABLE IF NOT EXISTS `mouse_disease_summary` (
  `disease_id` VARCHAR(20) NOT NULL,
  `human_curated` BOOLEAN NULL DEFAULT FALSE,
  `mod_curated` BOOLEAN NULL DEFAULT FALSE,
  `mod_predicted` BOOLEAN NULL DEFAULT FALSE,
  `htpc_predicted` BOOLEAN NULL DEFAULT FALSE,
  `mod_predicted_in_locus` BOOLEAN NULL DEFAULT FALSE,
  `htpc_predicted_in_locus` BOOLEAN NULL DEFAULT FALSE,
  `mod_predicted_known_gene` BOOLEAN NULL DEFAULT FALSE,
  `novel_mod_predicted_in_locus` BOOLEAN NULL DEFAULT FALSE,
  `htpc_predicted_known_gene` BOOLEAN NULL DEFAULT FALSE,
  `novel_htpc_predicted_in_locus` BOOLEAN NULL DEFAULT FALSE,
  PRIMARY KEY (`disease_id`));

-- -----------------------------------------------------
-- Table `best_impc_hp_mp_mapping`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `best_impc_hp_mp_mapping` ;

CREATE TABLE `best_impc_hp_mp_mapping` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `hp_id` varchar(12) NOT NULL,
  `hp_term` varchar(200) DEFAULT NULL,
  `mp_id` varchar(12) NOT NULL,
  `mp_term` varchar(200) DEFAULT NULL,
  `ic` double DEFAULT NULL,
  `lcs` varchar(200),
  PRIMARY KEY (`id`));

CREATE INDEX `best_hp_mp` ON `best_impc_hp_mp_mapping` (`hp_id` ASC, `mp_id` ASC);

-- -----------------------------------------------------
-- Table `best_impc_hp_mp_mapping`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `best_impc_hp_mp_mapping` ;

CREATE TABLE `best_impc_hp_mp_mapping` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `hp_id` varchar(12) NOT NULL,
  `hp_term` varchar(200) DEFAULT NULL,
  `mp_id` varchar(12) NOT NULL,
  `mp_term` varchar(200) DEFAULT NULL,
  `ic` double DEFAULT NULL,
  `lcs` varchar(200),
  PRIMARY KEY (`id`));

CREATE INDEX `best_hp_mp` ON `best_impc_hp_mp_mapping` (`hp_id` ASC, `mp_id` ASC);

