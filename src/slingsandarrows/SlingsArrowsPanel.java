/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package slingsandarrows;

import java.awt.*; 
import java.awt.event.ActionEvent; 
import java.awt.event.ActionListener; 
import java.awt.event.KeyAdapter; 
import javax.swing.Timer; 
import java.awt.GraphicsEnvironment;
import java.awt.event.KeyEvent; 
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import sun.audio.AudioPlayer;
import sun.audio.AudioStream;

//import java.net.*;

/**
 * Grace Guo 
 * January 20th 2020
 * Mr.Payne
 */

/**
 * Slings and arrows is a multi-player game where 2 players gave can shoot arrows or 
 * bullets at each other, with the objective of killing the other player.
 * 
 */

/*
    ELEVEN FEATURES

    Importing and utilizing custom font 
    Importing and playing different sound effects for each collisions
    Players with Healthbar 
    Introduction Screen 
    Customizing player Usernames
    Accessing a URL from java for showing instructions 
    
*/

public class SlingsArrowsPanel extends javax.swing.JPanel {
    
    //initilaizing variables for images
    Image player1, player2, bullet, arrow, bullet2, arrow2, player1Dead, player2Dead, thought, thought1, healthBar, health2Bar, backG; 
    
    //Variable for the image updating timer 
    Timer timer1;
    
    int curKeyP, curKeyR; //tracking the key pressed and released respectively 
    
    boolean flyingBullet, flyingArrow; //checking whether bullet/arrow have been shot by player 1
    boolean flying2Bullet, flying2Arrow; //checking whether bullet/arrow have been shot by player 2
    
    boolean player1Death, player2Death; //checking whether players have been shot
    int deathCount = 0; //timer for no movement
    
    boolean [] moveKeys = new boolean [4]; //arrays to check player movement 
    boolean [] shootKeys = new boolean [4]; //arrays to check player shooting
    
    boolean movingRight, moving2Right; //checking direction of player before shooting arrow 
    boolean movingRightNow, moving2RightNow;
    
    boolean collRock1, collRock2, collArrow1, collArrow2; /////////////////////NEW SET OF BOOLEANS HERE
    
    int bulletX1, bulletY1, bulletX2, bulletY2; //bullet x and y positions 
    
    int arrowX1, arrowY1, arrowX2, arrowY2; //arrow x and y positions  
    
    int player1x = 60, player1y = 75, player1xNow; //player 1 x and y positions 
    int player2x = 300, player2y = 570, player2xNow; //player 2 x and y positions 
    
    //player image dimensions 
    int player1Width = 75, player1Height = 162; 
    int player2Width = 80, player2Height = 162; 
    
    int bulletWidth = 40; //bullet/arrow image dimension 
    int speed = 7; //speed of player movement 
 
    int choosingResponse; 
    String [] deadResponse = new String [5]; //array for responses when shot 
    String p2DeadResp, p1DeadResponse; //variable to store response 
    
    int health1Length = 120, health2Length = 120; //player health
    
    boolean playingGame = true; //check if game is being played 
    boolean player1Win, player2Win; //checking which player won 
    
    boolean instructScreen = true; //displaying instruction screen  
    
    String winner,loser, p1Name, p2Name; 
    
    Font font, fontTitle; //variable for registering and displying fonts 
    
    public SlingsArrowsPanel() {
        initComponents();

        try{
            //Importing custom fonts into the Graphics Enviornment 
            font = Font.createFont(Font.TRUETYPE_FONT, new File ("INFO56_0.TTF")).deriveFont(24f);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File("INFO56_0.TTF")));

