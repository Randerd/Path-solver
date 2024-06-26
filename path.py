from tiles import *
from multipledispatch import dispatch
# from tabulate import tabulate

class PathFinder():
    def __init__(self, size = 5):
        self.tileList = []
        self.tiles = {
            'North': [],
            'NorthEast': [],
            'East': [],
            'SouthEast': [],
            'South': [],
            'SouthWest': [],
            'West': [],
            'NorthWest': [],
        }
        self.start = None
        self.end = None

        self.set_size(size)

    def set_size(self, size):
        self.size = size
        self.board = [[None for _ in range(size)] for _ in range(size)]

    @dispatch()
    def find_path(self):
        if self.find_path(self.start, 0):
            print("Path Found")
        else:
            print('No Path Found')

        print()

    @dispatch(object, int)
    def find_path(self, current, num):
        num+=1

        #Base case
        # if self.tiles_left() == 0:
        #     print('**', current)
        #     if current.can_connect(self.end, self.board):
        #         return True
            
        direction = current.get_open_connection()
        connecting_direction = pairs[direction]
        new_position = add_positions(current.position, directions[direction])
        possible_tiles = self.tiles[connecting_direction][:]
        used = []
        for tile in possible_tiles:
            if tile.tile_type in used:
                continue
            used.append(tile.tile_type)
            
            tile.set_position(new_position)
            y,x = next_connection_location(current, tile)
            
            # print(num if num>9 else '0'+str(num), self.print_path(),'-->', tile.name, new_position, (y,x), inbounds((x,y), size = self.size))
            if inbounds((x,y), size = self.size) and self.board[y][x] == None:
                current.connect(direction, tile)
                self.use_tile(tile)
                self.place_tile(tile)

                if self.find_path(tile, num):
                    return True
                
                current.disconnect(direction, tile)
                self.unuse_tile(tile)
                self.unplace_tile(tile)
            #Base Case
            elif self.tiles_left() == 1 and inbounds((x,y), size = self.size) and self.board[y][x].__class__.__name__ == 'End':
                current.connect(direction, tile)
                self.place_tile(tile)
                tile.connect(end)
                return True
            
            tile.remove_position()
        return False
            
    def add_tile(self, tile):
        self.tileList.append(tile)
        self.unuse_tile(tile)
    
    def remove_tile(self, tile):
        self.tileList.remove(tile)
        self.use_tile(tile)
    
    def add_blank(self, y,x):
        self.board[y][x] = "BLANK"
        
    def unuse_tile(self, tile):
        direction1, direction2 = list(tile.connections)
        self.tiles[direction1].append(tile) 
        self.tiles[direction2].append(tile)
        # self.tiles[direction1].insert(0,tile) #Turns out using O(n) operations really slow things down
        # self.tiles[direction2].insert(0,tile)
        # self.unplace_tile(tile)

    def use_tile(self, tile):
        direction1, direction2 = list(tile.connections)
        self.tiles[direction1].remove(tile)
        self.tiles[direction2].remove(tile)
        # self.place_tile(tile)

    def set_start(self, start):
        self.start = start
        self.place_tile(start)
    
    def set_end(self, end):
        self.end = end
        self.place_tile(end)

    def place_tile(self, tile):
        y,x= tile.position
        self.board[y][x] = tile

    def unplace_tile(self, tile):
        y,x= tile.position
        self.board[y][x] = None

    def tiles_left(self):
        count = 0
        for _, value in self.tiles.items():
            count += len(value)
        return count/2

    def check_trap(self, cur_tile, y,x):
        #TODO
        checks = [(1,0),(1,1),(0,1),(-1,1),(-1,0),(-1,-1),(0,-1),(1,-1)]
        for Y,X in checks:
            new_y, new_x = y + Y, x + X
            print(f'\n-={new_y,new_x}=-')
            if inbounds((new_y,new_x), self.size):
                for Y2,X2 in checks:
                    count = 0
                    new_y2, new_x2 = new_y + Y2, new_x + X2
                    if inbounds((new_y2, new_x2), self.size):
                        if self.board[new_y2][new_x2] == None:
                            count+=1
                        
                        if count == 2:
                            return True
                        print(new_y2,new_x2)
                    

    @dispatch()
    def print_path(self):
        return self.print_path(self.start, self.start.pointing)

    @dispatch(object, str)
    def print_path(self,current, direction):
        if current == None:
            return ''
        
        string = f'{current.name} -> '
        directions = list(current.connections)
        if len(directions)>1:
            directions.remove(direction)
        next_direction = directions[0]
 
        return string + self.print_path(current.connections[next_direction], pairs[next_direction])

    def print_board(self):
        for row in self.board:
            lst = ['','','']
            for tile in row:
                if tile == None or tile == "BLANK":
                    lst[0] += '   '
                    lst[1] += '   '
                    lst[2] += '   '
                else:
                    lst[0] += tile.shape[0]
                    lst[1] += tile.shape[1]
                    lst[2] += tile.shape[2]
                lst[0] += ' '
                lst[1] += ' '
                lst[2] += ' '
            
            for i in lst:
                print(i)
            # print('-'*self.size*4)
            print()
            
    def __str__(self) -> str:
        string = f'{self.start}\n'
        for tile in self.tileList:
            string += f'{tile}\n'
        string += f'{self.end}'
        return string


