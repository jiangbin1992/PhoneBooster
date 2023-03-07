
-keep class org.litepal.** {
    *;
}
-keep class * extends org.litepal.crud.DataSupport {
    *;
}
-keep class * extends org.litepal.crud.LitePalSupport {
    *;
}

-keepattributes *Annotation*
-keepattributes Signature
-keepattributes Exceptions
#Glide
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public class * extends com.bumptech.glide.module.AppGlideModule
-keep public enum com.bumptech.glide.load.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}

-keep class android.content.pm.** { *; }
-keep class com.gm.phonecleaner.password.model.** { *; }

