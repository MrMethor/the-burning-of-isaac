public class Controls {

    private Launcher launcher;
    private Manager manager;

    // Game inicialization
    public Controls(Launcher launcher){
        this.launcher = launcher;
        this.manager = new Manager();
        this.manager.manageObject(this);
    }

    // Refresh rate
    public void tick(){
        this.launcher.tick();
    }

    // Controls
    public void escape(){
        switch(this.launcher.state()){
            case 0:
            this.launcher.exit();
            break;
            case 1:
            this.launcher.game().pause().show();
            break;
            case 2:
            this.launcher.game().pause().close();
            break;
            case 3:
            this.launcher.game().deathScreen().close();
            break;
        }
    }

    public void click(int x, int y){
        x -= 400;
        y = -y + 300;
        switch(this.launcher.state()){
            case 0:
            this.launcher.click(x, y);
            break;
            case 2:
            this.launcher.game().pause().click(x, y);
            break;
            case 3:
            this.launcher.game().deathScreen().click(x, y);
            break;
        }
    }

    public void e(){
        if(this.launcher.state() == 1)
            this.launcher.game().player().open();
    }

    public void r(){
        if(this.launcher.state() == 1)
            this.launcher.game().resetStart();
    }

    public void r_release(){
        if(this.launcher.state() == 1)
            this.launcher.game().resetStop();
    }

    // Moving
    public void d(){
        if(this.launcher.state() == 1)
            this.launcher.game().player().move(Side.RIGHT);
    }

    public void a(){
        if(this.launcher.state() == 1)
            this.launcher.game().player().move(Side.LEFT);
    }

    public void w(){
        if(this.launcher.state() == 1)
            this.launcher.game().player().move(Side.UP);
    }

    public void s(){
        if(this.launcher.state() == 1)
            this.launcher.game().player().move(Side.DOWN);
    }

    public void d_release(){
        if(this.launcher.state() == 1)
            this.launcher.game().player().stopMove(Side.RIGHT);
    }

    public void a_release(){
        if(this.launcher.state() == 1)
            this.launcher.game().player().stopMove(Side.LEFT);
    }

    public void w_release(){
        if(this.launcher.state() == 1)
            this.launcher.game().player().stopMove(Side.UP);
    }

    public void s_release(){
        if(this.launcher.state() == 1)
            this.launcher.game().player().stopMove(Side.DOWN);
    }

    // Shooting
    public void right(){
        if(this.launcher.state() == 1)
            this.launcher.game().player().shoot(Side.RIGHT);
    }

    public void left(){
        if(this.launcher.state() == 1)
            this.launcher.game().player().shoot(Side.LEFT);
    }

    public void up(){
        if(this.launcher.state() == 1)
            this.launcher.game().player().shoot(Side.UP);
    }

    public void down(){
        if(this.launcher.state() == 1)
            this.launcher.game().player().shoot(Side.DOWN);
    }

    public void right_release(){
        if(this.launcher.state() == 1)
            this.launcher.game().player().stopShoot(Side.RIGHT);
    }

    public void left_release(){
        if(this.launcher.state() == 1)
            this.launcher.game().player().stopShoot(Side.LEFT);
    }

    public void up_release(){
        if(this.launcher.state() == 1)
            this.launcher.game().player().stopShoot(Side.UP);
    }

    public void down_release(){
        if(this.launcher.state() == 1)
            this.launcher.game().player().stopShoot(Side.DOWN);
    }
}