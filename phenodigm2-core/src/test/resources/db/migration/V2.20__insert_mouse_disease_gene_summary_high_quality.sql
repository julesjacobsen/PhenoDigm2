--insert of the high quality data from mouse_disease_gene_summary - this will reduce the amount of data to about 120k rows from 140 mill

insert into mouse_disease_gene_summary_high_quality 
select *
, (case when mod_raw_score is null then false else true end) as mod_predicted
, (case when (mod_raw_score is not null and human_curated) then true else false end) as mod_predicted_known_gene
, (case when (mod_raw_score is not null and not human_curated and in_locus) then true else false end) as novel_mod_predicted_in_locus
, (case when htpc_raw_score is null then false else true end) as htpc_predicted 
, (case when (htpc_raw_score is not null and human_curated) then true else false end) as htpc_predicted_known_gene
, (case when (htpc_raw_score is not null and not human_curated and in_locus) then true else false end) as novel_htpc_predicted_in_locus
 from mouse_disease_gene_summary where ((human_curated is true or mod_curated is true) or (mod_raw_score >= 1.7 or htpc_raw_score >= 1.7));
