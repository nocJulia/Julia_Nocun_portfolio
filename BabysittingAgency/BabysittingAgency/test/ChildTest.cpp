#include <boost/test/unit_test.hpp>
#include "Model/Address.h"
#include "Model/Child.h"
#include "typedefs.h"
#include <memory>

BOOST_AUTO_TEST_SUITE(TestSuiteChild)

    AddressPtr addressTest = std::make_shared<Address>("Wroclaw", "Kolorowa", "245");
    AddressPtr addressTestSecond = std::make_shared<Address>("Poznan", "Teczowa", "12");
    ChildPtr childTest = std::make_shared<Child>("Anna", "Nowak", 7, "223", true, addressTest);

    BOOST_AUTO_TEST_CASE(ConstrutorAndGettersTestsChildClass)
    {
        BOOST_REQUIRE_EQUAL(childTest->getFirstName(), "Anna");
        BOOST_REQUIRE_EQUAL(childTest->getLastName(), "Nowak");
        BOOST_REQUIRE_EQUAL(childTest->getChildAge(), 7);
        BOOST_REQUIRE_EQUAL(childTest->getID(), "223");
        BOOST_REQUIRE_EQUAL(childTest->getChildGender(), "a girl");
        BOOST_REQUIRE_EQUAL(childTest->getChildAddress()->getCity(), "Wroclaw");
        BOOST_REQUIRE_EQUAL(childTest->getChildAddress()->getStreet(), "Kolorowa");
        BOOST_REQUIRE_EQUAL(childTest->getChildAddress()->getNumber(), "245");
    }

    BOOST_AUTO_TEST_CASE(SettersEmptyValuesTestsChildClass)
    {
        childTest->setChildAddress(NULL);
        BOOST_REQUIRE_EQUAL(childTest->getChildAddress()->getCity(), "Wroclaw");
        BOOST_REQUIRE_EQUAL(childTest->getChildAddress()->getStreet(),"Kolorowa");
        BOOST_REQUIRE_EQUAL(childTest->getChildAddress()->getNumber(), "245");
    }

    BOOST_AUTO_TEST_CASE(SettersTestsChildClass)
    {
        childTest->setChildAge(5);
        BOOST_REQUIRE_EQUAL(childTest->getChildAge(), 5);
        childTest->setChildAddress(addressTestSecond);
        BOOST_REQUIRE_EQUAL(childTest->getChildAddress()->getCity(), "Poznan");
        BOOST_REQUIRE_EQUAL(childTest->getChildAddress()->getStreet(), "Teczowa");
        BOOST_REQUIRE_EQUAL(childTest->getChildAddress()->getNumber(), "12");
    }

BOOST_AUTO_TEST_SUITE_END()