# ProGuard consumer rules for core:data
# Keep @Serializable classes for Ktor JSON serialization in release builds
-keep class com.template.core.data.networking.AccessTokenRequest { *; }
-keep class com.template.core.data.networking.AccessTokenResponse { *; }
-keep class com.template.core.data.auth.AuthInfoSerializable { *; }
-keepclassmembers class com.template.core.data.networking.AccessTokenRequest { *; }
-keepclassmembers class com.template.core.data.networking.AccessTokenResponse { *; }
-keepclassmembers class com.template.core.data.auth.AuthInfoSerializable { *; }

# ErrorProne annotations referenced by Google Tink (AndroidX Security Crypto)
-dontwarn com.google.errorprone.annotations.*
