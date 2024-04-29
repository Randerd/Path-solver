import java.util.HashMap;

public interface Constants
{
    HashMap<String, String> oppDir = createPairs(); //new HashMap<String, String>();
    HashMap<String, int[]> dirToOffset = createDir();

    public static HashMap<String, String> createPairs(){
        HashMap<String, String> pairs = new HashMap<String, String>();

        pairs.put("North", "South");
        pairs.put("NorthEast", "SouthWest");
        pairs.put("East", "West");
        pairs.put("SouthEast", "NorthWest");
        pairs.put("South", "North");
        pairs.put("SouthWest", "NorthEast");
        pairs.put("West", "East");
        pairs.put("NorthWest", "SouthEast");

        return pairs;
    }

    public static HashMap<String, int[]> createDir(){
        HashMap<String, int[]> directions = new HashMap<String, int[]>();
        
        directions.put("North", new int[] {-1,0});
        directions.put("NorthEast", new int[] {-1,1});
        directions.put("East", new int[] {0,1});
        directions.put("SouthEast", new int[] {1,1});
        directions.put("South", new int[] {1,0});
        directions.put("SouthWest", new int[] {1,-1});
        directions.put("West", new int[] {0,-1});
        directions.put("NorthWest", new int[] {-1,-1});
        
        return directions;
    }
}