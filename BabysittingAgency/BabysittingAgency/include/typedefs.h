#ifndef BAYBYSITTINGAGENCY_TYPEDEFS_H
#define BAYBYSITTINGAGENCY_TYPEDEFS_H
#include <memory>
#include "Repository/Repository.h"

class Child;
class Address;
class Babysitter;
class BabysitterType;
class Reservation;

typedef std::shared_ptr<Child> ChildPtr;
typedef std::shared_ptr<Address> AddressPtr;
typedef std::shared_ptr<Babysitter> BabysitterPtr;
typedef std::shared_ptr<BabysitterType> BabysitterTypePtr;
typedef std::shared_ptr<Reservation> ReservationPtr;

#endif //BAYBYSITTINGAGENCY_TYPEDEFS_H