from multipledispatch import dispatch

pairs = {
    'North': 'South',
    'NorthEast': 'SouthWest',
    'East': 'West',
    'SouthEast': 'NorthWest',
    'South': 'North',
    'SouthWest': 'NorthEast',
    'West': 'East',
    'NorthWest': 'SouthEast'
}
directions = {
    'North': (-1,0),
    'NorthEast': (-1,1),
    'East': (0,1),
    'SouthEast': (1,1),
    'South': (1,0),
    'SouthWest': (1,-1),
    'West': (0,-1),
    'NorthWest': (-1,-1) 
}
class Tiles():
    def __init__(self, name = None, position = None, ori = 0):
        self.color = (255,255,255)
        self.connections = {}
        self.name = name
        self.position = position
        self.orientation = ori
        self.tile_type = self.__class__.__name__ + str(ori)

    @dispatch(object)
    def connect(self, other_tile):
        direction, connecting_direction = self.shared_open_connections(other_tile)
        self.connect(direction, other_tile)
        other_tile.connect(connecting_direction, self)

    @dispatch(str, object)
    def connect(self, direction, other_tile):
        self.connections[direction] = other_tile
        other_tile.connections[pairs[direction]] = self

    # @dispatch(object)
    # def disconnect(self, other_tile):
    #     direction, connecting_direction = self.shared_closed_connections(other_tile)
    #     self.disconnect(direction)
    #     other_tile.disconnect(connecting_direction)
    
    @dispatch(str)
    def disconnect(self, direction):
        self.connections[direction] = None
    
    @dispatch(str, object)
    def disconnect(self, direction, other_tile):
        self.connections[direction] = None
        other_tile.connections[pairs[direction]] = None

    # def can_connect(self, other_tile, board):
    #     y,x = next_connection_location(self, other_tile)

    #     return inbounds((y,x)) and (board[y][x] == None or board[y][x].__class__.__name__ == 'End')
    
    # def can_connect_end(self, end, board):
    #     if len(self.shared_open_connections(end)) == 0:
    #         return False
        
    #     direction, connecting_direction = self.shared_open_connections(end)
    #     open_connection = list(end.connections)
    #     if len(open_connection)>1:
    #         open_connection.remove(connecting_direction)
    #     open_connection = open_connection[0]
    #     y,x = add_positions(end.position, directions[open_connection])
    #     return inbounds((y,x)) and board[y][x].__class__.__name__ == 'End'

    def shared_open_connections(self, other_tile):
        for direction in self.connections:
            connecting_direction = pairs[direction]
            if connecting_direction in other_tile.connections and self.connections[direction] == None:
                return direction, connecting_direction
        return ()
    
    # def shared_closed_connections(self, other_tile):
    #     for direction in self.connections:
    #         connecting_direction = pairs[direction]
    #         if connecting_direction in other_tile.connections and self.connections[direction] != None:
    #             return direction, connecting_direction
    #     return ()
        # return list(set(self.connections) & set(other_tile.connections))

    # def num_connections(self):
    #     x = list(self.connections.values())
    #     return len(list(filter(lambda x: x != None, x)))
    
    # def is_connected(self):
    #     return self.num_connections() > 0
    
    def get_open_connection(self):
        for key, value in self.connections.items():
            if value is None:
                return key

    def set_position(self, new_position):
        self.position = new_position
    
    def remove_position(self):
        self.position = None
    
    def __str__(self):
        string = f'Type: {self.__class__.__name__}, Name: {self.name}, Orientation({self.orientation}): {list(self.connections)}, Position: {self.position}, Connections: '
        for key, value in self.connections.items():
            if value != None:
                string += f'{key} -> {value.name} | '
        return string

