import java.util.ArrayList;

public class Launcher {

    Canvas canvas = Canvas.getCanvas();
    private Game game;
    private Controls controls;
    private Image menu;
    private Button[] buttons;
    private int state;
    private int menuState;
    private Sound music;
    private boolean musicOn;
    private boolean soundOn;

    public Launcher(){
        this.controls = new Controls(this);
        this.menu = new Image("textures/menu.png");
        this.buttons = new Button[4];
        this.musicOn = false;
        this.soundOn = true;
        menuUpdate();
    }

    public void tick(){
        if(this.state == 1)
            this.game.tick();
        this.canvas.redraw();
    }

    public void click(int x, int y){
        switch(this.menuState){
            case 0:
            if(this.buttons[0].clicked(x, y))
                this.menuState = 1;
            else if(this.buttons[1].clicked(x, y)){}
            // Work in progress
            else if(this.buttons[2].clicked(x, y))
                this.menuState = 2;
            else if(this.buttons[3].clicked(x, y))
                System.exit(0);
            menuUpdate();
            break;
            case 1:
            if(this.buttons[0].clicked(x, y))
                newGame(0);
            else if(this.buttons[1].clicked(x, y))
                newGame(1);
            else if(this.buttons[2].clicked(x, y))
                newGame(2);
            else if(this.buttons[3].clicked(x, y))
                this.menuState = 0;
            menuUpdate();
            break;
            case 2:
            if(this.buttons[0].clicked(x, y)){
                if(this.soundOn)
                    this.soundOn = false;
                else
                    this.soundOn = true;
            }
            else if(this.buttons[1].clicked(x, y)){
                if(this.musicOn){
                    this.musicOn = false;
                    this.music.stop();
                    this.music = null;
                }
                else
                    this.musicOn = true;
            }
            else if(this.buttons[2].clicked(x, y))
                this.menuState = 0;
            menuUpdate();
            break;
        }
    }

    public void exit(){
        switch(this.menuState){
            case 0:
            System.exit(0);
            break;
            case 1:
            changeState(2);
            case 2:
            this.menuState = 0;
            break;
        }
        menuUpdate();
    }

    private void menuUpdate(){
        if(this.music == null && this.state == 0 && this.musicOn)
            this.music = new Sound("music/theme.wav", true);
        this.menu.hide();
        this.menu.show();
        for(int i = 0; i < this.buttons.length; i++){
            if(this.buttons[i] != null)
                this.buttons[i].hide();
            this.buttons[i] = null;
        }
        switch(this.menuState){
            case 0:
            newButton("textures/buttons/new_game.png", 0);
            newButton("textures/buttons/load.png", 1);
            newButton("textures/buttons/options.png", 2);
            newButton("textures/buttons/exit.png", 3);
            break;
            case 1:
            newButton("textures/buttons/easy.png", 0);
            newButton("textures/buttons/medium.png", 1);
            newButton("textures/buttons/hard.png", 2);
            newButton("textures/buttons/back.png", 3);
            break;
            case 2:
            if(this.soundOn)
                newButton("textures/buttons/sound_on.png", 0);
            else
                newButton("textures/buttons/sound_off.png", 0);
            if(this.musicOn)
                newButton("textures/buttons/music_on.png", 1);
            else
                newButton("textures/buttons/music_off.png", 1);
            newButton("textures/buttons/back.png", 2);
            break;
        }
    }

    private void newButton(String path, int slot){
        if(slot > -1 && slot < this.buttons.length)
            this.buttons[slot] = new Button(path, slot, 0, this);
    }

    public void newGame(int difficuilty){
        this.game = null;
        this.game = new Game(difficuilty, this);
        this.menuState = 0;
        changeState(1);
        if(this.music != null)
            this.music.stop();
    }

    public void changeState(int newState){
        this.state = newState;
        if(newState == 0)
            menuUpdate();
    }

    // Getters
    public Game game(){
        return this.game;
    }

    public int state(){
        return this.state;
    }

    public void soundSetNull(){
        this.music = null;
    }

    public boolean musicOn(){
        return this.musicOn;
    }

    public boolean soundOn(){
        return this.soundOn;
    }
}
