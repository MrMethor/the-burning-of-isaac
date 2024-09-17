public class Game {

    private Launcher launcher;
    private Sound music;
    private Pause pause;
    private Map map;
    private DeathScreen deathScreen;
    private Player player;
    private AI ai;
    private int difficuilty;
    private int resetTimer;
    private boolean reset;

    // Game inicialization
    public Game(int difficulty, Launcher launcher){
        this.launcher = launcher;
        this.pause = new Pause(this);
        this.ai = new AI(this);
        this.map = new Map(this, this.ai);
        // Speed Damage FireRate FireRange FireSpeed Health
        this.player = new Player(3, 3.5, 5, 70, 5, 6, this.map, this.ai, this);
        this.ai.addReference(this.map, this.player);

        this.deathScreen = new DeathScreen(this);
        this.reset = false;
        this.difficuilty = difficuilty;
        if(this.launcher.musicOn())
            this.music = new Sound("music/combat.wav", true);
    }

    // Refresh rate
    public void tick(){
        this.map.update();
        this.ai.update();
        this.player.update();
        reset();
    }
    
    // Executes when the player moves to a different room
    public void changeRoom(int side){
        this.ai.close();
        this.map.changeRoom(side);
        this.player.changeRoom(side);
    }

    // Changes the game state
    public void changeState(int newState){
        this.launcher.changeState(newState);
    }

    // Resets the game
    public void newGame(){
        close();
        this.launcher.newGame(this.difficuilty);
    }

    public void newMap(){
        this.ai.close();
        this.map.close();
        this.map = null;
        this.map = new Map(this, this.ai);
        this.player.newMap(this.map);
        this.ai.addReference(this.map, this.player);
        if(this.launcher.musicOn())
            this.music = new Sound("music/combat.wav", true);
    }

    private void reset(){
        if(this.reset){
            if(this.resetTimer > 100)
                newGame();
            this.resetTimer++;
        }
    }

    public void resetStart(){
        this.reset = true;
    }

    public void resetStop(){
        this.reset = false;
        this.resetTimer = 0;
    }

    public void stopMusic(){
        if(this.music != null){
            this.music.stop();
            this.music = null;
        }
    }

    // Erases all images (Almost all of 'em)
    private void close(){
        this.map.close();
        this.ai.close();
        this.player.close();
        if(this.music != null){
            this.music.stop();
            this.music = null;
        }
    }

    // Getters
    public Player player(){
        return this.player;
    }

    public Launcher launcher(){
        return this.launcher;
    }

    public Pause pause(){
        return this.pause;
    }

    public DeathScreen deathScreen(){
        return this.deathScreen;
    }

    public Sound music(){
        return this.music;
    }
}
