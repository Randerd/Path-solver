import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class PathFinder {

    static ArrayList<Tile> tileList = new ArrayList<Tile>();
    static HashMap<String, ArrayList<Tile>> tiles = new HashMap<>() {{
        put("North", new ArrayList<Tile>());
        put("NorthEast", new ArrayList<Tile>());
        put("East", new ArrayList<Tile>());
        put("SouthEast", new ArrayList<Tile>());
        put("South", new ArrayList<Tile>());
        put("SouthWest", new ArrayList<Tile>());
        put("West", new ArrayList<Tile>());
        put("NorthWest", new ArrayList<Tile>());
    }};

    static Start start;
    static End end;
    static int size; 

    static Tile [][] board;

    public PathFinder(){
        set_size(5);
    }

    public static void find_path(){
        if (find_path(start)){
            System.out.println("Path Found");
        }else{
            System.out.println("No Path Found");
        }
    }

    private static Boolean find_path(Tile current){

        String direction = current.get_open_connection();
        String connecting_direction = Tile.oppDir.get(direction);
        int[] new_position = add_positions(current.position, Tile.dirToOffset.get(direction));
        ArrayList<Tile> possible_tiles = new ArrayList<>(tiles.get(connecting_direction));
        Set<String> used = new HashSet<String>();

        for (Tile tile : possible_tiles){
            if (used.contains(tile.tile_type)){
                continue;
            }
            used.add(tile.tile_type);
            
            tile.set_position(new_position);
            int[] next_position = next_connection_location(current, tile);
            if (inbounds(next_position, size) && board[next_position[0]][next_position[1]] == null){
                current.connect(direction, tile);
                use_tile(tile);
                place_tile(tile);
                
                if (find_path(tile)){
                    return true;
                }
                
                current.disconnect(direction, tile);
                unuse_tile(tile);
                unplace_tile(tile);
            // Base case - One tile left, must connect to end
            } else if (tiles_left() == 1 && inbounds(next_position, size) && board[next_position[0]][next_position[1]] instanceof End){
                current.connect(direction, tile);
                place_tile(tile);
                tile.connect(end);
                return true;
            }
            tile.remove_position();
        }
        return false;
    }
    
    private static void set_size(int Size){
        size = Size;
        board = new Tile [size][size];
    }  

/*     private static void connect(Tile tile1, Tile tile2){
        String[] directions = shared_open_connections(tile1, tile2);
        connect(directions[0], tile1, tile2);
        connect(directions[1], tile2, tile1);
    }

    private static void connect(String direction, Tile tile1, Tile tile2){
        tile1.connections.put(direction, tile2);
        tile2.connections.put(oppDir.get(direction), tile1);
    }

    private static void disconnect(String direction, Tile tile){
        tile.connections.put(direction, null);
    }

    private static void disconnect(String direction, Tile tile1, Tile tile2){
        disconnect(direction, tile1);
        disconnect(oppDir.get(direction), tile2);
    }

    private static String[] shared_open_connections(Tile tile1, Tile tile2){
        for (String direction : tile1.connections.keySet()){
            String connecting_direction = oppDir.get(direction);
            if (tile2.connections.containsKey(connecting_direction) && tile1.connections.get(direction) == null){
                return new String[] {direction, connecting_direction};
            }
        }
        return new String[] {};
    } 
    private static Boolean find_path(Tile current){

        String direction = current.get_open_connection();
        String connecting_direction = oppDir.get(direction);
        int[] new_position = add_positions(current.position, dirToOffset.get(direction));
        ArrayList<Tile> possible_tiles = new ArrayList<>(tiles.get(connecting_direction));
        Set<String> used = new HashSet<String>();

        for (Tile tile : possible_tiles){
            if (used.contains(tile.tile_type)){
                continue;
            }
            used.add(tile.tile_type);
            
            tile.set_position(new_position);
            int[] next_position = next_connection_location(current, tile);
            if (inbounds(next_position, size) && board[next_position[0]][next_position[1]] == null){
                connect(direction, current, tile);
                use_tile(tile);
                place_tile(tile);
                
                if (find_path(tile)){
                    return true;
                }
                
                disconnect(direction, current, tile);
                unuse_tile(tile);
                unplace_tile(tile);
            // Base case - One tile left, must connect to end
            } else if (tiles_left() == 1 && inbounds(next_position, size) && board[next_position[0]][next_position[1]] instanceof End){
                connect(direction, current, tile);
                place_tile(tile);
                connect(tile, end);
                return true;
            }
            tile.remove_position();
        }
        return false;
    } */
    
    // Pathfinder Tile information
    private static int tiles_left(){
        int count = 0;
        for (ArrayList<Tile> tile: tiles.values()) {
            count += tile.size();
        }
        return count / 2;
    }

    private static void add_blank(int[] Position) {
        board[Position[0]][Position[1]] = new Blank();
    }

    private static void add_tile(Tile tile){
        tileList.add(tile);
        unuse_tile(tile);
    }

    private static void remove_tile(Tile tile){
        tileList.remove(tile);
        use_tile(tile);
    }

    // Pathfinder board information
    private static void use_tile(Tile tile) {
        for (String direction: tile.connections.keySet()) {
            tiles.get(direction).remove(tile);
        }
    }

    private static void unuse_tile(Tile tile) {
        for (String direction: tile.connections.keySet()) {
            tiles.get(direction).add(tile);
        }
    }

    private static void place_tile(Tile tile){
        board[tile.position[0]][tile.position[1]] = tile;
    }

    private static void unplace_tile(Tile tile){
        board[tile.position[0]][tile.position[1]] = null;
    }

    private static void set_start(Start Start){
        start = Start;
        place_tile(Start);
    }
   
    private static void set_end(End End){
        end = End;
        place_tile(End);
    }

    public static void print_board(){

        for (int i = 0; i < board.length; i++) {
            String[] rows = {"", "", ""};
            for (int j = 0; j < board.length; j++) {
                if (board[i][j] == null || board[i][j] instanceof Blank){
                    rows[0] += "   "; rows[1] += "   "; rows[2] += "   ";
                } else {
                    rows[0] += board[i][j].shape[0];
                    rows[1] += board[i][j].shape[1];
                    rows[2] += board[i][j].shape[2];
                }
                rows[0] += " "; rows[1] += " "; rows[2] += " ";
            }
            for (String string : rows) {
                System.out.println(string);
            } System.out.println();
        }
    }

    private static int[] next_connection_location(Tile current, Tile next_tile){
        String[] directions = current.shared_open_connections(next_tile);
        Set<String> open_connection = new HashSet<>(next_tile.connections.keySet());
        if (open_connection.size() > 1){
            open_connection.remove(directions[1]);
        }
        String connection = (String) open_connection.toArray()[0];
        
        int[] position = add_positions(next_tile.position, Constants.dirToOffset.get(connection));
        return position;
    }

/*     private static int[] next_connection_location(Tile current, Tile next_tile){
        String[] directions = shared_open_connections(current, next_tile);
        Set<String> open_connection = new HashSet<>(next_tile.connections.keySet());
        if (open_connection.size() > 1){
            open_connection.remove(directions[1]);
        }
        String connection = (String) open_connection.toArray()[0];
        
        int[] position = add_positions(next_tile.position, GlobalConstants.dirToOffset.get(connection));
        return position;
    } */

    public static boolean inbounds(int[] position, int size){
        int bound = size - 1;
        int x = position[1];
        int y = position[0];
        return (0 <= x && x <= bound) && (0 <= y && y <= bound);
    }
    
    public static int[] add_positions(int[] pos1, int[] pos2) {
        int[] new_pos = new int[pos1.length];
        for (int i = 0; i < pos1.length; i++) {
            new_pos[i] = pos1[i] + pos2[i];
        }
        return new_pos;
    }

    public static void main(String[] args) {

        int level = 221;
        if (level == 212){
            Start start = new Start("NorthEast", new int[]{2,1});
            End end = new End("SouthWest", new int[]{1,3});
            
            Ninety tile1 = new Ninety("Ninety1", 6);
            Ninety tile2 = new Ninety("Ninety2", 2);
            Ninety tile3 = new Ninety("Ninety3", 4);
            Ninety tile4 = new Ninety("Ninety4", 0);
            Ninety tile5 = new Ninety("Ninety5", 2);
            
            Forty_five tile6 = new Forty_five("FourtyFive1", 0);
            Forty_five tile7 = new Forty_five("FourtyFive2", 1);
            Forty_five tile8 = new Forty_five("FourtyFive3", 5);
            Forty_five tile9 = new Forty_five("FourtyFive4", 4);
            
            Line tile10 = new Line("HLine1", 2);
            Line tile11 = new Line("HLine2", 2);
            Line tile12 = new Line("HLine3", 2);
            Line tile13 = new Line("HLine4", 2);
            
            Line tile14 = new Line("VLine1", 0);
            Line tile15 = new Line("VLine2", 0);
            Line tile16 = new Line("VLine3", 0);
            Line tile17= new Line("VLine4", 0);
            
            Line tile18 = new Line("/Line1", 1);
            Line tile19 = new Line("/line2", 1);
            One_thirty_five tile20 = new One_thirty_five("OneThirtyFive1", 5);
            One_thirty_five tile21 = new One_thirty_five("OneThirtyFive2", 2);
            One_thirty_five tile22 = new One_thirty_five("OneThirtyFive3", 6);
            One_thirty_five tile23 = new One_thirty_five("OneThirtyFive4", 6);
            set_size(5);
            set_start(start);
            set_end(end);
            add_tile(tile1);
            add_tile(tile2);
            add_tile(tile3);
            add_tile(tile4);
            add_tile(tile5);
            add_tile(tile6);
            add_tile(tile7);
            add_tile(tile8);
            add_tile(tile9);
            add_tile(tile10);
            add_tile(tile11);
            add_tile(tile12);
            add_tile(tile13);
            add_tile(tile14);
            add_tile(tile15);
            add_tile(tile16);
            add_tile(tile17);
            add_tile(tile18);
            add_tile(tile19);
            add_tile(tile20);
            add_tile(tile21);
            add_tile(tile22);
            add_tile(tile23);
            print_board();
        }
        else if (level == 219){
            int size = 6;
            Start start = new Start("SouthEast", new int[]{0,2});
            end = new End("West", new int[]{0,4});
    
            Ninety tile1 = new Ninety("tile1", 2);
            Ninety tile2 = new Ninety("tile2", 2);
            Ninety tile3 = new Ninety("tile3", 6);
            Ninety tile4 = new Ninety("tile4", 6);
            Ninety tile5 = new Ninety("tile5", 6);
            Ninety tile6 = new Ninety("tile6", 0);
     
            Ninety tile7 = new Ninety("tile7", 7);
            Ninety tile8 = new Ninety("tile8", 5);
            Ninety tile9 = new Ninety("tile9", 3);
    
            Forty_five tile10 = new Forty_five("tile10", 0);
            Forty_five tile11 = new Forty_five("tile11", 7);
            Forty_five tile12 = new Forty_five("tile12", 7);
            Forty_five tile13 = new Forty_five("tile13", 2);
            Forty_five tile14 = new Forty_five("tile14", 2);
            Forty_five tile15 = new Forty_five("tile15", 3);
            Forty_five tile16 = new Forty_five("tile16", 3);
            Forty_five tile17 = new Forty_five("tile17", 4);
            Forty_five tile18 = new Forty_five("tile18", 4);
    
            One_thirty_five tile19 = new One_thirty_five("tile19", 3);
            One_thirty_five tile20 = new One_thirty_five("tile20", 4);
            One_thirty_five tile21 = new One_thirty_five("tile21", 6);
            One_thirty_five tile22 = new One_thirty_five("tile22", 6);
            One_thirty_five tile23 = new One_thirty_five("tile23", 7);
            One_thirty_five tile24 = new One_thirty_five("tile24", 7);
    
            Line tile25 = new Line("tile25", 0);
            Line tile26 = new Line("tile26", 0);
            Line tile27 = new Line("tile27", 0);
            Line tile28 = new Line("tile28", 0);
            Line tile29 = new Line("tile29", 2);
            Line tile30 = new Line("tile30", 2);

            set_size(size);
            set_start(start);
            set_end(end);
    
            add_blank(new int[]{0,0});
            add_blank(new int[]{0,1});
            add_blank(new int[]{1,0});
            add_blank(new int[]{1,1});
            add_tile(tile13);
            add_tile(tile2);
            add_tile(tile3);
            add_tile(tile4);
            add_tile(tile6);
            add_tile(tile1);
            add_tile(tile7);
            add_tile(tile8);
            add_tile(tile9);
            add_tile(tile10);
            add_tile(tile15);
            add_tile(tile11);
            add_tile(tile5);
            add_tile(tile18);
            add_tile(tile19);
            add_tile(tile12);
            add_tile(tile14);
            add_tile(tile16);
            add_tile(tile26);
            add_tile(tile17);
            add_tile(tile30);
            add_tile(tile20);
            add_tile(tile22);
            add_tile(tile23);
            add_tile(tile24);
            add_tile(tile25);
            add_tile(tile21);
            add_tile(tile27);
            add_tile(tile28);
            add_tile(tile29);
        } 
        else if (level == 221){
            int size = 6;
            Start start = new Start("NorthEast", new int[]{3,1});
            end = new End("East", new int[]{0,0});
    
            Ninety tile1 = new Ninety("tile1", 6);
            Ninety tile2 = new Ninety("tile2", 6);
            Ninety tile3 = new Ninety("tile3", 6);
            Ninety tile4 = new Ninety("tile4", 6);
            Ninety tile5 = new Ninety("tile5", 6);
            Ninety tile6 = new Ninety("tile6", 0);
            Ninety tile7 = new Ninety("tile7", 0);
            Ninety tile8 = new Ninety("tile8", 0);
            Ninety tile9 = new Ninety("tile9", 4);
            Ninety tile10 = new Ninety("tile10", 2);
    
            Forty_five tile11 = new Forty_five("tile11", 0);
            Forty_five tile12 = new Forty_five("tile12", 2);
            Forty_five tile13 = new Forty_five("tile13", 4);
            Forty_five tile14 = new Forty_five("tile14", 3);
            Forty_five tile15 = new Forty_five("tile15", 3);
            Forty_five tile16 = new Forty_five("tile16", 5);
            Forty_five tile17 = new Forty_five("tile17", 5);
            Forty_five tile18 = new Forty_five("tile18", 7);
    
            One_thirty_five tile19 = new One_thirty_five("tile19", 2);
            One_thirty_five tile20 = new One_thirty_five("tile20", 2);
            One_thirty_five tile21 = new One_thirty_five("tile21", 1);
            One_thirty_five tile22 = new One_thirty_five("tile22", 4);
            One_thirty_five tile23 = new One_thirty_five("tile23", 4);
            One_thirty_five tile24 = new One_thirty_five("tile24", 1);
            One_thirty_five tile25 = new One_thirty_five("tile25", 1);

            Line tile26 = new Line("tile26", 0);
            Line tile27 = new Line("tile27", 0);
            Line tile28 = new Line("tile28", 0);
            Line tile29 = new Line("tile29", 0);
            Line tile30 = new Line("tile30", 1);

            set_size(size);
            set_start(start);
            set_end(end);
    
            add_blank(new int[]{4,2});
            add_blank(new int[]{4,3});
            add_blank(new int[]{5,2});
            add_blank(new int[]{5,3});
            add_tile(tile13);
            add_tile(tile2);
            add_tile(tile3);
            add_tile(tile4);
            add_tile(tile6);
            add_tile(tile1);
            add_tile(tile7);
            add_tile(tile8);
            add_tile(tile9);
            add_tile(tile10);
            add_tile(tile15);
            add_tile(tile11);
            add_tile(tile5);
            add_tile(tile18);
            add_tile(tile19);
            add_tile(tile12);
            add_tile(tile14);
            add_tile(tile16);
            add_tile(tile26);
            add_tile(tile17);
            add_tile(tile30);
            add_tile(tile20);
            add_tile(tile22);
            add_tile(tile23);
            add_tile(tile24);
            add_tile(tile25);
            add_tile(tile21);
            add_tile(tile27);
            add_tile(tile28);
            add_tile(tile29); 
        }
        
        final long startTime = System.currentTimeMillis();
        find_path();
        final long endTime = System.currentTimeMillis();
        print_board();
        System.out.println("Total execution time: " + (endTime - startTime));
    }
}

/**
 *  Test
 */
class test {
    public static void main(String[] args) {
        int[] arr1 = {1,2};
        int[] arr2 = {1,1};
        // PathFinder pf = new PathFinder();
        int[] arr3 = PathFinder.add_positions(arr1, arr2);
        System.out.println(arr3[0] + ", " + arr3[1]);
    }
}