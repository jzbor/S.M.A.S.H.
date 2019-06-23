package hgv.smash.game;

import hgv.smash.Main;
import hgv.smash.gui.Frame;
import hgv.smash.resources.Avatar;
import hgv.smash.resources.Sound;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

public class Player extends GameObject {


    //acceleration and speed constants
    private static final double SPEED = 0.5; // speed of xpos movement (also used by jump())
    private static final double HIT_SPEED = 0.05;//speed when hit by other player
    private static final long JUMP_COOLDOWN = 1900;//millis needed between two jumps
    private final static long SUPER_PUNCH_COOLDOWN = 10000;//millis needed between two punches
    private final static long PUNCH_COOLDOWN = 2000;//millis needed between two punches
    private final static long GENERAL_PUNCH_COOLDOWN = PUNCH_COOLDOWN;//always between whatever punch performed
    private final static int NUMBER_JUMPS = 2;
    private static final int[] SUPER_DAMAGE_RANGE = new int[]{5, 40};
    private static final int SUPER_MULTIPLIER = 2;
    private static final double X_SLOWDOWN_ACCELERATION = 9.81;//acceleration slows down jump
    private static final int MAX_HEIGHT = -1500;//max height
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
    private Sound punchSound;
    private Sound jumpSound;
    private Sound superPunchSound;
    //other objects
    private Player otherPlayer;//other player (for punches)
    private Rectangle[] platformModels;//get all models from platform
    private int movementDirection;//gives direction of movement
    //jumping
    private boolean jumped;//set true be changeMovement(); performs a jump in calc()
    private int jumps = 2; // jumps left
    private long lastJump;//time since last jump in millis
    //punching
    private boolean punch, superPunch;//set to true when punch should be performed(performed in calc)
    private double vx_punch, vy_punch; // speed of player because of punch
    private long lastPunch;//time since last punch in millis
    private long lastSuperPunch;
    private long lastGeneralPunch;
    private int percentage = 15;
    private int number; // number of player ( 1 or 2 )
    private int hitDirection = -1;//direction of punch
    private int normalHitboxWidth, normalHitboxHeight;//dimesion of hitbox

