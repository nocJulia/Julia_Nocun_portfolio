#ifndef BAYBYSITTINGAGENCY_NEWBORNSBABYSITTER_H
#define BAYBYSITTINGAGENCY_NEWBORNSBABYSITTER_H

#include "Model/BabysitterType.h"

class NewbornsBabysitter : public BabysitterType {
private:
    double bonusHourlyWage = 30;

public:
    NewbornsBabysitter();
    double recalculateBabysitterHourlyWage(double oldHourlyWage) const override;
    std::string getTypeInfo() const override;
    bool isChildAgeValid(int childAge) const override;
};

#endif //BAYBYSITTINGAGENCY_NEWBORNSBABYSITTER_H

