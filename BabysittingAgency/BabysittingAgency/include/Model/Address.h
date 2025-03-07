#ifndef BAYBYSITTINGAGENCY_ADDRESS_H
#define BAYBYSITTINGAGENCY_ADDRESS_H
#include <string>

class Address {
private:
    mutable std::string city;
    mutable std::string street;
    mutable std::string number;
public:
    Address(const std::string &city, const std::string &street, const std::string &number);

    std::string getAddressInfo();

    const std::string &getCity() const;
    const std::string &getStreet() const;
    const std::string &getNumber() const;

    void setCity(const std::string &city);
    void setStreet(const std::string &street);
    void setNumber(const std::string &number);
};

#endif //BAYBYSITTINGAGENCY_ADDRESS_H