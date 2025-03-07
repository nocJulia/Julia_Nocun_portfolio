#include "Model/ToddlersBabysitter.h"

ToddlersBabysitter::ToddlersBabysitter() {}

double ToddlersBabysitter::recalculateBabysitterHourlyWage(double oldHourlyWage) const {
    return oldHourlyWage + bonusHourlyWage;
}

std::string ToddlersBabysitter::getTypeInfo() const {
    std::string Info;
    Info += "Toddlers";
    Info += "\nBonus hourly wage: " +std::to_string(bonusHourlyWage);
    Info += "\nThe babysitter can be reserved only for children in age range 1-3.";
    return Info;
}

bool ToddlersBabysitter::isChildAgeValid(int childAge) const {
    return (childAge >= 0 && childAge <= 3);
}