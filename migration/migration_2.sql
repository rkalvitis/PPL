--add primary key columns to tables
alter table Pasakuma_Rikotajs add Pasakuma_Rikotajs_ID int identity(1,1) primary key
alter table Atsauksme_Servisam add Atsauksme_Servisam_ID int identity(1,1) primary key
alter table Pasakuma_ServisaSniedzeji  add Pasakuma_ServisaSniedzeji_ID int identity(1,1) primary key
alter table Pasakuma_Viesi  add Pasakuma_Viesi_ID int identity(1,1) primary key
alter table Pienakumi_Lietotajam  add Pienakumi_Lietotajam_ID int identity(1,1) primary key
--drop contraint
alter table Pienakumi_Lietotajam  drop constraint FK__Pienakumi__lieto__628FA481
alter table Pienakumi_Lietotajam drop column lietotajs_ID
--add new column and new constriant
alter table Pienakumi_Lietotajam add Pasakuma_Rikotajs_ID int foreign key REFERENCES Pasakuma_Rikotajs(Pasakuma_Rikotajs_ID)
--changed column data type
ALTER TABLE Pasakums ALTER COLUMN atrasanas_vieta nvarchar(255);
