# Braintree Android SDK Rules
## PayPal SDK (does not include card.io)
## https://github.com/paypal/PayPal-Android-SDK/blob/master/paypal-proguard.cnf
-dontwarn com.google.android.gms.**
-dontwarn android.support.v4.**

# guillaume
-dontwarn com.braintreepayments.api.DataCollector.**
-dontwarn com.devicecollector.**
