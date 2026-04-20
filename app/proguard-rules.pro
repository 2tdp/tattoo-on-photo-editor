# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.

# ─── Debug stack traces ──────────────────────────────────────────────────────
# Preserve source file names and line numbers for readable crash stack traces
-keepattributes SourceFile,LineNumberTable
-renamesourcefileattribute SourceFile

# ─── Kotlin metadata ──────────────────────────────────────────────────────────
-keepattributes Signature
-keepattributes RuntimeVisibleAnnotations
-keepattributes AnnotationDefault

# ─── Gson serialization (CRITICAL) ───────────────────────────────────────────
# Without these rules, R8 obfuscates field names → Gson cannot deserialize
# project data → ALL saved projects/tattoos/text will be corrupt in release build
-dontwarn sun.misc.**
-keep class com.google.gson.** { *; }
-keep interface com.google.gson.** { *; }

# Keep all model classes used by Gson for JSON serialization/deserialization
-keep class com.tattoo.tattoomaker.on.myphoto.model.** { *; }
-keepclassmembers class com.tattoo.tattoomaker.on.myphoto.model.** {
    <fields>;
    <init>(...);
}

# ─── Native CGE library (Wysaid) ─────────────────────────────────────────────
-keep class org.wysaid.** { *; }
-keepclassmembers class org.wysaid.** {
    *;
}
-keepclassmembers class org.wysaid.nativePort.CGENativeLibrary {
    static <fields>;
    native <methods>;
}

# ─── StickerView library (Java reflection) ───────────────────────────────────
-keep class com.tattoo.tattoomaker.on.myphoto.viewcustom.stickerviewcustom.stickerview.** { *; }
-keepclassmembers class com.tattoo.tattoomaker.on.myphoto.viewcustom.stickerviewcustom.stickerview.** {
    <fields>;
    <init>(...);
}

# ─── Hilt dependency injection ────────────────────────────────────────────────
-keep class dagger.hilt.** { *; }
-keep @dagger.hilt.android.lifecycle.HiltViewModel class * extends androidx.lifecycle.ViewModel
-keep @dagger.hilt.android.AndroidEntryPoint class *
-keep interface javax.inject.** { *; }
-keep class javax.inject.** { *; }

# ─── Room database ────────────────────────────────────────────────────────────
-keep class * extends androidx.room.RoomDatabase
-keep @androidx.room.Entity class * { *; }
-keep @androidx.room.Dao interface * { *; }

# ─── Callback interfaces (used via anonymous class, must keep) ────────────────
-keep interface com.tattoo.tattoomaker.on.myphoto.callback.** { *; }

# ─── DataLocalManager & Constant ──────────────────────────────────────────────
-keep class com.tattoo.tattoomaker.on.myphoto.sharepref.DataLocalManager { *; }
-keep class com.tattoo.tattoomaker.on.myphoto.helper.Constant { *; }

# ─── Glide ───────────────────────────────────────────────────────────────────
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep class * extends com.bumptech.glide.module.AppGlideModule
-keep public enum com.bumptech.glide.load.ImageHeaderParser$** { *; }

# ─── Lottie ──────────────────────────────────────────────────────────────────
-dontwarn com.airbnb.lottie.**
-keep class com.airbnb.lottie.** { *; }

# ─── Suppress common warnings from third-party libs ──────────────────────────
-dontwarn kotlin.Unit
-dontwarn retrofit2.**
-dontwarn okio.**
