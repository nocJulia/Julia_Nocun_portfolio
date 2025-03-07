#include <boost/test/unit_test.hpp>
#include <boost/test/test_tools.hpp>
#include "Model/Address.h"
#include "typedefs.h"
#include "Model/Babysitter.h"
#include "Model/NewbornsBabysitter.h"
#include <memory>

BOOST_AUTO_TEST_SUITE(TestSuiteBabysitter)

    BabysitterTypePtr newborn = std::make_shared<NewbornsBabysitter>();
    BabysitterPtr babysitter = std::make_shared<Babysitter>("Anna", "Nowak", "43", 25, 10, newborn, 5);

    BOOST_AUTO_TEST_CASE(ConstructorAndGettersTestsBabysitterClass)
    {
        BOOST_CHECK(babysitter->getFirstName() == "Anna");
        BOOST_CHECK(babysitter->getLastName() == "Nowak");
        BOOST_CHECK(babysitter->getID() == "43");
        BOOST_CHECK(babysitter->getBabysitterAge() == 25);
        BOOST_CHECK(babysitter->getBabysitterMaxHoursWeekly() == 10);
        BOOST_CHECK(babysitter->getBabysitterYearsOfExperience() == 5);
        BOOST_CHECK(babysitter->getBabysitterType() == newborn);
        BOOST_CHECK(babysitter->getBabysitterReservedHours() == 0);
    }

    BOOST_AUTO_TEST_CASE(SettersEmptyValuesTestsBabysitterClass)
    {
        babysitter->setBabysitterType(NULL);
        BOOST_CHECK(babysitter->getBabysitterType() == newborn);
        babysitter->setMaxHoursWeekly(0);
        babysitter->setYearsOfExperience(0);
        BOOST_CHECK(babysitter->getBabysitterMaxHoursWeekly() == 10);
        BOOST_CHECK(babysitter->getBabysitterYearsOfExperience() == 5);
    }

    BOOST_AUTO_TEST_CASE(SettersTestsBabysitterClass)
    {
        BabysitterTypePtr newborn2 = std::make_shared<NewbornsBabysitter>();
        babysitter->setBabysitterType(newborn2);
        BOOST_CHECK(babysitter->getBabysitterType() == newborn2);
        babysitter->setMaxHoursWeekly(15);
        babysitter->setYearsOfExperience(33);
        BOOST_CHECK(babysitter->getBabysitterMaxHoursWeekly() == 15);
        BOOST_CHECK(babysitter->getBabysitterYearsOfExperience() == 33);
    }

    BOOST_AUTO_TEST_CASE(ReservedHoursTestsBabysitterClass)
    {
        babysitter->addReservationHours(5);
        BOOST_CHECK(babysitter->getBabysitterReservedHours() == 5);
        babysitter->subtractReservationHours(3);
        BOOST_CHECK(babysitter->getBabysitterReservedHours() == 2);
        babysitter->addReservationHours(-2);
        babysitter->subtractReservationHours(-1);
        BOOST_CHECK(babysitter->getBabysitterReservedHours() == 2);
        BOOST_CHECK(babysitter->isBabysitterAvailable() == true);
        babysitter->addReservationHours(13);
        BOOST_CHECK(babysitter->isBabysitterAvailable() == false);
        babysitter->addReservationHours(1);
        BOOST_CHECK(babysitter->isBabysitterAvailable() == false);
    }

    BOOST_AUTO_TEST_CASE(CalculateHourlyWageTestsBabysitterClass)
    {
        BOOST_CHECK(babysitter->calculateBabysitterHourlyWage() == 120);
        babysitter->setYearsOfExperience(1);
        BOOST_CHECK(babysitter->calculateBabysitterHourlyWage() == 30);
        babysitter->setYearsOfExperience(5);
        BOOST_CHECK(babysitter->calculateBabysitterHourlyWage() == 50);
        babysitter->setYearsOfExperience(10);
        BOOST_CHECK(babysitter->calculateBabysitterHourlyWage() == 80);
    }

BOOST_AUTO_TEST_SUITE_END()