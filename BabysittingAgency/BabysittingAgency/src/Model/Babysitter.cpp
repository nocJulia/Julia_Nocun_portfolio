#include "Model/Babysitter.h"
#include "Model/BabysitterType.h"
#include <iostream>

Babysitter::Babysitter(const std::string &babysitterFirstName, const std::string &babysitterLastName, const std::string &babysitterId,
                       int babysitterAge, int maxHoursWeekly, BabysitterTypePtr babysitterType, int yearsOfExperience) :
                       babysitterFirstName(babysitterFirstName), babysitterLastName(babysitterLastName), babysitterID(babysitterId),
        babysitterAge(babysitterAge), maxHoursWeekly(maxHoursWeekly), babysitterType(babysitterType),
        yearsOfExperience(yearsOfExperience) {
    reservedHours = 0;
}

const std::string &Babysitter::getFirstName() const {
    return babysitterFirstName;
}

const std::string &Babysitter::getLastName() const {
    return babysitterLastName;
}

const std::string &Babysitter::getID() const {
    return babysitterID;
}

int Babysitter::getBabysitterAge() const {
    return babysitterAge;
}

int Babysitter::getBabysitterYearsOfExperience() const {
    return yearsOfExperience;
}

int Babysitter::getBabysitterMaxHoursWeekly() const {
    return maxHoursWeekly;
}

int Babysitter::getBabysitterReservedHours() const {
    return reservedHours;
}

void Babysitter::setBabysitterType(BabysitterTypePtr babysitterType) {
    if (babysitterType == NULL) {
        std::cout << "An empty babysitter type was provided.";
    }
    else {
        Babysitter::babysitterType = babysitterType;
    }
}

void Babysitter::setYearsOfExperience(int yearsOfExperience) {
    if (yearsOfExperience > 0) {
        Babysitter::yearsOfExperience = yearsOfExperience;
    }
}

void Babysitter::setMaxHoursWeekly(int newMaxHours) {
    if (newMaxHours > 0) {
        Babysitter::maxHoursWeekly = newMaxHours;
    }
}

std::string Babysitter::getBabysitterInfo() {
    std::string info;
    info += "\nFirst name: " +babysitterFirstName;
    info += "\nLast name: " +babysitterLastName;
    info += "\nID: " +babysitterID;
    info += "\nAge: " + std::to_string(babysitterAge);
    info += "\nReserved hours: " +std::to_string(reservedHours);
    info += "\nMax. hours of work weekly: " +std::to_string(maxHoursWeekly);
    info += "\nBabysitter type: " +babysitterType->getTypeInfo();
    return info;
}

int Babysitter::calculateBabysitterHourlyWage() {
    if (yearsOfExperience <= 2) {
        return 30;
    }
    else if (yearsOfExperience <= 5) {
        return 50;
    }
    else if (yearsOfExperience <= 10) {
        return 80;
    }
    else {
        return 120;
    }
}

bool Babysitter::isBabysitterAvailable() {
    if (reservedHours < maxHoursWeekly) {
        return true;
    }
    else {
        return false;
    }
}

const BabysitterTypePtr Babysitter::getBabysitterType() const {
    return babysitterType;
}

void Babysitter::addReservationHours(int newReservedHours) {
    if (newReservedHours > 0) {
        if (newReservedHours + reservedHours <= maxHoursWeekly) {
            reservedHours += newReservedHours;
        }
        else {
            throw BadArguments("The maximum number of working hours of the babysitter, has been exceeded!");
        }
    }
}

void Babysitter::subtractReservationHours(int oldReservedHours) {
    if (oldReservedHours > 0) {
        reservedHours -= oldReservedHours;
    }
}
