#ifndef BAYBYSITTINGAGENCY_BABYSITTINGAGENCY_H
#define BAYBYSITTINGAGENCY_BABYSITTINGAGENCY_H
#include <memory>

#include "Managers/ChildrenManager.h"
#include "Managers/BabysittersManager.h"
#include "Managers/ReservationsManager.h"
#include "typedefs.h"

class BabysittingAgency{
private:
    std::shared_ptr<ChildrenManager> childrenManager;
    std::shared_ptr<BabysittersManager> babysittersManager;
    std::shared_ptr<ReservationsManager> reservationsManager;

public:
    BabysittingAgency(const std::shared_ptr<ChildrenManager> &childrenManager,
                      const std::shared_ptr<BabysittersManager> &babysittersManager,
                      const std::shared_ptr<ReservationsManager> &reservationsManager);

    virtual ~BabysittingAgency();

    void start();
    void children();
    void babysitters();
    void reservations();
    void specificChildren();
    void specificBabysitters();
    void specificReservations();
};

#endif //BAYBYSITTINGAGENCY_BABYSITTINGAGENCY_H
