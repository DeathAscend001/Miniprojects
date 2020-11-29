#include "File_Support.h"

#include <iostream>
#include <map>
#include <limits>
#include <memory>
#include <windows.h>


class AccountStore
{
public:
    enum class Options { Add, Remove };
    enum class ModType { Encode, Decode };

    AccountStore();
    ~AccountStore();

    void Init_Env();
    void Handler(std::string Key = "");

    template<typename T>
    T GetInput(const std::string& Text="");

private:
    inline void LabelFunc(std::string Title, DWORD DelayLength=500);
    inline void BackFunc();
    inline void Menu();
    inline std::string SiteChoose();
    void Choices(int Choice);

    void Sites(const std::string& SiteName, const Options& option, const std::string& SiteLink="");
    void Accounts(const std::string& Label, const Options& option ,const std::string& SiteName, const std::string& Username="", const std::string& Password="");

    std::vector<std::vector<std::string>> FileDataFixer(const std::vector<std::string>& FileData);
    std::vector<std::string> DataToFile(const std::vector<std::vector<std::string>>& Data);

    std::vector<std::string> AccountCrypt(const std::vector<std::string>& Datas, const ModType& modtype);
    std::string Shifter(const std::string& Data, const ModType& modtype);

    inline std::string EqualizeLength(const std::string& BaseData, const std::string& TargetData);
    int FindCharPos(const char& Needle, const std::string& Haystack); // Currently Unused

    // Key Length = 6
    // Shuffle Length = 90
    /* Simple Caesar Cipher */
    std::string _Ref = "Y|CQ@~0fk*ad;l4N=: iT<3j8qxS,)VvnuEwbc&X675_ygJ?zKHU-mZ!L{#.pBG2P^rM%OoRW`9hF}tDseI][>1$+(A";

    /// Change Dir To Desired Directory
    const char* AccountData = "C:\\Users\\hp\\Documents\\-----------------\\C++\\AccountStore\\Accounts.dat";
    const char* SiteData = "C:\\Users\\hp\\Documents\\-----------------\\C++\\AccountStore\\Sites.dat";

    std::string CurrentKey;
};

static bool isRunning;

int main(int argc, char *argv[])
{
    std::unique_ptr<AccountStore> accs = std::make_unique<AccountStore>();
    // Initialize Environment
    accs->Init_Env();
    // Handle
    switch(argc)
    {
    case 1:
        accs->Handler(accs->GetInput<std::string>("Enter Key: "));
        break;
    case 2:
        accs->Handler(argv[1]);
        break;
    default:
        break;
    }
    return 0;
}

// Constructor + Deconstructor

AccountStore::AccountStore()
{
    std::cout << "Storage Opened!" << std::endl;
}

AccountStore::~AccountStore()
{
    std::cout << "Storage Closed!" << std::endl;
}

// Functions

void AccountStore::Init_Env()
{
    // Create Files
    // // Create Account File
    hr::File_Support::FileCreate(AccountData);
    // Create Site File
    hr::File_Support::FileCreate(SiteData);
}

void AccountStore::Handler(std::string Key)
{
    // Check For Key
    if(Key != "" && Key.length() == 6)
    {
        isRunning = true;
        CurrentKey = Key;
        // Right Key Type
        while(isRunning)
        {
            Menu();

            Choices(GetInput<int>("Choice: "));
        }
    }
    else
    {
        // Wrong Key Type
    }
}

template<typename T>
T AccountStore::GetInput(const std::string& Text)
{
    T var;
    std::cout << Text;
    std::cin >> var;
    std::cin.clear();
    std::cin.ignore(std::numeric_limits<std::streamsize>::max(), '\n');
    return var;
}

void AccountStore::LabelFunc(std::string Title, DWORD DelayLength)
{
    std::cout << Title << std::endl;
    Sleep(DelayLength);
    system("CLS");
}

void AccountStore::BackFunc()
{
    // Wait For Back
    std::cout << std::endl;
    GetInput<std::string>("Enter Any Key to Go Back: ");
}

void AccountStore::Menu()
{
    system("CLS");

    std::cout << "Account Storage" << "\n";
    std::cout << "\n\n";
    std::cout << "[1] => Show All Accounts and Sites" << "\n";
    std::cout << "[2] => Add New Site" << "\n";
    std::cout << "[3] => Remove Existing Site" << "\n";
    std::cout << "[4] => Add New Account" << "\n";
    std::cout << "[5] => Delete Existing Account" << "\n";
    std::cout << "[6] => Change Key [Not yet finished]" << "\n";
    std::cout << "[0] => Exit" << std::endl;
    std::cout << "\n";
}

