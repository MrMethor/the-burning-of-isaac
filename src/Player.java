import java.util.ArrayList;
import java.lang.Math;
import java.util.Random;

public class Player {

    private Map map;
    private Game game;
    private AI ai;
    private HUD hud;
    private Image character;
    private double[] coords;
    private int[] velocity;
    private Hitbox hitbox;
    private double speed;
    private boolean[] moving;
    private double animationTimer;
    private int soundTimer;
    private int health;
    private int maxHealth;
    private int gracePeriod;
    private boolean hudUpdated;

    private ArrayList<Side> shootingDirection;
    private double fireDamage;
    private double fireRate;
    private int fireRange;
    private double fireSpeed;
    private int fireRefresh;
    private ArrayList<Fireball> fireballs;
    private Random random;

    // Player inicialization
    public Player(double speed, double fireDamage, double fireRate, int fireRange, double fireSpeed, int health, Map map, AI ai, Game game) {
        this.game = game;
        this.map = map;
        this.ai = ai;
        this.coords = new double[] {this.map.modifier()[0], this.map.modifier()[1]};
        this.velocity = new int[] {0, 0};
        this.hitbox = new Hitbox (28, 45, this.coords, true);
        this.character = new Image("textures/character/character_idle.png");
        this.character.changePosition(this.coords);
        this.moving = new boolean[] {false, false, false, false};
        this.speed = speed;
        this.animationTimer = 0;
        this.soundTimer = 0;
        this.health = health;
        this.maxHealth = 6;
        this.gracePeriod = 0;
        this.hudUpdated = false;

        this.shootingDirection = new ArrayList<Side>(4);
        this.fireDamage = fireDamage;
        this.fireRate = fireRate;
        this.fireRange = fireRange;
        this.fireSpeed = fireSpeed;
        this.fireRefresh = 0;
        this.fireballs = new ArrayList<Fireball>();
        this.hud = new HUD(this.map);
        this.random = new Random();
    }

    // Player update
    public void update(){
        // Movement input
        if(this.moving[Side.RIGHT.number()]){
            if(this.velocity[0] < 10)
                this.velocity[0] += 2;
            animation(Side.RIGHT);
        }
        if(this.moving[Side.LEFT.number()]){
            if(this.velocity[0] > -10)
                this.velocity[0] -= 2;
            animation(Side.LEFT);
        }
        if(this.moving[Side.UP.number()]){
            if(this.velocity[1] < 10)
                this.velocity[1] += 2;
            animation(Side.UP);
        }
        if(this.moving[Side.DOWN.number()]){
            if(this.velocity[1] > -10)
                this.velocity[1] -= 2;
            animation(Side.DOWN);
        }

        // Converts velocity to movement output
        if(this.velocity[0] > 0){
            collisionCheck(0);
            this.velocity[0] -= 1;
        }
        else if(this.velocity[0] < 0){
            collisionCheck(1);
            this.velocity[0] += 1;
        }
        if(this.velocity[1] > 0){
            collisionCheck(2);
            this.velocity[1] -= 1;
        }
        else if(this.velocity[1] < 0){
            collisionCheck(3);
            this.velocity[1] += 1;
        }

        // Update
        this.hitbox.update(this.coords);
        this.character.changePosition(this.coords);
        if(this.gracePeriod < 1)
            this.character.show();

        // Hit
        if(!this.ai.enemies().isEmpty()){
            for(Enemy enemy : this.ai.enemies()){
                char side = this.hitbox.hit(enemy.hitbox());
                if(side != 'n'){
                    hit(side, 2);
                    break;
                }
            }
        }
        if(this.gracePeriod > 0){
            if(this.gracePeriod % 6 == 0)
                this.character.hide();
            else
                this.character.show();
            this.gracePeriod--;
        }

        // Shooting
        if(!this.shootingDirection.isEmpty()){
            if(this.fireRefresh < 1){
                this.fireballs.add(new Fireball(this.coords, this.shootingDirection.get(0), this.fireDamage, this.fireRange, this.fireSpeed, this.map, this.ai));
                this.fireRefresh = 100;
                if(this.game.launcher().soundOn())
                    new Sound("sounds/fireball.wav", false);
            }
        }
        if(this.fireRefresh > 1)
            this.fireRefresh -= this.fireRate;
        for(int i = 0; i < this.fireballs.size(); i++){
            if(!this.fireballs.get(i).delete())
                this.fireballs.get(i).update();
            else
                this.fireballs.remove(i);
        }
        this.hud.upgradeTimer();
        if(!this.hudUpdated){
            this.hud.healthUpdate(this.health);
            this.hud.minimapUpdate();
            this.hudUpdated = true;
        }

        // Dead check
        if(this.health < 1)
            this.game.deathScreen().show();
    }

    // Movement commands received from the Game instance
    public void move(Side direction){
        this.moving[direction.number()] = true;
    }

    public void stopMove(Side direction){
        this.moving[direction.number()] = false;
        resetAnimation();
    }

