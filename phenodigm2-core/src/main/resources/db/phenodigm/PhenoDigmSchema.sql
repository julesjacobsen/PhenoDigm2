--liquibase formatted sql
--changeset jules:1
DROP TABLE IF EXISTS disease;
CREATE TABLE disease (
  disease_id VARCHAR(20) NOT NULL,
  type varchar(10) NULL,
  disease_full_id varchar(20) NULL,
  disease_term varchar(512) NULL,
  disease_alts varchar(512),
  PRIMARY KEY (disease_id)
) ;

--changeset jules:2

--
-- Table structure for table omim_genes
--

DROP TABLE IF EXISTS omim_genes;


CREATE TABLE omim_genes (
  omim_gene_id varchar(20) DEFAULT NULL,
  omim_disease_id varchar(20) DEFAULT NULL,
  gene_symbol varchar(100) DEFAULT NULL,
  id int(11) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (id),
  UNIQUE KEY omim_gene_id_2 (omim_gene_id,omim_disease_id,gene_symbol),
  KEY omim_disease_id (omim_disease_id),
  KEY omim_gene_id (omim_gene_id),
  CONSTRAINT omim_genes_ibfk_2 FOREIGN KEY (omim_gene_id) REFERENCES disease (disease_id),
  CONSTRAINT omim_genes_ibfk_3 FOREIGN KEY (omim_disease_id) REFERENCES disease (disease_id)
);


--
-- Table structure for table mouse_models
--

DROP TABLE IF EXISTS mouse_models;


CREATE TABLE mouse_models (
  mouse_model_id int(8) unsigned NOT NULL,
  allelic_composition varchar(256) NOT NULL,
  genetic_background varchar(256) NOT NULL,
  gsym_link varchar(1000) DEFAULT NULL,
  allcomp_link varchar(1000) DEFAULT NULL,
  source varchar(10) DEFAULT 'MGI',
  hom_het varchar(3) DEFAULT NULL,
  KEY mouse_model_id (mouse_model_id),
  KEY source (source),
  KEY allelic_composition (allelic_composition)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


--
-- Table structure for table mgi_gene_models
--

DROP TABLE IF EXISTS mgi_gene_models;


CREATE TABLE mgi_gene_models (
  mgi_gene_id varchar(20) NOT NULL,
  mouse_model_id int(10) unsigned NOT NULL,
  id int(11) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (id),
  KEY mgi_gene_id (mgi_gene_id),
  KEY mouse_model_id (mouse_model_id),
  CONSTRAINT mgi_gene_models_ibfk_1 FOREIGN KEY (mgi_gene_id) REFERENCES mgi_genes (mgi_gene_id),
  CONSTRAINT mgi_gene_models_ibfk_2 FOREIGN KEY (mouse_model_id) REFERENCES mouse_models (mouse_model_id)
) ENGINE=InnoDB AUTO_INCREMENT=30370 DEFAULT CHARSET=latin1;


--
-- Table structure for table omim_disease_2_mgi_mouse_models
--

DROP TABLE IF EXISTS omim_disease_2_mgi_mouse_models;


CREATE TABLE omim_disease_2_mgi_mouse_models (
  omim_disease_id varchar(20) DEFAULT NULL,
  mouse_model_id int(8) DEFAULT NULL,
  id int(11) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (id),
  UNIQUE KEY od_mid (omim_disease_id,mouse_model_id),
  KEY gid (omim_disease_id),
  KEY mid (mouse_model_id)
) ENGINE=InnoDB AUTO_INCREMENT=3141 DEFAULT CHARSET=latin1;


--
-- Table structure for table disease_mouse_genotype_associations
--

DROP TABLE IF EXISTS disease_mouse_genotype_associations;


CREATE TABLE disease_mouse_genotype_associations (
  disease_id varchar(20) DEFAULT NULL,
  mouse_model_id int(8) unsigned NOT NULL,
  mgi_gene_id varchar(20) NOT NULL,
  mgi_gene_symbol varchar(100) NOT NULL,
  source varchar(10) DEFAULT NULL,
  allelic_composition varchar(256) DEFAULT NULL,
  genetic_background varchar(256) DEFAULT NULL,
  allcomp_link varchar(1000) DEFAULT NULL,
  raw_score float(5,2) DEFAULT NULL,
  disease_to_mouse_perc_score float(5,2) DEFAULT NULL,
  mouse_to_disease_perc_score float(5,2) DEFAULT NULL,
  KEY disease_id (disease_id),
  KEY mouse_model_id (mouse_model_id),
  KEY mgi_gene_id (mgi_gene_id)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


--
-- Table structure for table hp_term_infos
--

DROP TABLE IF EXISTS hp_term_infos;


CREATE TABLE hp_term_infos (
  term_id varchar(12) NOT NULL,
  name varchar(255) NOT NULL,
  definition text,
  is_obsolete enum('yes','no') NOT NULL,
  comment text,
  UNIQUE KEY term_id (term_id),
  KEY ontoidx (term_id)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


--
-- Table structure for table disease_hp
--

DROP TABLE IF EXISTS disease_hp;


CREATE TABLE disease_hp (
  disease_id varchar(20) NOT NULL DEFAULT '',
  hp_id varchar(12) NOT NULL DEFAULT '',
  evidence varchar(50) DEFAULT NULL,
  annotation_source varchar(200) DEFAULT NULL,
  frequency varchar(20) DEFAULT NULL,
  PRIMARY KEY (disease_id,hp_id),
  KEY disease_id (disease_id),
  KEY hp_id (hp_id),
  CONSTRAINT omim_hp_ibfk_1 FOREIGN KEY (disease_id) REFERENCES disease (disease_id),
  CONSTRAINT omim_hp_ibfk_2 FOREIGN KEY (hp_id) REFERENCES hp_term_infos (term_id)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


--
-- Table structure for table mp_mouse_models
--

DROP TABLE IF EXISTS mp_mouse_models;


CREATE TABLE mp_mouse_models (
  mouse_model_id int(8) unsigned DEFAULT NULL,
  mp_id varchar(12) DEFAULT NULL,
  id int(11) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (id),
  KEY mouse_model_id (mouse_model_id,mp_id),
  KEY mp_id (mp_id),
  KEY mouse_model_id_2 (mouse_model_id),
  CONSTRAINT mp_mouse_models_ibfk_1 FOREIGN KEY (mouse_model_id) REFERENCES mouse_models (mouse_model_id),
  CONSTRAINT mp_mouse_models_ibfk_2 FOREIGN KEY (mp_id) REFERENCES mp_term_infos (term_id)
) ENGINE=InnoDB AUTO_INCREMENT=167570 DEFAULT CHARSET=latin1;


--
-- Table structure for table mp_term_infos
--

DROP TABLE IF EXISTS mp_term_infos;


CREATE TABLE mp_term_infos (
  term_id varchar(12) NOT NULL,
  name varchar(255) NOT NULL,
  definition text,
  is_obsolete enum('yes','no') NOT NULL,
  comment text,
  UNIQUE KEY term_id (term_id),
  KEY ontoidx (term_id)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

