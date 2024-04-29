import java.util.HashMap;
import java.util.Map;


/**
 * tiles
 */
public class Tile implements Constants{
    // Tuple<Integer, Integer, Integer> color;
    int[] position;
    String name;
    String tile_type;
    Integer orientation;
    String[] shape;

    Map<String, Tile> connections = new HashMap<String, Tile>();

    public Tile(String Name, int[] Position, int Ori){
        this(Name, Position);
        orientation = Ori;
        tile_type = tile_type + Ori;
    }

    public Tile(String Name, int[] Position){
        name = Name;
        position = Position;
        tile_type = this.getClass().getSimpleName();
    }

    public void connect(Tile other_tile){
        String[] directions = shared_open_connections(other_tile);
        connect(directions[0], other_tile);
        other_tile.connect(directions[1], this);
    }

    public void connect(String direction, Tile other_tile){
        connections.put(direction, other_tile);
        other_tile.connections.put(oppDir.get(direction), this);
    }

    public void disconnect(String direction){
        connections.put(direction, null);
    }

    public void disconnect(String direction, Tile other_tile){
        disconnect(direction);
        other_tile.disconnect(oppDir.get(direction));
    }

    public String[] shared_open_connections(Tile other_tile){
        for (String direction : connections.keySet()){
            String connecting_direction = oppDir.get(direction);
            if (other_tile.connections.containsKey(connecting_direction) && connections.get(direction) == null){
                return new String[] {direction, connecting_direction};
            }
        }
        return new String[] {};
    }

    public String[] shared_closed_connections(Tile other_tile){
        for (String direction : connections.keySet()){
            String connecting_direction = oppDir.get(direction);
            if (other_tile.connections.containsKey(connecting_direction) && connections.get(direction) != null){
                return new String[] {direction, connecting_direction};
            }
        }
        return new String[] {};
    }

    public String get_open_connection(){
        for (Map.Entry<String, Tile> connection : connections.entrySet()){
            if (connection.getValue() == null){
                return connection.getKey();
            }
        }
        return "Null";
    }

    public void set_position(int[] new_position){
        position = new_position;
    }

    public void remove_position(){
        position = null;
    }
}

class Edge extends Tile {
    String pointing;

    public Edge(String Name, String Pointing, int[] Position) {
        super(Name, Position);
        pointing = Pointing;
        connections.put(pointing, null);
    }

    public void update_shape(){
        int y = dirToOffset.get(pointing)[0];
        int x = dirToOffset.get(pointing)[1];

        char[] row = shape[y+1].toCharArray();
        row[x+1] = '.';
        shape[y+1] = new String(row);
    }
}

class Start extends Edge {
    
    public Start(String Pointing, int[] Position){
        super("Start", Pointing, Position);
        shape = new String[] {
            "SSS",
            "SSS",
            "SSS"
        };
        update_shape();
    }
}

class End extends Edge{

    public End(String Pointing, int[] Position){
        super("End", Pointing, Position);
        shape = new String[] {
            "EEE",
            "EEE",
            "EEE"
        };
        update_shape();
    }
}

class Line extends Tile {

    public Line(String name, int Ori){
        super(name, null, Ori);
        if (Ori == 0){
            connections.put("North", null);
            connections.put("South", null);
            shape = new String[] {
                " | ",
                " | ",
                " | "
            };
        } else if (Ori == 1){
            connections.put("NorthEast", null);
            connections.put("SouthWest", null);
            shape = new String[] {
                "  /",
                " / ",
                "/  "
            };
        } else if (Ori == 2){
            connections.put("West", null);
            connections.put("East", null);
            shape = new String[] {
                "   ",
                "───",
                "   "
            };
        } else if (Ori == 3){
            connections.put("SouthEast", null);
            connections.put("NorthWest", null);
            shape = new String[] {
                "\\  ",
                " \\ ",
                "  \\"
            };
        }
    }
}

class Forty_five extends Tile {

