import java.util.ArrayList;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;
import java.util.Collections;

public class Manager {

    private ArrayList<Object> managedObjects;
    private ArrayList<Integer> deletedObjects;
    private long oldTick;
    private static final long TICK_LENGTH = 15000000;

    private class KeyManager extends KeyAdapter {
        public void keyPressed(KeyEvent event) {
            if (event.getKeyCode() == KeyEvent.VK_D) {
                Manager.this.sendMessage("d");
            } else if (event.getKeyCode() == KeyEvent.VK_A) {
                Manager.this.sendMessage("a");
            } else if (event.getKeyCode() == KeyEvent.VK_W) {
                Manager.this.sendMessage("w");
            } else if (event.getKeyCode() == KeyEvent.VK_S) {
                Manager.this.sendMessage("s");
            } else if (event.getKeyCode() == KeyEvent.VK_RIGHT) {
                Manager.this.sendMessage("right");
            } else if (event.getKeyCode() == KeyEvent.VK_LEFT) {
                Manager.this.sendMessage("left");
            } else if (event.getKeyCode() == KeyEvent.VK_UP) {
                Manager.this.sendMessage("up");
            } else if (event.getKeyCode() == KeyEvent.VK_DOWN) {
                Manager.this.sendMessage("down");
            } else if (event.getKeyCode() == KeyEvent.VK_E) {
                Manager.this.sendMessage("e");
            } else if (event.getKeyCode() == KeyEvent.VK_R) {
                Manager.this.sendMessage("r");
            } else if (event.getKeyCode() == KeyEvent.VK_SPACE) {
                Manager.this.sendMessage("space");
            } else if (event.getKeyCode() == KeyEvent.VK_ENTER) {
                Manager.this.sendMessage("enter");
            } else if (event.getKeyCode() == KeyEvent.VK_ESCAPE) {
                Manager.this.sendMessage("escape");
            } 
        }

        public void keyReleased(KeyEvent event) {
            if (event.getKeyCode() == KeyEvent.VK_D) {
                Manager.this.sendMessage("d_release");
            } else if (event.getKeyCode() == KeyEvent.VK_A) {
                Manager.this.sendMessage("a_release");
            } else if (event.getKeyCode() == KeyEvent.VK_W) {
                Manager.this.sendMessage("w_release");
            } else if (event.getKeyCode() == KeyEvent.VK_S) {
                Manager.this.sendMessage("s_release");
            } else if (event.getKeyCode() == KeyEvent.VK_RIGHT) {
                Manager.this.sendMessage("right_release");
            } else if (event.getKeyCode() == KeyEvent.VK_LEFT) {
                Manager.this.sendMessage("left_release");
            } else if (event.getKeyCode() == KeyEvent.VK_UP) {
                Manager.this.sendMessage("up_release");
            } else if (event.getKeyCode() == KeyEvent.VK_DOWN) {
                Manager.this.sendMessage("down_release");
            } else if (event.getKeyCode() == KeyEvent.VK_R) {
                Manager.this.sendMessage("r_release");
            }
        }
    }

    private class TimerManager implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            long newTick = System.nanoTime();
            if (newTick - Manager.this.oldTick >= Manager.TICK_LENGTH || newTick < Manager.TICK_LENGTH) {
                Manager.this.oldTick = (newTick / Manager.TICK_LENGTH) * Manager.TICK_LENGTH;
                Manager.this.sendMessage("tick");
            }
        }
    }

    private class MouseManager extends MouseAdapter {
        public void mouseClicked(MouseEvent event) {
            if (event.getButton() == MouseEvent.BUTTON1) {
                Manager.this.sendMessage("click", event.getX(), event.getY());
            }
        }
    }

    private void sendMessage(String selector) {
        for (Object addressee : this.managedObjects) {
            try {
                if (addressee != null) {                    
                    Method message = addressee.getClass().getMethod(selector);
                    message.invoke(addressee);
                }
            } catch (SecurityException e) {
                this.doNothing();
            } catch (NoSuchMethodException e) {
                this.doNothing();
            } catch (IllegalArgumentException e) {
                this.doNothing();
            } catch (IllegalAccessException e) {
                this.doNothing();
            } catch (InvocationTargetException e) {
                this.doNothing();
            }
        }
        this.removeDeletedObjects();
    }

    private void sendMessage(String selector, int firstParameter, int secondParameter) {
        for (Object addressee : this.managedObjects) {
            try {
                if (addressee != null) {
                    Method message = addressee.getClass().getMethod(selector, Integer.TYPE, Integer.TYPE);
                    message.invoke(addressee, firstParameter, secondParameter);
                }
            } catch (SecurityException e) {
                this.doNothing();
            } catch (NoSuchMethodException e) {
                this.doNothing();
            } catch (IllegalArgumentException e) {
                this.doNothing();
            } catch (IllegalAccessException e) {
                this.doNothing();
            } catch (InvocationTargetException e) {
                this.doNothing();
            }
        }
        this.removeDeletedObjects();
    }

    private void doNothing() {

    }

    private void removeDeletedObjects() {
        if (this.deletedObjects.size() > 0) {
            Collections.sort(this.deletedObjects, Collections.reverseOrder());
            for(int i = this.deletedObjects.size() - 1; i >= 0; i--) {
                this.managedObjects.remove(this.deletedObjects.get(i));
            }
            this.deletedObjects.clear();
        }        
    }

    /**
     * Vytvori novy manazer, ktory nespravuje zatial ziadne objekty.
     */
    public Manager() {
        this.managedObjects = new ArrayList<Object>();
        this.deletedObjects = new ArrayList<Integer>();
        Canvas.getCanvas().addKeyListener(new KeyManager());
        Canvas.getCanvas().addTimerListener(new TimerManager());
        Canvas.getCanvas().addMouseListener(new MouseManager());
    }

    /**
     * Manazer bude spravovat dany objekt.
     */
    public void manageObject(Object object) {
        this.managedObjects.add(object);
    }

    /**
     * Manazer prestane spravovat dany objekt.
     */
    public void stopManageObject(Object object) {
        int index = this.managedObjects.indexOf(object);
        if (index >= 0) {
            this.managedObjects.set(index,null);
            this.deletedObjects.add(index);
        }
    }
}