    public Player(Avatar avatar, int xpos, LevelMap levelMap, int number) {
        //punches directly available
        lastPunch = PUNCH_COOLDOWN;
        lastSuperPunch = SUPER_PUNCH_COOLDOWN;
        lastGeneralPunch = PUNCH_COOLDOWN;

        //load sounds
        punchSound = new Sound(Sound.HIT_SOUND);
        jumpSound = new Sound(Sound.JUMP_SOUND);
        superPunchSound = new Sound(Sound.SUPER_HIT_SOUND);

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
        //Map on which players are
        this.platformModels = levelMap.getPlatformModels();//platform does not move right now
        this.number = number;
        BufferedImage normalImage = avatar.getImage(Avatar.NORMAL);
        height = normalImage.getHeight();
        width = normalImage.getWidth();
        normalHitboxHeight = height;
        normalHitboxWidth = width;
    }

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
        lastSuperPunch += millis;
        lastGeneralPunch += millis;
        if (punch) {
            if (Frame.getInstance().getSound()) {
                punchSound.play();
            }
            punchOtherPlayer(false, hitDirection);
            avatar.setLastHit(System.currentTimeMillis());
            punch = false;
        }
        if (superPunch) {
            if (Frame.getInstance().getSound()) {
                superPunchSound.play();
            }
            punchOtherPlayer(true, hitDirection);
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
        if (ypos[1] < MAX_HEIGHT) {
            vy[1] = 0;
            ypos[1] = MAX_HEIGHT;
        }

        // update model
        this.model = new Rectangle(xpos[0], ypos[0], width, height);

        //detect collision, reset [1] params if collision detected
        for (Rectangle platform : platformModels) {

            if ((platform.x + platform.width - xpos[1] >= 0 && xpos[1] + width - platform.x >= 0) && (ypos[0] + height >= platform.y && ypos[0] <= platform.y + platform.height)) {
                xpos[1] = xpos[0];
                lastJump = JUMP_COOLDOWN;
                jumps = NUMBER_JUMPS;
            }
            if (platform.y + platform.height - ypos[1] >= 0 && ypos[1] + height - platform.y >= 0 && (xpos[1] + width >= platform.x && xpos[1] <= platform.x + platform.width)) {
                ypos[1] = ypos[0];
                vy[1] = 0;
                lastJump = JUMP_COOLDOWN;
                jumps = NUMBER_JUMPS;
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
                if (jumps > 0 && lastJump >= JUMP_COOLDOWN) {
                    lastJump = 0;
                    jumped = true;
                    jumps--;
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
                if (lastPunch > PUNCH_COOLDOWN && lastGeneralPunch > GENERAL_PUNCH_COOLDOWN) {
                    lastPunch = 0;
                    lastGeneralPunch = 0;
                    punch = true;
                }
                break;
            }
            case Movement.SUPER_HIT: {
                if (lastSuperPunch > SUPER_PUNCH_COOLDOWN && lastGeneralPunch > GENERAL_PUNCH_COOLDOWN) {
                    lastSuperPunch = 0;
                    lastGeneralPunch = 0;
                    superPunch = true;
                }
                break;
            }
            default: {
                System.err.println("not implemented movement in player");
            }
        }
    }

    private void punchOtherPlayer(boolean superdamage, int direction) {
        if (!superdamage) {
            if (direction == -1) {
                Shape hitbox = new Rectangle(xpos[0] + width / 2 - normalHitboxWidth, ypos[0] + height / 2 - normalHitboxHeight / 2, normalHitboxWidth, normalHitboxHeight);
                otherPlayer.hit(hitbox, xpos[0] + width / 2, ypos[0] + height / 2, superdamage);
            } else if (direction == 1) {
                Shape hitbox = new Rectangle(xpos[0] + width / 2, ypos[0] + height / 2 - normalHitboxHeight / 2, normalHitboxWidth, normalHitboxHeight);
                otherPlayer.hit(hitbox, xpos[0] + width / 2, ypos[0] + height / 2, superdamage);
            } else {
                System.err.println("unresolved hit direction");
            }
        } else {
            if (direction == -1) {
                Shape hitbox = new Rectangle(xpos[0] + width / 2 - normalHitboxWidth, ypos[0] + height / 2 - normalHitboxHeight / 2, normalHitboxWidth, normalHitboxHeight);
                otherPlayer.hit(hitbox, xpos[0] + width / 2, ypos[0] + height / 2, superdamage);
            } else if (direction == 1) {
                Shape hitbox = new Rectangle(xpos[0] + width / 2, ypos[0] + height / 2 - normalHitboxHeight / 2, normalHitboxWidth, normalHitboxHeight);
                otherPlayer.hit(hitbox, xpos[0] + width / 2, ypos[0] + height / 2, superdamage);
            } else {
                System.err.println("unresolved hit direction");
            }
        }
    }

    private void hit(Shape hitbox, int xCentre, int yCentre, boolean superdamage) {
        double hitMuliplyer = percentage / 5;
        if (hitbox.intersects((Rectangle2D) model)) {
            percentage += Math.random() * (SUPER_DAMAGE_RANGE[1] - SUPER_DAMAGE_RANGE[0]) + SUPER_DAMAGE_RANGE[0];
            Vector2D punchVector = new Vector2D(xpos[0] + width / 2 - xCentre, ypos[0] + height / 2 - yCentre);
            //unit vector so that every punch results in same speed
            Vector2D unitPunchVector = punchVector.directionVector();
            if (superdamage) {
                vx_punch = unitPunchVector.getX() * HIT_SPEED * hitMuliplyer * SUPER_MULTIPLIER;
                vy_punch = unitPunchVector.getY() * HIT_SPEED * hitMuliplyer * SUPER_MULTIPLIER;
                System.out.println(unitPunchVector.toString());
            } else {
                vx_punch = unitPunchVector.getX() * HIT_SPEED * hitMuliplyer;
                vy_punch = unitPunchVector.getY() * HIT_SPEED * hitMuliplyer;
                System.out.println(unitPunchVector.toString());
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

    public void draw(Graphics2D graphics2D) {

        avatar.draw(graphics2D, xpos[0], ypos[0], movementDirection, hitDirection);

        if (Main.DEBUG) {
            graphics2D.setColor(Color.RED);
            graphics2D.draw(model);
        }
    }

    public String getName() {
        return "Spieler " + number + " (" + avatar.getName() + ")";
    }

    //class representing all possible movements
    public static class Movement {
        public static final int MOVE_LEFT = -1;
        public static final int MOVE_RIGHT = 1;
        public static final int STOP_MOVING = 0;
        public static final int JUMP = 2;
        public static final int NORMAL_HIT = 3;
        public static final int SUPER_HIT = 4;
    }

}
