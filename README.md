# Tetris
Tetris game and bot for playing Tetris

## Implementing
* Piece Abstraction
* Board Abstraction
* Rotation that is CCW
* Use SRS for board (Most confusing part)

## Bugs/Fixing
### Fixes
* Use the Sanity checks provided
* The L needs to be lifted up a y value at creation to fix weird rotation Behavior
### Major Bugs
* Witnessed rotation on final drop eliminated the block of another value (Could be wallkicks)

### Resources
* [PDF](https://d1b10bmlvqabco.cloudfront.net/attach/j6uasov5t8x37n/idlsqw8r5ys6zf/j86fig7u1oib/prog4.pdf)
* [Model Game](http://tetris.com/play-tetris/)

### Driving Table

| Driver | Hours | Date | Individual | Comments |
|:---:|:---:|:---:|:---:|:---:|
|Ryan|1.5|10/06/2017|No|Rotation Code|
|Matthew|1.5|10/07/2017|Yes|Rotation Testing|
|Ryan|1|10/07/2017|Yes|Piece Calculations|
|Ryan|1|10/08/2017|Yes|Piece Movements|
|Ryan|1|10/09/2017|No|Finishing Touches for Early Submission|

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
