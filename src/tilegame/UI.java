/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tilegame;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JPanel;
import static tilegame.Image.*;
import static tilegame.Panel.player; 

public class UI extends JPanel{
    Thread th;
    static Image image = new Image();
    static int MY = Panel.MY;
    static int MX = Panel.MX;
    public static MapChip map[][] = Panel.map;
  
    UI (Thread th){
        this.th = th;
    }
    
    public static void paintCanvas(Graphics g) {
        switch(Panel.gamestate){  
          case(0): 
                //スタート画面
                g.drawImage(StageSelect, 319, 0, null); 
                g.drawImage(Yajirusi,50,300+Panel.nowstage*50,null);
                g.drawImage(One,120,350,null);
                g.drawImage(Two,120,400,null);
                g.drawImage(Three,120,450,null);

                for(int i=0;i<Panel.maxstage;i++){
                    if(Panel.ClearData[i] == 1){
                        g.drawImage(Star,150,350+i*50,null);
                    }
                }
                break;
          case(1):
              //ゲーム本編
              MapPaint(g);
              boyDraw(g, player.x, player.y);  
              break;
          case(2)://リトライ画面
              g.drawImage(Retry,120,350,null);
              g.drawImage(StageSelect,120,409,null);
              g.drawImage(Yajirusi,50,350 + Panel.retry*70,null);
              break;
          case(3):
              //クリア画面 
              g.drawImage(Clear,500,0,null);
              if(Panel.nowstage < Panel.maxstage){
                  g.drawImage(Nextstage,120,350,null);
                  g.drawImage(StageSelect,120,409,null);
                  g.drawImage(Yajirusi,50,350 + Panel.retry*70,null);
              }else{
                  g.drawImage(StageSelect,120,409,null);
                  g.drawImage(Yajirusi,50,420,null);
              }
              break;          
          default:break;
        }
    }
    
    public static void boyDraw(Graphics g, int x, int y) {
	g.drawImage(boyImg, (int)(30*x), (int)(30*y), null);
    }
    
    public static void UIStartPanel(int key){
        switch ( key ) {
            case KeyEvent.VK_UP:
                if(Panel.nowstage > 1) Panel.nowstage -= 1;
                break;
            case KeyEvent.VK_DOWN:
                if(Panel.nowstage < Panel.maxstage) Panel.nowstage += 1; //ステージの数だけ変える
                break;
            case KeyEvent.VK_SPACE:
                if(Panel.endgame == 1){
                    Panel.gamestate = 1;
                    Panel.endgame = 0;
                    Panel.GameMain(Panel.nowstage);//決定
                }
                break;
        } 
    }
    
    public static void UIGamePanel(int key){
        switch ( key ) {
            case KeyEvent.VK_LEFT:
                player.left = true;
                try {
                    boyImg = ImageIO.read(boy_left);
                } catch (IOException f) {}
                break;// left
            case KeyEvent.VK_RIGHT:
                player.right = true;
                try {
                    boyImg = ImageIO.read(boy_right);
                } catch (IOException f) {}
                break;// right
            case KeyEvent.VK_SPACE:
                if(player.jumpcheck == "ground"||player.jumpcheck == "jump"){
                    player.up = true;
                    if(Player.jumpcount == 0)Player.jumpcount += 1;
                    Player.jumpcheck = "jump";
                    if(Player.jumpcount >= 4) Player.jumpcheck = "fall";
                }
                break;// up
            case KeyEvent.VK_DOWN:
                Player.down = true;
                break;// down
            case KeyEvent.VK_UP:
                if(Panel.map[Player.y][Player.x].chip == 'y'||Panel.map[Player.y][Player.x].chip == 'G'){
                    Panel.door = 1;
                }
                break;
            case KeyEvent.VK_ESCAPE:
                Panel.endgame = 1;
                Panel.gamestate = 0;
                Panel.GameStart();
                break;
            case KeyEvent.VK_P:
                //clip[1].stop();
               // bgm(0);
                player.state = 1;
                break;

        }
    }
    
     public static void MapPaint(Graphics g){
        for (int y = 0; y < MY; y++) {
            for (int x = 0; x < MX; x++) {
                map[y][x].ChipPaint(g);
            }
        } 
    }
    
    public static void UIRetryPanel(int key){
        switch ( key ) {
            case KeyEvent.VK_UP:
                if(Panel.retry > 0) Panel.retry -= 1;
                break;
            case KeyEvent.VK_DOWN:
                if(Panel.retry < 1) {
                    Panel.retry += 1;
                } 
                break;
            case KeyEvent.VK_SPACE:
                if(Panel.retry == 0){
                    Panel.gamestate = 1;
                    Panel.endgame = 0;
                    Panel.GameMain(Panel.nowstage);
                }else{
                    Panel.gamestate = 0;
                    Panel.GameStart();
                }
                break;
            }  
    }
    
    public static void UIClearPanel(int key){
        switch ( key ) {
            case KeyEvent.VK_UP:
                if(Panel.retry > 0) Panel.retry -= 1;
                break;
            case KeyEvent.VK_DOWN:
                if(Panel.retry < 1) {
                    Panel.retry += 1;
                } 
                break;
            case KeyEvent.VK_SPACE:
                if(Panel.retry == 0){
                    if(Panel.nowstage < Panel.maxstage){
                        Panel.gamestate = 1;
                        Panel.endgame = 0;
                        Panel.nowstage+=1;
                        Panel.GameMain(Panel.nowstage);
                    }else{
                        Panel.gamestate = 0;
                        Panel.GameStart();
                    } 
                }else{
                    Panel.gamestate = 0;
                    Panel.GameStart();
                }
                break;
        } 
    }   
}
