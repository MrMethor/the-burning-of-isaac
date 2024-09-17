public class Button {

    private Launcher launcher;
    private Image button;
    private double[] coords;
    private int width;
    private int height;

    public Button(String path, int slot, int type, Launcher launcher) {
        this.launcher = launcher;
        switch(type){
            case 0:
            this.coords = new double[] {-220, (slot * -80) + 120};
            break;
            case 1:
            this.coords = new double[] {0, (slot * -80) + 80};
            break;
            case 2:
            this.coords = new double[] {0, (slot * -80) - 40};
            break;
        }
        this.width = 245;
        this.height = 56;

        this.button = new Image(path);
        this.button.changePosition(this.coords);
        this.button.show();
    }

    public boolean clicked(int x, int y){
        if((x > (int)this.coords[0] - (this.width / 2) && x < (int)this.coords[0] + (this.width / 2))
        && (y > (int)this.coords[1] - (this.height / 2) && y < (int)this.coords[1] + (this.height / 2))){
            if(this.launcher.soundOn())
                new Sound("sounds/click.wav", false);
            return true;
        }
        else
            return false;
    }

    public void hide(){
        this.button.hide();
    }
}
