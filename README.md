# Griddlers
A Griddlers game Java application

For info about the Griddlers game: https://en.wikipedia.org/wiki/Nonogram

(This project was my Object Oriented Programming course project)

### The setup window
After launching the application you are met with the setup window.
In the Game Setup tab you can choose either to set up a new game or load a previously saved one (when loading a game you can choose whether to use the current UI preferences or
the ones that were used when the game was saved).
When setting up a new game you can choose to either generate a random grid or generate the grid based on an image file.
In addition, you will need to choose the grid size (10 means 10 on 10 squares) and the number of clues (more on that later).
In the Preferences tab you can customize some aspects of the UI of the game window.

### The game window
How to play the game:

 The cells in the grid have to be colored or left blank according to the numbers given at the side of the grid to reveal a hidden picture.
 The numbers measure how many unbroken lines of filled-in squares there are in any given row or column.
 For example, a clue of '4 8 3' would mean there are sets of four, eight, and three filled squares, in that order, with at least one blank square between successive groups.

User Interface explanation:
- Left clicking on a square in the grid will mark it as 'filled-in'. Performing this once again will 'de-fill' the square.
- Right clicking on a square in the grid will mark it as 'leave blank'. Performing this once again will unmark the square.
- Middle clicking on a square in the grid will set it blinking, enabling single-square functions (such as the 'clue' function).
- Middle clicking once more will mark the square as 'not sure'.
 Performing this sequence (middle clicking twice) once more will remove the 'not sure' mark.
- The 'Clue' button reveals the correct state of a square. Choose a square by middle clicking it (the square should blink).
 Note that the number of clues may be limited.
- The 'Check' button is used for submitting the solved puzzle. If the submitted solution is not the right one, the incorrect squares will be colored.
 Note that this will end the game.
- The 'Save' button saves the game (along with its settings) to a file.
