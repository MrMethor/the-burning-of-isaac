public class Rock {

    private Image rock;
    private double[] coords;
    private Hitbox hitbox;

    public Rock(int x, int y) {
        this.coords = new double[] {x, y};
        this.rock = new Image("textures/rock.png");
        this.rock.changePosition(this.coords);
        this.hitbox = new Hitbox(50, 50, this.coords, false);
    }

    public void show(){
        this.rock.show();
    }

    public void close(){
        this.rock.hide();
    }

    public Hitbox hitbox(){
        return this.hitbox;
    }
}
