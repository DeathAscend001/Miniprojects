#include "RNG.h"

unsigned int Custom_RNG::SysRand(int CustomSeed)
{
    // Mersenne Twister + Rand()
    srand(CustomSeed);
    std::mt19937 mt(rand());
    std::uniform_int_distribution<int> uid(0, 2000000000);
    return uid(mt);
}

//

unsigned int Custom_RNG::PRNG(unsigned int Seed)
{
    Seed = 8253729 * Seed/2 + Seed + 2396403;
    return Seed % 99999;
}

unsigned int Custom_RNG::InvertedBits(unsigned int Seed)
{
    return ~Seed;
}

//////

unsigned int Custom_RNG::Generate_RNG(int CustomSeed)
{
    return PRNG(SysRand(CustomSeed)) ^ InvertedBits(SysRand(CustomSeed));
}
