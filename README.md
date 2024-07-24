Personal Netflix app!

I want to be able to stream my personal media to any device at home (and potentially across the internet in the future).

This involves:
- A backend server with routes and the ability to serve media requests.
- A frontend web UI for use on PC with user registration and accounts.
- A database to store user credentials and user watch data (what have they watched, what time are they at in a certain movie etc.).
- An app for use on Android phones and TV (possibly iOS but I don't have any devices with apple software).

Optional extras:
- Web deployment. VPN set up and deploying app to cloud service.
- Automated testing framework (K6 for performance, Playwright/Selenium for E2E coverage, Appium for Android).
- CI/CD pipeline if this gets deployed to AWS (or other cloud provider).

Backend is built with NodeJS (written in Typescript)
Frontend is React (Typescript)
Database is MongoDB
Android app is Kotlin
