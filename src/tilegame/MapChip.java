
package tilegame;

import java.awt.Color;
import java.awt.Graphics;
import static tilegame.Image.*;


class MapChip{ 
    public int x,y;
    Move move;
    Tuuka tuuka;
    public char chip;
    boolean IsNextMove; 
    static Player player = Panel.player;
    
    //ベルトコンベアの表示用
    private int BeltCount;
    
    MapChip(int y,int x,char a){
	this.x = x;
	this.y = y;
	chip = a;
        move = Move.NONE;
	change(chip);
        IsNextMove = true;
        BeltCount = 0;
    }
    
    protected enum Move{
        NONE,
        RIGHT,
        LEFT,
        UP,
        DOWN,
        PRE_UP,     //次のターンで上に移動する
        PRE_DOWN,   //次のターンで下に移動する
        CLOSE,      //閉じたドア
        OPEN,       //開いたドア
        FLOAT,      //ブロックが宙に浮いている
        TOGE        //トゲ
    };
    
    protected enum Tuuka{  //通れるかどうか
        CLEAR,     //通れる
        BLOCK,     //条件付きで通れる
        WALL,      //通れない
    };

    
    public boolean ChipCheck(char chip){
        return this.chip == chip;
    }
    
    public void BeltChange(){
        //ベルトコンベアの絵の表示を変える
        BeltCount++;
    }
    
    public void ChipPaint(Graphics g){
	int xx = 30*x, yy = 30*y;
        switch (chip) {
          case 'W':
              g.setColor(Color.white);
              g.fillRect(xx,yy,30,30); 
              break;
          case '1': 
              g.setColor(Color.white);
              g.fillRect(xx,yy+5,30,21);
               break;
          case '2': 
              g.setColor(Color.white);
	      g.fillRect(xx,yy+5,9,21);
              g.fillRect(xx+18,yy+5,12,21);
              break;
          case '3': 
              g.setColor(Color.white);;
              g.fillRect(xx,yy+5,12,21);
              g.fillRect(xx+21,yy+5,9,21);
              break;
          case '4':
              g.setColor(Color.white);
              g.fillRect(xx,yy+5,30,21);
              break;
	  case 'R':
              g.setColor(new Color(150, 0 ,0));
              g.fillRect(xx+3,yy+3, 54, 54);
            break;
	  case 'F':
              g.setColor(Color.blue);
              g.fillRect(xx,yy+5,30,21); 
              break;
          case 'E':
              g.setColor(Color.blue);
              g.fillRect(xx, yy+5, 60, 21);
              break;
	  case 'B':
              g.drawImage(Belt[4 - BeltCount % 5], xx, yy,xx+30,yy+30,30,0,0,30,null);
	      break;
          case 'b':
              g.drawImage(Belt[BeltCount % 5], xx, yy,xx+30,yy+30,30,0,0,30,null);
	      break;
          case 'Z':
            g.drawImage(Sura, xx, yy,null);
            break;
          case 'z':
            g.drawImage(Suraleft, xx, yy,null);
            break; 
          case 'y':
            if(move == Move.CLOSE)g.drawImage(Yellowdoor, xx, yy,null);
            else g.drawImage(YellowdoorOpen, xx, yy,null);
                
            break;  
          case 'G':
            if(move == Move.CLOSE)g.drawImage(Reddoor, xx, yy,null);
            else g.drawImage(ReddoorOpen, xx, yy,null);          
            break;            
          case 'i':
            g.drawImage(TogeUp, xx, yy,null);
            break; 
          case 's':
            g.drawImage(TogeRight, xx, yy,null);
            break; 
          case 'v':
            g.drawImage(TogeDown, xx, yy,null);
            break;             
          case 'd':
            g.drawImage(TogeLeft, xx, yy,null);
            break;   
          case 'l':
            g.drawImage(LeftBelt, xx, yy,null);
            break;   
          case 'r':
            g.drawImage(RightBelt, xx, yy,null);
            break;   
            
        }
    }


    public void change(char chip){
        this.chip = chip;
        IsNextMove = true;
	switch(chip){
	case '1':
	case '2':
	case '3':
	case '4':
	case 'w':
	case 'F': 
            move = Move.RIGHT;
	    tuuka = Tuuka.WALL;
            break;
        case 'E': 
            move = Move.PRE_UP;
            tuuka = Tuuka.WALL;
            break;
	case 'B':
	    tuuka = Tuuka.WALL;
            break;
	case 'b':
	    tuuka = Tuuka.WALL;
            break;            
	case 'R':
            tuuka = Tuuka.BLOCK;
            break;
        case 'Z':
            move = Move.RIGHT;
            tuuka = Tuuka.CLEAR;
            break;
        case 'z':
            move = Move.LEFT;
            tuuka = Tuuka.CLEAR;
            break;
        case 'l':
            tuuka = Tuuka.WALL;
            break;    
         case 'r':
            tuuka = Tuuka.WALL;
            break;
         case 'y':
            move = Move.CLOSE;
            tuuka = Tuuka.CLEAR;
            break;
         case 'G':
            move = Move.CLOSE;
            tuuka = Tuuka.CLEAR;
            break;
        case 'i':
        case 's':
        case 'v':
        case 'd':
            move = Move.TOGE;
            tuuka = Tuuka.CLEAR;
	default:
            tuuka = Tuuka.CLEAR;
            break;
	}	
    }   
}