std::string AccountStore::SiteChoose()
{
    int Index = 0;
    std::map<int, std::string> SiteMap;

    std::vector<std::string> SiteDatas = hr::File_Support::FileGet(SiteData);
    for(std::string Data : SiteDatas)
    {
        std::string Label = Data.substr(0, Data.find("|"));
        std::cout << Index << " : " << Label << std::endl;
        // Map Each Value
        SiteMap.insert(std::pair<int, std::string>(Index, Label));
        Index++;
    }
    // Tell User to make a site first before making an account
    if(Index == 0)
    {
        std::cout << "-- Make a site first!! --" << std::endl;
    }
    // Choose
    int Choice = GetInput<int>("Enter Choice: ");
    for(auto itr = SiteMap.begin(); itr != SiteMap.end(); itr++)
    {
        if(itr->first == Choice)
            return itr->second;
    }
    return "None";
}

void AccountStore::Choices(int Choice)
{
    switch(Choice)
    {
        case 0:
            {
                isRunning = false;
                std::cout << "Exiting..." << std::endl;
                Sleep(800);
            }
            break;
        case 1:
            {
                // Show All
                std::cout << "Showing All Accounts and Sites..." << std::endl;
                Sleep(800);
                system("CLS");
                /// Accounts
                std::cout << "-- All Accounts --" << std::endl;
                // Get All File Contents
                // Decode File
                std::vector<std::vector<std::string>> AccountDatas = FileDataFixer(AccountCrypt(hr::File_Support::FileGet(AccountData), ModType::Decode));
                // Show
                for(auto itr = AccountDatas.begin(); itr != AccountDatas.end(); itr++)
                {
                    for(std::string Data : *itr)
                    {
                        std::cout << Data << std::endl;
                    }
                    std::cout << std::endl;
                }
                /// Sites
                // Get All File Contents
                std::cout << "-- All Sites --" << std::endl;
                std::vector<std::string> SiteDatas = hr::File_Support::FileGet(SiteData);
                for(std::string Data : SiteDatas)
                {
                    std::cout << Data << std::endl;
                }
                // Back
                BackFunc();
            }
            break;
        case 2:
            {
                // Add Site
                LabelFunc("Give Site Information");
                // Give Info
                std::string SiteName = GetInput<std::string>("SiteName: ");
                std::string SiteLink = GetInput<std::string>("SiteLink: ");
                Sites(SiteName, Options::Add, SiteLink);
                // Back
                BackFunc();
            }
            break;
        case 3:
            {
                // Remove Site
                LabelFunc("Give Site Information");
                // Give Info
                Sites(GetInput<std::string>("SiteName: "), Options::Remove);
                // Back
                BackFunc();
            }
            break;
        case 4:
            {
                // Add Account
                LabelFunc("Give Account Information");
                // [S] Site
                // Display All Available Sites then choose
                std::string Site = SiteChoose();
                if(Site != "None")
                {
                    // Label, Username, and Password
                    std::string Label = GetInput<std::string>("Enter Label: ");
                    std::string Username = GetInput<std::string>("Enter Username: ");
                    std::string Password = GetInput<std::string>("Enter Password: ");
                    // Send
                    Accounts(Label, Options::Add, Site, Username, Password);
                    // Back
                    BackFunc();
                }
            }
            break;
        case 5:
            {
                // Remove Account
                LabelFunc("Give Account Information");
            }
            break;
        case 6:
            {
                // Change Key
                LabelFunc("-- Not Yet Finished --");
                BackFunc();
            }
            break;
        default:
            Sleep(1000);
            std::cout << "\n" << "-- ?? --" << std::endl;
            break;
    }
}

std::vector<std::vector<std::string>> AccountStore::FileDataFixer(const std::vector<std::string>& FileData)
{
    // Always Check for [site]
    std::string EndOfLine = "{END}";
    std::string SiteSeparator = "[site]";
    std::vector<std::vector<std::string>> ret_data;
    std::vector<std::string> t_data = {};

    for(std::string Datas : FileData)
    {
        if(Datas.find(EndOfLine) == std::string::npos)
        {
            if(Datas.find(SiteSeparator) != std::string::npos)
            {
                // Push Vector to Main
                if(!t_data.empty())
                {
                    ret_data.push_back(t_data);
                }
                // Clear Vector
                t_data.clear();
                // Add Site
                t_data.push_back(Datas.substr(SiteSeparator.length(), Datas.length()-SiteSeparator.length()));
            }
            else
            {
                // Push Datas under Same Site
                t_data.push_back(Datas);
            }
        }
        else
        {
            // Check For Break
            // Push Last Vector to Main
            ret_data.push_back(t_data);
        }
    }

    return ret_data;
}

std::vector<std::string> AccountStore::DataToFile(const std::vector<std::vector<std::string>>& Data)
{
    std::vector<std::string> ReturnVector;
    for(std::vector V_Datas : Data)
    {
        for(std::string Datas : V_Datas)
        {
            ReturnVector.push_back(Datas);
        }
    }
    return ReturnVector;
}

std::vector<std::string> AccountStore::AccountCrypt(const std::vector<std::string>& Datas, const ModType& modtype)
{
    std::vector<std::string> ReturnVector;
    for(std::string Data : Datas)
    {
        ReturnVector.push_back(Shifter(Data, modtype));
    }
    return ReturnVector;
}

