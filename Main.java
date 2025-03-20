import java.util.*;
import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;//handles graphics,fonts and colors
import java.awt.event.*;//handles keyboard inputs



class runTheGame {
    static String playerName;
    static boolean increaseSpeed = false;
    static boolean decreasePipeGap = false;
    static boolean fasterFall = false;


    public static void main (String[] arg){
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter your name: ");
        playerName = scanner.nextLine();
        if(playerName.isEmpty()){
            playerName = "Jetha lal";
            System.out.println("Welcome "+playerName);
        }
        else{
            System.out.println("Welcome "+playerName);
        }
        System.out.println("Choose difficulty settings:-");
        System.out.println("1. Increase Bird Speed");
        System.out.println("2. Decrease Pipe Gap");
        System.out.println("3. Increase Fall Speed");
        System.out.println("4. Start Game");
         int count =0;
         boolean check = true ;
        while (check) {

            System.out.print("Enter option (1-5, or 5 to start): ");

            int choice = scanner.nextInt();
            switch (choice) {
                case 1 :
                    increaseSpeed = true;
                System.out.println("===> With every 5 points scored, the bird moves faster, making the game more challenging!");
                ++count;
                if(count>1){
                    System.out.println("===> You are Making the Game unPlayable , ARE YOU PROFESSIONAL BIRD? <====");
                }
                break;
                case 2 :
                    decreasePipeGap = true;
                    System.out.println("==> So you are not CLAUSTROPHOBIC!!!  ");
                    ++count;
                    if(count>1){
                        System.out.println("===>You are Making the Game unPlayable , ARE YOU PROFESSIONAL BIRD??");
                    }
                    break;
                case 3 :
                    fasterFall = true;
                    System.out.println("===> Not scared of height ?!");
                    ++count;
                    if(count>1){
                        System.out.println("===>You are Making the Game unPlayable , ARE YOU PROFESSIONAL BIRD??<===");
                    }
                break;
                case 4 :
                    System.out.println("Starting game...");
                    System.out.println("Game Started , Please Use Your Mouse And Open Minimized Frame ");
                    check = false;
                    new MyFrame();
                    break;
                default :
                    System.out.println("Invalid choice, try again.");
                    break;
            }
        }

    }

}
class MyFrame extends JFrame {

      int boardWidth = 360;
      int boardHeight = 640;


    MyFrame(){
        ImageIcon logo = new ImageIcon("C:\\Users\\tanaz\\Downloads\\logo.png");
        Panel p = new Panel();
        this.add(p);
        p.requestFocus();
        p.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                p.keyPressed(e);  // Handles the keypress in the Panel class
            }
        });
      this.setSize(boardWidth,boardHeight);//sets Our Frame Size
        this.setIconImage(logo.getImage());
        this.setAlwaysOnTop(true);  // Forces window to be on top
        this.setVisible(true);
        this.setAlwaysOnTop(false); // Removes forced focus
        this.setTitle("Flappy Bird");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();
        this.setLocationRelativeTo(null);//automatically open's frame in center
        this.setResizable(false);//prevents user from Manipulating the size
    }
}
class Panel extends JPanel {
       int boardWidth= 360;
       int boardHeight = 640;

    Image backgroundImg;
    Image birdImg;
    Image topPipeImg;
    Image bottomPipeImg;

    //Bird Alignments
    int birdX=boardWidth/8;
    int birdY=boardHeight/2;
    int birdWidth = 42;
    int birdHeight=32;

    boolean gameOver=false;
    double score=0;


    //inner class
    class Bird
    {
        int x = birdX;
        int y = birdY;
        int height=birdHeight;
        int width = birdWidth;

        Image img;
        Bird(Image img ){
        this.img = img;
        }
    }
    //PIPES ALIGNMENT
    int pipeX=boardWidth;//sets pipe's x position(360)
    int pipeY=0;
    int pipeWidth=64;
    int pipeHeight=512;
    class Pipe {
      int x = pipeX;
      int y = pipeY;
      int width=pipeWidth;
      int height=pipeHeight;
      Image img;
      boolean passed =false;//to check if bird has passed the pipe
        Pipe (Image img ){
        this.img = img ;//This basically helps rendering pipes by drawing with change in frames
        }
    }
    //game logic
       Bird bird;
     Timer gameLoop;//creates timer
     Timer placePipesTimer;
    int velocityX=-4;//assume it as pipes are moving 4 pixels left side per frame
    void updateDifficulty() {
        velocityX = -4 - (int)(score / 5); // Speed increases every 5 points
    }
    int velocityY=0;//moves birds upwards,no x needed since bird only moves up words and down words
    int gravity = 1;
    Pipe[] pipes = new Pipe[100000];// Fixed size array for pipes
    int pipeIndex = 0;

