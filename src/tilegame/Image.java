/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tilegame;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 *
 * @author pip
 */
public class Image {
    public static File boy,boy_right,boy_left,YDoorOpen;
    public static BufferedImage boyImg,StageSelect,Star,One,Two,Three,Four,Five,Six,Seven,Eight,Nine,Yajirusi,Retry,Clear,Nextstage,Sura,Suraleft,Yellowdoor,Reddoor,ReddoorOpen,TogeUp,TogeDown,TogeRight,TogeLeft,YellowdoorOpen,RightBelt,LeftBelt;
    public static BufferedImage[] Belt = new BufferedImage [5];
    Image(){
        ObjectImport();
    }

    private static void ObjectImport(){
        File stageselect = new File("./img/stageselect.png");
        File yajirusi = new File("./img/dotsankaku.png");
        File one = new File("./img/1.png");
        File two = new File("./img/2.png");
        File three = new File("./img/3.png");
        File retry = new File("./img/retry.png");
        File clear = new File("./img/clear.png");
        File nextstage = new File("./img/nextstage.png");
        File star = new File("./img/star.png");
        File sura = new File("./img/dogright.png");
        File suraleft = new File("./img/dogleft.png");
        File ydoor = new File("./img/door_yellow.png");
        YDoorOpen = new File("./img/door_yellow_open.png");
        File rdoor = new File("./img/door_red.png");
        File rdoor_open = new File("./img/door_red_open.png");
        boy = new File("./img/pict.png");
        boy_right = new File("./img/pict_right.png");
        boy_left = new File("./img/pict_left.png");
        File tup = new File("./img/toge_up.png");
        File tright = new File("./img/toge_right.png");
        File tdown = new File("./img/toge_down.png");
        File tleft = new File("./img/toge_left.png");  
        File beltRight = new File("./img/belt_right.png");  
        File beltLeft = new File("./img/belt_left.png"); 
        File[] belt = new File[5];
        for(int i = 0;i < 5;i++){
            belt[i] = new File("./img/belt_"+ i +".png");
        }
        try{     
	    StageSelect = ImageIO.read(stageselect);
	    Yajirusi = ImageIO.read(yajirusi);
	    One = ImageIO.read(one);
	    Two = ImageIO.read(two);
	    Three = ImageIO.read(three);
            Retry = ImageIO.read(retry);
            Clear = ImageIO.read(clear);
            Nextstage =ImageIO.read(nextstage);
            Star = ImageIO.read(star);         
	    boyImg = ImageIO.read(boy);        
            Sura = ImageIO.read(sura);
            Suraleft = ImageIO.read(suraleft);
            Yellowdoor = ImageIO.read(ydoor);
            YellowdoorOpen = ImageIO.read(YDoorOpen);
            Reddoor = ImageIO.read(rdoor);
            ReddoorOpen = ImageIO.read(rdoor_open);
            TogeUp = ImageIO.read(tup);
            TogeRight = ImageIO.read(tright);
            TogeDown = ImageIO.read(tdown);
            TogeLeft = ImageIO.read(tleft);
            RightBelt = ImageIO.read(beltRight);
            LeftBelt = ImageIO.read(beltLeft);
            for(int i = 0;i < 5;i++){
                Belt[i] = ImageIO.read(belt[i]);
            }
	}catch(IOException e){
            e.printStackTrace();
        } 
    }
}