if __name__ == '__main__':
    import time
    level = int(input('What level would you like to solve: '))
    pf = PathFinder()
    if level == 212:
        size = 5
        start = Start('NorthEast', position=(2,1))
        end = End('SouthWest', position=(1,3))

        tile1 = Ninety(name = 'Ninety1', ori = 6)
        tile2 = Ninety(name = 'Ninety2', ori = 2)
        tile3 = Ninety(name = 'Ninety3', ori = 4)
        tile4 = Ninety(name = 'Ninety4', ori = 0)
        tile5 = Ninety(name = 'Ninety5', ori = 2)

        tile6 = Fourty_five(name = 'FourtyFive1', ori = 0)
        tile7 = Fourty_five(name = 'FourtyFive2', ori = 1)
        tile8 = Fourty_five(name = 'FourtyFive3', ori = 5)
        tile9 = Fourty_five(name = 'FourtyFive4', ori = 4)

        tile10 = Line(name = 'HLine1', ori = 2)
        tile11 = Line(name = 'HLine2', ori = 2)
        tile12 = Line(name = 'HLine3', ori = 2)
        tile13 = Line(name = 'HLine4', ori = 2)

        tile14 = Line(name = 'VLine1', ori = 0) 
        tile15 = Line(name = 'VLine2', ori = 0)
        tile16 = Line(name = 'VLine3', ori = 0)
        tile17= Line(name = 'VLine4', ori = 0)

        tile18 = Line(name = '/Line1', ori = 1) 
        tile19 = Line(name = '/line2', ori = 1)
        tile20 = One_thirty_five(name = 'OneThirtyFive1', ori = 5)
        tile21 = One_thirty_five(name = 'OneThirtyFive2', ori = 2)
        tile22 = One_thirty_five(name = 'OneThirtyFive3', ori = 6)
        tile23 = One_thirty_five(name = 'OneThirtyFive4', ori = 6)

        pf.set_size(size)
        pf.set_start(start)
        pf.set_end(end)
        pf.add_tile(tile1)
        pf.add_tile(tile2)
        pf.add_tile(tile3)
        pf.add_tile(tile4)
        pf.add_tile(tile5)
        pf.add_tile(tile6)
        pf.add_tile(tile7)
        pf.add_tile(tile8)
        pf.add_tile(tile9)
        pf.add_tile(tile10)
        pf.add_tile(tile11)
        pf.add_tile(tile12)
        pf.add_tile(tile13)
        pf.add_tile(tile14)
        pf.add_tile(tile15)
        pf.add_tile(tile16)
        pf.add_tile(tile17)
        pf.add_tile(tile18)
        pf.add_tile(tile19)
        pf.add_tile(tile20)
        pf.add_tile(tile21)
        pf.add_tile(tile22)
        pf.add_tile(tile23)

    if level == 0:
        size = 4
        start = Start('East', position=(0,0))
        end = End('West', position = (3,3))

        tile1 = Ninety(name = 'tile1', ori = 4)
        tile2 = Ninety(name = 'tile2', ori = 0)
        tile3 = Ninety(name = 'tile3', ori = 6)
        tile4 = Ninety(name = 'tile4', ori = 2)
        tile5 = Ninety(name = 'tile5', ori = 4)
        tile6 = Line(name = 'tile6', ori = 0)
        tile7 = Ninety(name = 'tile7', ori = 6)
        tile8 = Line(name = 'tile8', ori = 2)
        tile9 = One_thirty_five(name = 'tile9', ori = 7)
        tile10 = Fourty_five(name = 'tile10', ori = 3)
        tile11 = Line(name = 'tile11', ori = 0)
        tile12 = Ninety(name = 'tile12', ori = 0)
        tile13 = Line(name = 'tile13', ori = 2)
        tile14 = Line(name = 'tile14', ori = 2)

        pf.set_size(size)
        pf.set_start(start)
        pf.set_end(end)
        pf.add_tile(tile8)
        pf.add_tile(tile2)
        pf.add_tile(tile3)
        pf.add_tile(tile13)
        pf.add_tile(tile5)
        pf.add_tile(tile12)
        pf.add_tile(tile11)
        pf.add_tile(tile4)
        pf.add_tile(tile14)
        pf.add_tile(tile7)
        pf.add_tile(tile10)
        pf.add_tile(tile6)
        pf.add_tile(tile9)
        pf.add_tile(tile1)

    if level == 219:
        size = 6
        start = Start('SouthEast', position=(0,2))
        end = End('West', position = (0,4))

        tile1 = Ninety(name = 'tile1', ori = 2)
        tile2 = Ninety(name = 'tile2', ori = 2)
        tile3 = Ninety(name = 'tile3', ori = 6)
        tile4 = Ninety(name = 'tile4', ori = 6)
        tile5 = Ninety(name = 'tile5', ori = 6)
        tile6 = Ninety(name = 'tile6', ori = 0)

        tile7 = Ninety(name = 'tile7', ori = 7)
        tile8 = Ninety(name = 'tile8', ori = 5)
        tile9 = Ninety(name = 'tile9', ori = 3)

        tile10 = Fourty_five(name = 'tile10', ori = 0)
        tile11 = Fourty_five(name = 'tile11', ori = 7)
        tile12 = Fourty_five(name = 'tile12', ori = 7)
        tile13 = Fourty_five(name = 'tile13', ori = 2)
        tile14 = Fourty_five(name = 'tile14', ori = 2)
        tile15 = Fourty_five(name = 'tile15', ori = 3)
        tile16 = Fourty_five(name = 'tile16', ori = 3)
        tile17 = Fourty_five(name = 'tile17', ori = 4)
        tile18 = Fourty_five(name = 'tile18', ori = 4)

        tile19 = One_thirty_five(name = 'tile19', ori = 3)
        tile20 = One_thirty_five(name = 'tile20', ori = 4)
        tile21 = One_thirty_five(name = 'tile21', ori = 6)
        tile22 = One_thirty_five(name = 'tile22', ori = 6)
        tile23 = One_thirty_five(name = 'tile23', ori = 7)
        tile24 = One_thirty_five(name = 'tile24', ori = 7)

        tile25 = Line(name = 'tile25', ori = 0)
        tile26 = Line(name = 'tile26', ori = 0)
        tile27 = Line(name = 'tile27', ori = 0)
        tile28 = Line(name = 'tile28', ori = 0)
        tile29 = Line(name = 'tile29', ori = 2)
        tile30 = Line(name = 'tile30', ori = 2)


        pf.set_size(size)
        pf.set_start(start)
        pf.set_end(end)

        pf.add_blank(0,0)
        pf.add_blank(0,1)
        pf.add_blank(1,0)
        pf.add_blank(1,1)
        pf.add_tile(tile13)
        pf.add_tile(tile2)
        pf.add_tile(tile3)
        pf.add_tile(tile4)
        pf.add_tile(tile6)
        pf.add_tile(tile1)
        pf.add_tile(tile7)
        pf.add_tile(tile8)
        pf.add_tile(tile9)
        pf.add_tile(tile10)
        pf.add_tile(tile15)
        pf.add_tile(tile11)
        pf.add_tile(tile5)
        pf.add_tile(tile18)
        pf.add_tile(tile19)
        pf.add_tile(tile12)
        pf.add_tile(tile14)
        pf.add_tile(tile16)
        pf.add_tile(tile26)
        pf.add_tile(tile17)
        pf.add_tile(tile30)
        pf.add_tile(tile20)
        pf.add_tile(tile22)
        pf.add_tile(tile23)
        pf.add_tile(tile24)
        pf.add_tile(tile25)
        pf.add_tile(tile21)
        pf.add_tile(tile27)
        pf.add_tile(tile28)
        pf.add_tile(tile29)

    
    start_time = time.time()
    pf.find_path()
    end_time = time.time()
    pf.print_board()
    print(end_time - start_time)
