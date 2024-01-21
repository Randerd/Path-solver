# Pathcreating algorithm
Aka Linko (by 1PixelStudio) solver  
Given an 𝑛 × 𝑛 board, a start & end location, and a series of tiles, utilizes a backtracking algorithm to find a possible path

## TODO
- [ ] Algorithm Improvments
    - [x] Duplicate tile check - Don't backtrack to a tile type that has already been tested
    - [ ] "Traps" - If placing a tile creates spaces that cannot be be used
    - [ ] "Must" tiles - Tiles that must be placed at certain positions 
- [ ] Transfer to Java
- [ ] Design Convolution Neural Network (CNN) for tile placement
    - [ ] Create data set of possible tiles
    - [ ] Train CNN 
- [ ] Implement UI
    - [ ] Let user change board size
    - [ ] Use CNN to let users draw in tiles
    - [ ] Let users attempt to solve their puzzle 
    - [ ] Button to run algorithm to find solution to user puzzle (if solution exists)