    public void changePosition(int x, int y){
        this.coords = new double[] {x + this.map.modifier()[0], y + this.map.modifier()[1]};
        this.hitbox.changeCoords(this.coords);
    }

    // Shooting commands recieved from the Controls instance
    public void shoot(Side direction){
        if(!this.shootingDirection.contains(direction))
            this.shootingDirection.add(direction);
    }

    public void stopShoot(Side direction){
        this.shootingDirection.remove(direction);
    }

    // Open door
    public void open(){
        if(this.map.currentRoom().trapDoor() != null && this.hitbox.hit(this.map.currentRoom().trapDoor().hitbox()) != 'n'){
            this.game.newMap();
            return;
        }
        for(int i = 0; i < 4; i++){
            if(this.map.currentRoom().doors()[i] != null && this.hitbox.hit(this.map.currentRoom().doors()[i].hitbox()) != 'n'){
                this.game.changeRoom(i);
                return;
            }
        }
    }

    // Moves the player to a different room
    public void changeRoom(int side){
        if(!this.fireballs.isEmpty()){
            for(Fireball fireball : this.fireballs)
                fireball.close();
        }
        switch(side){
            case 0:
            changePosition(-300, 0);
            break;
            case 1:
            changePosition(300, 0);
            break;
            case 2:
            changePosition(0, -150);
            break;
            case 3:
            changePosition(0, 150);
            break;
        }
        this.hudUpdated = false;
    }

    // Hit
    private void hit(char side, int knockback){
        if(this.gracePeriod < 1){
            this.health--;
            this.gracePeriod = 80;
            this.hudUpdated = false;
        }
        switch(side){
            case 'R':
            this.velocity[0] += knockback;
            break;
            case 'L':
            this.velocity[0] -= knockback;
            break;
            case 'U':
            this.velocity[1] += knockback;
            break;
            case 'D':
            this.velocity[1] -= knockback;
            break;
        }
    }

    private boolean[] collisionCheck(){
        boolean[] check = new boolean[]{false, false, false, false};
        if(this.hitbox.side(0) >= this.map.walls()[0])
            check[Side.RIGHT.number()] = true;
        else if(this.hitbox.side(1) <= this.map.walls()[1])
            check[Side.LEFT.number()] = true;
        if(this.hitbox.side(2) >= this.map.walls()[2])
            check[Side.UP.number()] = true;
        else if(this.hitbox.side(3) <= this.map.walls()[3])
            check[Side.DOWN.number()] = true;
        for(Rock rock : this.map.currentRoom().rocks()){
            if(this.hitbox.hit(rock.hitbox()) == 'L')
                check[Side.RIGHT.number()] = true;
            else if(this.hitbox.hit(rock.hitbox()) == 'R')
                check[Side.LEFT.number()] = true;
            if(this.hitbox.hit(rock.hitbox()) == 'D')
                check[Side.UP.number()] = true;
            else if(this.hitbox.hit(rock.hitbox()) == 'U')
                check[Side.DOWN.number()] = true;
        }
        for(Fire fire : this.map.currentRoom().fires()){
            if(fire.isBurning()){
                if(this.hitbox.hit(fire.hitbox()) == 'L'){
                    hit('L', 20);
                    check[Side.RIGHT.number()] = true;
                }
                else if(this.hitbox.hit(fire.hitbox()) == 'R'){
                    hit('R', 20);
                    check[Side.LEFT.number()] = true;
                }
                if(this.hitbox.hit(fire.hitbox()) == 'D'){
                    hit('D', 20);
                    check[Side.UP.number()] = true;
                }
                else if(this.hitbox.hit(fire.hitbox()) == 'U'){
                    hit('U', 20);
                    check[Side.DOWN.number()] = true;
                }
            }
        }
        return check;
    }
    