std::string AccountStore::Shifter(const std::string& Data, const ModType& modtype)
{
    // Equalize Data Length To Key Length
    std::string KeyToUse = EqualizeLength(CurrentKey, Data);
    // Initialize Res
    std::string Res;
    switch(modtype)
    {
        case ModType::Encode:
            {
                // Loop Each Character In Data
                for(unsigned int i = 0; i < Data.length(); i++)
                {
                    // Loop With Key
                    // Key + Data = Lock
                    Res += _Ref[(_Ref.find(KeyToUse[i % _Ref.length()]) + _Ref.find(Data[i % _Ref.length()])) % _Ref.length()];
                }
            }
            break;
        case ModType::Decode:
            {
                // Loop Each Character In Data
                for(unsigned int i = 0; i < Data.length(); i++)
                {
                    // Loop With Key
                    // Lock - Key = Data
                    int Number = _Ref.find(Data[i % _Ref.length()]) - _Ref.find(KeyToUse[i % _Ref.length()]);
                    if(Number < 0)
                    {
                        Number = _Ref.length() + Number;
                    }
                    Res += _Ref[Number];
                }
            }
            break;
    }
    return Res;
}

std::string AccountStore::EqualizeLength(const std::string& BaseData, const std::string& TargetData)
{
    std::string KeyUsed = "";
    for(unsigned int i = 0; i < TargetData.length(); i++)
    {
        KeyUsed += BaseData[i%BaseData.length()];
    }
    return KeyUsed;
}

int AccountStore::FindCharPos(const char& Needle, const std::string& Haystack)
{
    for(unsigned int i = 0; i < Haystack.length(); i++)
    {
        if(Needle == Haystack[i])
            return i;
    }
    return INT_MIN;
}

void AccountStore::Sites(const std::string& SiteName, const Options& option, const std::string& SiteLink)
{
    switch(option)
    {
        case Options::Add:
            {
                // Add Site Name and Site Link on Sites.dat
                hr::File_Support::FileSet(SiteData, hr::File_Support::FileWrite::Append, {SiteName+"|"+SiteLink});
            }
            break;
        case Options::Remove:
            {
                // Get All
                std::vector<std::string> Datas = hr::File_Support::FileGet(SiteData);
                // Clear File
                hr::File_Support::FileSet(SiteData, hr::File_Support::FileWrite::Clear, {});
                // Filter
                for(std::string Data : Datas)
                {
                    // Split Label and Link
                    std::string Label = Data.substr(0, Data.find("|"));
                    // Check if Label == Given Label
                    if(Label != SiteName)
                    {
                        // Push Data Back
                        hr::File_Support::FileSet(SiteData, hr::File_Support::FileWrite::Append, {Data});
                    }
                }
            }
            break;
    }
}

void AccountStore::Accounts(const std::string& Label, const Options& option ,const std::string& SiteName, const std::string& Username, const std::string& Password)
{
    // Initialize Return Var
    std::vector<std::vector<std::string>> ReturnVector;
    // Get File Data
    std::vector<std::vector<std::string>> AccountDatas = FileDataFixer(AccountCrypt(hr::File_Support::FileGet(AccountData), ModType::Decode));
    switch(option)
    {
        case Options::Add:
            {
                bool SiteAlreadyExist = false;
                // Find Site
                for(std::vector V_Datas : AccountDatas)
                {
                    // Fix Site
                    V_Datas[0] = "[site]" + V_Datas[0];
                    if(V_Datas.front() == "[site]" + SiteName)
                    {
                        SiteAlreadyExist = true;
                        // Append Info To Last List of Vector
                        // Label
                        V_Datas.push_back("Label: " + Label);
                        // Username
                        V_Datas.push_back("Username: " + Username);
                        // Password
                        V_Datas.push_back("Password: " + Password);
                    }
                    // Push To Main Vector
                    ReturnVector.push_back(V_Datas);
                }
                if(!SiteAlreadyExist)
                {
                    // Add New Site At Bottom when Site doesn't exists yet
                    std::vector<std::string> NewSiteAdd;
                    NewSiteAdd.push_back("[site]" + SiteName); // Add Site
                    NewSiteAdd.push_back("-- -- -- -- -- --");
                    // Label
                    NewSiteAdd.push_back("Label: " + Label);
                    // Username
                    NewSiteAdd.push_back("Username: " + Username);
                    // Password
                    NewSiteAdd.push_back("Password: " + Password);

                    // Append To ReturnVector
                    ReturnVector.push_back(NewSiteAdd);
                }
                ReturnVector.push_back({"{END}"});

                // Add To File
                hr::File_Support::FileSet(AccountData, hr::File_Support::FileWrite::Write, AccountCrypt(DataToFile(ReturnVector), ModType::Encode));
            }
            break;
        case Options::Remove:
            {
            }
            break;
    }
}
