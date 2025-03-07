#ifndef BAYBYSITTINGAGENCY_CHILDRENMANAGER_H
#define BAYBYSITTINGAGENCY_CHILDRENMANAGER_H
#include "typedefs.h"
#include "string"

class ChildrenManager{
private:
    Repository<Child> children;
public:
    ChildrenManager();

    std::string registerChild(const std::string &childFirstName, const std::string &childLastName, int childAge,
                              const std::string &childId, const bool &isChildAGirl, const std::string &city,
                              const std::string &street, const std::string &number);
    std::string deregisterChild(std::string ID);
    std::string getAllChildrenReport();
    std::string getSpecificChildReportByFirstName(std::string firstName);
    std::string getSpecificChildReportByID(std::string ID);
    std::string getAllChildrenReportNamesAndIDsOnly();
    Repository<Child> getChildren();
};

#endif //BAYBYSITTINGAGENCY_CHILDRENMANAGER_H
