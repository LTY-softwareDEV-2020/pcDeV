import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;

public class tcpMouseControler extends Thread{
    Robot robot;
    public tcpMouseControler(){
        try{
            robot = new Robot();
        }
        catch (AWTException  e){
            e.printStackTrace();
        }
    }
    @Override
    public void run(){
        Boolean debugFlag = false;
        if(debugFlag){
            return;
        }
        ServerSocket server;
        try {
            DatagramSocket socket = new DatagramSocket(2345);
            DatagramPacket packet = new DatagramPacket(new byte[1024], 1024);
            while(true){
                Point p  = MouseInfo.getPointerInfo().getLocation();
                int x = (int)p.getX();
                int y = (int)p.getY();
                socket.receive(packet);
                byte[] arr = packet.getData();
                int len = packet.getLength();
                String control = new String(arr,0,len);
                if(control.equals("L")){
                    robot.mousePress(InputEvent.BUTTON1_MASK);
                    robot.delay(10);
                    robot.mouseRelease(InputEvent.BUTTON1_MASK);
                }
                else if(control.equals("R")){
                    robot.mousePress(InputEvent.BUTTON3_MASK);
                    robot.delay(10);
                    robot.mouseRelease(InputEvent.BUTTON3_MASK);
                }
                else{
                    String []pos = control.split("\t");
                    System.out.println(control);
                    Integer relativex = Integer.parseInt(pos[0]);
                    Integer relativey = Integer.parseInt(pos[1]);
                    robot.mouseMove(x+relativex,y+relativey);
                }
            }
//            socket.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
