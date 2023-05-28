# PPL

## OOP principles

Projekts ir izstrādāts, izmantojot modernas Android lietotņu izstrādes labākās prakses un arhitektūras modeļus, lai nodrošinātu kodu, kas ir viegli uzturēt un paplašināt.

Mēs izmantojām Model-View-Controller (MVC) un Model-View-ViewModel (MVVM) modeļus. 
Modelis definē lietotnes datu struktūru un biznesa loģiku. Skats, ko parasti reprezentē aktivitātes, fragmenti un XML izkārtojumi, atspoguļo šos datus lietotājam. 
Kontrolieris vai ViewModel apstrādā lietotāja ievadi un atjaunina modeli, nodrošinot vispārīgu lietotāja saskarnes un datu atdalīšanu.

Izmantojot Data Access Object (DAO) principu, mēs definējām, kā lietotne sazinās ar datubāzi. DAO ļauj mums izolēt datu apstrādāšanas 
loģiku un nodrošināt vienkāršāku un tīrāku kodu, kas ir viegli uzturēt.

Papildus izstrādājām DatabaseHelper klasi, kas veic tiešo saziņu ar datubāzi (CRUD darbības). DatabaseHelper klase tika integrēta ar DAO komponentēm.

Mēs arī izmantojām adapterus, lai savienotu datus ar Android skatu komponentiem, piemēram, RecyclerView vai ListView. 
Adapteri nodrošina efektīvu un optimizētu datu parādīšanu skatā, tādējādi uzlabojot lietotāja pieredzi.


Šāda arhitektūra nodrošina modulāru un uzturējamu kodu, kas ir viegli testēt un paplašināt.

## Application design and views

#### Pamata skats
<br/>

<img src="https://github.com/rkalvitis/PPL/assets/62305390/17a4e217-d289-4fee-a148-c45eb51fbbf4" width="300">

<br/>

#### Reģistrēšanās skats

<br/>

<img src="https://github.com/rkalvitis/PPL/assets/62305390/dc207906-74d5-46cc-ba6c-4fcb6b4639b1" width="300">

<br/>

#### Lietotāja sakts

<br/>

<img src="https://github.com/rkalvitis/PPL/assets/62305390/fab09c30-7541-4599-a4ee-106e62c813c5" width="300">

<br/>

#### Meklēšanas skats

<br/>

<img src="https://github.com/rkalvitis/PPL/assets/62305390/52eba067-22ef-41dd-bee3-37e97fc176bb" width="300">

<br/>

#### Servisa rezervācijas skats

<br/>

<img src="https://github.com/rkalvitis/PPL/assets/62305390/8fd04f32-9fe8-48bf-baec-ab4c31fb11da" width="300">

<br/>

#### Administratora skats-1

<br/>

<img src="https://github.com/rkalvitis/PPL/assets/62305390/adcc136b-3e0d-4d47-b440-c45fa1946b0b" width="300">

<br/>

#### Administratora skats-2

<br/>

<img src="https://github.com/rkalvitis/PPL/assets/62305390/77d4c31d-71be-4961-8e65-9037aed0a67b" width="300">

<br/>

#### Administratora skats-3

<br/>

<img src="https://github.com/rkalvitis/PPL/assets/62305390/3ea13c18-7154-48d6-b93d-26ef3d0dd164" width="300">

