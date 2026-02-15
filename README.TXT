This is a school project for our "IMP-Class". It was created using blueJ. The project is a game, which we weren't allowed to make in VS-Code or greenfoot, so we HAD TO USE FUCKING BLUEJ. I don't know what the game is about

To use it, dowload the zip-file, by clicking on "code" (top left corner) and selecting "dowload zip-file"
Then, import the zip-file in blueJ. To start the game --- [...]

Authors: Aaron, Leo, Darragh, Paul

Branch V2 tries to restructure the existing source code in a "better" source code structure:
- use folders to group objects
- less public variables
- clear seperation of concerns
- use events to decouble view from game logic
- use english variable and method names

Authors adevel

Idea is not to touch/change the existing filesfrom main. 
Thus, changes in the main branch are easily incorporated into V2 for easy reference.

Instead complete new source code files base on the currently existing files are created.

First major improvement is to abendon BlueJ. 

LauncherV1.java allows to start the main version inside Visual Studio Code.

notes/VisualStudioSetup.txt contains infos about the Visual Studio Code setup and JDK version used on windows.
