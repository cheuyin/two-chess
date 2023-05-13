
# Personal Project: TwoChess

_TwoChess_ allows two players to play games of **chess** together and track their respective scores. It is intended for anyone who is interested in the game of chess and wants to play against another human player on the same computer. 

## Why I chose this project
- I think implementing a complete chess game will be a difficult but very fun project
- I can imagine others and myself playing with TwoChess for fun
- I have many ideas in mind on how I can add extra features and improvements in the future

## User stories
- As a user, I want to be able to start a new game of chess with all the black and white pieces in their correct places
- As a user, I want to be able to play a complete game of chess against another player on the same board where illegal moves are prohibited and the game stops when there is a checkmate
- As a user, I want to be able to reset the current match and begin a new game
- As a user, I want to be able to add an arbitrary number of moves to a collection of moves so I can track the progression of the game 
- As a user, I want to know when it is my turn to move and the legal moves that are available to me
- As a user, when I select the quit option from the application menu, I want to be reminded to save the current state of the chess game to file and have the option to do so or not.
- As a user, when I start the application, I want to be given the option to load my saved chess game from file or not.

## Instructions for Grader (Phase 3)
- You can generate the first required action related to adding Xs to a Y by selecting an option in the dropdown menu allowing you to filter the move list 
- You can generate the second required action related to adding Xs to a Y by clicking on a move in the list of moves (in algebraic notation) that updates the label underneath to show more detailed information about the move
- You can locate my visual component by looking at the images of chess pieces on the chess board
- You can save the state of my application by pressing the red button that closed a window. You will be prompted with a window asking if you want to save the game
- You can reload the state of my application by starting the program. A pop-up menu will give you the option to load a previous game

## Phase 4: Task 2
```
A piece was moved: new move object added to the list of moves
Piece moved from e2 to e4
Created filtered list of only Black moves
Create filtered list of only White moves
A piece was moved: new move object added to the list of moves
Piece moved from f7 to f5
Created filtered list of only Black moves
Create filtered list of only White moves
A piece was moved: new move object added to the list of moves
Piece moved from e4 to f5
Created filtered list of only Black moves
Create filtered list of only White moves
```

## Phase 4: Task 3
If I had more time to work on this project, I would refactor the Board class. Right now, this class has multiple responsibilities on top of just representing a chess board. It also acts as the game controller by moving pieces and verifying whether a move is legal, in addition to checking whether a game is over. This violates the single responsibility principle and decreases the cohesion of the program. So, I would abstract out the game logic functionality from the Board class and make it its own class. Ideally, that way, the Board class does not have to know how the rules of chess actually work.

## References
- JsonReader.java, JsonWriter.java, and associated JSON data persistence-related classes and methods were modelled after the sample project https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo
- Chess pieces images: https://github.com/lichess-org/lila/tree/master/public/piece/cburnett
  - Author: https://en.wikipedia.org/wiki/User:Cburnett