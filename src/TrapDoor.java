public class TrapDoor {

    private Image trapDoor;
    private double[] coords;
    private Hitbox hitbox;

    public TrapDoor(int x, int y) {
        this.coords = new double[] {x, y};
        this.trapDoor = new Image("textures/trapdoor.png");
        this.trapDoor.changePosition(this.coords);
        this.hitbox = new Hitbox(50, 50, this.coords, false);
    }

    public void show(){
        this.trapDoor.show();
    }

    public void close(){
        this.trapDoor.hide();
    }

    public Hitbox hitbox(){
        return this.hitbox;
    }
}
