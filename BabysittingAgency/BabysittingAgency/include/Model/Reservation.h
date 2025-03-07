#ifndef BAYBYSITTINGAGENCY_RESERVATION_H
#define BAYBYSITTINGAGENCY_RESERVATION_H

#include <boost/date_time.hpp>

class Child;
class Babysitter;

class Reservation {
private:
    std::string reservationID;
    boost::posix_time::ptime beginTime;
    boost::posix_time::ptime endTime;
    double finalReservationCost;
    ChildPtr child;
    BabysitterPtr babysitter;

public:
    Reservation(std::string reservationId, const boost::posix_time::ptime &beginTime, const boost::posix_time::ptime &endTime,
                ChildPtr child);

    std::string getID() const;
    const boost::posix_time::ptime &getBeginTime() const;
    const boost::posix_time::ptime &getEndTime() const;

    void setBabysitter(BabysitterPtr babysitter);
    void setFinalReservationCost(double FinalCost);

    double getFinalReservationCost();
    int getReservationLength();
    void calculateReservationCost();
    std::string getReservationInfo();
    ChildPtr getChild();
    BabysitterPtr getBabysitter();
};

#endif //BAYBYSITTINGAGENCY_RESERVATION_H

