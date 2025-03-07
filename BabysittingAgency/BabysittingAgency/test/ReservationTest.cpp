#include <boost/test/unit_test.hpp>
#include <boost/test/test_tools.hpp>
#include "typedefs.h"
#include <memory>
#include "Model/Child.h"
#include "Model/Address.h"
#include "Model/Babysitter.h"
#include "Model/NewbornsBabysitter.h"
#include "Model/Reservation.h"
#include "Model/ToddlersBabysitter.h"
#include "Model/RemainingAgeRangeBabysitter.h"
#include <boost/date_time.hpp>

BOOST_AUTO_TEST_SUITE(TestSuiteReservation)

    BabysitterTypePtr newbornBabysitter = std::make_shared<NewbornsBabysitter>();
    BabysitterTypePtr toddlerBabysitter = std::make_shared<ToddlersBabysitter>();
    BabysitterPtr babysitter1 = std::make_shared<Babysitter>("Anna", "Nowak", "43", 25, 10, newbornBabysitter, 5);
    BabysitterPtr babysitter2 = std::make_shared<Babysitter>("Wiktoria", "Kowalska", "57", 28, 35, toddlerBabysitter, 8);
    AddressPtr address1 = std::make_shared<Address>("Warszawa", "Koralowa", "76");
    AddressPtr address2 = std::make_shared<Address>("Warszawa", "Lipowa", "37");
    ChildPtr childPiotrW = std::make_shared<Child>("Piotr", "Wilczak", 0.6, "N1", false, address1);
    ChildPtr childMichalN = std::make_shared<Child>("Michal", "Najman", 2, "T1", false, address2);
    boost::posix_time::ptime beginTime1 = boost::posix_time::time_from_string("2023-06-16 8:30:00");
    boost::posix_time::ptime endTime1 = boost::posix_time::time_from_string("2023-06-16 15:30:00");
    ReservationPtr reservation1 = std::make_shared<Reservation>("1", beginTime1, endTime1, childPiotrW);
    ReservationPtr reservation2 = std::make_shared<Reservation>("2", beginTime1, endTime1, childMichalN);

    BOOST_AUTO_TEST_CASE(ConstructorAndGettersTestsReservationClass)
    {
        BOOST_CHECK(reservation1->getEndTime() == boost::posix_time::time_from_string("2023-06-16 15:30:00"));
        BOOST_CHECK(reservation1->getBeginTime() == boost::posix_time::time_from_string("2023-06-16 08:30:00"));
        BOOST_CHECK(reservation1->getID() == "1");
        BOOST_CHECK(reservation1->getReservationLength() == 7);
        reservation1->setBabysitter(babysitter1);
        BOOST_CHECK(reservation1->getBabysitter()->getFirstName() == "Anna");
        BOOST_CHECK(reservation1->getChild()->getFirstName() == "Piotr");
    }

    BOOST_AUTO_TEST_CASE(SetAndCalculateFinalReservationCostTestsReservationClass)
    {
        reservation1->calculateReservationCost();
        BOOST_CHECK(reservation1->getFinalReservationCost() == 560);
        reservation2->setBabysitter(babysitter2);
        reservation2->calculateReservationCost();
        BOOST_CHECK(reservation2->getFinalReservationCost() == 700);

        AddressPtr address3 = std::make_shared<Address>("Warszawa", "Wektorowa", "35");
        BabysitterTypePtr remainingAgeRangeBabysitter = std::make_shared<RemainingAgeRangeBabysitter>();
        BabysitterPtr babysitter3 = std::make_shared<Babysitter>("Maria", "Kowalska", "58", 30, 40, remainingAgeRangeBabysitter, 15);
        ChildPtr childMajaW = std::make_shared<Child>("Maja", "Wilan", 12, "R1", true, address3);
        ReservationPtr reservation3 = std::make_shared<Reservation>("3", beginTime1, endTime1, childMajaW);
        reservation3->setBabysitter(babysitter3);
        reservation3->calculateReservationCost();
        BOOST_CHECK(reservation3->getFinalReservationCost() == 840);
    }

BOOST_AUTO_TEST_SUITE_END()
