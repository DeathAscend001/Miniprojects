#include <boost/filesystem.hpp>
#include <direct.h>
#include <iostream>
#include <fstream>
#include <vector>
#include <chrono>
#include <string>
#include <ctime>
#include <windows.h>
using namespace std;

// Select File Extension
// Copy All that has that file extension
// Paste On A Folder in Desktop

class FileTaker
{
private:
    int CopyFileCount = 0;
    /*
        Change Destination Path or Modify this Code
    */
    const string DestPath = "C:\\Users\\trias\\Desktop\\Taken Files\\";
    chrono::high_resolution_clock::time_point Start, End;
    chrono::duration<float> ElapsedTime;
private:
    vector<string> GetMainFile(vector<string> AbsPath);
    string GetDateOfExec();
    vector<string> GetAllMatchedFiles(vector<string> FileExt, string PathToScan);
    void CopyFile(string SrcFile, string DestFile);
    void DescFile(int OriginalSize, int CopySize, vector<string> Extensions, string FinalDestPath, string OriginalPath, float ExecutionTime);
public:
    void Handler();
};


int main()
{
    FileTaker* FT = new FileTaker();
    FT->Handler();
}


/// Class Defined Here

/// PRIVATE

vector<string> FileTaker::GetMainFile(vector<string> AbsPath)
{
    vector<string> FileName;
    for(auto Path = AbsPath.begin(); Path != AbsPath.end(); Path++)
    {
        size_t SplitPos = (*Path).find_last_of("\\/");
        FileName.push_back((*Path).substr(SplitPos + 1));
    }
    return FileName;
}

string FileTaker::GetDateOfExec()
{
    time_t CurrTime = time(0);
    tm *ltm = localtime(&CurrTime);
    return to_string(ltm->tm_mon + 1) + "_" + to_string(ltm->tm_mday) + "_" + to_string(ltm->tm_year + 1900) + "#" + to_string(ltm->tm_hour) + "_" + to_string(ltm->tm_min) + "@" + to_string(ltm->tm_sec) + "\\";
}

vector<string> FileTaker::GetAllMatchedFiles(vector<string> FileExt, string PathToScan)
{
    vector<string> MatchedFiles;
    string tmp_path = "";
    boost::filesystem::path CurrPath = boost::filesystem::path(PathToScan);
    cout << "Scanning At: " << CurrPath << endl;
    boost::filesystem::recursive_directory_iterator PathWalk(CurrPath);
    while(PathWalk != boost::filesystem::recursive_directory_iterator())
    {
        tmp_path = PathWalk->path().string();
        for(auto Ext = FileExt.begin(); Ext != FileExt.end(); Ext++)
        {
            if(tmp_path.substr(tmp_path.length() - (*Ext).length()) == *Ext)
            {
                MatchedFiles.push_back(tmp_path);
            }
        }
        PathWalk++;
    }
    return MatchedFiles;
}

/// PUBLIC

void FileTaker::Handler()
{
    // ASK WHERE TO SCAN
    cout << "Absolute Path On Where To Scan: ";
    string PathToScan; getline(cin, PathToScan);
    // ASK FILE EXTENSIONS
    vector<string> Exts;
    string Extensions;
    cout << "Input File Extensions [Input END to end list]" << endl;
    while(true)
    {
        getline(cin, Extensions);
        if(Extensions == "END") { break; }
        Exts.push_back(Extensions);
    }
    /// //// START //// ///
    Start = chrono::high_resolution_clock::now();
    // Get Original Paths
    vector<string> OriginalFiles = GetAllMatchedFiles(Exts, PathToScan);
    // Get New Paths
    vector<string> MainFiles = GetMainFile(OriginalFiles);
    // Create Folder To Copy Data
    string FinalDestPath = DestPath + GetDateOfExec();
    mkdir(FinalDestPath.c_str());
    // Copy Data
    cout << "Duplicating Files..." << endl;
    for(unsigned int cFiles = 0; cFiles < OriginalFiles.size(); cFiles++)
    {
        CopyFile(OriginalFiles[cFiles], FinalDestPath + MainFiles[cFiles]);
    }
    cout << "Finished!" << endl;
    End = chrono::high_resolution_clock::now();
    /// //// END //// ///
    ElapsedTime = End - Start;
    // ADD DESC
    DescFile(OriginalFiles.size(), MainFiles.size(), Exts, FinalDestPath, PathToScan, ElapsedTime.count());
    system("PAUSE");
}

void FileTaker::CopyFile(string SrcFilePath, string DestFilePath)
{
    ifstream Original(SrcFilePath, ios::binary);
    ofstream NewFile(DestFilePath, ios::binary);
    NewFile << Original.rdbuf();
    Original.close();
    NewFile.close();
    CopyFileCount++;
}

void FileTaker::DescFile(int OriginalSize, int CopySize, vector<string> Extensions, string FinalDestPath, string OriginalPath, float ExecutionTime)
{
    // CREATE TEXT FILE OF DETAILS
    ofstream dFile(FinalDestPath + "_README_.txt");
    dFile << "Original File Count: " << OriginalSize << endl;
    dFile << "Copied File Count: " << CopySize << endl;
    for(unsigned int c = 0; c < Extensions.size(); c++)
    {
        dFile << "File Extension " << to_string(c + 1) << ": " << Extensions[c] << endl;
    }
    dFile << "Source Path: " << OriginalPath << endl;
    dFile << "Execution Time: " << ExecutionTime << endl;
    dFile.close();
}

