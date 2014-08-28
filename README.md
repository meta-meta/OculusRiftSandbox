Oculus Rift + Processing.org
============================

gradlew idea to create IntelliJ Idea project files

This build.gradle is a work in progress.

git clone https://github.com/processing/processing.git
git checkout processing-0227-2.2.1

then add processing/core as a module dependency 
comment out PJOGL.java line 629 so the Rift SDK can render


SpaceNavigator  (to be moved to a separate project)
--------------
this is only relevant if you have a 3DConnexion Space Navigator.
add procontroll.jar as a library dependency
procontroll requires that jinput-dx8.dll be on the system path