#include "Model/NewbornsBabysitter.h"

NewbornsBabysitter::NewbornsBabysitter() {}

double NewbornsBabysitter::recalculateBabysitterHourlyWage(double oldHourlyWage) const {
    return oldHourlyWage + bonusHourlyWage;
}

std::string NewbornsBabysitter::getTypeInfo() const {
    std::string Info;
    Info += "Newborns ";
    Info += "\nBonus hourly wage: " + std::to_string(bonusHourlyWage);
    Info += "\nThe babysitter can be reserved only for children in age range 0-1.";
    return Info;
}

bool NewbornsBabysitter::isChildAgeValid(int childAge) const  {
    return (childAge >= 0 && childAge <= 1);
}