class Edge(Tiles):
    def __init__(self, name, pointing, position = None):
        super().__init__(name,position)
        self.color = (56,189,220)
        self.pointing = pointing
        self.connections[pointing] = None

    def update_shape(self):
        y,x = directions[self.pointing]
        thing = list(self.shape[1+y])
        thing[x+1] = '.'
        self.shape[y+1] = ''.join(thing)

    def __str__(self):
        conn = 'None' if self.connections[self.pointing] == None else self.connections[self.pointing].name
        return f'Type: Edge, Name: {self.name}, Connection: {self.pointing} -> {conn}'
      
class Start(Edge):
    def __init__(self, pointing, position = None):
        super().__init__('Start', pointing, position)
        self.shape = [
            'SSS',
            'SSS',
            'SSS'
        ]
        self.update_shape()
    
class End(Edge):
    def __init__(self, pointing, position = None):
        super().__init__('End', pointing, position)
        self.shape = [
            'EEE',
            'EEE',
            'EEE'
        ]
        self.update_shape()

class Line(Tiles):
    def __init__(self, name='Line', ori=0, position=None):
        super().__init__(name, position,ori)
        if ori == 0:
            self.connections['North'] = None
            self.connections['South'] = None
            self.shape = [
                ' | ',
                ' | ',
                ' | '
            ]
        elif ori == 1:
            self.connections['NorthEast'] = None
            self.connections['SouthWest'] = None
            self.shape = [
                '  /',
                ' / ',
                '/  '
            ]
        elif ori == 2:
            self.connections['West'] = None
            self.connections['East'] = None
            self.shape = [
                '   ',
                '───',
                '   '
            ]
        elif ori == 3:
            self.connections['SouthEast'] = None
            self.connections['NorthWest'] = None
            self.shape = [
                '\\  ',
                ' \\ ',
                '  \\'
            ]

class Fourty_five(Tiles):
    def __init__(self, name='Fourty-Five', ori = 0, position = None):
        super().__init__(name, position, ori)
        if ori == 0:
            self.connections['North'] = None
            self.connections['NorthEast'] = None
            self.shape = [
                ' |/',
                '   ',
                '   '
            ]
        elif ori == 1:
            self.connections['NorthEast'] = None
            self.connections['East'] = None
            self.shape = [
                '  /',
                '  ─',
                '   '
            ]
        elif ori == 2:
            self.connections['East'] = None
            self.connections['SouthEast'] = None
            self.shape = [
                '   ',
                '  ─',
                '  \\'
            ]
        elif ori == 3:
            self.connections['SouthEast'] = None
            self.connections['South'] = None
            self.shape = [
                '   ',
                '   ',
                ' |\\'
            ]
        elif ori == 4:
            self.connections['South'] = None
            self.connections['SouthWest'] = None
            self.shape = [
                '   ',
                '   ',
                '/| '
            ]
        elif ori == 5:
            self.connections['SouthWest'] = None
            self.connections['West'] = None
            self.shape = [
                '   ',
                '─  ',
                '/  ',
            ]
        elif ori == 6:
            self.connections['West'] = None
            self.connections['NorthWest'] = None
            self.shape = [
                '\\  ',
                '─  ',
                '   '
            ]
        elif ori == 7:
            self.connections['NorthWest'] = None
            self.connections['North'] = None
            self.shape = [
                '\\| ',
                '   ',
                '   '
            ]
    
class Ninety(Tiles):
    def __init__(self, name='Ninety', ori = 0, position = None):
        super().__init__(name, position, ori)
        if ori == 0:
            self.connections['North'] = None
            self.connections['East'] = None
            self.shape = [
                ' | ',
                ' ╰─', 
                '   '
            ]
        elif ori == 1:
            self.connections['NorthEast'] = None
            self.connections['SouthEast'] = None
            self.shape = [
                '  /',
                ' | ',
                '  \\'
            ]
        elif ori == 2:
            self.connections['East'] = None
            self.connections['South'] = None
            self.shape = [
                '   ',
                ' ╭─', 
                ' | '
            ]
        elif ori == 3:
            self.connections['SouthEast'] = None
            self.connections['SouthWest'] = None
            self.shape = [
                '   ',
                ' ︵ ', 
                '/ \\'
            ]
        elif ori == 4:
            self.connections['South'] = None
            self.connections['West'] = None
            self.shape = [
                '   ',
                '─╮ ',
                ' | '
            ]
        elif ori == 5:
            self.connections['SouthWest'] = None
            self.connections['NorthWest'] = None
            self.shape = [
                '\\  ',
                ' | ',
                '/  '
            ]
        elif ori == 6:
            self.connections['West'] = None
            self.connections['North'] = None
            self.shape = [
                ' | ',
                '─╯ ', 
                '   '
            ]
        elif ori == 7:
            self.connections['NorthWest'] = None
            self.connections['NorthEast'] = None
            self.shape = [
                '\\  /',
                ' ︶ ',
                '   '
            ]
    
