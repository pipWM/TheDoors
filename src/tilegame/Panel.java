
package tilegame;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import javax.imageio.ImageIO;
import javax.swing.JPanel; 
import tilegame.MapChip.Move;
import tilegame.MapChip.Tuuka;

class Panel extends JPanel implements KeyListener, Runnable{
    public static Thread th = new Thread(new Panel());
    public static int MX = 36;
    public static int MY = 26;
    public static MapChip map[][] = new MapChip[MY][MX];
    public static String map_str[] = new String[MY];
    
    public static int maxstage = 3;     //ステージの数だけ変える
    
    public static int ClearData[] = new int[maxstage];

    int music_number = 2;//bgmの数だけ変える
   
    public static Player player;
    public static int gamestate;
    public static int nowstage = 1;
    public static int endgame = 0;
    public static int door = 0;
    public static int retry = 0;
   
    Panel(){
	setFocusable(true);
        addKeyListener(this);
        setFocusable(true);
        GameStart();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        switch (gamestate){
            case (0):
                UI.UIStartPanel(key);
                break;
            case (1):  
                UI.UIGamePanel(key);
                break;
            case 2://リトライ画面
                 UI.UIRetryPanel(key);          
                break;
            case 3://クリア
                UI.UIClearPanel(key);
                break;
       }
    }
    
