/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tasks_update.task;

import io.github.bonigarcia.wdm.WebDriverManager;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.*;
import java.time.LocalDateTime;
import java.util.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;

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
    private String OS_Path;
    // Set Main Font
    private final String MainFont = "Champagne & Limousines";
    // Store All Datas Locally
    /*
        Storage Structure
    - Base Folder
    - - [Copy of Program]
    - - SiteList.dat
    - - AccountList.dat
    - - RoleList.dat
    - - TaskList.dat
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
    
    // Role Data Model
    /*
    - Event-Role
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
                case "Role":
                {
                    // Get RoleList.dat
                    File rl_File = new File(String.join(File.separator, OS_Path, "RoleList.dat"));
                    BufferedReader readRoles = new BufferedReader(new FileReader(rl_File));
                    String line;
                    while((line = readRoles.readLine()) != null)
                    {
                        ArrayList<String> RolesData = new ArrayList<>(Arrays.asList(line.split("-")));
                        Infos.add(RolesData);
                    }
                    readRoles.close();
                    return Infos;
                }
                case "Account":
                {
                    // Get RoleList.dat
                    File rl_File = new File(String.join(File.separator, OS_Path, "AccountList.dat"));
                    BufferedReader readAccounts = new BufferedReader(new FileReader(rl_File));
                    String line;
                    while((line = readAccounts.readLine()) != null)
                    {
                        ArrayList<String> AccountsData = new ArrayList<>(Arrays.asList(line.split("-")));
                        Infos.add(AccountsData);
                    }
                    readAccounts.close();
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
        private final int MaxNameLength = 14;
        private final int MaxTaskCount = 12;
        private final String FileExt = ".task";
        
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
            if(ValidDeadline(TaskInfo.get(1))) { TaskPane.setBackgroundAt(CurrentTaskCount, new Color(0, 255, 0, 100)); }
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
        
        private Boolean ValidDeadline(String CurrentDateTime)
        {
            System.out.println(CurrentDateTime);
            Boolean Flag;
            String[] DateTime = CurrentDateTime.split(" ");
            String Date = DateTime[0];
            String Time = DateTime[1];
            String AMPM = DateTime[2];
            // Get CurrentDateTime
            LocalDateTime CTime = LocalDateTime.now();
            // Create vars
            String[] MDY = Date.split("/");
            String[] HM = Time.split(":");
            int Year = Integer.parseInt(MDY[2]);
            int Month = Integer.parseInt(MDY[0]);
            int Day = Integer.parseInt(MDY[1]);
            int Hour = ("PM".equals(AMPM)) ? Integer.parseInt(HM[0]) + 12 : Integer.parseInt(HM[0]);
            int Minute = Integer.parseInt(HM[1]);
            // Check Date and Time
            if(Year > CTime.getYear()) return true;
            else if(Year == CTime.getYear())
            {
                if(Month > CTime.getMonthValue()) return true;
                else if(Month == CTime.getMonthValue())
                {
                    if(Day > CTime.getDayOfMonth()) return true;
                    else if(Day == CTime.getDayOfMonth())
                    {
                        if(Hour > CTime.getHour()) return true;
                        else if(Hour == CTime.getHour())
                        {
                            if(Minute > CTime.getMinute()) return true;
                        }
                    }
                }
            }
            return false;
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
        
        public Boolean TaskErrorCheck(ArrayList<String> Infos)
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
                if(!ValidDeadline(Infos.get(1))) return false;
                // Check Task Content
                if("".equals(Infos.get(2).trim())) return false;
                // Check If Task Limit
                if(CurrentTaskCount > MaxTaskCount) return false;
                // Forbidden Chars
                if(Infos.get(0).matches("[^A-Za-z-_. ]")) return false;
            }
            catch(Exception ex) { ex.printStackTrace(); }
            return true;
        }
    }
    
    class RolesAndAccountsTab
    {
        private int CurrentRoleCount;
        private int CurrentAccountCount;
        // [Temporary]
        private HashMap<String, String> SiteMap;
        private HashMap<String, ArrayList<String>> SiteElements;

        RolesAndAccountsTab()
        {
            this.CurrentAccountCount = 0;
            this.CurrentRoleCount = 0;
        }
        
        // Roles
        public void RoleRefresh_RDeleteCustom(JComboBox Event_Role)
        {
            // Clear ComboBox
            Event_Role.removeAllItems();
            // Add Items To ComboBox
            Iterator<ArrayList<String>> rl_itr = GetAllData("Role").iterator();
            while(rl_itr.hasNext())
            {
                Event_Role.addItem(String.join("-", rl_itr.next()));
            }
        }
        
        public void RoleDisplay(ArrayList<String> RoleInfo, JTable RolesTable)
        {
            // Table
            DefaultTableModel dataModel = (DefaultTableModel) RolesTable.getModel();
            dataModel.addRow(new Object[] {RoleInfo.get(0), RoleInfo.get(1)});
            RolesTable.setModel(dataModel);
        }
        
        public void ModifyRoles(ArrayList<String> RoleInfo, JTable RolesTable, char Type)
        {
            try
            {
                switch(Type)
                {
                    case '+':
                    {
                        // Add To File
                        File roleFile = new File(String.join(File.separator, OS_Path, "RoleList.dat"));
                        BufferedWriter writeRole = new BufferedWriter(new FileWriter(roleFile, true));
                        writeRole.write(String.join("-", RoleInfo));
                        writeRole.newLine();
                        writeRole.close();
                    }
                    break;
                    case '-':
                        // Remove To File
                        File roleFile = new File(String.join(File.separator, OS_Path, "RoleList.dat"));
                        BufferedReader readRole = new BufferedReader(new FileReader(roleFile));
                        ArrayList<String> roles = new ArrayList<>();
                        String line;
                        while((line = readRole.readLine()) != null)
                        {
                            roles.add(line);
                        }
                        readRole.close();
                        BufferedWriter rewriteRole = new BufferedWriter(new FileWriter(roleFile));
                        Iterator<String> r_itr = roles.iterator();
                        while(r_itr.hasNext())
                        {
                            String Event_Role = r_itr.next();
                            if(!Event_Role.equals(String.join("-", RoleInfo)))
                            {
                                rewriteRole.write(Event_Role);
                                rewriteRole.newLine();
                            }
                        }
                        rewriteRole.close();
                    break;
                }
            }
            catch(Exception ex) { ex.printStackTrace(); }
            LoadRoles(RolesTable);
        }
        
        public void LoadRoles(JTable RolesTable)
        {
            // Clear Table
            RolesTable.setModel(new javax.swing.table.DefaultTableModel(new Object [][] {}, new String [] {"Event", "Role(s)"}));
            // Get Data From RoleList.dat
            Iterator<ArrayList<String>> rl_itr = GetAllData("Role").iterator();
            // Push Datas to Table
            while(rl_itr.hasNext())
            {
                CurrentRoleCount++;
                RoleDisplay(rl_itr.next(), RolesTable);
            }
        }
        
        public Boolean RoleErrorCheck(String Event, String Role)
        {
            try
            {
                // Check For Null Inputs
                if("".equals(Event.trim())) return false;
                if("".equals(Role.trim())) return false;
                // Check For Duplicates
                File rFile = new File(String.join(File.separator, OS_Path, "RoleList.dat"));
                BufferedReader readRoles = new BufferedReader(new FileReader(rFile));
                String line;
                while((line = readRoles.readLine()) != null)
                {
                    if(line.equals(Event+"-"+Role)) return false;
                }
                // Check for forbidden characters
                if(Event.contains("-") || Role.contains("-")) return false;
            }
            catch(Exception ex) { ex.printStackTrace(); }
            return true;
        }
        
        
        // Accounts
        public void AccountRefresh_ALogInDeleteCustom(JComboBox Accounts)
        {
            // Clear ComboBox
            Accounts.removeAllItems();
            // Add Items To ComboBox
            Iterator<ArrayList<String>> rl_itr = GetAllData("Account").iterator();
            while(rl_itr.hasNext())
            {
                Accounts.addItem(String.join("-", rl_itr.next()));
            }
        }
        
        public void AccountDisplay(ArrayList<String> RoleInfo, JTable AccountsTable)
        {
            // Table
            DefaultTableModel dataModel = (DefaultTableModel) AccountsTable.getModel();
            dataModel.addRow(new Object[] {RoleInfo.get(0), RoleInfo.get(1), RoleInfo.get(2)});
            AccountsTable.setModel(dataModel);
        }
        
        public void ModifyAccount(ArrayList<String> AccountInfo, JTable AccountsTable, char Type)
        {
            try
            {
                switch(Type)
                {
                    case '+':
                    {
                        // Add To File
                        File accountFile = new File(String.join(File.separator, OS_Path, "AccountList.dat"));
                        BufferedWriter writeAccount = new BufferedWriter(new FileWriter(accountFile, true));
                        writeAccount.write(String.join("-", AccountInfo));
                        writeAccount.newLine();
                        writeAccount.close();
                    }
                    break;
                    case '-':
                        // Remove To File
                        File accountFile = new File(String.join(File.separator, OS_Path, "AccountList.dat"));
                        BufferedReader readAccount = new BufferedReader(new FileReader(accountFile));
                        ArrayList<String> roles = new ArrayList<>();
                        String line;
                        while((line = readAccount.readLine()) != null)
                        {
                            roles.add(line);
                        }
                        readAccount.close();
                        BufferedWriter rewriteAccount = new BufferedWriter(new FileWriter(accountFile));
                        Iterator<String> a_itr = roles.iterator();
                        while(a_itr.hasNext())
                        {
                            String Account_Info = a_itr.next();
                            if(!Account_Info.equals(String.join("-", AccountInfo)))
                            {
                                rewriteAccount.write(Account_Info);
                                rewriteAccount.newLine();
                            }
                        }
                        rewriteAccount.close();
                    break;
                }
            }
            catch(Exception ex) { ex.printStackTrace(); }
            LoadAccounts(AccountsTable);
        }
        
        public void LoadAccounts(JTable AccountsTable)
        {
            // Clear Table
            AccountsTable.setModel(new javax.swing.table.DefaultTableModel(new Object [][] {}, new String [] {"Site", "Username", "Password"}));
            // Get Data From RoleList.dat
            Iterator<ArrayList<String>> rl_itr = GetAllData("Account").iterator();
            // Push Datas to Table
            while(rl_itr.hasNext())
            {
                CurrentAccountCount++;
                AccountDisplay(rl_itr.next(), AccountsTable);
            }
        }
        
        public boolean AccountErrorCheck(String Site, String Username, String Password)
        {
            try
            {
                // Check For Null Inputs
                if("".equals(Site.trim())) return false;
                if("".equals(Username.trim())) return false;
                if("".equals(Password.trim())) return false;
                // Check For Duplicates
                File aFile = new File(String.join(File.separator, OS_Path, "AccountList.dat"));
                BufferedReader readAccounts = new BufferedReader(new FileReader(aFile));
                String line;
                while((line = readAccounts.readLine()) != null)
                {
                    if(line.equals(String.join("-", Site, Username, Password))) return false;
                }
                // Check for forbidden characters
                if(Site.contains("-") || Username.contains("-") || Password.contains("-")) return false;
            }
            catch(Exception ex) { ex.printStackTrace(); }
            return true;
        }
        
        public void AccountLogIn(String AcctInfo, JFrame AccountFrame)
        {
            try
            {
                String[] Infos = AcctInfo.split("-");
                // Set-up Driver
                WebDriverManager.chromedriver().setup();
                // Call WebDriver {Chrome As Default} {Incognito}
                ChromeOptions options = new ChromeOptions();
                options.addArguments("incognito");
                options.addArguments("start-maximized");
                WebDriver driver = new ChromeDriver(options);
                if("Facebook".equals(Infos[0]))
                {
                    // Launch Site Chosen [Facebook only Available]
                    driver.get("https://www.facebook.com/");
                    driver.findElement(By.id("email")).sendKeys(Infos[1]);
                    driver.findElement(By.id("pass")).sendKeys(Infos[2]);
                    driver.findElement(By.name("login")).click();
                    // Close chromedriver.exe
                    Runtime.getRuntime().exec("taskkill /F /IM chromedriver.exe");
                }
                else
                {
                    JOptionPane.showMessageDialog(AccountFrame, "Facebook support only");
                    driver.quit();
                }
            }
            catch(Exception ex) { ex.printStackTrace(); }
        }
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
                    // Create Extra Files and Folders within Base Folder
                    // // Task Folder
                    File TaskFolder = new File(String.join(File.separator, WindowsPath, TaskFolderName));
                    if(TaskFolder.mkdir()) { System.out.println("TaskFolder Created!"); }
                    else if(TaskFolder.exists()) { System.out.println("TaskFolder Already Exists!"); }
                    else { System.out.println("TaskFolder Creation Failed!"); }
                    // // TaskList.dat
                    File tl_File = new File(String.join(File.separator, WindowsPath, "TaskList.dat"));
                    if(!tl_File.exists()) { tl_File.createNewFile(); }
                    // // RoleList.dat
                    File role_File = new File(String.join(File.separator, WindowsPath, "RoleList.dat"));
                    if(!role_File.exists()) { role_File.createNewFile(); }
                    // // AccountList.dat
                    File accs_File = new File(String.join(File.separator, WindowsPath, "AccountList.dat"));
                    if(!accs_File.exists()) { accs_File.createNewFile(); }
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
