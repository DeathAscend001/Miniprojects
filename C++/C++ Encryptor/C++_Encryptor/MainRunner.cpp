#include <iostream>
#include <string>
#include <vector>
#include <windows.h>
using namespace std;


class Encrypt
{
private:
    vector<string> Encode(string PlainText);
    string Decode(string Key, string Lock);
public:
    void Handler();
};


int main(int argc, char *argv[])
{
    Encrypt* E = new Encrypt();
    cout << "<Experimental Encryptor>" << endl;
    E->Handler();
    system("PAUSE");
    return 0;
}

vector<string> Encrypt::Encode(string PlainText)
{
    // Key
    string Key = "";
    int Sum = 0;
    for(unsigned int i = 0; i < PlainText.length(); i++)
    {
        Sum += (int)(PlainText[i] + PlainText[i] + (char) i+1);
    }
    Sum %= 255;
    Sum = 128 - Sum;
    if(Sum <= 0) { Sum += PlainText.length() + (int)PlainText[0] * (int)PlainText[0]; }
    Sum %= 255;
    for(unsigned int i = 0; i < PlainText.length(); i++)
    {
        Key += (char)((Sum + i*i + PlainText[i]) % 255);
    }
    // Lock
    string Lock = "";
    unsigned int KeyIndex = 0;
    for(unsigned int i = 0; i < PlainText.length(); i++)
    {
        if(KeyIndex == Key.length() -1) {KeyIndex = 0;}
        Lock += (char) ((int)PlainText[i]) + Key[KeyIndex];
        KeyIndex++;
    }
    return {Key, Lock};
}

string Encrypt::Decode(string Key, string Lock)
{
    // PlainText
    string PlainText = "";
    unsigned int KeyIndex = 0;
    for(unsigned int i = 0; i < Lock.length(); i++)
    {
        if(KeyIndex == Key.length() -1) {KeyIndex = 0;}
        PlainText += (char) ((int)Lock[i]) - Key[KeyIndex];
        KeyIndex++;
    }
    return PlainText;
}

void Encrypt::Handler()
{
    cout << "Choose [1] To Encode" << "\n";
    cout << "Choose [2] To Decode" << "\n";
    int Choice;
    cin >> Choice;
    cin.get();
    switch(Choice)
    {
    case 1:
        {
            cout << "Enter PlainText to Encode: ";
            string PlainText;
            getline(cin, PlainText);
            vector<string> E_ = Encode(PlainText);
            cout << "Key: " << "<" + E_[0] + ">" << "\n";
            cout << "Lock: " << "<" + E_[1] + ">" << "\n";
            cout << "--------------------" << endl;
        }
        break;
    case 2:
        {
            cout << "Enter Key to Encode: ";
            string Key;
            getline(cin, Key);
            cout << "Enter Lock to Encode: ";
            string Lock;
            getline(cin, Lock);
            cout << "PlainText: " << "<" + Decode(Key, Lock) + ">" << "\n";
            cout << "--------------------" << "\n";
        }
        break;
    default:
        break;
    }
}

// // // // //
