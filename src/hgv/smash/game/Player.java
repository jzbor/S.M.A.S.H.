package hgv.smash.game;

import hgv.smash.Main;
import hgv.smash.gui.GamePanel;
import hgv.smash.resources.Avatar;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

public class Player extends GameObject {


    //class representing all possible movements
    public static class Movement {
        public static final int MOVE_LEFT = -1;
        public static final int MOVE_RIGHT = 1;
        public static final int STOP_MOVING = 0;
        public static final int JUMP = 2;
        public static final int NORMAL_HIT = 3;
    }

    //acceleration and speed constants
    private static final double SPEED = 0.5; // speed of xpos movement (also used by jump())
    private static final double HIT_SPEED = 0.5;//speed when hit by other player
    private final double x_slowdown_acceleration = 9.81;//acceleration slows down jump

    //size of model ?loaded automatically
    private int width = 95; // width of model
    private int height = 189; // height of model

    //speed and position of player
        //arrays of size 3:
            // [0] recent frame parameters
            // [1] parameters calculated from recent frame, used for collision detection, set by all inputs from keyboard and physics
            // [2] all should be filled with 0 for convenient shifting
    private int[] xpos;
    private int[] ypos; // position of player (usually upper left corner of model)
    private double[] vx;
    private double[] vy;

    //classes used for calculation and drawing
    private Avatar avatar; // avatar to display for player
    private Shape model; // model (mainly for collision detection)

    //other objects
    private Player otherPlayer;//other player (for punches)
    private LevelMap levelMap;//Map on which players are
    private Rectangle[] platformModels;//get all models from platform

    private int movementDirection;//gives direction of movement

    //jumping
    private boolean jumped;//set true be changeMovement(); performs a jump in calc()
    private int jumps = 2; // jumps left
    private long jumpCooldown=2000;//millis needed between two jumps
    private long lastJump;//time since last jump in millis

