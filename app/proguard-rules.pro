# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.

# Preserve line numbers and source file names for crash reporting in Google Play Console
-keepattributes SourceFile,LineNumberTable
-renamesourcefileattribute SourceFile

# Keep data models
-keep class com.example.model.** { *; }
-keepclassmembers class com.example.model.** { *; }

# Keep native methods if any
-keepclasseswithmembers class * {
    native <methods>;
}

