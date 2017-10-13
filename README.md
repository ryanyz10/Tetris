# Tetris
Tetris game and bot for playing Tetris

## Implementing
* Piece Abstraction
* Board Abstraction
* Rotation that is CCW
* Use SRS for board (Most confusing part)

## Bugs/Fixing/Todo
### Todo
* Implement dropHeight method
### Fixes
* Use the Sanity checks provided
* The I needs to be lifted up a y value at creation to fix weird rotation Behavior
### Major Bugs
* Witnessed rotation on final drop eliminated the block of another value (Could be wallkicks)
* Moving left/right when piece is at bottom leads to index out of bounds

### Resources
* [PDF](https://d1b10bmlvqabco.cloudfront.net/attach/j6uasov5t8x37n/idlsqw8r5ys6zf/j86fig7u1oib/prog4.pdf)
* [Model Game](http://tetris.com/play-tetris/)
* [Genetic Algorithm](https://codemyroad.wordpress.com/2013/04/14/tetris-ai-the-near-perfect-player/)

### Driving Table

| Driver | Hours | Date | Individual | Comments |
|:---:|:---:|:---:|:---:|:---:|
|Ryan|1.5|10/06/2017|No|Rotation Code|
|Matthew|1.5|10/07/2017|Yes|Rotation Testing|
|Ryan|1|10/07/2017|Yes|Piece Calculations|
|Ryan|1|10/08/2017|Yes|Piece Movements|
|Ryan|1|10/09/2017|No|Finishing Touches for Early Submission|
|Ryan|1.5|10/11/2017|Yes|Bug fixes and refactoring|
|Ryan|2|10/12/2017|Yes|More bug fixes, refactoring and genetic algorithm|
|Matthew|2.5|10/12/17|Yes|Wall Kicks and piece state reflection|
|Matthew|4|10/12/17|Yes|Board Testing|
|Ryan|5|10/12/17|Yes|Genetic Algorithm|
|Matthew|2|10/13/17|Yes|Testing Completion|
|Ryan|4|10/13/17|Yes|More work on our trashy GA|

### Fixing Last Lab - Our Issues
#### Negatives
* Code failed some cases
* More commenting (particularly on utility methods)
* Report may have been a little too terse
* More explicit discussion of edge-cases
* Degroup Test cases
#### Positives
* Good test coverage of positive/negative and unit/integration tests
* He was impressed by brevity (shortest report of all)

### Assumptions
* We assumed pieces would be given in a certain way - every time. (The way being how they are written in JTetris) (This probably our biggest and most dangerous assumption)
* We assumed their would only be 7 different pieces, and thus 3 different classifications for them (Square, "I", Other)
* We assumed a board would have to be at least 4x4 (to fit the "I") piece, if not we throw an error
* For the clockwise function we assume that the object will have 4 rotations (excluding Square) so we can call counterclockwise 3 times


