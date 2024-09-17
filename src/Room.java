import java.util.ArrayList;
import java.util.Scanner;
import java.io.File;
import java.io.IOException;

public class Room {

    private Map map;
    private AI ai;
    private Image room;
    private Image victory;
    private boolean isShown;
    private boolean cleared;
    private int[] position;
    private Door[] doors;
    private ArrayList<double[]> enemies;
    private ArrayList<Rock> rocks;
    private ArrayList<Fire> fires;
    private ArrayList<Item> items;
    private TrapDoor trapDoor;

    public Room(int type, int[] position, Map map, AI ai){
        this.map = map;
        this.ai = ai;
        this.position = position;
        this.cleared = false;
        this.doors = new Door[4];
        this.room = new Image("textures/map.png");
        this.room.changePosition(new double[]{0, 0});
        this.victory = new Image("textures/victory.png");
        this.victory.changePosition(new double[]{-250, -200});
        this.enemies = new ArrayList<double[]>();
        this.rocks = new ArrayList<Rock>();
        this.fires = new ArrayList<Fire>();
        this.items = new ArrayList<Item>();

        File file;
        if(type == 0)
            file = new File("rooms/room_start.txt");
        else
            file = new File("rooms/room" + type + ".txt");
        try{
            Scanner scanner = new Scanner(file);
            for(int i = 0; i < 7; i++){
                for(int j = 0; j < 13; j++){
                    int e = scanner.nextInt();
                    switch(e){
                        case 0:
                        break;
                        case 1:
                        this.enemies.add(new double[]{(j * 50) - 300, (i * -50) + 150});
                        break;
                        case 2:
                        this.rocks.add(new Rock((j * 50) - 300 + this.map.modifier()[0], (i * -50) + 150 + this.map.modifier()[1]));
                        break;
                        case 3:
                        this.fires.add(new Fire((j * 50) - 300 + this.map.modifier()[0], (i * -50) + 150 + this.map.modifier()[1]));
                        break;
                    }
                    if(e > 9)
                        this.items.add(new Item((j * 50) - 300 + this.map.modifier()[0], (i * -50) + 150 + this.map.modifier()[1], e % 10));
                }
                if(scanner.hasNextLine()) 
                    scanner.nextLine();
            }
        }
        catch(IOException ex){
            System.out.println("Couldn't find the requested file");
        }
    }

    public void update(){
        if(this.isShown != true){
            this.isShown = true;
            this.room.show();
            for(int i = 0; i < 4; i++){
                if(this.doors[i] != null)
                    this.doors[i].show();
            }
            for(Rock rock : this.rocks)
                rock.show();
            if(!this.cleared){
                for(double[] enemy : this.enemies)
                    this.ai.spawnEnemy(enemy);
            }
            if(this.trapDoor != null)
                this.trapDoor.show();
        }
        for(Fire fire : this.fires)
            fire.update();
        for(Item item : this.items){
            item.close();
            item.show();
        }
        if(this.map.isCleared()){
            this.victory.hide();
            this.victory.show();
        }
    }

    public void spawnItem(int ID, double[] coords){
        this.items.add(new Item((int)coords[0], (int)coords[1], ID));
        for(Item item : this.items)
            item.show();
    }

    public void spawnTrapDoor(){
        this.trapDoor = new TrapDoor(0 + this.map.modifier()[0], 0 + this.map.modifier()[1]);
    }

    public void inicializeConnection(){
        if(this.position[1] != this.map.rooms()[0].length - 1 && this.map.rooms()[this.position[0]][this.position[1] + 1] != null)
            this.doors[0] = new Door(0, this.map);
        if(this.position[1] != 0 && this.map.rooms()[this.position[0]][this.position[1] - 1] != null)
            this.doors[1] = new Door(1, this.map);
        if(this.position[0] != 0 && this.map.rooms()[this.position[0] - 1][this.position[1]] != null)
            this.doors[2] = new Door(2, this.map);
        if(this.position[0] != this.map.rooms().length - 1 && this.map.rooms()[this.position[0] + 1][this.position[1]] != null)
            this.doors[3] = new Door(3, this.map);
    }

    public void close(){
        if(this.trapDoor != null)
            this.trapDoor.close();
        this.victory.hide();
        this.isShown = false;
        this.room.hide();
        for(Rock rock : this.rocks)
            rock.close();
        for(Item item : this.items)
            item.close();
        for(Fire fire : this.fires)
            fire.close();
    }

    public void cleared(){
        if(!this.cleared){
            this.cleared = true;
            this.map.clearedCheck();
        }
    }

    // Getters
    public boolean isCleared(){
        return this.cleared;
    }

    public Door[] doors(){
        return this.doors;
    }    

    public int[] position(){
        return this.position;
    }

    public ArrayList<Rock> rocks(){
        return this.rocks;
    }

    public ArrayList<Fire> fires(){
        return this.fires;
    }

    public ArrayList<Item> items(){
        return this.items;
    }

    public TrapDoor trapDoor(){
        return this.trapDoor;
    }
}
