public class Fire {

    private Image fire;
    private double[] coords;
    private Hitbox hitbox;
    private int animationTimer;
    private int health;
    private boolean isBurning;

    public Fire(int x, int y) {
        this.coords = new double[] {x, y};
        this.fire = new Image("textures/fire/fire1.png");
        this.fire.changePosition(this.coords);
        this.hitbox = new Hitbox(25, 25, this.coords, false);
        this.animationTimer = 0;
        this.health = 10;
        this.isBurning = true;
    }

    public void update(){
        if(this.isBurning)
            animationCycle();
        else
            loadAnimation(0);
    }

    public void hit(double damage){
        this.health -= damage;
        if(this.health <= 0)
            this.isBurning = false;
    }

    public void close(){
        this.fire.hide();
    }

    public Hitbox hitbox(){
        return this.hitbox;
    }

    public boolean isBurning(){
        return this.isBurning;
    }

    private void animationCycle(){
        if(this.animationTimer < 15)
            loadAnimation(1);
        else if(this.animationTimer < 30)
            loadAnimation(2);
        else if(this.animationTimer < 45)
            loadAnimation(3);
        else if(this.animationTimer < 60)
            loadAnimation(4);
        else
            this.animationTimer = 0;
    }

    private void loadAnimation(int num){
        this.fire.hide();
        this.fire = new Image("textures/fire/fire" + num +".png");
        this.fire.changePosition(this.coords);
        this.animationTimer++;
        this.fire.show();
    }
}
