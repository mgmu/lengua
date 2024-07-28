# Lengua
Lengua is an Android app that aims to help memorize new terms with their
definition. 
For now, the user can add, edit and delete terms and their definitions.

## Installation

For now, the app is not hosted on any repository such as the Google Play Store
or others but can be installed by doing one of the following:
- downlaod the APK (see [Releases](https://github.com/mgmu/lengua/releases)),
put it in your device and launch it.
- clone this repo and download the app with `adb` or Android Studio directly to
your device.

I will try to publish it as soon as possible to a known app repository, as
Google Play Store.

## Future features

Ordered according to their priority (high to low):
 - export to text file option
 - Improve memorization with spaced repetition: the user could choose to
 periodically receive a certain number of notifications, each displaying a term.
 The goal is to remember the associated definition but if the user wishes to see
 it they could click on it and the term would be marked as not remembered, for a
 future batch of notifications. If the user manages to remember the definition,
 they could indicate it by swiping the notificaton away, which would make it
 less likely to be selected in future batches of notification.
 - Save dictionnaries for definition lookups.
 - Automatic definition fetch given a term.
