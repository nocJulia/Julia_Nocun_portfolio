#include "Model/RemainingAgeRangeBabysitter.h"

RemainingAgeRangeBabysitter::RemainingAgeRangeBabysitter() {}

std::string RemainingAgeRangeBabysitter::getTypeInfo() const {
    std::string Info;
    Info += "RemainingAgeRange ";
    Info += "\nThe babysitter can be reserved only for children in age range 4+.";
    return Info;
}

double RemainingAgeRangeBabysitter::recalculateBabysitterHourlyWage(double oldHourlyWage) const {
    return oldHourlyWage;
}

bool RemainingAgeRangeBabysitter::isChildAgeValid(int childAge) const {
    return (childAge >= 3 && childAge <= 150);
}