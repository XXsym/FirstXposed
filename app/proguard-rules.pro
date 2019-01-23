
-dontwarn com.handsomexi.**
-keep class com.handsomexi.firstxposed.bean.* { *; }
-keep class com.handsomexi.firstxposed.xposed.XPHook { *; }
-keep class com.handsomexi.firstxposed.activity.MainActivity { *; }
#图标
-keep class com.github.mikephil.charting.** { *; }
# # -------------------------------------------
# # ############### volley混淆 ###############
# # -------------------------------------------
-keep class com.android.volley.** {*;}
-keep class com.android.volley.toolbox.** {*;}
-keep class com.android.volley.Response$* { *; }
-keep class com.android.volley.Request$* { *; }
-keep class com.android.volley.RequestQueue$* { *; }
-keep class com.android.volley.toolbox.HurlStack$* { *; }
-keep class com.android.volley.toolbox.ImageLoader$* { *; }

# # -------------------------------------------
# # ############### 系统api等常规混淆 ###############
# # -------------------------------------------

-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application

##---------------Begin: proguard configuration for Gson  ----------
# Gson uses generic type information stored in a class file when working with fields. Proguard
# removes such information by default, so configure it to keep all of it.
-keepattributes Signature

# For using GSON @Expose annotation
-keepattributes *Annotation*

# Gson specific classes
-keep class sun.misc.Unsafe { *; }
#-keep class com.google.gson.stream.** { *; }

# Application classes that will be serialized/deserialized over Gson
-keep class com.google.gson.examples.android.model.** { *; }

##---------------End: proguard configuration for Gson  ----------

-ignorewarnings
-obfuscationdictionary dict.txt
-classobfuscationdictionary dict.txt
-packageobfuscationdictionary dict.txt