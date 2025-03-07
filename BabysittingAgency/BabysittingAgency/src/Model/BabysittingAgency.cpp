#include "Model/BabysittingAgency.h"
#include "Exceptions/BadArguments.h"
#include "Exceptions/LogicError.h"
#include <iostream>

#include "Managers/ChildrenManager.h"
#include "Managers/BabysittersManager.h"
#include "Managers/ReservationsManager.h"

#include "Model/Child.h"
#include "Model/Address.h"
#include "Model/Babysitter.h"
#include "Model/Reservation.h"
#include "Model/NewbornsBabysitter.h"
#include "Model/ToddlersBabysitter.h"
#include "Model/RemainingAgeRangeBabysitter.h"
#include <boost/date_time/posix_time/ptime.hpp>

using namespace std;

BabysittingAgency::BabysittingAgency(const shared_ptr<ChildrenManager> &childrenManager,
                                     const shared_ptr<BabysittersManager> &babysittersManager,
                                     const shared_ptr<ReservationsManager> &reservationsManager) : childrenManager(
        childrenManager), babysittersManager(babysittersManager), reservationsManager(reservationsManager) {}

BabysittingAgency::~BabysittingAgency() {}

void BabysittingAgency::start() {
    cout << "Welcome to the Babysitting Agency!" << endl;

    bool exit = false;
    while (!exit) {
        cout
                << "==================================" << endl
                << "1. Manage children" << endl
                << "2. Manage babysitters" << endl
                << "3. Manage reservations" << endl
                << "0. Finish the program" << endl
                << "==================================" << endl;

        char input;
        cin >> input;

        switch (input) {
            // 1. Manage children
            case '1':
                children();
                break;

                // 2. Manage babysitters
            case '2':
                babysitters();
                break;

                // 3. Manage reservations
            case '3':
                reservations();
                break;

                // 0. Finish the program
            case '0':
                exit = true;
                break;

            default:
                cout << "Wrong choice!" << endl;
                break;
        }
    }
}


void BabysittingAgency::children() {
    bool exit = false;
    while (!exit) {
        cout
                << "==================================" << endl
                << "1. Register a new child " << endl
                << "2. Show information about all children" << endl
                << "3. Show information about the specific child" << endl
                << "4. Delete the child's profile" << endl
                << "0. Back to menu" << endl
                << "==================================" << endl;

        char input;
        cin >> input;

        switch (input) {
            // 1. Register a new child
            case '1': {
                string childFirstName, childLastName, childID, isChildAGirlStr;
                int childAge;
                bool isChildAGirl;
                cout << "Enter the child's first name: ";
                cin.ignore();
                getline(cin, childFirstName);
                cout << "Enter the child's last name: ";
                getline(cin, childLastName);
                cout << "Enter the child's Id: ";
                getline(cin, childID);
                cout << "Enter the child's age: ";
                cin >> childAge;
                cout << "Is child a girl (true/false): ";
                cin >> isChildAGirlStr;
                isChildAGirl = (isChildAGirlStr == "true");

                cout << "Enter the address:" << endl;
                string city, street, number;
                cout << "Enter city: ";
                cin.ignore();
                getline(cin, city);
                cout << "Enter street: ";
                getline(cin, street);
                cout << "Enter the apartment number: ";
                getline(cin, number);

                try {
                    cout << childrenManager->registerChild(childFirstName, childLastName, childAge, childID, isChildAGirl,
                                                   city, street, number) << endl;
                } catch (BadArguments &e) {
                    cerr << "Arguments error: " << e.what() << endl;
                } catch (LogicError &e) {
                    cerr << "Logic error: " << e.what() << endl;
                }
                break;
            }

                  // 2. Show information about all children
              case '2': {
                  cout << childrenManager->getAllChildrenReport() << endl;
                  break;
              }

                // 3. Show information about the specific child
              case '3': {
                  specificChildren();
                  break;
              }

                // 4. Delete the child's profile
              case '4': {
                  string childID;
                  cout << "Enter the child's Id: ";
                  cin.ignore();
                  getline(cin, childID);

                  try {
                      cout << childrenManager->deregisterChild(childID) << endl;
                  } catch (BadArguments &e) {
                      cerr << "Arguments error: " << e.what() << endl;
                  } catch (LogicError &e) {
                      cerr << "Logic error: " << e.what() << endl;
                  }
                  break;
              }

                // 0. Back to menu
            case '0':
                exit = true;
                break;

            default:
                cout << "Wrong choice!" << endl;
                break;
        }
    }
}

