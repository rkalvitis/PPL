# PPL

## Pilns apraksts
[OOP - full.pdf](https://github.com/rkalvitis/PPL/files/11584984/OOP.-.full.pdf)

## Tehnoloģijas
- Java (front-end + back-end)
- Miscrosoft Azure SQL (datubāze)

## OOP principi

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

## Lietotnes dizains un skati

#### Pamata skats
<br/>

<img src="https://github.com/rkalvitis/PPL/assets/62305390/c8002117-7879-4180-b475-873be7f849d9" width="300">

<br/>

#### Reģistrēšanās skats

<br/>

<img src="https://github.com/rkalvitis/PPL/assets/62305390/2669d593-f7ff-48b8-8351-a724aba3cf13" width="300">

<br/>
<br/>

#### Lietotāja sakts

<br/>

<img src="https://github.com/rkalvitis/PPL/assets/62305390/3976b246-373b-4582-a4e2-4db000377ed6" width="300">

<br/>
<br/>

#### Meklēšanas skats

<br/>

<img src="https://github.com/rkalvitis/PPL/assets/62305390/e5d0c238-2ad6-44d4-9d2b-6f452df5c4b6" width="300">

<br/>
<br/>

#### Servisa rezervācijas skats

<br/>

<img src="https://github.com/rkalvitis/PPL/assets/62305390/cca01c52-75f0-46f0-a646-db4dd6ef0328" width="300">

<br/>
<br/>

#### Administratora skats-1

<br/>

<img src="https://github.com/rkalvitis/PPL/assets/62305390/fa864b7f-356b-406e-b097-4efdc6e44571" width="300">

<br/>
<br/>

#### Administratora skats-2

<br/>

<img src="https://github.com/rkalvitis/PPL/assets/62305390/7a009ae9-663d-4b90-a226-3c0573c3440b" width="300">

<br/>
<br/>

#### Administratora skats-3

<br/>

<img src="https://github.com/rkalvitis/PPL/assets/62305390/fa5320ae-ca06-47e5-84c7-adb3e6d5ba1f" width="300">

<br/>
<br/>

#### Pasākuma skats

<br/>

<img src="https://github.com/rkalvitis/PPL/assets/97198248/08f9f0e2-d72a-419b-8b3e-e0a7f84e7eb2" width="300">
