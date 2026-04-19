# ProGuard consumer rules for profile:data
# Keep @Serializable DTO classes for Ktor JSON serialization in release builds
-keep class com.template.profile.data.dto.UserProfileDto { *; }
-keepclassmembers class com.template.profile.data.dto.UserProfileDto { *; }
