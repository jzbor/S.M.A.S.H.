package hgv.smash.game;

import hgv.smash.Main;
import hgv.smash.gui.GamePanel;
import hgv.smash.resources.Avatar;

import java.awt.*;
import java.awt.geom.Rectangle2D;

public class Player extends GameObject {

    private static final int WIDTH = 95; // width of model
    private static final int HEIGHT = 189; // height of model
    private static final double SPEED = 0.5; // speed of xpos movement (also used by jump())
    private static final double HIT_SPEED = 0.5;//speed when hit by other player
    private int jumps = 2; // jumps left

    //arrays of size 4:
    // [0] recent frame parameters
    // [1] parameters calculated from recent frame, used for collision detection, set by all inputs from keyboard and physics
    // [3] all should be filled with 0 for convenient shifting
    private int[] xpos;
    private int[] ypos; // position of player (usually upper left corner of model)
    private double[] vx;
    private double[] vy;
    private double vx_punch; // speed of player
    private Avatar avatar; // avatar to display for player
    private Shape model; // model (mainly for collision detection)
    private Player otherPlayer;//other player (for punches)
    private LevelMap levelMap;//Map on which players are
    private Rectangle[] platformModels;//get all models from platform
    private boolean jumped;//set true be changeMovement(); performs a jump in calc()
    private boolean punch;//set to true when punch should be performed(performed in calc)
    private int movementDirection;//gives direction of movement
    private double x_slowdown_acceleration = 9.81;//acceleration slows down jump

    public Player(Avatar avatar, int xpos, LevelMap levelMap) {
        this.xpos = new int[3];
        this.xpos[0] = xpos;
        this.ypos = new int[3];
        this.ypos[0] = 50;
        vx = new double[3];
        vx[0] = 0;
        vy = new double[3];
        vy[0] = 0;
        this.avatar = avatar;
        this.model = new Rectangle(this.xpos[0], this.ypos[0], WIDTH, HEIGHT);
        this.levelMap = levelMap;
        this.platformModels = levelMap.getPlatformModels();//platform does not move right now
        jumped = false;
    }

    //give other player for punches
    public void setOtherPlayer(Player otherPlayer) {
        this.otherPlayer = otherPlayer;
    }


    //not needed right now
    @Override
    public void collide(GameObject go) {
 /*       if(go instanceof LevelMap){
            //calculate new params
            vx=
        }
        if (go instanceof LevelMap) {
            LevelMap levelMap = (LevelMap) go;
            if (levelMap.intersects(model)) {
                vx = 0;
                vy = 0;
                // reset jumps
                jumps = 2;
            }
        }*/
    }

