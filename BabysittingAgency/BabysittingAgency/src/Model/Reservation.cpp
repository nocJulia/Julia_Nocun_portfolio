#include <boost/date_time/posix_time/ptime.hpp>
#include <string>
#include "Model/Child.h"
#include "Model/Reservation.h"
#include "Model/Babysitter.h"
#include "Model/BabysitterType.h"

Reservation::Reservation(std::string reservationId, const boost::posix_time::ptime &beginTime, const boost::posix_time::ptime &endTime,
                         ChildPtr child) : reservationID(reservationId), beginTime(beginTime), endTime(endTime), child(child) {
    babysitter = nullptr;
}

std::string Reservation::getID() const {
    return reservationID;
}

const boost::posix_time::ptime &Reservation::getBeginTime() const {
    return beginTime;
}

const boost::posix_time::ptime &Reservation::getEndTime() const {
    return endTime;
}

void Reservation::setBabysitter(BabysitterPtr babysitter) {
    Reservation::babysitter = babysitter;
    double reservationLength = getReservationLength();
    babysitter->addReservationHours(reservationLength);
}

int Reservation::getReservationLength() {
    boost::posix_time::time_period period(beginTime, endTime);
    return period.length().hours();
}

void Reservation::calculateReservationCost() {
    double hours = getReservationLength();
    double oldHourlyWage = this->babysitter->calculateBabysitterHourlyWage();
    double hourlyWage = this->babysitter->getBabysitterType()->recalculateBabysitterHourlyWage(oldHourlyWage);
    setFinalReservationCost(hours * hourlyWage);
}

std::string Reservation::getReservationInfo() {
    std::string info = "Reservation Info:\n";
    info += "Reservation ID: " +reservationID;
    const std::string beg_time = to_simple_string(beginTime);
    info += "\nReservation Start time: " +beg_time;
    const std::string end_time = to_simple_string(endTime);
    info += "\nReservation End time: " +end_time;
    if (babysitter != nullptr) {
        calculateReservationCost();
        info += "\nReservation cost: " + std::to_string(finalReservationCost);
    }
    info += "\nChild info: " +child->getChildInfo();
    if (babysitter == nullptr) {
        return info;
    }
    info += "\nBabysitter info: " +babysitter->getBabysitterInfo();
    return info;
}

void Reservation::setFinalReservationCost(double FinalCost) {
    finalReservationCost = FinalCost;
}

double Reservation::getFinalReservationCost() {
    return finalReservationCost;
}

ChildPtr Reservation::getChild() {
    return child;
};

BabysitterPtr Reservation::getBabysitter() {
    return babysitter;
}