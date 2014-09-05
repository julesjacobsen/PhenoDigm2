-- create disease tables

-- -----------------------------------------------------
-- Table `disease`
-- -----------------------------------------------------

DROP TABLE IF EXISTS `disease` ;

CREATE TABLE IF NOT EXISTS `disease` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `disease_id` VARCHAR(20) NOT NULL,
  `disease_term` VARCHAR(255) NOT NULL,
  `disease_alts` TEXT NULL,
  `disease_locus` VARCHAR(255) NULL,
  `disease_classes` VARCHAR(255) NULL DEFAULT 'unclassified',
  PRIMARY KEY (`id`));

CREATE INDEX `disease_id` ON `disease` (`disease_id` ASC);

-- -----------------------------------------------------
-- Table `disease_hp`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `disease_hp` ;

CREATE TABLE IF NOT EXISTS `disease_hp` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `disease_id` VARCHAR(20) NOT NULL,
  `hp_id` VARCHAR(12) NOT NULL,
  PRIMARY KEY (`id`));

CREATE INDEX `disease_id_hp_id` ON `disease_hp` (`disease_id` ASC, `hp_id` ASC);


-- -----------------------------------------------------
-- Table `hp`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `hp` ;

CREATE TABLE IF NOT EXISTS `hp` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `hp_id` VARCHAR(12) NOT NULL,
  `term` VARCHAR(200) NULL,
  PRIMARY KEY (`id`));

CREATE INDEX `hp_id` ON `hp` (`hp_id` ASC);

-- -----------------------------------------------------
-- Table `hp_synonym`
-- -----------------------------------------------------

DROP TABLE IF EXISTS `hp_synonym` ;

CREATE TABLE IF NOT EXISTS `hp_synonym` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `hp_id` VARCHAR(12) NOT NULL,
  `synonym` VARCHAR(200) NULL,
  PRIMARY KEY (`id`));

CREATE INDEX `hps_hp_id` ON `hp_synonym` (`hp_id` ASC);


-- -----------------------------------------------------
-- Table `disease_disease_association`
-- -----------------------------------------------------

DROP TABLE IF EXISTS `disease_disease_association` ;

CREATE TABLE `disease_disease_association` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `disease_id` varchar(20) NOT NULL,
  `disease_match` varchar(20) NOT NULL,
  `disease_to_disease_perc_score` double NOT NULL DEFAULT '0',
  `raw_score` double NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`));
  
CREATE INDEX `dda_disease_id` ON `disease_disease_association` (`disease_id` ASC);
CREATE INDEX `dda_disease_match` ON `disease_disease_association` (`disease_match` ASC);
CREATE INDEX `dda_disease_disease` ON `disease_disease_association` (`disease_id` ASC,`disease_match` ASC);

-- -----------------------------------------------------
-- Table `disease_disease_association_detail`
-- -----------------------------------------------------

DROP TABLE IF EXISTS `disease_disease_association_detail` ;

CREATE TABLE `disease_disease_association_detail` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `disease_id` varchar(20) NOT NULL,
  `disease_match` varchar(20) NOT NULL,
  `hp_id` varchar(12) NOT NULL,
  `hp_match` varchar(12) NOT NULL,
  `simJ` double DEFAULT NULL,
  `ic` double DEFAULT NULL,
  `lcs` varchar(200),
  PRIMARY KEY (`id`));

CREATE INDEX `ddad_disease_disease` ON `disease_disease_association_detail` (`disease_id` ASC,`disease_match` ASC);

-- -----------------------------------------------------
-- Table `hp_hp_mapping`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `hp_hp_mapping` ;

CREATE TABLE `hp_hp_mapping` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `hp_id` varchar(12) NOT NULL,
  `hp_term` varchar(200) DEFAULT NULL,
  `hp_id_hit` varchar(12) NOT NULL,
  `hp_term_hit` varchar(200) DEFAULT NULL,
  `simJ` float DEFAULT NULL,
  `ic` float DEFAULT NULL,
  `lcs` varchar(200),
  `ic_ratio` float DEFAULT NULL,
  PRIMARY KEY (`id`));

CREATE INDEX `hphpm_hp_hp` ON `hp_hp_mapping` (`hp_id` ASC,`hp_id_hit` ASC);

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