    @Override
    public void calc(long millis) {
        double factor = 0.00005;

        // falling
        vy[1] = vy[0] + millis * 9.81 * factor;

        //calc horizontal movement
        vx[1] = SPEED * movementDirection;
        //slow down x movement after hit, no infinite movement after hit
        factor *= 0.5;
        if (vx_punch - (x_slowdown_acceleration * (millis * factor)) > 0 && vx_punch > 0) {
            vx_punch = vx_punch - (x_slowdown_acceleration * millis * factor);
        } else if ((vx_punch + (x_slowdown_acceleration * millis * factor) < 0 && vx_punch < 0)) {
            vx_punch = vx_punch + (x_slowdown_acceleration * millis * factor);
        } else {
            vx_punch = 0;
        }
        vx[1] += vx_punch * 2;

        //change speed if hit
        if (punch) {
            punchOtherPlayer();
            punch = false;
        }
        //change speed if jumped
        if (jumped) {
            vy[1] = -SPEED;
            jumped = false;
        }

        // calc position by velocity
        ypos[1] = (int) (ypos[0] + vy[0] * millis);
        xpos[1] = (int) (xpos[0] + vx[0] * millis);

        // update model
        this.model = new Rectangle(xpos[0], ypos[0], WIDTH, HEIGHT);

        //detect collision, reset [1] params if collision detected
        //@todo set coordinates to coordinats of platfrom(no early stop of movement)
        for (Rectangle platform : platformModels) {
            if (platform.x + platform.width - xpos[1] >= 0 && xpos[1] + WIDTH - platform.x >= 0 && (ypos[0] + HEIGHT >= platform.y && ypos[0] <= platform.y + platform.height)) {
                vx[1] = 0;
                xpos[1] = xpos[0];
                //you can jump if you hit platform from side
                //last_jump = 0;
                jumps = 2;
            }
            if (platform.y + platform.height - ypos[1] >= 0 && ypos[1] + HEIGHT - platform.y >= 0 && (xpos[0] + WIDTH >= platform.x && xpos[0] <= platform.x + platform.width)) {
                vy[1] = 0;
                ypos[1] = ypos[0];
                //Attention bug(or not) if you hit platform from below you can instantly jump again
                //last_jump = 0;
                jumps = 2;
            }
        }

        //shift to new frame
        for (int i = 0; i < 2; i++) {
            vx[i] = vx[i + 1];
            vy[i] = vy[i + 1];
            xpos[i] = xpos[i + 1];
            ypos[i] = ypos[i + 1];
        }
    }

/*    public void walkLeft() {
        this.vx[1] = -SPEED;
    }

    public void walkRight() {
        this.vx[1] = +SPEED;
    }

    public void jump() {
        // jump only limited times
       //if (jumps > 0) {
            // speed up to the north
            //this.vy[] = -SPEED * 1;
            jumps--;
            jumped=true;
            System.out.println("jumped");
        //}
    }

    public void stay() {
        // reset x velocity
        this.vx[1] = 0;
    }*/

    public void changeMovement(int i) {
        switch (i) {
            case GamePanel.Movement.JUMP: {
                //todo: cooldown
                if (jumps > 0) {
                    jumped = true;
                    jumps--;
                }
                break;
            }
            case GamePanel.Movement.MOVE_LEFT: {
                movementDirection = -1;
                break;
            }
            case GamePanel.Movement.MOVE_RIGHT: {
                movementDirection = 1;
                break;
            }
            case GamePanel.Movement.STOP_MOVING: {
                movementDirection = 0;
                break;
            }
            case GamePanel.Movement.NORMAL_HIT: {
                //todo: cooldown
                punch = true;
                break;
            }
            default: {
                System.err.println("not implemented movement in player");
            }
        }
    }

    private void punchOtherPlayer() {
        Shape hitbox = new Rectangle(xpos[0] - WIDTH / 2, ypos[0] - HEIGHT / 3, WIDTH * 2, HEIGHT * 5 / 3);
        otherPlayer.hit(hitbox, xpos[0] + WIDTH / 2, ypos[0] + HEIGHT / 2);
    }

    private void hit(Shape hitbox, int xCentre, int yCentre) {
        if (hitbox.intersects((Rectangle2D) model)) {
            Vector2D punchVector = new Vector2D(xpos[0] + WIDTH / 2 - xCentre, ypos[0] + HEIGHT / 2 - yCentre);
            //unit vector so that every punch results in same speed
            Vector2D unitPunchVector = punchVector.directionVector();
            vx_punch = unitPunchVector.getX() * HIT_SPEED;
            vy[2] = unitPunchVector.getY() * HIT_SPEED;//todo why 0?
            System.out.println(unitPunchVector.toString());
        }
    }


    @Override
    public void draw(Graphics2D graphics2D) {
        // @TODO change to drawing the avatar

        if (Main.DEBUG) {
            // for debugging purposes only:
            graphics2D.setColor(Color.RED);
            graphics2D.fillRect(xpos[0], ypos[0], WIDTH, HEIGHT);
        }

        avatar.draw(graphics2D, xpos[0], ypos[0], 0);
    }
}
