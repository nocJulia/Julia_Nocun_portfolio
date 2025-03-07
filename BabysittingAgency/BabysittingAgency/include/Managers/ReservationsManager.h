#ifndef BAYBYSITTINGAGENCY_RESERVATIONSMANAGER_H
#define BAYBYSITTINGAGENCY_RESERVATIONSMANAGER_H
#include "string"
#include "typedefs.h"
#include <boost/date_time.hpp>

class ReservationsManager{
private:
    Repository<Reservation> reservations;
public:
    ReservationsManager();

    std::string createReservation(std::string reservationId, const boost::posix_time::ptime &beginTime,
                                  const boost::posix_time::ptime &endTime, ChildPtr child);
    std::string assignBabysitter(BabysitterPtr babysitter, std::string ID);
    std::string reportAllCurrentReservations();
    std::string cancelReservation(std::string ID);
    std::string getSpecificReservationReportByID(std::string ID);
    std::string getSpecificReservationReportByChildID(std::string childID);
    std::string getUnassignedReservationsReport();
};

#endif //BAYBYSITTINGAGENCY_RESERVATIONSMANAGER_H
