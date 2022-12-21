# Venom

[![](https://jitpack.io/v/YarikSOffice/venom.svg)](https://jitpack.io/#YarikSOffice/venom)
[![](https://github.com/YarikSOffice/venom/workflows/Build/badge.svg)](https://github.com/YarikSOffice/venom/actions?query=workflow%3ABuild)
[![](https://img.shields.io/badge/code%20style-%E2%9D%A4-FF4081.svg)](https://ktlint.github.io/)
[![](https://androidweekly.net/issues/issue-411/badge.svg)](https://androidweekly.net/issues/issue-411/)

<img src="preview/header.png" width="300">

**Venom** is a lightweight tool that simplifies testing of the process death scenario for your android application. 

<details>
    <summary>🎞️ Click to show the preview…</summary>
    <img src="preview/preview.gif" width="300">
</details>

## Why Venom?

The Android Run Time aggressively manages its resources and occasionally terminates background applications while the user is away interacting with other apps. In such a case, all the activities are destroyed along with application scope objects and background tasks. 

When the user relaunches the app, the top stack activity is restored from the saved instance state. Knowing this, your applications should always present a consistent interface and be tested against the process death scenario. 

Venom makes it possible to kill the app process from the notification drawer making the testing easier and more straightforward versus the traditional ways like setting the background processes limit in Developer Options or using the stop process button in Android Studio, especially for a QA team.

## Setup

The setup is pretty simple:

1. Initialize the library in Application.onCreate:

```kotlin
val venom = Venom.createInstance(this)
venom.initialize()
```

2. Call `start`/`stop` whenever you need:

```kotlin
venom.start()
// or
venom.stop()
```
See the sample app for an example.

## Customize the notification

Use the builder to customize the notification:
```kotlin
val notification = NotificationConfig.Builder(this)
    .buttonCancel(R.string.venom_notification_button_cancel_override)
    .buttonKill(getString(R.string.venom_notification_button_kill_override))
    .build()   
venom.initialize(notification)
```

## Notification Permission

Android 13 has introduced a new runtime permission [`POST_NOTIFICATIONS`](https://developer.android.com/reference/android/Manifest.permission#POST_NOTIFICATIONS) which is required to display any kind of notifications including ones for foreground services. Note that this permission is required for Venom to function properly.

Venom **doesn't** manage the notification permission on your behalf to keep the library flexible. Please, see an example of handling that in the sample app.


## Download

```groovy
repositories {
    maven { url 'https://jitpack.io' }
}

dependencies {
    debugImplementation "com.github.YarikSOffice.Venom:venom:0.6.0"
    releaseImplementation "com.github.YarikSOffice.Venom:venom-no-op:0.6.0"
}
```

## License

```
The MIT License (MIT)

Copyright 2020 Yaroslav Berezanskyi

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```
