package hgv.smash.gui;

import hgv.smash.Main;
import hgv.smash.game.GameloopThread;
import hgv.smash.game.LevelMap;
import hgv.smash.game.Player;
import hgv.smash.resources.Avatar;
import hgv.smash.resources.Design;
import hgv.smash.resources.Music;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;

public class GamePanel extends Panel {


    private static final int FRAMERATE = 100;
    //size of frame
    private double width;
    private double height;
    //keys for movement
    private static long JUMP_COOLDOWN = 2000;
    //size of triangle representing player out of map
    private final int triangleSize = 150;
    // y coords defining a death
    private static int RANGE_OF_DEATH = 10000;
    //player1
    private char[] keys_player_1 = {'w', 'a', 'd', 'f', 'r'};
    private boolean[] booleans_player1 = {false, false, false, false, false};
    //player2
    private char[] keys_player_2 = {'i', 'j', 'l', 'ö', 'p'};
    private boolean[] booleans_player2 = {false, false, false, false, false};
    //actions for keys in same order as keys
    private int[] actions = {Player.Movement.JUMP, Player.Movement.MOVE_LEFT, Player.Movement.MOVE_RIGHT,
            Player.Movement.NORMAL_HIT, Player.Movement.SUPER_HIT};
    //last performed action by pressing
    private int lastChangePlayer1 = Player.Movement.STOP_MOVING;
    private int lastChangePlayer2 = Player.Movement.STOP_MOVING;
    private boolean running;
    private int currentFramerate;
    private Player player1;
    private Player player2;
    private Player[] players;
    private LevelMap levelMap;
    private Image frameBuffer;
    //
    private BufferedImage originalArrow;

