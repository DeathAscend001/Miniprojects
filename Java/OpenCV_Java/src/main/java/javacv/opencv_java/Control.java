/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javacv.opencv_java;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import org.bytedeco.javacv.FrameGrabber;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.bytedeco.javacv.OpenCVFrameConverter;
import org.bytedeco.javacv.VideoInputFrameGrabber;
import org.bytedeco.opencv.opencv_core.IplImage;
import org.bytedeco.opencv.opencv_core.Mat;
import org.bytedeco.opencv.opencv_videoio.VideoCapture;

/**
 *
 * @author trias
 */
public class Control extends Thread
{
    private DaemonThread myThread = null;
    FrameGrabber Grab = new VideoInputFrameGrabber(0);
    Java2DFrameConverter convert = new Java2DFrameConverter();
    IplImage img;
    
    class DaemonThread implements Runnable
    {
        public javax.swing.JLabel Label;
        
        protected volatile boolean runnable = false;
        
        public void run()
        {
            synchronized(this)
            {
                while(runnable)
                {
                    try
                    {
                        // Stuffs Here
                        BufferedImage BI = convert.getBufferedImage(Grab.grab());
                        Label.setIcon(new ImageIcon(new ImageIcon(BI).getImage().getScaledInstance(Label.getWidth(), Label.getHeight(), Image.SCALE_SMOOTH)));
                        // Stuffs To Happen
                        if(!runnable)
                        {
                            this.wait();
                        }
                    }
                    catch(Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
    
    public void Start(javax.swing.JLabel Window)
    {
        myThread = new DaemonThread();
        Thread t = new Thread(myThread);
        t.setDaemon(true);
        myThread.runnable = true;
        myThread.Label = Window;
        // Open //
        try
        {
            Grab.start();
        }
        catch (FrameGrabber.Exception ex) 
        {
            Logger.getLogger(Control.class.getName()).log(Level.SEVERE, null, ex);
        }
        // Continue //
        t.start();
    }
    
    public void Stop()
    {
        myThread.runnable = false;
        try
        {
            Grab.close();
        }
        catch (FrameGrabber.Exception ex)
        {
            Logger.getLogger(Control.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
