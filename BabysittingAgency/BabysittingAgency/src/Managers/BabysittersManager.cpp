#include "Managers/BabysittersManager.h"
#include "string"
#include "typedefs.h"
#include "Model/Babysitter.h"

BabysittersManager::BabysittersManager() {}

std::string BabysittersManager::hireBabysitter(const std::string &babysitterFirstName, const std::string &babysitterLastName, const std::string &babysitterId,
                                        int babysitterAge, int maxHoursWeekly, BabysitterTypePtr babysitterType, int yearsOfExperience) {
    std::shared_ptr<Babysitter> existingBabysitter = babysitters.find(FindByID<Babysitter>(babysitterId));
    if (existingBabysitter != nullptr) {
        throw BadArguments("Babysitter with ID " + babysitterId + " already exists. Please provide a different ID.");
    }

    BabysitterPtr babysitter = std::make_shared<Babysitter>(babysitterFirstName, babysitterLastName, babysitterId,
                                                            babysitterAge, maxHoursWeekly, babysitterType, yearsOfExperience);
    babysitters.add(babysitter);
    return "The babysitter has successfully been hired!";
}

std::string BabysittersManager::layBabysitterOff(std::string ID) {
    std::shared_ptr<Babysitter> babysitterToLayOff = babysitters.find(FindByID<Babysitter>(ID));
    if (babysitterToLayOff != nullptr) {
        if (babysitterToLayOff->getBabysitterReservedHours() > 0) {
            throw BadArguments("Cannot lay the babysitter off, because she has active reservations. Please cancel all reservations first.");
        }
        babysitters.remove(babysitterToLayOff);
        return "The babysitter with the given ID: " +ID+ " has been laid off.";
    }
    else {
        throw BadArguments("An error has occurred, while trying to lay the babysitter with the"
                           "given ID: " +ID+ " off. (Perhaps there is no such babysitter?)");
    }
}

std::string BabysittersManager::reportAllCurrentBabysitters() {
    std::string report;
    for (int i = 0; i < babysitters.size(); i++) {
        report += babysitters.get(i)->getBabysitterInfo() + "\n";
    }
    return report;
}

std::string BabysittersManager::getSpecificBabysitterReportByFirstName(std::string firstName) {
    std::vector<std::shared_ptr<Babysitter>> matchingBabysitters = babysitters.findAll(FindByFirstName<Babysitter>(firstName));
    std::string report;
    if (matchingBabysitters.empty()) {
        report = "No babysitter found.";
    } else {
        report = "Matching babysitters:\n";
        for (const auto& babysitter : matchingBabysitters) {
            report += babysitter->getBabysitterInfo() + "\n";
        }
    }

    return report;
}

std::string  BabysittersManager::getSpecificBabysitterReportByID(std::string ID) {
    std::vector<std::shared_ptr<Babysitter>> matchingBabysitters = babysitters.findAll(FindByID<Babysitter>(ID));
    std::string report;
    if (matchingBabysitters.empty()) {
        report = "No babysitters found.";
    } else {
        report = "Matching babysitters:\n";
        for (const auto &babysitter: matchingBabysitters) {
            report += babysitter->getBabysitterInfo() + "\n";
        }
    }

    return report;
}

std::string BabysittersManager::getAvailableBabysittersReport() {
    std::vector<std::shared_ptr<Babysitter>> availableBabysitters = babysitters.findAll(FindAvailableBabysitters<Babysitter>());

    std::string report;
    if (availableBabysitters.empty()) {
        report = "No available babysitters found.\n";
    } else {
        report = "Available babysitters:\n";
        for (const auto& babysitter : availableBabysitters) {
            report += babysitter->getBabysitterInfo() + "\n\n";
        }
    }

    return report;
}

std::shared_ptr<Babysitter> BabysittersManager::getSpecificBabysitterByID(std::string ID) {
    std::shared_ptr<Babysitter> matchingBabysitter = babysitters.find(FindByID<Babysitter>(ID));
    if (matchingBabysitter == nullptr) {
        return nullptr;
    } else {
        return matchingBabysitter;
    }
}