    public Forty_five(String name, int Ori){
        super(name, null, Ori);
        if (Ori == 0){
            connections.put("North", null);
            connections.put("NorthEast", null);
            shape = new String[] {
                " |/",
                "   ",
                "   "
            };
        } else if (Ori == 1){
            connections.put("NorthEast", null);
            connections.put("East", null);
            shape = new String[] {
                "  /",
                "  ─",
                "   "
            };
        } else if (Ori == 2){
            connections.put("East", null);
            connections.put("SouthEast", null);
            shape = new String[] {
                "   ",
                "  ─",
                "  \\"
            };
        } else if (Ori == 3){
            connections.put("SouthEast", null);
            connections.put("South", null);
            shape = new String[] {
                "   ",
                "   ",
                " |\\"
            };
        } else if (Ori == 4){
            connections.put("South", null);
            connections.put("SouthWest", null);
            shape = new String[] {
                "   ",
                "   ",
                "/| "
            };
        } else if (Ori == 5){
            connections.put("SouthWest", null);
            connections.put("West", null);
            shape = new String[] {
                "   ",
                "─  ",
                "/  ",
            };
        } else if (Ori == 6){
            connections.put("West", null);
            connections.put("NorthWest", null);
            shape = new String[] {
                "\\  ",
                "─  ",
                "   "
            };
        } else if (Ori == 7){
            connections.put("NorthWest", null);
            connections.put("North", null);
            shape = new String[] {
                "\\| ",
                "   ",
                "   "
            };
        }
    }
}

class Ninety extends Tile {
    public Ninety(String name, int Ori){
        super(name, null, Ori);
        if (Ori == 0){
            connections.put("North", null);
            connections.put("East", null);
            shape = new String[] {
                " | ",
                " \\─", 
                "   "
            };
        } else if (Ori == 1){
            connections.put("NorthEast", null);
            connections.put("SouthEast", null);
            shape = new String[] {
                "  /",
                " | ",
                "  \\"
            };
        } else if (Ori == 2){
            connections.put("East", null);
            connections.put("South", null);
            shape = new String[] {
                "   ",
                " /─", 
                " | "
            };
        } else if (Ori == 3){
            connections.put("SouthEast", null);
            connections.put("SouthWest", null);
            shape = new String[] {
                "   ",
                " _ ", 
                "/ \\"
            };
        } else if (Ori == 4){
            connections.put("South", null);
            connections.put("West", null);
            shape = new String[] {
                "   ",
                "─\\ ",
                " | "
            };
        } else if (Ori == 5){
            connections.put("SouthWest", null);
            connections.put("NorthWest", null);
            shape = new String[] {
                "\\  ",
                " | ",
                "/  "
            };
        } else if (Ori == 6){
            connections.put("West", null);
            connections.put("North", null);
            shape = new String[] {
                " | ",
                "─/ ", 
                "   "
            };
        } else if (Ori == 7){
            connections.put("NorthWest", null);
            connections.put("NorthEast", null);
            shape = new String[] {
                "\\  /",
                " - ",
                "   "
            };
        }
    }
}

class One_thirty_five extends Tile{
    public One_thirty_five(String name, int Ori){
        super(name, null, Ori);
        if (Ori == 0){
            connections.put("North", null);
            connections.put("SouthEast", null);
            shape = new String[] {
                " | ",
                " | ",
                "  \\"
            };
        } else if (Ori == 1){
            connections.put("NorthEast", null);
            connections.put("South", null);
            shape = new String[] {
                "  /",
                " | ",
                " | "
            };
        } else if (Ori == 2){
            connections.put("East", null);
            connections.put("SouthWest", null);
            shape = new String[] {
                "   ",
                " ──",
                "/  "
            };
        } else if (Ori == 3){
            connections.put("SouthEast", null);
            connections.put("West", null);
            shape = new String[] {
                "   ",
                "── ",
                "  \\"
            };
        } else if (Ori == 4){
            connections.put("South", null);
            connections.put("NorthWest", null);
            shape = new String[] {
                "\\  ",
                " | ",
                " | "
            };
        } else if (Ori == 5){
            connections.put("SouthWest", null);
            connections.put("North", null);
            shape = new String[] {
                " | ",
                " | ",
                "/  "
            };
        } else if (Ori == 6){
            connections.put("West", null);
            connections.put("NorthEast", null);
            shape = new String[] {
                "  /",
                "── ",
                "   "
            };
        } else if (Ori == 7){
            connections.put("NorthWest", null);
            connections.put("East", null);
            shape = new String[] {
                "\\  ",
                " ──",
                "   "
            };
        }
    }
}

class Blank extends Tile {

    public Blank(){
        super("Blank", null);
    }
}


class test2 {
    public static void main(String[] args) {
        Map<String, Tile> connections = new HashMap<String, Tile>();
        System.out.println(connections.get("South"));
        Ninety test = new Ninety("test", 0);
        System.out.println(test.connections);
    }
}