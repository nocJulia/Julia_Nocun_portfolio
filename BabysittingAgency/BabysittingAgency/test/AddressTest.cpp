#include <boost/test/unit_test.hpp>
#include "Model/Address.h"
#include "typedefs.h"
#include <memory>

BOOST_AUTO_TEST_SUITE(TestSuiteAddress)

    AddressPtr addressTest = std::make_shared<Address>("Warszawa", "Kosciuszki", "35");

    BOOST_AUTO_TEST_CASE(ConstrutorAndGettersTestsAddressClass)
    {
        BOOST_REQUIRE_EQUAL(addressTest->getCity(), "Warszawa");
        BOOST_REQUIRE_EQUAL(addressTest->getStreet(), "Kosciuszki");
        BOOST_REQUIRE_EQUAL(addressTest->getNumber(), "35");
    }

    BOOST_AUTO_TEST_CASE(SettersEmptyValuesTestsAddressClass)
    {
        addressTest->setCity("");
        BOOST_REQUIRE_EQUAL(addressTest->getCity(), "Warszawa");
        addressTest->setStreet("");
        BOOST_REQUIRE_EQUAL(addressTest->getStreet(), "Kosciuszki");
        addressTest->setNumber("");
        BOOST_REQUIRE_EQUAL(addressTest->getNumber(), "35");
    }

    BOOST_AUTO_TEST_CASE(SettersTestsAddressClass)
    {
        addressTest->setCity("Lodz");
        BOOST_REQUIRE_EQUAL(addressTest->getCity(), "Lodz");
        addressTest->setStreet("Piotrkowska");
        BOOST_REQUIRE_EQUAL(addressTest->getStreet(), "Piotrkowska");
        addressTest->setNumber("127");
        BOOST_REQUIRE_EQUAL(addressTest->getNumber(), "127");
    }

BOOST_AUTO_TEST_SUITE_END()