    Panel(){

           //We can use without that p.command since entire JPanel is extended as object to the class
           setPreferredSize(new Dimension(boardWidth,boardHeight));//creates panel and sets dimension
          // setBackground(Color.blue);//sets panel color
            setFocusable(true);

         //load Images of background
         backgroundImg = new ImageIcon(getClass().getResource("flappybirdbg.png")).getImage();
         birdImg = new ImageIcon(getClass().getResource("flappybird.png")).getImage();
         topPipeImg = new ImageIcon(getClass().getResource("toppipe.png")).getImage();
         bottomPipeImg = new ImageIcon(getClass().getResource("bottompipe.png")).getImage();

         bird = new Bird(birdImg);

         //pipes Timer
         placePipesTimer =new Timer(1500 , new ActionListener() {
             public void actionPerformed(ActionEvent e) {
                 placePipes();//calling the pipe placing method given below
             }
         });
         placePipesTimer.start();
         //game timer
        gameLoop = new Timer(1000 / 60, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                move();
                repaint();
                if (gameOver) {
                    placePipesTimer.stop();
                    gameLoop.stop();
                }
            }
        });//sets fps rn it is 60fps also make sure there is library for timer is  imported
       gameLoop.start();
       }
       void placePipes(){
         //(0-1)*pipeHeight/2-->(0-256)    (math .random and pipeheight does this)
           //0-128-(0-1)*256
         int randomPipeY=(int)(pipeY-pipeHeight/4-Math.random()*pipeHeight/2);
         int openingSpace=boardHeight/4;
         if(runTheGame.decreasePipeGap){
             openingSpace = Math.max(boardHeight / 4 - (int)(score / 5), 96);
         }
         Pipe topPipe = new Pipe(topPipeImg);//made a constructor of class pipe where i am passing top pipe img
           pipes[pipeIndex++]=(topPipe);//here i added topPipe to the array  i just created
           topPipe.y=randomPipeY;//changes draw method's y

           Pipe bottomPipe=new Pipe(bottomPipeImg);
           bottomPipe.y= topPipe.y+pipeHeight+openingSpace;//we will get Y position of bottom pipe imagine graph in terms of swing frame
           pipes[pipeIndex++]=(bottomPipe);
       }

    void move(){
         velocityY+=gravity;
        bird.y+=velocityY;
       bird.y=Math.max(bird.y,0);//original doesn't have limit border ur choice though
        if(runTheGame.increaseSpeed) {
            updateDifficulty();//I just love my self for this feature
        }
        if(runTheGame.fasterFall) {
            velocityY += 1; // Extra gravity effect
        }
        //pipes
        for(int i =0 ; i<pipeIndex;i++){
            Pipe pipe = pipes[i];
            pipe.x+=velocityX;//moves pipes
            if(!pipe.passed && bird.x>pipe.x+pipe.width){
                score+=0.5;//0.5 because there are tow pipes so score is divided between them
                pipe.passed=true;
            }
            if(collision(bird , pipe)){
              gameOver=true;
            }
        }
        if (bird.y>boardHeight){
            gameOver=true;
        }
       }
       //use of Graphics that is included in awt package  to draw shape,image,text etc.....
    //had to use public keyWord
    //got to know after bit reaserch that method underneath is best for continues rendering of images for games like flappy bird
      public void  paintComponent(Graphics g){
         super.paintComponent(g);
         draw(g);
    }
  void draw(Graphics g){
         //background
      g.drawImage(backgroundImg,0,0,boardWidth,boardHeight , null);
      //bird
      g.drawImage(bird.img,bird.x, bird.y , bird.width,bird.height,null);
      //pipes
      for (int i =0 ; i< pipeIndex;i++){
          Pipe pipe = pipes[i];
          g.drawImage(pipe.img,pipe.x,pipe.y,pipe.width,pipe.height,null);
      }
      //Statements

      if (gameOver && score!=0) {
         g.setColor(Color.red);
          g.setFont(new Font("04b_19", Font.BOLD, 20));
          g.drawString("(Game Over) Score: " + (int) score, 30, 200);
          g.drawString ("Press SPACE to restart " ,30,220 );

      } else if(score==0 && gameOver){
          g.setColor(Color.WHITE);
          g.setFont(new Font("04b_19",Font.BOLD ,20 ));
          g.drawString("MOUSECLICK On Frame and,",30,200);
          g.drawString("Press SPACE to start",30,220);
      }
      else{
          g.setFont(new Font("04b_19",Font.BOLD ,35));
          g.drawString("Score:"+((int) score), 10, 35);
      }
   }
     boolean collision (Bird a ,Pipe b ){
        //you can also use if
         return a.x < b.x + b.width &&
                 a.x + a.width > b.x &&
                 a.y < b.y + b.height &&
                 a.y + a.height > b.y;
     }
     void keyPressed(KeyEvent e) {
     if(e.getKeyCode()==KeyEvent.VK_SPACE) {

         if(runTheGame.fasterFall){
             velocityY = -14;
         }
         else{
             velocityY = -9;
         }
         if (gameOver) {
             //restart game by resetting conditions
             bird.y = birdY;
             bird.x = birdX;
             velocityX = -4; // Resets speed
             velocityY = 0;
             pipeIndex=0;
             gameOver = false;
             score = 0;
             gameLoop.start();
             placePipesTimer.start();
         }
     }
    }
    }
