# Test Bench Data

This is an example where I'm using 

- Spring JPA for MySQL and MSSQL
- Spring Security for login
- CRUD Add-on
- App-Layout
- FTP Downloader
- ApexCharts

This web application can plot data from database, seach database, change, delete, create new data, analyze data in form of
line charts, download data in form of CSV files and it's easy to include more databases.

# How to use

1. Download this project
2. Configure all `.properties` files after your own settings
3. Go to `LxDataRepository.java` and `RsqDataRepository.java` and uncomment the right `@Query` command for your SQL database
4. Run the project with `mvn spring-boot:run -Pproduction`

# Necessary softwares

- NodeJs +14
- Maven
- MySQL or MSSQL
- OpenJDK +11

# How it looks like

Login page 

![](https://github.com/DanielMartensson/Test-Bench-Data/blob/main/pictures/login.PNG)

Data table

![](https://github.com/DanielMartenssonTest-Bench-Data/blob/main/pictures/Data.PNG)

Plotting curve

![](https://github.com/DanielMartensson/Test-Bench-Data/blob/main/pictures/Curve.PNG)

Other plots such as particle coutning and spool leakage

![](https://github.com/DanielMartensson/Test-Bench-Data/blob/main/pictures/Particle%20counting%20and%20spool%20leakage.PNG)