class One_thirty_five(Tiles):
    def __init__(self, name='One-Thirty-Five', ori = 0, position = None):
        super().__init__(name, position, ori)
        if ori == 0:
            self.connections['North'] = None
            self.connections['SouthEast'] = None
            self.shape = [
                ' | ',
                ' | ',
                '  \\'
            ]
        elif ori == 1:
            self.connections['NorthEast'] = None
            self.connections['South'] = None
            self.shape = [
                '  /',
                ' | ',
                ' | '
            ]
        elif ori == 2:
            self.connections['East'] = None
            self.connections['SouthWest'] = None
            self.shape = [
                '   ',
                ' ──',
                '/  '
            ]
        elif ori == 3:
            self.connections['SouthEast'] = None
            self.connections['West'] = None
            self.shape = [
                '   ',
                '── ',
                '  \\'
            ]
        elif ori == 4:
            self.connections['South'] = None
            self.connections['NorthWest'] = None
            self.shape = [
                '\\  ',
                ' | ',
                ' | '
            ]
        elif ori == 5:
            self.connections['SouthWest'] = None
            self.connections['North'] = None
            self.shape = [
                ' | ',
                ' | ',
                '/  '
            ]
        elif ori == 6:
            self.connections['West'] = None
            self.connections['NorthEast'] = None
            self.shape = [
                '  /',
                '── ',
                '   '
            ]
        elif ori == 7:
            self.connections['NorthWest'] = None
            self.connections['East'] = None
            self.shape = [
                '\\  ',
                ' ──',
                '   '
            ]
    
def inbounds(position, size=4):
    bound = size-1
    x,y = position
    return 0 <= x <= bound and 0 <= y <= bound

def next_connection_location(current, next_tile):
    if len(current.shared_open_connections(next_tile)) == 0:
        print("test")
        return False
    
    direction, connecting_direction = current.shared_open_connections(next_tile)
    open_connection = list(next_tile.connections)
    if len(open_connection)>1:
        open_connection.remove(connecting_direction)
    open_connection = open_connection[0]
    y,x = add_positions(next_tile.position, directions[open_connection])

    return (y,x)

def add_positions(position1, position2):
    return tuple(map(sum, zip(position1, position2)))

if __name__ == '__main__':
    start = Start('East', position=(0,0))
    end = End('West', position=(3,3))

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
    board = [[start,None,None,None],
            [None,None,None,None],
            [None,None,None,None],
            [None,None,None,end],
    ]
    start.connect(tile1)
    tile1.connect(tile2)
    tile2.connect(tile3)
    tile3.connect(tile4)
    tile4.connect(tile5)
    tile5.connect(tile6)
    tile6.connect(tile7)
    tile7.connect(tile8)
    tile8.connect(tile9)
    tile9.connect(tile10)
    tile10.connect(tile11)
    tile11.connect(tile12)
    tile12.connect(tile13)
    tile13.connect(tile14)
    tile14.connect(end) 

    print(start)
    print(tile1)
    print(tile2)
    print(tile3)
    print(tile4)
    print(tile5)
    print(tile6)
    print(tile7)
    print(tile8)
    print(tile9)
    print(tile10)
    print(tile11)
    print(tile12)
    print(tile13)
    print(tile14)
    print(end)
