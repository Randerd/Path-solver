import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class PathFinder {

    static ArrayList<Tiles> tileList = new ArrayList<Tiles>();
    static HashMap<String, ArrayList<Tiles>> tiles = new HashMap<>() {{
        put("North", new ArrayList<Tiles>());
        put("NorthEast", new ArrayList<Tiles>());
        put("East", new ArrayList<Tiles>());
        put("SouthEast", new ArrayList<Tiles>());
        put("South", new ArrayList<Tiles>());
        put("SouthWest", new ArrayList<Tiles>());
        put("West", new ArrayList<Tiles>());
        put("NorthWest", new ArrayList<Tiles>());
    }};

    static Tiles start;
    static Tiles end;
    static int size; 

    static Tiles [][] board;

    public PathFinder(){
        set_size(5);
    }

    public static void connect(Tiles tile1, Tiles tile2){

    }

    public static void connect(Tiles tile1, Tiles tile2, String Direction){

    }

    public static void disconnect(Tiles tile1, Tiles tile2){

    }

    public static void disconnect(Tiles tile, String Direction){

    }

    public static String[] shared_open_connections(){
        return new String[] {};
    }

    public static String[] shared_closed_connections(){
        return new String[] {};
    }
    
    public static int tiles_left(){
        int count = 0;
        for (ArrayList<Tiles> tile: tiles.values()) {
            count += tile.size();
        }
        return count / 2;
    }

    public static void add_tile(Tiles tile){
        tileList.add(tile);
        unuse_tile(tile);
    }

    public static void remove_tile(Tiles tile){
        tileList.remove(tile);
        use_tile(tile);
    }
    
    public static void add_blank(int[] Position) {
        board[Position[0]][Position[1]] = new Blank(Position);
    }

    private static void use_tile(Tiles tile) {
        for (String direction: tile.connections.keySet()) {
            tiles.get(direction).add(tile);
        }
    }

    private static void unuse_tile(Tiles tile) {
        for (String direction: tile.connections.keySet()) {
            tiles.get(direction).remove(tile);
        }
    }

    public static void place_tile(Tiles tile){
        board[tile.position[0]][tile.position[1]] = tile;
    }

    public static void unplace_tile(Tiles tile){
        board[tile.position[0]][tile.position[1]] = null;
    }

    public static void set_size(int Size){
        size = Size;
        board = new Tiles [size][size];
    }
    
    public static void set_end(End End){
        end = End;
        place_tile(end);
    }

    public static void set_start(Start Start){
        start = Start;
        place_tile(start);
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
    public static void main(String[] args) {
        set_size(5);
        
        // System.out.println(size);
        // System.out.println(board.length);
        Start s = new Start("South", new int[] {0,0});
        End e = new End("NorthWest", new int[] {4,4});
        set_end(e);
        set_start(s);
        System.out.println(end.position[0]);
        print_board();
    }
}

/**
 *  Test
 */
// class test {
//     public static void main(String[] args) {
//         Start start = new Start("South", new int[] {0,0});
//         End end = new End("NorthEast", new int[] {4,4});
//         System.out.println(Arrays.toString(start.shape));
//         System.out.println(Arrays.toString(end.shape));
//     }
// }