void BabysittingAgency::babysitters() {
    bool exit = false;
    while (!exit) {
        cout
                << "==================================" << endl
                << "1. Hire a new babysitter" << endl
                << "2. Show information about all babysitters" << endl
                << "3. Show information about the specific babysitter" << endl
                << "4. Delete the babysitter's profile" << endl
                << "0. Back to menu" << endl
                << "==================================" << endl;

        char input;
        cin >> input;

        switch (input) {
            // 1. Hire a new babysitter
            case '1': {
                string babysitterFirstName, babysitterLastName, babysitterID;
                int babysitterAge, maxHoursWeekly, yearsOfExperience;

                cout << "Enter the type of babysitter:" << endl
                     << "1. Newborns babysitter (0-1)" << endl
                     << "2. Toddlers babysitter (1-3)" << endl
                     << "3. Remaining age range babysitter" << endl;
                int type;
                cin >> type;
                if (type == 1)
                {
                    shared_ptr<NewbornsBabysitter> newborns = make_shared<NewbornsBabysitter>();
                    cout << "Enter the babysitter's first name: ";
                    cin.ignore();
                    getline(cin, babysitterFirstName);
                    cout << "Enter the babysitter's last name: ";
                    getline(cin, babysitterLastName);
                    cout << "Enter the babysitter's Id: ";
                    getline(cin, babysitterID);
                    cout << "Enter the babysitter's age: ";
                    cin >> babysitterAge;
                    cout << "Enter the babysitter's years of experience: ";
                    cin >> yearsOfExperience;
                    cout << "Enter the maximum number of hours per week the babysitter can work: ";
                    cin >> maxHoursWeekly;


                    try {
                        cout << babysittersManager->hireBabysitter(babysitterFirstName, babysitterLastName, babysitterID, babysitterAge,
                                                           maxHoursWeekly, newborns, yearsOfExperience) << endl;
                    } catch (BadArguments &e) {
                        cerr << "Arguments error: " << e.what() << endl;
                    } catch (LogicError &e) {
                        cerr << "Logic error: " << e.what() << endl;
                    }
                }
                else if (type == 2)
                {
                    shared_ptr<ToddlersBabysitter> toddlers = make_shared<ToddlersBabysitter>();
                    cout << "Enter the babysitter's first name: ";
                    cin.ignore();
                    getline(cin, babysitterFirstName);
                    cout << "Enter the babysitter's last name: ";
                    getline(cin, babysitterLastName);
                    cout << "Enter the babysitter's Id: ";
                    getline(cin, babysitterID);
                    cout << "Enter the babysitter's age: ";
                    cin >> babysitterAge;
                    cout << "Enter the babysitter's years of experience: ";
                    cin >> yearsOfExperience;
                    cout << "Enter the maximum number of hours per week the babysitter can work: ";
                    cin >> maxHoursWeekly;


                    try {
                        cout << babysittersManager->hireBabysitter(babysitterFirstName, babysitterLastName, babysitterID, babysitterAge,
                                                           maxHoursWeekly, toddlers, yearsOfExperience) << endl;
                    } catch (BadArguments &e) {
                        cerr << "Arguments error: " << e.what() << endl;
                    } catch (LogicError &e) {
                        cerr << "Logic error: " << e.what() << endl;
                    }
                }

                else if (type == 3)
                {
                    shared_ptr<RemainingAgeRangeBabysitter> remaining = make_shared<RemainingAgeRangeBabysitter>();
                    cout << "Enter the babysitter's first name: ";
                    cin.ignore();
                    getline(cin, babysitterFirstName);
                    cout << "Enter the babysitter's last name: ";
                    getline(cin, babysitterLastName);
                    cout << "Enter the babysitter's Id: ";
                    getline(cin, babysitterID);
                    cout << "Enter the babysitter's age: ";
                    cin >> babysitterAge;
                    cout << "Enter the babysitter's years of experience: ";
                    cin >> yearsOfExperience;
                    cout << "Enter the maximum number of hours per week the babysitter can work: ";
                    cin >> maxHoursWeekly;


                    try {
                        cout << babysittersManager->hireBabysitter(babysitterFirstName, babysitterLastName, babysitterID, babysitterAge,
                                                                   maxHoursWeekly, remaining, yearsOfExperience) << endl;
                    } catch (BadArguments &e) {
                        cerr << "Arguments error: " << e.what() << endl;
                    } catch (LogicError &e) {
                        cerr << "Logic error: " << e.what() << endl;
                    }
                }

                break;
            }

                // 2. Show information about all babysitters
            case '2': {
                cout << babysittersManager->reportAllCurrentBabysitters() << endl;
                break;
            }

            case '3': {
                specificBabysitters();
                break;
            }

                // 4. Delete the babysitter's profile
            case '4': {
                string babysitterID;
                cout << "Enter the babysitter's Id: ";
                cin >> babysitterID;

                try {
                    cout << babysittersManager->layBabysitterOff(babysitterID) << endl;
                } catch (BadArguments &e) {
                    cerr << "Arguments error: " << e.what() << endl;
                } catch (LogicError &e) {
                    cerr << "Logic error: " << e.what() << endl;
                }
                break;
            }

                // 0. Back to menu
            case '0':
                exit = true;
                break;

            default:
                cout << "Wrong choice!" << endl;
                break;
        }
    }
}

