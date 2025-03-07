#include <boost/test/unit_test.hpp>
#include "typedefs.h"
#include <memory>
#include "Model/NewbornsBabysitter.h"
#include "Model/ToddlersBabysitter.h"
#include "Model/Babysitter.h"

BOOST_AUTO_TEST_SUITE(TestSuiteBabysitterTypes)

    BabysitterTypePtr newbornsBabysitter = std::make_shared<NewbornsBabysitter>();
    BabysitterTypePtr toddlersBabysitter = std::make_shared<ToddlersBabysitter>();
    BabysitterPtr babysitter = std::make_shared<Babysitter>("Anna", "Nowak", "43", 25, 10, newbornsBabysitter, 5);

    BOOST_AUTO_TEST_CASE(ConstrutorAndRecalculateHourlyWageTestsAddressClass)
    {
        BOOST_CHECK(babysitter->calculateBabysitterHourlyWage() == 50);
        BOOST_CHECK(babysitter->getBabysitterType()->recalculateBabysitterHourlyWage(50) == 80);
        babysitter->setBabysitterType(toddlersBabysitter);
        BOOST_CHECK(babysitter->getBabysitterType()->recalculateBabysitterHourlyWage(50) == 70);
    }

BOOST_AUTO_TEST_SUITE_END()