    public GamePanel(Avatar a1, Avatar a2, LevelMap map) {
        height = Frame.getInstance().getHeight();
        width = Frame.getInstance().getWidth();
        // assign and create params
        running = true;
        GameloopThread gameloopThread = new GameloopThread(this);
        player1 = new Player(a1, 200, map);
        player2 = new Player(a2, 300, map);
        players=new Player[2];
        players[0]=player1;
        players[1]=player2;
        player1.setOtherPlayer(player2);
        player2.setOtherPlayer(player1);
        levelMap = map;

        try {
            originalArrow = ImageIO.read(new File("./resources/icons/arrow4.png"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        OurKeyListener.getInstance().setPanel(this);

        // start gameloop
        gameloopThread.start();
    }

    public void gameloop() {
        long lastFrame = System.currentTimeMillis() - 1;
        while (running) {
            // get time for the physics to calculate
            long thisFrame = System.currentTimeMillis();
            long timedelta = thisFrame - lastFrame;
            currentFramerate = (int) (1000 / timedelta);

            // detect death
            detectGameover();

            player1.calc(timedelta);
            player2.calc(timedelta);
            levelMap.calc(timedelta);

            // collision detection
            //player1.collide(levelMap);
            //player2.collide(levelMap);

            // buffer frame
            BufferedImage bi = new BufferedImage(1024, 768, BufferedImage.TYPE_INT_ARGB_PRE);
            Graphics2D graphics2D = (Graphics2D) bi.getGraphics();

            // draw items on buffer
            levelMap.draw(graphics2D);
            player1.draw(graphics2D);
            player2.draw(graphics2D);

            if (Main.DEBUG) {
                // draw fps
                graphics2D.setColor(Color.BLACK);
                graphics2D.drawString(currentFramerate + " FPS", 20, 20);
            }

            // @TODO implement thread safety
            frameBuffer = calculateCamera(bi);

            // request ui update
            updateUI();

            // sleep @TODO solve weird shit
            if ((System.currentTimeMillis() - thisFrame) < (1000.0 / FRAMERATE)) {
                int sleep = (int) ((1000.0 / FRAMERATE) - (System.currentTimeMillis() - thisFrame));
                if (sleep > 0) {
                    try {
                        Thread.sleep(sleep);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            lastFrame = thisFrame;
        }
    }

    private void detectGameover() {
        boolean gameover = false;
        Player p = null;
        if (player1.getYPos() > RANGE_OF_DEATH) {
            p = player2;
            gameover = true;
        } else if (player2.getYPos() > RANGE_OF_DEATH) {
            p = player1;
            gameover = true;
        }

        if (gameover) {
            System.out.println("Gameover");
            BufferedImage img = new BufferedImage(getHeight(), getWidth(), BufferedImage.TYPE_INT_ARGB_PRE);
            Graphics2D imageGraphics = img.createGraphics();
            printAll(imageGraphics);
            imageGraphics.dispose();

            Panel panel = new ScorePanel(p, p.getOtherPlayer(), img);
            Frame.getInstance().getContentPane().removeAll();
            Frame.getInstance().getContentPane().add(panel);
            Frame.getInstance().repaint();
            panel.updateUI();

            Music.getInstanceGameMusic().stop();
            running = false;
        }
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);

        // paint buffer
        Graphics2D graphics2D = (Graphics2D) graphics;

        graphics2D.drawImage(frameBuffer, 0, 0, this);
        graphics2D.setColor(Design.getPrimaryColor());
        graphics2D.setFont(Design.getDefaultFont(30));
        FontMetrics fm = graphics2D.getFontMetrics();
        int PADDING = 50;
        String p1p = player1.getPercentage() + "%";
        graphics2D.drawString(p1p, PADDING, PADDING);
        String p2p = player2.getPercentage() + "%";
        graphics2D.drawString(p2p, (int) (width - fm.stringWidth(p2p) - PADDING), PADDING);
    }

    private Image calculateCamera(BufferedImage bufferedImage) {
        int offset = 100;


        //find smallest rectangle wich has both players with offset inside
        double yTop;
        if (player1.getYPos() > player2.getYPos()) {
            yTop = player2.getYPos();
        } else {
            yTop = player1.getYPos();
        }
        yTop -= offset;

        double yBottom;
        if (player1.getYPos() + player1.getHeight() > player2.getYPos() + player2.getHeight()) {
            yBottom = player1.getYPos() + player1.getHeight();
        } else {
            yBottom = player2.getYPos() + player2.getHeight();
        }
        yBottom += offset;


        double xLeft;
        if (player1.getXPos() > player2.getXPos()) {
            xLeft = player2.getXPos();
        } else {
            xLeft = player1.getXPos();
        }
        xLeft -= offset;

        double xRight;
        if (player1.getXPos() + player1.getWidth() > player2.getXPos() + player2.getWidth()) {
            xRight = player1.getXPos() + player1.getWidth();
        } else {
            xRight = player2.getXPos() + player2.getWidth();
        }
        xRight += offset;


        //calculate length of side of smallest rectangle and increase smaller size till it fits to relation of window
        double xDiff = xRight - xLeft;
        double yDiff = yBottom - yTop;

        //no images greater than picture of map
        if (xDiff > width) {
            xDiff = width;
        }
        if (yDiff > height) {
            yDiff = height;
        }

        if (xDiff / yDiff < width / height) {
            double xDiffOld = xDiff;
            xDiff = width / height * yDiff;
            double xAdd = (xDiff - xDiffOld) / 2;
            xLeft -= xAdd;
            xRight += xAdd;
        } else {
            double yDiffOld = yDiff;
            yDiff = height / width * xDiff;
            double yAdd = (yDiff - yDiffOld) / 2;
            yTop -= yAdd;
            yBottom += yAdd;
        }

        //no images greater than picture of map(double check)
        if (xDiff > width) {
            xDiff = width;
        }
        if (yDiff > height) {
            yDiff = height;
        }

        //no images outside of picture of map
        if (xLeft < 0) {
            xLeft = 0;
            xRight = xLeft + xDiff;
        }
        if (xLeft + xDiff > width) {
            xLeft = width - xDiff;
            xRight = width;
        }
        if (yTop < 0) {
            yTop = 0;
            yBottom = yTop + yDiff;
        } else if (yTop + yDiff > height) {
            yTop = height - yDiff;
            yBottom = height;
        }

        //create subimage and return upscaled one
        BufferedImage subimage = bufferedImage.getSubimage((int) xLeft, (int) yTop, (int) xDiff, (int) yDiff);





        //draw direction of player if outside of map

        //create picture for final arrow
        BufferedImage transformImage;


        //graphics to draw arrow on
        Graphics2D graphics2D = (Graphics2D) subimage.getGraphics();


        //player1

        int[] xPos = new int[3];
        int[] yPos = new int[3];

        int xLeftInt = (int) xLeft;
        int xRightInt = (int) xRight;
        int yTopInt = (int) yTop;
        int yBottomInt = (int) yBottom;
        int xDiffInt = xRightInt - xLeftInt;
        int yDiffInt = yBottomInt - yTopInt;

        boolean isPlayerOutOfMap;
        double theta = 0.0;
        int xArrow = 0;
        int yArrow = 0;

        double prefactor = 0.5;
        double factor = xDiff / width * prefactor;
        transformImage = resizePicture(originalArrow, factor);


        int halfArrowWidth=transformImage.getWidth()/2;
        int halfArrowHeight=transformImage.getHeight()/2;


        for(Player player:players) {
            isPlayerOutOfMap=true;
            //corners
            //top left corner
            if (player.getXPos() + player.getWidth() < xLeftInt + halfArrowWidth && player.getYPos() + player.getHeight() < yTop || player.getXPos() + player.getWidth() < xLeftInt && player.getYPos() + player.getHeight() < yTop + halfArrowHeight) {
                if (Main.DEBUG) {
                    xPos[0] = 0;
                    xPos[1] = 0;
                    xPos[2] = triangleSize;
                    yPos[0] = 0;
                    yPos[1] = triangleSize;
                    yPos[2] = 0;
                }
                theta = 1.25 * Math.PI;
                xArrow = 0;
                yArrow = 0;
                //System.out.println("top left");
            }
            //bottom left corner
            else if (player.getXPos() + player.getWidth() < xLeftInt + halfArrowWidth && player.getYPos() > yBottomInt || player.getXPos() + player.getWidth() < xLeftInt && player.getYPos() > yBottomInt - halfArrowHeight) {
                if (Main.DEBUG) {
                    xPos[0] = 0;
                    xPos[1] = 0;
                    xPos[2] = triangleSize;
                    yPos[0] = yDiffInt - triangleSize;
                    yPos[1] = yDiffInt;
                    yPos[2] = yDiffInt;
                }
                theta = 0.75 * Math.PI;
                xArrow = 0;
                yArrow = yDiffInt - transformImage.getHeight();
                //System.out.println("bottom left");
            }
            //top right corner
            else if (player.getXPos() > xRightInt - halfArrowWidth && player.getYPos() + player.getHeight() < yTop || player.getXPos() > xRightInt && player.getYPos() + player.getHeight() < yTop + halfArrowHeight) {
                if (Main.DEBUG) {
                    xPos[0] = xDiffInt;
                    xPos[1] = xDiffInt;
                    xPos[2] = xDiffInt - triangleSize;
                    yPos[0] = 0;
                    yPos[1] = triangleSize;
                    yPos[2] = 0;
                }
                theta = 1.75 * Math.PI;
                xArrow = xDiffInt - transformImage.getWidth();
                yArrow = 0;
                //System.out.println("top right");
            }
            //bottom right corner
            else if (player.getXPos() > xRightInt - halfArrowWidth && player.getYPos() > yBottomInt || player.getXPos() > xRightInt && player.getYPos() > yBottomInt - halfArrowHeight) {
                if (Main.DEBUG) {
                    xPos[0] = xRightInt;
                    xPos[1] = xRightInt;
                    xPos[2] = xRightInt - triangleSize;
                    yPos[0] = yBottomInt;
                    yPos[1] = yBottomInt - triangleSize;
                    yPos[2] = yBottomInt;
                }
                theta = 0.25 * Math.PI;
                xArrow = xDiffInt - transformImage.getWidth();
                yArrow = yDiffInt - transformImage.getHeight();
                //System.out.println("bottom right");
            }

            //side


            //left side
            else if (player.getXPos() + player.getWidth() < (int) xLeft) {
                if (Main.DEBUG) {
                    xPos[0] = 0;
                    xPos[1] = (int) Math.sqrt(2 * Math.pow(triangleSize, 2)) / 2;
                    xPos[2] = (int) Math.sqrt(2 * Math.pow(triangleSize, 2)) / 2;
                    yPos[0] = player.getYPos() - yTopInt + player.getHeight() / 2;
                    yPos[1] = player.getYPos() - yTopInt - (int) Math.sqrt(2 * Math.pow(triangleSize, 2)) / 2 + player.getHeight() / 2;
                    yPos[2] = player.getYPos() - yTopInt + (int) Math.sqrt(2 * Math.pow(triangleSize, 2)) / 2 + player.getHeight() / 2;
                }
                theta = Math.PI;
                xArrow = 0;
                yArrow = player.getYPos() - yTopInt + player.getHeight() / 2 - transformImage.getHeight() / 2;
                //System.out.println("left side");
            }
            //right side
            else if (player.getXPos() > xRight) {
                if (Main.DEBUG) {
                    xPos[0] = xDiffInt;
                    xPos[1] = xDiffInt - (int) Math.sqrt(2 * Math.pow(triangleSize, 2)) / 2;
                    xPos[2] = xDiffInt - (int) Math.sqrt(2 * Math.pow(triangleSize, 2)) / 2;
                    yPos[0] = player.getYPos() - yTopInt + player.getHeight() / 2;
                    yPos[1] = player.getYPos() - yTopInt - (int) Math.sqrt(2 * Math.pow(triangleSize, 2)) / 2 + player.getHeight() / 2;
                    yPos[2] = player.getYPos() - yTopInt + (int) Math.sqrt(2 * Math.pow(triangleSize, 2)) / 2 + player.getHeight() / 2;
                }
                theta = 0;
                xArrow = xDiffInt - transformImage.getWidth();
                yArrow = player.getYPos() - yTopInt + player.getHeight() / 2 - transformImage.getHeight() / 2;
                //System.out.println("right side");
            }
            //top side
            else if (player.getYPos() + player.getHeight() < yTop) {
                if (Main.DEBUG) {
                    xPos[0] = player.getXPos() + player.getWidth() / 2;
                    xPos[1] = player.getXPos() + player.getWidth() / 2 - (int) Math.sqrt(2 * Math.pow(triangleSize, 2)) / 2;
                    xPos[2] = player.getXPos() + player.getWidth() / 2 + (int) Math.sqrt(2 * Math.pow(triangleSize, 2)) / 2;
                    yPos[0] = 0;
                    yPos[1] = (int) Math.sqrt(2 * Math.pow(triangleSize, 2)) / 2;
                    yPos[2] = (int) Math.sqrt(2 * Math.pow(triangleSize, 2)) / 2;
                }
                theta = Math.PI * 1.5;
                xArrow = player.getXPos() + player.getWidth() / 2 - xLeftInt - transformImage.getWidth() / 2;
                yArrow = 0;
                //System.out.println("top side");
            }
            //bottom side
            else if (player.getYPos() > yBottom) {
                if (Main.DEBUG) {
                    xPos[0] = player.getXPos() + player.getWidth() / 2;
                    xPos[1] = player.getXPos() + player.getWidth() / 2 - (int) Math.sqrt(2 * Math.pow(triangleSize, 2)) / 2;
                    xPos[2] = player.getXPos() + player.getWidth() / 2 + (int) Math.sqrt(2 * Math.pow(triangleSize, 2)) / 2;
                    yPos[0] = yDiffInt;
                    yPos[1] = yDiffInt - (int) Math.sqrt(2 * Math.pow(triangleSize, 2)) / 2;
                    yPos[2] = yDiffInt - (int) Math.sqrt(2 * Math.pow(triangleSize, 2)) / 2;
                }
                theta = Math.PI * 0.5;
                xArrow = player.getXPos() - xLeftInt + player.getWidth() / 2 - halfArrowWidth;
                yArrow = yDiffInt - transformImage.getHeight();
                //System.out.println("bottom side");
            }
            //not out of map
            else {
                isPlayerOutOfMap=false;
            }
            if (Main.DEBUG) {
                graphics2D.fillPolygon(xPos, yPos, 3);
            }

            //calculate size of arrow
            if (isPlayerOutOfMap) {
                transformImage = rotatePicture(transformImage, theta);
                //draw arrow
                graphics2D.drawImage(transformImage, xArrow, yArrow, null);
            }
        }

        //test end

        //@todo replace with faster scaling method
        return subimage.getScaledInstance(1024, 768, Image.SCALE_FAST);
    }


    @Override
    public void keyTyped(KeyEvent keyEvent) {
    }

    @Override
    public void keyPressed(KeyEvent keyEvent) {
        char key = keyEvent.getKeyChar();

        for (int i = 0; i < keys_player_1.length; i++) {
            if (key == keys_player_1[i] && !booleans_player1[i]) {
                player1.changeMovement(actions[i]);
                booleans_player1[i] = true;
            }
            if (key == keys_player_2[i] && !booleans_player2[i]) {
                player2.changeMovement(actions[i]);
                booleans_player2[i] = true;
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent keyEvent) {
        char key = keyEvent.getKeyChar();

        for (int i = 0; i < keys_player_1.length; i++) {
            if (key == keys_player_1[i] && booleans_player1[i]) {
                booleans_player1[i] = false;
            }
            if (key == keys_player_2[i] && booleans_player2[i]) {
                booleans_player2[i] = false;
            }
        }
        if (!booleans_player1[1] && !booleans_player1[2]) {
            player1.changeMovement(Player.Movement.STOP_MOVING);
        }
        if (!booleans_player2[1] && !booleans_player2[2]) {
            player2.changeMovement(Player.Movement.STOP_MOVING);
        }
        //player1 change direction if both keys were pressed and now one was released
        if (!booleans_player1[1] && booleans_player1[2]) {
            player1.changeMovement(actions[2]);
        } else if (booleans_player1[1] && !booleans_player1[2]) {
            player1.changeMovement(actions[1]);
        }
        //player2 change direction if both keys were pressed and now one was released
        if (!booleans_player2[1] && booleans_player2[2]) {
            player2.changeMovement(actions[2]);
        } else if (booleans_player2[1] && !booleans_player2[2]) {
            player2.changeMovement(actions[1]);
        }
    }

    private BufferedImage resizePicture(BufferedImage bufferedImage, double factor) {
        int size = bufferedImage.getWidth();
        BufferedImage dest = new BufferedImage((int) (size * factor), (int) (size * factor), bufferedImage.getType());
        AffineTransform affineTransform = AffineTransform.getScaleInstance(factor, factor);
        AffineTransformOp op = new AffineTransformOp(affineTransform, AffineTransformOp.TYPE_BILINEAR);
        op.filter(bufferedImage, dest);
        return dest;
    }

    private BufferedImage rotatePicture(BufferedImage bufferedImage, double theta) {
        BufferedImage dest = new BufferedImage(bufferedImage.getWidth(), bufferedImage.getHeight(), bufferedImage.getType());
        AffineTransform affineTransform = AffineTransform.getRotateInstance(theta, bufferedImage.getWidth() / 2, bufferedImage.getHeight() / 2);
        AffineTransformOp op = new AffineTransformOp(affineTransform, AffineTransformOp.TYPE_BILINEAR);
        op.filter(bufferedImage, dest);
        return dest;
    }

}
