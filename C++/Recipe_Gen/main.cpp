#include <iostream>
#include <fstream>
#include <vector>
#include <windows.h>
#include "RNG.h"
using namespace std;

string line;

vector<string> ReadFiles(string FileName)
{
    vector<string> Lines;
    if(FileName != "__PullFiles__.txt")
    {
        //Lines.push_back(FileName);
    }
    ifstream File("C:\\Users\\trias\\Documents\\_\\Tests\\MiniProjects\\C++\\Recipe_Gen\\Ingredients\\" + FileName);
    if(File.is_open())
    {
        while(getline(File, line))
        {
            Lines.push_back(line);
        }
        File.close();
    }
    return Lines;
}


int main()
{
    Custom_RNG RNG;
    vector<vector<string>> Recipes;
    vector<string> RecipeFiles;
    // Get Recipe Files
    RecipeFiles = ReadFiles("__PullFiles__.txt");
    // Get Recipe For Each File
    for(auto i = RecipeFiles.begin(); i != RecipeFiles.end(); i++)
    {
        Recipes.push_back(ReadFiles(*i + ".txt"));
    }
    // Display
    cout << "Try Eating: " << "\n";
    for(unsigned int j = 0; j < Recipes.size(); j++)
    {
        cout << Recipes[j][RNG.Generate_RNG(j+1 * time(0)) % Recipes[j].size()];
        if(j < Recipes.size() - 1) { cout << " + "; }
    }
    cout << endl;
    system("PAUSE");
    return 0;
}
