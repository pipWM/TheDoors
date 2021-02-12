
package tilegame;

import static tilegame.Panel.map;
import tilegame.MapChip.Tuuka;

public class Player {
    public static int x, y;
    public static int jumpcount = 0;
    public static String jumpcheck = "";
    public static boolean right = false;
    public static boolean left = false;
    public static boolean up = false;
    public static boolean down = false;
    
    public static int state = 0;
    Player(int x, int y){
        this.x = x;
        this.y = y;
    }
    
    private static int boyMoveRight(){
        int dx =  1;
        if(map[y][x+2].tuuka == Tuuka.WALL || map[y+1][x+2].tuuka == Tuuka.WALL) dx = 0;
        else{
            if(!NotGround()){//地面についてる
                for(int i = 0;i < 3;i++){
                    if(map[y-1+i][x+2].tuuka == Tuuka.BLOCK){
                        if(map[y-1+i][x+4].tuuka == Tuuka.CLEAR &&map[y+i][x+4].tuuka == Tuuka.CLEAR){
                            map[y-1+i][x+2].change(' ');
                            map[y-1+i][x+3].change('R');
                        }else {
                            dx = 0;
                        }
                    }
                }
            }else{
                if(map[y][x+2].tuuka == Tuuka.BLOCK||map[y+1][x+2].tuuka == Tuuka.BLOCK||map[y-1][x+2].tuuka == Tuuka.BLOCK){
                    dx= 0;
                }  
            } 
        }
        return dx;
    }
    
    private static int boyMoveLeft(){
        int dx = 1;
	if(map[y][x-1].tuuka == Tuuka.WALL ||map[y+1][x-1].tuuka == Tuuka.WALL) dx = 0;
        else{
            if(!NotGround()){
                if(map[y][x-2].tuuka == Tuuka.BLOCK){
                    if(map[y][x-3].tuuka == Tuuka.CLEAR &&map[y+1][x-3].tuuka == Tuuka.CLEAR &&map[y][x-4].tuuka != Tuuka.BLOCK &&map[y+1][x-4].tuuka != Tuuka.BLOCK){
                      map[y][x-2].change(' ');
                      map[y][x-3].change('R');
                    }else {
                      dx = 0;
                    }
                }
                if(map[y+1][x-2].tuuka == Tuuka.BLOCK){
                    if(map[y+1][x+4].tuuka == Tuuka.CLEAR && map[y+2][x+4].tuuka == Tuuka.CLEAR &&map[y+1][x-4].tuuka != Tuuka.BLOCK &&map[y+2][x-4].tuuka != Tuuka.BLOCK &&!NotGround()){
                      map[y+1][x-2].change(' ');
                      map[y+1][x-3].change('R');
                    }else {
                      dx = 0;
                    }
                }
                if(map[y-1][x-2].tuuka == Tuuka.BLOCK){
                    if(map[y-1][x+4].tuuka == Tuuka.CLEAR&&map[y-2][x+4].tuuka == Tuuka.CLEAR&&map[y-1][x-4].tuuka != Tuuka.BLOCK &&map[y-2][x-4].tuuka != Tuuka.BLOCK && NotGround() == false){
                      map[y-1][x-2].change(' ');
                      map[y-1][x-3].change('R');
                    }else {
                       dx = 0;
                    }
                } 
            }else{
                  if(map[y][x-2].tuuka == Tuuka.BLOCK ||map[y+1][x-2].tuuka == Tuuka.BLOCK ||map[y-1][x-2].tuuka == Tuuka.BLOCK){
                      dx= 0;
                  }              
            }    
        }
        return dx;
    }
    public static void boyMove() {
        int dx = 0;
        int dy = 0;
           
        if(up&&jumpcheck != "fall"){
            dy -= 1;
            if(map[y-1][x].tuuka == Tuuka.WALL || map[y-1][x+1].tuuka == Tuuka.WALL) dy = 0;
            if(map[y-2][x].tuuka == Tuuka.BLOCK ||map[y-2][x+1].tuuka == Tuuka.BLOCK||map[y-2][x-1].tuuka == Tuuka.BLOCK)dy = 0;
        }
      
        if(down){
            dy +=  1;
            if(map[y+2][x].tuuka == Tuuka.WALL || map[y+2][x+1].tuuka == Tuuka.WALL) dy = 0;
            if(map[y+2][x].tuuka == Tuuka.BLOCK ||map[y+2][x+1].tuuka == Tuuka.BLOCK || map[y+2][x-1].tuuka == Tuuka.BLOCK)dy = 0;
        }    
      
        y += dy;
          
        if(right){
            dx += boyMoveRight();
        } 
        if(left){
            dx -= boyMoveLeft();
        }
           
        x += dx;
      
        int bx =0;
      
        if((map[y+2][x].chip == 'B' || map[y+2][x].chip == 'r')&& !up){
            bx += 1;
            if(right)bx -= 1;
            if(map[y][x+2].tuuka == Tuuka.WALL || map[y+1][x+2].tuuka == Tuuka.WALL) bx = 0;
            if(!NotGround()){
                if(map[y][x+2].tuuka == Tuuka.BLOCK){
                    if(map[y][x+4].tuuka == Tuuka.CLEAR && map[y+1][x+4].tuuka == Tuuka.CLEAR){
                        map[y][x+2].change(' ');
                        map[y][x+3].change('R');
                    }else {
                        bx = 0;
                    }
                }
            }
            if(map[y+1][x+2].tuuka == Tuuka.BLOCK){
                if(map[y+1][x+4].tuuka == Tuuka.CLEAR &&map[y+2][x+4].tuuka == Tuuka.CLEAR && !NotGround()){
                    map[y+1][x+2].change(' ');
                    map[y+1][x+3].change('R');
                }else {
                    bx = 0;
                }
            }            
        }
        x += bx;
    }
    
    public static void boyJump(){
        if(NotGround()){//浮いてるとき
            if(jumpcheck == "ground"){
                jumpcount = 1;
                jumpcheck = "jump";
            }
            if(jumpcheck == "fall"){//落ちてるとき
                down = true;
            }else if(jumpcheck == "jump"){//ジャンプ中
                if(!up){
                    down = true;
                    jumpcheck = "fall";
                }else{
                    jumpcount += 1;
                    if(jumpcount <= 4){
                        up = true;
                    }else{
                        jumpcount = 0;
                        jumpcheck = "fall";
                    }
                }
            }else if(jumpcheck== "ground"){//落下はじめ
                if(!up){
                    jumpcheck = "fall";
                    down = true;
                }
            }
        }else{//地面
            down = false;
            jumpcount = 0;
            jumpcheck = "ground";
        }
    }
    
    public static boolean NotGround(){
        //地面についていないかどうか
        if(map[y+2][x].tuuka != Tuuka.CLEAR ||map[y+2][x+1].tuuka != Tuuka.CLEAR || map[y+2][x-1].tuuka==Tuuka.BLOCK){
	    //地面についている
            return false;
	} 
        //地面についていない
	return true;
    }
}
