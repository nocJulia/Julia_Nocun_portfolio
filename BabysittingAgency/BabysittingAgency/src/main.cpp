#include <iostream>
#include <memory>
#include "Repository/Repository.h"
#include "Model/BabysittingAgency.h"
#include "Managers/ChildrenManager.h"
#include "Managers/ReservationsManager.h"
#include "Managers/BabysittersManager.h"
#include "Model/Babysitter.h"
#include "Model/NewbornsBabysitter.h"
#include "Model/Child.h"
#include "typedefs.h"

int main() {
    std::shared_ptr<ChildrenManager> childrenManager = std::make_shared<ChildrenManager>();
    std::shared_ptr<BabysittersManager> babysittersManager = std::make_shared<BabysittersManager>();
    std::shared_ptr<ReservationsManager> reservationsManager = std::make_shared<ReservationsManager>();

    BabysittingAgency communication(childrenManager, babysittersManager, reservationsManager);
    communication.start();

    /*Repository<Babysitter> repo;

    std::shared_ptr<NewbornsBabysitter> noworodki1 = std::make_shared<NewbornsBabysitter>();
    std::shared_ptr<Babysitter> opiekunka1 = std::make_shared<Babysitter>("Alina", "Mierzej", "1N", 25, 40, noworodki1, 10);
    std::shared_ptr<Address> address1 = std::make_shared<Address>("Warszawa", "Koloowa", "53c");
    std::shared_ptr<Child> dziecko1 = std::make_shared<Child>("Weronika", "KappaHD", 5, "2", true, address1);
    repo.add(opiekunka1);*/

//                                                                          noworodki1, 5);
//    std::shared_ptr<Babysitter> opiekunka3 = std::make_shared<Babysitter>("Blazejnia", "Tralala", "3N", 26, 43,
//                                                                          noworodki1, 6);
//
//    repo.add(opiekunka1);
//    repo.add(opiekunka2);
//    repo.add(opiekunka3);
//
//    std::cout << "Size: " << repo.size() << std::endl;
//
//    std::shared_ptr<Babysitter> foundObject = repo.get(1);
//    if (foundObject) {
//        std::cout << "Found object: " << foundObject->getBabysitterId() << ", " << foundObject->getBabysitterFirstName()
//                  << std::endl;
//    } else {
//        std::cout << "Object not found." << std::endl;
//    }
//
//    std::cout << "Repository report: " << repo.report() << std::endl;
//
//    repo.remove(opiekunka1);
//    std::cout << "Size: " << repo.size() << std::endl;
//
//    std::shared_ptr<Babysitter> foundObject2 = repo.get(1);
//    if (foundObject2) {
//        std::cout << "Found object: " << foundObject2->getBabysitterId() << ", "
//                  << foundObject2->getBabysitterFirstName() << std::endl;
//    } else {
//        std::cout << "Object not found." << std::endl;
//    }
//
//    std::cout << "Repository report: " << repo.report() << std::endl;
//    std::shared_ptr<Babysitter> foundBabysitter = repo.find(FindByFirstName("Blazejnia"));
//
//    if (foundBabysitter) {
//        std::cout << foundBabysitter->getBabysitterFirstName();
//    }
}