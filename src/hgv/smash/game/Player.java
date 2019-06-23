package hgv.smash.game;

import hgv.smash.Main;
import hgv.smash.gui.Frame;
import hgv.smash.resources.Avatar;
import hgv.smash.resources.Sound;

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
        public static final int SUPER_HIT = 4;
    }

    //acceleration and speed constants
    private static final double SPEED = 0.5; // speed of xpos movement (also used by jump())
    private static final double HIT_SPEED = 0.005;//speed when hit by other player
    private static final long JUMP_COOLDOWN = 1000;//millis needed between two jumps
    private final static long SUPER_PUNCH_COOLDOWN = 5000;//millis needed between two punches
    private final static long PUNCH_COOLDOWN = 2000;//millis needed between two punches
    private static final int[] DAMAGE_RANGE = new int[]{5, 15};
    private static final int[] SUPER_DAMAGE_RANGE = new int[]{5, 60};
    private static final int SUPER_MULTIPLIER = 10;
    private static final double X_SLOWDOWN_ACCELERATION = 9.81;//acceleration slows down jump
    private static final int MAX_HEIGHT=-1500;//max height
    //size of model loaded automatically
    private int width; // width of model
    private int height; // height of model

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
    //sounds
    private Sound punchSound=Sound.getInstanceSoundHit();
    private Sound jumpSound=Sound.getInstanceSoundJump();

    //other objects
    private Player otherPlayer;//other player (for punches)
    private LevelMap levelMap;//Map on which players are
    private Rectangle[] platformModels;//get all models from platform

    private int movementDirection;//gives direction of movement

    //jumping
    private boolean jumped;//set true be changeMovement(); performs a jump in calc()
    private int jumps = 2; // jumps left
    private long jumpCooldown = 2000;//millis needed between two jumps
    private long lastJump;//time since last jump in millis

    //punching
    private boolean punch, superPunch;//set to true when punch should be performed(performed in calc)
    private double vx_punch, vy_punch; // speed of player because of punch
    private long lastPunch;//time since last punch in millis
    private long lastSuperPunch;
    private int percentage = 15;
    private int number; // number of player ( 1 or 2 )
    private int hitDirection = -1;//direction of punch
    private int normalHitboxWidth,normalHitboxHeight;//dimesion of hitbox

    public Player(Avatar avatar, int xpos, LevelMap levelMap, int number) {
        lastPunch = PUNCH_COOLDOWN;
        lastSuperPunch = SUPER_PUNCH_COOLDOWN;
        this.xpos = new int[3];
        this.xpos[0] = xpos;
        this.ypos = new int[3];
        this.ypos[0] = -300;
        vx = new double[3];
        vx[0] = 0;
        vy = new double[3];
        vy[0] = 0;
        this.avatar = avatar;
        this.model = new Rectangle(this.xpos[0], this.ypos[0], width, height);
        this.levelMap = levelMap;
        this.platformModels = levelMap.getPlatformModels();//platform does not move right now
        this.number = number;
        BufferedImage normalImage = avatar.getImage(Avatar.NORMAL);
        height = normalImage.getHeight();
        width = normalImage.getWidth();
        normalHitboxHeight=height*2;
        normalHitboxWidth = width;
    }

    @Override
    public void calc(long millis) {
        double factor = 0.00004;


        // falling
        vy[1] = vy[0] + millis * 9.81 * factor + (vy_punch);
        vy_punch = 0;

        //calc horizontal movement
        vx[1] = SPEED * movementDirection;
        //slow down x movement after hit, no infinite movement after hit
        //factor *= 0.5;
        if (vx_punch - (X_SLOWDOWN_ACCELERATION * (millis * factor)) > 0 && vx_punch > 0) {
            vx_punch = vx_punch - (X_SLOWDOWN_ACCELERATION * millis * factor);
        } else if (vx_punch + (X_SLOWDOWN_ACCELERATION * millis * factor) < 0 && vx_punch < 0) {
            vx_punch = vx_punch + (X_SLOWDOWN_ACCELERATION * millis * factor);
        } else {
            vx_punch = 0;
        }
        vx[1] += vx_punch;


        //change speed if hit
        lastPunch += millis;
        if (punch) {
            if (Frame.getInstance().getSound()) {
                punchSound.play();
            }
            punchOtherPlayer(false,hitDirection);
            avatar.setLastHit(System.currentTimeMillis());
            punch = false;
        }
        lastSuperPunch += millis;
        if (superPunch) {
            if (Frame.getInstance().getSound()) {
                punchSound.play();
            }
            punchOtherPlayer(true,hitDirection);
            avatar.setLastSuperHit(System.currentTimeMillis());
            superPunch = false;
        }
        //change speed if jumped
        lastJump += millis;
        if (jumped) {
             if (Frame.getInstance().getSound()) {
                jumpSound.play();
            }
            vy[1] = -SPEED;
            jumped = false;
        }

        // calc position by velocity
        ypos[1] = (int) (ypos[0] + vy[0] * millis);
        xpos[1] = (int) (xpos[0] + vx[0] * millis);

        //detect if player is higher than max high
        if(ypos[1]<MAX_HEIGHT){
            vy[1]=0;
            ypos[1]=MAX_HEIGHT;
        }

        // update model
        this.model = new Rectangle(xpos[0], ypos[0], width, height);

        //detect collision, reset [1] params if collision detected
        //@todo set coordinates to coordinats of platfrom(no early stop of movement)
        for (Rectangle platform : platformModels) {

            if (platform.y + platform.height - ypos[1] >= 0 && ypos[1] + height - platform.y >= 0 && (xpos[0] + width >= platform.x && xpos[0] <= platform.x + platform.width)) {
                vy[1] = 0;
                //hit at bottom
                if (platform.y - (ypos[1] + height) < ypos[1] - (platform.y + height)) {
                    System.out.println("bottom");
                    ypos[1] = platform.y + platform.height + 1;
                }
                //hit at top
                else {
                    System.out.println("top");
                    ypos[1] = platform.y - height - 1;
                }
                jumps = 2;
                //todo instant jump if hit platform???
                lastJump = JUMP_COOLDOWN;
            }
            //horizontal collision
            if ((platform.x + platform.width - xpos[1] >= 0 && xpos[1] + width - platform.x >= 0) && (ypos[0] + height >= platform.y && ypos[0] <= platform.y + platform.height)) {
                vx[1] = 0;
                System.out.println("side");

                //hit left
                if (platform.x - (xpos[1] + width) > (xpos[1] - (platform.x + platform.width))) {
                    xpos[1] = platform.x - width - 1;
                }
                //hit right
                else {
                    xpos[1] = platform.x + platform.width + 1;
                }
                //you can jump if you hit platform from side
                jumps = 2;
                //todo instant jump if hit platform???
                lastJump = JUMP_COOLDOWN;
            }
            //safety if hit directly in corner
            if (platform.intersects((Rectangle2D) model)) {
                xpos[1] = xpos[0];
                ypos[1] = ypos[0];
                vy[1] = 0;
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

    //not needed right now
    @Override
    public void collide(GameObject go) {

    }

    public void changeMovement(int i) {
        switch (i) {
            case Movement.JUMP: {
                if (jumps > 0 && lastJump >= JUMP_COOLDOWN) {
                    jumped = true;
                    jumps--;
                    lastJump = 0;
                }
                break;
            }
            //could be solved easier: movementDirection=i;
            case Movement.MOVE_LEFT: {
                hitDirection = -1;
                movementDirection = -1;
                break;
            }
            case Movement.MOVE_RIGHT: {
                hitDirection = 1;
                movementDirection = 1;
                break;
            }
            case Movement.STOP_MOVING: {
                movementDirection = 0;
                break;
            }
            case Movement.NORMAL_HIT: {
                //todo: cooldown
                if (lastPunch > PUNCH_COOLDOWN) {
                    punch = true;
                    lastPunch = 0;
                    //not both punches directly after each other but cooldown only as long as  @todo change?
                    lastSuperPunch=SUPER_PUNCH_COOLDOWN-PUNCH_COOLDOWN;
                }
                break;
            }
            case Movement.SUPER_HIT: {
                //todo: cooldown
                if (lastSuperPunch > SUPER_PUNCH_COOLDOWN) {
                    superPunch = true;
                    lastSuperPunch = 0;
                    //not both punches directly after each other @todo change?
                    lastPunch=0;
                }
                break;
            }
            default: {
                System.err.println("not implemented movement in player");
            }
        }
    }

    private void punchOtherPlayer(boolean superdamage,int direction) {
        if(!superdamage){
            if(direction==-1) {
                Shape hitbox = new Rectangle(xpos[0] + width / 2-normalHitboxWidth, ypos[0] + height / 2-normalHitboxHeight/2, normalHitboxWidth, normalHitboxHeight);
                otherPlayer.hit(hitbox, xpos[0] + width / 2, ypos[0] + height / 2, superdamage);
            }
            else if(direction==1){
                Shape hitbox = new Rectangle(xpos[0] + width / 2, ypos[0] +height / 2-normalHitboxHeight/2, normalHitboxWidth, normalHitboxHeight);
                otherPlayer.hit(hitbox, xpos[0] + width / 2, ypos[0] + height / 2, superdamage);
            }
            else {
                System.err.println("unresolved hit direction");
            }
        }
        //@todo smaller hitbox
        else{
            if(direction==-1) {
                Shape hitbox = new Rectangle(xpos[0] + width / 2-normalHitboxWidth, ypos[0] + height / 2-normalHitboxHeight/2, normalHitboxWidth, normalHitboxHeight);
                otherPlayer.hit(hitbox, xpos[0] + width / 2, ypos[0] + height / 2, superdamage);
            }
            else if(direction==1){
                Shape hitbox = new Rectangle(xpos[0] + width / 2, ypos[0] +height / 2-normalHitboxHeight/2, normalHitboxWidth, normalHitboxHeight);
                otherPlayer.hit(hitbox, xpos[0] + width / 2, ypos[0] + height / 2, superdamage);
            }
            else {
                System.err.println("unresolved hit direction");
            }
        }
    }

    private void hit(Shape hitbox, int xCentre, int yCentre, boolean superdamage) {
        if (hitbox.intersects((Rectangle2D) model)) {
            Vector2D punchVector = new Vector2D(xpos[0] + width / 2 - xCentre, ypos[0] + height / 2 - yCentre);
            //unit vector so that every punch results in same speed
            Vector2D unitPunchVector = punchVector.directionVector();
            if (superdamage) {
                vx_punch = unitPunchVector.getX() * HIT_SPEED * percentage * SUPER_MULTIPLIER;
                vy_punch = unitPunchVector.getY() * HIT_SPEED * percentage * SUPER_MULTIPLIER;
                System.out.println(unitPunchVector.toString());
                percentage += Math.random() * (SUPER_DAMAGE_RANGE[1] - SUPER_DAMAGE_RANGE[0]) + SUPER_DAMAGE_RANGE[0];
            } else {
                vx_punch = unitPunchVector.getX() * HIT_SPEED * percentage;
                vy_punch = unitPunchVector.getY() * HIT_SPEED * percentage;
                System.out.println(unitPunchVector.toString());
                percentage += Math.random() * (DAMAGE_RANGE[1] - DAMAGE_RANGE[0]) + DAMAGE_RANGE[0];
            }

        }
    }

    public BufferedImage getSuperIcon() {
        if (lastSuperPunch < SUPER_PUNCH_COOLDOWN) {
            return avatar.getIcon(Avatar.SUPER_LOADING);
        } else {
            return avatar.getIcon(Avatar.SUPER_READY);
        }
    }

    public void compensateCooldown(long l) {     // @TODO rename?
        lastSuperPunch += l;
        lastPunch += l;
    }

    public int getPercentage() {
        return percentage;
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

    public int getXPos() {
        return xpos[0];
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getNumber() {
        return number;
    }

    @Override
    public void draw(Graphics2D graphics2D) {
        // @TODO change to drawing the avatar

        avatar.draw(graphics2D, xpos[0], ypos[0], movementDirection,hitDirection);

        if (Main.DEBUG) {
            graphics2D.setColor(Color.RED);
            graphics2D.draw(model);
        }
    }

    public String getName() {
        return "Player " + number;
    }

}
