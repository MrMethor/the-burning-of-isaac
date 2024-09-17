import java.util.Random;

public class Map {

    private Game game;
    private AI ai;
    private int[] modifier;
    private int[] walls;

    private Room[][] rooms;
    private int numberOfRooms;
    private int roomSetter;
    private Room currentRoom;
    private boolean isCleared;

    public Map(Game game, AI ai){
        this.ai = ai;
        this.isCleared = false;
        int roomWidth = 650;
        int roomHeight = 350;
        this.modifier = new int[] {0, -50};
        this.walls = new int[4];
        this.walls[0] = (roomWidth / 2) + modifier[0];
        this.walls[1] = -(roomWidth / 2) + modifier[0];
        this.walls[2] = (roomHeight / 2) + modifier[1];
        this.walls[3] = -(roomHeight / 2) + modifier[1];

        this.game = game;
        this.roomSetter = 5;
        Random number = new Random();
        this.numberOfRooms = (this.roomSetter * 2) + (number.nextInt(this.roomSetter) - (this.roomSetter / 2));

        this.rooms = new Room[this.roomSetter][this.roomSetter];
        this.rooms[this.roomSetter / 2][this.roomSetter / 2] = new Room(0, new int[] {this.roomSetter / 2, this.roomSetter / 2}, this, this.ai);
        this.currentRoom = this.rooms[this.roomSetter / 2][this.roomSetter / 2];
        int roomCounter = 0;
        while(roomCounter < this.numberOfRooms){
            for(int i = 0; i < this.rooms.length; i++){
                for(int j = 0; j < this.rooms[0].length; j++){
                    if(this.rooms[i][j] == null){
                        boolean inicialise = false;
                        if(i != 0 && this.rooms[i - 1][j] != null)
                            inicialise = true;
                        if(i != this.rooms.length - 1 && this.rooms[i + 1][j] != null)
                            inicialise = true;
                        if(j != 0 && this.rooms[i][j - 1] != null)
                            inicialise = true;
                        if(j != this.rooms[0].length - 1 && this.rooms[i][j + 1] != null)
                            inicialise = true;
                        if(inicialise){
                            int generationChance = number.nextInt(10);
                            if(generationChance > 1)
                                continue;
                            int roomType = number.nextInt(20) + 1;
                            this.rooms[i][j] = new Room(roomType, new int[] {i, j}, this, this.ai);
                            roomCounter++;
                        }
                    }
                }
            }
        }
        for(int i = 0; i < this.rooms.length; i++){
            for(int j = 0; j < this.rooms[0].length; j++){
                if(this.rooms[i][j] != null)
                    this.rooms[i][j].inicializeConnection();
            }
        }
    }

    public void changeRoom(int side){
        this.currentRoom.close();
        switch(side){
            case 0:
            this.currentRoom = this.rooms[this.currentRoom.position()[0]][this.currentRoom.position()[1] + 1];
            break;
            case 1:
            this.currentRoom = this.rooms[this.currentRoom.position()[0]][this.currentRoom.position()[1] - 1];
            break;
            case 2:
            this.currentRoom = this.rooms[this.currentRoom.position()[0] - 1][this.currentRoom.position()[1]];
            break;
            case 3:
            this.currentRoom = this.rooms[this.currentRoom.position()[0] + 1][this.currentRoom.position()[1]];
            break;
        }
        update();
    }

    public void update(){
        this.currentRoom.update();
    } 

    public void close(){
        for(int i = 0; i < this.rooms.length; i++){
            for(int j = 0; j < this.rooms[0].length; j++){
                if(this.rooms[i][j] != null)
                    this.rooms[i][j].close();
            }
        }
    }

    public void clearedCheck(){
        boolean cleared = true;
        for(int i = 0; i < this.rooms.length; i++){
            for(int j = 0; j < this.rooms[0].length; j++){
                if(this.rooms[i][j] != null && !this.rooms[i][j].isCleared())
                    cleared = false;
            }
        }
        if(cleared){
            this.currentRoom.spawnTrapDoor();
            this.currentRoom.trapDoor().show();
            this.isCleared = true;
            this.game.stopMusic();
            if(this.game.launcher().soundOn())
                new Sound("sounds/victory.wav", false);
        }
    }

    // Getters
    public int[] modifier(){
        return this.modifier;
    }

    public int[] walls(){
        return this.walls;
    }

    public Room currentRoom(){
        return this.currentRoom;
    }

    public Room[][] rooms(){
        return this.rooms;
    }

    public boolean isCleared(){
        return this.isCleared;
    }
}
