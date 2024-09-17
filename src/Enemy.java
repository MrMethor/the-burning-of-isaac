import java.lang.Math;
import java.util.Random;

public class Enemy {

    private Game game;
    private Map map;
    private Player player;
    private AI ai;
    private Image enemy;
    private double[] coords;
    private int[] velocity;
    private Hitbox hitbox;
    private boolean dead;
    private double speed;
    private double health;
    private int hitTimer;
    private double animationTimer;
    private int waitTime;
    private Random random;

    public Enemy(double[] coords, double speed, Player player, Map map, AI ai, Game game) {
        this.map = map;
        this.player = player;
        this.ai = ai;
        this.game = game;
        this.coords = new double[] {coords[0] + this.map.modifier()[0], coords[1] + this.map.modifier()[1]};
        this.velocity = new int[] {0, 0};
        this.hitbox = new Hitbox(33, 47, this.coords, false);

        this.enemy = new Image("textures/enemy/enemy_idle.png");
        this.enemy.changePosition(this.coords);
        this.enemy.show();
        this.speed = speed;
        this.animationTimer = 0;
        this.health = 20;
        this.hitTimer = 0;
        this.waitTime = 40;
        this.random = new Random();
    }

    public void update(){
        // Dead check
        if(this.health <= 0){
            this.dead = true;
            if(random.nextInt(5) == 0){
                if(random.nextInt(6) == 0)
                    this.map.currentRoom().spawnItem(2, this.coords);
                else{
                    if(random.nextInt(3) == 0)
                        this.map.currentRoom().spawnItem(0, this.coords);
                    else
                        this.map.currentRoom().spawnItem(1, this.coords);
                }
            }
            return;
        }

        // Follows the player
        if(this.waitTime == 0){
            double distanceX = this.player.coords()[0] - this.coords[0];
            double distanceY = this.player.coords()[1] - this.coords[1];
            if(distanceX == 0)
                distanceX = 0.00001;
            if(distanceY == 0)
                distanceY = 0.00001;
            double angle = Math.atan(Math.abs(distanceY) / Math.abs(distanceX));

            if(distanceX > 0 && collisionCheck(0))
                this.coords[0] += this.speed * Math.cos(angle) * (distanceX / Math.abs(distanceX));
            else if(distanceX < 0 && collisionCheck(1))
                this.coords[0] += this.speed * Math.cos(angle) * (distanceX / Math.abs(distanceX));
            if(distanceY > 0 && collisionCheck(2))
                this.coords[1] += this.speed * Math.sin(angle) * (distanceY / Math.abs(distanceY));
            else if(distanceY < 0 && collisionCheck(3))
                this.coords[1] += this.speed * Math.sin(angle) * (distanceY / Math.abs(distanceY));

            Side direction;
            if(Math.cos(angle) <= Math.sin(angle)){
                if(distanceY / Math.abs(distanceY) > 0)
                    direction = Side.UP;
                else
                    direction = Side.DOWN;
            }
            else{
                if(distanceX / Math.abs(distanceX) > 0)
                    direction = Side.RIGHT;
                else
                    direction = Side.LEFT;
            }
            animation(direction);
        }
        else{
            this.waitTime--;
            loadIdle();
        }

        // Converts velocity to movement output
        if(this.velocity[0] > 0){
            if(collisionCheck(0))
                this.coords[0] += this.velocity[0];
            this.velocity[0] -= 1;
        }
        else if(this.velocity[0] < 0){
            if(collisionCheck(1))
                this.coords[0] += this.velocity[0];
            this.velocity[0] += 1;
        }
        if(this.velocity[1] > 0){
            if(collisionCheck(2))
                this.coords[1] += this.velocity[1];
            this.velocity[1] -= 1;
        }
        else if(this.velocity[1] < 0){
            if(collisionCheck(3))
                this.coords[1] += this.velocity[1];
            this.velocity[1] += 1;
        }

        // Damage indicator
        if(this.hitTimer != 0){
            this.enemy = new Image("textures/enemy/enemy_damaged.png");
            this.hitTimer--;
        }

        // Update
        this.hitbox.update(this.coords);
        this.enemy.changePosition(this.coords);
        this.enemy.show();

        // Hit check
        for(Enemy enemy : this.ai.enemies()){
            char side = this.hitbox.hit(enemy.hitbox());
            if(enemy != this){
                if(side == 'R' || side == 'L')
                    enemy.hit(side, this.velocity[0]);
                if(side == 'U' || side == 'D')
                    enemy.hit(side, this.velocity[1]);
            }
        }
    }

    // Executes when hit
    public void hit(double damage, Side side){
        this.health -= damage;
        this.hitTimer = 5;
        switch(side){
            case RIGHT:
            this.velocity[0] += 8;
            break;
            case LEFT:
            this.velocity[0] -= 8;
            break;
            case UP:
            this.velocity[1] += 8;
            break;
            case DOWN:
            this.velocity[1] -= 8;
            break;
        }
        if(this.game.launcher().soundOn())
            new Sound("sounds/hit.wav", false);
    }

    public void hit(char side, int velocity){
        switch(side){
            case 'R':
            this.velocity[0] -= (Math.abs(velocity) / 5) + 1;
            break;
            case 'L':
            this.velocity[0] += (Math.abs(velocity) / 5) + 1;
            break;
            case 'U':
            this.velocity[1] -= (Math.abs(velocity) / 5) + 1;
            break;
            case 'D':
            this.velocity[1] += (Math.abs(velocity) / 5) + 1;
            break;
        }
    }

    // Collision check
    private boolean collisionCheck(int side){
        if(side == 0 && this.hitbox.side(0) >= this.map.walls()[0])
            return false;
        else if(side == 1 && this.hitbox.side(1) <= this.map.walls()[1])
            return false;
        if(side == 2 && this.hitbox.side(2) >= this.map.walls()[2])
            return false;
        else if(side == 3 && this.hitbox.side(3) <= this.map.walls()[3])
            return false;
        for(Rock rock : this.map.currentRoom().rocks()){
            if(side == 0 && this.hitbox.hit(rock.hitbox()) == 'L')
                return false;
            else if(side == 1 && this.hitbox.hit(rock.hitbox()) == 'R')
                return false;
            if(side == 2 && this.hitbox.hit(rock.hitbox()) == 'D')
                return false;
            else if(side == 3 && this.hitbox.hit(rock.hitbox()) == 'U')
                return false;
        }
        return true;
    }

    public void close(){
        this.enemy.hide();
    }

    // Animations
    private void animation(Side direction){
        if(direction != null)
            animationCycle(direction.name());
        else
            loadIdle();
    }

    private void animationCycle(String directionS){
        if(this.animationTimer < 15)
            loadDirection(directionS, 1);
        else if(this.animationTimer < 30)
            loadDirection(directionS, 2);
        else if(this.animationTimer < 45)
            loadDirection(directionS, 3);
        else if(this.animationTimer < 60)
            loadDirection(directionS, 2);
        else
            this.animationTimer = 0;
    }

    private void loadIdle(){
        this.enemy.hide();
        this.enemy = new Image("textures/enemy/enemy_idle.png");
        this.enemy.changePosition(this.coords);
    }

    private void loadDirection(String directionS, int num){
        this.enemy.hide();
        this.enemy = new Image("textures/enemy/enemy_" + directionS + num +".png");
        this.enemy.changePosition(this.coords);
        this.animationTimer += this.speed / 2;
    }

    // Getters
    public boolean dead(){
        this.enemy.hide();
        return this.dead;
    }

    public Hitbox hitbox(){
        return this.hitbox;
    }
}
 