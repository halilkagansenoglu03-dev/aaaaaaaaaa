# Empty Quit (Fabric 1.21.11, client-side)

Disconnects you from a multiplayer server if no other entity (player, mob,
animal, item...) has been loaded around you for 10 seconds in a row.
It never triggers in singleplayer. The timer restarts when you join a server.

## Build the .jar

You need JDK 21, Gradle 9.x and internet access.

1. Open a terminal in this folder.
2. Run:  gradle build
3. Take `build/libs/emptyquit-1.0.0.jar` (ignore any -sources or -dev jar)
   and put it in your `mods` folder next to Fabric API for 1.21.11.

Easier alternative: copy `src/` and the `.gradle` / `.properties` files into
a fresh copy of the official fabric-example-mod (1.21.11 branch), which
already includes the Gradle wrapper, then run `gradlew build` (Windows) or
`./gradlew build` (Mac/Linux).

## Change the time

Edit `SECONDS_ALONE` in
`src/main/java/com/emptyquit/EmptyQuitClient.java`, then rebuild.