    // Collision check
    private void collisionCheck(int side){
        if(!this.map.currentRoom().items().isEmpty()){
            for(int i = 0; i < this.map.currentRoom().items().size(); i++){
                boolean removeItem;
                if(this.hitbox.hit(this.map.currentRoom().items().get(i).hitbox()) != 'n'){
                    removeItem = true;
                    switch(this.map.currentRoom().items().get(i).id()){
                        case 0:
                        if(this.health < this.maxHealth){
                            if(this.health == this.maxHealth - 1)
                                this.health ++;
                            else
                                this.health += 2;
                        }
                        else
                            removeItem = false;
                        break;
                        case 1:
                        if(this.health < this.maxHealth)
                            this.health++;
                        else
                            removeItem = false;
                        break;
                        case 2:
                        int effect = this.random.nextInt(6);
                        switch(effect){
                            case 0:
                            this.maxHealth += 2;
                            this.hud.upgradeUpdate("health");
                            break;
                            case 1:
                            this.speed += 0.5;
                            this.hud.upgradeUpdate("speed");
                            break;
                            case 2:
                            this.fireRange += 10;
                            this.hud.upgradeUpdate("range");
                            break;
                            case 3:
                            this.fireRate += 0.5;
                            this.hud.upgradeUpdate("fireRate");
                            break;
                            case 4:
                            this.fireSpeed++;
                            this.hud.upgradeUpdate("shotSpeed");
                            break;
                            case 5:
                            this.fireDamage += 0.5;
                            this.hud.upgradeUpdate("damage");
                            break;
                        }
                        if(this.game.launcher().soundOn())
                            new Sound("sounds/power_up.wav", false);
                        break;
                    }
                    if(removeItem){
                        this.hudUpdated = false;
                        this.map.currentRoom().items().get(i).close();
                        this.map.currentRoom().items().remove(i);
                    }
                }
            }
        }
        if(side == 0 && this.hitbox.side(0) >= this.map.walls()[0])
            return;
        else if(side == 1 && this.hitbox.side(1) <= this.map.walls()[1])
            return;
        if(side == 2 && this.hitbox.side(2) >= this.map.walls()[2])
            return;
        else if(side == 3 && this.hitbox.side(3) <= this.map.walls()[3])
            return;
        for(Rock rock : this.map.currentRoom().rocks()){
            if(side == 0 && this.hitbox.hit(rock.hitbox()) == 'L')
                return;
            else if(side == 1 && this.hitbox.hit(rock.hitbox()) == 'R')
                return;
            if(side == 2 && this.hitbox.hit(rock.hitbox()) == 'D')
                return;
            else if(side == 3 && this.hitbox.hit(rock.hitbox()) == 'U')
                return;
        }
        for(Fire fire : this.map.currentRoom().fires()){
            if(fire.isBurning()){
                if(side == 0 && this.hitbox.hit(fire.hitbox()) == 'L'){
                    hit('L', 20);
                    return;
                }
                else if(side == 1 && this.hitbox.hit(fire.hitbox()) == 'R'){
                    hit('R', 20);
                    return;
                }
                if(side == 2 && this.hitbox.hit(fire.hitbox()) == 'D'){
                    hit('D', 20);
                    return;
                }
                else if(side == 3 && this.hitbox.hit(fire.hitbox()) == 'U'){
                    hit('U', 20);
                    return;
                }
            }
        }
        int axis = 0;
        if(side > 1)
            axis = 1;
        this.coords[axis] += this.velocity[axis] * this.speed / 10;
    }

    // Movement reset
    public void reset(){
        for(int i = 0; i < this.moving.length; i++){
            //this.moving[i] = false;
            loadIdle();
        }
        for(int i = 0; i < this.shootingDirection.size(); i++)
            this.shootingDirection.remove(0);
    }

    public void close(){
        this.character.hide();
        for(Fireball fireball : this.fireballs)
            fireball.close();
        this.hud.close();
    }

    // Animations
    private void animation(Side direction){
        switch(direction){
            case RIGHT:
            if(this.moving[Side.RIGHT.number()] && !this.moving[Side.LEFT.number()] && !this.moving[Side.UP.number()] && !this.moving[Side.DOWN.number()])
                animationCycle("right");
            else if(this.moving[Side.RIGHT.number()] && this.moving[Side.LEFT.number()])
                loadIdle();
            break;
            case LEFT:
            if(!this.moving[Side.RIGHT.number()] && this.moving[Side.LEFT.number()] && !this.moving[Side.UP.number()] && !this.moving[Side.DOWN.number()])
                animationCycle("left");
            else if(this.moving[Side.RIGHT.number()] && this.moving[Side.LEFT.number()])
                loadIdle();
            break;
            case UP:
            if(this.moving[Side.UP.number()] && !this.moving[Side.DOWN.number()])
                animationCycle("back");
            else if(this.moving[Side.UP.number()] && this.moving[Side.DOWN.number()])
                loadIdle();
            break;
            case DOWN:
            if(!this.moving[Side.UP.number()] && this.moving[Side.DOWN.number()])
                animationCycle("front");
            else if(this.moving[Side.UP.number()] && this.moving[Side.DOWN.number()])
                loadIdle();
            break;
        }
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
        switch(this.soundTimer){
            case 0:
            if(this.game.launcher().soundOn())
                new Sound("sounds/step_left.wav", false);
            break;
            case 20:
            if(this.game.launcher().soundOn())
                new Sound("sounds/step_right.wav", false);
            break;
            case 40:
            this.soundTimer = 0;
            break;
        }
        this.soundTimer++;
    }

    private void loadIdle(){
        this.character.hide();
        this.character = new Image("textures/character/character_idle.png");
        this.character.changePosition(this.coords);
    }

    private void resetAnimation(){
        loadIdle();
        this.animationTimer = 0;
    }

    private void loadDirection(String directionS, int num){
        this.character.hide();
        this.character = new Image("textures/character/character_" + directionS + num +".png");
        this.character.changePosition(this.coords);
        this.animationTimer += this.speed / 2;
    }

    // Reference
    public void newMap(Map map){
        this.map = null;
        this.map = map;
        this.changePosition(0, 0);
        this.hud.newMap(this.map);
        this.hudUpdated = false;
    }

    // Getters
    public double[] coords(){
        return this.coords;
    }

    public int health(){
        return this.health;
    }
}