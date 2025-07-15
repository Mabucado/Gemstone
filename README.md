# Gemstone Collection App

The Gemstone Collection App is an Android application for gemstone enthusiasts to digitally manage, track, and showcase their gemstone collections. Users can register, log in, add gemstones with images and videos, organize them into categories, set collection goals, and view their achievements.

## Features

- **User Authentication:** Register and log in with email and password. User data is securely stored in Firestore.
- **Dashboard:** Central hub for navigation and viewing collection achievement status.
- **Add & Manage Gemstones:** Add gemstones with details, images (from camera or gallery), and videos. Edit and view your collection.
- **Categories:** Organize gemstones into custom categories with images.
- **Gallery:** View all gemstone images in a grid gallery.
- **Video Gallery:** Browse and play videos associated with gemstones.
- **Achievements:** Set collection goals for categories and track progress visually.
- **History:** Review a detailed history of all gemstones added, including when and where they were found.
- **Firebase Integration:** All data, images, and videos are securely stored and retrieved using Firebase services.

## Screens & Components

- **MainActivity:** Login screen.
- **RegisterActivity:** User registration.
- **dashboardActivity2:** Main dashboard for navigation and achievement status.
- **categories:** Add/view gemstone categories.
- **Achievements:** Track progress toward collection goals.
- **Items:** List and manage gemstone items.
- **itemsForm:** Add new gemstone items with image/video upload.
- **History:** View collection history.
- **image_gallery:** Grid gallery of gemstone images.
- **VideoGallary:** Grid gallery of gemstone videos.
- **VideoPlayerActivity:** Play videos.
- **Fragments:** Simple navigation between app sections.
- **StoreImage:** Dialog fragment for capturing or uploading images.

## Getting Started

1. **Clone the repository:**
   ```sh
   git clone https://github.com/Mabucado/gemstone-collection-app.git
   ```

2. **Open in Android Studio or VS Code.**

3. **Set up Firebase:**
   - Create a Firebase project.
   - Add your `google-services.json` to the `app` directory.
   - Enable Firestore and Firebase Storage.

4. **Build and run the app on your Android device or emulator.**

## Dependencies

- AndroidX
- Firebase Firestore & Storage
- ExoPlayer (for video playback)
- Glide (for image loading)
- ViewModel & LiveData

## License

This project is licensed under the MIT License.

---

**Enjoy managing your gemstone collection!**