    //punching
    private boolean punch;//set to true when punch should be performed(performed in calc)
    private double vx_punch,vy_punch; // speed of player because of punch
    private final long punchCooldown=2000;//millis needed between two punches
    private long lastPunch;//time since last punch in millis

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
        this.model = new Rectangle(this.xpos[0], this.ypos[0], width, height);
        this.levelMap = levelMap;
        this.platformModels = levelMap.getPlatformModels();//platform does not move right now
        jumped = false;
        BufferedImage normalImage = avatar.getImage(Avatar.NORMAL);
        height = normalImage.getHeight();
        width = normalImage.getWidth();
    }

    //not needed right now
    @Override
    public void collide(GameObject go) {

    }

    @Override
    public void calc(long millis) {
        double factor = 0.00005;


        // falling
        vy[1] = vy[0] + millis * 9.81 * factor+(vy_punch*2);
        vy_punch=0;

        //calc horizontal movement
        vx[1] = SPEED * movementDirection;
        //slow down x movement after hit, no infinite movement after hit
        factor *= 0.5;
        if (vx_punch - (x_slowdown_acceleration *(millis * factor)) > 0 && vx_punch > 0) {
            vx_punch = vx_punch - (x_slowdown_acceleration * millis * factor);
        } else if ((vx_punch + (x_slowdown_acceleration * millis * factor) < 0 && vx_punch < 0)) {
            vx_punch = vx_punch + (x_slowdown_acceleration * millis * factor);
        } else {
            vx_punch = 0;
        }
        vx[1] += vx_punch;

        //change speed if hit
        lastPunch+=millis;
        if (punch) {
            punchOtherPlayer();
            punch = false;
        }
        //change speed if jumped
        lastJump+=millis;
        if (jumped) {
            vy[1] = -SPEED;
            jumped = false;
        }

        // calc position by velocity
        ypos[1] = (int) (ypos[0] + vy[0] * millis);
        xpos[1] = (int) (xpos[0] + vx[0] * millis);

        // update model
        this.model = new Rectangle(xpos[0], ypos[0], width, height);

        //detect collision, reset [1] params if collision detected
        //@todo set coordinates to coordinats of platfrom(no early stop of movement)
        for (Rectangle platform : platformModels) {

            if (platform.y + platform.height - ypos[1] >= 0 && ypos[1] + height - platform.y >= 0 && (xpos[0] + width >= platform.x && xpos[0] <= platform.x + platform.width)) {
                vy[1] = 0;
                //hit at bottom
                if(platform.y-(ypos[1]+height)<ypos[1]-(platform.y+height)){
                    System.out.println("bottom");
                    ypos[1]=platform.y+platform.height+1;
                }
                //hit at top
                else {
                    System.out.println("top");
                    ypos[1]=platform.y-height-1;
                }
                jumps = 2;
                //todo instant jump if hit platform???
                lastJump=jumpCooldown;
            }
            //horizontal collision
            if ((platform.x + platform.width - xpos[1] >= 0 && xpos[1] + width - platform.x >= 0) && (ypos[0] + height >= platform.y && ypos[0] <= platform.y + platform.height)) {
                vx[1] = 0;
                System.out.println("side");

                //hit left
                if(platform.x-(xpos[1]+width)>(xpos[1]-(platform.x+platform.width))){
                    xpos[1]=platform.x-width-1;
                }
                //hit right
                else {
                    xpos[1]=platform.x+platform.width+1;
                }
                //you can jump if you hit platform from side
                jumps = 2;
                lastJump=jumpCooldown;
            }
            //safety if hit directly in corner
            if(platform.intersects((Rectangle2D) model)){
                xpos[1]=xpos[0];
                ypos[1]=ypos[0];
                vy[1]=0;
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

    public void changeMovement(int i) {
        switch (i) {
            case Movement.JUMP: {
                if (jumps > 0&&lastJump>=jumpCooldown) {
                    jumped = true;
                    jumps--;
                    lastJump=0;
                }
                break;
            }
            //could be solved easier: movementDirection=i;
            case Movement.MOVE_LEFT: {
                movementDirection = -1;
                break;
            }
            case Movement.MOVE_RIGHT: {
                movementDirection = 1;
                break;
            }
            case Movement.STOP_MOVING: {
                movementDirection = 0;
                break;
            }
            case Movement.NORMAL_HIT: {
                //todo: cooldown
                if(lastPunch>=punchCooldown){
                    punch = true;
                    lastPunch=0;
                }
                break;
            }
            default: {
                System.err.println("not implemented movement in player");
            }
        }
    }

    private void punchOtherPlayer() {
        Shape hitbox = new Rectangle(xpos[0] - width / 2, ypos[0] - height / 3, width * 2, height * 5 / 3);
        otherPlayer.hit(hitbox, xpos[0] + width / 2, ypos[0] + height / 2);
    }

    private void hit(Shape hitbox, int xCentre, int yCentre) {
        if (hitbox.intersects((Rectangle2D) model)) {
            Vector2D punchVector = new Vector2D(xpos[0] + width / 2 - xCentre, ypos[0] + height / 2 - yCentre);
            //unit vector so that every punch results in same speed
            Vector2D unitPunchVector = punchVector.directionVector();
            vx_punch = unitPunchVector.getX() * HIT_SPEED;
            vy_punch = unitPunchVector.getY() * HIT_SPEED;
            System.out.println(unitPunchVector.toString());
        }
    }

    public Player getOtherPlayer() {
        return otherPlayer;
    }

    //give other player for punches
    public void setOtherPlayer(Player otherPlayer) {
        this.otherPlayer = otherPlayer;
    }

    public Avatar getAvatar() {
        return avatar;
    }

    public int getYPos() {
        return ypos[0];
    }

    public int getXPos(){
        return xpos[0];
    }
    public int getWidth(){
        return width;
    }
    public int getHeight(){
        return height;
    }


    @Override
    public void draw(Graphics2D graphics2D) {
        // @TODO change to drawing the avatar

        if (Main.DEBUG) {
            // for debugging purposes only:
            graphics2D.setColor(Color.RED);
            graphics2D.fillRect(xpos[0], ypos[0], width, height);
        }

        avatar.draw(graphics2D, xpos[0], ypos[0], movementDirection);
    }
}
