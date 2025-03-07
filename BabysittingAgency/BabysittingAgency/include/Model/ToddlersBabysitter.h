#ifndef BAYBYSITTINGAGENCY_TODDLERSBABYSITTER_H
#define BAYBYSITTINGAGENCY_TODDLERSBABYSITTER_H

#include "Model/BabysitterType.h"

class ToddlersBabysitter : public BabysitterType {
private:
    double bonusHourlyWage = 20;

public:
    ToddlersBabysitter();
    double recalculateBabysitterHourlyWage(double oldHourlyWage) const override;
    std::string getTypeInfo() const override;
    bool isChildAgeValid(int childAge) const override;
};

#endif //BAYBYSITTINGAGENCY_TODDLERSBABYSITTER_H