            fontTitle = Font.createFont(Font.TRUETYPE_FONT, new File ("ARCADECLASSIC.TTF")).deriveFont(60f);
            ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File("ARCADECLASSIC.TTF")));
        }
        catch (FontFormatException | IOException e){
            e.printStackTrace();
        }
        
        jDialog1.setVisible(false);
        
        //Importing images into Java 
        backG = Toolkit.getDefaultToolkit().getImage("backGr.jpg"); 

        player1 = Toolkit.getDefaultToolkit().getImage("player1.png"); 
        player2 = Toolkit.getDefaultToolkit().getImage("player2.PNG"); 
        bullet = Toolkit.getDefaultToolkit().getImage("missileRE1.png"); 
        arrow = Toolkit.getDefaultToolkit().getImage("arrowRE1.png");
        
        bullet2 = Toolkit.getDefaultToolkit().getImage("missileRE2.png");
        arrow2 = Toolkit.getDefaultToolkit().getImage("arrowRE2.png");
        
        healthBar = Toolkit.getDefaultToolkit().getImage("healthBar.png");
        health2Bar = Toolkit.getDefaultToolkit().getImage("healthBar.png");
        
        player2Dead = Toolkit.getDefaultToolkit().getImage("play2Dead.PNG"); 
        player1Dead = Toolkit.getDefaultToolkit().getImage("play1Dead.png"); 
        thought = Toolkit.getDefaultToolkit().getImage("thoughtRE.png"); 
        thought1 = Toolkit.getDefaultToolkit().getImage("thoughtRE2.png"); 

        //focusing on a component 
        setFocusable(true); 
        addKeyListener(new SlingsArrowsPanel.MKeyListener()); //adding a key listener
        
        //initializing and starting the timer for updating the images 
        timer1 = new Timer(10,new TimerListener()); 
        timer1.start(); 
        
        //Array of responses for when player collisions occur
        deadResponse [0] = "Why me?"; 
        deadResponse [1] = "NOOooo...!"; 
        deadResponse [2] = "I demand a rematch!"; 
        deadResponse [3] = "Et tu, Brutus?"; 
        deadResponse [4] = "You'll regret this!"; 
    }
    
    public class MKeyListener extends KeyAdapter{
        @Override
        
        public void keyPressed(KeyEvent e){
            curKeyP = e.getKeyCode(); 
            
            if (curKeyP == e.VK_LEFT){ //left movement for player 1
                moveKeys[0] = true;
            }
            if (curKeyP == e.VK_RIGHT){ //right movement for player 1
                moveKeys[1] = true; 
            }
            if (curKeyP == e.VK_A){ //left movement for player 2
                moveKeys[2] = true; 
            }
            if (curKeyP == e.VK_D){ //right movement for player 2 
                moveKeys[3] = true; 
            }
            if (curKeyP == e.VK_K){ //shooting bullet for player 1
                shootKeys[0] = true; 
            }
            if (curKeyP == e.VK_L){ //shooting arrow for player 1 
                shootKeys[1] = true; 
            }
            if (curKeyP == e.VK_G){ //shooting bullet for player 2
                shootKeys[2] = true; 
            }
            if (curKeyP == e.VK_H){ //shooting arrow for player 2 
                shootKeys[3] = true; 
            }
        } 
    
        @Override
        public void keyReleased (KeyEvent e) {
            curKeyR = e.getKeyCode(); 
            
            if (curKeyR == e.VK_LEFT){
                moveKeys[0] = false;
            }
            if (curKeyR == e.VK_RIGHT){
                moveKeys[1] = false; 
            }
            if (curKeyR == e.VK_A){
                moveKeys[2] = false; 
            }
            if (curKeyR == e.VK_D){
                moveKeys[3] = false; 
            }
            if (curKeyR == e.VK_K){     
                shootKeys [0] = false; 
            }
            if (curKeyR == e.VK_L){
                shootKeys[1] = false; 
            } 
            if (curKeyR == e.VK_G){
                shootKeys[2] = false; 
            }
            if (curKeyR == e.VK_H){
                shootKeys[3] = false; 
            }
        }
    }
    
    private class TimerListener implements ActionListener{       
        @Override
        public void actionPerformed(ActionEvent ae){
            
            //events that enabled when the users are on the game play screen 
            if (playingGame == true)
            {
                //events that are enabled when the players are not dead
                if (deathCount == 0){
                    if (moveKeys[0] && player1x > 0){
                        //moving player 1 left when within the jPanel 
                        movingRight = false; 
                        player1x = player1x - speed; 
                    }
                    if (moveKeys[1] && player1x < 1021){
                        //moving player 1 right when within the jPanel
                        movingRight = true; 
                        player1x = player1x + speed; 
                    }
                    if (moveKeys[2] && player2x > 0){
                        //moving player 2 left when within the jPanel 
                        moving2Right = false; 
                        player2x = player2x - speed; 
                    }
                    if (moveKeys[3] && player2x < 1021){
                        //moving player 2 right when within the jPanel 
                        moving2Right = true; 
                        player2x = player2x + speed; 
                    }
                    if (shootKeys[0] && flyingBullet == false){
                        //putting player 1 bullet into the jPanel
                        bulletX1 = player1x + 25; 
                        bulletY1 = player1y; 

                        flyingBullet = true; 
                    }
                    if (shootKeys[1] && flyingArrow == false){
                        //putting the player 1 arrow into the jPanel 
                        movingRightNow = movingRight; 
                        arrowX1 = player1x + 25; 
                        arrowY1 = player1y; 

                        player1xNow = player1x + 25;
                        flyingArrow = true; 
                    } 
                    if (shootKeys[2] && flying2Bullet == false){ 
                        //putting player 2 bullet into the jPanel
                        bulletX2 = player2x + 25; 
                        bulletY2 = player2y; 
                        
                        flying2Bullet = true;  
                    }
                    if (shootKeys[3] && flying2Arrow == false){
                        //putting the player 2 arrow into the jPanel
                        moving2RightNow = moving2Right;
                        arrowX2 = player2x + 25; 
                        arrowY2 = player2y; 
                        
                        player2xNow = player2x + 25; 
                        flying2Arrow = true; 
                    }
                } 
                else{
                    //count down the timer for playing staying dead 
                    deathCount --; 
                }
            }
            repaint();
        }
    }
    
    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        
        //Graphics for the instruction screen 
        checkGraphics(); 
        
        g.drawImage(backG, 0, 0, this); //drawing the background 
        
        //Graphics drawn when gameplay is occurring 
        if (playingGame == true){
            g.setColor(Color.GREEN); 
            g.fillRect(87, 24, health1Length, 27); 
            g.drawImage(healthBar, 50, 0, this); //player 1 healthbar 
            g.fillRect(87, 775, health2Length, 27); 
            g.drawImage(health2Bar, 50, 750, this); //player 2 healthbar 
            
            if (instructScreen == false){ //showing the players entered username when the game starts 
                g.setColor(Color.WHITE); 
                g.setFont(fontTitle);
                g.drawString(p1Name, 250, 57);
                
                g.drawString(p2Name, 250, 807);
            }
            
            if (deathCount == 0){ //images diplayed when no one has been shot 
                g.drawImage(player2, player2x, player2y, this); //regular player 2 image 
                g.drawImage(player1, player1x, player1y, this); //regular player 1 image 
            }
            else{
                if (player2Death == true){ //images displayed when player 2 has been shot 
                    g.drawImage(player2Dead, player2x, player2y + (player2Height - player2Width), this); //dead player 2 image 
                    g.drawImage(thought, player2x + 30, player2y, this); //thought bubble image 
                    p2DeadResp = deadResponse [choosingResponse]; //choosing a random thought for player 2

                    g.setColor(Color.BLACK); 
                    g.setFont(font); 
                    g.drawString(p2DeadResp, player2x + 130, player2y + 40); //showing the thought 
                    g.drawImage(player1, player1x, player1y, this); //regular player 1 image 
                }
                if (player1Death == true){ //images displayed when player 1 has been shot 
                    
                    g.setColor(Color.BLACK);
                    g.drawImage(player1Dead, player1x, player1y + (player1Height - player1Width), this); //dead player 1 image 
                    g.drawImage(thought1,player1x + 30, player1y + 150 ,this); //thought bubble image
                    p1DeadResponse = deadResponse [choosingResponse]; //choosing a random thought for player 1
                    
                    g.setFont(font); 
                    g.drawImage(player2, player2x, player2y, this); //regular player 2 image 
                    g.drawString(p1DeadResponse,player1x + 130, player1y + 210);//showing the thought 
                }
            } 
            
            //collision of player 1's bullet with player 2 
            if (bulletX1 >= (player2x - bulletWidth) && bulletX1 <= (player2x + player2Width + bulletWidth) && bulletY1 >= player2y && bulletY1 <= (player2y + player2Height)){
                
                collRock1 = true;
                collRock2 = false; 
                collArrow1 = false; 
                collArrow2 = false; 
                
                collison(); 
            }
            else{

                if (bulletY1 > 0 && bulletY1 < getHeight()){ //when player 1 bullet is within the jPanel 
                    if (flyingBullet == true){ //when the bullet has been shot 
                        g.drawImage(bullet, bulletX1, bulletY1, this); //show player 1 bullet on screen 
                        bulletY1 = bulletY1 + 5; //move the bullet down 
                    }
                }
                else{
                    flyingBullet = false; 
                    bulletY1 = -20; //move the bullet out of the screen 
                }
            }
            
            //collision of player 2's bullet with player 1
            if (bulletX2 >= (player1x-bulletWidth) && bulletX2 <= (player1x + player2Width + bulletWidth) && bulletY2 >= player1y && bulletY2 <= (player1y + player1Height)){
                
                collRock1 = false;
                collRock2 = true; 
                collArrow1 = false; 
                collArrow2 = false; 
                
                collison(); 
                
            }
            else{
                if (bulletY2 < getHeight() && bulletY2 > 0){ //when player 2 bullet is within the jPanel 
                    if (flying2Bullet == true){
                        g.drawImage(bullet2, bulletX2, bulletY2, this); //show player 2 bullet on screen 
                        bulletY2 = bulletY2 - 5; //move the bullet up 
                    }
                }
                else{
                    flying2Bullet = false; 
                    bulletY2 = getHeight() +100; //move the bullet out of the screen 
                }
            }
            
            //collision of player 1's arrow with player 2 
            if (arrowX1 >= (player2x - bulletWidth) && arrowX1 <= (player2x + player2Width + bulletWidth) && arrowY1 >= player2y && arrowY1 <= (player2y + player2Height)){
                
                collRock1 = false;
                collRock2 = false; 
                collArrow1 = true; 
                collArrow2 = false; 
                
                collison(); 
            }
            else{
                if (arrowY1 > 0 && arrowY1 < getHeight()){ //when player 1 arrow is within the jPanel 
                    if (flyingArrow == true){ 
                        g.drawImage(arrow, arrowX1, arrowY1, this); //draw player 1 arrow on screen 
                        fireArrow(); //method for player 1's arrow movement
                    }
                }
                else{
                    flyingArrow = false; 
                    arrowY1 = -20; //move the arrow out of the screen 
                }
            }
            
            //collision of player 2's arrow with player 1
            if (arrowX2 >= (player1x-bulletWidth) && arrowX2 <= (player1x + player1Width + bulletWidth) && arrowY2 >= player1y && arrowY2 <= (player1y + player1Height)){
                
                collRock1 = false;
                collRock2 = false; 
                collArrow1 = false; 
                collArrow2 = true; 
                
                collison(); 
            }
            else{
                if (arrowY2 < getHeight() && arrowY2 > 0){ //when player 2 arrow is within the jPanel 
                    g.drawImage(arrow2, arrowX2, arrowY2, this); //draw player 1 arrow on screen 
                    fire2Arrow(); //method for player 2's arrow movement
                }
                else{
                    flying2Arrow = false; 
                    arrowY2 = getHeight() + 100; //move the arrow out of the screen 
                }
            }
        }
        //when a player fully dies 
        else{
            playerDeath(); 
        }
    }
    
    public void checkGraphics(){
        if (instructScreen == true){
            jDialog2.setVisible(true); //showing the introduction/instruction screen 
            jLabelTitle.setFont(fontTitle);
            p1User.setFont(font); 
            p2User.setFont(font);
            playButton.setFont(font);  
            bInstruct.setFont(font); 
        }
        else{
           jDialog2.setVisible(false);
        }
        
        
        if (health1Length == 0){ //player 1 healthbar is fully drain 
                    playingGame = false; //show end screen 
                    
                    player2Win = true; //end screen display payer 2 wins and player 1 loses
                    player1Win = false; 
                }
        if (health2Length == 0){ //player 2 healthbar is fully drain 
                    playingGame = false; //show the end screen 
                    
                    player1Win = true; //for end screen display player 1 wins and player 2 loses 
                    player2Win = false; 
        }
    }
    
    public void collison(){
        
        if (collRock1 == true||collArrow1 == true){
            if(collRock1 == true){
                playMusic("player2Lose.wav"); //play sound effect 
                health2Length -= 30; //player 2 lose health 
                bulletY1 = -20; //move bullet out of jPanel 

                deathCount = 100; //pause player movement 
                choosingResponse = (int) (Math.random() * 5); //choose a random response for the thought bubble 

                player2Death = true; //player 2 is shot 
                player1Death = false; 
            }
            else{
                playMusic("player2Lose.wav"); //player sound effect 
                health2Length -= 30;//player 2 lose health 
                arrowY1 = -20; //move arrow out of jPanel
                
                deathCount = 100; //pause player movement 
                choosingResponse = (int) (Math.random() * 5); //choose a random response for the thought bubble 

                player2Death = true; //player 2 is shot 
                player1Death = false;
            }
        }
        else{
            if (collRock2 == true){
                playMusic("player1Lose.wav"); //play sound effect 
                health1Length -= 30; //player 1 lose health 
                
                bulletY2 = getHeight() + 100; //move bullet out of jPanel
                deathCount = 100; //pause player movement 
                choosingResponse = (int) (Math.random() * 5); //choose a random response for the thought bubble 

                player1Death = true; //player 1 is shot 
                player2Death = false;
            }
            else{
                playMusic("player1Lose.wav"); //play sound effect 
                
                health1Length -= 30; //player 1 lose health 
                arrowY2 = getHeight() + 100; //move arrow out of jPanel
                deathCount = 100; //pause player movement 
                choosingResponse = (int) (Math.random() * 5); //choose a random response for the thought bubble 

                player1Death = true; //player 1 is shot 
                player2Death = false;
            }
        }
    }
    
    public void playerDeath(){
        jDialog1.setVisible(true); //show end screen 
            player1Result.setFont(font); 
            player2Result.setFont(font);
            jBExit.setFont(font); 
            jBPlayAgain.setFont(font); 
            
            if (player1Win == true){ 
                //player 1 won the game 
                player1Result.setText(p1Name + " Wins!"); //display player 1 wins, player 2 loses 
                player2Result.setText(p2Name + " Loses!"); 
            }
            else{
                player1Result.setText(p1Name + " Loses!"); //display player 2 wins, player 1 loses 
                player2Result.setText(p2Name + " Wins!");  
            }
    }
    
    //method for player 1 firing arrow 
    public void fireArrow(){         
        arrowY1 = (int)(0.005*(int)Math.pow((arrowX1 - player1xNow),2) + player1y); //set player 1 arrow to a parabolic function 
        
        if (movingRightNow == true){ //player 1 moving right before shooting 
            arrowX1 = arrowX1 + 7; //move player 1 arrow x position to the right 
        }
        else{ //player 1 pressed left before shooting 
            arrowX1 = arrowX1 - 7; //move player 1 arrow x position to the left 
        }    
    }
    
    //method for player 2 firing arrow 
    public void fire2Arrow(){         
        arrowY2 = (int)(-0.005*(int)Math.pow((arrowX2 - player2xNow),2) + player2y);//set player 1 arrow to a parabolic function 
        
        if (moving2RightNow == true){ //player 2 moving right before shooting 
            arrowX2 = arrowX2 + 7; //move player 2 arrow x position to the right 
        }
        else{ //player 2 pressed left before shooting 
            arrowX2 = arrowX2 - 7; //move player 2 arrow x position to the left 
        }  
    }
    
    //method for playing sound effects 
    public static void playMusic(String filepath){
        InputStream music; 
        try{
            music = new FileInputStream(new File(filepath)); //initilaize variable to store sound effect file 
            AudioStream audios = new AudioStream(music); //create a new audio stream 
            AudioPlayer.player.start (audios); //start the player 
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jDialog1 = new javax.swing.JDialog();
        jBPlayAgain = new javax.swing.JButton();
        jBExit = new javax.swing.JButton();
        player1Result = new javax.swing.JLabel();
        player2Result = new javax.swing.JLabel();
        jDialog2 = new javax.swing.JDialog();
        p1Username = new javax.swing.JTextField();
        p1User = new javax.swing.JLabel();
        p2User = new javax.swing.JLabel();
        p2Username = new javax.swing.JTextField();
        jLabelTitle = new javax.swing.JLabel();
        playButton = new javax.swing.JButton();
        bInstruct = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();

        jDialog1.setMinimumSize(new java.awt.Dimension(400, 400));

        jBPlayAgain.setText("Play Again");
        jBPlayAgain.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBPlayAgainActionPerformed(evt);
            }
        });

        jBExit.setText("Exit");
        jBExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBExitActionPerformed(evt);
            }
        });

        player1Result.setText("player 1 result");

        player2Result.setText("player 2 result");

        javax.swing.GroupLayout jDialog1Layout = new javax.swing.GroupLayout(jDialog1.getContentPane());
        jDialog1.getContentPane().setLayout(jDialog1Layout);
        jDialog1Layout.setHorizontalGroup(
            jDialog1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDialog1Layout.createSequentialGroup()
                .addContainerGap(44, Short.MAX_VALUE)
                .addGroup(jDialog1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jDialog1Layout.createSequentialGroup()
                        .addComponent(jBPlayAgain, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jBExit, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(44, 44, 44))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jDialog1Layout.createSequentialGroup()
                        .addGroup(jDialog1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(player2Result, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(player1Result, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(79, 79, 79))))
        );
        jDialog1Layout.setVerticalGroup(
            jDialog1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jDialog1Layout.createSequentialGroup()
                .addContainerGap(108, Short.MAX_VALUE)
                .addComponent(player1Result, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(45, 45, 45)
                .addComponent(player2Result, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(66, 66, 66)
                .addGroup(jDialog1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jBPlayAgain)
                    .addComponent(jBExit))
                .addGap(86, 86, 86))
        );

        jDialog2.setMinimumSize(new java.awt.Dimension(620, 500));

        p1Username.setText("Player 1");

        p1User.setText("Player 1 Username:");

        p2User.setText("Player 2 Username:");

        p2Username.setText("Player 2");

        jLabelTitle.setText("Slings And Arrows");

        playButton.setText("READY TO PLAY!");
        playButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                playButtonActionPerformed(evt);
            }
        });

        bInstruct.setText("INSTRUCTIONS");
        bInstruct.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bInstructActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jDialog2Layout = new javax.swing.GroupLayout(jDialog2.getContentPane());
        jDialog2.getContentPane().setLayout(jDialog2Layout);
        jDialog2Layout.setHorizontalGroup(
            jDialog2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDialog2Layout.createSequentialGroup()
                .addContainerGap(55, Short.MAX_VALUE)
                .addGroup(jDialog2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jDialog2Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jLabelTitle, javax.swing.GroupLayout.PREFERRED_SIZE, 558, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jDialog2Layout.createSequentialGroup()
                        .addGroup(jDialog2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jDialog2Layout.createSequentialGroup()
                                .addGroup(jDialog2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(p2User)
                                    .addComponent(p1User))
                                .addGap(18, 18, 18)
                                .addGroup(jDialog2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(p1Username, javax.swing.GroupLayout.PREFERRED_SIZE, 332, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(p2Username, javax.swing.GroupLayout.PREFERRED_SIZE, 332, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jDialog2Layout.createSequentialGroup()
                                .addComponent(playButton, javax.swing.GroupLayout.PREFERRED_SIZE, 222, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(bInstruct, javax.swing.GroupLayout.PREFERRED_SIZE, 207, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jDialog2Layout.setVerticalGroup(
            jDialog2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDialog2Layout.createSequentialGroup()
                .addGap(44, 44, 44)
                .addComponent(jLabelTitle, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jDialog2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(p1Username, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(p1User))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jDialog2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(p2User)
                    .addComponent(p2Username, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(115, 115, 115)
                .addGroup(jDialog2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(playButton)
                    .addComponent(bInstruct))
                .addContainerGap(90, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        setMinimumSize(new java.awt.Dimension(400, 400));
        setPreferredSize(new java.awt.Dimension(600, 600));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jBPlayAgainActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBPlayAgainActionPerformed
        //pressing button for playing again 
        
        //stop all moevemnt of players 
        moveKeys[0] = false; 
        moveKeys[1] = false; 
        moveKeys[2] = false; 
        moveKeys[3] = false; 
        shootKeys[0] = false; 
        shootKeys[1] = false; 
        shootKeys[2] = false; 
        shootKeys[3] = false;
        
        //remove all images of bullets and arrow 
        flyingBullet = false;
        flying2Bullet = false; 
        flyingArrow = false; 
        flying2Arrow = false; 
        
        //set the positions of all bullets and arrows out of the jPanel 
        bulletY1 = -20; 
        bulletY2 = getHeight() + 100; 
        arrowY1 = -20; 
        bulletY2 = getHeight() + 100; 
 
        //reset player to be all alive 
        deathCount = 0;
        
        //reset helthbars to be full 
        health1Length = 120; 
        health2Length = 120; 
        
        //close the end screen 
        jDialog1.setVisible(false);
        
        //clear the results of winning and losing 
        player1Result.setText(""); 
        player2Result.setText(""); 
        
        //start the game 
        playingGame = true;
        //display the instructions screen 
        instructScreen = true; 
    }//GEN-LAST:event_jBPlayAgainActionPerformed

    private void jBExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBExitActionPerformed
        //pressing button for exiting game 
        
        System.exit(0); //exit the game 
    }//GEN-LAST:event_jBExitActionPerformed

    private void playButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_playButtonActionPerformed
       
        //pressing button for playing the game 
        
        //checking for empty usernames 
        if ((p1Username.getText()).equals("")){
            p1Username.setText("Player 1"); //set username to Player 1 for empty textbox
        }
        if ((p2Username.getText()).equals("")){
            p2Username.setText("Player 2"); //set username to Player 2 for empty textbox
        }
        
        //set the players username to the inputted username in textbox
        p1Name = p1Username.getText(); 
        p2Name = p2Username.getText(); 
        
        //close the instructions screen 
        instructScreen = false; 
    }//GEN-LAST:event_playButtonActionPerformed

    private void bInstructActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bInstructActionPerformed
        //pressing button brings user to the instructions screen 
        try {
            Desktop desktop = java.awt.Desktop.getDesktop();
            URI website = new URI("https://docs.google.com/document/d/19W_gOh06IKlnGUGD8KeYvdjOoqa2Ub9D69W_dBuqqQU/edit");// your url here
            desktop.browse(website); //bring the user to the instuctions document 
        } catch (Exception e) {
            e.printStackTrace();
        }

    }//GEN-LAST:event_bInstructActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton bInstruct;
    private javax.swing.JButton jBExit;
    private javax.swing.JButton jBPlayAgain;
    private javax.swing.JDialog jDialog1;
    private javax.swing.JDialog jDialog2;
    private javax.swing.JLabel jLabelTitle;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel p1User;
    private javax.swing.JTextField p1Username;
    private javax.swing.JLabel p2User;
    private javax.swing.JTextField p2Username;
    private javax.swing.JButton playButton;
    private javax.swing.JLabel player1Result;
    private javax.swing.JLabel player2Result;
    // End of variables declaration//GEN-END:variables
}
