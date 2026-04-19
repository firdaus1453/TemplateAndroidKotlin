# ProGuard consumer rules for auth:data
# Keep @Serializable DTO classes for Ktor JSON serialization in release builds
-keep class com.template.auth.data.LoginRequest { *; }
-keep class com.template.auth.data.LoginResponse { *; }
-keepclassmembers class com.template.auth.data.LoginRequest { *; }
-keepclassmembers class com.template.auth.data.LoginResponse { *; }
