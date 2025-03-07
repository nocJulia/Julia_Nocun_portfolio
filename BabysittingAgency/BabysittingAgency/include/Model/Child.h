#ifndef BABYSITTINGAGENCY_CHILD_H
#define BABYSITTINGAGENCY_CHILD_H
#include <string>
#include <memory>
#include "typedefs.h"
#include "Model/Address.h"

class Child {
private:
    std::string childFirstName;
    std::string childLastName;
    int childAge;
    std::string childID;
    bool isChildAGirl;
    AddressPtr childAddress;

public:
    Child(const std::string &childFirstName, const std::string &childLastName, int childAge, const std::string &childId,
          const bool &isChildAGirl, const AddressPtr childAddress);

    const std::string &getFirstName() const;
    const std::string &getLastName() const;
    int getChildAge() const;
    const std::string &getID() const;
    std::string getChildGender() const;
    const AddressPtr &getChildAddress() const;

    std::string getChildInfo();
    std::string getChildFullName();

    //void setChildFirstName(const std::string &childFirstName);
    //void setChildLastName(const std::string &childLastName);
    void setChildAge(int childAge);
    //void setChildId(const std::string &childId);
    //void setChildGender(const std::string &childGender);
    void setChildAddress(const AddressPtr &childAddress);
};

#endif //BABYSITTINGAGENCY_CHILD_H