  @Override
    public void keyReleased(KeyEvent e) { 
        int key = e.getKeyCode();
        switch(gamestate){
            case 0:
                if(key == KeyEvent.VK_SPACE) endgame = 1;
                break;
            case 1:
                switch ( key ) {
                    case KeyEvent.VK_RIGHT:
                        Player.right = false;
                        break;
                    case KeyEvent.VK_DOWN:
                        Player.down = false;
                        break;
                    case KeyEvent.VK_SPACE:
                        Player.up = false;
                        break;
                    case KeyEvent.VK_LEFT:
                        Player.left = false;
                        break;
                } 	  
                break;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) { }

    @Override
    public void paintComponent(Graphics g){
        UI.paintCanvas(g);  
        repaint();
    }
    
    public static void GameStart(){ 
        th = null;
        try{//クリアマーク
            File stage = new File("./public/public_data.txt");
            BufferedReader br = new BufferedReader(new FileReader(stage));
            for(int i = 0;i < maxstage;i++){
                ClearData[i] = Integer.valueOf(br.readLine());
            }
            br.close();
        }catch(IOException e){
            System.err.println("パブリックデータの読み込みに失敗しました");
        }   
    }

    public static void GameMain(int a){
        door = 0;             
	try{
	    File stage = new File("./stage/stage"+a+".txt");
            try (BufferedReader br = new BufferedReader(new FileReader(stage))) {
                for(int i = 0;i < MY;i++){
                    map_str[i] = br.readLine();
                }
            }
	}catch(IOException e){
	    System.err.println("ステージファイルの読み込みに失敗しました");
	}      

	for (int y = 0; y < MY; y++) {
	    for (int x = 0; x < MX; x++) {
		map[y][x] = new MapChip(y,x,map_str[y].charAt(x));
		if ( map[y][x].chip == 'M') {
                    player = new Player(x, y);
		}    
	    }
	}
        player.state = 0;
        th = new Thread(new Panel());
        th.start();
    }

    private void FloorMove(int x, int y){
        if(map[y][x].IsNextMove){
            Move move = map[y][x].move;
            switch(move){
                case RIGHT:
                    if(map[y][x+2].tuuka == Tuuka.CLEAR){
                       //右に進む
                       map[y][x].change(' ');
                       map[y][x].move = Move.NONE;
                       map[y][x+1].change('F');
                       map[y][x+1].move = Move.RIGHT;
                       map[y][x+1].IsNextMove = false;
                       map[y][x+2].change('F');
                       map[y][x+2].move = Move.RIGHT;
                       map[y][x+2].IsNextMove = false;
                    }else{
                        //壁にぶつかって反射
                       map[y][x+1].change(' ');
                       map[y][x+1].move = Move.NONE;
                       map[y][x].move= Move.LEFT;                          
                       map[y][x-1].change('F');
                       map[y][x-1].move = Move.LEFT;
                    }
                    break;
                case LEFT:
                    if(map[y][x-1].tuuka == Tuuka.CLEAR){
                       //左に進む
                       map[y][x-1].change('F');
                       map[y][x-1].move = Move.LEFT;
                       map[y][x+1].change(' ');
                       map[y][x+1].move= Move.NONE;
                    }else{
                       //壁にぶつかって反射
                       map[y][x].change(' ');
                       map[y][x].move = Move.NONE;
                       map[y][x+1].move = Move.RIGHT;
                       map[y][x+1].IsNextMove = false;
                       map[y][x+2].change('F');
                       map[y][x+2].move = Move.RIGHT;
                       map[y][x+2].IsNextMove = false;
                    }
                    break;
                default:
                    break;
            }  
        }
    }
    
    private void RightBelt(int x, int y){
        if(map[y-2][x].chip == 'R' && map[y-2][x].IsNextMove){
           if(map[y-2][x+2].tuuka == Tuuka.CLEAR && map[y-1][x+2].tuuka == Tuuka.CLEAR){
               map[y-2][x].change(' ');
               map[y-2][x].move = Move.NONE;
               map[y-2][x+1].change('R');
               map[y-2][x+1].IsNextMove = false;
               if(map[y][x+1].chip==' '){
                   map[y-2][x+1].move = Move.FLOAT;
               }else{
                  map[y-2][x+1].move = Move.NONE;
               }
           }                        
        }
        if(map[y-2][x].chip == 'R' && !map[y-2][x].IsNextMove) map[y-2][x].IsNextMove = true;
        map[y][x].BeltChange();
    }
    
    private void LeftBelt(int x, int y){
        if(map[y-2][x].chip == 'R' && map[y-2][x].IsNextMove){
           map[y-2][x].change(' ');
           map[y-2][x].move = Move.NONE;
           map[y-2][x-1].change('R');
           if(map[y][x-1].chip==' '){
               map[y-2][x-1].move = Move.FLOAT;
           }else{
              map[y-2][x-1].move = Move.NONE;
           }
        }else{
            
           if(map[y-2][x-1].chip == 'R'){
               map[y-2][x-1].change(' ');
               map[y-2][x-1].move = Move.NONE;
               map[y-2][x-2].change('R');
               if(map[y][x-1].chip==' '){
                   map[y-2][x-2].move = Move.FLOAT;
               }else{
                  map[y-2][x-2].move = Move.NONE;
               }
           }                     
        }
       map[y][x].BeltChange();
    }
    
    private void EnemyMove(int x, int y){
        if(map[y][x].IsNextMove){
            Move move = map[y][x].move;
            switch(move){
                case RIGHT:
                    if(map[y][x+2].tuuka == Tuuka.CLEAR&&map[y+2][x+2].tuuka == Tuuka.WALL){
                       map[y][x].change(' ');
                       map[y][x].move = Move.NONE;
                       map[y][x+1].change('Z');
                       map[y][x+1].move = Move.RIGHT;
                       map[y][x+1].IsNextMove = false;
                    }else{
                       map[y][x].change(' ');
                       map[y][x].move= Move.NONE;                          
                       map[y][x-1].change('z');
                       map[y][x-1].move = Move.LEFT;
                    } 
                    break;
                case LEFT:
                    if(map[y][x-1].tuuka == Tuuka.CLEAR &&map[y+2][x-1].tuuka == Tuuka.WALL){
                       map[y][x].change(' ');
                       map[y][x].move = Move.NONE;
                       map[y][x-1].change('z');
                       map[y][x-1].move = Move.LEFT;
                    }else{
                       map[y][x].change(' ');
                       map[y][x].move= Move.NONE;
                       map[y][x+1].change('Z');
                       map[y][x+1].move = Move.RIGHT;
                       map[y][x+1].IsNextMove = false;
                    }
                    break;
                default:
                    break;
            }
        }
    }
    
    private boolean IsRightBelt(int x, int y){
        //map[y][x]が右向きのベルトコンベアもしくはベルトコンベアの端かどうか
        return map[y][x].ChipCheck('B')|| (map[y][x].ChipCheck('r')&& map[y][x - 1].ChipCheck('B')) || (map[y][x].ChipCheck('l') && map[y][x+1].ChipCheck('B'));
    }
    
    private boolean IsLeftBelt(int x, int y){
        //map[y][x]が右向きのベルトコンベアもしくはベルトコンベアの端かどうか
        return map[y][x].chip == 'b' || (map[y][x].chip == 'r' && map[y][x - 1].chip == 'b') || (map[y][x].chip == 'l' && map[y][x+1].chip == 'b');
    }
    
    public void move(){
        for (int y = MY-1; y>= 0; y--) {
           for (int x = 0; x < MX; x++) {
                if(map[y][x].chip == 'F'){
                    //動く床
                    FloorMove(x, y);
                    map[y][x].IsNextMove = true;
                }
      
                if(IsRightBelt(x, y)){//ベルトコンベアー右向き
                    RightBelt(x, y);
                }

                if(IsLeftBelt(x, y)){//ベルトコンベアー左向き
                    LeftBelt(x, y);
                }
               
                if(map[y][x].chip == 'R' && x != 0 &&y<= MY-3){//レンガブロック
                    Block(x, y);
                }
                
                if(map[y][x].chip == 'Z' || map[y][x].chip == 'z'){//雑魚敵の動き
                    EnemyMove(x, y);
                    map[y][x].IsNextMove =true;
                }              
            }
        } 	
    }
    
    private void Block(int x, int y){
        if(map[y][x].move == Move.FLOAT){
            map[y][x].move = Move.NONE;
            return;
        }
        if(map[y+2][x].tuuka == Tuuka.CLEAR && map[y+2][x+1].tuuka == Tuuka.CLEAR &&map[y+2][x-1].tuuka != Tuuka.BLOCK){
            if(map[y][x].move == Move.NONE){
                if(y+2 == player.y){
                    if(x == player.x||x-1==player.x||x+1==player.x){ //ブロックの真下にプレイヤーがいる
                        map[y][x].change(' ');
                        map[y+1][x].change('R');    
                        if(player.NotGround()){//浮いてる状態
                            player.y += 1;
                        }
                    }else{
                        map[y][x].change(' ');
                        map[y+1][x].change('R');
                    }
                }else{
                    map[y][x].change(' ');
                    map[y+1][x].change('R');
                }
            }else{
               map[y][x].move = Move.NONE;
            }
        }
    }
    
    private void ElevatorMove(int x, int y){
        //エレベーターが動く
        Move move = map[y][x].move;
        switch(move){
            case UP:
                for(int i = 0;i < 3;i++){
                    //上にレンガブロックが乗っている
                    if(map[y-2][x-1+i].chip == 'R') map[y-2][x-1+i].move = Move.UP;
                }  

                if(player.y+2 == y && (x-1 ==player.x||x==player.x || x+1 ==player.x)){
                    //プレイヤーが上に乗っている場合上に移動する
                    player.y -= 1;
                }

                if(map[y-1][x].tuuka == Tuuka.WALL){//上に壁があったら 
                    map[y][x].move = Move.PRE_DOWN;
                }else{
                    map[y-1][x].change('E');
                    map[y-1][x].move = move.PRE_UP;
                    map[y-1][x].IsNextMove = false;
                    map[y-1][x+1].tuuka = Tuuka.WALL;
                    map[y][x].change(' ');
                    map[y][x].move = move.NONE;  
                    map[y][x+1].tuuka = Tuuka.CLEAR;
                } 
                break;
            case DOWN:
                if(map[y+1][x].tuuka == Tuuka.WALL){
                    map[y][x].move = Move.PRE_UP;
                }else{
                    map[y][x].change(' ');
                    map[y+1][x].change('E');
                    map[y][x].move = Move.NONE;
                    map[y+1][x].move = Move.PRE_DOWN;
                    map[y][x+1].tuuka = Tuuka.CLEAR;
                    map[y+1][x+1].tuuka = Tuuka.WALL;
                    if(map[y+1][x-1].chip == 'R') map[y][x-1].change(' ');
                    if(map[y+1][x].chip == 'R') map[y][x].change(' ');
                    if(map[y+1][x+1].chip == 'R') map[y][x+1].change(' ');                           
                }
                break;
            case PRE_UP:
                if(map[y][x].IsNextMove)map[y][x].move = Move.UP; 
                else map[y][x].IsNextMove = true;
                break;
            case PRE_DOWN:
                map[y][x].move = Move.DOWN;
                break;
            default:
                break;
        }
    }
    
    private void BlockOnElevatorMove(int x, int y){
        if(map[y-1][x].tuuka != Tuuka.WALL && map[y-1][x+1].tuuka != Tuuka.WALL){
            //レンガブロックが上に上がる
            map[y-1][x].change('R');
            map[y][x].change(' ');
            map[y][x].move = Move.NONE;
            map[y-1][x].move = Move.NONE;
        }else{
            //レンガブロックが潰れる
            map[y][x].change(' ');
            map[y][x].move = Move.NONE;
        }
    }
    
    private void Elevator(){
        for(int y = MY - 2;y >= 2;y --){
            for(int x = 0;x <= MX - 1;x++){
                if(map[y][x].chip == 'E'){
                    //エレベーターが動く
                    ElevatorMove(x, y);
                }
                if(map[y][x].move == Move.UP && map[y][x].chip == 'R'){
                    //エレベーターに乗っているレンガが動く
                    BlockOnElevatorMove(x, y);
                }                
            }
        }
    }
    
    public void danger(){//死ぬかどうか
        boolean IsDead = false;
        if(map[player.y-1][player.x].chip == 'R'||map[player.y-1][player.x+1].chip == 'R'||map[player.y-1][player.x-1].chip == 'R'){
            IsDead = true;
        }else if(map[player.y+1][player.x].chip == 'Z'||map[player.y+1][player.x+1].chip == 'Z'||map[player.y+1][player.x-1].chip=='Z'||map[player.y][player.x-1].chip == 'Z'||map[player.y][player.x].chip == 'Z'||map[player.y][player.x+1].chip=='Z'){
            IsDead = true;
        }else if(map[player.y+1][player.x].chip == 'z'||map[player.y+1][player.x+1].chip == 'z'||map[player.y+1][player.x-1].chip=='z'||map[player.y][player.x-1].chip == 'z'||map[player.y][player.x].chip == 'z'||map[player.y][player.x+1].chip=='z'){
            IsDead = true;
        }else if(map[player.y+1][player.x].chip == 'F'||map[player.y+1][player.x+1].chip == 'F'||map[player.y][player.x].chip == 'F'||map[player.y][player.x+1].chip=='F'){
            IsDead = true;
        }else if(map[player.y+1][player.x].tuuka == Tuuka.WALL ||map[player.y+1][player.x+1].tuuka == Tuuka.WALL || map[player.y][player.x].tuuka == Tuuka.WALL || map[player.y][player.x+1].tuuka == Tuuka.WALL){
            IsDead = true;
        }else if(map[player.y][player.x].move == Move.TOGE||map[player.y][player.x+1].move == Move.TOGE||map[player.y+1][player.x].move == Move.TOGE||map[player.y+1][player.x+1].move == Move.TOGE){
            IsDead = true;
        }else if(map[player.y][player.x].chip == 'z'|| map[player.y][player.x+1].chip == 'z'||map[player.y+1][player.x].chip == 'z'||map[player.y+1][player.x+1].chip == 'z'){
            IsDead = true;
        }
        
        if(IsDead){
            player.state = 1;
        }    
    }

       

    private void DoorMove(char door_color){
        int tmp_y = 0;
        int tmp_x = 0;
        for(int y = 0;y < MY;y++){
            for(int x = 0;x < MX;x++){
                if(map[y][x].chip == door_color){
                    if(y != player.y || x != player.x){
                        tmp_y = y;
                        tmp_x = x;
                    }
                }
            }
        }
        map[player.y][player.x].move = Move.OPEN;
        try{
            Thread.sleep(1000);
        }catch(InterruptedException e){}

        map[player.y][player.x].change(' ');
        map[tmp_y][tmp_x].change(' ');
        map[player.y][player.x].move = Move.NONE;
        player.y = tmp_y;
        player.x = tmp_x;
        door = 0;    
    }
    
    public void TurnMove(){
        //1ターン進む
        while(th != null){
            repaint();
            if(door == 1){//ドア関係
                char door_color = map[player.y][player.x].chip;
                if(door_color != 'G'){
                    DoorMove(door_color);
                }else{//ゲームクリア                             
                    GameClear();                                 
                    break;
                }
                door = 0;
            }//ドア終了

            if(!Player.right && !Player.left){
                try {
                    Image.boyImg = ImageIO.read(Image.boy);
                    repaint();
                } catch (IOException f) {}
            }
            move();
            Elevator();
            //ジャンプ処理
            player.boyJump();                 

            player.boyMove();
            
            repaint();
            if(player.state == 1){
                break;
            }
            try{
                Thread.sleep(470);
            }catch(InterruptedException e){}   //ゲーム速度

            danger();

            if(player.state == 1){
                endgame = 1;
                if(player.up) endgame = 2;
                try{
                    Thread.sleep(3000);
                }catch(InterruptedException e){}
                gamestate = 2;
                break;
            }
        }
    }

    
    @Override
    public void run(){
        TurnMove();
    }
    
    public void GameClear(){
        map[player.y][player.x].move = Move.OPEN;
        try{
            Thread.sleep(1000);
        }catch(InterruptedException e){}
        
        try{
            FileWriter file = new FileWriter("./public/public_data.txt");
            PrintWriter pw = new PrintWriter(new BufferedWriter(file));

            for(int j=0;j<maxstage-1;j++){//クリアマーク関係
                if(j+1==nowstage||ClearData[j]==1){
                    pw.println(1);
                    ClearData[j]=1;                                                
                }else{
                    pw.println(ClearData[j]);
                }
            }
            if(maxstage == nowstage||ClearData[maxstage-1]==1){
                pw.println(1);
                ClearData[maxstage-1] = 1;                                            
            }else{
                pw.print(ClearData[maxstage-1]);
            }
            pw.close();
        }catch(IOException e){}

        endgame = 1;
        gamestate = 3;
    }
}


