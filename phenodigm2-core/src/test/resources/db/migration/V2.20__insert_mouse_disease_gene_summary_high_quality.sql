--insert of the high quality data from mouse_disease_gene_summary - this will reduce the amount of data to about 120k rows from 140 mill

insert into mouse_disease_gene_summary_high_quality 
select * 
from mouse_disease_gene_summary where ((human_curated is true or mod_curated is true) or (mod_raw_score >= 1.8 or htpc_raw_score >= 1.8));
