# ProGuard consumer rules for home:data
# Keep @Serializable DTO classes for Ktor JSON serialization in release builds
-keep class com.template.home.data.dto.ProductDto { *; }
-keep class com.template.home.data.dto.ProductsResponse { *; }
-keepclassmembers class com.template.home.data.dto.ProductDto { *; }
-keepclassmembers class com.template.home.data.dto.ProductsResponse { *; }
