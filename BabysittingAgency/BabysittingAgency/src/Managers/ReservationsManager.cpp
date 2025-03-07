#include "Managers/ReservationsManager.h"
#include "string"
#include <boost/date_time/posix_time/ptime.hpp>
#include "typedefs.h"
#include "Model/Reservation.h"
#include "Model/Child.h"
#include "Model/Babysitter.h"
#include "Model/BabysitterType.h"


ReservationsManager::ReservationsManager() {}

std::string ReservationsManager::createReservation(std::string reservationId, const boost::posix_time::ptime &beginTime,
                                            const boost::posix_time::ptime &endTime, ChildPtr child) {
    ReservationPtr reservation = std::make_shared<Reservation>(reservationId, beginTime, endTime, child);
    reservations.add(reservation);
    return "A reservation has successfully been created!";
}

std::string ReservationsManager::reportAllCurrentReservations() {
    std::string report;
    for (int i = 0; i < reservations.size(); i++) {
        report += reservations.get(i)->getReservationInfo() + "\n";
    }
    if (report.empty()) {
        return "No reservations have been made yet.";
    }
    return report;
}

std::string ReservationsManager::cancelReservation(std::string ID) {
    std::shared_ptr<Reservation> reservationToCancel = reservations.find(FindByID<Reservation>(ID));
    std::shared_ptr<Babysitter> babysitter = reservationToCancel->getBabysitter();
    int reservationLength = reservationToCancel->getReservationLength();
    reservations.remove(reservationToCancel);
    if (!reservations.find(FindByID<Reservation>(ID))) {
        babysitter->subtractReservationHours(reservationLength);
        return "The reservation with the given ID: " +ID+ " has been cancelled.";
    }
    else {
        throw BadArguments("An error has occurred, while trying to cancel the reservation with the"
                           "given ID: " +ID+ ". (Perhaps there is no such reservation?)");
    }
}

std::string ReservationsManager::assignBabysitter(BabysitterPtr babysitter, std::string ID) {
    std::shared_ptr<Reservation> reservationToAssignBabysitter = reservations.find(FindByID<Reservation>(ID));
    reservationToAssignBabysitter->setBabysitter(babysitter);
    if (!babysitter->getBabysitterType()->isChildAgeValid(reservationToAssignBabysitter->getChild()->getChildAge())) {
        throw BadArguments("Cannot assign the babysitter to the child. Invalid babysitter type for child's age.");
    }

    if (reservations.find(FindByID<Reservation>(ID))->getBabysitter() == babysitter) {
        return "The babysitter has successfully been assigned!";
    }
    else {
        throw BadArguments("An assignment of the babysitter has failed! (no such babysitter?)");
    }
}


std::string ReservationsManager::getSpecificReservationReportByID(std::string id) {
    std::vector<std::shared_ptr<Reservation>> matchingReservations = reservations.findAll(FindByID<Reservation>(id));
    std::string report;
    if (matchingReservations.empty()) {
        report = "No reservations found.";
    } else {
        report = "Matching reservations:\n";
        for (const auto& reservation : matchingReservations) {
            report += reservation->getReservationInfo() + "\n";
        }
    }

    return report;
}

std::string ReservationsManager::getSpecificReservationReportByChildID(std::string childID) {
    std::vector<std::shared_ptr<Reservation>> matchingReservations;

    for (int i = 0; i < reservations.size(); i++) {
        if (reservations.get(i)->getChild()->getID() == childID) {
            matchingReservations.push_back(reservations.get(i));
        }
    }

    std::string report;
    if (matchingReservations.empty()) {
        report = "No reservations found for child with ID: " + childID;
    } else {
        report = "Matching reservations for child with ID " + childID + ":\n";
        for (const auto& reservation : matchingReservations) {
            report += reservation->getReservationInfo() + "\n";
        }
    }

    return report;
}

std::string ReservationsManager::getUnassignedReservationsReport() {
    std::vector<std::shared_ptr<Reservation>> unassignedReservations = reservations.findAll(FindWithoutBabysitter<Reservation>());

    std::string report;
    if (unassignedReservations.empty()) {
        report = "No unassigned reservations found.\n";
    } else {
        report = "Unassigned reservations:\n";
        for (const auto& reservation : unassignedReservations) {
            report += "Reservation ID: " + reservation->getID() + "\n";
            report += "Begin Time: " + to_simple_string(reservation->getBeginTime()) + "\n";
            report += "End Time: " + to_simple_string(reservation->getEndTime()) + "\n";
            report += "Child Name: " + reservation->getChild()->getFirstName() + "\n";
            report += "Child ID: " + reservation->getChild()->getID() + "\n";
            report += "Child Age: " + std::to_string(reservation->getChild()->getChildAge()) + "\n\n";
        }
    }

    return report;
}