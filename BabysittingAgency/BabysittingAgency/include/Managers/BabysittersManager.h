#ifndef BAYBYSITTINGAGENCY_BABYSITTERSMANAGER_H
#define BAYBYSITTINGAGENCY_BABYSITTERSMANAGER_H
#include "typedefs.h"

class BabysittersManager{
private:
    Repository<Babysitter> babysitters;
public:
    BabysittersManager();

    std::string hireBabysitter(const std::string &babysitterFirstName, const std::string &babysitterLastName, const std::string &babysitterId,
                               int babysitterAge, int maxHoursWeekly, BabysitterTypePtr babysitterType, int yearsOfExperience);
    std::string layBabysitterOff(std::string ID);
    std::string reportAllCurrentBabysitters();
    std::string getSpecificBabysitterReportByFirstName(std::string firstName);
    std::string getSpecificBabysitterReportByID(std::string ID);
    std::string getAvailableBabysittersReport();
    std::shared_ptr<Babysitter> getSpecificBabysitterByID(std::string ID);
};

#endif //BAYBYSITTINGAGENCY_BABYSITTERSMANAGER_H
