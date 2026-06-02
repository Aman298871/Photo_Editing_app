# 📸 Photo Editing App

A modern Android Photo Editing App built using **Kotlin** and **Jetpack Compose**. The application allows users to select images from their device gallery, apply image enhancement filters, preview changes instantly, and save the edited image back to the gallery.

---

## 🚀 Features

### 🖼️ Image Selection
- Select images directly from the device gallery.
- Fast and user-friendly image picker.

### 🎨 Image Filters
- Brightness Adjustment
- Contrast Adjustment
- Saturation Adjustment
- Real-time image preview

### 💾 Save Edited Images
- Save edited images directly to the device gallery.
- High-quality JPEG image output.

### ⚡ Modern UI
- Built entirely using Jetpack Compose.
- Responsive and clean user interface.
- Material Design components.

---

## 📱 Screenshots
![image alt](https://github.com/Aman298871/Photo_Editing_app/blob/67a7e7e40ea374c19ed6a11de2083bb26c5bfbfa/photo_2026-06-02_20-41-55.jpg)

![image alt](https://github.com/Aman298871/Photo_Editing_app/blob/67a7e7e40ea374c19ed6a11de2083bb26c5bfbfa/photo_2026-06-02_20-41-58.jpg)


| Home Screen | Filter Editing | Saved Image |
|------------|---------------|------------|
| Screenshot | Screenshot | Screenshot |

---

## 🛠️ Tech Stack

### Language
- Kotlin

### UI Framework
- Jetpack Compose
- Material 3

### Android Components
- Activity Result API
- MediaStore API
- Content Resolver

### Image Processing
- Bitmap
- Canvas
- ColorMatrix
- ColorMatrixColorFilter

---

## 📂 Project Structure

```text
com.example.photoeditingapp
│
├── MainActivity.kt
│
├── PhotoEditingApp()
│
├── FilterControl()
│
├── loadBitmapFromUri()
│
├── applyFilter()
│
└── saveImageToGallery()
```

---

## ⚙️ How It Works

1. User selects an image from the gallery.
2. The image is converted into a Bitmap.
3. Brightness, Contrast, and Saturation values are adjusted using sliders.
4. ColorMatrix transformations are applied to the image.
5. The edited image is displayed in real-time.
6. User saves the image to the gallery.

---

## 📸 Filter Controls

### Brightness
Adjusts image brightness by increasing or decreasing RGB channel values.

### Contrast
Enhances or reduces the difference between light and dark areas.

### Saturation
Controls the intensity and richness of colors.

---

## 🎯 Learning Objectives

This project demonstrates:

- Jetpack Compose UI Development
- State Management in Compose
- Activity Result API Usage
- Bitmap Manipulation
- ColorMatrix Image Processing
- MediaStore Image Saving
- Android Storage APIs

---

## 🔮 Future Improvements

- Crop Image
- Rotate Image
- Undo / Redo
- Exposure Filter
- Sharpness Filter
- Blur Effects
- Preset Filters
- Dark Mode Support
- Share Edited Images
- Image Compression

---

## 📋 Requirements

- Android Studio
- Kotlin
- Android SDK 24+
- Jetpack Compose

---

## 👨‍💻 Author

Aman Singh

Android Developer | Kotlin | Jetpack Compose

---

## ⭐ Support

If you found this project helpful, consider giving it a ⭐ on GitHub.
