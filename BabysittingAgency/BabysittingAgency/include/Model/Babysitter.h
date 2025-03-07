#ifndef BAYBYSITTINGAGENCY_BABYSITTER_H
#define BAYBYSITTINGAGENCY_BABYSITTER_H

#include <string>
#include <memory>
#include "typedefs.h"

class BabysitterType;

class Babysitter {
private:
    std::string babysitterFirstName;
    std::string babysitterLastName;
    std::string babysitterID;
    int babysitterAge;
    int reservedHours;
    int maxHoursWeekly;
    BabysitterTypePtr babysitterType;
    int yearsOfExperience;

public:
    Babysitter(const std::string &babysitterFirstName, const std::string &babysitterLastName, const std::string &babysitterId,
               int babysitterAge, int maxHoursWeekly, BabysitterTypePtr babysitterType, int yearsOfExperience);

    const std::string &getFirstName() const;
    const std::string &getLastName() const;
    const std::string &getID() const;
    int getBabysitterReservedHours() const;
    int getBabysitterAge() const;
    int getBabysitterYearsOfExperience() const;
    int getBabysitterMaxHoursWeekly() const;
    const BabysitterTypePtr getBabysitterType() const;

    void setBabysitterType(BabysitterTypePtr babysitterType);
    void setYearsOfExperience(int yearsOfExperience);
    void setMaxHoursWeekly(int newMaxHours);
    //methods below are used when a new reservation is made or an old reservation has expired
    void addReservationHours(int newReservedHours);
    void subtractReservationHours(int oldReservedHours);

    int calculateBabysitterHourlyWage();
    std::string getBabysitterInfo();
    bool isBabysitterAvailable();
};

#endif //BAYBYSITTINGAGENCY_BABYSITTER_H
