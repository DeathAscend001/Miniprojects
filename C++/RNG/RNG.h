#ifndef RNG_DLL
    #define RNG_API __declspec(dllimport)
#else
    #define RNG_API __declspec(dllexport)
#endif // RNG_DLLS

#include <random>
#include <ctime>

class RNG_API Custom_RNG
{
private:
    unsigned int InvertedBits(unsigned int Seed);
    unsigned int PRNG(unsigned int Seed);
    //
    unsigned int SysRand(int CustomSeed);
public:
    unsigned int Generate_RNG(int CustomSeed);
};
