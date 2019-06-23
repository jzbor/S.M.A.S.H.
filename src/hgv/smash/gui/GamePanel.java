package hgv.smash.gui;

import hgv.smash.Main;
import hgv.smash.game.GameloopThread;
import hgv.smash.game.LevelMap;
import hgv.smash.game.Player;
import hgv.smash.game.Vector2D;
import hgv.smash.resources.Avatar;
import hgv.smash.resources.Design;
import hgv.smash.resources.KeyBoardLayout;
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
    private static final int[] FREEZE_COLOR = new int[]{126, 197, 252, 125};
    // y coords defining a death
    private static final int RANGE_OF_DEATH = 1000;
    //size of frame
    private double width;
    private double height;

    private boolean cameraRunning;
    //player1
    private char[] keys_player_1;
    private boolean[] booleans_player1 = {false, false, false, false, false};
    //player2
    private char[] keys_player_2;
    private boolean[] booleans_player2 = {false, false, false, false, false};
    //actions for keys in same order as keys
    private int[] actions = {Player.Movement.JUMP, Player.Movement.MOVE_LEFT, Player.Movement.MOVE_RIGHT,
            Player.Movement.NORMAL_HIT, Player.Movement.SUPER_HIT};
    private boolean running;
    private Player player1;
    private Player player2;
    private Player[] players;
    private LevelMap levelMap;
    private Image frameBuffer;
    private BufferedImage originalArrow;
    private boolean paused;

    public GamePanel(Avatar a1, Avatar a2, LevelMap map) {
        updateKeys();

        height = Frame.getInstance().getContentPane().getHeight() + 2/*map.getBackgroundImage().getHeight()*/;
        width = Frame.getInstance().getContentPane().getWidth() + 2/*map.getBackgroundImage().getWidth()*/;
        // assign and create params
        running = true;
        GameloopThread gameloopThread = new GameloopThread(this);
        int[] spawnPositions = map.getSpawnPositions();
        player1 = new Player(a1, spawnPositions[0], map, 1);
        player2 = new Player(a2, spawnPositions[1] - a1.getImage(Avatar.NORMAL).getWidth(), map, 2);
        players = new Player[2];
        players[0] = player1;
        players[1] = player2;
        player1.setOtherPlayer(player2);
        player2.setOtherPlayer(player1);
        levelMap = map;

        cameraRunning = true;

        try {
            originalArrow = ImageIO.read(new File("./resources/icons/arrow.png"));
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
            int currentFramerate = (int) (1000 / timedelta);

            // detect death
            detectGameover();

            if (!paused) {
                player1.calc(timedelta);
                player2.calc(timedelta);
                levelMap.calc(timedelta);
            }


            // buffer frame
            BufferedImage bi = new BufferedImage(1024, 768, BufferedImage.TYPE_INT_ARGB_PRE);
            Graphics2D graphics2D = (Graphics2D) bi.getGraphics();

            // draw items on buffer
            levelMap.draw(graphics2D);
            player1.draw(graphics2D);
            player2.draw(graphics2D);

            //calculate camera
            ImageExtract imageExtract = null;
            if (cameraRunning) {
                imageExtract = calculateCamera(bi);
            } else {
                imageExtract = new ImageExtract(0, 0, (int) width, (int) height, bi);
            }

            if (Main.DEBUG) {
                // draw fps
                graphics2D.setColor(Color.BLACK);
                graphics2D.drawString(currentFramerate + " FPS", 20, 20);
            }

            //calculate arrows
            imageExtract = calculateArrows(imageExtract);
            bi = imageExtract.getImage();

            // @TODO implement thread safety
            frameBuffer = bi;

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

            Panel panel = new ScorePanel(p, p.getOtherPlayer(), levelMap);
            Frame.getInstance().getContentPane().removeAll();
            Frame.getInstance().getContentPane().add(panel);
            Frame.getInstance().repaint();
            panel.updateUI();

            Music.getInstanceGameMusic().stop();
            running = false;
        }
    }

    private void afterDraw(Graphics2D graphics2D) {
        graphics2D.drawImage(frameBuffer, 0, 0, this);
        graphics2D.setColor(Design.getPrimaryColor());
        graphics2D.setFont(Design.getDefaultFont(50));
        FontMetrics fm = graphics2D.getFontMetrics();
        int PADDING = 25;

        String p1p = player1.getPercentage() + "%";
        BufferedImage p1Icon = scaleImgToHeight(player1.getSuperIcon(), player1.getSuperIcon().getHeight()); // @TODO adjust height
        graphics2D.drawImage(p1Icon, PADDING,
                PADDING, null);
        graphics2D.drawString(p1p, (PADDING + p1Icon.getWidth()) + PADDING,
                PADDING + p1Icon.getHeight() / 2);

        String p2p = player2.getPercentage() + "%";
        BufferedImage p2Icon = scaleImgToHeight(player2.getSuperIcon(), player2.getSuperIcon().getHeight()); // @TODO adjust height
        graphics2D.drawImage(p2Icon, (int) (width - PADDING - p2Icon.getWidth()),
                PADDING, null);
        graphics2D.drawString(p2p, (int) ((width - PADDING - p2Icon.getWidth()) - fm.stringWidth(p2p) - PADDING),
                PADDING + p2Icon.getHeight() / 2);

        if (paused) {
            Color color = new Color(FREEZE_COLOR[0], FREEZE_COLOR[1], FREEZE_COLOR[2], FREEZE_COLOR[3]);
            graphics2D.setColor(color);
            graphics2D.fillRect(0, 0, (int) width, (int) height);
        }

    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);

        // paint buffer
        Graphics2D graphics2D = (Graphics2D) graphics;

        afterDraw(graphics2D);
    }

    private ImageExtract calculateCamera(BufferedImage bufferedImage) {
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
        BufferedImage subimage;
        //linux bug
        try {
            subimage = bufferedImage.getSubimage((int) xLeft, (int) yTop, (int) xDiff, (int) yDiff);
        } catch (Exception e) {
            //System.out.println("Linux error");
            subimage = bufferedImage.getSubimage((int) xLeft, (int) yTop, (int) xDiff - 2, (int) yDiff - 2);
        }
        subimage = resizePicture(subimage, width / xDiff);

        return new ImageExtract((int) xLeft, (int) yTop, (int) xDiff, (int) yDiff, subimage);
    }

    private ImageExtract calculateArrows(ImageExtract imageExtract) {

        //draw direction of player if outside of map

        //create picture for final arrow
        BufferedImage transformedArrowImage;


        //graphics to draw arrow on
        Graphics2D graphics2D = (Graphics2D) imageExtract.getImage().getGraphics();


        //coordinates for debugging arrows
        int[] xPos = new int[3];
        int[] yPos = new int[3];

        int xLeft = imageExtract.getxOffset();
        int xRight = xLeft + imageExtract.getWidth();
        int yTop = imageExtract.getyOffset();
        int yBottom = yTop + imageExtract.getHeight();
        int xDiff = imageExtract.getWidth();
        int yDiff = imageExtract.getHeight();

        double factor = 0.7;

        for (Player player : players) {
            transformedArrowImage = resizePicture(originalArrow, factor);

            int halfArrowLength = transformedArrowImage.getWidth() / 2;//picture is square

            boolean isPlayerOutOfMap = true;

            //player coordinates
            int yPlayerTop = player.getYPos();
            int yPlayerBottom = yPlayerTop + player.getHeight();
            int yPlayerMiddle = player.getYPos() + player.getHeight() / 2;

            int xPlayerLeft = player.getXPos();
            int xPlayerRight = xPlayerLeft + player.getWidth();
            int xPlayerMiddle = player.getXPos() + player.getWidth() / 2;

            //angle to turn arrow
            double theta = 0.0;

            //position of arrow
            int xArrow = 0;
            int yArrow = 0;

            double playerIconFactor = 50.0 / player.getHeight();
            //position of player icon image (located in arrow middle)
            int xPlayerIcon = 0;
            int playerIconWidth = (int) (player.getWidth() * playerIconFactor);

            int yPlayerIcon = 0;
            int playerIconHeight = (int) (player.getHeight() * playerIconFactor);


            //corners
            //top left corner
            if (xPlayerMiddle < xLeft + halfArrowLength && yPlayerBottom < yTop || xPlayerRight < xLeft && yPlayerMiddle < yTop + halfArrowLength) {

                Vector2D arrowVector = new Vector2D(xPlayerMiddle - (halfArrowLength + xLeft), yPlayerMiddle - (halfArrowLength + yTop));
                theta = arrowVector.angle(new Vector2D(0, 1)) - 1.5 * Math.PI;
                xArrow = 0;
                yArrow = 0;

                xPlayerIcon = halfArrowLength - playerIconWidth / 2;
                yPlayerIcon = halfArrowLength - playerIconHeight / 2;
            }
            //bottom left corner
            else if (xPlayerMiddle < xLeft + halfArrowLength && yPlayerTop > yBottom || xPlayerRight < xLeft && yPlayerMiddle > yBottom - halfArrowLength) {

                Vector2D arrowVector = new Vector2D(xPlayerMiddle - (halfArrowLength + xLeft), yPlayerMiddle - (halfArrowLength + yBottom));
                theta = arrowVector.angle(new Vector2D(0, 1)) - 1.5 * Math.PI;
                xArrow = 0;
                yArrow = (int) height - transformedArrowImage.getHeight();

                xPlayerIcon = halfArrowLength - playerIconWidth / 2;
                yPlayerIcon = (int) height - halfArrowLength - playerIconHeight / 2;

            }
            //top right corner
            else if (xPlayerMiddle > xRight - halfArrowLength && yPlayerBottom < yTop || xPlayerLeft > xRight && yPlayerMiddle < yTop + halfArrowLength) {

                Vector2D arrowVector = new Vector2D(xPlayerMiddle - (xRight - halfArrowLength), yPlayerMiddle - (yTop + halfArrowLength));
                theta = 0.5 * Math.PI - arrowVector.angle(new Vector2D(0, 1));

                xArrow = (int) width - transformedArrowImage.getWidth();
                yArrow = 0;

                xPlayerIcon = (int) width - halfArrowLength - playerIconWidth / 2;
                yPlayerIcon = halfArrowLength - playerIconHeight / 2;

            }
            //bottom right corner
            else if (xPlayerMiddle > xRight - halfArrowLength && yPlayerTop > yBottom || xPlayerLeft > xRight && yPlayerMiddle > yBottom - halfArrowLength) {

                Vector2D arrowVector = new Vector2D(xPlayerMiddle - (xRight - halfArrowLength), yPlayerMiddle - (yBottom - halfArrowLength));
                theta = 0.5 * Math.PI - arrowVector.angle(new Vector2D(0, 1));

                xArrow = (int) width - transformedArrowImage.getWidth();
                yArrow = (int) height - transformedArrowImage.getHeight();

                xPlayerIcon = (int) width - halfArrowLength - playerIconWidth / 2;
                yPlayerIcon = (int) height - halfArrowLength - playerIconHeight / 2;

            }

            //side


            //left side
            else if (xPlayerRight < xLeft) {

                theta = Math.PI;
                xArrow = 0;
                yArrow = yPlayerMiddle - transformedArrowImage.getHeight() / 2;

                xPlayerIcon = halfArrowLength - playerIconWidth / 2;
                yPlayerIcon = yPlayerMiddle - playerIconHeight / 2;

            }
            //right side
            else if (xPlayerLeft > xRight) {
                theta = 0;
                xArrow = (int) width - transformedArrowImage.getWidth();
                yArrow = yPlayerMiddle - transformedArrowImage.getHeight() / 2;

                xPlayerIcon = (int) width - halfArrowLength - playerIconWidth / 2;
                yPlayerIcon = yPlayerMiddle - playerIconHeight / 2;

            }
            //top side
            else if (yPlayerBottom < yTop) {

                theta = Math.PI * 1.5;

                xArrow = xPlayerMiddle - transformedArrowImage.getWidth() / 2;
                yArrow = 0;

                xPlayerIcon = xPlayerMiddle - playerIconWidth / 2;
                yPlayerIcon = halfArrowLength - playerIconHeight / 2;

            }
            //bottom side
            else if (yPlayerTop > yBottom) {

                theta = Math.PI * 0.5;
                xArrow = xPlayerMiddle - halfArrowLength;
                yArrow = (int) height - transformedArrowImage.getHeight();

                xPlayerIcon = xPlayerMiddle - playerIconWidth / 2;
                yPlayerIcon = (int) height - halfArrowLength - playerIconHeight / 2;

            }
            //not out of map
            else {
                isPlayerOutOfMap = false;
            }
            if (Main.DEBUG) {
                //graphics2D.fillPolygon(xPos, yPos, 3);
            }

            //calculate size of arrow
            if (isPlayerOutOfMap) {
                transformedArrowImage = rotatePicture(transformedArrowImage, theta);
                //draw arrow
                graphics2D.drawImage(transformedArrowImage, xArrow, yArrow, null);
                BufferedImage playerIcon = resizePicture(player.getAvatar().getImage(Avatar.NORMAL), playerIconFactor);
                graphics2D.drawImage(playerIcon, xPlayerIcon, yPlayerIcon, null);
            }
        }

        return imageExtract;
    }

    public void pause() {
        paused = !paused;
    }

    public void changeCameraRunning() {
        cameraRunning = !cameraRunning;
    }

    @Override
    public void keyTyped(KeyEvent keyEvent) {
    }

    @Override
    public void keyPressed(KeyEvent keyEvent) {
        char key = Character.toLowerCase(keyEvent.getKeyChar());

        if (!paused) {
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
    }

    @Override
    public void keyReleased(KeyEvent keyEvent) {
        char key = Character.toLowerCase(keyEvent.getKeyChar());

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

    public void updateKeys() {
        KeyBoardLayout keyBoardLayout = KeySetPanel.getKeyBoardLayoutFromFile();
        keys_player_1 = keyBoardLayout.getPlayer1Keys();
        keys_player_2 = keyBoardLayout.getPlayer2Keys();
    }

    public void setPanelActive() {
        Frame.getInstance().getContentPane().removeAll();
        Frame.getInstance().getContentPane().add(this);
        OurKeyListener.getInstance().setPanel(this);
    }

    private BufferedImage resizePicture(BufferedImage bufferedImage, double factor) {
        int width = bufferedImage.getWidth();
        int height = bufferedImage.getHeight();
        BufferedImage dest = new BufferedImage((int) (width * factor), (int) (height * factor), bufferedImage.getType());
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
