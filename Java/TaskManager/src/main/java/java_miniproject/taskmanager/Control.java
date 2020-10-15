/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package java_miniproject.taskmanager;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.*;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
/**
 *
 * @author trias
 */
public class Control
{
    private int TaskCount = 0;
    private static final String Username = "root";
    private static final String Password = "Gv4Kr3fSBv61Pqiru3W0r4";
    private static final String Link = "jdbc:mysql://localhost/tasks_and_projects?useTimezone=true&serverTimezone=UTC";
    private static final String Connector = "com.mysql.cj.jdbc.Driver";
    
    
    private void AddInfoToSQL(ArrayList<String> Info)
    {
        try
        {
            Class.forName(Connector);
            
            Connection sql_con = null;
            sql_con = DriverManager.getConnection(Link, Username, Password);
            
            Statement Query = null;
            Query = sql_con.createStatement();
            Query.executeUpdate(String.format("INSERT INTO Tasks VALUES('%s', '%s', '%s')", Info.get(0), Info.get(2), Info.get(1)));
            sql_con.close();
        }
        catch (SQLException | ClassNotFoundException ex)
        {
            Logger.getLogger(Control.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void DeleteInfoFromSQL(String TaskName)
    {
        try
        {
            Connection sql_con = DriverManager.getConnection(Link, Username, Password);
            Statement Query = sql_con.createStatement();
            Query.executeUpdate(String.format("DELETE FROM Tasks WHERE Name='%s'", TaskName));
            sql_con.close();
        }
        catch (SQLException ex)
        {
            Logger.getLogger(Control.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private ArrayList<ArrayList<String>> GetInfoFromSQL()
    {
        ArrayList<ArrayList<String>> ReturnInfo = new ArrayList<>(); 
        try
        {
            Class.forName(Connector);
            
            Connection sql_con = null;
            sql_con = DriverManager.getConnection(Link, Username, Password);
            
            Statement QUERY = null;
            QUERY = sql_con.createStatement();
            
            ResultSet Res = QUERY.executeQuery("SELECT * FROM Tasks");
            while(Res.next())
            {
                ArrayList<String> CurrentInfo = new ArrayList<>();
                // Add Name
                CurrentInfo.add(Res.getString("Name"));
                // Add DateTime
                String TimeStamp = Res.getTimestamp("Deadline").toInstant().atOffset(ZoneOffset.UTC).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
                String Hour = TimeStamp.substring(11, TimeStamp.length() - 3);
                if(Integer.parseInt(Hour) > 12) { TimeStamp += " PM"; Hour = "0" + Integer.toString(Integer.parseInt(Hour) - 12); }
                else { TimeStamp += " AM"; }
                CurrentInfo.add(TimeStamp.substring(0, 11) + Hour + TimeStamp.substring(13, TimeStamp.length()));
                // Add Desc
                CurrentInfo.add(Res.getString("Description"));
                // Add to Main Vector
                ReturnInfo.add(CurrentInfo);
            }
            sql_con.close();
            return ReturnInfo;
        }
        catch (SQLException | ClassNotFoundException ex)
        {
            Logger.getLogger(Control.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    private Boolean CheckDeadlines(String Date)
    {
        return true;
    }
    
    private void TasksGUIMaker(ArrayList<String> Info, JTabbedPane TaskPane)
    {
        /*
        [0] ==> Name
        [1] ==> Description
        [2] ==> Deadline
        */
        
        // -- Initialization -- //
        
        // Add Tab
        JPanel IPanel = new JPanel();
        IPanel.setBackground(new Color(20, 20, 20));
        IPanel.setOpaque(true);
        TaskPane.addTab(Info.get(0), IPanel);
        
        GroupLayout GL = new GroupLayout(IPanel);
        IPanel.setLayout(GL);
        
        // Add Title
        JLabel Title = new JLabel();
        IPanel.add(Title);
        Title.setText(Info.get(0));
        Title.setFont(new java.awt.Font("Comic Sans MS", 0, 16));
        Title.setForeground(new Color(255,133,0));
        Title.setHorizontalAlignment(SwingConstants.CENTER);
        
        // Add Date and Time
        JLabel Date = new JLabel();
        IPanel.add(Date);
        Date.setText(Info.get(1));
        Date.setFont(new java.awt.Font("Comic Sans MS", 0, 13));
        Date.setForeground(new Color(255,133,0));
        Date.setHorizontalAlignment(SwingConstants.LEFT);
        
        // Add Description
        JLabel Desc = new JLabel();
        IPanel.add(Desc);
        Desc.setText(Info.get(2));
        Desc.setFont(new java.awt.Font("Comic Sans MS", 0, 13));
        Desc.setForeground(new Color(255,133,0));
        Desc.setHorizontalAlignment(SwingConstants.LEFT);
        
        // {S} Remove Task Button
        JButton RemoveTask = new JButton();
        IPanel.add(RemoveTask);
        RemoveTask.setText("Remove This Task");
        RemoveTask.setFont(new java.awt.Font("Comic Sans MS", 0, 13));
        RemoveTask.setForeground(new Color(255,133,0));
        RemoveTask.setBackground(new Color(20, 20, 20));
        
        // Set Tab Color
        TaskPane.setBackgroundAt(TaskCount, Color.ORANGE);
        
        // -- LAYOUT -- //
        GL.setHorizontalGroup(GL.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addComponent(Title, GroupLayout.DEFAULT_SIZE, 716, Short.MAX_VALUE)
                .addGroup(GroupLayout.Alignment.TRAILING, GL.createSequentialGroup()
                    .addGap(0, 24, Short.MAX_VALUE)
                    .addComponent(Date, GroupLayout.PREFERRED_SIZE, 692, GroupLayout.PREFERRED_SIZE))
                .addGroup(GL.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(Desc)
                    .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGroup(GroupLayout.Alignment.TRAILING, GL.createSequentialGroup()
                    .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(RemoveTask)
                    .addGap(318, 318, 318)
        ));
        GL.setVerticalGroup(GL.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(GL.createSequentialGroup()
                    .addGap(5, 5, 5)
                    .addComponent(Title, GroupLayout.PREFERRED_SIZE, 20, GroupLayout.PREFERRED_SIZE)
                    .addGap(10, 10, 10)
                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(Date)
                    .addGap(20, 20, 20)
                    .addComponent(Desc)
                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 241, Short.MAX_VALUE)
                    .addComponent(RemoveTask)
                    .addContainerGap()
        ));
        
        // -- EVENTS -- //
        RemoveTask.addActionListener(new java.awt.event.ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                TaskPane.remove(IPanel);
                DeleteInfoFromSQL(Info.get(0));
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        });
    }
    
    // PUBLIC //
    
    public void Clear(JTextField TaskName, JTextArea TaskDesc, JComboBox[] Date, JSpinner[] Time, Object[] SpinnerVal)
    {
        TaskName.setText("");
        TaskDesc.setText("");
        for(JComboBox CB : Date)
        {
            CB.setSelectedIndex(0);
        }
        for(int i = 0; i < Time.length; i++)
        {
            Time[i].setValue(SpinnerVal[i]);
        }
    }
    
    public void LoadExistingTasks(JTabbedPane TaskPane)
    {
        Iterator<ArrayList<String>> itr = GetInfoFromSQL().iterator();
        while(itr.hasNext())
        {
            ArrayList<String> tmp = itr.next();
            TaskCount++;
            TasksGUIMaker(tmp, TaskPane);
        }
    }
    
    public void AddTask(JTabbedPane TaskPane, ArrayList<String> Info)
    {
        // Add To SQL
        AddInfoToSQL(Info);
        // Add To Current Session
        TasksGUIMaker(Info, TaskPane);
    }
}
