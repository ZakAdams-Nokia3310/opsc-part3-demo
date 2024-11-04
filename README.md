SyncUp - Event Discovery App Documentation (Final Submission)

Group 3 OPSC7312 POE - Final Submission

Gregory Mbiya - ST10090997
Zakariyyah Adams - ST10091209
Imaan Ebrahim - ST10021922
Jasmin Kisten - ST10025239
Ryan Khan - ST10155076
Kyle Govender - ST10090959

Overview

SyncUp is a sophisticated Android event discovery app designed to enhance the user experience of finding, creating, and participating in events. Using Single Sign-On (SSO) with Google, biometric authentication for added security, Firebase Realtime Database for real-time data handling, and an API-based architecture for seamless synchronization, SyncUp allows users to personalize their event recommendations, vote on event playlists, manage language preferences, and receive real-time notifications. This final version incorporates enhancements and adjustments based on prior feedback, ensuring it is fully prepared for deployment on the Google Play Store.

Table of Contents:
App Purpose
Features
Design Considerations
Database Structure
API Endpoints
GitHub Version Control
GitHub Actions & Automated Testing
Blob Storage
Real-Time Notifications
Multi-Language Support
Demo Video
Release Notes
Credits
Conclusion

How to Run the Application
1. App Purpose
The SyncUp app is designed to simplify and enhance the process of event discovery and participation by offering personalized event recommendations, user-friendly navigation, and secure authentication. The main functionalities include:

User Registration and Login:

Single Sign-On (SSO) with Google for fast and seamless authentication.
Biometric options (Fingerprint and Face ID) for secure, quick access.
Event Creation and Discovery:

Users can browse and create events with custom details like event name, location, date, time, and genre.
Offline support allows users to interact with the app even without internet connectivity, with automatic synchronization when back online.
Event Interaction:

Join events directly through the app, and access event locations on an integrated map.
Vote on event-specific playlists and view live updates across devices.
Profile Management:

Manage personal settings, such as favorite event genres and notification preferences.
2. Features
Single Sign-On (SSO) Integration:
Provides a simple and seamless login experience with Google accounts, reducing login friction for users.

Biometric Authentication:
Adds a layer of security with fingerprint and Face ID authentication, ensuring user data remains safe.

Profile Settings:
Users can update and personalize their profile information, preferences, and notification settings.

Offline Mode with Sync:
By using RoomDB for local storage, the app allows full functionality even offline, synchronizing data automatically with Firebase Realtime Database when a connection is available.

Real-time Notifications:
Event updates, reminders, and personalized recommendations are sent using Firebase Cloud Messaging, keeping users engaged and informed.

Multi-Language Support:
SyncUp includes support for English and an additional South African language, making it accessible to a wider audience.

Blob Storage for Images:
Firebase Storage enables the secure and scalable upload and management of event images and profile pictures.

3. Design Considerations
SyncUp was carefully designed with the following principles to ensure a smooth and responsive user experience:

Intuitive User Interface (UI):
The interface was developed with a focus on simplicity and accessibility, allowing users to navigate through the app’s features with ease.

Robust Error Handling and Validation:
Validations ensure that user inputs are checked for accuracy, and error handling has been implemented to prevent unexpected app crashes, enhancing reliability.

Scalability:
The backend structure and API design allow for future expansion and efficient handling of growing data and user activity.

Performance Optimization:
Firebase Realtime Database and API-based architecture ensure that data loads quickly, providing a responsive experience for users.

4. Database Structure
SyncUp utilizes a combination of Firebase Realtime Database for real-time data synchronization and RoomDB (SQLite) for offline storage. Firebase Realtime Database serves as the app’s cloud-based NoSQL database, enabling data to be accessed, updated, and synchronized across devices seamlessly, while RoomDB facilitates offline interactions by caching data locally. Together, they support both real-time interactions and offline functionality.

5. API Endpoints
The app’s functionality is powered by a REST API that interacts with Firebase Realtime Database. Key endpoints include:

GET /events: Retrieve a list of all available events.
POST /events: Allow users to create new events.
GET /events/{id}/votes: Fetch playlist votes for a specific event.
POST /events/{id}/votes: Submit a vote for an event’s playlist.
These endpoints are accessed via Retrofit, ensuring efficient and reliable data retrieval and submission.

