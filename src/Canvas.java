import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.Image;
import java.awt.Shape;
import java.awt.Graphics;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.TreeMap;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class Canvas {

    private static Canvas canvasSingleton;

    /**
     * Factory method to get the canvas singleton object.
     */
    public static Canvas getCanvas() {
        if (Canvas.canvasSingleton == null) {
            Canvas.canvasSingleton = new Canvas("The Burning Of Isaac", 800, 600, Color.white);
        }
        Canvas.canvasSingleton.setVisible(true);
        return Canvas.canvasSingleton;
    }

    //  ----- instance part -----

    private JFrame frame;
    private CanvasPane canvas;
    private Graphics2D graphic;
    private Color background;
    private Image canvasImage;
    private Timer timer;
    private List<Object> objects;
    private HashMap<Object, IDraw> shape;

    /**
     * Create a Canvas.
     * @param title  title to appear in Canvas Frame
     * @param width  the desired width for the canvas
     * @param height  the desired height for the canvas
     * @param bgClour  the desired background colour of the canvas
     */
    private Canvas(String title, int width, int height, Color background) {
        this.frame = new JFrame();
        this.canvas = new CanvasPane();
        this.frame.setContentPane(this.canvas);
        this.frame.setTitle(title);
        this.canvas.setPreferredSize(new Dimension(width, height));
        this.timer = new javax.swing.Timer(0, null);
        this.timer.start();
        this.background = background;
        this.frame.pack();
        this.objects = new ArrayList<Object>();
        this.shape = new HashMap<Object, IDraw>();
    }

    /**
     * Set the canvas visibility and brings canvas to the front of screen
     * when made visible. This method can also be used to bring an already
     * visible canvas to the front of other windows.
     * @param visible  boolean value representing the desired visibility of
     * the canvas (true or false) 
     */
    public void setVisible(boolean visible) {
        if (this.graphic == null) {
            // first time: instantiate the offscreen image and fill it with
            // the background colour
            Dimension size = this.canvas.getSize();
            this.canvasImage = this.canvas.createImage(size.width, size.height);
            this.graphic = (Graphics2D)this.canvasImage.getGraphics();
            this.graphic.setColor(this.background);
            this.graphic.fillRect(0, 0, size.width, size.height);
            this.graphic.setColor(Color.black);
            this.frame.setVisible(visible);
        }
    }

    /**
     * Draw a given image onto the canvas.
     * @param  referenceObject  an object to define identity for this image
     * @param  image            the image object to be drawn on the canvas
     * @param  transform        the transformation applied to the image
     */
    // Note: this is a slightly backwards way of maintaining the shape
    // objects. It is carefully designed to keep the visible shape interfaces
    // in this project clean and simple for educational purposes.
    public void draw(Object object, BufferedImage image, AffineTransform transform) {
        this.objects.remove(object);   // just in case it was already there
        this.objects.add(object);      // add at the end
        this.shape.put(object, new ImageDescription(image, transform));
    }

    /**
     * Erase a given shape's from the screen.
     * @param  referenceObject  the shape object to be erased 
     */
    public void erase(Object object) {
        this.objects.remove(object);   // just in case it was already there
        this.shape.remove(object);
    }

    /**
     * Set the foreground colour of the Canvas.
     * @param  newColour   the new colour for the foreground of the Canvas 
     */
    public void setForegroundColor(String color) {
        if (color.equals("red")) {
            this.graphic.setColor(Color.red);
        } else if (color.equals("black")) {
            this.graphic.setColor(Color.black);
        } else if (color.equals("blue")) {
            this.graphic.setColor(Color.blue);
        } else if (color.equals("yellow")) {
            this.graphic.setColor(Color.yellow);
        } else if (color.equals("green")) {
            this.graphic.setColor(Color.green);
        } else if (color.equals("magenta")) {
            this.graphic.setColor(Color.magenta);
        } else if (color.equals("white")) {
            this.graphic.setColor(Color.white);
        } else {
            this.graphic.setColor(Color.black);
        }
    }

    /**
     * * Redraw all shapes currently on the Canvas.
     */
    public void redraw() {
        this.erase();
        for (Object shape : this.objects ) {
            this.shape.get(shape).draw(this.graphic);
        }
        this.canvas.repaint();
    }

    /**
     * Erase the whole canvas. (Does not repaint.)
     */
    public void erase() {
        Color original = this.graphic.getColor();
        this.graphic.setColor(this.background);
        Dimension size = this.canvas.getSize();
        this.graphic.fill(new Rectangle(0, 0, size.width, size.height));
        this.graphic.setColor(original);
    }

    public void addKeyListener(KeyListener listener) {
        this.frame.addKeyListener(listener);
    }

    public void addMouseListener(MouseListener listener) {
        this.canvas.addMouseListener(listener);
    }

    public void addTimerListener(ActionListener listener) {
        this.timer.addActionListener(listener);
    }

    /************************************************************************
     * Inner class CanvasPane - the actual canvas component contained in the
     * Canvas frame. This is essentially a JPanel with added capability to
     * refresh the image drawn on it.
     */
    private class CanvasPane extends JPanel {
        public void paint(Graphics graphic) {
            graphic.drawImage(Canvas.this.canvasImage, 0, 0, null);
        }
    }

    /***********************************************************************
     * Inner interface IDraw - defines functions that need to be supported by
     * shapes descriptors
     */
    private interface IDraw {
        public void draw(Graphics2D graphic);
    }

    /************************************************************************
     * Inner class CanvasPane - the actual canvas component contained in the
     * Canvas frame. This is essentially a JPanel with added capability to
     * refresh the image drawn on it.
     */
    private class ShapeDescription implements IDraw{
        private Shape shape;
        private String color;

        public ShapeDescription(Shape shape, String color) {
            this.shape = shape;
            this.color = color;
        }

        public void draw(Graphics2D graphic) {
            Canvas.this.setForegroundColor(this.color);
            graphic.fill(this.shape);
        }
    }

    private class ImageDescription implements IDraw{
        private BufferedImage image;
        private AffineTransform transformation;

        public ImageDescription(BufferedImage image, AffineTransform transformation) {
            this.image = image;
            this.transformation = transformation;
        }

        public void draw(Graphics2D graphics) {
            graphic.drawImage(this.image, this.transformation, null); 
        }
    }
}
