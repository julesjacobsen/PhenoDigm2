-- create disease tables

-- -----------------------------------------------------
-- Table `disease`
-- -----------------------------------------------------

DROP TABLE IF EXISTS `disease` ;

CREATE TABLE IF NOT EXISTS `disease` (
  `disease_id` VARCHAR(20) NULL,
  `disease_term` VARCHAR(255) NOT NULL,
  `disease_alts` TEXT NULL,
  `disease_locus` VARCHAR(255) NULL,
  `disease_classes` VARCHAR(255) NULL DEFAULT 'unclassified',
  PRIMARY KEY (`disease_id`));


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
  `hp_id` VARCHAR(12) NOT NULL,
  `term` VARCHAR(200) NULL,
  PRIMARY KEY (`hp_id`));

