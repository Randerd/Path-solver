import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


/**
 * tiles
 */
public class Tiles implements GlobalConstants{
    // Tuple<Integer, Integer, Integer> color;
    int[] position;
    String name;
    String tile_type;
    Integer orientation;
    String[] shape;

    Map<String, Tiles> connections = new HashMap<String, Tiles>();

    public Tiles(String Name, int[] Position, int Ori){
        this(Name, Position);
        orientation = Ori;
        tile_type = tile_type + Ori;
    }

    public Tiles(String Name, int[] Position){
        name = Name;
        position = Position;
        tile_type = this.getClass().getSimpleName();
    }

    public int num_connections(){
        return 1;
    }

    public boolean is_connected(){
        return true;
    }

    public String get_open_connection(){
        return "None";
    }

    public void set_position(int[] new_position){
        position = new_position;
    }

    public void remove_position(){
        position = null;
    }

    public static void main(String[] args) {
        System.out.println("Hello World2");
        int y = dirToOffset.get("North")[0];
        int x = dirToOffset.get("North")[1];
        System.out.printf("%d %d", y, x);
    }
}

class Edge extends Tiles {
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
        // System.out.println(row);

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

class Line extends Tiles {

    public Line(int[] Position, int Ori){
        super("Line", Position, Ori);
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

class Forty_five extends Tiles {

    public Forty_five(int[] Position, int Ori){
        super("Forty_five", Position, Ori);
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

class Ninety extends Tiles {
    public Ninety(int[] Position, int Ori){
        super("Ninety", Position, Ori);
        if (Ori == 0){
            connections.put("North", null);
            connections.put("East", null);
            shape = new String[] {
                " | ",
                " ╰─", 
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
                " ╭─", 
                " | "
            };
        } else if (Ori == 3){
            connections.put("SouthEast", null);
            connections.put("SouthWest", null);
            shape = new String[] {
                "   ",
                " ︵ ", 
                "/ \\"
            };
        } else if (Ori == 4){
            connections.put("South", null);
            connections.put("West", null);
            shape = new String[] {
                "   ",
                "─╮ ",
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
                "─╯ ", 
                "   "
            };
        } else if (Ori == 7){
            connections.put("NorthWest", null);
            connections.put("NorthEast", null);
            shape = new String[] {
                "\\  /",
                " ︶ ",
                "   "
            };
        }
    }
}

class One_thirty_five extends Tiles{
    public One_thirty_five(int[] Position, int Ori){
        super("One_thirty_five", Position, Ori);
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

class Blank extends Tiles {

    public Blank(int[] Position){
        super("Blank", Position);
    }
}
class test {
    public static void main(String[] args) {
        Start start = new Start("South", new int[] {0,0});
        End end = new End("NorthEast", new int[] {4,4});
        System.out.println(Arrays.toString(start.shape));
        System.out.println(Arrays.toString(end.shape));
    }
}