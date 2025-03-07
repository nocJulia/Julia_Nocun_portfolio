#ifndef BAYBYSITTINGAGENCY_REMAININGAGERANGEBABYSITTER_H
#define BAYBYSITTINGAGENCY_REMAININGAGERANGEBABYSITTER_H

#include "Model/BabysitterType.h"

class RemainingAgeRangeBabysitter : public BabysitterType {
public:
    RemainingAgeRangeBabysitter();
    double recalculateBabysitterHourlyWage(double oldHourlyWage) const override;
    std::string getTypeInfo() const override;
    bool isChildAgeValid(int childAge) const override;
};

#endif //BAYBYSITTINGAGENCY_REMAININGAGERANGEBABYSITTER_H