6. GitHub Version Control
GitHub link: https://github.com/ZakAdams-Nokia3310/opsc-part3-demo.git

The app’s development followed strict version control practices using GitHub:

Repository Initialization:
A README file was included from the start to document the project’s purpose and structure.

Commit Strategy:
Consistent commits were made at each development milestone to ensure transparency and organization in code updates.

Branching:
Separate branches were created for feature development, bug fixes, and UI adjustments to streamline collaboration and maintain clean code.

7. GitHub Actions & Automated Testing
GitHub Actions was used to implement Continuous Integration (CI), automating tests and builds to ensure that the app is stable and functional at each stage of development.

Unit Tests:
Automated tests validate core features such as user registration, event creation, offline functionality, and playlist voting.

Tests include:

User registration and Google SSO login
Event creation and retrieval
Offline data sync with Firebase
Playlist voting feature
How to Run Tests:

Open the Test panel in Android Studio.
Select Run All Tests.
8. Blob Storage
Firebase Storage is used for managing images associated with events and user profiles. The benefits include:

Efficient Storage: Firebase Storage supports large files without straining app resources.
Secure Access: Firebase offers robust security for storing and accessing multimedia content.
Real-Time Sync: Updates to images appear instantly across user devices.
9. Real-Time Notifications
Using Firebase Cloud Messaging, the app delivers timely notifications, ensuring users are informed and engaged. Notifications include:

Event Reminders: Notifying users of upcoming events they’ve joined.
Playlist Updates: Alerting users about voting results and changes to event playlists.
Personalized Recommendations: Event suggestions based on user preferences.
10. Multi-Language Support
SyncUp provides language options in English and an additional South African language. Users can select their preferred language in the settings, with all text dynamically updated.

11. Demo Video
A demonstration video of SyncUp is available here.

https://youtu.be/fco-EfGO51g

Features Showcased:

User registration and biometric authentication
Event discovery, creation, and joining
Playlist voting with real-time updates
Offline functionality and Firebase synchronization
Notifications and multi-language support
12. Release Notes
Version 1.0 - Final Prototype

New Features Added

SSO and Biometric Authentication: Integrated Google SSO and biometric options for improved login security.
Real-Time Sync: Firebase Realtime Database syncs data across devices seamlessly.
Real-Time Notifications: Firebase Cloud Messaging for event updates and reminders.
Multi-Language Support: English and an additional South African language.
Blob Storage for Images: Firebase Storage for event and profile images.
Improvements Made

Performance Optimization: Enhanced data loading and sync speed.
UI/UX Refinements: Improved layout, font consistency, and accessibility.
Scalability Adjustments: Backend optimized for potential growth in users and data.
13. Credits
Group Members

Gregory Mbiya
Zakariyyah Adams
Imaan Ebrahim
Jasmin Kisten
Ryan Khan
Kyle Govender
External Libraries Used

Firebase Realtime Database for NoSQL data storage and real-time sync.
Firebase Storage for image storage.
Firebase Cloud Messaging for notifications.
Retrofit for API communication.
Google Sign-In for SSO integration.
14. Conclusion
SyncUp provides a dynamic solution for event discovery and participation, with robust features like SSO, real-time playlist voting, offline capabilities, and multilingual support. This comprehensive app is designed for scalability, efficiency, and user engagement.

Future Enhancements

Advanced recommendations for event suggestions.
Expanded social features for event interactions.
Integrations with third-party services for enhanced functionality.
15. How to Run the Application
1. Clone the Repository

bash
Copy code
git clone https://github.com/yourusername/SyncUpApp.git
2. Open in Android Studio
Open the project in Android Studio.

3. Configure Firebase

Add the google-services.json file to the app/ directory (available from Firebase Console).
Ensure Firebase Realtime Database and Storage are set up in Firebase.
4. Build the Project
In Android Studio, go to Build > Make Project to compile the app.

5. Run on Device
Connect an Android device or use an emulator, then select Run > Run 'app'.

6. Usage

Register or sign in with Google SSO.
Browse, create, and join events.
Use playlist voting, offline mode, and receive notifications when online.
