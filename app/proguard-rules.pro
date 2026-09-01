# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

# Room entities & Dao
-keep class androidx.room.** { *; }
-keep class * extends androidx.room.RoomDatabase { *; }
# Keep all @Entity classes
-keep @androidx.room.Entity class * { *; }
# Keep Dao methods
-keepclassmembers class * {
    @androidx.room.* <methods>;
}

-keep class com.google.mlkit.** { *; }
-dontwarn com.google.mlkit.**