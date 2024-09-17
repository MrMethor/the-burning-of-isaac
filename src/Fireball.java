public class Fireball {

    private Map map;
    private AI ai;
    private Image fireball;
    private double[] coords;
    private Hitbox hitbox;
    private int[] walls;

    private Side direction;
    private double range;
    private double speed;
    private double damage;
    private boolean deleted;

    public Fireball(double[] coords, Side direction, double damage, double range, double speed, Map map, AI ai) {
        this.map = map;
        this.ai = ai;
        this.coords = new double[] {coords[0], coords[1]};
        this.fireball = new Image("textures/fireball.png");
        this.fireball.changePosition(this.coords);
        this.hitbox = new Hitbox(20, 20, this.coords, false);

        this.damage = damage;
        this.direction = direction;
        this.range = range;
        this.speed = speed;
        this.deleted = false;
    }

    // Updates the fireball
    public void update(){
        // Moves the fireball depending on the inputs
        switch(this.direction){
            case RIGHT:
            if(this.coords[0] < this.map.walls()[0] && this.range > 0){
                this.coords[0] += this.speed;
                this.range--;
            }
            else
                this.deleted = true;
            break;
            case LEFT:
            if(this.coords[0] > this.map.walls()[1] && this.range > 0){
                this.coords[0] -= this.speed;
                this.range--;
            }
            else
                this.deleted = true;
            break;
            case UP:
            if(this.coords[1] < this.map.walls()[2] && this.range > 0){
                this.coords[1] += this.speed;
                this.range--;
            }
            else
                this.deleted = true;
            break;
            case DOWN:
            if(this.coords[1] > this.map.walls()[3] && this.range > 0){
                this.coords[1] -= this.speed;
                this.range--;
            }
            else
                this.deleted = true;
            break;
        }
        this.hitbox.update(this.coords);
        this.fireball.changePosition(this.coords);
        this.fireball.show();

        // Hit check
        for(Enemy enemy : this.ai.enemies()){
            char side = this.hitbox.hit(enemy.hitbox());
            if(side != 'n'){
                enemy.hit(this.damage, this.direction);
                this.deleted = true;
                break;
            }
        }
        for(Rock rock : this.map.currentRoom().rocks()){
            char side = this.hitbox.hit(rock.hitbox());
            if(side != 'n'){
                this.deleted = true;
                break;
            }
        }
        for(Fire fire : this.map.currentRoom().fires()){
            if(fire.isBurning()){
                char side = this.hitbox.hit(fire.hitbox());
                if(side != 'n'){
                    fire.hit(this.damage);
                    this.deleted = true;
                    break;
                }
            }
        }
    }

    public void close(){
        this.deleted = true;
    }

    // Getter
    public boolean delete(){
        this.fireball.hide();
        return this.deleted;
    }
}
