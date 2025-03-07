#include <string>
#include <memory>
#include <iostream>
#include "Model/Child.h"
#include "Exceptions/BadArguments.h"

class Address;

Child::Child(const std::string &childFirstName, const std::string &childLastName, int childAge,
             const std::string &childId, const bool &isChildAGirl, const AddressPtr childAddress)
        : childFirstName(childFirstName), childLastName(childLastName), childAge(childAge), childID(childId),
          isChildAGirl(isChildAGirl), childAddress(childAddress) {

    //if (childAddress == nullptr) { throw BadArguments("Cannot create child without address!"); }
    //if (childFirstName.empty()) { throw BadArguments("Invalid childFirstName (can't be empty)!"); }
    //if (childLastName.empty()) { throw BadArguments("Invalid childLastName (can't be empty)!"); }
    //if (childId.empty()) { throw BadArguments("Invalid childID (can't be empty)!"); }
}


std::string Child::getChildInfo(){
    return "\nChild: " + this->getChildFullName() + " \n Age: " + std::to_string(this->childAge)+ "\n ID: " + this->childID+
           "\n Gender: " + this->getChildGender() + "\n" + this->childAddress->getAddressInfo();
}

const std::string &Child::getFirstName() const {
    return childFirstName;
}

const std::string &Child::getLastName() const {
    return childLastName;
}

int Child::getChildAge() const {
    return childAge;
}

const std::string &Child::getID() const {
    return childID;
}

std::string Child::getChildGender() const {
    if (isChildAGirl) {
        return "a girl";
    }
    else {
        return "a boy";
    }
}


void Child::setChildAge(int childAge) {
    Child::childAge = childAge;
}

std::string Child::getChildFullName() {
    return "\n First name: " + this->childFirstName + "\n Last name: " + this->childLastName;
}

const AddressPtr &Child::getChildAddress() const {
    return childAddress;
}

void Child::setChildAddress(const AddressPtr &childAddress) {
    if (childAddress == NULL)
    {
        std::cout << "\nThe address was not provided.";
    }
    else
    {
        Child::childAddress = childAddress;
    }
}

