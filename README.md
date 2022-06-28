# fitter

![Untitled](https://github.com/Mene-hub/fitter/blob/main/app/src/main/res/mipmap-hdpi/ic_fitter_logo_foreground.png)

Fitter is an App developed by two university student: [@Code-Dani](https://github.com/Code-Dani), [@Mene-Hub](https://github.com/Mene-hub).

Fitter is an application for managing gym cards, but not only. It also presents the option to search for other users registered to the application and see their profile and if the latter is not private it will be possible to peek at the various tabs that the user presents.

The user can choose each time he completes a card to record the weights used in order to keep track of them.

Using this last feature we can create a monthly recap of the athlete's performance. The latter will be private and visible only by the athlete himself. The monthly recaps will be saved in the database along with the other basic information of the athlete and the cards he owns. The athlete will also be able to view the recaps passed up to 6 months later.

In this application it will be possible to register only through Facebook or Google for now as the login / registration by email of Firebase is not secure and has some important flaws.

This application implements [Firebase](https://firebase.google.com/) services such as Auth and Realtime database.

It also utilises [wger](https://github.com/wger-project/wger) REST API when choosing an exercise.
