import java.awt.event.KeyEvent;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.awt.AWTException;
import java.awt.Robot;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class tcpkeyBoardControler extends Thread{
    Robot robot;
    Map keyMap;
    public tcpkeyBoardControler(){
        keyMap = new HashMap<>() {
            {
                put("01esc", new int[]{KeyEvent.VK_ESCAPE});
                put("01up", new int[]{KeyEvent.VK_UP});
                put("01alttab", new int[]{KeyEvent.VK_ALT,KeyEvent.VK_TAB});
                put("01left",new int[]{KeyEvent.VK_LEFT});
                put("01enter",new int[]{KeyEvent.VK_ENTER});
                put("01right",new int[]{KeyEvent.VK_RIGHT});
                put("01win",new int[]{KeyEvent.VK_WINDOWS});
                put("01down",new int[]{KeyEvent.VK_DOWN});
                put("01screencut",new int[]{KeyEvent.VK_PRINTSCREEN});
                put("01shutdownpresentscreen", new int[]{KeyEvent.VK_ALT, KeyEvent.VK_F4});
            }
        };

        try{
            robot = new Robot();
        }
        catch (AWTException  e){
            e.printStackTrace();
        }
    }
    @Override
    public void run(){
        ServerSocket server = null;
        try {
            server = new ServerSocket(1346);
            while (true){
                Socket socket = server.accept();//等待连接，一直阻塞
                System.out.println("keyboardConnected");
                DataInputStream dis = new DataInputStream(socket.getInputStream());
                String control = dis.readUTF();
                System.out.println(control);
                int [] ks;
                if( keyMap.containsKey(control)){
                    ks = (int[])keyMap.get(control);
                    for(int k : ks){
                        robot.keyPress(k);
                        robot.delay(10);
                    }
                    robot.delay(100);
                    for (int i = ks.length-1 ; i != -1 ; i-- ){
                        robot.delay(10);
                        robot.keyRelease(ks[i]);
                    }
                }
                else {
                    if(control.equals("01shutdownpc")){
                        System.out.println("关机");
                        Runtime run=Runtime.getRuntime();
                        run.exec("Shutdown.exe -s -t 6000");
                    }
                    else{
                        continue;
                    }
                }
                dis.close();
                socket.close();
            }
        }
        catch (IOException e) {
                e.printStackTrace();
            }
    }
}
