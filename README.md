# Minesweeper AI

## About

This project emulates the classic Minesweeper game and display's a custom algorithm used to visualise a game solve in real time (1 step per game update).

Game settings can be changed in `src/danielbatchford/minesweeper/Settings.java`

This project was developed in Java using [Processing](https://processing.org/) for GUI rendering.

## Sample Run
![](https://github.com/danielbatchford/MineSweeperAI/blob/master/sample.gif)

## AI implementation
Currently, the AI obeys the following logic:
- Select a random uncovered tile:
    - If the number of uncovered neighbors equals the value of the selected tile:
        - Uncover the first neighbor in the neighbor list
    - If the number of covered neighbors + flagged neighbors equals the value of the selected tile:
        - Flag the first neighbor in the neighbor list

#### Controls
- Use `P` to pause the game
- Use `R` to restart the game 
- Use `M` to toggle the mine display

## Contributing
Feel free to submit a pull request to contribute to this project.

## Credits
[Processing for Java](https://processing.org/) for GUI rendering