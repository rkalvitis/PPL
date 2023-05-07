create table Pienakums (
	pienakums_ID int IDENTITY PRIMARY KEY NOT NULL,
	nosaukums nvarchar(30) NOT NULL,
	apraksts nvarchar(200) NOT NULL,
	paveikts bit NOT NULL DEFAULT 0
)

create table Lietotajs (
	lietotajs_ID int IDENTITY PRIMARY KEY NOT NULL,
	vards nvarchar(20) NOT NULL,
	uzvards nvarchar(20) NOT NULL,
	epasts nvarchar(50) NOT NULL,
	telefons nvarchar(15),
	Parole nvarchar(65) NOT NULL,
)

create table Pienakumi_Lietotajam(
	pienakums_ID int NOT NULL,
	lietotajs_ID int NOT NULL,
	FOREIGN KEY(pienakums_ID) REFERENCES Pienakums(pienakums_ID),
	FOREIGN KEY(lietotajs_ID) REFERENCES Lietotajs(lietotajs_ID)
)


create table Pasakums (
	pasakums_ID int IDENTITY PRIMARY KEY NOT NULL,
	nosaukums nvarchar(50) NOT NULL,
	datumslaiks datetime NOT NULL,
	atrasanas_vieta geography,
	foto_video nvarchar(255)
)

create table Pasakuma_Rikotajs (
	pasakums_ID int NOT NULL,
	lietotajs_ID int NOT NULL,
	rikotajs bit NOT NULL,
	paligs bit NOT NULL,
	FOREIGN KEY(pasakums_ID) REFERENCES Pasakums(pasakums_ID),
	FOREIGN KEY(lietotajs_ID) REFERENCES Lietotajs(lietotajs_ID)
)

create table Viesis (
	viesis_ID int IDENTITY PRIMARY KEY NOT NULL,
	vards nvarchar(20),
	uzvards nvarchar(20),
	epasts nvarchar(50) NOT NULL,
	telefons nvarchar(15),
)

create table Pasakuma_Viesi (
	pasakums_ID int NOT NULL,
	viesis_ID int NOT NULL,
	FOREIGN KEY(pasakums_ID) REFERENCES Pasakums(pasakums_ID),
	FOREIGN KEY(viesis_ID) REFERENCES Viesis(viesis_ID)
)

create table Servisa_sniedzejs (
	servisasniedzejs_ID int IDENTITY PRIMARY KEY NOT NULL,
	nosaukums nvarchar(50) NOT NULL,
	epasts nvarchar(50) NOT NULL,
	pilseta nvarchar(30) NOT NULL,
	adrese nvarchar(40) NOT NULL,
	telefons nvarchar(15),
	parole nvarchar(65) NOT NULL
)

create table Pasakuma_ServisaSniedzeji (
	pasakums_ID int NOT NULL,
	servisasniedzejs_ID int NOT NULL,
	FOREIGN KEY(pasakums_ID) REFERENCES Pasakums(pasakums_ID),
	FOREIGN KEY(servisasniedzejs_ID) REFERENCES Servisa_sniedzejs(servisasniedzejs_ID)
)

create table Servisa_tips (
	servisatips_ID int IDENTITY PRIMARY KEY NOT NULL,
	tips nvarchar(100) NOT NULL,
	servisasniedzejs_ID int NOT NULL,
	FOREIGN KEY(servisasniedzejs_ID) REFERENCES Servisa_sniedzejs(servisasniedzejs_ID)
)

create table Sniedzeja_rezervesana (
	rezervacijas_ID int IDENTITY PRIMARY KEY NOT NULL,
	datumsNo datetime NOT NULL,
	datumsLidz datetime NOT NULL,
	servisasniedzejs_ID int NOT NULL,
	FOREIGN KEY(servisasniedzejs_ID) REFERENCES Servisa_sniedzejs(servisasniedzejs_ID)
)

create table Atsauksme (
	atsauksmes_ID int IDENTITY PRIMARY KEY NOT NULL,
	vertejums int NOT NULL,
	apraksts nvarchar(200) NOT NULL,
)

create table Atsauksme_Servisam (
	servisasniedzejs_ID int NOT NULL,
	atsauksmes_ID int NOT NULL,
	FOREIGN KEY(servisasniedzejs_ID) REFERENCES Servisa_sniedzejs(servisasniedzejs_ID),
	FOREIGN KEY(atsauksmes_ID) REFERENCES Atsauksme(atsauksmes_ID),
)
