#ifndef BAYBYSITTINGAGENCY_REPOSITORY_H
#define BAYBYSITTINGAGENCY_REPOSITORY_H
#include <memory>
#include <string>
#include <vector>
#include "typedefs.h"
#include "Exceptions/BadArguments.h"
#include <functional>
#include <algorithm>

template <class T>
class Repository {
    std::vector<std::shared_ptr<T>> objects;

public:
    void add(std::shared_ptr<T> object);
    void remove(std::shared_ptr<T> object);
    std::string report();
    int size();
    std::shared_ptr<T> get(int ID);
    std::shared_ptr<T> find(std::function<bool(std::shared_ptr<T>&)> predicate);
    std::vector<std::shared_ptr<T>> findAll(std::function<bool(std::shared_ptr<T>&)> predicate);
};

template<class T>
std::shared_ptr<T> Repository<T>::get(int ID) {
    if (ID >= 0 && ID < objects.size()) {
        return objects[ID];
    }
    return nullptr;
}

template<class T>
int Repository<T>::size() {
    return objects.size();
}

template<class T>
std::string Repository<T>::report() {
    std::string report;
    report = "The repository contains of " + std::to_string(objects.size()) + " objects.";
    return report;
}

template<class T>
void Repository<T>::remove(std::shared_ptr<T> object) {
    if (object != nullptr) {
        for (int i = 0; i < objects.size(); i++) {
            if (objects[i] == object) {
                objects.erase(objects.begin() + i);
            }
        }
    }
    else {
        throw BadArguments("An empty pointer was provided!");
    }
}

template<class T>
void Repository<T>::add(std::shared_ptr<T> object) {
    if (object.get() != nullptr) {
        if (std::find(objects.begin(), objects.end(), object) != objects.end()) {
            throw BadArguments("The object is already in the repository!");
        }
        objects.push_back(object);
    }
    else {
        throw BadArguments("Can't add an empty pointer to the repository!");
    }
}

template<class T>
std::shared_ptr<T> Repository<T>::find(const std::function<bool(std::shared_ptr<T>&)> predicate) {
    auto object = std::find_if(objects.begin(), objects.end(), predicate);
    if (object != objects.end()) {
        return *object;
    }

    return nullptr;
}

template<class T>
std::vector<std::shared_ptr<T>> Repository<T>::findAll(const std::function<bool(std::shared_ptr<T>&)> predicate) {
    std::vector<std::shared_ptr<T>> result;
    for (const auto& object : objects) {
        if (predicate(const_cast<std::shared_ptr<T>&>(object))) {
            result.push_back(object);
        }
    }
    return result;
}

template<class T>
struct FindByFirstName {
    std::string firstName;

    FindByFirstName(const std::string& firstName) : firstName(firstName) {}
    bool operator()(const std::shared_ptr<T>& object) const {
        return object->getFirstName() == firstName;
    }
};

template<class T>
struct FindByLastName {
    std::string lastName;

    FindByLastName(std::string& lastName) : lastName(lastName) {}
    bool operator()(const std::shared_ptr<T>& object) {
        return object->getLastName() == lastName;
    }
};

template<class T>
struct FindByID {
    std::string ID;

    FindByID(const std::string& ID) : ID(ID) {}
    bool operator()(const std::shared_ptr<T>& object) {
        return object->getID() == ID;
    }
};

template<class T>
struct FindWithoutBabysitter {
    bool operator()(const std::shared_ptr<T>& object) {
        return object->getBabysitter() == nullptr;
    }
};

template<class T>
struct FindAvailableBabysitters {
    bool operator()(const std::shared_ptr<T>& object) {
        return object->isBabysitterAvailable();
    }
};

#endif //BAYBYSITTINGAGENCY_REPOSITORY_H
