--insert of the high quality data from mouse_disease_gene_summary - this will reduce the amount of data to about 120k rows from 140 mill

insert into mouse_disease_gene_summary_high_quality select * from mouse_disease_gene_summary where ((human_curated is true or mod_curated is true) or (mod_raw_score >= 1.97 or htpc_raw_score >= 1.97));

-- Using OWLSim2 the scores are quite different: 

-- insert into mouse_disease_gene_summary_high_quality 
-- select * from mouse_disease_gene_summary 
-- where ((human_curated is true or mod_curated is true) 
-- or ((mod_raw_score > 5.5 and max_mod_disease_to_model_perc_score > 50.0) 
-- or (htpc_raw_score > 5.5 and max_htpc_disease_to_model_perc_score > 50.0)));