/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tasks_update.task;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import org.apache.commons.io.FileUtils;

/**
 *
 * @author trias
 */
public class Control
{
    private final String ProjectName = "Task_Data";
    // Path For Windows, Mac, Linux
    private final String OS_Check = System.getProperty("os.name");
    private final String WindowsPath = "C:\\ProgramData\\" + ProjectName;
    private final String MacOSXPath = "/Applications/" + ProjectName;
    private final String LinuxPath = "/opt/" + ProjectName;
    private final String TaskFolderName = "Tasks";
    // OS SET
    private String OS_Path = "";
    // Set Main Font
    private String MainFont = "Champagne & Limousines";
    // Store All Datas Locally
    /*
        Storage Structure
    - Base Folder
    - - [Copy of Program]
    - - AccountList.dat
    - - RoleList.dat
    - - TaskList.dat
    - - About.dat
    - - Tasks
    - - - SampleTask.task
    */
    
    // Task Data Model
    /*
    - Name
    - Date
    - Priority
    - Content
    */
    
    public ArrayList<ArrayList<String>> GetAllData(String TabName)
    {
        try
        {
            ArrayList<ArrayList<String>> Infos = new ArrayList<>();
            switch(TabName)
            {
                case "Task":
                {
                    // Get TaskList.dat
                    File tl_File = new File(String.join(File.separator, OS_Path, "TaskList.dat"));
                    BufferedReader readTaskList = new BufferedReader(new FileReader(tl_File));
                    String TaskName;
                    // Get Each Task
                    while((TaskName = readTaskList.readLine()) != null)
                    {
                        ArrayList<String> TaskData = new ArrayList<>();
                        File TaskFile = new File(String.join(File.separator, OS_Path, TaskFolderName, TaskName + ".task"));
                        BufferedReader td_read = new BufferedReader(new FileReader(TaskFile));
                        TaskData.add(td_read.readLine()); // Name
                        TaskData.add(td_read.readLine()); // Date
                        TaskData.add(td_read.readLine()); // Priority
                        TaskData.add(td_read.readLine()); // Content
                        td_read.close();
                        Infos.add(TaskData);
                    }
                    readTaskList.close();
                    return Infos;
                }
                case "R&A":
                {
                    return Infos;
                }
                case "About":
                {
                    return Infos;
                }
            }
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return null;
    }
    
    class TasksTab
    {
        private int CurrentTaskCount = 0;
        private int MaxNameLength = 14;
        private int MaxTaskCount = 12;
        private String FileExt = ".task";
        
        private void TaskDisplay(ArrayList<String> TaskInfo, JTabbedPane TaskPane)
        {
            // Make a Panel
            JPanel Panel = new JPanel();
            Panel.setBackground(Color.white);
            Panel.setOpaque(true);
            
            // Add Panel to TabPane
            TaskPane.addTab(TaskInfo.get(0), Panel);
            
            // Create GroupLayout
            GroupLayout GL = new GroupLayout(Panel);
            Panel.setLayout(GL);

            // Add Title
            JLabel Title = new JLabel();
            Title.setText(TaskInfo.get(0));
            Title.setFont(new java.awt.Font(MainFont, 1, 18));
            Title.setHorizontalAlignment(JLabel.CENTER);
            Panel.add(Title);

            // Add DateTime
            // // Label
            JLabel DateTimeLabel = new JLabel();
            DateTimeLabel.setText("Deadline:");
            DateTimeLabel.setFont(new java.awt.Font(MainFont, 1, 14));
            Panel.add(DateTimeLabel);
            // // Content
            JTextField DateTimeContent = new JTextField();
            DateTimeContent.setText(TaskInfo.get(1));
            DateTimeContent.setFont(new java.awt.Font(MainFont, 1, 14));
            Panel.add(DateTimeContent);

            // Add Priority
            // // Label
            JLabel PriorityLabel = new JLabel();
            PriorityLabel.setText("Priority:");
            PriorityLabel.setFont(new java.awt.Font(MainFont, 1, 14));
            Panel.add(PriorityLabel);
            // // Content
            JLabel PriorityContent = new JLabel();
            PriorityContent.setText(TaskInfo.get(2));
            PriorityContent.setFont(new java.awt.Font(MainFont, 1, 14));
            Panel.add(PriorityContent);
            
            // Add Description
            JTextArea Desc = new JTextArea();
            JScrollPane DescScrollPane = new JScrollPane(Desc, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
            Desc.setText(TaskInfo.get(3));
            Desc.setFont(new java.awt.Font(MainFont, 1, 14));
            Desc.setWrapStyleWord(true);
            Desc.setEditable(false);
            Panel.add(DescScrollPane);
            
            // Edit Button
            JButton EditButton = new JButton();
            EditButton.setText("Edit Task");
            EditButton.setFont(new java.awt.Font(MainFont, 1, 14));
            EditButton.setForeground(new java.awt.Color(207, 235, 52));
            Panel.add(EditButton);
            // // Function
            EditButton.addActionListener(new java.awt.event.ActionListener()
            {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    // Not Yet Supported
                    javax.swing.JOptionPane.showMessageDialog(TaskPane, "Not Yet Available");
                }
            });
            
            // Remove Button
            JButton RemoveButton = new JButton();
            RemoveButton.setText("Remove Task");
            RemoveButton.setFont(new java.awt.Font(MainFont, 1, 14));
            RemoveButton.setForeground(new java.awt.Color(217, 30, 74));
            Panel.add(RemoveButton);
            // // Function
            RemoveButton.addActionListener(new java.awt.event.ActionListener()
            {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    // Remove Task From Storage
                    ModifyTask(new ArrayList<>(java.util.List.of(TaskInfo.get(0))), TaskPane, '-');
                    // Reload
                    LoadTasks(TaskPane);
                }
            });

            // Determine Tab Color By Task Status
            if(UpdateTasks(TaskInfo.get(1))) { TaskPane.setBackgroundAt(CurrentTaskCount, new Color(0, 255, 0, 100)); }
            else { TaskPane.setBackgroundAt(CurrentTaskCount, new Color(255, 0, 0, 100)); }
            
            // Layout
            // Horizontal
                GL.setHorizontalGroup(
                        GL.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(GL.createSequentialGroup()
                            .addGap(38, 38, 38)
                            .addGroup(GL.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(DescScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 480, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(GL.createSequentialGroup()
                                    .addGap(88, 88, 88)
                                    .addComponent(EditButton)
                                    .addGap(106, 106, 106)
                                    .addComponent(RemoveButton))
                                .addGroup(GL.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, GL.createSequentialGroup()
                                        .addComponent(DateTimeLabel)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(DateTimeContent, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(PriorityLabel)
                                        .addGap(18, 18, 18)
                                        .addComponent(PriorityContent, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, GL.createSequentialGroup()
                                        .addGap(47, 47, 47)
                                        .addComponent(Title, javax.swing.GroupLayout.PREFERRED_SIZE, 424, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addContainerGap(319, Short.MAX_VALUE))
                    );
            // Vertical
                    GL.setVerticalGroup(
                        GL.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(GL.createSequentialGroup()
                            .addGap(33, 33, 33)
                            .addComponent(Title, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(18, 18, 18)
                            .addGroup(GL.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(PriorityLabel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(PriorityContent, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(GL.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(DateTimeContent, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(DateTimeLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGap(26, 26, 26)
                            .addComponent(DescScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addGroup(GL.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(EditButton)
                                .addComponent(RemoveButton))
                            .addGap(244, 244, 244))
                    );
        }
        
        private Boolean UpdateTasks(String Date)
        {
            return true;
        }
        
        public void ModifyTask(ArrayList<String> TaskInfo, JTabbedPane TaskPane, char Type)
        {
            try
            {
                switch(Type)
                {
                    case '+':
                    {
                        // TaskList.dat
                        File tl_File = new File(String.join(File.separator, OS_Path, "TaskList.dat"));
                        if(!tl_File.exists()) { tl_File.createNewFile(); }
                        BufferedWriter write_tl = new BufferedWriter(new FileWriter(tl_File, true));
                        write_tl.write(TaskInfo.get(0));
                        write_tl.newLine();
                        write_tl.close();
                        // Access Task Folder
                        String T_Folder = String.join(File.separator, OS_Path, TaskFolderName);
                        File TaskFolder = new File(T_Folder);
                        if(TaskFolder.mkdir()) { System.out.println("TaskFolder Created!"); }
                        else if(TaskFolder.exists()) { System.out.println("TaskFolder Already Exists!"); }
                        else { System.out.println("TaskFolder Creation Failed!"); }
                        // Create Task File
                        File NewTaskFile = new File(String.join(File.separator, T_Folder, TaskInfo.get(0) + FileExt));
                        NewTaskFile.createNewFile();
                        // Write to Task File
                        BufferedWriter writeFile = new BufferedWriter(new FileWriter(NewTaskFile));
                        Iterator<String> itr = TaskInfo.iterator();
                        while(itr.hasNext())
                        {
                            writeFile.write(itr.next());
                            writeFile.newLine();
                        }
                        writeFile.close();
                    }
                    break;
                    case '-':
                    {
                        // Access Task Folder
                        String T_Folder = String.join(File.separator, OS_Path, TaskFolderName);
                        File TaskFolder = new File(T_Folder);
                        if(TaskFolder.mkdir()) { System.out.println("TaskFolder Created!"); }
                        else if(TaskFolder.exists()) { System.out.println("TaskFolder Already Exists!"); }
                        else { System.out.println("TaskFolder Creation Failed!"); }
                        // Delete File
                        File DeleteFile = new File(String.join(File.separator, T_Folder, TaskInfo.get(0) + FileExt));
                        if(DeleteFile.exists()) { DeleteFile.delete(); }
                        // Remove File From List
                        // // Get All Tasks from List
                        File tl_File = new File(String.join(File.separator, OS_Path, "TaskList.dat"));
                        BufferedReader readTaskList = new BufferedReader(new FileReader(tl_File));
                        ArrayList<String> gTaskList = new ArrayList<>();
                        String line;
                        while((line = readTaskList.readLine()) != null) { gTaskList.add(line); }
                        readTaskList.close();
                        // // Filter
                        BufferedWriter rewriteTaskList = new BufferedWriter(new FileWriter(tl_File));
                        Iterator<String> itr = gTaskList.iterator();
                        while(itr.hasNext())
                        {
                            String TaskName = itr.next();
                            if(!TaskName.equals(TaskInfo.get(0)))
                            {
                                rewriteTaskList.write(TaskName);
                            }
                        }
                        rewriteTaskList.close();
                    }
                    break;
                }
            }
            catch(Exception ex) { ex.printStackTrace(); }
            // Call Load Task Each Modification
            LoadTasks(TaskPane);
        }
        
        public void LoadTasks(JTabbedPane TaskPane)
        {
            // Reset Current Tabs
            while(TaskPane.getTabCount() > 1)
            {
                TaskPane.removeTabAt(1);
            }
            // Reset Tab Counter
            CurrentTaskCount = 0;
            // Get Task Infos
            Iterator<ArrayList<String>> itr_TaskInfos = GetAllData("Task").iterator();
            // Set Tabs
            while(itr_TaskInfos.hasNext())
            {
                CurrentTaskCount++;
                TaskDisplay(itr_TaskInfos.next(), TaskPane);
            }
        }
        
        public Boolean ErrorCheck(ArrayList<String> Infos)
        {
            try
            {
                // Check Task Name
                if("".equals(Infos.get(0).trim())) return false;
                if(Infos.get(0).length() > MaxTaskCount) return false;
                // Check Duplicate Name //
                File gTaskList = new File(String.join(File.separator, OS_Path, "TaskList.dat"));
                BufferedReader rTaskList = new BufferedReader(new FileReader(gTaskList));
                String TaskName;
                while((TaskName = rTaskList.readLine()) != null)
                {
                    if(TaskName.equals(Infos.get(0))) return false;
                }
                // //
                // Check Task Date
                if(!UpdateTasks(Infos.get(1))) return false;
                // Check Task Content
                if("".equals(Infos.get(2).trim())) return false;
            }
            catch(Exception ex) { ex.printStackTrace(); }
            return true;
        }
    }
    
    class RolesAndAccountsTab
    {
    }
    
    class Files
    {
        private void DeleteDir(File aFile)
        {
            for(File FileObj : aFile.listFiles())
            {
                if(FileObj.isDirectory()) DeleteDir(FileObj);
                else { FileObj.delete(); }
            }
        }
        public void Init_File()
        {
            try
            {
                if(OS_Check.toLowerCase().contains("win")) // Windows
                {
                    // Set OS Path
                    OS_Path = WindowsPath;
                    // Create Task Data
                    File BaseFolder = new File(WindowsPath);
                    if(BaseFolder.mkdir()) { System.out.println("Base Folder Created!"); }
                    else if(BaseFolder.exists()) { System.out.println("Base Folder Already Exists!"); }
                    else { System.out.println("Base Folder Creation Failed!"); }
                    // Clone App
                    File SrcFile = new File(getClass().getProtectionDomain().getCodeSource().getLocation().toURI().getPath());
                    File DestFile = new File(String.join(File.separator, WindowsPath, "Taskv3.jar"));
                    FileUtils.copyFile(SrcFile, DestFile);
                    // Create Extra Folders within Base Folder
                    // // Task Folder
                    File TaskFolder = new File(String.join(File.separator, WindowsPath, TaskFolderName));
                    if(TaskFolder.mkdir()) { System.out.println("TaskFolder Created!"); }
                    else if(TaskFolder.exists()) { System.out.println("TaskFolder Already Exists!"); }
                    else { System.out.println("TaskFolder Creation Failed!"); }
                }
                else if(OS_Check.toLowerCase().contains("mac")) // Mac
                {
                    System.out.println("Your Operating System is Not Yet Supported!");
                }
                else if(OS_Check.toLowerCase().contains("nix") || OS_Check.toLowerCase().contains("nux") || OS_Check.toLowerCase().contains("aix")) // Linux
                {
                    System.out.println("Your Operating System is Not Yet Supported!");
                }
                else
                {
                    System.out.println("Your Operating System is Not Yet Supported!");
                }
            }
            catch(Exception ex)
            {
                ex.printStackTrace();
            }
        }
        public void Uninstall()
        {
            File BaseFolder = new File(OS_Path);
            // Delete All Inside BaseFolder
            DeleteDir(BaseFolder);
            // Delete Directory
            BaseFolder.delete();
        }
    }
}