void BabysittingAgency::reservations() {
    bool exit = false;
    while (!exit) {
        cout << "==================================" << endl
             << "1. Create a reservation" << endl
             << "2. Show all reservations" << endl
             << "3. Show specific reservation" << endl
             << "4. Assign a babysitter" << endl
             << "5. Cancel reservation" << endl
             << "0. Back to menu" << endl
             << "==================================" << endl;

        char input;
        cin >> input;

        switch (input) {
            // 1. Create a reservation
            case '1': {

                string reservationId;

                cout << "Enter reservation's ID: ";
                cin.ignore();
                getline(cin, reservationId);

                string beginTimeString;
                cout << "Enter begin time (YYYY-MM-DD HH:MM:SS): ";
                getline(cin, beginTimeString);
                istringstream beginTimeStream(beginTimeString);

                boost::posix_time::ptime beginTime;
                string format = "%Y-%m-%d %H:%M:%S";
                beginTimeStream.imbue(locale(cout.getloc(), new boost::posix_time::time_input_facet(format)));
                beginTimeStream >> beginTime;

                if (beginTimeStream.fail()) {
                    cerr << "Invalid date or time format. Please enter the date and time in the format: YYYY-MM-DD HH:MM:SS" << endl;
                    break;
                }

                string endTimeString;
                cout << "Enter end time (YYYY-MM-DD HH:MM:SS): ";
                getline(cin, endTimeString);
                istringstream endTimeStream(endTimeString);

                boost::posix_time::ptime endTime;
                endTimeStream.imbue(locale(cout.getloc(), new boost::posix_time::time_input_facet(format)));
                endTimeStream >> endTime;

                if (endTimeStream.fail()) {
                    cerr << "Invalid date or time format. Please enter the date and time in the format: YYYY-MM-DD HH:MM:SS" << endl;
                    break;
                }

                string childID;
                cout << "Choose a child for the reservation by entering the child's ID:" << endl;
                cout << childrenManager->getAllChildrenReportNamesAndIDsOnly() << endl;

                if (childrenManager->getAllChildrenReportNamesAndIDsOnly() == "No available children currently.\n") {
                    break;
                }
                cout << "Enter the child's ID: ";
                getline(cin, childID);

                shared_ptr<Child> child = childrenManager->getChildren().find(FindByID<Child>(childID));
                if (child == nullptr) {
                    cerr << "Child with ID " << childID << " does not exist." << endl;
                    break;
                }

                try {
                    cout << reservationsManager->createReservation(reservationId, beginTime, endTime, child) << endl;
                } catch (BadArguments &e) {
                    cerr << "Arguments error: " << e.what() << endl;
                } catch (LogicError &e) {
                    cerr << "Logic error: " << e.what() << endl;
                }
                break;
                }

                // 2. Show all reservations
                case '2': {
                    cout << reservationsManager->reportAllCurrentReservations() << endl;
                    break;
                }

                case '3': {
                    specificReservations();
                    break;
                }
                // 4. Assign a babysitter
                case '4': {
                    string unassignedReservationsReport = reservationsManager->getUnassignedReservationsReport();
                    cout << unassignedReservationsReport << endl;
                    if (unassignedReservationsReport == "No unassigned reservations found.\n") {
                        break;
                    }

                    cout << "Enter the ID of the reservation you want to assign a babysitter to: ";
                    string reservationID;
                    cin >> reservationID;

                    string reservationReport = reservationsManager->getSpecificReservationReportByID(reservationID);
                    if (reservationReport == "No reservations found.") {
                        cout << "Reservation with ID " << reservationID << " not found.\n";
                        break;
                    }

                    string availableBabysittersReport = babysittersManager->getAvailableBabysittersReport();
                    cout << availableBabysittersReport;

                    if (availableBabysittersReport != "No available babysitters found.\n") {
                        cout << "Please select the babysitter you want to assign to the reservation (by her ID): \n";
                        string babysitterID;
                        cin >> babysitterID;

                        shared_ptr<Babysitter> babysitterToBeAssigned = babysittersManager->getSpecificBabysitterByID(
                                babysitterID);

                        if (babysitterToBeAssigned != nullptr) {
                            cout << reservationsManager->assignBabysitter(babysitterToBeAssigned, reservationID)
                                 << endl;
                        }
                        else {
                            cout << "There is no such babysitter! Try again." << endl;
                        }
                    }
                    break;
                }
                // 5. Cancel reservation
                case '5': {
                    string Id;
                    cout << "Enter the reservation's ID: ";
                    cin.ignore();
                    getline(cin, Id);

                    try {
                        cout << reservationsManager->cancelReservation(Id) << endl;
                    } catch (BadArguments &e) {
                        cerr << "Arguments error: " << e.what() << endl;
                    } catch (LogicError &e) {
                        cerr << "Logic error: " << e.what() << endl;
                    }
                    break;
                }
                // 0. Back to menu
                case '0':
                    exit = true;
                break;

                default:
                    cout << "Wrong choice!" << endl;
                break;
            }
        }
    }

    void BabysittingAgency::specificChildren() {
        bool exit = false;
        while (!exit) {
            cout
                    << "==================================" << endl
                    << "1. Show information about the specific child by the first name " << endl
                    << "2. Show information about the specific child by the Id " << endl
                    << "0. Back to menu" << endl
                    << "==================================" << endl;

            char input;
            cin >> input;

            switch (input) {
                case '1': {
                    string name;
                    cout << "Enter the child's name: ";
                    cin.ignore();
                    getline(cin, name);
                    cout << childrenManager->getSpecificChildReportByFirstName(name) << endl;
                    break;
                }

                case '2': {
                    string id;
                    cout << "Enter the child's Id: ";
                    cin.ignore();
                    getline(cin, id);
                    cout << childrenManager->getSpecificChildReportByID(id) << endl;
                    break;
                }

                    // 0. Back to menu
                case '0':
                    exit = true;
                    break;

                default:
                    cout << "Wrong choice!" << endl;
                    break;
            }
        }
    }

    void BabysittingAgency::specificBabysitters() {
        bool exit = false;
        while (!exit) {
            cout
                    << "==================================" << endl
                    << "1. Show information about the specific babysitter by the first name " << endl
                    << "2. Show information about the specific babysitter by the Id " << endl
                    << "0. Back to menu" << endl
                    << "==================================" << endl;

            char input;
            cin >> input;

            switch (input) {
                case '1': {
                    string name;
                    cout << "Enter the babysitter's first name: ";
                    cin.ignore();
                    getline(cin, name);
                    cout << babysittersManager->getSpecificBabysitterReportByFirstName(name) << endl;
                    break;
                }

                case '2': {
                    string id;
                    cout << "Enter the babysitter's Id: ";
                    cin.ignore();
                    getline(cin, id);
                    cout << babysittersManager->getSpecificBabysitterReportByID(id) << endl;
                    break;
                }

                    // 0. Back to menu
                case '0':
                    exit = true;
                    break;

                default:
                    cout << "Wrong choice!" << endl;
                    break;
            }
        }
    }

void BabysittingAgency::specificReservations() {
    bool exit = false;
    while (!exit) {
        cout
                << "==================================" << endl
                << "1. Show information about the specific reservation by the reservation's id " << endl
                << "2. Show information about the specific reservation by the child's id" << endl
                << "0. Back to menu" << endl
                << "==================================" << endl;

        char input;
        cin >> input;

        switch (input) {
            case '1': {
                string id;
                cout << "Enter the reservation's id: ";
                cin.ignore();
                getline(cin, id);
                cout << reservationsManager->getSpecificReservationReportByID(id) << endl;
                break;
            }

            case '2': {
                string id;
                cout << "Enter the reservation's child id: ";
                cin.ignore();
                getline(cin, id);
                cout << reservationsManager->getSpecificReservationReportByChildID(id) << endl;
                break;
            }

                // 0. Back to menu
            case '0':
                exit = true;
                break;

            default:
                cout << "Wrong choice!" << endl;
                break;
        }
    }
}