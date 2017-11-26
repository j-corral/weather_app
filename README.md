# Weather App

## Authors
Jonathan Corral

Student in master degree of computer science at CERI Avignon


## Contributors
Stephane Huet : [JSonResponseHandler](https://github.com/johnlejardinnier/weather_app/blob/master/app/src/main/java/com/quai13/weather/JSONResponseHandler.java)

## Instructions
- Clone repo on your workspace : `git clone https://github.com/johnlejardinnier/weather_app.git`

- Open the project with Android Studio

- Connect a smartphone to your computer

- Run the project


## Requirements
Require Android SDK
- Min SDK version: 23
- Target SDK version: 25


## About
Weather is a native Android application which allows you to get weather from Yahoo Api.

![Home](home.png)

![Add city](add.png)

![View city](city.png)

![Delete city](delete.png)



## Project state
- TP1 finished:

    - use ListView
    - use ArrayList
    - use Activities
    - use Button Listener
    - use Async Task


- TP2 : 90%

    - use DB with SQLite (ok)
    - use Content Provider (ok)
    - use Loader with SimpleCursorAdapter (ok)
    - use Sync Adapter (90%, cf. Bugs section)



## Features
- Add city
- Remove city
- Update weather
- View basic weather information for a given city
- View cities ordered by name ASC

## Bugs
- Crash if city or country does not exist
- Layouts are not perfect
- Date is not parsed properly
- Automatic sync does not seems to work
