#include "Managers/ChildrenManager.h"
#include "Repository/Repository.h"
#include "string"
#include "Model/Child.h"


ChildrenManager::ChildrenManager() {}

std::string ChildrenManager::registerChild(const std::string &childFirstName, const std::string &childLastName, int childAge,
                                    const std::string &childId, const bool &isChildAGirl, const std::string &city,
                                    const std::string &street, const std::string &number) {
    std::shared_ptr<Child> existingChild = children.find(FindByID<Child>(childId));
    if (existingChild != nullptr) {
        throw BadArguments("Child with ID " + childId + " already exists. Please provide a different ID.");
    }

    AddressPtr childAddress = std::make_shared<Address>(city, street, number);
    ChildPtr child = std::make_shared<Child>(childFirstName, childLastName, childAge, childId, isChildAGirl, childAddress);
    children.add(child);
    return "The child  has successfully been registered!";
}

std::string ChildrenManager::deregisterChild(std::string ID) {
    std::shared_ptr<Child> childToDeregister = children.find(FindByID<Child>(ID));
    if (childToDeregister != nullptr) {
        children.remove(childToDeregister);
        return "The child with the given ID: " +ID+ " has been deregistered.";
    }

    else {
        throw BadArguments("An error has occurred, while trying to deregister the child with the"
                           "given ID: " +ID+ ". (Perhaps there is no such child?)");
    }
}

std::string ChildrenManager::getAllChildrenReport() {
    std::string report;
    for (int i = 0; i < children.size(); i++) {
        report += children.get(i)->getChildInfo() + "\n";
    }
    return report;
}

std::string ChildrenManager::getSpecificChildReportByFirstName(std::string firstName) {
    std::vector<std::shared_ptr<Child>> matchingChildren = children.findAll(FindByFirstName<Child>(firstName));
    std::string report;
    if (matchingChildren.empty()) {
        report = "No children found.";
    } else {
        report = "Matching children:\n";
        for (const auto& child : matchingChildren) {
            report += child->getChildInfo() + "\n";
        }
    }

    return report;
}


std::string ChildrenManager::getSpecificChildReportByID(std::string ID) {
    std::vector<std::shared_ptr<Child>> matchingChildren = children.findAll(FindByID<Child>(ID));
    std::string report;
    if (matchingChildren.empty()) {
        report = "No children found.";
    } else {
        report = "Matching children:\n";
        for (const auto& child : matchingChildren) {
            report += child->getChildInfo() + "\n";
        }
    }

    return report;
}

std::string ChildrenManager::getAllChildrenReportNamesAndIDsOnly() {
    std::string report;
    report += "List of available children:\n";

    for (int i = 0; i < children.size(); i++) {
        report += "Child ID: " + children.get(i)->getID() + " | Child Name: " + children.get(i)->getFirstName() + "\n";
    }

    if (report == "List of available children:\n") {
        return "No available children currently.\n";
    }

    return report;
}

Repository<Child> ChildrenManager::getChildren() {
    return children;
}



