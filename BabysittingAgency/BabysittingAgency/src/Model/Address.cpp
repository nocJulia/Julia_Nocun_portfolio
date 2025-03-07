#include <string>
#include <iostream>
#include "Exceptions/BadArguments.h"
#include "Model/Address.h"


Address::Address(const std::string &city, const std::string &street, const std::string &number) :
        city(city), street(street), number(number) {
    //if (city.empty()) { throw BadArguments("Invalid city (can't be empty)!"); }
    //if (street.empty()) { throw BadArguments("Invalid street (can't be empty)!"); }
    //if (number.empty()) { throw BadArguments("Invalid number (can't be empty)!"); }
}


std::string Address::getAddressInfo(){
    return "\nAddress:\n City: " +this->city+ "\n Street: " +this->street+ "\n Number: " +this->number+ "\n";
}

const std::string &Address::getCity() const {
    return city;
}

const std::string &Address::getStreet() const {
    return street;
}

const std::string &Address::getNumber() const {
    return number;
}

void Address::setCity(const std::string &city) {
    if (city.empty())
    {
        std::cout << "\nCity name was not provided.";
    }
    else
    {
        Address::city = city;
    }
}

void Address::setStreet(const std::string &street) {
    if (street.empty())
    {
        std::cout << "\nStreet name was not provided.";
    }
    else
    {
        Address::street = street;
    }
}

void Address::setNumber(const std::string &number) {
    if (number.empty())
    {
        std::cout << "\nHouse number was not provided.";
    }
    else
    {
        Address::number = number;
    }
}