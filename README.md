# Rubist-Application
Rubist (Rubbish Sorting Assistant) Application - C23-PS480

<img src="https://github.com/berlianm/Rubist-Application/blob/main/logo/logo_green.png" alt="Logo" style="display: inline-block; margin: 0 auto; max-width: 100px">

<br />

### 🙋🏻‍♂️ Team Member

- (ML) M360DKX4331 – Berlian Muhammad Galin Al Awienoor
- (ML) M360DKX4329 – Fakhrul Maulidan Gustiana
- (CC) C151DSY1649 – Putri Ainul Khikmah
- (CC) C281DSX3151 – Andi Reski
- (MD) A313DKX4555 – Gutri Rahmad Zuwa
- (MD) A360DKX4348 – Kiki Dwi Prasetyo
<br />

### Theme: Sustainable Living

### Abstract:
Rubbish Sorting Assistant is a mobile application designed to address the problem of improper rubbish management in Indonesia. With a lack of awareness of proper rubbish management, many people contribute to the growing rubbish problem. Through the use of machine learning algorithms, Rubbish Sorting Assistant aims to provide a solution by assisting users in identifying the type of rubbish and providing instructions on how to properly dispose or recycle it. The goal of the project is to promote better rubbish management solutions and create a cleaner and healthier environment. By helping people manage rubbish properly, we hope to reduce the rubbish problem in Indonesia and contribute to a cleaner and healthier future.

_Rubist is an app designed to help users detect types of waste and provide important information about them. In addition, the app also provides a forum feature that allows users to share stories, discuss, and upload posts related to waste and the environment._

### 🔑 Key Feature
- Waste Type Detection: The Rubist app comes with the ability to detect the type of litter. Users can use their phone's camera or image from gallery to take pictures of litter, and Rubist will provide information on the type of litter detected.
- Important Information: After detecting the type of waste, Rubist will display important information related to the waste. This information includes a description of the waste, its environmental impact, proper disposal, whether or not the waste can be recycled, and how to recycle it.
- Forum Feature: Rubist provides a forum feature that allows users to share stories, discuss, and upload posts related to waste and the environment. Users can upload images, write text, and interact with other users through comments.

### Dataset: 
- [Garbage Classification](https://drive.google.com/drive/folders/1aDMo-ZzUSCMDFDD16CPP3Lofb64It41J?usp=sharing) or
- https://github.com/berlianm/Rubist-Application

<br />

# 1. Machine Learning
You can see more details for Machine Learning at [MODEL](https://github.com/berlianm/Rubist-Application/tree/main/Model%20Classification).

## Convolutional Neural Network (CNN)
CNN is a type of neural network architecture specifically for visual data processing, such as images. CNN is inspired by the structure of the visual cortex in the human brain and is designed to effectively extract features from images.
<img src="https://github.com/berlianm/Rubist-Application/blob/main/Model%20Classification/cnn.jpeg" alt="cnn" style="display: inline-block; margin: 0 auto; max-width: 500px">

## MobileNet V2
MobileNetV2 is a Convolutional Neural Network (CNN) architecture specifically designed for image processing on mobile devices or Internet of Things (IoT) devices. It combines several techniques to achieve good performance in image processing tasks. This architecture provides a balance between computational speed and image recognition quality, making it suitable for applications that require real-time image processing on devices with limited resources. <br />
<img src="https://github.com/berlianm/Rubist-Application/blob/main/Model%20Classification/MobileNet%20V2/mobilenetv2.png" alt="mbnetv2" style="display: inline-block; margin: 0 auto; max-width: 500px">

### Tasks for the Machine Learning Team
1. Literature Reviews
2. Collecting  Dataset
3. Pre-processing Dataset
4. Build Model
5. Get Best Model
6. Evaluation Performance

DATASET:
- Data Cardboard: `403`
- Data Glass    : `501`
- Data Metal    : `410`
- Data Paper    : `594`
- Data Plastic  : `482`
- Data Trash    : `347`

Total: `2737` Data

### The Best Model is using MobileNet V2
#### Here The Result (ACCURACY & LOSS)
<img src="https://github.com/berlianm/Rubist-Application/blob/main/Model%20Classification/MobileNet%20V2/accuracy.png" alt="accu" style="display: inline-block; margin: 0 auto; max-width: 500px">
<img src="https://github.com/berlianm/Rubist-Application/blob/main/Model%20Classification/MobileNet%20V2/loss.png" alt="loss" style="display: inline-block; margin: 0 auto; max-width: 500px">
<br />



<br /><br />

# 2. Cloud Computing

## Our Architecture Cloud Design
<img src="https://github.com/berlianm/Rubist-Application/blob/main/architecture%20cloud%20design.png" alt="cc" style="display: inline-block; margin: 0 auto; max-width: 500px">
<br />

You can see more details at [here](https://github.com/berlianm/Rubist-Application/tree/main/CC%20API).

<br /><br />

# 3. Android Development
### Clone Repository
1. Download code from branch "main" OR
2. Clone Repository. Click on dropdown Code and **copy** this link <br/>
```
git clone https://github.com/berlianm/Rubist-Application.git
```
### Featured Technologies
* [Android Studio](https://developer.android.com/studiogclid=CjwKCAjwp6CkBhB_EiwAlQVyxRlqEBd1HaF0B9PVKBPhUST26W_3W5vsPddfzTOr4kraMXGyVNtu0RoCBfEQAvD_BwE&gclsrc=aw.ds) : Android Studio is an integrated development environment (IDE) used to develop Android applications. It is the official software provided by Google for Android-based application development.
* [Kotlin](https://kotlinlang.org/) : Kotlin is a modern programming language that was officially introduced by JetBrains in 2011. Kotlin is designed to be an expressive, concise, secure, and interoperable language with Java. It is designed to address some of the weaknesses and limitations of the Java language while maintaining compatibility with the vast Java ecosystem.
*	[Retrofit](https://square.github.io/retrofit/): Retrofit is an open-source library for Android application development that is used to facilitate communication with web services or APIs (Application Programming Interface). Retrofit provides an easy and efficient way to access and send requests to servers via the HTTP protocol.
* [Camera intent](https://developer.android.com/training/camera/camera-intents): Camera Intent is an intent in Android app development used to access the device's camera and take new pictures directly through the device's default camera app. By using Camera Intent, developers can integrate camera functionality into their apps without the need to develop their own camera features.
*	[Gallery intent](): Gallery Intent is an intent in Android app development used to access the device's media (image or video) gallery and select existing media files for use in apps. By using the Gallery Intent, users can select media files that already exist on their device, such as images or videos, and use them in the application that triggered the intent.


### Task for the Mobile Development Team
1. Designing UI/UX prototype of the application,
2. Implementing UI/UX design to Android Studio using Kotlin,
3. Integrate Camera intent and Gallery intent,
4. Implementing retrofit library to connect with API, and
5. Build APK.

### Screenshot

<img src="https://github.com/berlianm/Rubist-Application/blob/main/logo/SS_app.png" alt="Logo" style="display: inline-block; margin: 0 auto; max-width: 30px">

<img src="https://github.com/berlianm/Rubist-Application/blob/main/logo/SS_result.png" alt="Logo" style="display: inline-block; margin: 0 auto; max-width: 30px">
