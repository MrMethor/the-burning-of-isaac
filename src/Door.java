public class Door {

    private Image door;
    private double[] coords;
    private Hitbox hitbox;

    public Door(int side, Map map) {
        this.door = new Image("textures/door" + side + ".png");
        switch(side){
            case 0:
            this.door.changePosition(this.coords = new double[] {350 + map.modifier()[0] , 0 + map.modifier()[1]});
            break;
            case 1:
            this.door.changePosition(this.coords = new double[] {-350 + map.modifier()[0] , 0 + map.modifier()[1]});
            break;
            case 2:
            this.door.changePosition(this.coords = new double[] {0 + map.modifier()[0] , 200 + map.modifier()[1]});
            break;
            case 3:
            this.door.changePosition(this.coords = new double[] {0 + map.modifier()[0] , -200 + map.modifier()[1]});
            break;
        }
        this.hitbox = new Hitbox(80, 80, this.coords, false);
    }

    public void show(){
        this.door.show();
    }

    public void close(){
        this.door.hide();
    }

    public Hitbox hitbox(){
        return this.hitbox;
    }
}
