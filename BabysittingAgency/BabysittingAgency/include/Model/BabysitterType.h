#ifndef BAYBYSITTINGAGENCY_BABYSITTERTYPE_H
#define BAYBYSITTINGAGENCY_BABYSITTERTYPE_H
#include <string>

class BabysitterType {
public:
    virtual double recalculateBabysitterHourlyWage(double bonusHourlyWage) const = 0;
    virtual std::string getTypeInfo() const = 0;
    virtual bool isChildAgeValid(int childAge) const = 0;
};
#endif //BAYBYSITTINGAGENCY_BABYSITTERTYPE_H

