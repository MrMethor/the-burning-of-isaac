import java.util.ArrayList;
import java.util.Random;

public class AI {

    private Game game;
    private Map map;
    private Player player;
    private ArrayList<Enemy> enemies;
    private int spawnTimer;
    private int enemyAmount;

    public AI(Game game) {
        this.game = game;
        this.spawnTimer = 30;
        this.enemies = new ArrayList<Enemy>();
        this.enemyAmount = 0;
    }

    // Updates enemies
    public void update(){
        // Updates each enemy
        if(!this.enemies.isEmpty()){
            for(int i = 0; i < this.enemies.size(); i++){
                if(!this.enemies.get(i).dead())
                    this.enemies.get(i).update();
                else
                    this.enemies.remove(i);
            }
        }
        else 
            this.map.currentRoom().cleared();
    }

    public void spawnEnemy(double[] coords){
        this.enemies.add(new Enemy(coords, 1.5, this.player, this.map, this, this.game));
    }

    public void close(){
        for(Enemy enemy : this.enemies)
            enemy.close();
        this.enemies.clear();
    }

    // Additional references
    public void addReference(Map map, Player player){
        this.map = map;
        this.player = player;
    }

    //Getter
    public ArrayList<Enemy> enemies(){
        return this.enemies;
    }
}
